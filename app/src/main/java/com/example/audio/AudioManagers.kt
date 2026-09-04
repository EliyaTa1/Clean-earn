package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.ToneGenerator
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

class SoundEffectsManager(private val context: Context) {
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 90)
        } catch (e: Exception) {
            Log.e("SoundEffectsManager", "ToneGenerator init failed", e)
        }
    }

    /**
     * Play cheerful melodic coin chime
     */
    fun playCoinEarnedSound(scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            try {
                playToneChord(listOf(523.25, 659.25, 783.99, 1046.50), durationMs = 70)
            } catch (e: Exception) {
                // Fallback to ToneGenerator
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
            }
        }
    }

    /**
     * Play celebratory fanfare (e.g. daily cap reached or big chore completed)
     */
    fun playCelebrationFanfare(scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            try {
                val notes = listOf(
                    523.25 to 100, // C5
                    659.25 to 100, // E5
                    783.99 to 100, // G5
                    1046.50 to 250 // C6
                )
                for ((freq, dur) in notes) {
                    playTone(freq, dur)
                    delay(30)
                }
            } catch (e: Exception) {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 250)
            }
        }
    }

    /**
     * Play button hold feedback sound (while pressing to start screen time)
     */
    fun playHoldProgressBeep(pitchStep: Int) {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_DTMF_1 + (pitchStep % 9), 50)
        } catch (_: Exception) {}
    }

    /**
     * Play launch countdown sound
     */
    fun playLaunchSuccess(scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            playTone(440.0, 100)
            delay(50)
            playTone(880.0, 180)
        }
    }

    /**
     * Play sleep / time up gentle chime (Zzz gentle descending lullaby)
     */
    fun playTimeUpLullaby(scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            try {
                val notes = listOf(
                    880.0 to 180,
                    659.25 to 220,
                    523.25 to 260,
                    392.00 to 450
                )
                for ((freq, dur) in notes) {
                    playTone(freq, dur)
                    delay(60)
                }
            } catch (e: Exception) {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 300)
            }
        }
    }

    /**
     * Play soft countdown warning tick (when 1 minute remains)
     */
    fun playWarningTick() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
        } catch (_: Exception) {}
    }

    private fun playTone(freqHz: Double, durationMs: Int) {
        try {
            val sampleRate = 44100
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            val generatedSnd = ByteArray(2 * numSamples)

            for (i in 0 until numSamples) {
                val time = i.toDouble() / sampleRate
                // Sine wave with soft attack & decay envelope
                val envelope = when {
                    i < sampleRate * 0.02 -> i / (sampleRate * 0.02)
                    i > numSamples - (sampleRate * 0.03) -> (numSamples - i) / (sampleRate * 0.03)
                    else -> 1.0
                }
                val sampleVal = (sin(2.0 * Math.PI * freqHz * time) * 32767 * envelope * 0.7).toInt().toShort()
                generatedSnd[2 * i] = (sampleVal.toInt() and 0x00ff).toByte()
                generatedSnd[2 * i + 1] = ((sampleVal.toInt() and 0xff00) ushr 8).toByte()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(generatedSnd.size)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(generatedSnd, 0, generatedSnd.size)
            audioTrack.play()
            Thread.sleep(durationMs.toLong())
            audioTrack.release()
        } catch (_: Exception) {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, durationMs)
        }
    }

    private fun playToneChord(frequencies: List<Double>, durationMs: Int) {
        for (freq in frequencies) {
            playTone(freq, durationMs)
        }
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}

class SpeechManager(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var isHebrewSupported = false

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("SpeechManager", "TTS initialization error", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val candidateLocales = listOf(
                Locale("he", "IL"),
                Locale("iw", "IL"),
                Locale("he"),
                Locale("iw")
            )
            var matched = false
            for (loc in candidateLocales) {
                val res = tts?.isLanguageAvailable(loc)
                if (res == TextToSpeech.LANG_AVAILABLE || res == TextToSpeech.LANG_COUNTRY_AVAILABLE || res == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE) {
                    tts?.language = loc
                    matched = true
                    break
                }
            }
            if (!matched) {
                // Fallback attempt
                try {
                    tts?.language = Locale("he", "IL")
                } catch (_: Exception) {
                    tts?.language = Locale.getDefault()
                }
            }
            tts?.setSpeechRate(0.95f)
            tts?.setPitch(1.15f) // Friendly cheerful voice pitch for children
            isInitialized = true
        }
    }

    fun speakHebrew(text: String) {
        if (isInitialized && tts != null && text.isNotBlank()) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tidy_speak_${System.currentTimeMillis()}")
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
