package com.example.aiphoneassistant

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TtsManager(
    context: Context
) {

    private var tts: TextToSpeech? = null

    init {

        tts = TextToSpeech(
            context
        ) { status ->

            if (status == TextToSpeech.SUCCESS) {

                tts?.language =
                    Locale("hi", "IN")
            }
        }
    }

    fun speak(text: String) {

        tts?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "assistant_response"
        )
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
