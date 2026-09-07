package com.askinz.publisher

import org.junit.Assert.*
import org.junit.Test

class PublishingContractsTest {
  @Test
  fun acceptsStandardPinterestAspectRatios() {
    assertTrue(PublishingContracts.isPinterestAspectRatio(1000, 1500))
    assertTrue(PublishingContracts.isPinterestAspectRatio(600, 900))
    assertTrue(PublishingContracts.isPinterestAspectRatio(735, 1102)) // Standard web rounded pin
    assertTrue(PublishingContracts.isPinterestAspectRatio(1024, 1536))
  }

  @Test
  fun rejectsSquareAndHorizontalImages() {
    assertFalse(PublishingContracts.isPinterestAspectRatio(1000, 1000)) // 1:1 square
    assertFalse(PublishingContracts.isPinterestAspectRatio(1500, 1000)) // 3:2 landscape
    assertFalse(PublishingContracts.isPinterestAspectRatio(1920, 1080)) // 16:9 landscape
    assertFalse(PublishingContracts.isPinterestAspectRatio(0, 0))
  }

  @Test
  fun validatesMimeTypes() {
    val pngBytes = byteArrayOf(0x89.toByte(), 0x50.toByte(), 0x4E.toByte(), 0x47.toByte(), 0x0D, 0x0A, 0x1A, 0x0A)
    assertEquals("image/png", PublishingContracts.validatedImageMimeType("image/png", pngBytes))

    val jpegBytes = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 0xE0.toByte())
    assertEquals("image/jpeg", PublishingContracts.validatedImageMimeType("image/jpeg", jpegBytes))
  }

  @Test
  fun requiresHttps() {
    assertEquals("https://example.com", PublishingContracts.requireHttpsUrl("https://example.com/", "Site"))
  }

  @Test(expected = IllegalArgumentException::class)
  fun rejectsHttp() {
    PublishingContracts.requireHttpsUrl("http://example.com", "Site")
  }
}
