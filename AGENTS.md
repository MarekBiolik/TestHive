# Agent steering — TestHive

Product docs start at [docs/README.md](docs/README.md).

## Agent skills

### Issue tracker

GitHub Issues via `gh`. See `docs/agents/issue-tracker.md`.

### Triage labels

Five roles: `needs-triage`, `needs-info`, `ready-for-agent`, `ready-for-human`, `wontfix`. See `docs/agents/triage-labels.md`.

### Domain docs

Single-context: [docs/domain/glossary.md](docs/domain/glossary.md) and [docs/decisions/](docs/decisions/) (ADRs). See `docs/agents/domain.md`.

### Delivery / UI skills (in-repo)

| Skill | Path |
|-------|------|
| `ui-ux` | `.cursor/skills/ui-ux/` |
| `design-tokens` | `.cursor/skills/design-tokens/` |
| `android-compose` | `.cursor/skills/android-compose/` |
| `android-emulator` | `.cursor/skills/android-emulator/` |
| `core-kmp` | `.cursor/skills/core-kmp/` |
| `platform-delivery` | `.cursor/skills/platform-delivery/` |
| `qa-testing` | `.cursor/skills/qa-testing/` |

Env overview: [docs/conventions/agent-dev-environment.md](docs/conventions/agent-dev-environment.md).

## Delivery loop

After foundations: **issue → branch + PR → focused commits → merge** (see `.cursor/rules/workflow-issue-pr-commit.mdc`). Public artifacts in English. Multi-area slices: `.cursor/rules/platform-delivery.mdc`.
