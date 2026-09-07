package com.askinz.publisher

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.InputStream
import java.util.UUID

object ImageManager {
  private fun baseDirectory(context: Context): File =
    File(context.filesDir, "askinz-images").apply { mkdirs() }

  fun siteDirectory(context: Context, siteId: String): File {
    val cleanSite = siteId.trim().ifBlank { SettingsPersistenceContract.DEFAULT_SITE_ID }
      .replace(Regex("[^A-Za-z0-9_-]"), "_").take(80)
    return if (cleanSite == SettingsPersistenceContract.DEFAULT_SITE_ID) {
      baseDirectory(context)
    } else {
      File(baseDirectory(context), cleanSite).apply { mkdirs() }
    }
  }

  fun storeImageFromStream(
    context: Context,
    inputStream: InputStream,
    declaredMime: String,
    isPinterest: Boolean,
    siteId: String
  ): Pair<String, String> {
    val bytes = inputStream.readBytes()
    require(bytes.isNotEmpty() && bytes.size <= 12_000_000) { "Choose an image smaller than 12 MB." }
    val mime = PublishingContracts.validatedImageMimeType(declaredMime, bytes)

    if (isPinterest) {
      val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
      BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
      require(PublishingContracts.isPinterestAspectRatio(bounds.outWidth, bounds.outHeight)) {
        "Pinterest image must have a 2:3 vertical aspect ratio, such as 1000×1500 (received ${bounds.outWidth}×${bounds.outHeight})."
      }
    }

    val ext = when (mime) {
      "image/jpeg" -> "jpg"
      "image/webp" -> "webp"
      else -> "png"
    }

    val filename = "${UUID.randomUUID()}.$ext"
    val file = File(siteDirectory(context, siteId), filename)
    file.writeBytes(bytes)
    return "local://$filename" to mime
  }

  fun storeImageFromBytes(
    context: Context,
    bytes: ByteArray,
    declaredMime: String,
    isPinterest: Boolean,
    siteId: String
  ): Pair<String, String> {
    require(bytes.isNotEmpty() && bytes.size <= 12_000_000) { "Choose an image smaller than 12 MB." }
    val mime = PublishingContracts.validatedImageMimeType(declaredMime, bytes)

    if (isPinterest) {
      val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
      BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
      require(PublishingContracts.isPinterestAspectRatio(bounds.outWidth, bounds.outHeight)) {
        "Pinterest image must have a 2:3 vertical aspect ratio, such as 1000×1500 (received ${bounds.outWidth}×${bounds.outHeight})."
      }
    }

    val ext = when (mime) {
      "image/jpeg" -> "jpg"
      "image/webp" -> "webp"
      else -> "png"
    }

    val filename = "${UUID.randomUUID()}.$ext"
    val file = File(siteDirectory(context, siteId), filename)
    file.writeBytes(bytes)
    return "local://$filename" to mime
  }

  fun resolveFile(context: Context, reference: String, siteId: String): File {
    require(reference.startsWith("local://")) { "Invalid local image reference." }
    val filename = reference.removePrefix("local://")
    require(Regex("^[a-f0-9-]+\\.(jpg|png|webp)$").matches(filename)) { "Invalid image filename." }
    return File(siteDirectory(context, siteId), filename)
  }

  fun loadBitmap(context: Context, reference: String, siteId: String, maxDimension: Int = 800): Bitmap? {
    val file = resolveFile(context, reference, siteId)
    if (!file.exists() || !file.isFile) return null

    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(file.absolutePath, bounds)
    val width = bounds.outWidth
    val height = bounds.outHeight
    if (width <= 0 || height <= 0) return null

    var inSampleSize = 1
    val largest = maxOf(width, height)
    while (largest / inSampleSize > maxDimension) {
      inSampleSize *= 2
    }

    val options = BitmapFactory.Options().apply {
      this.inSampleSize = inSampleSize
      inPreferredConfig = Bitmap.Config.RGB_565
    }
    return BitmapFactory.decodeFile(file.absolutePath, options)
  }

  fun deleteImage(context: Context, reference: String, siteId: String) {
    try {
      val file = resolveFile(context, reference, siteId)
      if (file.exists()) file.delete()
    } catch (_: Exception) {}
  }

  fun cleanupUnusedImages(context: Context, referencedFiles: Set<String>, siteId: String) {
    try {
      val dir = siteDirectory(context, siteId)
      dir.listFiles()?.forEach { file ->
        val ref = "local://${file.name}"
        if (!referencedFiles.contains(ref)) {
          file.delete()
        }
      }
    } catch (_: Exception) {}
  }
}
