package com.askinz.publisher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PinterestTrendsContractTest {
  @Test
  fun mapsProfilesToPinterestInterests() {
    assertEquals("food_and_drinks", PinterestTrendsContract.interestForProfile("food"))
    assertEquals("gardening", PinterestTrendsContract.interestForProfile("gardening"))
    assertEquals("home_decor", PinterestTrendsContract.interestForProfile("home-decor"))
    assertEquals("", PinterestTrendsContract.interestForProfile("custom"))
  }

  @Test
  fun validatesTrendFiltersAndClampsLimit() {
    assertTrue(PinterestTrendsContract.isValidRegion("GB+IE"))
    assertFalse(PinterestTrendsContract.isValidRegion("gb"))
    assertTrue(PinterestTrendsContract.isValidTrendType("seasonal"))
    assertFalse(PinterestTrendsContract.isValidTrendType("random"))
    assertEquals(1, PinterestTrendsContract.clampLimit(0))
    assertEquals(50, PinterestTrendsContract.clampLimit(99))
  }
}
