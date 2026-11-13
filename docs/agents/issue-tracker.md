# Issue tracker: GitHub

Issues and specs for this repo live as GitHub issues. Use the `gh` CLI for all operations.

Repo: `MarekBiolik/TestHive` (`origin`).

## Conventions

- **Create an issue**: `gh issue create --title "..." --body-file ...` (prefer `--body-file` on Windows PowerShell).
- **Read an issue**: `gh issue view <number> --json number,title,body,labels,comments`.
- **List issues**: `gh issue list --state open --json number,title,body,labels`.
- **Make an issue a sub-issue of a parent**: `gh issue edit <parent> --add-sub-issue <child>` (`gh` 2.94+), or put `Part of #<parent>` at the top of the child body.
- **Comment**: `gh issue comment <number> --body "..."`
- **Labels**: `gh issue edit <number> --add-label "..."` / `--remove-label "..."`
- **Close**: `gh issue close <number> --comment "..."`

## Pull requests as a triage surface

**PRs as a request surface: no.**

## When a skill says "publish to the issue tracker"

Create a GitHub issue.

## When a skill says "fetch the relevant ticket"

Read it as in **Read an issue** above.

## Wayfinding operations

Used by `/wayfinder`. The **map** is a single issue with **child** issues as tickets.

- **Map**: issue labelled `wayfinder:map`.
- **Child ticket**: sub-issue of the map, or `Part of #<map>` in the body.
- **Blocking**: prefer GitHub native dependencies; else `Blocked by: #<n>` / `Depends on #N` in the body.
- **Claim**: `gh issue edit <n> --add-assignee @me`.
- **Resolve**: comment with the answer, then close.
