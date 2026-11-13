# Spec: design tokens contract

**Status:** accepted (v1 hand-sync)  
**Decision:** [ADR 0007](../decisions/0007-design-tokens-ssot.md)  
**First theme:** [visual-style-neon-glass](visual-style-neon-glass.md)

## Layers

1. **Primitive** — raw color/type/space scales (`color.blue.500`, `space.4`).
2. **Semantic** — roles (`color.bg.canvas`, `color.action.primary`).
3. **Component** (optional later) — button/card recipes.

## Ownership

| Path | Role |
|------|------|
| `design/tokens/` | SSOT (JSON) |
| `apps/android/.../theme/` | Hand-synced Kotlin Theme (v1) |
| Generator + CI regen-clean | Deferred |

## Naming

Follow `.cursor/skills/design-tokens/references/naming.md`. Prefer `color.*`, `type.*`, `space.*`, `radius.*`, `effect.glow.*`.

## Client rules

- UI reads MaterialTheme / app theme accessors — **no** feature-level hex.
- Changing neon-glass values → edit SSOT, then sync Android Theme in the same PR (or immediate follow-up).

## Related

- [ADR 0003](../decisions/0003-native-ui-per-platform.md) — native UI unchanged
- [ADR 0007](../decisions/0007-design-tokens-ssot.md)
