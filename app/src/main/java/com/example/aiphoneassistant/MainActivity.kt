package com.example.aiphoneassistant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            statusText.text = "Permissions updated"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            requestPermissionsIfNeeded()

            val intent = Intent(this, VoiceAssistantService::class.java)

            ContextCompat.startForegroundService(this, intent)

            statusText.text = "Assistant running"
        }

        findViewById<Button>(R.id.stopButton).setOnClickListener {
            stopService(
                Intent(this, VoiceAssistantService::class.java)
            )

            statusText.text = "Assistant stopped"
        }

        findViewById<Button>(R.id.accessibilityButton).setOnClickListener {
            startActivity(
                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            )
        }

        findViewById<Button>(R.id.testButton).setOnClickListener {
            AssistantAccessibilityService.speakAndListen()
        }
    }

    private fun requestPermissionsIfNeeded() {

        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.POST_NOTIFICATIONS
        )

        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isNotEmpty()) {
            permissionLauncher.launch(
                missing.toTypedArray()
            )
        }
    }
}
