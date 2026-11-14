package com.example.notes.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = darkColorScheme(
    primary = Button_Primary,
    onPrimary = Text_Primary,
    secondary = Button_Secondary,
    onSecondary = Text_Primary,
    tertiary = Basic_Success,
    background = Main_Bg,
    onBackground = Text_Primary,
    surface = Basic_Dark,
    onSurface = Text_Primary,
    surfaceVariant = Input_Bg,
    onSurfaceVariant = Text_Secondary,
    outline = Gray_8,
    outlineVariant = Button_Secondary,
    error = Basic_Error,
    onError = Text_Primary,
    errorContainer = Input_Error_Bg,
)

@Composable
fun NotesTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content,
    )
}
