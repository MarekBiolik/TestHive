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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.testhive.android.theme.NeonGlassTokens
import com.testhive.core.domain.Deck

@Composable
fun HomeScreen(
    decks: List<Deck>,
    selectedIds: Set<String>,
    onToggle: (String) -> Unit,
    onStart: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeonGlassTokens.Canvas)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Keep learning.", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Pick a deck or mix — short cards, immediate feedback.",
            style = MaterialTheme.typography.bodyMedium,
        )
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
            text = if (selectedIds.size <= 1) "Start session" else "Start mix (${selectedIds.size})",
            onClick = onStart,
            enabled = selectedIds.isNotEmpty(),
        )
    }
}
