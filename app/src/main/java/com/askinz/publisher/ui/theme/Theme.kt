package com.askinz.publisher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ForestDeep = Color(0xFF101827)
val Forest = Color(0xFF172033)
val OrbitEmerald = Color(0xFF315D37)
val OrbitMint = Color(0xFF65F5C8)
val OrbitCobalt = Color(0xFF4169FF)
val OrbitAmber = Color(0xFFAD7B2D)
val OrbitCoral = Color(0xFFF07F68)
val OrbitCream = Color(0xFFF7F5F0)
val OrbitPaper = Color(0xFFFFFFFF)
val OrbitLine = Color(0xFFDEDFE5)
val OrbitMuted = Color(0xFF6F7788)

private val LightColorScheme = lightColorScheme(
  primary = Forest,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE8ECEF),
  onPrimaryContainer = ForestDeep,
  secondary = OrbitEmerald,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFDDF1DE),
  onSecondaryContainer = Color(0xFF153319),
  tertiary = OrbitCobalt,
  background = OrbitCream,
  onBackground = Color(0xFF1A1C1E),
  surface = OrbitPaper,
  onSurface = Color(0xFF1A1C1E),
  surfaceVariant = Color(0xFFF0EFEB),
  onSurfaceVariant = Color(0xFF43474E),
  outline = OrbitLine,
  error = OrbitCoral
)

private val DarkColorScheme = darkColorScheme(
  primary = OrbitMint,
  onPrimary = ForestDeep,
  primaryContainer = Color(0xFF22304A),
  onPrimaryContainer = OrbitMint,
  secondary = Color(0xFF8FD897),
  onSecondary = ForestDeep,
  secondaryContainer = Color(0xFF1F4624),
  onSecondaryContainer = Color(0xFFDDF1DE),
  tertiary = Color(0xFF8BA6FF),
  background = ForestDeep,
  onBackground = Color(0xFFE2E2E6),
  surface = Color(0xFF182236),
  onSurface = Color(0xFFE2E2E6),
  surfaceVariant = Color(0xFF202C45),
  onSurfaceVariant = Color(0xFFC3C6CF),
  outline = Color(0xFF364461),
  error = Color(0xFFFFB4AB)
)

@Composable
fun OrbitPressTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colors = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colors,
    content = content
  )
}
