package com.testhive.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.testhive.android.session.SelectionStats
import com.testhive.android.theme.NeonGlassTokens
import com.testhive.core.domain.Deck

@Composable
fun HomeScreen(
    decks: List<Deck>,
    selectedIds: Set<String>,
    selectionStats: SelectionStats = SelectionStats(),
    importMessage: String? = null,
    onToggle: (String) -> Unit,
    onImportDeck: () -> Unit,
    onDismissImportMessage: () -> Unit = {},
    onResetProgress: () -> Unit = {},
    onStart: () -> Unit,
) {
    var resetArmed by remember(selectedIds) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeonGlassTokens.Canvas)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Keep learning.", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Pick a deck or mix — short cards, immediate feedback. Check Got it before Continue to hide a card until you reset progress.",
            style = MaterialTheme.typography.bodyMedium,
        )
        if (importMessage != null) {
            GlassPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onDismissImportMessage),
            ) {
                Text(importMessage, style = MaterialTheme.typography.bodyMedium)
            }
        }
        if (selectedIds.isNotEmpty() && selectionStats.total > 0) {
            Text(
                "${selectionStats.remaining} remaining · ${selectionStats.mastered} got it · ${selectionStats.total} total",
                style = MaterialTheme.typography.labelSmall,
                color = NeonGlassTokens.ActionAccent,
            )
        }
        Spacer(Modifier.height(4.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(decks, key = { it.id }) { deck ->
                GlassPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggle(deck.id) },
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = deck.id in selectedIds,
                            onCheckedChange = { onToggle(deck.id) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = NeonGlassTokens.ActionAccent,
                                uncheckedColor = NeonGlassTokens.TextSecondary,
                                checkmarkColor = NeonGlassTokens.Canvas,
                            ),
                        )
                        Column(Modifier.padding(start = 8.dp)) {
                            Text(deck.title, style = MaterialTheme.typography.titleLarge)
                            Text(
                                "${deck.questions.size} questions · ${deck.language}",
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }
            }
        }
        NeonPrimaryButton(
            text = "Import deck JSON",
            onClick = onImportDeck,
            containerColor = NeonGlassTokens.GlassSolid,
        )
        NeonPrimaryButton(
            text = if (resetArmed) "Tap again to reset progress" else "Reset progress",
            onClick = {
                if (!resetArmed) {
                    resetArmed = true
                } else {
                    onResetProgress()
                    resetArmed = false
                }
            },
            enabled = selectedIds.isNotEmpty(),
            containerColor = if (resetArmed) NeonGlassTokens.FeedbackError else NeonGlassTokens.GlassSolid,
        )
        NeonPrimaryButton(
            text = if (selectedIds.size <= 1) "Start session" else "Start mix (${selectedIds.size})",
            onClick = onStart,
            enabled = selectedIds.isNotEmpty() && selectionStats.remaining > 0,
        )
    }
}
