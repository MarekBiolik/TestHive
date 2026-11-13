# Product vision

## What this is

**TestHive** turns prepared knowledge decks into a pleasant, continuous quiz feed — a doomscrolling substitute for practice. The user imports one or more question banks, picks a deck or a mix on the home screen, and answers in short cards with immediate correct/incorrect feedback (Duolingo-like).

The product is intentionally not feature-heavy. The ambition is **professional build and evolution**: a shared mobile core, clear boundaries, documented decisions, and predictable growth across Android and web (iOS later).

## Platforms

| Platform | Form | Status |
|----------|------|--------|
| Android | Native app (Jetpack Compose) | Primary delivery target |
| Web | Web app (React + TypeScript) | Parallel client; own grading copy ([ADR 0006](decisions/0006-web-grading-in-typescript.md)) |
| iOS | Native app (SwiftUI) | Goal, **blocked** until a test device/machine exists ([OQ-02](open-questions.md)) |

## Product shape (outline)

1. **Deck library** — import versioned JSON decks; list them on the home screen; study one deck or a mix of selected decks.
2. **Card loop** — prompt first (think time), second interaction reveals answer controls, submit → immediate grade + explanation → next card.
3. **Question templates** — `single`, `multi`, `order`, `match`, `cloze`, `reveal` ([templates](domain/templates.md)).
4. **Local progress** — per-question stats on device; removing a deck clears that deck’s progress.

## Goals

- Replace idle scrolling with short, satisfying knowledge practice.
- Shared domain rules for Android (and later iOS) in one KMP core; thin native UIs.
- Strict, versioned deck contract so banks are portable and import-safe.
- Immediate feedback after every graded answer.
- Documentation and ADRs as the foundation before application code.

## Non-goals (for now)

- Accounts, cloud sync, or a hosted backend ([ADR 0002](decisions/0002-local-only-storage.md); [OQ-01](open-questions.md)).
- Full spaced-repetition scheduling ([OQ-03](open-questions.md)) — v1 uses a simple unseen / last-wrong / last-right queue.
- In-app deck editor ([OQ-06](open-questions.md)).
- Product AI agents / skills / workflows.
- Final UI/UX polish and branding — foundations and scaffolding first.
- Shipping an iOS app while testing is blocked.

## Target user (outline)

Someone who wants to practice a topic (starting with modern C++) in short sessions on phone or laptop, importing their own prepared decks, without signing up.
