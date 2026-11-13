---
name: qa-testing
description: >-
  Run TestHive test gates: core unit, web build, Android emulator smoke —
  tests required, no skips; blocked → stop and ask. Use when verifying a
  slice before PR or the user says QA / test / verify.
---

# QA / testing

Priority: [testing](../../../docs/conventions/testing.md). CI goals: [ci-cd](../../../docs/conventions/ci-cd.md).

Tests **are required**. No skip gate. If a layer cannot run, **stop and ask**.

## Priority order

1. **Core unit** — `.\gradlew.bat :core:testDebugUnitTest` when `core/` changed
2. **Web** — `pnpm web:build` when `apps/web/` changed
3. **Android smoke** — `android-emulator` when `apps/android/` changed (starts AVD if needed)

## Workflow

```
QA gate:
- [ ] 1. Change-set matrix
- [ ] 2. Run every applicable layer
- [ ] 3. Triage failures
- [ ] 4. Report
```

## Guardrails

- Flaky test = bug
- JDK 17+ via `JAVA_HOME` when needed
- Do not tick AC without running the layer
