# Coding standards

Foundations only — detailed style pins land with scaffold.

## Seams

- Business rules and grading live in `core/` (mobile) or the thin documented TS grading module (web) — not in UI widgets.
- Platform storage behind interfaces owned by core / web data layer.
- Deck JSON field names match [templates](../domain/templates.md); do not invent a second schema in code comments.

## Languages

| Area | Language |
|------|----------|
| Core | Kotlin |
| Android UI | Kotlin + Compose |
| Web | TypeScript + React |
| iOS (later) | Swift + SwiftUI |

## Formatting (goals)

- Kotlin: ktlint / Spotless (pin at scaffold).
- Web: ESLint + Prettier (pin at scaffold).
- Swift: SwiftFormat when iOS exists.

## Identifiers and comments

English in committed code ([english-public-artifacts](../../.cursor/rules/english-public-artifacts.mdc)). App UI copy language is a later product decision.
