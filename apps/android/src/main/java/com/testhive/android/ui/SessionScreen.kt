package com.testhive.android.ui

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
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.testhive.android.session.CardPhase
import com.testhive.android.session.SessionUiState
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
    when (state) {
        is SessionUiState.Empty -> {
            Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
                Text("No questions in this selection.")
                Button(onClick = onExit, modifier = Modifier.padding(top = 16.dp)) { Text("Back") }
            }
        }
        is SessionUiState.Finished -> {
            Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
                Text("Session complete", style = MaterialTheme.typography.headlineMedium)
                Text("Answered ${state.answered} cards.")
                Button(onClick = onExit, modifier = Modifier.padding(top = 16.dp)) { Text("Home") }
            }
        }
        is SessionUiState.Active -> {
            val ref = state.current
            val q = ref.question
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    "${state.index + 1} / ${state.total}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(q.prompt, style = MaterialTheme.typography.titleLarge)
                q.code?.let { code ->
                    Text(
                        code.source,
                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    )
                }

                when (state.phase) {
                    CardPhase.Think -> {
                        Text("Think first, then reveal answers.")
                        Button(onClick = onRevealControls, modifier = Modifier.fillMaxWidth()) {
                            Text(if (q is RevealQuestion) "Reveal model answer" else "Show answer controls")
                        }
                    }
                    CardPhase.Answering -> {
                        when (q) {
                            is SingleQuestion -> SingleControls(q, state.selectedOptions, onToggleOption)
                            is MultiQuestion -> MultiControls(q, state.selectedOptions, onToggleOption)
                            is OrderQuestion -> OrderControls(q, state.orderIds, onSetOrder)
                            is MatchQuestion -> MatchControls(q, state.matchPairs, onSetMatch)
                            is ClozeQuestion -> ClozeControls(q, state.clozeAnswers, onSetCloze)
                            is RevealQuestion -> {
                                Text(q.answerText, style = MaterialTheme.typography.bodyLarge)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(onClick = { onRevealKnew(true) }) { Text("Knew it") }
                                    OutlinedButton(onClick = { onRevealKnew(false) }) { Text("Didn't") }
                                }
                            }
                        }
                        if (q !is RevealQuestion) {
                            Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) {
                                Text("Check")
                            }
                        }
                    }
                    CardPhase.Feedback -> {
                        val correct = state.lastCorrect == true
                        Text(
                            if (correct) "Correct" else "Not quite",
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (correct) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.error,
                        )
                        q.explanation?.let { Text(it) }
                        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
                            Text("Next")
                        }
                    }
                }

                OutlinedButton(onClick = onExit, modifier = Modifier.fillMaxWidth()) {
                    Text("Exit session")
                }
            }
        }
    }
}

@Composable
private fun SingleControls(
    q: SingleQuestion,
    selected: Set<String>,
    onToggle: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        q.options.forEach { opt ->
            FilterChip(
                selected = opt.id in selected,
                onClick = { onToggle(opt.id) },
                label = { Text(opt.text) },
            )
        }
    }
}

@Composable
private fun MultiControls(
    q: MultiQuestion,
    selected: Set<String>,
    onToggle: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Select all that apply", style = MaterialTheme.typography.labelMedium)
        q.options.forEach { opt ->
            FilterChip(
                selected = opt.id in selected,
                onClick = { onToggle(opt.id) },
                label = { Text(opt.text) },
            )
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
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Tap ↑ / ↓ to reorder", style = MaterialTheme.typography.labelMedium)
        current.forEachIndexed { index, id ->
            val item = q.items.first { it.id == id }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("${index + 1}. ${item.text}", modifier = Modifier.weight(1f))
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
}

@Composable
private fun MatchControls(
    q: MatchQuestion,
    pairs: Map<String, String>,
    onSetMatch: (String, String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        q.left.forEach { left ->
            Text(left.text, style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                q.right.forEach { right ->
                    FilterChip(
                        selected = pairs[left.id] == right.id,
                        onClick = { onSetMatch(left.id, right.id) },
                        label = { Text(right.text) },
                    )
                }
            }
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        q.blanks.keys.forEach { blankId ->
            Text("Blank: $blankId", style = MaterialTheme.typography.labelMedium)
            val choices = (q.choices + q.blanks.getValue(blankId)).distinct()
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                choices.forEach { choice ->
                    FilterChip(
                        selected = answers[blankId] == choice,
                        onClick = { onSetCloze(blankId, choice) },
                        label = { Text(choice) },
                    )
                }
            }
        }
    }
}
