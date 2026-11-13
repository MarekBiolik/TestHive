# Shared core

## Role

The KMP `core` module owns product rules for mobile clients:

- Deck JSON parse and validate (`formatVersion`, template schema).
- Grading for `single`, `multi`, `order`, `match`, `cloze`; self-report handling for `reveal`.
- Session queue construction (v1: unseen → last-wrong → last-right).
- Progress updates keyed by `deckId` + `questionId`.
- Mix selection: union of questions from selected decks (uniform for v1; [OQ-07](../open-questions.md)).

## Boundary with UI

| In core | In UI |
|---------|--------|
| Is this answer correct? | Animations, colors, haptics |
| Which card is next? | Layout, navigation chrome |
| Is this JSON a valid deck? | File picker, error toast copy |
| Persist progress model | Platform storage adapters behind a core interface |

Public API should be use-case oriented (import deck, start session, submit answer, list decks) — exact signatures at scaffold.

## Platforms consuming core

| Client | Consumes KMP core? |
|--------|--------------------|
| Android | Yes |
| iOS | Yes, when unblocked |
| Web | No — TS copy of contract ([ADR 0006](../decisions/0006-web-grading-in-typescript.md)) |

## Related

- [ADR 0001](../decisions/0001-shared-core-kmp.md)
- [Templates](../domain/templates.md)
- [Clients](clients.md)
