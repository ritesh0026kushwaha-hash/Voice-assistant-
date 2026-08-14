package com.example.aiphoneassistant

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.provider.Settings
import android.telephony.SmsManager
import androidx.core.content.ContextCompat

class CommandProcessor(
    private val context: Context,
    private val tts: TtsManager
) {

    fun process(command: String) {

        val text = command
            .trim()
            .lowercase()

        when {

            text.contains("होम") ||
            text.contains("home") -> {

                pressHome()

                tts.speak("होम स्क्रीन खोल रहा हूँ")
            }

            text.contains("सेटिंग") ||
            text.contains("settings") -> {

                openSettings()

                tts.speak("सेटिंग्स खोल रहा हूँ")
            }

            text.contains("वॉल्यूम बढ़ा") ||
            text.contains("volume up") -> {

                changeVolume(true)

                tts.speak("वॉल्यूम बढ़ा दिया")
            }

            text.contains("वॉल्यूम कम") ||
            text.contains("volume down") -> {

                changeVolume(false)

                tts.speak("वॉल्यूम कम कर दिया")
            }

            text.contains("वाईफाई") ||
            text.contains("wifi") -> {

                openWifiSettings()

                tts.speak("वाई-फाई सेटिंग खोल रहा हूँ")
            }

            text.contains("ब्लूटूथ") ||
            text.contains("bluetooth") -> {

                openBluetoothSettings()

                tts.speak("ब्लूटूथ सेटिंग खोल रहा हूँ")
            }

            text.startsWith("कॉल ") ||
            text.startsWith("call ") -> {

                val number =
                    text.substringAfter(" ")
                        .trim()

                if (number.isNotEmpty()) {
                    call(number)
                } else {
                    tts.speak("किसे कॉल करना है?")
                }
            }

            text.startsWith("मैसेज ") ||
            text.startsWith("message ") -> {

                tts.speak(
                    "मैसेज भेजने के लिए नंबर और संदेश दोनों बताना होगा"
                )
            }

            text.contains("youtube") ||
            text.contains("यूट्यूब") -> {

                openApp(
                    "com.google.android.youtube"
                )

                tts.speak("यूट्यूब खोल रहा हूँ")
            }

            text.contains("whatsapp") ||
            text.contains("व्हाट्सएप") -> {

                openApp(
                    "com.whatsapp"
                )

                tts.speak("व्हाट्सऐप खोल रहा हूँ")
            }

            text.contains("chrome") ||
            text.contains("ब्राउज़र") -> {

                openApp(
                    "com.android.chrome"
                )

                tts.speak("ब्राउज़र खोल रहा हूँ")
            }

            text.contains("असिस्टेंट बंद") ||
            text.contains("assistant stop") -> {

                tts.speak("असिस्टेंट बंद कर रहा हूँ")

                if (context is VoiceAssistantService) {
                    context.stopSelf()
                }
            }

            else -> {

                tts.speak(
                    "मैंने सुना: $command"
                )
            }
        }
    }

    private fun pressHome() {

        AssistantAccessibilityService
            .instance
            ?.performGlobalAction(
                android.accessibilityservice.AccessibilityService
                    .GLOBAL_ACTION_HOME
            )
    }

    private fun openSettings() {

        val intent = Intent(
            Settings.ACTION_SETTINGS
        )

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )

        context.startActivity(intent)
    }

    private fun openWifiSettings() {

        val intent = Intent(
            Settings.ACTION_WIFI_SETTINGS
        )

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )

        context.startActivity(intent)
    }

    private fun openBluetoothSettings() {

        val intent = Intent(
            Settings.ACTION_BLUETOOTH_SETTINGS
        )

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )

        context.startActivity(intent)
    }

    private fun changeVolume(up: Boolean) {

        val audio =
            context.getSystemService(
                Context.AUDIO_SERVICE
            ) as AudioManager

        audio.adjustVolume(
            if (up)
                AudioManager.ADJUST_RAISE
            else
                AudioManager.ADJUST_LOWER,
            AudioManager.FLAG_SHOW_UI
        )
    }

    private fun call(number: String) {

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            tts.speak(
                "कॉल की permission नहीं मिली"
            )

            return
        }

        val intent = Intent(
            Intent.ACTION_CALL,
            Uri.parse("tel:$number")
        )

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )

        context.startActivity(intent)

        tts.speak("कॉल कर रहा हूँ")
    }

    private fun openApp(packageName: String) {

        val manager =
            context.packageManager

        val intent =
            manager.getLaunchIntentForPackage(
                packageName
            )

        if (intent == null) {

            tts.speak(
                "यह ऐप फोन में मौजूद नहीं है"
            )

            return
        }

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )

        context.startActivity(intent)
    }
}
