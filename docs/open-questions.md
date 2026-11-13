# Open questions

| Id | Topic | Status | Close-by phase | Default lean |
|----|--------|--------|----------------|--------------|
| OQ-01 | Sync / account between Android and web | open | Phase 6 | Stay local-only until one-device product feels good |
| OQ-02 | iOS unblocking (device / machine for testing) | open | Phase 5 | Keep iOS out of scaffold until testable |
| OQ-03 | Review algorithm beyond unseen / wrong / right | open | Phase 4 | Simple queue for v1; SRS later |
| OQ-04 | Partial credit for multi / order | open | Phase 4 | All-or-nothing (as in templates v1) |
| OQ-05 | Revisit KMP→JS/Wasm for web if grading drifts | open | Phase 3+ | Stay on TS grading + shared fixtures |
| OQ-06 | In-app deck editor vs hand-written JSON | open | Later | Hand-written JSON first |
| OQ-07 | Mix weighting when decks differ wildly in size | open | Phase 4 | Uniform draw across selected questions for v1 |

## Notes

### OQ-01 — Sync / account

Local-only is accepted for foundations ([ADR 0002](decisions/0002-local-only-storage.md)). Revisit when multi-device progress matters more than signup friction.

### OQ-02 — iOS

Architecture assumes SwiftUI + KMP later. No `apps/ios` until the owner can run builds and tests.

### OQ-05 — Web grading drift

[ADR 0006](decisions/0006-web-grading-in-typescript.md) accepts a TypeScript grading copy. Shared [grading-cases.json](samples/grading-cases.json) is the early canary; persistent failures may justify shared binary evaluation.
