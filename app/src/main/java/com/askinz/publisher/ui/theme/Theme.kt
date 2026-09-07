package com.askinz.publisher.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ==========================================
// 🎨 Comprehensive Full-Spectrum Theme Presets
// ==========================================

enum class ThemePreset(
  val id: String,
  val displayName: String,
  val subtitle: String,
  // Primary branding
  val primaryLight: Color,
  val primaryDark: Color,
  val secondaryColor: Color,
  val accentColor: Color,
  // Full App Canvas Backgrounds
  val bgLight: Color,
  val bgDark: Color,
  // Surface / Card Colors
  val surfaceLight: Color,
  val surfaceDark: Color,
  val surfaceVariantLight: Color,
  val surfaceVariantDark: Color,
  // Cadre / Border Outlines
  val borderLight: Color,
  val borderDark: Color
) {
  CYBER_ORBIT(
    id = "cyber_orbit",
    displayName = "Cyber Orbit",
    subtitle = "Electric Violet & Neon Glow",
    primaryLight = Color(0xFF6366F1),
    primaryDark = Color(0xFF818CF8),
    secondaryColor = Color(0xFF06B6D4),
    accentColor = Color(0xFFA855F7),
    bgLight = Color(0xFFF5F3FF),
    bgDark = Color(0xFF0B0F19),
    surfaceLight = Color(0xFFFFFFFF),
    surfaceDark = Color(0xFF131B2E),
    surfaceVariantLight = Color(0xFFEDE9FE),
    surfaceVariantDark = Color(0xFF1E293B),
    borderLight = Color(0xFFDDD6FE),
    borderDark = Color(0xFF2E3D5B)
  ),
  EMERALD_FOREST(
    id = "emerald_forest",
    displayName = "Emerald Forest",
    subtitle = "Organic Sage & Mint Glow",
    primaryLight = Color(0xFF059669),
    primaryDark = Color(0xFF34D399),
    secondaryColor = Color(0xFF10B981),
    accentColor = Color(0xFFF59E0B),
    bgLight = Color(0xFFF0FDF4),
    bgDark = Color(0xFF061A12),
    surfaceLight = Color(0xFFFFFFFF),
    surfaceDark = Color(0xFF0D291E),
    surfaceVariantLight = Color(0xFFDCFCE7),
    surfaceVariantDark = Color(0xFF163E2E),
    borderLight = Color(0xFFBBF7D0),
    borderDark = Color(0xFF1E5640)
  ),
  SUNSET_AMBER(
    id = "sunset_amber",
    displayName = "Sunset Amber",
    subtitle = "Warm Terracotta & Sun Gold",
    primaryLight = Color(0xFFEA580C),
    primaryDark = Color(0xFFFB923C),
    secondaryColor = Color(0xFFF43F5E),
    accentColor = Color(0xFFFBBF24),
    bgLight = Color(0xFFFFFBEB),
    bgDark = Color(0xFF18100C),
    surfaceLight = Color(0xFFFFFFFF),
    surfaceDark = Color(0xFF261913),
    surfaceVariantLight = Color(0xFFFEF3C7),
    surfaceVariantDark = Color(0xFF382319),
    borderLight = Color(0xFFFED7AA),
    borderDark = Color(0xFF523324)
  ),
  NORDIC_SAPPHIRE(
    id = "nordic_sapphire",
    displayName = "Nordic Sapphire",
    subtitle = "Deep Cobalt & Ice Blue",
    primaryLight = Color(0xFF2563EB),
    primaryDark = Color(0xFF60A5FA),
    secondaryColor = Color(0xFF38BDF8),
    accentColor = Color(0xFF6366F1),
    bgLight = Color(0xFFF0F9FF),
    bgDark = Color(0xFF081026),
    surfaceLight = Color(0xFFFFFFFF),
    surfaceDark = Color(0xFF0F1E3D),
    surfaceVariantLight = Color(0xFFE0F2FE),
    surfaceVariantDark = Color(0xFF162B54),
    borderLight = Color(0xFFBAE6FD),
    borderDark = Color(0xFF22427C)
  ),
  CRIMSON_LUXE(
    id = "crimson_luxe",
    displayName = "Crimson Luxe",
    subtitle = "Royal Ruby & Rose Gold",
    primaryLight = Color(0xFFE11D48),
    primaryDark = Color(0xFFFB7185),
    secondaryColor = Color(0xFFF43F5E),
    accentColor = Color(0xFFFDE047),
    bgLight = Color(0xFFFFF1F2),
    bgDark = Color(0xFF18080E),
    surfaceLight = Color(0xFFFFFFFF),
    surfaceDark = Color(0xFF280E18),
    surfaceVariantLight = Color(0xFFFFE4E6),
    surfaceVariantDark = Color(0xFF3C1423),
    borderLight = Color(0xFFFECDD3),
    borderDark = Color(0xFF5A1E35)
  );

  companion object {
    fun fromId(id: String): ThemePreset =
      entries.find { it.id.equals(id, ignoreCase = true) } ?: CYBER_ORBIT
  }
}

// -------------------------------------------------------------
// Helper to build Light Color Scheme (100% Theme Integration)
// -------------------------------------------------------------
fun createLightScheme(preset: ThemePreset): ColorScheme = lightColorScheme(
  primary = preset.primaryLight,
  onPrimary = Color.White,
  primaryContainer = preset.primaryLight.copy(alpha = 0.14f),
  onPrimaryContainer = preset.primaryLight,
  secondary = preset.secondaryColor,
  onSecondary = Color.White,
  secondaryContainer = preset.secondaryColor.copy(alpha = 0.16f),
  onSecondaryContainer = Color(0xFF0F172A),
  tertiary = preset.accentColor,
  onTertiary = Color.White,
  tertiaryContainer = preset.accentColor.copy(alpha = 0.16f),
  onTertiaryContainer = Color(0xFF0F172A),
  background = preset.bgLight,
  onBackground = Color(0xFF0F172A),
  surface = preset.surfaceLight,
  onSurface = Color(0xFF0F172A),
  surfaceVariant = preset.surfaceVariantLight,
  onSurfaceVariant = Color(0xFF475569),
  outline = preset.borderLight,
  outlineVariant = preset.borderLight.copy(alpha = 0.7f),
  error = Color(0xFFEF4444),
  onError = Color.White,
  errorContainer = Color(0xFFFEE2E2),
  onErrorContainer = Color(0xFF991B1B)
)

// -------------------------------------------------------------
// Helper to build Dark Color Scheme (100% Theme Integration + AMOLED)
// -------------------------------------------------------------
fun createDarkScheme(preset: ThemePreset, isAmoled: Boolean): ColorScheme {
  val bg = if (isAmoled) Color(0xFF000000) else preset.bgDark
  val surface = if (isAmoled) Color(0xFF0A0A0A) else preset.surfaceDark
  val surfaceVariant = if (isAmoled) Color(0xFF141414) else preset.surfaceVariantDark
  val outline = if (isAmoled) Color(0xFF262626) else preset.borderDark

  return darkColorScheme(
    primary = preset.primaryDark,
    onPrimary = Color(0xFF0B0F19),
    primaryContainer = preset.primaryDark.copy(alpha = 0.25f),
    onPrimaryContainer = preset.primaryDark,
    secondary = preset.secondaryColor,
    onSecondary = Color(0xFF0B0F19),
    secondaryContainer = preset.secondaryColor.copy(alpha = 0.25f),
    onSecondaryContainer = Color(0xFFE2E8F0),
    tertiary = preset.accentColor,
    onTertiary = Color(0xFF0B0F19),
    tertiaryContainer = preset.accentColor.copy(alpha = 0.25f),
    onTertiaryContainer = Color(0xFFE2E8F0),
    background = bg,
    onBackground = Color(0xFFF8FAFC),
    surface = surface,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = outline,
    outlineVariant = if (isAmoled) Color(0xFF1E1E1E) else preset.borderDark.copy(alpha = 0.6f),
    error = Color(0xFFF87171),
    onError = Color(0xFF0F172A),
    errorContainer = Color(0xFF450A0A),
    onErrorContainer = Color(0xFFFCA5A5)
  )
}

// -------------------------------------------------------------
// Modern Cadre Border & Card Helpers
// -------------------------------------------------------------
@Composable
fun modernCardBorder(): BorderStroke =
  BorderStroke(1.dp, MaterialTheme.colorScheme.outline)

@Composable
fun modernAccentBorder(): BorderStroke =
  BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))

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
