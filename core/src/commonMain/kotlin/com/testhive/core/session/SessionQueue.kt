package com.testhive.core.session

import com.testhive.core.domain.LastResult
import com.testhive.core.domain.QuestionProgress
import com.testhive.core.domain.QuestionRef

/**
 * v1 queue: unseen → last wrong → last right ([docs/domain/templates.md](templates)).
 */
object SessionQueue {
    fun build(
        questions: List<QuestionRef>,
        progress: List<QuestionProgress>,
    ): List<QuestionRef> {
        val byKey = progress.associateBy { it.deckId to it.questionId }
        val unseen = mutableListOf<QuestionRef>()
        val wrong = mutableListOf<QuestionRef>()
        val right = mutableListOf<QuestionRef>()

        for (ref in questions) {
            val row = byKey[ref.deckId to ref.question.id]
            when (row?.lastResult) {
                null -> unseen += ref
                LastResult.Wrong -> wrong += ref
                LastResult.Correct -> right += ref
            }
        }
        return unseen + wrong + right
    }
}
