# Spec: Neon Glass Learning visual style

**Status:** accepted for Android v1 redesign  
**Style id:** `neon-glass`  
**Token contract:** [design-tokens](design-tokens.md) · [ADR 0007](../decisions/0007-design-tokens-ssot.md)

Adult, premium, lively learning UI: dark midnight canvas, electric blue / cyan accents, frosted glass panels, restrained neon glow. Mood reference: premium dark SaaS (e.g. SOAR-like collage) adapted for quiz practice — not Duolingo-cute, not sterile light admin, not GymManager orange OLED.

Product display name may change later; this style does **not** depend on “TestHive” branding.

## Goals

1. Dark-first neon-glass look that feels alive for adults.
2. Clear card-loop affordances (think → controls → grade → next).
3. Semantic token roles for palette, type, space, radius, glow.
4. Accessibility floor on dark surfaces.

## Non-goals

| Out |
|-----|
| Light-mode finalization (defer) |
| Full Style Dictionary CI (hand-sync v1) |
| Product rename / new logo |
| Web/iOS theme parity in this slice |

## Preferred mode

**Dark neon-glass** as default. Light mode not in v1.

## Palette (illustrative → SSOT)

### Primitives

| Token | Hex | Role |
|-------|-----|------|
| `color.blue.500` | `#3B82F6` | Electric blue primary |
| `color.cyan.400` | `#22D3EE` | Cyan highlight / selection |
| `color.navy.950` | `#020617` | Deep canvas |
| `color.navy.900` | `#0B1220` | Raised surface |
| `color.navy.800` | `#111827` | Glass fill base |

### Semantic (dark)

| Token | Value | Role |
|-------|-------|------|
| `color.bg.canvas` | `#020617` | App background |
| `color.bg.surface` | `#0B1220` | Solid panels |
| `color.bg.glass` | `#111827` @ ~72% opacity | Frosted cards |
| `color.text.primary` | `#F8FAFC` | Titles / body |
| `color.text.secondary` | `#94A3B8` | Meta |
| `color.action.primary` | `{color.blue.500}` | CTA fill |
| `color.action.glow` | cyan/blue soft outer glow | Focus / selected / CTA emphasis |
| `color.border.glass` | `#334155` @ 40% | Glass edge |
| `color.feedback.success` | `#34D399` | Correct |
| `color.feedback.error` | `#F87171` | Wrong |

## Glow rules (restrained)

Use soft outer glow **only** for: focused/selected answer, primary CTA, success halo on Correct. Do not glow every surface.

## Type

Sans UI (platform default OK until brand typeface chosen). Code: monospace.

| Token | Size | Use |
|-------|------|-----|
| `type.title.lg` | 28 / 34 | Screen titles |
| `type.title.md` | 20 / 28 | Question stem |
| `type.body.md` | 16 / 24 | Body / options |
| `type.code.sm` | 13 / 20 | Code blocks |
| `type.label.sm` | 12 / 16 | Progress meta |

## Space (4px base)

`4, 8, 12, 16, 20, 24, 32, 40, 48` → `space.1` … `space.12`.

## Radius

| Token | px | Use |
|-------|-----|-----|
| `radius.md` | 12 | Buttons / chips |
| `radius.lg` | 16 | Glass cards |
| `radius.xl` | 24 | Hero panels |

Avoid stadium `rounded-full` for primary CTAs — prefer `radius.md`–`lg`.

## Layout intent

- **Home:** dark canvas; glass deck rows; strong electric-blue Start CTA; brand/wordmark secondary until rename.
- **Session:** one glass question card; code in darker inset glass; answer rows as large tappable glass strips; selected = cyan edge + soft glow.
- **Feedback:** Correct/Wrong with success/error color + short explanation; Next as primary CTA.

## Card loop

Matches [vision](../00-vision.md) / [templates](../domain/templates.md): think → reveal controls → submit → immediate grade → next.

## Accessibility

- Body text on canvas ≥ 4.5:1.
- Do not use glow/color alone for state (pair with label / icon / border).
- Touch targets ≥ 48dp where practical.
- Respect reduced motion when adding celebratory motion later.

## Related

- [design-tokens](design-tokens.md)
- Samples (Cursor assets): `learn-style-soar-*.jpg`
