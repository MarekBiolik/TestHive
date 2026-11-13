# ADR 0005 — Monorepo: Gradle + pnpm

- **Status:** accepted
- **Date:** 2026-11-13
- **Decision makers:** Marek (+ architecture foundations session)

## Context

One product, multiple clients, shared mobile core. Need a layout that fits KMP/Android and a standard React web toolchain without premature orchestrators.

## Decision

**Hybrid monorepo without Nx/Turborepo:**

1. **Gradle multi-project** covers `core/` (KMP) and `apps/android/` (Compose). Future iOS consumes a KMP artifact; Xcode project under `apps/ios/` when unblocked.
2. **`apps/web/`** is a **pnpm** workspace + Vite + React/TS — outside the Gradle tree.
3. **Root** holds thin orchestration scripts calling Gradle and pnpm.
4. **`core` versioning:** source-linked in the same commit until an external consumer needs SemVer publish.

## Alternatives considered

1. **One mega-Gradle including web** — rejected: poor web DX.
2. **Nx / Turborepo over the whole repo** — rejected at start; three ecosystems, little gain for solo start.
3. **Polyrepo** — rejected: frequent cross-cutting docs/core changes.
4. **Hybrid Gradle + pnpm** — chosen (same shape as GymManager).

## Consequences

- Positive: natural Android/KMP path; standard web tooling; simple mental model.
- Negative: two (later three) build systems; root scripts maintained by hand.
- Later: optional Turborepo under web only if the JS graph grows.

## Related

- [overview](../architecture/overview.md)
- [tech-stack](../architecture/tech-stack.md)
