---
name: design-tokens
description: >-
  Own TestHive design-token SSOT under design/tokens/ and theme outputs for
  clients. Use when editing token values, theme wiring, or the user says
  design tokens / neon-glass palette / theme tokens.
---

# Design tokens

Own the **visual value pipeline**: language-neutral SSOT → (hand-sync or thin generator) → native theme artifacts. Do **not** implement screens (→ `android-compose`) or UX flows (→ `ui-ux`).

- [ADR 0007](../../../docs/decisions/0007-design-tokens-ssot.md)
- [design-tokens contract](../../../docs/specs/design-tokens.md)
- [Neon Glass visual style](../../../docs/specs/visual-style-neon-glass.md)

References: [token-architecture](references/token-architecture.md), [naming](references/naming.md), [platform-outputs](references/platform-outputs.md).

## Ownership

| Path | Role |
|------|------|
| `design/tokens/` | SSOT (primitive → semantic) |
| Generated / hand-synced theme under `apps/android/` | Consume only; no parallel palette |
| Full Style Dictionary CI | Deferred until hand-sync hurts |

## Workflow

```
Token slice:
- [ ] 1. Confirm change belongs in SSOT
- [ ] 2. Edit design/tokens
- [ ] 3. Sync Android Theme
- [ ] 4. Verify contrast / smoke
```

## Guardrails

- Platforms consume SSOT values — do not invent hex in UI PRs
- Neon-glass is the first theme; do not silently reintroduce light Ink Desk or Gym orange OLED
