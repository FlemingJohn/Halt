# Halt

> **Reclaim your attention.**
> A native Android utility that manages infinite scrolling at the OS level.

![Status](https://img.shields.io/badge/Status-Active_Development-brightgreen)
![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin)

Halt is a precision-engineered utility designed to manage digital consumption. By utilizing Android's Accessibility Services, Halt implements surgical filters for features such as Instagram Reels, Explore, and YouTube Shorts. Unlike traditional application blockers, Halt preserves core functionality, such as messaging and search, while restricting addictive interface elements.

---

## Technical Specifications

Halt is built using native Android APIs to ensure high performance and deep OS integration.

### Core Service
*   **`AccessibilityService`**: The application's engine, `HaltAccessibilityService`, inherits from the Android framework's built-in `AccessibilityService`. It receives real-time UI updates via the `onAccessibilityEvent` callback.

### Built-in APIs and Functions
*   **UI Analysis**: Uses `rootInActiveWindow` to retrieve the current screen's `AccessibilityNodeInfo` hierarchy.
*   **Targeting**: Implements `findAccessibilityNodeInfosByText()` as a primary heuristic for identifying interactive elements (e.g., "Reels", "Shorts").
*   **State Detection**: Leverages properties like `isSelected` to detect active navigation tabs and `isClickable` to differentiate between static headers and interactive controls.
*   **Context Management**: Uses `SharedPreferences` via `SettingsManager` to persist state (Pause, Strict Mode) across application lifecycles.
*   **Overlay Execution**: Launches the blocking interface using `Intent.FLAG_ACTIVITY_NEW_TASK` and `FLAG_ACTIVITY_CLEAR_TASK` for immediate intervention.

---

## Compatibility

Halt provides native support for specific application contexts and widely used mobile browsers.

| Category | Targeted Packages |
| :--- | :--- |
| **Native Apps** | `com.instagram.android`, `com.google.android.youtube` |
| **Web Browsers** | `com.android.chrome`, `com.sec.android.app.sbrowser`, `org.mozilla.firefox` |

---

## Technical Advantages

Traditional digital wellness tools are often imprecise, blocking entire applications and disrupting essential communications.

**Halt provides a more granular approach:**
*   **Precision Blocking:** Targets specific components like Reels and Shorts rather than the entire application.
*   **Contextual Awareness:** Allows content viewed within appropriate contexts, such as direct messages.
*   **Multi-Platform Detection:** Identifies and manages short-form video content within supported browsers.

---

## System Architecture

The following diagram illustrates the lifecycle of the Halt service and its surgical detection engine.

```mermaid
graph LR
    Start([User opens Halt]) --> Toggle{Service Enabled?}
    
    Toggle -- "No"  --> End([Dashboard])
    Toggle -- "Yes" --> Active[[Monitoring Loop]]

    subgraph Targets [Targeted Content]
        direction TB
        Active --> IG["Instagram: Reels & Explore"]
        Active --> YT["YouTube: Shorts"]
        Active --> Web["Browsers: Reels & Shorts"]
    end

    IG & YT & Web --> Decision{Blocked?}
    
    Decision -- "Yes" --> Block[Block Screen]
    Decision -- "No"  --> Active
```

👉 [Full Architecture Diagram](Diagram/architecture.mmd)

---

## Features and Status

| Feature | Status | Description |
| :--- | :---: | :--- |
| **Reels Management** | Complete | Detects and manages the Instagram Reels interface. |
| **Explore Management** | Complete | Prevents scrolling on the Explore grid. |
| **Shorts Management** | Complete | Manages YouTube Shorts in supported browsers and applications. |
| **Contextual Exceptions** | Complete | Intelligent handling of content shared via direct messages. |
| **Intervention Overlays** | Complete | Dynamic overlays designed to interrupt scrolling habits. |
| **Administrative UI** | Complete | Professional dashboard for configuration. |
| **Pause Functionality** | Complete | Temporary suspension of management for defined intervals. |
| **Strict Mode** | In Progress | Enhanced safeguards against service deactivation. |

---

## Documentation

*   **[Setup Guide](Guides/SETUP.md)**: Detailed instructions for installation and on-device configuration.
*   **[Product Requirements Document (PRD)](Guides/PRD.md)**: Vision and functional requirements.

---

## Implementation

### Prerequisites
*   Android Studio (Hedgehog or newer)
*   Physical Android Device (Accessibility features are best tested on hardware)
*   Minimum SDK: 26 (Android 8.0)

---

## Privacy and Security

Halt is designed with a focus on user privacy:
*   **Local Execution**: All analysis occurs locally on the device.
*   **No Network Access**: The application does not request internet permissions.
*   **Code Transparency**: The detection logic is open for independent review.

---

Halt — Stop scrolling. Start living.
