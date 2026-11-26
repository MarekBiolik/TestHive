#!/usr/bin/env python3
"""Lightweight formatVersion:1 deck validation (mirrors core importer rules)."""
from __future__ import annotations

import json
import sys
from pathlib import Path


def err(msg: str) -> None:
    raise SystemExit(msg)


def validate(path: Path) -> None:
    deck = json.loads(path.read_text(encoding="utf-8"))
    if deck.get("formatVersion") != 1:
        err(f"{path}: formatVersion must be 1")
    for key in ("id", "title", "language", "questions"):
        if key not in deck:
            err(f"{path}: missing {key}")
    questions = deck["questions"]
    if not questions:
        err(f"{path}: empty questions")
    ids = set()
    for q in questions:
        qid = q.get("id")
        if not qid or qid in ids:
            err(f"{path}: bad/duplicate question id {qid!r}")
        ids.add(qid)
        t = q.get("type")
        if t == "single":
            opts = {o["id"] for o in q["options"]}
            if len(opts) < 2 or q["answer"] not in opts:
                err(f"{path}:{qid}: bad single")
        elif t == "multi":
            opts = {o["id"] for o in q["options"]}
            ans = q["answer"]
            if not ans or len(ans) != len(set(ans)) or not set(ans) <= opts:
                err(f"{path}:{qid}: bad multi")
        elif t == "order":
            items = [i["id"] for i in q["items"]]
            if len(items) < 2 or sorted(q["answer"]) != sorted(items) or len(q["answer"]) != len(items):
                err(f"{path}:{qid}: bad order")
        elif t == "match":
            left = {i["id"] for i in q["left"]}
            right = {i["id"] for i in q["right"]}
            ans = q["answer"]
            if set(ans) != left or len(set(ans.values())) != len(ans) or not set(ans.values()) <= right:
                err(f"{path}:{qid}: bad match")
        elif t == "cloze":
            blanks = q["blanks"]
            if not blanks or any(not v for v in blanks.values()):
                err(f"{path}:{qid}: bad cloze")
            for bid in blanks:
                if "{{" + bid + "}}" not in q["prompt"]:
                    err(f"{path}:{qid}: blank {bid} missing in prompt")
        elif t == "reveal":
            if not q.get("answerText"):
                err(f"{path}:{qid}: bad reveal")
        else:
            err(f"{path}:{qid}: unknown type {t}")
    print(f"OK {path} ({len(questions)} questions)")


def main() -> None:
    roots = [Path(r"E:\Work\Testownik\docs\samples"), Path(r"E:\Work\Testownik\docs\decks")]
    files = []
    for root in roots:
        if root.exists():
            files.extend(sorted(root.glob("*.json")))
    if not files:
        err("no decks found")
    for f in files:
        if f.name == "grading-cases.json":
            continue
        validate(f)


if __name__ == "__main__":
    main()
