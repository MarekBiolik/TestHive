package com.testhive.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("TestHive", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Pick one or more decks, then study.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(decks, key = { it.id }) { deck ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggle(deck.id) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = deck.id in selectedIds,
                        onCheckedChange = { onToggle(deck.id) },
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(deck.title, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "${deck.questions.size} questions · ${deck.language}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        Button(
            onClick = onStart,
            enabled = selectedIds.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (selectedIds.size <= 1) "Start studying" else "Start mix (${selectedIds.size})")
        }
    }
}
