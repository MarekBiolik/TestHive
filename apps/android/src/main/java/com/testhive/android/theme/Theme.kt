package com.testhive.android.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val NeonColorScheme = darkColorScheme(
    primary = NeonGlassTokens.ActionPrimary,
    onPrimary = NeonGlassTokens.TextPrimary,
    secondary = NeonGlassTokens.ActionAccent,
    onSecondary = NeonGlassTokens.Canvas,
    background = NeonGlassTokens.Canvas,
    onBackground = NeonGlassTokens.TextPrimary,
    surface = NeonGlassTokens.Surface,
    onSurface = NeonGlassTokens.TextPrimary,
    onSurfaceVariant = NeonGlassTokens.TextSecondary,
    error = NeonGlassTokens.FeedbackError,
    onError = NeonGlassTokens.TextPrimary,
    outline = NeonGlassTokens.BorderGlass,
)

private val NeonTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        color = NeonGlassTokens.TextPrimary,
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        color = NeonGlassTokens.TextPrimary,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = NeonGlassTokens.TextPrimary,
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = NeonGlassTokens.TextSecondary,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = NeonGlassTokens.TextPrimary,
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = NeonGlassTokens.TextSecondary,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        color = NeonGlassTokens.TextPrimary,
    ),
)

@Composable
fun NeonGlassTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NeonColorScheme,
        typography = NeonTypography,
        content = content,
    )
}
