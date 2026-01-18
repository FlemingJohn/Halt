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

### Instagram Detection
The `ScreenDetector` now uses a purely node-based approach, removing dependencies on `AccessibilityEvent` for detection, which improves reliability and reduces lint warnings.

```kotlin
fun isReels(root: AccessibilityNodeInfo): Boolean {
    val reelsText = root.findAccessibilityNodeInfosByText("Reels")
    return reelsText.isNotEmpty()
}

fun isExplore(root: AccessibilityNodeInfo): Boolean {
    val searchNodes = root.findAccessibilityNodeInfosByText("Search")
    val exploreNodes = root.findAccessibilityNodeInfosByText("Explore")
    return searchNodes.isNotEmpty() && exploreNodes.isNotEmpty()
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

### 3. The "Reality Check" Overlay
When a block is triggered, we launch `BlockActivity` with a dynamic messaging system.

```kotlin
private val focusMessages = listOf(
    "STAY LURKING?" to "The algorithm is winning...",
    "TIME IS LEAKING." to "Every reel you watch is a minute you'll never get back.",
    "BUILDING CAPACITY." to "Attention is your most valuable resource."
)

// ... randomly chosen upon creation ...
```

**Why this works**:
The activity launches *immediately* on top of the distracting content. The professional Slate UI and striking typography help break the "scroll trance" by forcing a moment of reflection.

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
