package com.halt

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class HaltAccessibilityService : AccessibilityService() {

    private val TAG = "HaltAccessibilityService"
    private val INSTAGRAM_PACKAGE = "com.instagram.android"
    private val CHROME_PACKAGE = "com.android.chrome"
    private val SAMSUNG_BROWSER_PACKAGE = "com.sec.android.app.sbrowser"
    private val FIREFOX_PACKAGE = "org.mozilla.firefox"
    
    private val SUPPORTED_PACKAGES = setOf(
        INSTAGRAM_PACKAGE, 
        CHROME_PACKAGE, 
        SAMSUNG_BROWSER_PACKAGE, 
        FIREFOX_PACKAGE
    )
    
    private val screenDetector = ScreenDetector()
    private lateinit var settingsManager: SettingsManager

    override fun onServiceConnected() {
        super.onServiceConnected()
        settingsManager = SettingsManager(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val packageName = event.packageName?.toString() ?: return
        
        if (packageName !in SUPPORTED_PACKAGES) return
        
        // 0. Check Settings (Pause)
        if (!::settingsManager.isInitialized) settingsManager = SettingsManager(this)
        if (settingsManager.isPaused()) {
            return
        }

        val rootNode = rootInActiveWindow ?: return

        // APP SPECIFIC CHECKS
        if (packageName == INSTAGRAM_PACKAGE) {
             // 1. Check Exceptions (DMs)
            if (screenDetector.isAllowedContext(rootNode, event)) {
                Log.d(TAG, "Allowed Context (DM/Message)")
                return
            }
    
            // 2. Check Reels
            if (screenDetector.isReels(rootNode, event)) {
                 blockScreen("Reels Blocked")
                 return
            }
            
            // 3. Check Explore
            if (screenDetector.isExplore(rootNode, event)) {
                blockScreen("Explore Blocked")
                return
            }
        } else {
            // BROWSER CHECKS
            val browserBlockReason = screenDetector.isBrowserReelOrShort(rootNode)
            if (browserBlockReason != null) {
                blockScreen(browserBlockReason)
                return
            }
        }
    }

    private fun blockScreen(reason: String) {
        Log.w(TAG, "BLOCKING: $reason")
        
        // 1. Overlay Strategy (Primary)
        val intent = Intent(this, BlockActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("REASON", reason)
        }
        startActivity(intent)
        
        // 2. Global Action Strategy (Force Back) - Optional reinforcement
        // performGlobalAction(GLOBAL_ACTION_BACK)
    }

    override fun onInterrupt() {
        Log.d(TAG, "Service Interrupted")
    }
}
