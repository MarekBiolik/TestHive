# TestHive

Doomscrolling substitute for knowledge practice: import question decks, study one deck or a mix, get immediate feedback.

Architecture and decisions: [docs/README.md](docs/README.md).

## Layout

| Path | Role |
|------|------|
| `core/` | Kotlin Multiplatform shared logic |
| `apps/android/` | Jetpack Compose client |
| `apps/web/` | React + TypeScript client |
| `docs/` | Vision, ADRs, samples |

## Prerequisites

- JDK 17+
- Android SDK (`local.properties` → `sdk.dir=…`)
- Node.js 20+ and [pnpm](https://pnpm.io/)

## Build

```bash
# Android / core
./gradlew :core:assemble :apps:android:assembleDebug

# Web
pnpm install
pnpm web:build
```

On Windows use `.\gradlew.bat` instead of `./gradlew`.

## Question banks

| Path | Role |
|------|------|
| [docs/decks/cpp-senior-interview.json](docs/decks/cpp-senior-interview.json) | Bundled product bank (~200 senior C++ interview cards) |
| [docs/samples/cpp-modern.json](docs/samples/cpp-modern.json) | Small importer/CI fixture (all templates) |

Android seeds every `assets/decks/*.json` into local storage, and can **Import deck JSON** at runtime without rebuilding. Add another bank by dropping a `formatVersion: 1` file into `docs/decks/` + `assets/decks/` (or importing on device). See [docs/architecture/data-and-sync.md](docs/architecture/data-and-sync.md).
