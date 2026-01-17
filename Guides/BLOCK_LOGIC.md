Let’s strip this down to **how it actually works on a real phone**, not theory, not buzzwords.

I’ll explain **Instagram Reels** and **Instagram Explore** separately, with **real-time flow + tiny Kotlin snippets**, exactly how blocker apps do it.

---

## First: one core truth (important)

Android **cannot modify Instagram**.
So your app does **this instead**:

> Watch the screen → recognize where the user is → stop them instantly.

That’s it. No hacking, no APIs.

---

## How Instagram Reels is blocked (step by step)

### What happens in real life

1. User opens Instagram
2. User taps **Reels button** (bottom navbar)
3. Instagram loads the Reels screen
4. Your Accessibility Service sees this
5. Your app **immediately blocks the screen**

User never gets to scroll.

---

### How your app detects “Reels”

Instagram Reels screen has:

* A visible **“Reels” tab/button**
* Continuous **vertical scroll**
* Specific **UI text nodes**

Your app listens for **UI events**.

---

### Detection logic (simple & real)

```kotlin
override fun onAccessibilityEvent(event: AccessibilityEvent) {
    if (event.packageName != "com.instagram.android") return

    val root = rootInActiveWindow ?: return
    val reelsNodes = root.findAccessibilityNodeInfosByText("Reels")

    if (reelsNodes.isNotEmpty()) {
        blockScreen("Instagram Reels blocked")
    }
}
```

The moment the Reels screen appears → overlay shows.

---

### What the user experiences

* Tap Reels
* Screen instantly turns into **“Reels Blocked”**
* No scrolling possible
* Back button returns to Instagram home

This feels **instant**, not delayed.

---

## How Instagram Explore is blocked (step by step)

Explore is trickier — but still doable.

---

### Real-life Explore flow

1. User taps 🔍 **Search / Explore**
2. Grid of endless posts appears
3. User scrolls infinitely

That grid is what you block.

---

### How your app detects Explore

Explore page has:

* “Search” text field at top
* Image grid
* No DM/chat context

You detect **Search + scroll**.

---

### Detection logic (real approach)

```kotlin
val searchNodes = root.findAccessibilityNodeInfosByText("Search")

if (searchNodes.isNotEmpty()) {
    blockScreen("Instagram Explore blocked")
}
```

Or block when scrolling starts:

```kotlin
if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
    blockScreen("Explore feed blocked")
}
```

Most blocker apps combine **text + scroll detection** for stability.

---

### What the user experiences

* Tap Explore
* Grid loads for a split second
* Block overlay appears
* Scroll never happens

Mission accomplished.

---

## Important: Allow Reels opened via DM (smart behavior)

This is where your app feels **intelligent**, not annoying.

---

### DM reel flow (real)

1. Friend sends reel in DM
2. User taps the message
3. Reel opens directly
4. User watches → allowed

Why allowed?

* No Reels button click
* Entry is intentional
* No infinite loop

---

### How your app allows it

```kotlin
if (event.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
    val text = event.text?.joinToString() ?: ""
    if (text.contains("sent you", true)) {
        return // allow
    }
}
```

Also:

* Short session
* No repeated scrolling

---

## How scrolling itself is stopped

Two methods (used together):

### Method 1: Full-screen overlay (most reliable)

```kotlin
startActivity(
    Intent(this, BlockActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
)
```

Overlay:

* Covers entire screen
* No interaction passes through
* User must go back

---

### Method 2: Force Back (optional)

```kotlin
performGlobalAction(GLOBAL_ACTION_BACK)
```

Used only when overlay fails.

---

## Why this works even if Instagram updates UI

Because you’re not relying on:

* API
* App internals
* Private methods

You’re relying on:

* What the **user sees**
* What Android exposes
* Human-visible UI patterns

That’s why apps like **No Scroll** survive updates.

---

## One honest limitation (every judge knows this)

Instagram may:

* Rename labels
* Change hierarchy

Mitigation:

* Multiple detection rules
* Text + scroll + timing

This is **expected**, not a failure.

---

## Mental model (remember this)

Think of your app as:

> A security guard watching the screen
> If the door says “Reels” or “Explore” → access denied

You’re not fighting Instagram.
You’re controlling **entry points**.

---

