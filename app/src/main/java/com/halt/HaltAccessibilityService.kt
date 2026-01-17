package com.halt

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class HaltAccessibilityService : AccessibilityService() {

    private val TAG = "HaltAccessibilityService"
    private val INSTAGRAM_PACKAGE = "com.instagram.android"

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        
        // We only care about Instagram
        if (event.packageName?.toString() != INSTAGRAM_PACKAGE) return

        val rootNode = rootInActiveWindow ?: return

        // Check for Reels
        if (isReelsVisible(rootNode)) {
             // Check if it's a DM Reel (Allowed)
             if (!isOpenedFromDM(rootNode, event)) {
                 blockScreen("Reels Blocked")
             }
        }
        
        // Check for Explore
        if (isExploreVisible(rootNode)) {
            blockScreen("Explore Blocked")
        }
    }

    private fun isReelsVisible(root: AccessibilityNodeInfo): Boolean {
        // Naive check: Look for "Reels" text.
        // In reality, we might need more specific ID checks or hierarchy checks.
        val reelsNodes = root.findAccessibilityNodeInfosByText("Reels")
        return reelsNodes.isNotEmpty()
    }
    
    private fun isExploreVisible(root: AccessibilityNodeInfo): Boolean {
        // Naive check: Look for "Search" text in a specific context or "Explore"
        val searchNodes = root.findAccessibilityNodeInfosByText("Search")
        val exploreNodes = root.findAccessibilityNodeInfosByText("Explore")
        return searchNodes.isNotEmpty() || exploreNodes.isNotEmpty()
    }

    private fun isOpenedFromDM(root: AccessibilityNodeInfo, event: AccessibilityEvent): Boolean {
        // Check for "sent you" or chat indicators. 
        // This is a heuristic and might need refinement.
        val indicatorNodes = root.findAccessibilityNodeInfosByText("sent you")
        if (indicatorNodes.isNotEmpty()) return true
        
        // Also check event text if available
        val eventText = event.text?.joinToString() ?: ""
        if (eventText.contains("sent you", ignoreCase = true)) return true

        return false
    }

    private fun blockScreen(reason: String) {
        Log.d(TAG, "Blocking screen: $reason")
        val intent = Intent(this, BlockActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("REASON", reason)
        }
        startActivity(intent)
    }

    override fun onInterrupt() {
        Log.d(TAG, "Service Interrupted")
    }
}
