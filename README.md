# Secure Scan

An Android QR/barcode scanning app that checks whether a decoded web link uses HTTPS or unencrypted HTTP.

## Overview

Secure Scan lets users scan codes with their device camera or choose an image from the gallery. When a code contains a URL, the app checks its URI scheme and reports HTTPS links, HTTP links, or values that are not valid web links.

This is a basic transport-security indicator, not a website reputation or phishing checker. An HTTPS result means the link uses HTTPS; it does not guarantee that the destination is trustworthy.

The application processes scans on the device. It has no backend service or database, and it does not save scan history.

## Key Features

- **Camera scanning:** Uses CameraX to show a rear-camera preview and analyze frames for codes.
- **Gallery scanning:** Select an image from the device and process it with ML Kit.
- **URL classification:** Recognizes HTTP and HTTPS links with a host; reports other or malformed values as not valid web links.
- **Scan feedback:** Displays the classification and reports cases such as a missing code, image-processing failure, or denied camera permission.
- **Camera permission handling:** Requests camera access when the user starts a camera scan.
- **Animated onboarding:** Includes a launch animation, introductory screens, and a Skip option.

## Technologies

| Area | Technology |
|---|---|
| Language | Java (source and target compatibility: 11) |
| Platform | Android |
| Camera preview and image analysis | AndroidX CameraX 1.3.0 |
| Code recognition | Google ML Kit Barcode Scanning 17.2.0 |
| Android UI and support libraries | AndroidX AppCompat 1.7.0, Activity 1.10.0, Material Components 1.12.0, ConstraintLayout 2.2.0 |
| Unit testing | JUnit 4.13.2 |
| Instrumented testing | AndroidX Test JUnit 1.2.1, Espresso Core 3.6.1 |
| Build | Gradle Wrapper 8.10.2, Android Gradle Plugin 8.8.0, Kotlin DSL build scripts |
| Android SDK | Compile SDK 35; minimum SDK 24; target SDK 35 |

## Project Structure

The Git repository contains the Android Studio project in the `SecureScan/` directory:

```text
.
├── README.md
└── SecureScan/
    ├── app/
    │   ├── build.gradle.kts
    │   ├── proguard-rules.pro
    │   └── src/
    │       ├── main/
    │       │   ├── AndroidManifest.xml
    │       │   ├── anim/
    │       │   ├── java/com/example/securescan/
    │       │   │   ├── Activity_Scan.java
    │       │   │   ├── Activity_Splash.java
    │       │   │   ├── MainActivity.java
    │       │   │   └── ScanResultClassifier.java
    │       │   └── res/
    │       │       ├── anim/
    │       │       ├── drawable/
    │       │       ├── layout/
    │       │       ├── mipmap-hdpi/
    │       │       ├── mipmap-mdpi/
    │       │       ├── mipmap-xhdpi/
    │       │       ├── mipmap-xxhdpi/
    │       │       ├── mipmap-xxxhdpi/
    │       │       ├── mipmap-anydpi-v26/
    │       │       ├── values/
    │       │       ├── values-night/
    │       │       └── xml/
    │       ├── test/java/com/example/securescan/
    │       │   ├── ExampleUnitTest.java
    │       │   └── ScanResultClassifierTest.java
    │       └── androidTest/java/com/example/securescan/
    │           └── ExampleInstrumentedTest.java
    ├── gradle/
    │   ├── libs.versions.toml
    │   └── wrapper/
    │       └── gradle-wrapper.properties
    ├── build.gradle.kts
    ├── gradle.properties
    ├── gradlew
    ├── gradlew.bat
    └── settings.gradle.kts
```

Generated Gradle output, IDE metadata, and machine-specific `local.properties` are not shown.

## Prerequisites

- **JDK 17** to run Android Gradle Plugin 8.8.0. The app itself compiles with Java 11 source compatibility.
- **Android SDK Platform 35**, installed through Android Studio's SDK Manager or the Android SDK command-line tools.
- **Gradle 8.10.2** is provided by the checked-in Gradle Wrapper; a separate Gradle installation is not required.
- **Android Studio** with support for Android Gradle Plugin 8.8.0 is recommended.
- For device testing, an Android device or emulator running **Android 7.0 (API 24) or later**. A physical device with a rear camera is recommended for camera scanning.

## Installation & Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/eman-110/SecureScan.git
   cd SecureScan/SecureScan
   ```

2. Open the `SecureScan/` project directory (the directory containing `settings.gradle.kts`) in Android Studio. Allow Gradle sync to finish and install Android SDK Platform 35 if prompted.

   For command-line builds, make sure the Android SDK is installed and available to Gradle. Android Studio can configure the local SDK path for the project.

3. Build the debug APK:

   **Windows PowerShell:**

   ```powershell
   .\gradlew.bat assembleDebug
   ```

   **macOS or Linux:**

   ```bash
   ./gradlew assembleDebug
   ```

4. To install on a connected device or running emulator:

   **Windows PowerShell:**

   ```powershell
   .\gradlew.bat installDebug
   ```

   **macOS or Linux:**

   ```bash
   ./gradlew installDebug
   ```

The generated debug APK is located at `app/build/outputs/apk/debug/app-debug.apk` within the Android project directory. Alternatively, select a device in Android Studio and run the `app` configuration.

## Database Configuration

Secure Scan does not use a database. There are no database tables, connection strings, or schema setup steps. Scanned values are processed for the current result and are not saved as history.

## Application Usage

1. Launch **Secure Scan** and continue through the introduction, or select **Skip**.
2. On the scan screen:
   - Tap the QR scan button to start camera scanning. Grant camera permission if requested.
   - Tap **Import From Gallery** to choose an image containing a code.
3. Review the result:
   - **Secure HTTPS link detected** — the decoded URL uses HTTPS and has a host.
   - **Warning: HTTP link is not secure** — the decoded URL uses HTTP and has a host.
   - **Scanned code is not a valid web link** — the decoded value is not a parseable HTTP or HTTPS URL with a host.

The app reports results in a toast notification. It does not open the link or assess the destination's reputation.

## Screenshots

Add screenshots of the app to `docs/screenshots/` and replace these placeholders:

<!-- Screenshot placeholder: save the onboarding screen as docs/screenshots/onboarding.png. -->
<!-- Screenshot placeholder: save the camera scan screen as docs/screenshots/scan-screen.png. -->
<!-- Screenshot placeholder: save representative scan-result screens as docs/screenshots/results.png. -->

## Future Improvements

- Add an optional link-reputation or phishing-intelligence check and explain its results separately from HTTPS classification.
- Show the decoded URL and provide a user-controlled option to copy or open it.
- Add an opt-in scan history with controls to clear stored results.
- Expand automated coverage for camera, gallery, permission, and UI flows.
- Improve accessibility and validate layouts across a wider range of device sizes.

## Author

**eman-110**  
GitHub: [@eman-110](https://github.com/eman-110)
