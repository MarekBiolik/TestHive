package com.testhive.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.testhive.android.session.CardPhase
import com.testhive.android.session.SessionUiState
import com.testhive.android.theme.NeonGlassTokens
import com.testhive.core.domain.ClozeQuestion
import com.testhive.core.domain.MatchQuestion
import com.testhive.core.domain.MultiQuestion
import com.testhive.core.domain.OrderQuestion
import com.testhive.core.domain.Question
import com.testhive.core.domain.RevealQuestion
import com.testhive.core.domain.SingleQuestion

@Composable
fun SessionScreen(
    state: SessionUiState,
    onToggleOption: (String) -> Unit,
    onReorder: (List<String>) -> Unit,
    onSetMatch: (String, String) -> Unit,
    onSetCloze: (String, String) -> Unit,
    onRevealKnew: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit,
    onExit: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeonGlassTokens.Canvas),
    ) {
        when (state) {
            is SessionUiState.Empty -> {
                Column(Modifier.padding(20.dp)) {
                    Text("No questions in this selection.", style = MaterialTheme.typography.titleLarge)
                    NeonPrimaryButton("Back", onExit, Modifier.padding(top = 16.dp))
                }
            }
            is SessionUiState.Finished -> FinishedSession(state, onExit)
            is SessionUiState.Active -> ActiveSession(
                state = state,
                onToggleOption = onToggleOption,
                onReorder = onReorder,
                onSetMatch = onSetMatch,
                onSetCloze = onSetCloze,
                onRevealKnew = onRevealKnew,
                onSubmit = onSubmit,
                onNext = onNext,
                onExit = onExit,
            )
        }
    }
}

@Composable
private fun FinishedSession(
    state: SessionUiState.Finished,
    onExit: () -> Unit,
) {
    val rate = if (state.answered == 0) 0 else (100 * state.correctCount / state.answered)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = NeonGlassTokens.ActionAccent.copy(alpha = 0.35f),
                    spotColor = NeonGlassTokens.ActionPrimary.copy(alpha = 0.4f),
                )
                .background(NeonGlassTokens.Glass, RoundedCornerShape(24.dp))
                .border(1.dp, NeonGlassTokens.ActionAccent.copy(alpha = 0.45f), RoundedCornerShape(24.dp))
                .padding(28.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Nice work",
                    style = MaterialTheme.typography.headlineLarge,
                    color = NeonGlassTokens.ActionAccent,
                )
                Text(
                    "You cleared this run. Keep the momentum while it’s fresh.",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "${state.correctCount} / ${state.answered} correct · $rate%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        Spacer(Modifier.height(28.dp))
        NeonPrimaryButton("Back to decks", onExit)
    }
}

@Composable
private fun ActiveSession(
    state: SessionUiState.Active,
    onToggleOption: (String) -> Unit,
    onReorder: (List<String>) -> Unit,
    onSetMatch: (String, String) -> Unit,
    onSetCloze: (String, String) -> Unit,
    onRevealKnew: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit,
    onExit: () -> Unit,
) {
    val q = state.current.question
    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onExit) {
                Text("✕", color = NeonGlassTokens.TextSecondary, style = MaterialTheme.typography.titleLarge)
            }
            Text(
                "${state.index + 1} / ${state.total}",
                style = MaterialTheme.typography.labelSmall,
                color = NeonGlassTokens.ActionAccent,
                modifier = Modifier.padding(start = 4.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            GlassPanel(Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(q.prompt, style = MaterialTheme.typography.titleLarge)
                    q.code?.let { code ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(NeonGlassTokens.CodePanel, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                        ) {
                            Text(code.source, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            when (state.phase) {
                CardPhase.Answering -> AnsweringBody(
                    q = q,
                    state = state,
                    onToggleOption = onToggleOption,
                    onReorder = onReorder,
                    onSetMatch = onSetMatch,
                    onSetCloze = onSetCloze,
                    onRevealKnew = onRevealKnew,
                )
                CardPhase.Feedback -> FeedbackBody(q = q, correct = state.lastCorrect == true)
            }
            Spacer(Modifier.height(88.dp))
        }

        // Sticky bottom primary action (popular quiz apps pattern)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NeonGlassTokens.Canvas.copy(alpha = 0.96f))
                .padding(20.dp),
        ) {
            when (state.phase) {
                CardPhase.Answering -> {
                    if (q !is RevealQuestion) {
                        NeonPrimaryButton("Check", onSubmit)
                    }
                }
                CardPhase.Feedback -> {
                    if (state.lastCorrect == true) {
                        Text(
                            "Correct — next up…",
                            style = MaterialTheme.typography.labelLarge,
                            color = NeonGlassTokens.FeedbackSuccess,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    } else {
                        NeonPrimaryButton("Continue", onNext)
                    }
                }
            }
        }
    }
}

@Composable
private fun AnsweringBody(
    q: Question,
    state: SessionUiState.Active,
    onToggleOption: (String) -> Unit,
    onReorder: (List<String>) -> Unit,
    onSetMatch: (String, String) -> Unit,
    onSetCloze: (String, String) -> Unit,
    onRevealKnew: (Boolean) -> Unit,
) {
    when (q) {
        is SingleQuestion -> q.options.forEach { opt ->
            NeonAnswerRow(opt.text, opt.id in state.selectedOptions) { onToggleOption(opt.id) }
        }
        is MultiQuestion -> {
            Text("Select all that apply", style = MaterialTheme.typography.labelSmall)
            q.options.forEach { opt ->
                NeonAnswerRow(opt.text, opt.id in state.selectedOptions) { onToggleOption(opt.id) }
            }
        }
        is OrderQuestion -> DragReorderList(
            ids = state.orderIds.ifEmpty { q.items.map { it.id } },
            labelForId = { id -> q.items.first { it.id == id }.text },
            onReorder = onReorder,
        )
        is MatchQuestion -> MatchControls(q, state.matchPairs, onSetMatch)
        is ClozeQuestion -> ClozeControls(q, state.clozeAnswers, onSetCloze)
        is RevealQuestion -> {
            GlassPanel {
                Text(q.answerText, style = MaterialTheme.typography.bodyLarge)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NeonPrimaryButton("Knew it", { onRevealKnew(true) }, Modifier.weight(1f))
                NeonPrimaryButton("Missed it", { onRevealKnew(false) }, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun FeedbackBody(q: Question, correct: Boolean) {
    if (correct) {
        GlassPanel(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = NeonGlassTokens.FeedbackSuccess.copy(alpha = 0.35f),
                    spotColor = NeonGlassTokens.FeedbackSuccess.copy(alpha = 0.35f),
                ),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Correct",
                    style = MaterialTheme.typography.headlineLarge,
                    color = NeonGlassTokens.FeedbackSuccess,
                )
                q.explanation?.let { Text(it, style = MaterialTheme.typography.bodyLarge) }
            }
        }
    } else {
        GlassPanel(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, NeonGlassTokens.FeedbackError.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = NeonGlassTokens.FeedbackError.copy(alpha = 0.4f),
                    spotColor = NeonGlassTokens.FeedbackError.copy(alpha = 0.35f),
                ),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "Not quite",
                    style = MaterialTheme.typography.headlineLarge,
                    color = NeonGlassTokens.FeedbackError,
                )
                Text(
                    "Here’s the key — lock it in, then continue.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                CorrectKeySummary(q)
                q.explanation?.let {
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
private fun CorrectKeySummary(q: Question) {
    val line = when (q) {
        is SingleQuestion -> q.options.firstOrNull { it.id == q.answer }?.text
        is MultiQuestion -> q.answer.mapNotNull { id -> q.options.firstOrNull { it.id == id }?.text }
            .joinToString(" · ")
        is OrderQuestion -> q.answer.mapNotNull { id -> q.items.firstOrNull { it.id == id }?.text }
            .mapIndexed { i, t -> "${i + 1}. $t" }
            .joinToString("\n")
        is MatchQuestion -> q.answer.entries.joinToString("\n") { (l, r) ->
            val left = q.left.firstOrNull { it.id == l }?.text ?: l
            val right = q.right.firstOrNull { it.id == r }?.text ?: r
            "$left → $right"
        }
        is ClozeQuestion -> q.blanks.entries.joinToString(" · ") { (id, accepted) ->
            "$id: ${accepted.firstOrNull().orEmpty()}"
        }
        is RevealQuestion -> q.answerText
    }
    if (!line.isNullOrBlank()) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(NeonGlassTokens.CodePanel, RoundedCornerShape(12.dp))
                .padding(12.dp),
        ) {
            Text("Answer", style = MaterialTheme.typography.labelSmall, color = NeonGlassTokens.ActionAccent)
            Text(line, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun MatchControls(
    q: MatchQuestion,
    pairs: Map<String, String>,
    onSetMatch: (String, String) -> Unit,
) {
    q.left.forEach { left ->
        Text(left.text, style = MaterialTheme.typography.titleLarge)
        q.right.forEach { right ->
            NeonAnswerRow(
                text = right.text,
                selected = pairs[left.id] == right.id,
                onClick = { onSetMatch(left.id, right.id) },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ClozeControls(
    q: ClozeQuestion,
    answers: Map<String, String>,
    onSetCloze: (String, String) -> Unit,
) {
    q.blanks.keys.forEach { blankId ->
        Text("Blank · $blankId", style = MaterialTheme.typography.labelSmall)
        val choices = (q.choices + q.blanks.getValue(blankId)).distinct()
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            choices.forEach { choice ->
                NeonAnswerRow(
                    text = choice,
                    selected = answers[blankId] == choice,
                    onClick = { onSetCloze(blankId, choice) },
                )
            }
        }
    }
}
