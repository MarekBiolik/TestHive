# Tech stack

Concrete library versions will be pinned at scaffolding (phase 1). Below — technology choices and roles.

## Shared core (Android / later iOS)

| Technology | Role |
|------------|------|
| Kotlin Multiplatform (KMP) | Shared domain, grading, import, progress |
| Kotlin Coroutines / Flow | Async and state streams |
| kotlinx.serialization | Deck JSON and local persistence models |

Decision: [ADR 0001](../decisions/0001-shared-core-kmp.md).

## Android

| Technology | Role |
|------------|------|
| Kotlin | App language |
| Jetpack Compose | UI |
| Android Gradle Plugin | Build |
| `core` as Gradle dependency | Logic |

## iOS (later)

| Technology | Role |
|------------|------|
| Swift | App language |
| SwiftUI | UI |
| Xcode | Build / signing |
| core as XCFramework / SPM from KMP | Logic |

Blocked until [OQ-02](../open-questions.md).

## Web

| Technology | Role |
|------------|------|
| TypeScript | Language |
| React | UI |
| Vite | Tooling / build |
| Thin TS modules | Import + grading per [templates](../domain/templates.md) ([ADR 0006](../decisions/0006-web-grading-in-typescript.md)) |

## Data

| Technology | Role |
|------------|------|
| On-device / in-browser storage | Decks + progress ([ADR 0002](../decisions/0002-local-only-storage.md)) |
| Versioned JSON files | Deck interchange ([ADR 0004](../decisions/0004-deck-format-json.md)) |

Exact store API (DataStore, Room, IndexedDB, etc.) is chosen at scaffold — not foundations.

## Project tooling

Decision: [ADR 0005](../decisions/0005-monorepo-tooling.md).

| Area | Choice | Notes |
|------|--------|--------|
| Monorepo | Hybrid: **Gradle** (`core` + `apps/android`) + **pnpm** (`apps/web`) | No Nx/Turborepo at start |
| Orchestration | Thin root scripts | Invoke Gradle / pnpm |
| Core versioning | Source-linked in the same commit | SemVer publish only if needed later |
| CI | Build core + clients + lint; run grading fixtures | [ci-cd](../conventions/ci-cd.md) |
| Tests | Unit grading in core + TS; shared fixtures | [testing](../conventions/testing.md) |
| JS package manager | **pnpm** | Workspace = `apps/web` at start |

## Deliberately not chosen (for now)

- Flutter / React Native as primary UI — native UI per platform ([ADR 0003](../decisions/0003-native-ui-per-platform.md)).
- Supabase / Firebase / custom backend — local-only ([ADR 0002](../decisions/0002-local-only-storage.md)).
- KMP→JS/Wasm for web on day 1 — deferred ([ADR 0006](../decisions/0006-web-grading-in-typescript.md), [OQ-05](../open-questions.md)).
- YAML or Markdown as the interchange format — JSON chosen ([ADR 0004](../decisions/0004-deck-format-json.md)).
