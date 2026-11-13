---
name: android-emulator
description: >-
  Run TestHive Android smoke on an AVD/emulator via adb: assemble, install,
  launch, capture failures. Use when smoking Android UI, after android-compose
  changes, or the user says emulator / adb / Android smoke.
---

# Android emulator runner

Required smoke on local AVD/device. Complements `android-compose` and `qa-testing`.

Env: [references/env-checklist.md](references/env-checklist.md). Testing: [testing](../../../docs/conventions/testing.md).

## Workflow

```
Emulator smoke:
- [ ] 1. Preconditions (start AVD if idle)
- [ ] 2. Build
- [ ] 3. Install + launch
- [ ] 4. Smoke path
- [ ] 5. Report
```

### 1. Preconditions

Empty `adb devices` → **start AVD**. Prefer `Medium_Phone_API_37.0`. Poll `sys.boot_completed` until `1`. Only if no AVDs and no device: **stop and ask**.

### 2. Build

```text
.\gradlew.bat :apps:android:assembleDebug
```

Set `JAVA_HOME` to a JDK 17+ (e.g. `C:\Users\<user>\.jdks\jbr-21.0.11`) when `java` is not on PATH.

### 3. Install + launch

```text
adb uninstall com.testhive.android
adb install -r apps\android\build\outputs\apk\debug\android-debug.apk
adb shell am start -n com.testhive.android/.MainActivity
```

Confirm `MainActivity` resumed; check logcat for immediate crash.

### 4. Smoke path

Cold start → screen under test → one primary action from the issue (e.g. start deck → answer → feedback).

### 5. Report

Pass/fail, AVD name, build used. No fake greens.

## Guardrails

- Tests required; no skips
- No secrets in adb commands
