# Clients

## Android

- Jetpack Compose UI.
- Depends on `:core` for import, grading, session, progress.
- First vertical slice after scaffold: import + card loop + home deck list.

## Web

- React + TypeScript + Vite.
- Does **not** link KMP. Implements import/grading against the same docs and fixtures ([ADR 0006](../decisions/0006-web-grading-in-typescript.md)).
- Parallel product surface: home, import, card loop, local progress in the browser.

## iOS

- SwiftUI when [OQ-02](../open-questions.md) closes.
- Consumes KMP framework; no separate domain rewrite.

## Risky interop

| Risk | Mitigation |
|------|------------|
| Android vs web grade the same answer differently | Shared [templates](../domain/templates.md) + [grading-cases.json](../samples/grading-cases.json); CI on both sides later |
| Deck authors use Polish or other languages | Allowed in content fields; repo samples stay English |
| File size / huge decks | Defer; keep strict validation first |

## Related

- [ADR 0003](../decisions/0003-native-ui-per-platform.md)
- [Overview](overview.md)
