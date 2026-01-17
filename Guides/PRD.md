# PRD – 

**Kill Infinite Scrolling at the OS Level**

---

## 1. Product Overview

### Problem

Infinite scrolling apps (Instagram Reels, Explore, YouTube Shorts, LinkedIn feed, Twitter, Snapchat) exploit attention loops, causing time loss and reduced focus.

Android does not provide native controls to block specific app sections (like Reels). Existing digital wellbeing tools are app-level, not **screen-level**.

---

### Solution

A **native Android app** that uses **Accessibility Services** to detect and block infinite scrolling screens at the OS level.

The app **blocks entry points** (Reels / Explore / Shorts buttons) while **allowing intentional content** opened via direct messages or shared links.

---

## 2. Goals & Success Criteria

### Primary Goal

Prevent users from entering infinite scrolling feeds while allowing intentional consumption.

### Success Metrics

* Instagram Reels blocked consistently
* Instagram Explore blocked consistently
* No crashes during scrolling
* Accessibility service stability > 95%

---

## 3. MVP Scope (Finals Qualification)

### Must Work (Non-Negotiable)

* Instagram Reels → BLOCKED
* Instagram Explore → BLOCKED

If this fails → product is invalid.

---

## 4. Non-Goals (For MVP)

* Analytics dashboards
* Usage charts
* Gamification
* Social features

These are post-launch.

---

## 5. User Personas

### Focused Student

Wants Instagram for DMs but not Reels.

### Working Professional

Uses LinkedIn messaging but avoids feed scrolling.

### Creator

Needs intentional viewing but no algorithmic loops.

---

## 6. User Flow (Core)

1. User installs app
2. Grants Accessibility permission
3. Enables blocking rules
4. App runs silently in background
5. When forbidden screen is detected → overlay blocks access

---

## 7. Core Functional Requirements

---

### FR-1: Detect Active App

The system must detect which app is currently in the foreground.

**Implementation**

* Use `AccessibilityEvent.packageName`

```kotlin
val activeApp = event.packageName?.toString()
```

---

### FR-2: Detect Infinite Scrolling Screens (Instagram)

The system must detect Reels and Explore screens.

**Detection Strategy**

* Text matching
* Accessibility node hierarchy
* Click source detection

```kotlin
val root = rootInActiveWindow ?: return
val reelsNodes = root.findAccessibilityNodeInfosByText("Reels")
```

If nodes exist → user is in Reels.

---

### FR-3: Block Entry via UI Buttons

If user clicks Reels / Explore button → block immediately.

```kotlin
if (event.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
    val label = event.text?.joinToString() ?: ""
    if (label.contains("Reels", true)) {
        showBlockOverlay()
    }
}
```

---

### FR-4: Allow Content Opened via DM / Link

If a reel/video is opened via message or deep link → allow access.

**Logic**

* No Reels button click
* Entry via chat context
* Short session allowed

```kotlin
if (label.contains("sent you", true)) {
    return // Allow viewing
}
```

---

### FR-5: Overlay Blocking Screen

When blocked, the user must not interact with the feed.

**Implementation**

* Full-screen activity
* Launched with `FLAG_ACTIVITY_NEW_TASK`

```kotlin
startActivity(
    Intent(this, BlockActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
)
```

---

### FR-6: Pause or Strict Mode

Users can pause blocking temporarily or enable strict mode.

```kotlin
val pauseUntil = prefs.getLong("pause_until", 0)
if (System.currentTimeMillis() < pauseUntil) return
```

---

### FR-7: Browser Shorts Blocking

Short-form URLs opened in browsers must be blocked.

```kotlin
if (content.contains("youtube.com/shorts") ||
    content.contains("instagram.com/reels")) {
    showBlockOverlay()
}
```

---

## 8. UI / UX Requirements

### Design Principles

* Minimal
* No scrolling
* Low stimulation
* Neutral colors

---

### Screens

**Home**

* Toggle switches for platforms

**Block Overlay**

* Reason shown
* Back button only
* Optional pause

**Settings**

* Strict mode
* Boot start
* Accessibility status

---

## 9. Technical Architecture

```
AccessibilityService
 ├── App Detector
 ├── Screen Detector
 ├── Rule Engine
 └── Overlay Launcher
```

---

## 10. Permissions Required

* Accessibility Service (mandatory)
* Draw over other apps
* Usage access (optional)
* Boot receiver (optional)

---

## 11. Risks & Mitigation

### Risk: App UI changes

Mitigation: Text + hierarchy fallback detection.

### Risk: Play Store rejection

Mitigation:

* Clear purpose explanation
* No data collection
* No misleading behavior

---



## 14. Definition of Done

* Instagram Reels blocked
* Instagram Explore blocked
* No accidental blocking of DMs
* App runs silently

---

### Final Note (Mentor Truth)

This product is **not a clone**, it’s a **control layer**.
The UI is simple. The power is invisible.

If you want next:

* Full **Instagram node hierarchy dump**
* YouTube Shorts detection logic
* AccessibilityService XML config
* Play Store safe description text

This is a **launchable product**, not just a hackathon demo.
