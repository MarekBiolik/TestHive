package com.testhive.core.session

import com.testhive.core.domain.LastResult
import com.testhive.core.domain.LabeledItem
import com.testhive.core.domain.QuestionProgress
import com.testhive.core.domain.QuestionRef
import com.testhive.core.domain.SingleQuestion
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SessionQueueTest {
    private fun q(id: String) = SingleQuestion(
        id = id,
        prompt = id,
        options = listOf(LabeledItem("a", "A"), LabeledItem("b", "B")),
        answer = "a",
    )

    private fun refs(vararg ids: String) = ids.map { QuestionRef("d", q(it)) }

    @Test
    fun ordersUnseenThenWrongThenRight() {
        val progress = listOf(
            QuestionProgress("d", "b", lastResult = LastResult.Correct),
            QuestionProgress("d", "c", lastResult = LastResult.Wrong),
        )
        val order = SessionQueue.build(refs("a", "b", "c"), progress, Random(0))
            .map { it.question.id }
        assertEquals(listOf("a", "c", "b"), order)
    }

    @Test
    fun excludesMastered() {
        val progress = listOf(
            QuestionProgress("d", "a", mastered = true),
            QuestionProgress("d", "b", lastResult = LastResult.Wrong),
            QuestionProgress("d", "c", lastResult = LastResult.Correct),
        )
        val order = SessionQueue.build(refs("a", "b", "c"), progress, Random(1))
            .map { it.question.id }
        assertEquals(listOf("b", "c"), order)
        assertTrue("a" !in order)
    }

    @Test
    fun shufflesWithinBucketsWithFixedSeed() {
        val ids = listOf("u1", "u2", "u3", "w1", "w2", "r1", "r2")
        val progress = listOf(
            QuestionProgress("d", "w1", lastResult = LastResult.Wrong),
            QuestionProgress("d", "w2", lastResult = LastResult.Wrong),
            QuestionProgress("d", "r1", lastResult = LastResult.Correct),
            QuestionProgress("d", "r2", lastResult = LastResult.Correct),
        )
        val order = SessionQueue.build(refs(*ids.toTypedArray()), progress, Random(42))
            .map { it.question.id }
        assertEquals(setOf("u1", "u2", "u3"), order.take(3).toSet())
        assertEquals(setOf("w1", "w2"), order.drop(3).take(2).toSet())
        assertEquals(setOf("r1", "r2"), order.takeLast(2).toSet())

        // Same seed sequence as three successive shuffled() calls inside SessionQueue.
        val r2 = Random(42)
        val expectedSequential = listOf("u1", "u2", "u3").shuffled(r2) +
            listOf("w1", "w2").shuffled(r2) +
            listOf("r1", "r2").shuffled(r2)
        assertEquals(expectedSequential, order)
        assertTrue(expectedSequential != ids)
    }
}
