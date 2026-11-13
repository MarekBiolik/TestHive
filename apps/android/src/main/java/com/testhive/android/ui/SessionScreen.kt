package com.testhive.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.testhive.android.session.CardPhase
import com.testhive.android.session.SessionUiState
import com.testhive.android.theme.NeonGlassTokens
import com.testhive.core.domain.ClozeQuestion
import com.testhive.core.domain.MatchQuestion
import com.testhive.core.domain.MultiQuestion
import com.testhive.core.domain.OrderQuestion
import com.testhive.core.domain.RevealQuestion
import com.testhive.core.domain.SingleQuestion

@Composable
fun SessionScreen(
    state: SessionUiState,
    onRevealControls: () -> Unit,
    onToggleOption: (String) -> Unit,
    onSetOrder: (List<String>) -> Unit,
    onSetMatch: (String, String) -> Unit,
    onSetCloze: (String, String) -> Unit,
    onRevealKnew: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit,
    onExit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeonGlassTokens.Canvas)
            .padding(20.dp),
    ) {
        when (state) {
            is SessionUiState.Empty -> {
                Text("No questions in this selection.", style = MaterialTheme.typography.titleLarge)
                NeonPrimaryButton("Back", onExit, Modifier.padding(top = 16.dp))
            }
            is SessionUiState.Finished -> {
                Text("Session complete", style = MaterialTheme.typography.headlineLarge)
                Text(
                    "Answered ${state.answered} cards.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp),
                )
                NeonPrimaryButton("Home", onExit, Modifier.padding(top = 24.dp))
            }
            is SessionUiState.Active -> ActiveSession(
                state = state,
                onRevealControls = onRevealControls,
                onToggleOption = onToggleOption,
                onSetOrder = onSetOrder,
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
private fun ActiveSession(
    state: SessionUiState.Active,
    onRevealControls: () -> Unit,
    onToggleOption: (String) -> Unit,
    onSetOrder: (List<String>) -> Unit,
    onSetMatch: (String, String) -> Unit,
    onSetCloze: (String, String) -> Unit,
    onRevealKnew: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit,
    onExit: () -> Unit,
) {
    val q = state.current.question
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            "${state.index + 1} / ${state.total}",
            style = MaterialTheme.typography.labelSmall,
            color = NeonGlassTokens.ActionAccent,
        )
        GlassPanel(Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(q.prompt, style = MaterialTheme.typography.titleLarge)
                q.code?.let { code ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(NeonGlassTokens.CodePanel, androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                            .padding(12.dp),
                    ) {
                        Text(code.source, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        when (state.phase) {
            CardPhase.Think -> {
                Text("Think first — then reveal answers.", style = MaterialTheme.typography.bodyMedium)
                NeonPrimaryButton(
                    text = if (q is RevealQuestion) "Reveal model answer" else "Show answer controls",
                    onClick = onRevealControls,
                )
            }
            CardPhase.Answering -> {
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
                    is OrderQuestion -> OrderControls(q, state.orderIds, onSetOrder)
                    is MatchQuestion -> MatchControls(q, state.matchPairs, onSetMatch)
                    is ClozeQuestion -> ClozeControls(q, state.clozeAnswers, onSetCloze)
                    is RevealQuestion -> {
                        GlassPanel {
                            Text(q.answerText, style = MaterialTheme.typography.bodyLarge)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            NeonPrimaryButton("Knew it", { onRevealKnew(true) }, Modifier.weight(1f))
                            OutlinedButton(
                                onClick = { onRevealKnew(false) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = NeonGlassTokens.TextPrimary,
                                ),
                            ) { Text("Didn't") }
                        }
                    }
                }
                if (q !is RevealQuestion) {
                    NeonPrimaryButton("Check", onSubmit)
                }
            }
            CardPhase.Feedback -> {
                val correct = state.lastCorrect == true
                Text(
                    if (correct) "Correct" else "Not quite",
                    style = MaterialTheme.typography.headlineLarge,
                    color = feedbackColor(correct),
                )
                q.explanation?.let {
                    GlassPanel { Text(it, style = MaterialTheme.typography.bodyLarge) }
                }
                NeonPrimaryButton("Next", onNext)
            }
        }

        OutlinedButton(
            onClick = onExit,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonGlassTokens.TextSecondary),
        ) {
            Text("Exit session")
        }
    }
}

@Composable
private fun OrderControls(
    q: OrderQuestion,
    order: List<String>,
    onSetOrder: (List<String>) -> Unit,
) {
    val current = order.ifEmpty { q.items.map { it.id } }
    Text("Reorder with ↑ / ↓", style = MaterialTheme.typography.labelSmall)
    current.forEachIndexed { index, id ->
        val item = q.items.first { it.id == id }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("${index + 1}. ${item.text}", Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            OutlinedButton(
                onClick = {
                    if (index > 0) {
                        val next = current.toMutableList()
                        next[index] = next[index - 1].also { next[index - 1] = next[index] }
                        onSetOrder(next)
                    }
                },
                enabled = index > 0,
            ) { Text("↑") }
            OutlinedButton(
                onClick = {
                    if (index < current.lastIndex) {
                        val next = current.toMutableList()
                        next[index] = next[index + 1].also { next[index + 1] = next[index] }
                        onSetOrder(next)
                    }
                },
                enabled = index < current.lastIndex,
            ) { Text("↓") }
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
                    modifier = Modifier.fillMaxWidth(0.48f),
                )
            }
        }
    }
}
