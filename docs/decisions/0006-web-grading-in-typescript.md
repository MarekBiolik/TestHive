# ADR 0006 — Web grading in TypeScript

- **Status:** accepted
- **Date:** 2026-11-13
- **Decision makers:** Marek (+ architecture foundations session)

## Context

Android consumes KMP core ([ADR 0001](0001-shared-core-kmp.md)). Web needs the same deck import and grading behavior. Unlike GymManager, there is no Postgres/RLS layer to host shared rules ([ADR 0002](0002-local-only-storage.md)) — grading must run in the client. Compiling KMP to JS/Wasm adds toolchain risk for a solo start.

## Decision

`apps/web` implements **import validation and grading in TypeScript**, following [templates](../domain/templates.md) and shared fixtures ([grading-cases.json](../samples/grading-cases.json)). We do **not** add a JS/Wasm target to `core` for day 1. Drift is an accepted risk; persistent failures reopen [OQ-05](../open-questions.md).

## Alternatives considered

1. **KMP→JS/Wasm from React** — one grading binary. Rejected for day 1: toolchain/DX cost before the product loop exists.
2. **Android-first only; web waits** — rejected: owner wants web documented and built in parallel.
3. **TS grading copy + shared fixtures** — chosen: fastest web path with an explicit canary.

## Consequences

- Positive: ordinary Vite/React scaffold; web can ship on the same contract without KMP-JS.
- Negative: two grading implementations can disagree; discipline and CI fixtures are mandatory.
- What to refine later: if fixtures diverge in production, prefer shared binary evaluation (OQ-05) over growing a third copy.

## Related

- [clients](../architecture/clients.md)
- [testing](../conventions/testing.md)
- [ADR 0001](0001-shared-core-kmp.md)
