# Android theming from design tokens

Pipeline owner: `design-tokens` skill. Contract: [design-tokens.md](../../../../docs/specs/design-tokens.md). Style: [visual-style-neon-glass.md](../../../../docs/specs/visual-style-neon-glass.md). ADR: [0007](../../../../docs/decisions/0007-design-tokens-ssot.md).

## Wiring

1. Token values live in `design/tokens/` (SSOT).
2. v1: hand-sync into `apps/android/.../theme/` (`Color` / `Theme` / typography). Full generator deferred.
3. Preferred mode: **dark neon-glass** (midnight canvas, electric blue / cyan).

## Consumption

```kotlin
// Prefer theme accessors — not Color(0xFF…) in feature UI
MaterialTheme.colorScheme.primary
```

## Do not

- Hardcode brand hex in composables
- Invent a second Android-only palette that drifts from SSOT
- Reintroduce light Ink Desk or TestHive orange OLED as the default
