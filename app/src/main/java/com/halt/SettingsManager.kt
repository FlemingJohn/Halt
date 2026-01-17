package com.halt

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {

    private val PREFS_NAME = "halt_prefs"
    private val KEY_STRICT_MODE = "strict_mode"
    private val KEY_PAUSE_UNTIL = "pause_until"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setStrictMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_STRICT_MODE, enabled).apply()
    }

    fun isStrictModeEnabled(): Boolean {
        return prefs.getBoolean(KEY_STRICT_MODE, false)
    }

    fun pauseBlocking(durationMs: Long) {
        val resumeTime = System.currentTimeMillis() + durationMs
        prefs.edit().putLong(KEY_PAUSE_UNTIL, resumeTime).apply()
    }

    fun isPaused(): Boolean {
        val pauseUntil = prefs.getLong(KEY_PAUSE_UNTIL, 0)
        return System.currentTimeMillis() < pauseUntil
    }

    fun cancelPause() {
        prefs.edit().remove(KEY_PAUSE_UNTIL).apply()
    }
}
