package com.testhive.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.testhive.android.theme.NeonGlassTokens

private val GlassShape = RoundedCornerShape(16.dp)
private val ButtonShape = RoundedCornerShape(12.dp)

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .background(NeonGlassTokens.Glass, GlassShape)
            .border(1.dp, NeonGlassTokens.BorderGlass, GlassShape)
            .padding(16.dp),
    ) {
        content()
    }
}

@Composable
fun NeonPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (enabled) 12.dp else 0.dp,
                shape = ButtonShape,
                ambientColor = NeonGlassTokens.ActionPrimary.copy(alpha = 0.45f),
                spotColor = NeonGlassTokens.ActionPrimary.copy(alpha = 0.45f),
            ),
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = NeonGlassTokens.ActionPrimary,
            contentColor = NeonGlassTokens.TextPrimary,
            disabledContainerColor = NeonGlassTokens.GlassSolid,
            disabledContentColor = NeonGlassTokens.TextSecondary,
        ),
    ) {
        Text(text)
    }
}

@Composable
fun NeonAnswerRow(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val border = if (selected) NeonGlassTokens.ActionAccent else NeonGlassTokens.BorderGlass
    val bg = if (selected) NeonGlassTokens.ActionAccent.copy(alpha = 0.12f) else NeonGlassTokens.Glass
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (selected) {
                    Modifier.shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = NeonGlassTokens.ActionAccent.copy(alpha = 0.35f),
                        spotColor = NeonGlassTokens.ActionAccent.copy(alpha = 0.35f),
                    )
                } else {
                    Modifier
                },
            ),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bg,
            contentColor = NeonGlassTokens.TextPrimary,
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = border,
        ),
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )
    }
}

fun feedbackColor(correct: Boolean): Color =
    if (correct) NeonGlassTokens.FeedbackSuccess else NeonGlassTokens.FeedbackError
