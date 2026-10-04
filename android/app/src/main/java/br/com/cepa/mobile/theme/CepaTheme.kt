package br.com.cepa.mobile.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Wine = Color(0xFF430D20)
val WineLight = Color(0xFF682037)
val Rose = Color(0xFF8D3B50)
val Cream = Color(0xFFFAF5EF)
val Sand = Color(0xFFF2E9DF)
val Gold = Color(0xFFBD9358)
val Ink = Color(0xFF2C1D20)
val Muted = Color(0xFF766B6A)

private val colors = lightColorScheme(
    primary = Wine,
    onPrimary = Color.White,
    primaryContainer = Sand,
    onPrimaryContainer = Wine,
    secondary = Rose,
    onSecondary = Color.White,
    background = Cream,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    outline = Color(0xFFE6D7C6),
)

@Composable
fun CepaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colors, content = content)
}
