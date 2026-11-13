package com.testhive.android.session

import androidx.lifecycle.ViewModel
import com.testhive.android.AppContainer
import com.testhive.core.domain.AnswerSubmission
import com.testhive.core.domain.ClozeQuestion
import com.testhive.core.domain.Deck
import com.testhive.core.domain.MatchQuestion
import com.testhive.core.domain.MultiQuestion
import com.testhive.core.domain.OrderQuestion
import com.testhive.core.domain.RevealQuestion
import com.testhive.core.domain.SingleQuestion
import com.testhive.core.grading.Grader
import com.testhive.core.library.selectedQuestions
import com.testhive.core.session.SessionQueue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuizViewModel(
    private val container: AppContainer,
) : ViewModel() {
    private val _decks = MutableStateFlow<List<Deck>>(emptyList())
    val decks: StateFlow<List<Deck>> = _decks.asStateFlow()

    private val _selected = MutableStateFlow<Set<String>>(emptySet())
    val selected: StateFlow<Set<String>> = _selected.asStateFlow()

    private val _session = MutableStateFlow<SessionUiState>(SessionUiState.Empty)
    val session: StateFlow<SessionUiState> = _session.asStateFlow()

    init {
        refreshDecks()
    }

    fun refreshDecks() {
        val list = container.decks.list()
        _decks.value = list
        if (_selected.value.isEmpty() && list.isNotEmpty()) {
            _selected.value = setOf(list.first().id)
        }
    }

    fun toggleDeck(id: String) {
        _selected.update { cur ->
            if (id in cur) cur - id else cur + id
        }
    }

    fun startSession() {
        val ids = _selected.value
        if (ids.isEmpty()) return
        val questions = container.decks.selectedQuestions(ids)
        val progress = container.progress.allForDecks(ids)
        val queue = SessionQueue.build(questions, progress)
        _session.value = if (queue.isEmpty()) {
            SessionUiState.Empty
        } else {
            SessionUiState.Active(queue = queue, index = 0, phase = CardPhase.Think)
        }
    }

    fun revealControls() {
        _session.update { state ->
            val active = state as? SessionUiState.Active ?: return@update state
            val q = active.current.question
            val orderSeed = if (q is OrderQuestion) q.items.map { it.id }.shuffled() else emptyList()
            active.copy(phase = CardPhase.Answering, orderIds = orderSeed)
        }
    }

    fun toggleOption(optionId: String) {
        _session.update { state ->
            val active = state as? SessionUiState.Active ?: return@update state
            when (active.current.question) {
                is SingleQuestion -> active.copy(selectedOptions = setOf(optionId))
                is MultiQuestion -> {
                    val next = if (optionId in active.selectedOptions) {
                        active.selectedOptions - optionId
                    } else {
                        active.selectedOptions + optionId
                    }
                    active.copy(selectedOptions = next)
                }
                else -> active
            }
        }
    }

    fun setOrder(ids: List<String>) {
        _session.update { state ->
            val active = state as? SessionUiState.Active ?: return@update state
            active.copy(orderIds = ids)
        }
    }

    fun setMatch(leftId: String, rightId: String) {
        _session.update { state ->
            val active = state as? SessionUiState.Active ?: return@update state
            active.copy(matchPairs = active.matchPairs + (leftId to rightId))
        }
    }

    fun setCloze(blankId: String, value: String) {
        _session.update { state ->
            val active = state as? SessionUiState.Active ?: return@update state
            active.copy(clozeAnswers = active.clozeAnswers + (blankId to value))
        }
    }

    fun submitReveal(knewIt: Boolean) {
        gradeAndFeedback(AnswerSubmission.Reveal(knewIt))
    }

    fun submit() {
        val active = _session.value as? SessionUiState.Active ?: return
        val submission = when (val q = active.current.question) {
            is SingleQuestion -> AnswerSubmission.Single(active.selectedOptions.firstOrNull() ?: return)
            is MultiQuestion -> AnswerSubmission.Multi(active.selectedOptions.toList())
            is OrderQuestion -> AnswerSubmission.Order(active.orderIds.ifEmpty { q.items.map { it.id } })
            is MatchQuestion -> AnswerSubmission.Match(active.matchPairs)
            is ClozeQuestion -> AnswerSubmission.Cloze(active.clozeAnswers)
            is RevealQuestion -> return
        }
        gradeAndFeedback(submission)
    }

    private fun gradeAndFeedback(submission: AnswerSubmission) {
        val active = _session.value as? SessionUiState.Active ?: return
        val ref = active.current
        val result = Grader.grade(ref.question, submission)
        container.progress.record(
            deckId = ref.deckId,
            questionId = ref.question.id,
            correct = result.correct,
            answeredAtEpochMs = System.currentTimeMillis(),
        )
        _session.value = active.copy(
            phase = CardPhase.Feedback,
            lastCorrect = result.correct,
            answered = active.answered + 1,
        )
    }

    fun next() {
        _session.update { state ->
            val active = state as? SessionUiState.Active ?: return@update state
            val nextIndex = active.index + 1
            if (nextIndex >= active.queue.size) {
                SessionUiState.Finished(answered = active.answered)
            } else {
                SessionUiState.Active(
                    queue = active.queue,
                    index = nextIndex,
                    phase = CardPhase.Think,
                    answered = active.answered,
                )
            }
        }
    }

    fun exitSession() {
        _session.value = SessionUiState.Empty
    }
}
