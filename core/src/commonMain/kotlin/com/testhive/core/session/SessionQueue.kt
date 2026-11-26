package com.testhive.core.session

import com.testhive.core.domain.LastResult
import com.testhive.core.domain.QuestionProgress
import com.testhive.core.domain.QuestionRef
import kotlin.random.Random

/**
 * Session order: exclude mastered; among the rest prefer unseen → last wrong → last right,
 * shuffling within each bucket ([docs/domain/templates.md]).
 */
object SessionQueue {
    fun build(
        questions: List<QuestionRef>,
        progress: List<QuestionProgress>,
        random: Random = Random.Default,
    ): List<QuestionRef> {
        val byKey = progress.associateBy { it.deckId to it.questionId }
        val unseen = mutableListOf<QuestionRef>()
        val wrong = mutableListOf<QuestionRef>()
        val right = mutableListOf<QuestionRef>()

        for (ref in questions) {
            val row = byKey[ref.deckId to ref.question.id]
            if (row?.mastered == true) continue
            when (row?.lastResult) {
                null -> unseen += ref
                LastResult.Wrong -> wrong += ref
                LastResult.Correct -> right += ref
            }
        }
        return unseen.shuffled(random) + wrong.shuffled(random) + right.shuffled(random)
    }
}
