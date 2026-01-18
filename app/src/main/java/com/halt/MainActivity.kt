package com.halt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvStatusSubtitle: TextView
    private lateinit var btnPermission: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        tvStatusSubtitle = findViewById(R.id.tvStatusSubtitle)
        btnPermission = findViewById(R.id.btnPermission)

        updateStatus()

        btnPermission.setOnClickListener {
            if (!isAccessibilityServiceEnabled()) {
                openAccessibilitySettings()
            } else if (!Settings.canDrawOverlays(this)) {
                openOverlaySettings()
            }
        }

        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    private fun updateStatus() {
        val isServiceEnabled = isAccessibilityServiceEnabled()
        val isOverlayGranted = Settings.canDrawOverlays(this)

        if (isServiceEnabled && isOverlayGranted) {
            tvStatus.text = "System Active"
            tvStatus.setTextColor(getColor(R.color.emerald_600))
            tvStatusSubtitle.text = "Halt is protecting your time."
            btnPermission.visibility = android.view.View.GONE
        } else {
            tvStatus.setTextColor(getColor(R.color.slate_900))
            btnPermission.visibility = android.view.View.VISIBLE

            if (!isServiceEnabled) {
                tvStatus.text = "Permission Required"
                tvStatusSubtitle.text = "Accessibility service is disabled."
                btnPermission.text = "Enable Accessibility"
            } else if (!isOverlayGranted) {
                tvStatus.text = "Overlay Required"
                tvStatusSubtitle.text = "Permission to draw over apps is missing."
                btnPermission.text = "Grant Overlay Permission"
            }
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val expectedComponentName =
                android.content.ComponentName(this, HaltAccessibilityService::class.java)
        val enabledServicesSetting =
                Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
                )
                        ?: return false

        val colonSplitter = android.text.TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServicesSetting)

        while (colonSplitter.hasNext()) {
            val componentNameString = colonSplitter.next()
            val enabledService =
                    android.content.ComponentName.unflattenFromString(componentNameString)
            if (enabledService != null && enabledService == expectedComponentName) {
                val accessibilityEnabled =
                        Settings.Secure.getInt(
                                contentResolver,
                                Settings.Secure.ACCESSIBILITY_ENABLED,
                                0
                        )
                return accessibilityEnabled == 1
            }
        }
        return false
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    private fun openOverlaySettings() {
        val intent =
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivity(intent)
    }
}
