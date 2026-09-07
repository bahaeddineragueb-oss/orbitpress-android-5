package com.askinz.publisher

import org.json.JSONArray
import org.json.JSONObject

data class KeywordRecord(
  val id: String,
  val keyword: String,
  val contentType: String = "article",
  val nicheProfile: String = ContentProfileContract.FOOD,
  val priority: String = "normal",
  val categoryId: Int = 0,
  val categoryName: String = "",
  val pinterestBoardId: String = "",
  val status: String = "queued", // queued, generating, ready, published, failed, skipped
  val draftId: String? = null,
  val errorDetails: String = "",
  val siteId: String = SettingsPersistenceContract.DEFAULT_SITE_ID,
  val createdAt: Long = System.currentTimeMillis()
) {
  fun toJson(): JSONObject = JSONObject()
    .put("id", id)
    .put("keyword", keyword)
    .put("contentType", contentType)
    .put("nicheProfile", nicheProfile)
    .put("priority", priority)
    .put("categoryId", categoryId)
    .put("categoryName", categoryName)
    .put("pinterestBoardId", pinterestBoardId)
    .put("status", status)
    .put("draftId", draftId ?: JSONObject.NULL)
    .put("errorDetails", errorDetails)
    .put("siteId", siteId)
    .put("createdAt", createdAt)

  companion object {
    fun fromJson(json: JSONObject): KeywordRecord = KeywordRecord(
      id = json.optString("id", "kw-${System.currentTimeMillis()}"),
      keyword = json.optString("keyword", ""),
      contentType = json.optString("contentType", "article"),
      nicheProfile = json.optString("nicheProfile", ContentProfileContract.FOOD),
      priority = json.optString("priority", "normal"),
      categoryId = json.optInt("categoryId", 0),
      categoryName = json.optString("categoryName", ""),
      pinterestBoardId = json.optString("pinterestBoardId", ""),
      status = json.optString("status", "queued"),
      draftId = if (json.has("draftId") && !json.isNull("draftId")) json.optString("draftId") else null,
      errorDetails = json.optString("errorDetails", ""),
      siteId = json.optString("siteId", SettingsPersistenceContract.DEFAULT_SITE_ID),
      createdAt = json.optLong("createdAt", System.currentTimeMillis())
    )
  }
}

data class DraftRecord(
  val id: String,
  val keywordId: String = "",
  val title: String = "",
  val metaDescription: String = "",
  val slug: String = "",
  val contentType: String = "article",
  val categoryName: String = "",
  val nicheProfile: String = ContentProfileContract.FOOD,
  val htmlContent: String = "",
  val outlineJson: String = "[]",
  val internalLinksJson: String = "[]",
  val recipeJson: String = "{}",
  val recipesJson: String = "[]",
  val pinterestTitle: String = "",
  val pinterestDescription: String = "",
  val pinterestAltText: String = "",
  val focusKeyphrase: String = "",
  val seoTitle: String = "",
  val seoDescription: String = "",
  val images: Map<String, String> = emptyMap(), // kind -> local reference URI
  val generationStatus: String = "ready", // ready, published, failed
  val publishedUrl: String = "",
  val wordpressPostId: Int? = null,
  val publishedAt: Long? = null,
  val pinterestPinId: String = "",
  val pinterestStatus: String = "", // published, manual_review, not_configured, failed
  val errorDetails: String = "",
  val siteId: String = SettingsPersistenceContract.DEFAULT_SITE_ID,
  val createdAt: Long = System.currentTimeMillis()
) {
  fun toJson(): JSONObject {
    val json = JSONObject()
      .put("id", id)
      .put("keywordId", keywordId)
      .put("title", title)
      .put("metaDescription", metaDescription)
      .put("slug", slug)
      .put("contentType", contentType)
      .put("categoryName", categoryName)
      .put("nicheProfile", nicheProfile)
      .put("htmlContent", htmlContent)
      .put("outline", try { JSONArray(outlineJson) } catch (_: Exception) { JSONArray() })
      .put("internalLinks", try { JSONArray(internalLinksJson) } catch (_: Exception) { JSONArray() })
      .put("recipe", try { JSONObject(recipeJson) } catch (_: Exception) { JSONObject() })
      .put("recipes", try { JSONArray(recipesJson) } catch (_: Exception) { JSONArray() })
      .put("pinterestTitle", pinterestTitle)
      .put("pinterestDescription", pinterestDescription)
      .put("pinterestAltText", pinterestAltText)
      .put("pinterest", JSONObject().put("title", pinterestTitle).put("description", pinterestDescription).put("altText", pinterestAltText))
      .put("seo", JSONObject().put("focusKeyphrase", focusKeyphrase).put("title", seoTitle).put("metaDescription", seoDescription))
      .put("generationStatus", generationStatus)
      .put("publishedUrl", publishedUrl)
      .put("pinterestPinId", pinterestPinId)
      .put("pinterestStatus", pinterestStatus)
      .put("errorDetails", errorDetails)
      .put("siteId", siteId)
      .put("createdAt", createdAt)

    if (wordpressPostId != null) json.put("wordpressPostId", wordpressPostId)
    if (publishedAt != null) json.put("publishedAt", publishedAt)

    val imgObj = JSONObject()
    images.forEach { (k, v) -> imgObj.put(k, v) }
    json.put("images", imgObj)
    return json
  }

  companion object {
    fun fromJson(json: JSONObject): DraftRecord {
      val imagesMap = mutableMapOf<String, String>()
      val imgObj = json.optJSONObject("images")
      if (imgObj != null) {
        imgObj.keys().forEach { key ->
          val ref = imgObj.optString(key)
          if (ref.isNotBlank()) imagesMap[key] = ref
        }
      }
      val seoObj = json.optJSONObject("seo") ?: JSONObject()
      val pinObj = json.optJSONObject("pinterest") ?: JSONObject()

      return DraftRecord(
        id = json.optString("id", "draft-${System.currentTimeMillis()}"),
        keywordId = json.optString("keywordId", ""),
        title = json.optString("title", ""),
        metaDescription = json.optString("metaDescription", ""),
        slug = json.optString("slug", ""),
        contentType = json.optString("contentType", "article"),
        categoryName = json.optString("categoryName", ""),
        nicheProfile = json.optString("nicheProfile", ContentProfileContract.FOOD),
        htmlContent = json.optString("htmlContent", ""),
        outlineJson = json.optJSONArray("outline")?.toString() ?: "[]",
        internalLinksJson = json.optJSONArray("internalLinks")?.toString() ?: "[]",
        recipeJson = json.optJSONObject("recipe")?.toString() ?: "{}",
        recipesJson = json.optJSONArray("recipes")?.toString() ?: "[]",
        pinterestTitle = json.optString("pinterestTitle", pinObj.optString("title")),
        pinterestDescription = json.optString("pinterestDescription", pinObj.optString("description")),
        pinterestAltText = json.optString("pinterestAltText", pinObj.optString("altText")),
        focusKeyphrase = seoObj.optString("focusKeyphrase", ""),
        seoTitle = seoObj.optString("title", json.optString("title")),
        seoDescription = seoObj.optString("metaDescription", json.optString("metaDescription")),
        images = imagesMap,
        generationStatus = json.optString("generationStatus", "ready"),
        publishedUrl = json.optString("publishedUrl", ""),
        wordpressPostId = if (json.has("wordpressPostId") && !json.isNull("wordpressPostId")) json.optInt("wordpressPostId") else null,
        publishedAt = if (json.has("publishedAt") && !json.isNull("publishedAt")) json.optLong("publishedAt") else null,
        pinterestPinId = json.optString("pinterestPinId", ""),
        pinterestStatus = json.optString("pinterestStatus", ""),
        errorDetails = json.optString("errorDetails", ""),
        siteId = json.optString("siteId", SettingsPersistenceContract.DEFAULT_SITE_ID),
        createdAt = json.optLong("createdAt", System.currentTimeMillis())
      )
    }
  }
}

data class DraftVersionRecord(
  val id: String,
  val siteId: String,
  val draftId: String,
  val createdAt: Long,
  val snapshotJson: String
)

data class ActivityLogRecord(
  val id: String,
  val siteId: String,
  val action: String,
  val title: String,
  val createdAt: Long = System.currentTimeMillis(),
  val articleId: String = "",
  val wordpressStatus: String = "",
  val pinterestStatus: String = "",
  val publishedUrl: String = "",
  val errorDetails: String = ""
) {
  fun toJson(): JSONObject = JSONObject()
    .put("id", id)
    .put("siteId", siteId)
    .put("action", action)
    .put("title", title)
    .put("createdAt", createdAt)
    .put("articleId", articleId)
    .put("wordpressStatus", wordpressStatus)
    .put("pinterestStatus", pinterestStatus)
    .put("publishedUrl", publishedUrl)
    .put("errorDetails", errorDetails)

  companion object {
    fun fromJson(json: JSONObject): ActivityLogRecord = ActivityLogRecord(
      id = json.optString("id", "log-${System.currentTimeMillis()}"),
      siteId = json.optString("siteId", SettingsPersistenceContract.DEFAULT_SITE_ID),
      action = json.optString("action", ""),
      title = json.optString("title", ""),
      createdAt = json.optLong("createdAt", System.currentTimeMillis()),
      articleId = json.optString("articleId", ""),
      wordpressStatus = json.optString("wordpressStatus", ""),
      pinterestStatus = json.optString("pinterestStatus", ""),
      publishedUrl = json.optString("publishedUrl", ""),
      errorDetails = json.optString("errorDetails", "")
    )
  }
}

data class SiteProfileRecord(
  val id: String,
  val name: String
)

data class ContentBriefRecord(
  val id: String,
  val siteId: String,
  val keywordId: String,
  val audience: String,
  val intent: String,
  val outline: List<String>,
  val createdAt: Long = System.currentTimeMillis()
)

data class KeywordClusterRecord(
  val id: String,
  val siteId: String,
  val name: String,
  val keywords: List<String>
)

data class CalendarReminderRecord(
  val id: String,
  val siteId: String,
  val date: String,
  val title: String,
  val notes: String = "",
  val createdAt: Long = System.currentTimeMillis()
)

data class SiteSettings(
  val articleBaseUrl: String = "",
  val articleModel: String = "",
  val articleApiKey: String = "",
  val wordpressBaseUrl: String = "",
  val wordpressUsername: String = "",
  val wordpressAppPassword: String = "",
  val categoryId: String = "",
  val imageMode: String = "manual", // manual, automatic
  val imageProvider: String = "openai", // openai, cloudflare
  val imageBaseUrl: String = "",
  val imageModel: String = "",
  val imageApiKey: String = "",
  val cloudflareAccountId: String = "",
  val cloudflareModel: String = "@cf/black-forest-labs/flux-1-schnell",
  val cloudflareApiToken: String = "",
  val pinterestAccessToken: String = "",
  val pinterestBoardId: String = "",
  val pinterestPublishingMode: String = "manual_review", // manual_review, direct_api, disabled
  val profilePrompts: Map<String, String> = emptyMap()
) {
  fun isConfigured(): Boolean =
    articleBaseUrl.isNotBlank() && articleModel.isNotBlank() && articleApiKey.isNotBlank() &&
    wordpressBaseUrl.isNotBlank() && wordpressUsername.isNotBlank() && wordpressAppPassword.isNotBlank()

  fun isImageConfigured(): Boolean =
    if (imageProvider == "cloudflare") {
      cloudflareAccountId.isNotBlank() && cloudflareModel.isNotBlank() && cloudflareApiToken.isNotBlank()
    } else {
      imageApiKey.isNotBlank() && imageBaseUrl.isNotBlank() && imageModel.isNotBlank()
    }

  fun toJson(): JSONObject {
    val json = JSONObject()
      .put("articleBaseUrl", articleBaseUrl)
      .put("articleModel", articleModel)
      .put("articleApiKey", articleApiKey)
      .put("wordpressBaseUrl", wordpressBaseUrl)
      .put("wordpressUsername", wordpressUsername)
      .put("wordpressAppPassword", wordpressAppPassword)
      .put("categoryId", categoryId)
      .put("imageMode", imageMode)
      .put("imageProvider", imageProvider)
      .put("imageBaseUrl", imageBaseUrl)
      .put("imageModel", imageModel)
      .put("imageApiKey", imageApiKey)
      .put("cloudflareAccountId", cloudflareAccountId)
      .put("cloudflareModel", cloudflareModel)
      .put("cloudflareApiToken", cloudflareApiToken)
      .put("pinterestAccessToken", pinterestAccessToken)
      .put("pinterestBoardId", pinterestBoardId)
      .put("pinterestPublishingMode", pinterestPublishingMode)

    val promptsObj = JSONObject()
    profilePrompts.forEach { (k, v) -> promptsObj.put(k, v) }
    json.put("profilePrompts", promptsObj)
    return json
  }

  companion object {
    fun fromJson(json: JSONObject): SiteSettings {
      val prompts = mutableMapOf<String, String>()
      val pObj = json.optJSONObject("profilePrompts")
      if (pObj != null) {
        pObj.keys().forEach { k -> prompts[k] = pObj.optString(k) }
      }
      return SiteSettings(
        articleBaseUrl = json.optString("articleBaseUrl", ""),
        articleModel = json.optString("articleModel", ""),
        articleApiKey = json.optString("articleApiKey", ""),
        wordpressBaseUrl = json.optString("wordpressBaseUrl", ""),
        wordpressUsername = json.optString("wordpressUsername", ""),
        wordpressAppPassword = json.optString("wordpressAppPassword", ""),
        categoryId = json.optString("categoryId", ""),
        imageMode = json.optString("imageMode", "manual"),
        imageProvider = json.optString("imageProvider", "openai"),
        imageBaseUrl = json.optString("imageBaseUrl", ""),
        imageModel = json.optString("imageModel", ""),
        imageApiKey = json.optString("imageApiKey", ""),
        cloudflareAccountId = json.optString("cloudflareAccountId", ""),
        cloudflareModel = json.optString("cloudflareModel", "@cf/black-forest-labs/flux-1-schnell"),
        cloudflareApiToken = json.optString("cloudflareApiToken", ""),
        pinterestAccessToken = json.optString("pinterestAccessToken", ""),
        pinterestBoardId = json.optString("pinterestBoardId", ""),
        pinterestPublishingMode = json.optString("pinterestPublishingMode", "manual_review"),
        profilePrompts = prompts
      )
    }
  }
}

data class PinterestBoardRecord(val id: String, val name: String)

data class PinterestTrendRecord(
  val keyword: String,
  val pctGrowthYearOverYear: Double = 0.0,
  val pctGrowthMonthOverMonth: Double = 0.0
)
