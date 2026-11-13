# ADR 0003 — Native UI per platform

- **Status:** accepted
- **Date:** 2026-11-13
- **Decision makers:** Marek (+ architecture foundations session)

## Context

Platform goal: Android + web now, iOS later. Prefer shared mobile core with each platform owning UI (Compose, React, SwiftUI), matching GymManager’s approach and native quality.

## Decision

Each platform has its **own UI layer**:

- Android → Jetpack Compose
- Web → React + TypeScript
- iOS → SwiftUI (when unblocked)

Business/grading logic is not owned by UI widgets — it lives in KMP core (mobile) or the documented TS grading module (web).

## Alternatives considered

1. **One cross-platform UI (Expo RN / Flutter)** — less UI work. Rejected for nativeness and consistency with the KMP + native model.
2. **Web + PWA only** — cheapest. Rejected as the only form: Android native app is a primary target.
3. **Native UI per platform** — chosen despite multi-UI cost.

## Consequences

- Positive: full access to platform APIs and look-and-feel; clear split with core.
- Negative: features cost ×N UIs; need discipline so grading does not leak into widgets.
- Later: design tokens may be shared without sharing UI components.

## Related

- [clients](../architecture/clients.md)
- [ADR 0001](0001-shared-core-kmp.md)
- [vision](../00-vision.md)
