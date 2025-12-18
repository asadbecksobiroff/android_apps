package com.example.speedometr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.speedometr.ui.theme.SpeedometrTheme
import com.google.android.gms.location.*
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private var speedUpdateCallback: ((Float) -> Unit)? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            speedUpdateCallback?.let { startLocationUpdates(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        setContent {
            SpeedometrTheme {
                var speed by remember { mutableStateOf(0f) }
                
                LaunchedEffect(Unit) {
                    speedUpdateCallback = { newSpeed ->
                        speed = newSpeed
                    }
                    checkPermissionsAndStartUpdates { newSpeed ->
                        speed = newSpeed
                    }
                }
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SpeedometerScreen(
                        speed = speed,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun checkPermissionsAndStartUpdates(onSpeedUpdate: (Float) -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                startLocationUpdates(onSpeedUpdate)
            }
            else -> {
                speedUpdateCallback = onSpeedUpdate
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun startLocationUpdates(onSpeedUpdate: (Float) -> Unit) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L // Update interval: 1 second
        ).apply {
            setMinUpdateIntervalMillis(500L)
            setMaxUpdateDelayMillis(2000L)
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val speedInMps = location.speed // Speed in meters per second
                    val speedInKmh = speedInMps * 3.6f // Convert to km/h
                    onSpeedUpdate(speedInKmh)
                }
            }
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )
        }
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::fusedLocationClient.isInitialized && locationCallback != null && speedUpdateCallback != null) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                speedUpdateCallback?.let { startLocationUpdates(it) }
            }
        }
    }
}

@Composable
fun SpeedometerScreen(speed: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Speed display
            Text(
                text = "${speed.roundToInt()}",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Unit label
            Text(
                text = "km/h",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}