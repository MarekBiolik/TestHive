# ADR 0002 — Local-only decks and progress

- **Status:** accepted
- **Date:** 2026-11-13
- **Decision makers:** Marek (+ architecture foundations session)

## Context

Users import prepared decks and practice with immediate feedback. Multi-device sync would require accounts, a backend, and PII before the quiz loop exists. The first goal is a pleasant local feed (doomscrolling substitute), not cloud continuity.

## Decision

Decks and progress live **only on the device or in the browser**. No accounts, no hosted database, no sync in foundations or early product phases. Cross-device sync remains [OQ-01](../open-questions.md).

## Alternatives considered

1. **Supabase / BaaS from day 1** (GymManager path) — sync and identity early. Rejected: signup/maintenance cost before product value; unnecessary for single-device practice.
2. **Local now, schema shaped for sync** — optional future-proofing. Rejected as a foundations requirement; can reshape when OQ-01 closes.
3. **Local-only** — chosen: fastest path to a useful quiz loop with zero PII.

## Consequences

- Positive: no auth surface; simple threat model; works offline by default.
- Negative: Android and web progress diverge; no recovery across reinstalls without later export/sync.
- Later: OQ-01 if multi-device progress becomes valuable.

## Related

- [data-and-sync](../architecture/data-and-sync.md)
- [auth](../architecture/auth.md)
