# Product Requirements Document (PRD)

| **Project Name** | Halt |
| :--- | :--- |
| **Version** | 1.0 (MVP) |
| **Status** | Implementation Complete |
| **Last Updated** | January 17, 2026 |
| **Platform** | Android (Native Kotlin) |

---

## 1. Executive Summary
**Halt** is a digital wellness utility designed to combat algorithmic addiction. Unlike traditional app blockers that disable entire applications, Halt functions as an intelligent **Content Control Layer** at the operating system level. It selectively intercepts and blocks infinite-scrolling feeds (e.g., Instagram Reels, YouTube Shorts) while preserving access to utility features like Direct Messages and Search.

## 2. Problem Statement
Social media platforms are engineered to maximize time-on-screen through "infinite scrolling" mechanisms.
*   **User Pain Point**: Users want to stay connected (messaging, updates) but lack the self-control to avoid getting trapped in addictive video feeds.
*   **Market Gap**: Existing solutions are binary—they block the app entirely (isolating the user) or set easily ignored time limits. There is no solution that filters *content types* within native apps.

## 3. Goals & Objectives
*   **Primary Goal**: Minimize "doomscrolling" time by blocking access to short-form algorithmic video feeds.
*   **Secondary Goal**: Maintain user retention by allowing intentional usage (messaging, specific content lookups).
*   **Technical Goal**: Achieve <100ms detection latency with minimal battery impact (<1% per day).

## 4. User Personas
1.  **The Student**: Needs Instagram for class chats but loses hours to Reels.
2.  **The Professional**: Uses LinkedIn/Twitter for networking but gets distracted by the feed.
3.  **The Creator**: Needs to post content but wants to avoid consuming it.

---

## 5. Functional Requirements (FR)

### **Core Blocking Engine**

| ID | Requirement | Priority | Implementation Status |
| :--- | :--- | :--- | :--- |
| **FR-01** | **Active App Detection**<br>System must identify the foreground application package (Instagram, Chrome, etc.). | **P0** | ✅ Implemented |
| **FR-02** | **Reels Detection (Native)**<br>System must detect the "Reels" interface via accessibility node text scanning. | **P0** | ✅ Implemented |
| **FR-03** | **Explore Grid Detection**<br>System must identify the "Search/Explore" grid to prevent mindless browsing. | **P0** | ✅ Implemented |
| **FR-04** | **Browser Shorts Detection**<br>System must detect `youtube.com/shorts` and `instagram.com/reels` URLs in supported browsers (Chrome, Firefox). | **P1** | ✅ Implemented |

### **Smart Exceptions**

| ID | Requirement | Priority | Implementation Status |
| :--- | :--- | :--- | :--- |
| **FR-05** | **Intentional DM Access**<br>System must ALLOW access to Reels if opened within a Direct Message context ("sent you" indicator). | **P0** | ✅ Implemented |
| **FR-06** | **Pause Mechanism**<br>User can temporarily suspend blocking for a fixed duration (15 minutes). | **P1** | ✅ Implemented |
| **FR-07** | **Strict Mode**<br>Option to disable the "Pause" capability for enforcing discipline. | **P2** | ✅ Implemented |

### **User Interface**

| ID | Requirement | Priority | Implementation Status |
| :--- | :--- | :--- | :--- |
| **FR-08** | **Blocking Overlay**<br>A full-screen, non-dismissible overlay ("Take a breath") must appear immediately upon detection. | **P0** | ✅ Implemented |
| **FR-09** | **Dashboard**<br>Main app screen displaying service status and providing permission granting flows. | **P0** | ✅ Implemented |

---

## 6. Non-Functional Requirements (NFR)

*   **NFR-01: Performance**: Detection logic must execute in under 100ms to prevent user viewing of blocked content.
*   **NFR-02: Privacy**: All processing must occur **on-device**. No screen data shall be transmitted to external servers.
*   **NFR-03: Battery Efficiency**: The Accessibility Service must not poll; it should only react to `TYPE_WINDOW_CONTENT_CHANGED` events efficiently.
*   **NFR-04: Reliability**: The service must automatically restart or prompt the user if killed by the OS.

---

## 7. Technical Architecture
The solution relies on the Android **Accessibility API**.

*   **Service Layer**: `HaltAccessibilityService` (Background Process)
*   **Logic Layer**: `ScreenDetector` (Heuristic Analysis)
*   **Data Layer**: `SettingsManager` (SharedPreferences)

> **Constraint**: This architecture requires the user to manually grant "Accessibility" and "Display Over Apps" permissions. This is a known friction point acceptable for this utility category.

---

## 8. Success Metrics (KPIs)

*   **Block Rate**: % of Reels access attempts successfully blocked. (Target: 100%)
*   **False Positive Rate**: % of legitimate screens (DMs, Profile) incorrectly blocked. (Target: < 1%)
*   **Retention**: % of users who keep the Accessibility Service enabled after 7 days.

---

## 9. Future Roadmap (Post-MVP)

*   [ ] **YouTube App Support**: Native YouTube app blocking (complex node hierarchy).
*   [ ] **Usage Analytics**: Local charts showing "Time Saved" or "Reels Blocked".
*   [ ] **Gamification**: Streaks for days without disabling the service.
*   [ ] **Emergency Unlock**: A complex task (math problem or typing) to unlock Strict Mode.
