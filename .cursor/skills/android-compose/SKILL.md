---
name: android-compose
description: >-
  Implement TestHive Android UI in Jetpack Compose over KMP core: thin
  presentation, navigation — no domain rules in UI. Use when editing
  apps/android/, Compose screens, Android wiring to :core, or the user
  says Android / Compose.
---

# Android (Compose)

Own `apps/android/`: Jetpack Compose UI and Android platform wiring over `:core`. Native UI: [ADR 0003](../../../docs/decisions/0003-native-ui-per-platform.md). Clients: [clients](../../../docs/architecture/clients.md). Coding: [coding-standards](../../../docs/conventions/coding-standards.md).

Domain stays in `core-kmp`. Emulator smoke: `android-emulator` / `qa-testing`.

**Theme:** consume design tokens via MaterialTheme / app `Theme` wiring ([Neon Glass](../../../docs/specs/visual-style-neon-glass.md), [ADR 0007](../../../docs/decisions/0007-design-tokens-ssot.md)). Do **not** hardcode palette hex — change values via SSOT (+ hand-sync or generator). See [references/theming.md](references/theming.md).

Env: [references/env-checklist.md](references/env-checklist.md).

## Workflow

```
Android slice:
- [ ] 1. Confirm core contract
- [ ] 2. Thin UI
- [ ] 3. Platform wiring
- [ ] 4. Verify build
- [ ] 5. Hand-off
```

### 1. Confirm core contract

Android **consumes** public core API (import, grade, session, progress). New domain rules → `core-kmp` first (`platform-delivery`).

### 2. Thin UI

Compose screens, navigation, ViewModels that call core. No duplicate grading logic in UI.

### 3. Platform wiring

File pickers, assets, local storage adapters implementing core ports.

### 4. Verify build

`.\gradlew.bat :apps:android:assembleDebug`. Then `android-emulator` smoke when UI changed.

### 5. Hand-off

Note screens touched; tick issue AC.

## Guardrails

- No domain/grading reimplementation in Compose
- Theme from tokens / neon-glass — not default Material purple
- Package id remains `com.testhive.android` until rename issue
