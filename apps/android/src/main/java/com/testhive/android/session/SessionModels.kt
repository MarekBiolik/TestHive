package com.testhive.android.session

import com.testhive.core.domain.QuestionRef

enum class CardPhase {
    Answering,
    Feedback,
}

sealed class SessionUiState {
    data object Empty : SessionUiState()

    data class Finished(
        val answered: Int,
        val correctCount: Int,
    ) : SessionUiState()

    data class Active(
        val queue: List<QuestionRef>,
        val index: Int,
        val phase: CardPhase,
        val selectedOptions: Set<String> = emptySet(),
        val orderIds: List<String> = emptyList(),
        val matchPairs: Map<String, String> = emptyMap(),
        val clozeAnswers: Map<String, String> = emptyMap(),
        val lastCorrect: Boolean? = null,
        val answered: Int = 0,
        val correctCount: Int = 0,
        val autoAdvance: Boolean = false,
    ) : SessionUiState() {
        val current: QuestionRef get() = queue[index]
        val total: Int get() = queue.size
    }
}
