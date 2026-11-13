# CI/CD goals

No pipeline in foundations. Target behavior once scaffold exists:

| Check | Scope |
|-------|--------|
| Build | `:core`, `:apps:android`, `apps/web` |
| Unit tests | KMP grading + TS grading (shared fixtures) |
| Lint / format | Kotlin + web toolchains |
| Deck sample | Validate `docs/samples/cpp-modern.json` against the importer |

## Deploy

- Android: local/debug builds first; store release later.
- Web: static hosting decision deferred (no account/backend required for local-only product).
- iOS: after [OQ-02](../open-questions.md).

## Related

- [Testing](testing.md)
- [ADR 0005](../decisions/0005-monorepo-tooling.md)
