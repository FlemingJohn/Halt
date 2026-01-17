# Halt 🛑

**Halt** is a minimal, native Android application designed to stop "doomscrolling" at the operating system level. It uses Android's **Accessibility Services** to intelligently detect and block infinite scrolling feeds (like Instagram Reels and Explore) while preserving the ability to use the app for intentional communication (DMs).

> **Philosophy**: Not a clone, but a control layer. Invisible power.

## 📂 Project Structure

- **`app/`**: Standard Android App module.
  - **`java/com/halt/`**: Kotlin source code.
    - `HaltAccessibilityService.kt`: The core engine that processes UI events.
    - `ScreenDetector.kt`: Logic to analyze screen content and identify infinite feeds.
    - `BlockActivity.kt`: The "Take a breath" overlay screen.
    - `SettingsManager.kt`: Handles preferences like Strict Mode and Pausing.
  - **`res/`**: Layouts, Strings, and Accessibility Configuration.
- **`Guides/`**: Project documentation (PRD, Block Logic).
- **`Diagrams/`**: Visual architecture and flow diagrams.

## 🛠 Architecture

The app runs as a silent background service.

![Architecture](Diagrams/architecture.mmd)

1.  **Detection**: `ScreenDetector` scans `AccessibilityNodeInfo` for keywords ("Reels", "Explore") and structure.
2.  **Context Awareness**: It checks for "sent you" or chat indicators to **Allow** DMs.
3.  **Blocking**: If a feed is detected, `BlockActivity` is launched immediately on top.

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer.
- Android Device/Emulator (Min SDK 26, Target SDK 34).

### Installation
1.  Clone the repository.
2.  Open in Android Studio.
3.  Sync Gradle.
4.  Run on device.

### Permissions
On first launch, you must grant:
1.  **Accessibility Service**: To read screen content.
2.  **Display Over other Apps**: To show the block screen.

## ⚙️ Features
- **Reels Blocker**: Detects and bans the Reels interface.
- **Explore Blocker**: Detects the Search/Explore grid.
- **Strict Mode**: Prevents easy disabling (WIP).
- **Pause Mode**: Allow 15 minutes of free usage.

## 📊 User Flow

![User Flow](Diagrams/userflow.mmd)

## 🤝 Contributing
Refer to `Guides/PRD.md` for the product vision.
