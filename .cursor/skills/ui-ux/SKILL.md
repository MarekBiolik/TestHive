---
name: ui-ux
description: >-
  Shape TestHive cross-platform UX contracts: flows, layout intent,
  accessibility, consistency — appearance from design-tokens SSOT and the
  Neon Glass Learning visual style. Use when designing or reviewing
  interaction/UX, writing UX specs, a11y checks, or the user says UX / UI / flows.
---

# UI / UX

Own **cross-platform UX intent**: flows, information architecture, layout purpose, accessibility expectations, consistency notes. Do **not** own domain rules (→ `core-kmp`), platform implementation (→ `android-compose`, later web/iOS skills), or the token **pipeline** (→ `design-tokens`).

**Appearance SSOT:** [design-tokens](../design-tokens/SKILL.md) + [Neon Glass visual style](../../../docs/specs/visual-style-neon-glass.md) + [ADR 0007](../../../docs/decisions/0007-design-tokens-ssot.md). UX proposes layout/flow; **palette, type, space, motion values** change via tokens — not by hardcoding hex in specs or apps.

Native UI per platform: [ADR 0003](../../../docs/decisions/0003-native-ui-per-platform.md). Product rename is deferred — use neutral learning copy until a rename issue lands.

Deep topics: [references/flows.md](references/flows.md), [references/a11y-floor.md](references/a11y-floor.md). Env: [references/env-checklist.md](references/env-checklist.md).

## Scope split

| This skill | Not this skill |
|------------|----------------|
| Flow / wire intent, screen jobs, empty/error states | Compose / React implementation |
| A11y expectations (contrast, focus, labels, motion) | Editing `design/tokens/` → `design-tokens` |
| Consistency across clients | Domain grading / import → `core-kmp` |
| Specs, prototypes, review notes | Emulator smoke → `android-emulator` / `qa-testing` |

Honour **Neon Glass** (dark midnight, electric blue/cyan, frosted glass, restrained glow). Adult lively learning — not Duolingo-cute, not sterile admin gray.

## Card-loop UX (product)

1. Prompt (+ code) and answer controls together  
2. Submit → immediate grade + explanation (stronger wrong-state; correct auto-advances)  
3. One sticky primary bottom action; leave via top close  
4. `order`: drag whole rows

Home: list decks, select one or mix, strong CTA to start. Session end: adult rewarding summary (stats + Back to decks), not dual sad Exit.

## Workflow

```
UX slice:
- [ ] 1. Clarify surface and constraints
- [ ] 2. Contract or prototype
- [ ] 3. A11y floor
- [ ] 4. Hand off to platforms
```

### 1. Clarify surface and constraints

Which clients? Local-only ([ADR 0002](../../../docs/decisions/0002-local-only-storage.md))? Appearance from neon-glass + tokens.

**Done when:** platforms and open forks are listed.

### 2. Contract or prototype

Short UX contract (flow steps, one job per screen, primary actions, empty/error). Reference semantic token **roles**, not raw hex as the long-term contract.

**Done when:** platforms can implement without inventing a private palette.

### 3. A11y floor

See [references/a11y-floor.md](references/a11y-floor.md). On dark neon-glass: body contrast ≥ 4.5:1; do not rely on glow/color alone for state.

### 4. Hand off to platforms

Point at `android-compose` (and later web) + `design-tokens` for value changes.

## Guardrails

- No silent branding lock-in — use token SSOT
- No domain logic in UX specs
- No premature i18n architecture
