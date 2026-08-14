package com.example.aiphoneassistant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class SpeechManager(
    private val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit
) {

    private var recognizer: SpeechRecognizer? = null

    fun startListening() {

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onError("Voice recognition available नहीं है")
            return
        }

        recognizer?.destroy()

        recognizer = SpeechRecognizer
            .createSpeechRecognizer(context)

        recognizer?.setRecognitionListener(
            object : RecognitionListener {

                override fun onReadyForSpeech(
                    params: Bundle?
                ) {}

                override fun onBeginningOfSpeech() {}

                override fun onRmsChanged(
                    rmsdB: Float
                ) {}

                override fun onBufferReceived(
                    buffer: ByteArray?
                ) {}

                override fun onEndOfSpeech() {}

                override fun onError(
                    error: Int
                ) {
                    restart()
                }

                override fun onResults(
                    results: Bundle?
                ) {

                    val matches =
                        results?.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                        )

                    val text = matches
                        ?.firstOrNull()
                        ?.trim()

                    if (!text.isNullOrEmpty()) {
                        onResult(text)
                    }

                    restart()
                }

                override fun onPartialResults(
                    partialResults: Bundle?
                ) {}

                override fun onEvent(
                    eventType: Int,
                    params: Bundle?
                ) {}
            }
        )

        val intent = Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        ).apply {

            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "hi-IN"
            )

            putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                false
            )
        }

        recognizer?.startListening(intent)
    }

    private fun restart() {

        android.os.Handler(
            android.os.Looper.getMainLooper()
        ).postDelayed(
            {
                startListening()
            },
            1000
        )
    }

    fun destroy() {
        recognizer?.destroy()
        recognizer = null
    }
}
