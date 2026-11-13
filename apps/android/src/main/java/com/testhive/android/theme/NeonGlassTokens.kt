package com.testhive.android.theme

import androidx.compose.ui.graphics.Color

/**
 * Hand-synced from [design/tokens/neon-glass.json] (ADR 0007).
 * Do not invent a parallel palette in feature UI.
 */
object NeonGlassTokens {
    val Canvas = Color(0xFF020617)
    val Surface = Color(0xFF0B1220)
    val Glass = Color(0xB3111827) // navy.800 @ ~70%
    val GlassSolid = Color(0xFF111827)
    val TextPrimary = Color(0xFFF8FAFC)
    val TextSecondary = Color(0xFF94A3B8)
    val ActionPrimary = Color(0xFF3B82F6)
    val ActionAccent = Color(0xFF22D3EE)
    val BorderGlass = Color(0x66334155)
    val FeedbackSuccess = Color(0xFF34D399)
    val FeedbackError = Color(0xFFF87171)
    val CodePanel = Color(0xFF0A1628)
}
