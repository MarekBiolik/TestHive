# Domain Docs

How the engineering skills should consume this repo's domain documentation when exploring the codebase.

## Before exploring, read these

- **[docs/domain/glossary.md](../domain/glossary.md)** — domain vocabulary
- **[docs/decisions/](../decisions/)** — ADRs that touch the area you're about to work in
- **[docs/domain/templates.md](../domain/templates.md)** — deck templates and grading (when touching import/quiz)

If a file doesn't exist yet, proceed silently unless the task clearly depends on it.

## File structure

```
/
├── docs/
│   ├── domain/
│   │   ├── glossary.md
│   │   ├── data-model.md
│   │   └── templates.md
│   └── decisions/
│       ├── 0001-….md
│       └── …
└── (apps/ and core/ after scaffold)
```

Legacy skill default paths (`GLOSSARY.md` at repo root, `docs/adr/`) are **not** used here — TestHive keeps glossary and ADRs under `docs/` as established in architecture foundations.

## Use the glossary's vocabulary

When your output names a domain concept (in an issue title, a refactor proposal, a hypothesis, a test name), use the term as defined in `docs/domain/glossary.md`. Don't drift to synonyms the glossary explicitly avoids.

## Flag ADR conflicts

If your output contradicts an existing ADR, surface it explicitly rather than silently overriding.
