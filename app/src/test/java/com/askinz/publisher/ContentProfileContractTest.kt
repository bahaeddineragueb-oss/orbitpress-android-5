package com.askinz.publisher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentProfileContractTest {
  @Test fun unknownProfilesSafelyUseFoodWithoutChangingLegacyBehavior() {
    assertEquals(ContentProfileContract.FOOD, ContentProfileContract.normalize(null))
    assertEquals(ContentProfileContract.FOOD, ContentProfileContract.normalize("unknown"))
    assertTrue(ContentProfileContract.isFood("food"))
  }

  @Test fun supportedGeneralProfilesAreStable() {
    assertEquals(ContentProfileContract.GARDENING, ContentProfileContract.normalize(" Gardening "))
    assertEquals(ContentProfileContract.HOME_DECOR, ContentProfileContract.normalize("HOME-DECOR"))
    assertEquals(ContentProfileContract.CUSTOM, ContentProfileContract.normalize("custom"))
    assertFalse(ContentProfileContract.isFood(ContentProfileContract.GARDENING))
    assertFalse(ContentProfileContract.isFood(ContentProfileContract.HOME_DECOR))
  }

  @Test fun halalRuleExcludesRequestedCategories() {
    val rule = ContentProfileContract.halalRule().lowercase()
    assertTrue(rule.contains("alcoholic"))
    assertTrue(rule.contains("spirits"))
    assertTrue(rule.contains("pork"))
    assertTrue(rule.contains("pork-derived"))
  }
}
