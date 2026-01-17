package com.halt

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsManager: SettingsManager
    private lateinit var switchStrictMode: Switch
    private lateinit var btnPause15: Button
    private lateinit var btnCancelPause: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsManager = SettingsManager(this)
        
        switchStrictMode = findViewById(R.id.switchStrictMode)
        btnPause15 = findViewById(R.id.btnPause15)
        btnCancelPause = findViewById(R.id.btnCancelPause)

        setupUI()
    }

    private fun setupUI() {
        // Strict Mode
        switchStrictMode.isChecked = settingsManager.isStrictModeEnabled()
        switchStrictMode.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.setStrictMode(isChecked)
            updatePauseButtons() // Strict mode might affect ability to pause
        }

        // Pause Logic
        updatePauseButtons()

        btnPause15.setOnClickListener {
            if (settingsManager.isStrictModeEnabled()) {
                Toast.makeText(this, "Strict Mode enabled. Cannot pause.", Toast.LENGTH_SHORT).show()
            } else {
                settingsManager.pauseBlocking(15 * 60 * 1000)
                updatePauseButtons()
                Toast.makeText(this, "Paused for 15 minutes", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelPause.setOnClickListener {
            settingsManager.cancelPause()
            updatePauseButtons()
            Toast.makeText(this, "Blocking resumed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePauseButtons() {
        if (settingsManager.isPaused()) {
            btnPause15.visibility = android.view.View.GONE
            btnCancelPause.visibility = android.view.View.VISIBLE
        } else {
            btnPause15.visibility = android.view.View.VISIBLE
            btnCancelPause.visibility = android.view.View.GONE
        }
    }
}
