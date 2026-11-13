# ADR 0004 — Deck interchange: versioned JSON

- **Status:** accepted
- **Date:** 2026-11-13
- **Decision makers:** Marek (+ architecture foundations session)

## Context

Users supply prepared question banks. The app needs a strict, versioned interchange format that supports multiple templates (including optional code blocks for C++). Authors may write decks by hand initially ([OQ-06](../open-questions.md)).

## Decision

Decks are **JSON files** with `formatVersion: 1`. Field names and grading semantics are defined in [templates](../domain/templates.md). Import is all-or-nothing on validation failure. Repo samples are English; content `language` may be any BCP 47 tag.

## Alternatives considered

1. **YAML** — nicer multiline C++ for hand authors. Rejected for foundations: weaker ecosystem validation defaults; JSON is stricter and ubiquitous on both KMP and TS.
2. **Markdown + custom parser** — pleasant writing UX. Rejected: easy to break import; higher parser cost.
3. **Versioned JSON** — chosen: clear contract, easy fixtures, good kotlinx.serialization / TS typing fit.

## Consequences

- Positive: one schema; fixtures and samples are ordinary JSON; reject-bad-file is simple.
- Negative: C++ in strings needs escaping; hand-editing is less cozy than YAML/Markdown.
- Later: optional editor (OQ-06); `formatVersion` bumps via new ADR/spec.

## Related

- [templates](../domain/templates.md)
- [data-and-sync](../architecture/data-and-sync.md)
- [samples/cpp-modern.json](../samples/cpp-modern.json)
