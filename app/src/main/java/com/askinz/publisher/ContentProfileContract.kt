package com.askinz.publisher

object ContentProfileContract {
  const val FOOD = "food"
  const val GARDENING = "gardening"
  const val HOME_DECOR = "home-decor"
  const val CUSTOM = "custom"

  fun normalize(value: String?): String = when (value?.trim()?.lowercase()) {
    GARDENING -> GARDENING
    HOME_DECOR -> HOME_DECOR
    CUSTOM -> CUSTOM
    else -> FOOD
  }

  fun isFood(value: String?): Boolean = normalize(value) == FOOD

  fun halalRule(): String = "For food content, exclude alcoholic drinks, spirits, wine, beer, cooking alcohol, pork, ham, bacon, lard, gelatin from pork, and every pork-derived ingredient. If the keyword asks for a prohibited item, politely replace it with a halal alternative while keeping the search intent useful."
}
