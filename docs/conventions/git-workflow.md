# Git workflow

## Remote

| Item | Value |
|------|--------|
| Origin | `git@github.com:MarekBiolik/TestHive.git` |
| Default branch | `main` |
| Hosting | GitHub (`gh` for issues/PRs) |

Local working directory may remain named `Testownik`; the product and remote are **TestHive**.

## Loop after foundations

1. Issue (English title/body, acceptance checkboxes).
2. Feature branch + early PR.
3. Focused commits: `type(#ISSUE): subject`, then with `(#PR)` once known.
4. Merge only when the user asks (prefer squash).
5. Cleanup feature branch / worktree after merge.

See `.cursor/rules/workflow-issue-pr-commit.mdc` and `commit-after-steps.mdc`.

## Commit messages

- Conventional Commits, English, imperative, explain **why**.
- Never add `Co-authored-by` for the agent.
- Do not commit secrets, `node_modules`, or build outputs.

## Push

- Feature-branch push for open PRs is OK when working via PRs.
- Do not force-push shared history or push to `main` without an explicit request (bootstrap push of foundations was authorized in that plan).
