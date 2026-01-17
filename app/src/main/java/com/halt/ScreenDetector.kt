package com.halt

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Responsible for analyzing the screen content (AccessibilityNodeInfo)
 * to determine if the user is in a blocked section (Reels/Explore).
 */
class ScreenDetector {

    fun isReels(root: AccessibilityNodeInfo, event: AccessibilityEvent?): Boolean {
        // 1. Check for "Reels" text (Bottom Tab or Top Header)
        val reelsText = root.findAccessibilityNodeInfosByText("Reels")
        if (reelsText.isNotEmpty()) return true

        // 2. Fallback: Check for specific description/ID if known (omitted for generic approach)
        
        return false
    }

    fun isExplore(root: AccessibilityNodeInfo, event: AccessibilityEvent?): Boolean {
        // 1. "Search" bar usually present at top
        val searchNodes = root.findAccessibilityNodeInfosByText("Search")
        if (searchNodes.isNotEmpty()) {
            // Refinement: If we are scrolling in a Search view, it's likely Explore feed
            if (event?.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
                return true
            }
            // Even without scroll, if we see "Explore" text or grid structure
            if (root.findAccessibilityNodeInfosByText("Explore").isNotEmpty()) {
                return true
            }
        }
        return false
    }

    fun isAllowedContext(root: AccessibilityNodeInfo, event: AccessibilityEvent?): Boolean {
        // 1. Check for "sent you" (DM indication)
        if (root.findAccessibilityNodeInfosByText("sent you").isNotEmpty()) return true
        
        // 2. Check event text for notification/toast based entry
        val eventText = event?.text?.joinToString() ?: ""
        if (eventText.contains("sent you", ignoreCase = true)) return true

        return false
    }
}
