# Setup Guide

This guide provides detailed instructions for setting up the Halt application for both development and on-device usage.

---

## Developer Environment Setup

### 1. Prerequisites
- **Android Studio**: Version Hedgehog (2023.1.1) or newer.
- **Android SDK**: API Level 26 (Android 8.0) or higher.
- **Physical Device**: Highly recommended. Many accessibility features are constrained on emulators.

### 2. Project Initialization
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/halt.git
   ```
2. **Open in Android Studio**:
   - File > Open > Select the `Halt` project directory.
3. **Gradle Synchronization**:
   - Allow Android Studio to download dependencies and sync the project structure. This may take a few minutes.
4. **Build the Project**:
   - Navigate to `Build > Make Project` to ensure all components compile correctly.

---

## Device Configuration

Halt requires specific system-level permissions to function effectively. Follow these steps after installing the app on your device.

### 1. Drawing Over Other Apps
This permission allows Halt to display the "Take a breath" intervention overlay when blocked content is detected.
- **Action**: When the app launches, it will prompt for this permission.
- **Manual Path**: `Settings > Apps > Special app access > Display over other apps > Halt > Toggle ON`.

### 2. Accessibility Service
This is the core engine of Halt. It allows the app to analyze the screen content and detect addictive features like Reels or Shorts.
- **Action**: Follow the in-app prompt to the Accessibility settings.
- **Manual Path**: `Settings > Accessibility > Installed Apps / Downloaded Services > Halt > Toggle ON`.
- **Note**: Ensure you accept the system warning regarding screen analysis. Halt performs all processing locally on your device.

### 3. Battery Optimization (Recommended)
To prevent the Android OS from killing the background accessibility service, it is recommended to exclude Halt from battery optimization.
- **Manual Path**: `Settings > Apps > Halt > Battery > Set to "Unrestricted"`.

---

## Troubleshooting

- **Service Not Detecting**: If the app stops blocking, try toggling the Accessibility Service OFF and back ON.
- **Overlay Not Showing**: Ensure the "Display over other apps" permission is correctly granted.
- **Slow Response**: Performance may vary based on device processing power. Ensure no other heavyweight accessibility services are running simultaneously.
