package com.example.aiphoneassistant

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class AssistantAccessibilityService :
    AccessibilityService() {

    companion object {

        var instance:
            AssistantAccessibilityService? = null
            private set

        fun speakAndListen() {
            // Accessibility service availability
            // is checked through the instance.
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        instance = this
    }

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {
        // The service is intentionally passive.
        // It can later be extended for user-requested
        // screen interaction.
    }

    override fun onInterrupt() {
    }

    override fun onDestroy() {

        if (instance === this) {
            instance = null
        }

        super.onDestroy()
    }
    }
