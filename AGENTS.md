# Agent steering — TestHive

Product docs start at [docs/README.md](docs/README.md).

## Agent skills

### Issue tracker

GitHub Issues via `gh`. See `docs/agents/issue-tracker.md`.

### Triage labels

Five roles: `needs-triage`, `needs-info`, `ready-for-agent`, `ready-for-human`, `wontfix`. See `docs/agents/triage-labels.md`.

### Domain docs

Single-context: [docs/domain/glossary.md](docs/domain/glossary.md) and [docs/decisions/](docs/decisions/) (ADRs). See `docs/agents/domain.md`.

## Delivery loop

After foundations: **issue → branch + PR → focused commits → merge** (see `.cursor/rules/workflow-issue-pr-commit.mdc`). Public artifacts in English.
