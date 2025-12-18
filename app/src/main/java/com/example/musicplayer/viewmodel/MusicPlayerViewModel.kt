package com.example.musicplayer.viewmodel

import android.content.ContentUris
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicPlayerViewModel : ViewModel() {
    var songs by mutableStateOf<List<Song>>(emptyList())
        private set
    
    var currentSongIndex by mutableIntStateOf(-1)
        private set
    
    var isPlaying by mutableStateOf(false)
        private set
    
    var currentPosition by mutableLongStateOf(0L)
        private set
    
    var duration by mutableLongStateOf(0L)
        private set
    
    private var mediaPlayer: MediaPlayer? = null
    
    val currentSong: Song?
        get() = if (currentSongIndex >= 0 && currentSongIndex < songs.size) {
            songs[currentSongIndex]
        } else {
            null
        }
    
    fun loadSongs(context: Context) {
        viewModelScope.launch {
            songs = withContext(Dispatchers.IO) {
                scanForMusic(context)
            }
        }
    }
    
    private fun scanForMusic(context: Context): List<Song> {
        val songsList = mutableListOf<Song>()
        
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )
        
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        
        context.contentResolver.query(
            collection,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn) ?: "Noma'lum"
                val artist = cursor.getString(artistColumn) ?: "Noma'lum artist"
                val duration = cursor.getLong(durationColumn)
                val path = cursor.getString(pathColumn)
                
                if (duration > 0) {
                    songsList.add(
                        Song(
                            id = id,
                            title = title,
                            artist = artist,
                            duration = duration,
                            path = path
                        )
                    )
                }
            }
        }
        
        return songsList
    }
    
    fun playSong(context: Context, index: Int) {
        if (index < 0 || index >= songs.size) return
        
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    mediaPlayer?.release()
                    
                    val song = songs[index]
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        song.id
                    )
                    
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(context, uri)
                        prepare()
                        setOnCompletionListener {
                            this@MusicPlayerViewModel.isPlaying = false
                            this@MusicPlayerViewModel.currentPosition = 0L
                        }
                    }
                    
                    currentSongIndex = index
                    this@MusicPlayerViewModel.duration = mediaPlayer?.duration?.toLong() ?: 0L
                    currentPosition = 0L
                    isPlaying = true
                    mediaPlayer?.start()
                    
                    startPositionUpdates()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    fun togglePlayPause() {
        mediaPlayer?.let { player ->
            if (isPlaying) {
                player.pause()
                isPlaying = false
            } else {
                player.start()
                isPlaying = true
                startPositionUpdates()
            }
        }
    }
    
    fun seekTo(position: Long) {
        mediaPlayer?.seekTo(position.toInt())
        currentPosition = position
    }
    
    fun playNext(context: Context) {
        if (songs.isEmpty()) return
        val nextIndex = (currentSongIndex + 1) % songs.size
        playSong(context, nextIndex)
    }
    
    fun playPrevious(context: Context) {
        if (songs.isEmpty()) return
        val prevIndex = if (currentSongIndex <= 0) songs.size - 1 else currentSongIndex - 1
        playSong(context, prevIndex)
    }
    
    private fun startPositionUpdates() {
        viewModelScope.launch {
            while (true) {
                val player = mediaPlayer
                val playing = isPlaying
                if (player != null && playing && player.isPlaying) {
                    currentPosition = player.currentPosition.toLong()
                    kotlinx.coroutines.delay(100)
                } else {
                    break
                }
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

