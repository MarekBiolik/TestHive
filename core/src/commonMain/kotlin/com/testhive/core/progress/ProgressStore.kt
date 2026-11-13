package com.testhive.core.progress

import com.testhive.core.domain.LastResult
import com.testhive.core.domain.QuestionProgress

interface ProgressStore {
    fun get(deckId: String, questionId: String): QuestionProgress?
    fun allForDecks(deckIds: Set<String>): List<QuestionProgress>
    fun record(deckId: String, questionId: String, correct: Boolean, answeredAtEpochMs: Long)
    fun removeDeck(deckId: String)
}

class InMemoryProgressStore : ProgressStore {
    private val rows = linkedMapOf<String, QuestionProgress>()

    private fun key(deckId: String, questionId: String) = "$deckId::$questionId"

    override fun get(deckId: String, questionId: String): QuestionProgress? =
        rows[key(deckId, questionId)]

    override fun allForDecks(deckIds: Set<String>): List<QuestionProgress> =
        rows.values.filter { it.deckId in deckIds }

    override fun record(deckId: String, questionId: String, correct: Boolean, answeredAtEpochMs: Long) {
        val k = key(deckId, questionId)
        val prev = rows[k]
        rows[k] = QuestionProgress(
            deckId = deckId,
            questionId = questionId,
            correctCount = (prev?.correctCount ?: 0) + if (correct) 1 else 0,
            wrongCount = (prev?.wrongCount ?: 0) + if (correct) 0 else 1,
            lastResult = if (correct) LastResult.Correct else LastResult.Wrong,
            lastAnsweredAtEpochMs = answeredAtEpochMs,
        )
    }

    override fun removeDeck(deckId: String) {
        rows.keys.filter { it.startsWith("$deckId::") }.forEach { rows.remove(it) }
    }
}
