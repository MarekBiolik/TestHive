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

## Sample deck

[docs/samples/cpp-modern.json](docs/samples/cpp-modern.json) — modern C++ practice bank for the first product slice.
