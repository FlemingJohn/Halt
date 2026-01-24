package com.halt

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Responsible for analyzing the screen content (AccessibilityNodeInfo) to determine if the user is
 * in a blocked section (Reels/Explore).
 */
class ScreenDetector {

    fun isExplore(root: AccessibilityNodeInfo, event: AccessibilityEvent?): Boolean {
        // 1. Check for "Explore" header/text specifically as the prominent element
        val exploreNodes = root.findAccessibilityNodeInfosByText("Explore")
        if (exploreNodes.isEmpty()) return false

        // 2. Refinement: If we also see "Search" or grid-like structure, it's more likely Explore
        val searchNodes = root.findAccessibilityNodeInfosByText("Search")

        // 3. EXCLUSION: If we see Home indicators, it's not Explore
        if (isHomeFeed(root)) return false

        return searchNodes.isNotEmpty() || event?.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED
    }

    fun isAllowedContext(root: AccessibilityNodeInfo, event: AccessibilityEvent?): Boolean {
        // 1. Check for "sent you" (DM indication)
        if (root.findAccessibilityNodeInfosByText("sent you").isNotEmpty()) return true

        // 2. Check event text for notification/toast based entry
        val eventText = event?.text?.joinToString() ?: ""
        if (eventText.contains("sent you", ignoreCase = true)) return true

        return false
    }

    fun isReels(root: AccessibilityNodeInfo): Boolean {
        // 1. EXCLUSION: If we see Home feed specific elements, don't block
        if (isHomeFeed(root)) return false

        // 2. Check for "Reels" text
        val reelsText = root.findAccessibilityNodeInfosByText("Reels")
        if (reelsText.isNotEmpty()) {
            // Check if it's the selected tab or a high-level header
            for (node in reelsText) {
                if (node.isSelected || node.isClickable.not()
                ) { // Headers aren't usually clickable in the same way tabs are
                    return true
                }
            }
        }

        return false
    }

    private fun isHomeFeed(root: AccessibilityNodeInfo): Boolean {
        val homeSignals = listOf("Your Story", "Threads", "Messages", "Create", "Notifications")
        for (signal in homeSignals) {
            if (root.findAccessibilityNodeInfosByText(signal).isNotEmpty()) return true
        }
        return false
    }

    fun isYouTubeShorts(root: AccessibilityNodeInfo, event: AccessibilityEvent?): Boolean {
        // 1. Check for "Shorts" tab selection
        val shortsNodes = root.findAccessibilityNodeInfosByText("Shorts")
        for (node in shortsNodes) {
            if (node.isSelected) return true
        }

        // 2. Check for Shorts shelf in recommendations/search (The "Shorts" header)
        // Usually, the shelf has a "Shorts" title and specific vertically oriented thumbs
        // If we see "Shorts" and we are scrolling in a list that isn't the main player
        if (shortsNodes.isNotEmpty() && event?.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            // This is a bit broad, but targeted within the YouTube package
            return true
        }

        // 3. Full-screen Shorts player features (vertical layout buttons)
        if (root.findAccessibilityNodeInfosByText("Remix").isNotEmpty() &&
                        root.findAccessibilityNodeInfosByText("Subscriptions").isNotEmpty()
        ) {
            return true
        }

        return false
    }

    fun isBrowserReelOrShort(root: AccessibilityNodeInfo): String? {
        // More comprehensive list including mobile and desktop formats
        val blockedPatterns =
                listOf(
                        "youtube.com/shorts",
                        "m.youtube.com/shorts",
                        "instagram.com/reels",
                        "m.instagram.com/reels"
                )

        return findBlockedUrl(root, blockedPatterns)
    }

    private fun findBlockedUrl(node: AccessibilityNodeInfo?, patterns: List<String>): String? {
        if (node == null) return null

        // Check current node text and description
        val text = node.text?.toString() ?: ""
        val description = node.contentDescription?.toString() ?: ""

        for (pattern in patterns) {
            if (text.contains(pattern, ignoreCase = true) ||
                            description.contains(pattern, ignoreCase = true)
            ) {
                return if (pattern.contains("youtube")) "Shorts Blocked" else "Reels Blocked"
            }
        }

        // Recursively check children
        for (i in 0 until node.childCount) {
            val result = findBlockedUrl(node.getChild(i), patterns)
            if (result != null) return result
        }

        return null
    }
}
