package com.askinz.publisher

import android.content.Context
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class PublishingService(private val context: Context) {
  companion object {
    private const val USER_AGENT = "OrbitPress/5.0 (Android; Mobile)"
    private const val CONNECT_TIMEOUT_MS = 25_000
    private const val READ_TIMEOUT_MS = 90_000
  }

  suspend fun http(
    url: String,
    method: String,
    headers: Map<String, String> = emptyMap(),
    body: ByteArray? = null
  ): String = withContext(Dispatchers.IO) {
    var currentUrl = url
    var redirects = 0
    while (redirects < 5) {
      val connection = (URL(currentUrl).openConnection() as HttpURLConnection).apply {
        requestMethod = method
        connectTimeout = CONNECT_TIMEOUT_MS
        readTimeout = READ_TIMEOUT_MS
        instanceFollowRedirects = false
        setRequestProperty("User-Agent", USER_AGENT)
        headers.forEach { (k, v) -> setRequestProperty(k, v) }
        if (body != null) {
          doOutput = true
          outputStream.use { it.write(body) }
        }
      }

      try {
        val status = connection.responseCode
        if (status in listOf(301, 302, 307, 308)) {
          val location = connection.getHeaderField("Location")
          if (!location.isNullOrBlank()) {
            currentUrl = location
            redirects++
            continue
          }
        }

        val stream = if (status in 200..299) connection.inputStream else connection.errorStream
        val response = stream?.use { BufferedInputStream(it).readBytes().toString(StandardCharsets.UTF_8) } ?: ""
        if (status !in 200..299) {
          throw IllegalStateException("Request failed ($status): ${response.take(300)}")
        }
        return@withContext response
      } finally {
        connection.disconnect()
      }
    }
    throw IllegalStateException("Too many redirects.")
  }

  private fun wpHeaders(settings: SiteSettings): Map<String, String> {
    val raw = "${settings.wordpressUsername}:${settings.wordpressAppPassword}".toByteArray(StandardCharsets.UTF_8)
    return mapOf("Authorization" to "Basic ${Base64.encodeToString(raw, Base64.NO_WRAP)}")
  }

  private fun wpRoot(baseUrl: String): String =
    PublishingContracts.requireHttpsUrl(baseUrl, "WordPress URL").removeSuffix("/wp-json")

  private fun chatEndpoint(baseUrl: String): String =
    PublishingContracts.requireHttpsUrl(baseUrl, "Article API URL").let {
      if (it.endsWith("/chat/completions")) it else "$it/chat/completions"
    }

  // --- Article Generation ---
  suspend fun generateArticle(
    settings: SiteSettings,
    keyword: String,
    contentType: String,
    nicheProfile: String,
    categoryName: String,
    existingTitles: List<String>
  ): DraftRecord = withContext(Dispatchers.IO) {
    require(settings.isConfigured()) { "Complete and save the Article API and WordPress settings first." }
    val normalizedKeyword = keyword.trim()
    require(normalizedKeyword.length in 2..160) { "Enter a keyword between 2 and 160 characters." }

    val normalizedProfile = ContentProfileContract.normalize(nicheProfile)
    val isFood = ContentProfileContract.isFood(normalizedProfile)
    val provider = ProviderCompatibilityContract.normalize(settings.articleBaseUrl, settings.articleModel)
    val endpoint = chatEndpoint(provider.baseUrl)
    val siteBaseUrl = settings.wordpressBaseUrl.trim().trimEnd('/')
    val titlesSummary = existingTitles.take(20).joinToString(" | ") { it.take(255) }
    val requestedRecipeCount = if (isFood) RecipeRequestContract.requestedCount(normalizedKeyword) else 0

    val profileInstruction = when (normalizedProfile) {
      ContentProfileContract.GARDENING -> "Create a practical English gardening article. Cover materials, steps, timing, care, safety, and realistic variations. Do not invent recipes or nutrition fields."
      ContentProfileContract.HOME_DECOR -> "Create a practical English home decor article. Cover style decisions, room planning, materials, measurements where useful, budget-aware options, and implementation steps. Do not invent recipes or nutrition fields."
      ContentProfileContract.CUSTOM -> "Create a practical English informational article for the requested topic. Infer the correct audience, structure, terminology, and actionable steps from the keyword. Do not invent recipe fields unless the topic genuinely requires them."
      else -> ContentProfileContract.halalRule()
    }

    val customPrompt = settings.profilePrompts[normalizedProfile]?.trim().orEmpty().take(4000)

    val formatRequirements = if (isFood) """
      If the keyword contains a number of recipes, generate exactly that many distinct, fully populated recipes. For example, “5 fall recipes” means exactly 5 recipes. Never list recipe names only and never replace a requested roundup with a single recipe or summary.
      For one specific cookable dish, select recipe. recipe must contain sensible ingredients, 4 to 9 concrete steps, ISO 8601 durations such as PT15M, yield, cuisine, and 1 to 3 useful notes. Do not put a recipe card inside htmlContent.
      For a roundup or any request containing multiple recipes, select article, return one fully populated object in recipes for every recipe, give every object its own title, complete ingredient quantities, 4 to 9 concrete numbered instructions, ISO 8601 durations, yield, cuisine, and 1 to 3 useful notes, and write detailed sections in htmlContent for the collection. Do not put recipe cards inside htmlContent; the app adds one complete card per recipes object.
      For informational content with no recipes, select article; recipe.isRecipe must be false and every other recipe field must be empty or an empty array, and recipes must be an empty array.
    """.trimIndent() else """
      This is a general article profile, not a recipe request. Set contentType to article, keep recipe.isRecipe false, and return an empty recipes array. Use detailed, actionable H2 sections appropriate to the selected niche and do not invent food, recipe, nutrition, or medical fields.
    """.trimIndent()

    val prompt = """
      Create a complete, long-form English ${if (isFood) "food" else "general"} article from this keyword: $normalizedKeyword
      Requested format: $contentType
      Requested complete recipe count: ${if (requestedRecipeCount > 0) requestedRecipeCount else "not explicitly numbered"}
      $formatRequirements
      Preferred category: ${categoryName.ifBlank { if (isFood) "Choose the best existing food category" else "Choose the best existing category for this niche" }}
      Existing site titles to avoid duplicating: ${titlesSummary.ifBlank { "None supplied" }}
      Approved site homepage for internal linking: ${siteBaseUrl.ifBlank { "No site URL supplied; do not invent URLs" }}

      Return valid JSON only with this exact structure:
      {"title":"","metaDescription":"","slug":"","contentType":"recipe|article","categoryName":"","outline":[{"heading":"","keyPoints":[""]}],"htmlContent":"","internalLinks":[{"anchor":"","url":"","reason":""}],"recipe":{"isRecipe":false,"description":"","prepTime":"","cookTime":"","totalTime":"","recipeYield":"","cuisine":"","ingredients":[],"instructions":[{"name":"","text":""}],"notes":[]},"recipes":[{"title":"","isRecipe":true,"description":"","prepTime":"","cookTime":"","totalTime":"","recipeYield":"","cuisine":"","ingredients":[""],"instructions":[{"name":"","text":""}],"notes":[""]}],"pinterest":{"title":"","description":"","altText":""},"seo":{"focusKeyphrase":"","title":"","metaDescription":""}}

      Requirements:
      - Infer practical search intent and create a distinct article title. The SEO title must be 50-60 characters, never exceed 60 characters, and begin with the exact focus keyphrase.
      - The SEO meta description must be 120-160 characters, contain the exact focus keyphrase once, and communicate a clear benefit and reason to click.
      - Use the exact focus keyphrase naturally in the first paragraph, the SEO title, the SEO meta description, the slug, at least one H2 or H3, image alt text, and at least 3 total times in the body for a long-form article. Use synonyms elsewhere; never keyword-stuff.
      - Provide 3 to 6 outline H2 sections. htmlContent must start with a clear 2-3 sentence introduction that contains the exact focus keyphrase in its first sentence, then use descriptive H2/H3 headings. Do not put the title in an H1 because WordPress supplies it.
      - Include at least one genuine internal HTML link in htmlContent. Use only the approved site homepage URL supplied above when no article URL list is supplied; never invent a path or URL. Return the same link in internalLinks with its anchor, url, and reason.
      - Keep paragraphs short, use transition words, active voice, and answer the search intent immediately. Do not repeat the keyphrase unnaturally.
      - Create a concise natural Pinterest SEO title, a standalone Pinterest description, and descriptive image alt text.
      - Create an SEO object with focusKeyphrase, title, and metaDescription.
      - Profile-specific editorial rule: $profileInstruction
      ${if (customPrompt.isBlank()) "" else "- Custom editorial prompt: $customPrompt"}
    """.trimIndent()

    val body = JSONObject()
      .put("model", provider.model)
      .put("max_tokens", provider.maxOutputTokens)
      .put("messages", JSONArray()
        .put(JSONObject().put("role", "system").put("content", "You are an exacting English editorial strategist. Produce genuinely helpful original content for practical search intent while honoring the selected content profile. Write natural English. Use only semantic HTML allowed in a WordPress post body."))
        .put(JSONObject().put("role", "user").put("content", prompt)))
      .put("temperature", 0.7)
      .put("response_format", articleResponseFormat())

    val headers = mapOf("Authorization" to "Bearer ${settings.articleApiKey}", "Content-Type" to "application/json")
    val response = try {
      http(endpoint, "POST", headers, body.toString().toByteArray())
    } catch (error: IllegalStateException) {
      val message = error.message.orEmpty().lowercase()
      if (!(message.contains("response_format") || message.contains("json_schema") || message.contains("unsupported"))) {
        throw IllegalStateException(ProviderCompatibilityContract.diagnostic(error.message.orEmpty()))
      }
      body.remove("response_format")
      http(endpoint, "POST", headers, body.toString().toByteArray())
    }

    val content = JSONObject(response).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content")
    val json = content.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
    var draftJson = DraftContract.normalize(JSONObject(json), categoryName)

    if (requestedRecipeCount > 0 && !LongFormCompletenessContract.validate(draftJson, requestedRecipeCount).valid) {
      val issue = LongFormCompletenessContract.validate(draftJson, requestedRecipeCount).reason
      val repairPrompt = prompt + "\nCRITICAL COMPLETENESS REPAIR: return exactly $requestedRecipeCount fully populated objects in recipes[]. Previous output problem: $issue"
      val repairBody = body.put("messages", JSONArray()
        .put(JSONObject().put("role", "system").put("content", "You are a strict recipe-roundup completion editor. Never summarize requested recipes; return every complete recipe."))
        .put(JSONObject().put("role", "user").put("content", repairPrompt)))
      val repairedResponse = http(endpoint, "POST", headers, repairBody.toString().toByteArray())
      val repairedContent = JSONObject(repairedResponse).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content")
      val repairedJson = repairedContent.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
      draftJson = DraftContract.normalize(JSONObject(repairedJson), categoryName)
    }

    DraftRecord.fromJson(draftJson).copy(nicheProfile = normalizedProfile)
  }

  private fun articleResponseFormat(): JSONObject {
    val schema = JSONObject(
      """{
        "type":"object",
        "properties":{
          "title":{"type":"string"},"metaDescription":{"type":"string"},"slug":{"type":"string"},
          "contentType":{"type":"string","enum":["recipe","article"]},"categoryName":{"type":"string"},
          "outline":{"type":"array","items":{"type":"object","properties":{"heading":{"type":"string"},"keyPoints":{"type":"array","items":{"type":"string"}}},"required":["heading","keyPoints"],"additionalProperties":false}},
          "htmlContent":{"type":"string"},
          "internalLinks":{"type":"array","items":{"type":"object","properties":{"anchor":{"type":"string"},"url":{"type":"string"},"reason":{"type":"string"}},"required":["anchor","url","reason"],"additionalProperties":false}},
          "recipe":{"type":"object","properties":{"isRecipe":{"type":"boolean"},"description":{"type":"string"},"prepTime":{"type":"string"},"cookTime":{"type":"string"},"totalTime":{"type":"string"},"recipeYield":{"type":"string"},"cuisine":{"type":"string"},"ingredients":{"type":"array","items":{"type":"string"}},"instructions":{"type":"array","items":{"type":"object","properties":{"name":{"type":"string"},"text":{"type":"string"}},"required":["name","text"],"additionalProperties":false}},"notes":{"type":"array","items":{"type":"string"}}},"required":["isRecipe","description","prepTime","cookTime","totalTime","recipeYield","cuisine","ingredients","instructions","notes"],"additionalProperties":false},
          "recipes":{"type":"array","maxItems":12,"items":{"type":"object","properties":{"title":{"type":"string"},"isRecipe":{"type":"boolean"},"description":{"type":"string"},"prepTime":{"type":"string"},"cookTime":{"type":"string"},"totalTime":{"type":"string"},"recipeYield":{"type":"string"},"cuisine":{"type":"string"},"ingredients":{"type":"array","items":{"type":"string"}},"instructions":{"type":"array","items":{"type":"object","properties":{"name":{"type":"string"},"text":{"type":"string"}},"required":["name","text"],"additionalProperties":false}},"notes":{"type":"array","items":{"type":"string"}}},"required":["title","isRecipe","description","prepTime","cookTime","totalTime","recipeYield","cuisine","ingredients","instructions","notes"],"additionalProperties":false}},
          "pinterest":{"type":"object","properties":{"title":{"type":"string"},"description":{"type":"string"},"altText":{"type":"string"}},"required":["title","description","altText"],"additionalProperties":false},
          "seo":{"type":"object","properties":{"focusKeyphrase":{"type":"string"},"title":{"type":"string"},"metaDescription":{"type":"string"}},"required":["focusKeyphrase","title","metaDescription"],"additionalProperties":false}
        },
        "required":["title","metaDescription","slug","contentType","categoryName","outline","htmlContent","internalLinks","recipe","recipes","pinterest","seo"],
        "additionalProperties":false
      }""".trimIndent()
    )
    return JSONObject().put("type", "json_schema").put("json_schema", JSONObject().put("name", "askinz_food_article").put("strict", true).put("schema", schema))
  }

  // --- Image Generation ---
  suspend fun generateImageBytes(
    settings: SiteSettings,
    prompt: String,
    isPinterest: Boolean
  ): Pair<ByteArray, String> = withContext(Dispatchers.IO) {
    if (settings.imageProvider == "cloudflare") {
      val accountId = settings.cloudflareAccountId.trim()
      val model = settings.cloudflareModel.trim().ifBlank { "@cf/black-forest-labs/flux-1-schnell" }
      val token = settings.cloudflareApiToken.trim()
      require(accountId.isNotBlank() && token.isNotBlank()) { "Complete the Cloudflare Account ID and API Token." }

      val url = "https://api.cloudflare.com/client/v4/accounts/$accountId/ai/run/" + URLEncoder.encode(model, "UTF-8").replace("+", "%20")
      val body = JSONObject().put("prompt", prompt).put("steps", 4)
      val response = JSONObject(http(url, "POST", mapOf("Authorization" to "Bearer $token", "Content-Type" to "application/json"), body.toString().toByteArray()))
      val result = response.optJSONObject("result") ?: throw IllegalStateException("Cloudflare Workers AI returned no result.")
      val base64 = result.optString("image").trim()
      require(base64.isNotBlank()) { "Cloudflare Workers AI returned no Base64 image." }
      return@withContext Base64.decode(base64, Base64.DEFAULT) to "image/jpeg"
    }

    val baseUrl = settings.imageBaseUrl.trim().trimEnd('/')
    val model = settings.imageModel.trim()
    val apiKey = settings.imageApiKey.trim()
    require(baseUrl.isNotBlank() && model.isNotBlank() && apiKey.isNotBlank()) { "Complete the Image Generator API settings." }

    val size = if (isPinterest) "1024x1536" else "1536x1024"
    val body = JSONObject().put("model", model).put("prompt", prompt).put("n", 1).put("size", size).put("response_format", "b64_json")
    val response = JSONObject(http("$baseUrl/images/generations", "POST", mapOf("Authorization" to "Bearer $apiKey", "Content-Type" to "application/json"), body.toString().toByteArray()))
    val image = response.optJSONArray("data")?.optJSONObject(0) ?: throw IllegalStateException("The Image Generator API returned no image.")
    val base64 = image.optString("b64_json").trim()
    require(base64.isNotBlank()) { "The Image Generator API returned no Base64 image." }
    return@withContext Base64.decode(base64, Base64.DEFAULT) to "image/png"
  }

  // --- WordPress Operations ---
  suspend fun testWordPressConnection(settings: SiteSettings): String = withContext(Dispatchers.IO) {
    val root = wpRoot(settings.wordpressBaseUrl)
    val response = http("$root/wp-json/wp/v2/users/me?context=edit", "GET", wpHeaders(settings))
    val profile = JSONObject(response)
    profile.optString("name", profile.optString("slug", "WordPress account"))
  }

  suspend fun fetchCategories(settings: SiteSettings): List<WordPressCategoryRecord> = withContext(Dispatchers.IO) {
    val root = wpRoot(settings.wordpressBaseUrl)
    val fetched = mutableListOf<WordPressCategoryRecord>()
    for (page in 1..10) {
      val data = try {
        JSONArray(http("$root/wp-json/wp/v2/categories?context=edit&per_page=100&hide_empty=false&page=$page&orderby=name&order=asc", "GET", wpHeaders(settings)))
      } catch (e: IllegalStateException) {
        if (e.message.orEmpty().contains("400")) break else throw e
      }
      for (i in 0 until data.length()) {
        val item = data.getJSONObject(i)
        fetched.add(WordPressCategoryRecord(item.getInt("id"), item.getString("name")))
      }
      if (data.length() < 100) break
    }
    CategorySyncContracts.normalize(fetched)
  }

  suspend fun uploadMedia(
    settings: SiteSettings,
    file: File,
    mimeType: String,
    basename: String,
    altText: String
  ): Pair<Int, String> = withContext(Dispatchers.IO) {
    val root = wpRoot(settings.wordpressBaseUrl)
    val bytes = file.readBytes()
    val ext = file.extension.ifBlank { "jpg" }
    val response = http(
      "$root/wp-json/wp/v2/media",
      "POST",
      wpHeaders(settings) + mapOf(
        "Content-Type" to mimeType,
        "Content-Disposition" to "attachment; filename=\"$basename.$ext\""
      ),
      bytes
    )
    val media = JSONObject(response)
    val id = media.getInt("id")
    val sourceUrl = media.getString("source_url")

    // Update alt text
    try {
      http(
        "$root/wp-json/wp/v2/media/$id",
        "POST",
        wpHeaders(settings) + mapOf("Content-Type" to "application/json"),
        JSONObject().put("alt_text", altText.take(320)).toString().toByteArray()
      )
    } catch (_: Exception) {}

    id to sourceUrl
  }

  suspend fun publishArticle(
    settings: SiteSettings,
    draft: DraftRecord,
    siteId: String
  ): Triple<Int, String, JSONObject> = withContext(Dispatchers.IO) {
    val root = wpRoot(settings.wordpressBaseUrl)
    val slug = DraftContract.cleanSlug(draft.slug)
    require(slug.isNotBlank()) { "Invalid slug." }

    // Duplicate check
    val duplicates = JSONArray(http("$root/wp-json/wp/v2/posts?slug=${URLEncoder.encode(slug, "UTF-8")}&context=edit&per_page=1", "GET", wpHeaders(settings)))
    require(duplicates.length() == 0) { "A WordPress post with this slug already exists. Change the title or slug first." }

    val featuredRef = draft.images["featured"] ?: throw IllegalArgumentException("Featured image is required.")
    val pinterestRef = draft.images["pinterest"] ?: throw IllegalArgumentException("Pinterest image is required.")

    val featuredFile = ImageManager.resolveFile(context, featuredRef, siteId)
    val pinterestFile = ImageManager.resolveFile(context, pinterestRef, siteId)

    val featuredMime = PublishingContracts.validatedImageMimeType("image/${featuredFile.extension}", featuredFile.readBytes())
    val pinterestMime = PublishingContracts.validatedImageMimeType("image/${pinterestFile.extension}", pinterestFile.readBytes())

    val featuredAlt = PublishingContracts.featuredImageAltText(draft.title, draft.contentType)
    val pinterestAlt = draft.pinterestAltText.ifBlank { PublishingContracts.pinterestImageAltText(draft.pinterestTitle, draft.title) }

    val categoryId = draft.categoryName.toIntOrNull() ?: settings.categoryId.toIntOrNull() ?: 0
    if (categoryId > 0) {
      PublishingContracts.requireExistingCategoryId(categoryId)
    }

    val (featuredId, featuredUrl) = uploadMedia(settings, featuredFile, featuredMime, "$slug-featured", featuredAlt)
    val (_, pinterestUrl) = uploadMedia(settings, pinterestFile, pinterestMime, "$slug-pinterest", pinterestAlt)

    val pinTitle = draft.pinterestTitle.ifBlank { draft.title }.take(100)
    val pinDesc = draft.pinterestDescription.ifBlank { draft.metaDescription }.take(800)

    val shareUrl = "https://www.pinterest.com/pin/create/button/?url=" +
      URLEncoder.encode("$root/$slug/", "UTF-8") +
      "&media=" + URLEncoder.encode(pinterestUrl, "UTF-8") +
      "&description=" + URLEncoder.encode(pinDesc, "UTF-8")

    val featuredBlock = WordPressMarkup.featuredImage(featuredUrl, featuredAlt)
    val pinBlock = WordPressMarkup.pinterestSaveButton(shareUrl, pinTitle, pinDesc, pinterestUrl, pinterestAlt)
    val schema = DraftContract.buildSchema(draft.toJson(), "$root/$slug/", listOf(featuredUrl, pinterestUrl))

    val postBody = JSONObject()
      .put("title", draft.title)
      .put("slug", slug)
      .put("status", "publish")
      .put("content", featuredBlock + draft.htmlContent + pinBlock + WordPressMarkup.structuredData(schema.toString()))
      .put("excerpt", draft.metaDescription)
      .put("featured_media", featuredId)

    if (categoryId > 0) {
      postBody.put("categories", JSONArray().put(categoryId))
    }

    val publishedRes = JSONObject(http("$root/wp-json/wp/v2/posts", "POST", wpHeaders(settings) + mapOf("Content-Type" to "application/json"), postBody.toString().toByteArray()))
    val postId = publishedRes.getInt("id")
    val postUrl = publishedRes.optString("link", "$root/$slug/")

    // Save Pinterest Metadata to WP plugin if installed
    val seoObj = JSONObject().put("focusKeyphrase", draft.focusKeyphrase).put("title", draft.seoTitle).put("metaDescription", draft.seoDescription)
    val metaSaved = try {
      val metaBody = JSONObject()
        .put("post_id", postId)
        .put("title", pinTitle)
        .put("description", pinDesc)
        .put("alt_text", pinterestAlt)
        .put("image", pinterestUrl)
        .put("focus_keyphrase", draft.focusKeyphrase)
        .put("seo_title", draft.seoTitle)
        .put("seo_description", draft.seoDescription)
      http("$root/wp-json/orbitpress/v1/pinterest-meta", "POST", wpHeaders(settings) + mapOf("Content-Type" to "application/json"), metaBody.toString().toByteArray())
      true
    } catch (_: Exception) {
      false
    }

    // Direct Pinterest API if enabled and token present
    val pinToken = settings.pinterestAccessToken.trim()
    val pinBoard = settings.pinterestBoardId.trim()
    val pinResult = if (settings.pinterestPublishingMode == "disabled") {
      JSONObject().put("published", false).put("skipped", true)
    } else if (settings.pinterestPublishingMode == "manual_review") {
      JSONObject().put("published", false).put("manualReview", true).put("metadataSaved", metaSaved).put("composerUrl", shareUrl)
    } else if (pinToken.isNotBlank() && pinBoard.isNotBlank()) {
      try {
        val pinBytes = pinterestFile.readBytes()
        val pinApiBody = JSONObject()
          .put("board_id", pinBoard)
          .put("title", pinTitle)
          .put("alt_text", pinterestAlt)
          .put("description", pinDesc)
          .put("link", postUrl)
          .put("media_source", JSONObject()
            .put("source_type", "image_base64")
            .put("is_standard", false)
            .put("content_type", pinterestMime)
            .put("data", Base64.encodeToString(pinBytes, Base64.NO_WRAP)))
        val pinRes = JSONObject(http("https://api.pinterest.com/v5/pins", "POST", mapOf("Authorization" to "Bearer $pinToken", "Content-Type" to "application/json"), pinApiBody.toString().toByteArray()))
        JSONObject().put("published", true).put("pinId", pinRes.optString("id"))
      } catch (e: Exception) {
        JSONObject().put("published", false).put("error", e.message ?: "Pinterest API failed.")
      }
    } else {
      JSONObject().put("published", false).put("skipped", true)
    }

    Triple(postId, postUrl, pinResult)
  }

  // --- Pinterest Boards & Trends ---
  suspend fun fetchPinterestBoards(token: String): List<PinterestBoardRecord> = withContext(Dispatchers.IO) {
    require(token.isNotBlank()) { "Add a Pinterest Access Token in Settings first." }
    val response = JSONObject(http("https://api.pinterest.com/v5/boards?page_size=250", "GET", mapOf("Authorization" to "Bearer $token", "Content-Type" to "application/json")))
    val items = response.optJSONArray("items") ?: JSONArray()
    val list = mutableListOf<PinterestBoardRecord>()
    for (i in 0 until items.length()) {
      val obj = items.getJSONObject(i)
      list.add(PinterestBoardRecord(obj.getString("id"), obj.getString("name")))
    }
    list
  }

  suspend fun fetchPinterestTrends(
    token: String,
    region: String,
    trendType: String,
    limit: Int,
    profile: String
  ): List<PinterestTrendRecord> = withContext(Dispatchers.IO) {
    require(token.isNotBlank()) { "Add a Pinterest Access Token in Settings first." }
    require(PinterestTrendsContract.isValidRegion(region)) { "Invalid region." }
    require(PinterestTrendsContract.isValidTrendType(trendType)) { "Invalid trend type." }
    val clampedLimit = PinterestTrendsContract.clampLimit(limit)
    val interest = PinterestTrendsContract.interestForProfile(profile)
    val query = buildString {
      append("https://api.pinterest.com/v5/trends/keywords/")
      append(region).append("/top/").append(trendType).append("?limit=").append(clampedLimit)
      if (interest.isNotBlank()) append("&interests=").append(URLEncoder.encode(interest, "UTF-8"))
    }
    val response = JSONObject(http(query, "GET", mapOf("Authorization" to "Bearer $token", "Content-Type" to "application/json")))
    val trends = response.optJSONArray("trends") ?: JSONArray()
    val list = mutableListOf<PinterestTrendRecord>()
    for (i in 0 until trends.length()) {
      val t = trends.getJSONObject(i)
      list.add(PinterestTrendRecord(
        keyword = t.optString("keyword", ""),
        pctGrowthYearOverYear = t.optDouble("pct_growth_yoy", 0.0),
        pctGrowthMonthOverMonth = t.optDouble("pct_growth_mom", 0.0)
      ))
    }
    list
  }
}
