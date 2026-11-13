package com.testhive.android.session

import com.testhive.core.domain.QuestionRef

enum class CardPhase {
    Think,
    Answering,
    Feedback,
}

sealed class SessionUiState {
    data object Empty : SessionUiState()
    data class Finished(val answered: Int) : SessionUiState()
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
    ) : SessionUiState() {
        val current: QuestionRef get() = queue[index]
        val total: Int get() = queue.size
    }
}
