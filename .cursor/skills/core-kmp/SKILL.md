---
name: core-kmp
description: >-
  Implement or change TestHive shared KMP core: domain, import, grading,
  progress, session, commonTest — no UI, no JS/Wasm target. Use when editing
  core/, shared domain rules, or core unit tests; or the user says core / KMP.
---

# Core (KMP)

Work only in **`core/`**: domain, import/validate, grading, progress, session queue — **not** Compose/React. Architecture: [shared-core](../../../docs/architecture/shared-core.md), [ADR 0001](../../../docs/decisions/0001-shared-core-kmp.md). Templates: [templates](../../../docs/domain/templates.md). Tests: [testing](../../../docs/conventions/testing.md).

Env: [references/env-checklist.md](references/env-checklist.md).

## Workflow

```
Core slice:
- [ ] 1. Scope the seam
- [ ] 2. Contract first
- [ ] 3. Implement + unit tests
- [ ] 4. Verify
- [ ] 5. Hand off to platforms
```

### 1. Scope the seam

| In core | Elsewhere |
|---------|-----------|
| Deck import/validate, grading, session queue, progress model | Widgets, theme, navigation |
| Use cases / ports | Android file pickers, Compose |

No `js()` / `wasmJs()` on day 1 ([ADR 0006](../../../docs/decisions/0006-web-grading-in-typescript.md)). Local-only data ([ADR 0002](../../../docs/decisions/0002-local-only-storage.md)) — no Supabase in core.

### 2–5

Public facade for UI; tests against `docs/samples/grading-cases.json` / bundled fixtures; `.\gradlew.bat :core:testDebugUnitTest`.

## Guardrails

- No UI in `core/`
- No parallel domain in TypeScript beyond ADR 0006 thin grading copy
- Prefer strong model for grading rules; cheap helpers for Gradle
