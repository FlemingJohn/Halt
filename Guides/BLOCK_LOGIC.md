# How Halt Actually Works (The Logic)

Let’s strip this down to **how it actually works on a real phone**, based on the implementation in `com.halt`.

---

## The Core Loop

1. **Event Fired**: Android tells us "Something changed on screen" (`HaltAccessibilityService`).
2. **Context Check**: Are we in a supported app? (Instagram, Chrome, Firefox).
3. **Settings Check**: Is the user "Paused"? If yes, do nothing.
4. **Analysis**: We pass the screen content (`rootInActiveWindow`) to `ScreenDetector`.
5. **Action**: If `ScreenDetector` returns a match, we launch `BlockActivity`.

---

## 1. Smart Detection (`ScreenDetector.kt`)

We separated the detection logic into a pure class.

### Instagram Reels
```kotlin
fun isReels(root: AccessibilityNodeInfo): Boolean {
    // Look for "Reels" text on screen
    return root.findAccessibilityNodeInfosByText("Reels").isNotEmpty()
}
```

### Instagram Explore
```kotlin
fun isExplore(root: AccessibilityNodeInfo, event: AccessibilityEvent?): Boolean {
    // Look for "Search" + Context
    val hasSearch = root.findAccessibilityNodeInfosByText("Search").isNotEmpty()
    
    // If we are scrolling inside a search view, it's likely the Explore grid
    if (hasSearch && event?.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
        return true
    }
    
    // Explicit "Explore" label
    return root.findAccessibilityNodeInfosByText("Explore").isNotEmpty()
}
```

### Browser Blocking (New!)
We scan text nodes for specific URL patterns. This works even if the URL bar is hidden, as long as the URL is present in the node tree (common in accessibility trees).

```kotlin
fun isBrowserReelOrShort(root: AccessibilityNodeInfo): String? {
    val patterns = listOf("youtube.com/shorts", "instagram.com/reels")
    
    // Heuristic: Does any node contain these strings?
    // We scan the tree (or specific text nodes) for matches.
    // Result: "Shorts Blocked" or "Reels Blocked"
}
```

---

## 2. The Exception Rule (DMs)

The most important feature is **not being annoying**. We allow Reels if they are sent in a DM.

**Logic**:
We check for "sent you" indicators *before* running the blocking logic.

```kotlin
if (screenDetector.isAllowedContext(rootNode, event)) {
    // User is in a chat. Do not block.
    return
}
```

---

## 3. The "Halt" Action

When a block is triggered, we don't just kill the app (that's jarring). We overlay a calming screen.

```kotlin
val intent = Intent(this, BlockActivity::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Required for Service
    putExtra("REASON", "Reels Blocked")
}
startActivity(intent)
```

**Why this works**:
The activity launches *immediately* on top of Instagram. The user sees the blocker, not the feed.

---

## 4. Strict Mode & Pausing (`SettingsManager`)

We added a layer of control.

* **Pause**: Saves a timestamp `pause_until` in SharedPreferences.
* **Strict Mode**: If enabled, simply hides the "Pause" button in the UI.

```kotlin
if (settingsManager.isPaused()) return // Skip all checks
```

---

## Summary

This isn't magic. It's a highly specific **UI Watchdog** that knows exactly what "Doomscrolling" looks like to the Android Accessibility API.
