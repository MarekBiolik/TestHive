---
name: platform-delivery
description: >-
  Deliver a TestHive slice core-first then parallel platforms then tests:
  shared KMP contract before Android/web UI, tests required (no skips;
  blocked → stop and ask). Use when implementing multi-area features or
  the user says core-first / platform delivery.
---

# Platform delivery

Lead: plan, decide, dispatch. **Core first**, then **platforms**, then **tests**. Convention: [agent-dev-environment](../../../docs/conventions/agent-dev-environment.md). Issue → PR → commit rules stay in force.

## Workflow

```
Platform delivery:
- [ ] 1. Intake gate
- [ ] 2. Issues ready
- [ ] 3. Core slice
- [ ] 4. Parallel platforms
- [ ] 5. Tests
- [ ] 6. Hand-off
```

### 3. Core slice

Land shared seam in `core/` first (import/grading/session/progress). Focused PR; merge before UI unless user stacks.

### 4. Parallel platforms

| Area | Path | Notes |
|------|------|--------|
| Android | `apps/android/` | Full UI over core |
| Web | `apps/web/` | Thin TS grading (ADR 0006) |
| iOS | `apps/ios/` | Blocked until OQ-02 |

### 5. Tests

Required via `qa-testing`: core unit; Android emulator smoke when UI changed; web build when web changed. No skips.

## Guardrails

- No Supabase / custom backend in this product phase
- Empty `adb devices` → start AVD (`android-emulator`)
