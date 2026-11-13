# Templates and grading (`formatVersion: 1`)

Normative grading rules for Android (KMP) and web (TypeScript). Samples: [cpp-modern.json](../samples/cpp-modern.json), fixtures: [grading-cases.json](../samples/grading-cases.json).

## Card loop (all graded templates)

1. Show `prompt`, optional `code`, and answer controls together (no separate “think then reveal” step).
2. On submit, grade immediately; show `explanation` (and the key on wrong answers).
3. Correct answers auto-advance after a short beat; wrong answers wait for a single Continue.
4. Leave a session via a top close control — not a second bottom CTA competing with Continue.

True/false is `single` with two options. Optional `code` may appear on any template.

`order` rows are reordered by dragging whole fields (not steppers).

## Import

Unknown `type`, missing required fields, duplicate option ids, or answer keys that do not reference existing options → **reject the entire deck file**.

---

## `single`

Exactly one correct option.

**Payload**

- `options`: `[{ id, text }]` (at least 2)
- `answer`: option `id`

**Grade:** submitted option id equals `answer`.

---

## `multi`

All-or-nothing set equality. No partial credit ([OQ-04](../open-questions.md)).

**Payload**

- `options`: `[{ id, text }]`
- `answer`: array of option ids (non-empty, unique, subset of options)

**Grade:** submitted set of ids equals `answer` as sets (order ignored).

---

## `order`

User permutes all items; prompt states the criterion (chronological, size, speed, …).

**Payload**

- `items`: `[{ id, text }]` (at least 2)
- `answer`: array of all item ids in correct order

**Grade:** submitted id sequence equals `answer` exactly. No partial credit.

---

## `match`

Each left item pairs with exactly one right item.

**Payload**

- `left`: `[{ id, text }]`
- `right`: `[{ id, text }]`
- `answer`: map/object `leftId → rightId` covering every left id; right ids unique

**Grade:** for every left id, submitted right id equals `answer[leftId]`.

---

## `cloze`

Blanks marked in `prompt` as `{{blankId}}`.

**Payload**

- `blanks`: map `blankId → string[]` (accepted answers; non-empty)
- `choices`: optional distractor strings for chip UI

**Normalize** each submitted blank: trim whitespace, compare case-insensitively.

**Grade:** every blank’s normalized submission is in that blank’s accepted list (also normalized the same way).

---

## `reveal`

Self-graded open answer. App cannot compare objectively.

**Payload**

- `answerText`: model answer shown after second interaction

**Loop:** second interaction shows `answerText` (and then `explanation` as usual). User taps “knew it” / “didn’t”. That choice is the grade.

---

## Session queue (v1)

Among eligible questions in the selected deck(s): prefer **unseen**, then **last wrong**, then **last right**. Full SRS is [OQ-03](../open-questions.md).
