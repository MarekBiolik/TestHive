# Testing

## Priority

1. **Grading purity** — unit tests for every template against [grading-cases.json](../samples/grading-cases.json) in KMP and TypeScript.
2. **Import validation** — reject bad `formatVersion`, unknown types, inconsistent keys; accept [cpp-modern.json](../samples/cpp-modern.json).
3. **Session queue** — unseen / last-wrong / last-right ordering.
4. **UI** — smoke flows later (import → answer → feedback); not a foundations deliverable.

## Shared fixtures

`docs/samples/grading-cases.json` is the cross-client canary. Both implementations must agree. Persistent drift → revisit [OQ-05](../open-questions.md).

## What not to over-test early

- Pixel-perfect Compose/React snapshots.
- Full emulator/browser E2E before the card loop exists.
