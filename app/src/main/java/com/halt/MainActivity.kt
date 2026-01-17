package com.halt

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var btnPermission: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        btnPermission = findViewById(R.id.btnPermission)

        updateStatus()

        btnPermission.setOnClickListener {
            if (!isAccessibilityServiceEnabled()) {
                openAccessibilitySettings()
            } else if (!Settings.canDrawOverlays(this)) {
                openOverlaySettings()
            }
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
            tvStatus.text = getString(R.string.service_active)
            tvStatus.setTextColor(getColor(R.color.black)) // Or green
            btnPermission.isEnabled = false
            btnPermission.text = getString(R.string.service_active)
        } else {
            tvStatus.text = getString(R.string.service_inactive)
            tvStatus.setTextColor(getColor(R.color.accent_red))
            
            if (!isServiceEnabled) {
                btnPermission.text = getString(R.string.grant_permission)
            } else {
                btnPermission.text = getString(R.string.overlay_permission_required)
            }
            btnPermission.isEnabled = true
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = am.getEnabledAccessibilityServiceList(android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (service in enabledServices) {
            if (service.id.contains(packageName)) {
                return true
            }
        }
        return false
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    private fun openOverlaySettings() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivity(intent)
    }
}
