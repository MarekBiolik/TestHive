package com.testhive.core.session

import com.testhive.core.domain.LastResult
import com.testhive.core.domain.QuestionProgress
import com.testhive.core.domain.QuestionRef
import com.testhive.core.domain.SingleQuestion
import com.testhive.core.domain.LabeledItem
import kotlin.test.Test
import kotlin.test.assertEquals

class SessionQueueTest {
    private fun q(id: String) = SingleQuestion(
        id = id,
        prompt = id,
        options = listOf(LabeledItem("a", "A"), LabeledItem("b", "B")),
        answer = "a",
    )

    @Test
    fun ordersUnseenThenWrongThenRight() {
        val refs = listOf("a", "b", "c").map { QuestionRef("d", q(it)) }
        val progress = listOf(
            QuestionProgress("d", "b", lastResult = LastResult.Correct),
            QuestionProgress("d", "c", lastResult = LastResult.Wrong),
        )
        val order = SessionQueue.build(refs, progress).map { it.question.id }
        assertEquals(listOf("a", "c", "b"), order)
    }
}
