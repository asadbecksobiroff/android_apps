package com.example.speedometr.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Calculator-style monospace font for numbers
val CalculatorFont = FontFamily.Monospace

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = CalculatorFont,
        fontWeight = FontWeight.Bold,
        fontSize = 96.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = CalculatorFont,
        fontWeight = FontWeight.Bold,
        fontSize = 72.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = CalculatorFont,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)