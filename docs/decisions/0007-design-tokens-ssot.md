# ADR 0007 — Design tokens SSOT (Neon Glass)

- **Status:** accepted
- **Date:** 2026-11-13
- **Decision makers:** Marek (+ UX redesign session)

## Context

Android UI shipped as default Material3 and looked unfinished. We need a single appearance source for Neon Glass Learning across clients without locking hex into every composable. GymManager uses a token SSOT + generator; TestHive is smaller and local-only.

## Decision

1. Visual values live in **`design/tokens/`** as the SSOT.
2. First theme is **neon-glass** ([visual-style-neon-glass](../specs/visual-style-neon-glass.md)).
3. **v1 sync:** hand-map SSOT into Android Compose `Theme` (documented). Full Style Dictionary / CI regen-clean is deferred until drift hurts.
4. This ADR does **not** override native UI per platform ([ADR 0003](0003-native-ui-per-platform.md)).

## Alternatives considered

1. **Hardcode hex in Compose** — rejected: drift and redesign pain.
2. **Full generator from day 1** — deferred: overhead for one Android client.
3. **SSOT + hand-sync** — chosen.

## Consequences

- Positive: one palette contract; UX/skills point at docs + tokens.
- Negative: hand-sync can drift until a generator exists.
- Later: thin Node generator + CI diff-clean when web/iOS join.

## Related

- [design-tokens](../specs/design-tokens.md)
- [visual-style-neon-glass](../specs/visual-style-neon-glass.md)
