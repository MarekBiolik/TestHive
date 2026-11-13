# TestHive documentation

Reading order for newcomers and agents.

1. [Product vision](00-vision.md)
2. [Architecture overview](architecture/overview.md)
3. [Tech stack](architecture/tech-stack.md)
4. [Shared core](architecture/shared-core.md) · [Clients](architecture/clients.md) · [Data and sync](architecture/data-and-sync.md) · [Auth](architecture/auth.md)
5. [Domain glossary](domain/glossary.md) · [Data model](domain/data-model.md) · [Templates and grading](domain/templates.md)
6. [Open questions](open-questions.md)
7. [Roadmap](roadmap.md)
8. [Decisions (ADRs)](decisions/)
9. [Conventions](conventions/)
10. Samples: [cpp-modern.json](samples/cpp-modern.json) · [grading-cases.json](samples/grading-cases.json)

## Specs

- [Neon Glass visual style](specs/visual-style-neon-glass.md)
- [Design tokens contract](specs/design-tokens.md)

## ADR index

| ADR | Title |
|-----|--------|
| [0001](decisions/0001-shared-core-kmp.md) | Shared core: Kotlin Multiplatform |
| [0002](decisions/0002-local-only-storage.md) | Local-only decks and progress |
| [0003](decisions/0003-native-ui-per-platform.md) | Native UI per platform |
| [0004](decisions/0004-deck-format-json.md) | Deck interchange: versioned JSON |
| [0005](decisions/0005-monorepo-tooling.md) | Monorepo: Gradle + pnpm |
| [0006](decisions/0006-web-grading-in-typescript.md) | Web grading in TypeScript |
| [0007](decisions/0007-design-tokens-ssot.md) | Design tokens SSOT (Neon Glass) |

## Conventions

- [Docs lifecycle](conventions/docs-lifecycle.md)
- [Coding standards](conventions/coding-standards.md)
- [Git workflow](conventions/git-workflow.md)
- [Testing](conventions/testing.md)
- [CI/CD](conventions/ci-cd.md)
- [Agent dev environment](conventions/agent-dev-environment.md)
