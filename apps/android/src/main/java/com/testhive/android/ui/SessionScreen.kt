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
    onMarkEasy: () -> Unit = {},
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
                    Text(
                        "No questions left — all marked easy, or nothing selected. Reset progress on Home to bring cards back.",
                        style = MaterialTheme.typography.titleLarge,
                    )
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
                onMarkEasy = onMarkEasy,
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
    onMarkEasy: () -> Unit,
    onExit: () -> Unit,
) {
    val q = state.current.question
    val inFeedback = state.phase == CardPhase.Feedback
    val correct = state.lastCorrect == true
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

            AnsweringBody(
                q = q,
                state = state,
                locked = inFeedback,
                onToggleOption = onToggleOption,
                onReorder = onReorder,
                onSetMatch = onSetMatch,
                onSetCloze = onSetCloze,
                onRevealKnew = onRevealKnew,
            )

            if (inFeedback && !correct) {
                CorrectKeySummary(q)
                q.explanation?.let {
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
            } else if (inFeedback && correct) {
                q.explanation?.let {
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
            }
            Spacer(Modifier.height(88.dp))
        }

        FeedbackActionBar(
            phase = state.phase,
            correct = state.lastCorrect,
            showCheck = q !is RevealQuestion,
            onSubmit = onSubmit,
            onGotIt = onMarkEasy,
        )
    }
}

@Composable
private fun FeedbackActionBar(
    phase: CardPhase,
    correct: Boolean?,
    showCheck: Boolean,
    onSubmit: () -> Unit,
    onGotIt: () -> Unit,
) {
    when (phase) {
        CardPhase.Answering -> {
            if (showCheck) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeonGlassTokens.Canvas.copy(alpha = 0.96f))
                        .padding(20.dp),
                ) {
                    NeonPrimaryButton("Check", onSubmit)
                }
            }
        }
        CardPhase.Feedback -> {
            val ok = correct == true
            val accent = if (ok) NeonGlassTokens.FeedbackSuccess else NeonGlassTokens.FeedbackError
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NeonGlassTokens.Canvas.copy(alpha = 0.96f))
                    .border(
                        width = 1.dp,
                        color = accent.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = if (ok) "Correct" else "Incorrect",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = accent,
                )
                NeonPrimaryButton(
                    text = "Got it",
                    onClick = onGotIt,
                    containerColor = accent,
                )
                Text(
                    text = "Hides this card until you reset progress",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonGlassTokens.TextSecondary,
                )
            }
        }
    }
}

@Composable
private fun AnsweringBody(
    q: Question,
    state: SessionUiState.Active,
    locked: Boolean,
    onToggleOption: (String) -> Unit,
    onReorder: (List<String>) -> Unit,
    onSetMatch: (String, String) -> Unit,
    onSetCloze: (String, String) -> Unit,
    onRevealKnew: (Boolean) -> Unit,
) {
    when (q) {
        is SingleQuestion -> q.options.forEach { opt ->
            NeonAnswerRow(
                text = opt.text,
                selected = opt.id in state.selectedOptions,
                tone = optionTone(
                    optionId = opt.id,
                    selected = opt.id in state.selectedOptions,
                    correctIds = setOf(q.answer),
                    locked = locked,
                ),
                enabled = !locked,
            ) { onToggleOption(opt.id) }
        }
        is MultiQuestion -> {
            Text("Select all that apply", style = MaterialTheme.typography.labelSmall)
            q.options.forEach { opt ->
                NeonAnswerRow(
                    text = opt.text,
                    selected = opt.id in state.selectedOptions,
                    tone = optionTone(
                        optionId = opt.id,
                        selected = opt.id in state.selectedOptions,
                        correctIds = q.answer.toSet(),
                        locked = locked,
                    ),
                    enabled = !locked,
                ) { onToggleOption(opt.id) }
            }
        }
        is OrderQuestion -> {
            if (locked) {
                val accent = if (state.lastCorrect == true) {
                    NeonGlassTokens.FeedbackSuccess
                } else {
                    NeonGlassTokens.FeedbackError
                }
                GlassPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, accent.copy(alpha = 0.7f), RoundedCornerShape(16.dp)),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        state.orderIds.ifEmpty { q.items.map { it.id } }.forEachIndexed { i, id ->
                            Text(
                                "${i + 1}. ${q.items.first { it.id == id }.text}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = NeonGlassTokens.TextPrimary,
                            )
                        }
                    }
                }
            } else {
                DragReorderList(
                    ids = state.orderIds.ifEmpty { q.items.map { it.id } },
                    labelForId = { id -> q.items.first { it.id == id }.text },
                    onReorder = onReorder,
                )
            }
        }
        is MatchQuestion -> MatchControls(q, state.matchPairs, onSetMatch, locked)
        is ClozeQuestion -> ClozeControls(q, state.clozeAnswers, onSetCloze, locked)
        is RevealQuestion -> {
            if (!locked) {
                GlassPanel {
                    Text(q.answerText, style = MaterialTheme.typography.bodyLarge)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NeonPrimaryButton("Knew it", { onRevealKnew(true) }, Modifier.weight(1f))
                    NeonPrimaryButton("Missed it", { onRevealKnew(false) }, Modifier.weight(1f))
                }
            } else {
                val accent = if (state.lastCorrect == true) {
                    NeonGlassTokens.FeedbackSuccess
                } else {
                    NeonGlassTokens.FeedbackError
                }
                GlassPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, accent.copy(alpha = 0.7f), RoundedCornerShape(16.dp)),
                ) {
                    Text(q.answerText, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

private fun optionTone(
    optionId: String,
    selected: Boolean,
    correctIds: Set<String>,
    locked: Boolean,
): AnswerRowTone {
    if (!locked) {
        return if (selected) AnswerRowTone.Selected else AnswerRowTone.Neutral
    }
    val isCorrect = optionId in correctIds
    return when {
        isCorrect -> AnswerRowTone.Correct
        selected -> AnswerRowTone.Incorrect
        else -> AnswerRowTone.Neutral
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
    locked: Boolean,
) {
    q.left.forEach { left ->
        Text(left.text, style = MaterialTheme.typography.titleLarge)
        q.right.forEach { right ->
            val selected = pairs[left.id] == right.id
            val correctRight = q.answer[left.id]
            val tone = when {
                !locked -> if (selected) AnswerRowTone.Selected else AnswerRowTone.Neutral
                right.id == correctRight -> AnswerRowTone.Correct
                selected -> AnswerRowTone.Incorrect
                else -> AnswerRowTone.Neutral
            }
            NeonAnswerRow(
                text = right.text,
                selected = selected,
                tone = tone,
                enabled = !locked,
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
    locked: Boolean,
) {
    q.blanks.keys.forEach { blankId ->
        Text("Blank · $blankId", style = MaterialTheme.typography.labelSmall)
        val choices = (q.choices + q.blanks.getValue(blankId)).distinct()
        val accepted = q.blanks.getValue(blankId).map { it.lowercase() }.toSet()
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            choices.forEach { choice ->
                val selected = answers[blankId] == choice
                val tone = when {
                    !locked -> if (selected) AnswerRowTone.Selected else AnswerRowTone.Neutral
                    choice.lowercase() in accepted -> AnswerRowTone.Correct
                    selected -> AnswerRowTone.Incorrect
                    else -> AnswerRowTone.Neutral
                }
                NeonAnswerRow(
                    text = choice,
                    selected = selected,
                    tone = tone,
                    enabled = !locked,
                    onClick = { onSetCloze(blankId, choice) },
                )
            }
        }
    }
}
