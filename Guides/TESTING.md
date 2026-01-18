# Testing Guide – Halt 🛑

This guide explains how to install **Halt** on your physical Android device and verify that it blocks content correctly.

---

## 1. Prepare Your Phone

Before installation, you must enable **Developer Options**:

1.  Open **Settings** > **About Phone**.
2.  Tap **Build Number** 7 times rapidly until it says "You are a developer".
3.  Go back to **System** > **Developer Options**.
4.  Enable **USB Debugging**.

---

## 2. Install via Android Studio

1.  Connect your phone to your PC via USB.
2.  In Android Studio, check the device dropdown (top bar). It should show your phone info (e.g., "Pixel 7").
3.  Click the green **Run** ▶️ button (or press `Shift + F10`).
4.  The app will install and open automatically.

---

## 3. Initial Setup (Critical)

When the app opens, you will see the **Dashboard**. You MUST grant permissions for the app to work.

1.  **Overlay Permission**:
    *   Tap **Grant Permission** (if prompted) or **Enable Service**.
    *   Find "Halt" in the list.
    *   Toggle **Allow display over other apps** -> **ON**.
    *   Go Back.

2.  **Accessibility Permission**:
    *   Tap **Grant Permission** / **Enable Service**.
    *   Find "Halt" in the **Downloaded Apps** section.
    *   Toggle **Use Halt** -> **ON**.
    *   Confirm the warning dialog (Allow).

**Status Check**: The Dashboard should now say **"System Active"** in Green (`emerald_600`), and the permission buttons will disappear to maintain a clean workspace.

---

## 4. Test Scenarios

### Test 1: Instagram Reels (The Main Goal)
1.  Open **Instagram**.
2.  Tap the **Reels** icon (center or bottom bar).
3.  **Expected**: The "Reels Blocked" screen should appear immediately.
4.  Tap **Go Back** to return to safety.

### Test 2: Instagram Explore
1.  Tap the **Search / Explore** icon 🔍.
2.  Try to scroll down the grid.
3.  **Expected**: The "Explore Blocked" screen should appear.

### Test 3: Browser Blocking (Chrome/Firefox)
1.  Open **Chrome**.
2.  Go to `youtube.com/shorts`.
3.  **Expected**: The "Shorts Blocked" screen should appear.
4.  Go to `instagram.com/reels`.
5.  **Expected**: The "Reels Blocked" screen should appear.

### Test 4: The Exception (DMs)
1.  Ask a friend to send you a Reel via DM.
2.  Open the DM chat.
3.  Tap the Reel to watch it.
4.  **Expected**: The video plays. You are **NOT** blocked. This is the "Intentional Use" feature.

---

## 5. Troubleshooting

**"It's not blocking anything!"**
*   Go to Settings > Accessibility > Downloaded Apps > Halt.
*   Turn it **OFF**, wait 2 seconds, then turn it **ON** again.
*   Ensure **Battery Saver** is NOT restricting the app.

**"The overlay is stuck!"**
*   We haven't implemented a "Close" button for strictness.
*   Use your phone's **Home Gesture** or **Back Button** to escape.
*   If stuck, Force Stop the app in Settings.
