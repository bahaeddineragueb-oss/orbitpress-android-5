package com.askinz.publisher

object PinterestTrendsContract {
  private val allowedTypes = setOf("growing", "monthly", "yearly", "seasonal")

  fun interestForProfile(profile: String): String = when (profile.trim().lowercase()) {
    "food" -> "food_and_drinks"
    "gardening" -> "gardening"
    "home-decor" -> "home_decor"
    else -> ""
  }

  fun isValidRegion(region: String): Boolean = region.matches(Regex("[A-Z]+(?:\\+[A-Z]+)*"))

  fun isValidTrendType(type: String): Boolean = type in allowedTypes

  fun clampLimit(limit: Int): Int = limit.coerceIn(1, 50)
}
