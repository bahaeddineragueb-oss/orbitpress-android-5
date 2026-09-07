package com.askinz.publisher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ==========================================
// 🎨 Theme Presets & Color Definitions
// ==========================================

enum class ThemePreset(
  val id: String,
  val displayName: String,
  val subtitle: String,
  val primaryLight: Color,
  val primaryDark: Color,
  val secondaryColor: Color,
  val accentColor: Color
) {
  CYBER_ORBIT(
    id = "cyber_orbit",
    displayName = "Cyber Orbit",
    subtitle = "Electric Violet & Indigo Glow",
    primaryLight = Color(0xFF4F46E5),
    primaryDark = Color(0xFF818CF8),
    secondaryColor = Color(0xFF06B6D4),
    accentColor = Color(0xFFA855F7)
  ),
  EMERALD_FOREST(
    id = "emerald_forest",
    displayName = "Emerald Forest",
    subtitle = "Organic Sage & Deep Mint",
    primaryLight = Color(0xFF059669),
    primaryDark = Color(0xFF34D399),
    secondaryColor = Color(0xFF10B981),
    accentColor = Color(0xFFF59E0B)
  ),
  SUNSET_AMBER(
    id = "sunset_amber",
    displayName = "Sunset Amber",
    subtitle = "Warm Terracotta & Sun Gold",
    primaryLight = Color(0xFFEA580C),
    primaryDark = Color(0xFFFB923C),
    secondaryColor = Color(0xFFF43F5E),
    accentColor = Color(0xFFFBBF24)
  ),
  NORDIC_SAPPHIRE(
    id = "nordic_sapphire",
    displayName = "Nordic Sapphire",
    subtitle = "Deep Cobalt & Ice Blue",
    primaryLight = Color(0xFF2563EB),
    primaryDark = Color(0xFF60A5FA),
    secondaryColor = Color(0xFF38BDF8),
    accentColor = Color(0xFF6366F1)
  ),
  CRIMSON_LUXE(
    id = "crimson_luxe",
    displayName = "Crimson Luxe",
    subtitle = "Royal Ruby & Rose Gold",
    primaryLight = Color(0xFFE11D48),
    primaryDark = Color(0xFFFB7185),
    secondaryColor = Color(0xFFF43F5E),
    accentColor = Color(0xFFFDE047)
  );

  companion object {
    fun fromId(id: String): ThemePreset =
      entries.find { it.id.equals(id, ignoreCase = true) } ?: CYBER_ORBIT
  }
}

// -------------------------------------------------------------
// Helper to build Light Color Scheme for a preset
// -------------------------------------------------------------
fun createLightScheme(preset: ThemePreset): ColorScheme = lightColorScheme(
  primary = preset.primaryLight,
  onPrimary = Color.White,
  primaryContainer = preset.primaryLight.copy(alpha = 0.12f),
  onPrimaryContainer = preset.primaryLight,
  secondary = preset.secondaryColor,
  onSecondary = Color.White,
  secondaryContainer = preset.secondaryColor.copy(alpha = 0.15f),
  onSecondaryContainer = Color(0xFF0F172A),
  tertiary = preset.accentColor,
  onTertiary = Color.White,
  tertiaryContainer = preset.accentColor.copy(alpha = 0.15f),
  onTertiaryContainer = Color(0xFF0F172A),
  background = Color(0xFFF8FAFC),
  onBackground = Color(0xFF0F172A),
  surface = Color.White,
  onSurface = Color(0xFF0F172A),
  surfaceVariant = Color(0xFFF1F5F9),
  onSurfaceVariant = Color(0xFF475569),
  outline = Color(0xFFE2E8F0),
  outlineVariant = Color(0xFFCBD5E1),
  error = Color(0xFFEF4444),
  onError = Color.White,
  errorContainer = Color(0xFFFEE2E2),
  onErrorContainer = Color(0xFF991B1B)
)

// -------------------------------------------------------------
// Helper to build Dark Color Scheme for a preset
// -------------------------------------------------------------
fun createDarkScheme(preset: ThemePreset, isAmoled: Boolean): ColorScheme {
  val bg = if (isAmoled) Color(0xFF000000) else Color(0xFF0F172A)
  val surface = if (isAmoled) Color(0xFF0A0A0A) else Color(0xFF1E293B)
  val surfaceVariant = if (isAmoled) Color(0xFF141414) else Color(0xFF334155)
  val outline = if (isAmoled) Color(0xFF262626) else Color(0xFF475569)

  return darkColorScheme(
    primary = preset.primaryDark,
    onPrimary = Color(0xFF0F172A),
    primaryContainer = preset.primaryDark.copy(alpha = 0.22f),
    onPrimaryContainer = preset.primaryDark,
    secondary = preset.secondaryColor,
    onSecondary = Color(0xFF0F172A),
    secondaryContainer = preset.secondaryColor.copy(alpha = 0.22f),
    onSecondaryContainer = Color(0xFFE2E8F0),
    tertiary = preset.accentColor,
    onTertiary = Color(0xFF0F172A),
    tertiaryContainer = preset.accentColor.copy(alpha = 0.22f),
    onTertiaryContainer = Color(0xFFE2E8F0),
    background = bg,
    onBackground = Color(0xFFF8FAFC),
    surface = surface,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = outline,
    outlineVariant = if (isAmoled) Color(0xFF1A1A1A) else Color(0xFF334155),
    error = Color(0xFFF87171),
    onError = Color(0xFF0F172A),
    errorContainer = Color(0xFF450A0A),
    onErrorContainer = Color(0xFFFCA5A5)
  )
}

// -------------------------------------------------------------
// Gradient Brushes
// -------------------------------------------------------------
fun gradientPrimary(preset: ThemePreset, isDark: Boolean): Brush {
  val p = if (isDark) preset.primaryDark else preset.primaryLight
  val s = preset.secondaryColor
  return Brush.horizontalGradient(listOf(p, s))
}

fun gradientCard(isDark: Boolean, isAmoled: Boolean): Brush {
  return if (isDark) {
    if (isAmoled) {
      Brush.verticalGradient(listOf(Color(0xFF121212), Color(0xFF080808)))
    } else {
      Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))
    }
  } else {
    Brush.verticalGradient(listOf(Color.White, Color(0xFFF8FAFC)))
  }
}

// ==========================================
// 🚀 Main OrbitPressTheme Composable
// ==========================================
@Composable
fun OrbitPressTheme(
  presetId: String = "cyber_orbit",
  darkTheme: Boolean = isSystemInDarkTheme(),
  isAmoled: Boolean = false,
  content: @Composable () -> Unit
) {
  val preset = ThemePreset.fromId(presetId)
  val colors = if (darkTheme) {
    createDarkScheme(preset, isAmoled)
  } else {
    createLightScheme(preset)
  }

  MaterialTheme(
    colorScheme = colors,
    content = content
  )
}
