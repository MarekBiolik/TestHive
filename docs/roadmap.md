# Roadmap

Phases are sequential for the critical path; some later items may overlap once scaffolding exists.

## Phase 0 — Foundations (this docs set)

- Vision, architecture, ADRs, domain outline, template/grading contract, sample deck, conventions.
- Project steering rules under `.cursor/rules/`.
- Git bootstrap on `main` → [MarekBiolik/TestHive](https://github.com/MarekBiolik/TestHive).

## Phase 1 — Monorepo scaffold

- Gradle multi-project: `core/` (KMP) + `apps/android/`.
- pnpm workspace: `apps/web/` (Vite + React + TypeScript).
- Root orchestration scripts; no app features yet.
- Pin library versions at scaffold time.

## Phase 2 — Android import + card session

- Parse/validate deck JSON (`formatVersion: 1`); reject invalid decks wholly.
- Local deck store + progress keyed by `deckId` + `questionId`.
- Home: list decks, select one or mix, start feed.
- Card loop for all templates; immediate feedback.
- Ship bundled sample [cpp-modern.json](samples/cpp-modern.json) for first-run practice.

## Phase 3 — Web parity on the same contract

- Import, store, session UI on web.
- TypeScript grading aligned with [templates](domain/templates.md); shared fixtures from [grading-cases.json](samples/grading-cases.json).
- Monitor drift ([OQ-05](open-questions.md)).

## Phase 4 — Learning quality

- Close or refine [OQ-03](open-questions.md) (queue → SRS) and [OQ-07](open-questions.md) (mix weights).
- Optional partial credit ([OQ-04](open-questions.md)).

## Phase 5 — iOS (when unblocked)

- Close [OQ-02](open-questions.md); scaffold `apps/ios/` consuming KMP core; parity with Android session.

## Phase 6 — Optional sync / account

- Only if [OQ-01](open-questions.md) is accepted; otherwise remain local-only.

## Later

- Deck editor ([OQ-06](open-questions.md)).
- Design tokens / visual system.
- CI hardening beyond scaffold goals.
