# Auth

## Current state

**No authentication.** TestHive does not collect accounts, emails, or other PII for product use. Decks and progress stay on the device or in the browser.

## Implications

- No sign-in screens in the v1 product surface.
- No RLS / multi-tenant backend concerns until [OQ-01](../open-questions.md) introduces sync.
- “User” in domain docs means the person using the local install, not an identity record.

## Related

- [ADR 0002](../decisions/0002-local-only-storage.md)
- [Data and sync](data-and-sync.md)
