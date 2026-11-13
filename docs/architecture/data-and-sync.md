# Data and sync

## Source of truth

| Data | Source of truth | Location |
|------|-----------------|----------|
| Deck content | Imported JSON file (after successful validation) | Copied into local app storage |
| Progress / attempts | App runtime | Local store per device or browser |
| Contract | Docs + samples | Repository `docs/` |

There is **no** server-side source of truth in foundations ([ADR 0002](../decisions/0002-local-only-storage.md)).

## Sync

Cross-device sync is **out of scope**. Android and web do not share progress. Revisit under [OQ-01](../open-questions.md).

## Import rules

- Accept only `formatVersion` known to the client (v1 = `1`).
- Unknown template, missing required fields, or inconsistent answer keys → **reject the entire file**.
- Re-importing the same `deckId` replaces deck content; progress policy for deleted/changed questions is defined at implementation (prefer: drop progress for missing `questionId`s).

## Progress model (outline)

Key: `deckId` + `questionId`.

Fields (conceptual): `correctCount`, `wrongCount`, `lastResult`, `lastAnsweredAt`.

Removing a deck removes its progress rows.

## Related

- [Data model](../domain/data-model.md)
- [ADR 0004](../decisions/0004-deck-format-json.md)
