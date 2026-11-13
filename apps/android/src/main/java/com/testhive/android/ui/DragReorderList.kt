package com.testhive.android.ui

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.testhive.android.theme.NeonGlassTokens
import kotlin.math.roundToInt

@Composable
fun DragReorderList(
    ids: List<String>,
    labelForId: (String) -> String,
    onReorder: (List<String>) -> Unit,
) {
    var localIds by remember { mutableStateOf(ids) }
    LaunchedEffect(ids) {
        if (ids != localIds) localIds = ids
    }

    var dragIndex by remember { mutableIntStateOf(-1) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    val rowStridePx = with(LocalDensity.current) { 68.dp.toPx() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            "Long-press, then drag the whole row",
            style = MaterialTheme.typography.labelSmall,
            color = NeonGlassTokens.TextSecondary,
        )
        localIds.forEachIndexed { index, id ->
            val dragging = index == dragIndex
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(if (dragging) 2f else 0f)
                    .offset {
                        IntOffset(0, if (dragging) dragOffsetY.roundToInt() else 0)
                    }
                    .pointerInput(localIds, index) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                dragIndex = index
                                dragOffsetY = 0f
                            },
                            onDragEnd = {
                                dragIndex = -1
                                dragOffsetY = 0f
                            },
                            onDragCancel = {
                                dragIndex = -1
                                dragOffsetY = 0f
                            },
                            onDrag = { change, amount ->
                                change.consume()
                                if (dragIndex < 0) return@detectDragGesturesAfterLongPress
                                dragOffsetY += amount.y
                                val from = dragIndex
                                val shift = (dragOffsetY / rowStridePx).roundToInt()
                                val to = (from + shift).coerceIn(0, localIds.lastIndex)
                                if (to != from) {
                                    val next = localIds.toMutableList()
                                    val item = next.removeAt(from)
                                    next.add(to, item)
                                    localIds = next
                                    onReorder(next)
                                    dragIndex = to
                                    dragOffsetY -= (to - from) * rowStridePx
                                }
                            },
                        )
                    },
            ) {
                NeonAnswerRow(
                    text = "${index + 1}. ${labelForId(id)}",
                    selected = dragging,
                    onClick = {},
                )
            }
        }
    }
}
