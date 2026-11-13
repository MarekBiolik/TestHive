# ADR 0001 — Shared core: Kotlin Multiplatform

- **Status:** accepted
- **Date:** 2026-11-13
- **Decision makers:** Marek (+ architecture foundations session)

## Context

TestHive targets Android now, web in parallel, and iOS later. Domain rules (deck import, grading, session queue, progress) must not be reinvented ad hoc in each mobile UI. GymManager already uses KMP + native UI; we reuse that architectural shape for the mobile core.

## Decision

We build a shared **`core` module in Kotlin Multiplatform (KMP)** with domain rules, grading, import/validation, and progress use cases. Android (and later iOS) consume core natively. Web does **not** link a KMP binary on day 1 ([ADR 0006](0006-web-grading-in-typescript.md)).

## Alternatives considered

1. **Expo / React Native (+ web)** — one UI for all platforms. Rejected: weaker native UI fit; diverges from the chosen core + native UI model.
2. **Flutter** — one UI, mature multiplatform. Rejected: Dart instead of Kotlin/Swift/TS; abandons native UI per platform.
3. **Backend-only “core”** — rejected: product is local-only with no server ([ADR 0002](0002-local-only-storage.md)).
4. **Kotlin Multiplatform** — chosen for mobile shared logic, Android proximity, and a clear iOS path later.

## Consequences

- Positive: one rules implementation for Android/iOS; unit tests in core; cheap Android integration.
- Negative: KMP toolchain cost; web needs a disciplined TS copy of grading.
- Later: scaffold `core/`; revisit web shared binary only via [OQ-05](../open-questions.md).

## Related

- [shared-core](../architecture/shared-core.md)
- [ADR 0003](0003-native-ui-per-platform.md)
- [ADR 0006](0006-web-grading-in-typescript.md)
