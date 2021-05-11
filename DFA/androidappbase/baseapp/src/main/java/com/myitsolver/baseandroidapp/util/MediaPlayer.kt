package com.myitsolver.baseandroidapp.util

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri

class MediaPlayerHelper {

    private var isPrepared = false
    val mediaPlayer: MediaPlayer = MediaPlayer()

    fun loadDataFromUrl(url: String?, context: Context?): Boolean {
        try {
            val myUri = Uri.parse(url)
            if (myUri != null && context != null) {
                mediaPlayer.setDataSource(context, myUri)
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mediaPlayer.prepare() //don't use prepareAsync for mp3 playback
                isPrepared = true
                return true
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


    fun pause() {
        try {
            if (isPrepared) {
                try {
                    mediaPlayer.pause()
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        } catch (_: java.lang.Exception) {

        }
    }

    fun stop() {
        try {
            if (isPrepared) {
                try {
                    mediaPlayer.stop()
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        } catch (_: java.lang.Exception) {

        }
    }

    fun start() {
        try {
            if (isPrepared) {
                try {
                    mediaPlayer.start()
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        } catch (_: java.lang.Exception) {

        }
    }

    fun seekTo(timeInSec: Int) {
        try {
            if (isPrepared) {
                if (mediaPlayer.duration != 0) {
                    try {
                        mediaPlayer.seekTo(timeInSec * 1000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (_: java.lang.Exception) {

        }
    }

    fun isPlaying(): Boolean {
        return isPrepared and try {
            mediaPlayer.isPlaying
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}