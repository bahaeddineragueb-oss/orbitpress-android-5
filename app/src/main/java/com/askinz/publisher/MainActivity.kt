package com.askinz.publisher

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.UUID

private const val PUBLISH_NOTIFICATION_CHANNEL_ID = "publish_results"

class MainActivity : Activity() {
  private lateinit var webView: WebView
  private var fileCallback: ValueCallback<Array<Uri>>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    webView = WebView(this)
    webView.settings.javaScriptEnabled = true
    webView.settings.domStorageEnabled = true
    webView.settings.allowFileAccess = false
    webView.settings.allowContentAccess = true
    webView.settings.javaScriptCanOpenWindowsAutomatically = false
    webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
    webView.settings.setSupportZoom(false)
    webView.settings.builtInZoomControls = false
    webView.settings.displayZoomControls = false
    WebView.setWebContentsDebuggingEnabled(false)
    webView.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url ?: return false
        if (url.scheme == "https" || url.scheme == "http") {
          startActivity(Intent(Intent.ACTION_VIEW, url))
          return true
        }
        return false
      }
    }
    webView.webChromeClient = object : WebChromeClient() {
      override fun onShowFileChooser(view: WebView?, callback: ValueCallback<Array<Uri>>, params: FileChooserParams): Boolean {
        fileCallback?.onReceiveValue(null)
        fileCallback = callback
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
          addCategory(Intent.CATEGORY_OPENABLE)
          type = "image/*"
        }
        startActivityForResult(intent, FILE_PICKER_REQUEST)
        return true
      }
    }
    webView.addJavascriptInterface(NativeBridge(this, webView), "Native")
    webView.loadUrl("file:///android_asset/index.html")
    setContentView(webView)
    createNotificationChannel()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST)
    }
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(PUBLISH_NOTIFICATION_CHANNEL_ID, "Publish results", NotificationManager.IMPORTANCE_DEFAULT).apply {
        description = "Results from publish actions started inside OrbitPress"
      }
      getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode != FILE_PICKER_REQUEST) return
    val callback = fileCallback ?: return
    fileCallback = null
    callback.onReceiveValue(if (resultCode == RESULT_OK && data?.data != null) arrayOf(data.data!!) else null)
  }

  companion object {
    private const val FILE_PICKER_REQUEST = 7001
    private const val NOTIFICATION_PERMISSION_REQUEST = 7002
  }
}

private class NativeBridge(private val activity: Activity, private val webView: WebView) {
  private val preferences by lazy {
    val key = MasterKey.Builder(activity).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    EncryptedSharedPreferences.create(
      activity,
      "askinz_secure_settings",
      key,
      EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
      EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )
  }

  @JavascriptInterface fun loadSettingsLock(): String = JSONObject()
    .put("enabled", preferences.getString("settingsLockHash", "").orEmpty().isNotBlank())
    .toString()

  @JavascriptInterface fun saveSettingsLock(pin: String) {
    val normalized = SettingsLockContract.normalizePin(pin)
    val editor = preferences.edit()
    if (normalized.isBlank()) {
      editor.remove("settingsLockHash").apply()
      return
    }
    require(SettingsLockContract.isValidPin(normalized)) { "Settings PIN must contain 4 to 12 digits." }
    editor.putString("settingsLockHash", SettingsLockContract.hashPin(normalized)).apply()
  }

  @JavascriptInterface fun verifySettingsLock(pin: String): Boolean = try {
    SettingsLockContract.matches(pin, preferences.getString("settingsLockHash", "").orEmpty())
  } catch (_: Exception) {
    false
  }

  @JavascriptInterface fun loadSettings(siteId: String): String {
    val saved = storedSettings(SettingsPersistenceContract.canonicalSiteId(siteId))
    return JSONObject()
      .put("articleBaseUrl", saved.optString("articleBaseUrl"))
      .put("articleModel", saved.optString("articleModel"))
      .put("wordpressBaseUrl", saved.optString("wordpressBaseUrl"))
      .put("wordpressUsername", saved.optString("wordpressUsername"))
      .put("categoryId", saved.optString("categoryId"))
      .put("imageMode", saved.optString("imageMode", "manual"))
      .put("imageProvider", saved.optString("imageProvider", "openai"))
      .put("imageBaseUrl", saved.optString("imageBaseUrl"))
      .put("imageModel", saved.optString("imageModel"))
      .put("cloudflareAccountId", saved.optString("cloudflareAccountId"))
      .put("cloudflareModel", saved.optString("cloudflareModel", "@cf/black-forest-labs/flux-1-schnell"))
      .put("pinterestBoardId", saved.optString("pinterestBoardId"))
      .put("pinterestPublishingMode", saved.optString("pinterestPublishingMode", "manual_review"))
      .put("profilePrompts", saved.optJSONObject("profilePrompts") ?: JSONObject())
      .put("articleApiConfigured", saved.optString("articleApiKey").isNotBlank())
      .put("wordpressConfigured", saved.optString("wordpressAppPassword").isNotBlank())
      .put("imageConfigured", (saved.optString("imageProvider", "openai") == "cloudflare" && saved.optString("cloudflareAccountId").isNotBlank() && saved.optString("cloudflareApiToken").isNotBlank() && saved.optString("cloudflareModel").isNotBlank()) || saved.optString("imageApiKey").isNotBlank())
      .put("pinterestAccessTokenConfigured", saved.optString("pinterestAccessToken").isNotBlank())
      .toString()
  }

  @JavascriptInterface fun saveSettings(json: String, siteId: String) {
    require(json.length <= 30_000) { "Settings payload is too large." }
    val incoming = JSONObject(json)
    val canonicalSiteId = SettingsPersistenceContract.canonicalSiteId(siteId)
    val saved = SettingsPersistenceContract.merge(storedSettings(canonicalSiteId), incoming)
    val profiles = try { JSONObject(preferences.getString("settingsBySite", "{}") ?: "{}") } catch (_: Exception) { JSONObject() }
    profiles.put(canonicalSiteId, saved)
    preferences.edit().putString("settingsBySite", profiles.toString()).apply()
  }

  @JavascriptInterface fun loadWorkspace(): String = preferences.getString("workspace", "{}") ?: "{}"

  @JavascriptInterface fun saveWorkspace(json: String) {
    require(json.length <= 1_500_000) { "The local workspace is too large. Remove older drafts before adding more." }
    preferences.edit().putString("workspace", json).apply()
  }

  private fun storedSettings(siteId: String = SettingsPersistenceContract.DEFAULT_SITE_ID): JSONObject = try {
    val profiles = JSONObject(preferences.getString("settingsBySite", "{}") ?: "{}")
    val scoped = profiles.optJSONObject(SettingsPersistenceContract.canonicalSiteId(siteId))
    if (scoped != null) scoped else JSONObject(preferences.getString("settings", "{}") ?: "{}")
  } catch (_: Exception) {
    JSONObject()
  }

  private fun requireStoredSettings(request: JSONObject): JSONObject {
    val settings = storedSettings(request.optString("siteId", "site-default"))
    val required = listOf("articleBaseUrl", "articleModel", "articleApiKey", "wordpressBaseUrl", "wordpressUsername", "wordpressAppPassword")
    require(required.all { settings.optString(it).isNotBlank() }) { "Complete and save the Article API and WordPress settings first." }
    PublishingContracts.requireHttpsUrl(settings.getString("articleBaseUrl"), "Article API URL")
    PublishingContracts.requireHttpsUrl(settings.getString("wordpressBaseUrl"), "WordPress URL")
    return settings
  }

  @JavascriptInterface fun call(requestJson: String) {
    val request = try { JSONObject(requestJson) } catch (_: Exception) { return }
    val id = request.optString("id", UUID.randomUUID().toString())
    Thread {
      val result = try {
        when (request.getString("type")) {
          "generate" -> generate(request)
          "generateImage" -> generateImage(request)
          "categories" -> categories(request)
          "pinterestBoards" -> pinterestBoards(request)
          "pinterestTrends" -> pinterestTrends(request)
          "syncPublishedPosts" -> syncPublishedPosts(request)
          "testConnection" -> testConnection(request)
          "storeImage" -> storeImage(request)
          "loadImage" -> loadImage(request)
          "removeImage" -> removeImage(request)
          "repairPreview" -> repairPreview(request)
          "repairApply" -> repairApply(request)
          "publish" -> publish(request)
          else -> throw IllegalArgumentException("Unknown operation.")
        }
      } catch (error: Exception) {
        JSONObject().put("ok", false).put("message", error.message ?: "An unexpected error occurred.")
      }
      if (request.optString("type") == "publish") notifyPublishResult(request, result)
      activity.runOnUiThread {
        webView.evaluateJavascript("window.__nativeResult(${JSONObject.quote(id)}, ${JSONObject.quote(result.toString())})", null)
      }
    }.start()
  }

  private fun generate(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val keyword = request.getString("keyword").trim()
    require(keyword.length in 2..160) { "Enter a keyword between 2 and 160 characters." }
    val type = request.optString("requestedType", "auto")
    val nicheProfile = ContentProfileContract.normalize(request.optString("nicheProfile", ContentProfileContract.FOOD))
    val isFoodProfile = ContentProfileContract.isFood(nicheProfile)
    val provider = ProviderCompatibilityContract.normalize(settings.getString("articleBaseUrl"), settings.getString("articleModel"))
    val endpoint = chatEndpoint(provider.baseUrl)
    val category = request.optString("categoryName").trim()
    val existingTitles = request.optJSONArray("existingTitles") ?: JSONArray()
    val titleList = (0 until minOf(existingTitles.length(), 20)).joinToString(" | ") { existingTitles.optString(it).take(255) }
    val requestedRecipeCount = if (isFoodProfile) RecipeRequestContract.requestedCount(keyword) else 0
    val profileInstruction = when (nicheProfile) {
      "gardening" -> "Create a practical English gardening article. Cover materials, steps, timing, care, safety, and realistic variations. Do not invent recipes or nutrition fields."
      "home-decor" -> "Create a practical English home decor article. Cover style decisions, room planning, materials, measurements where useful, budget-aware options, and implementation steps. Do not invent recipes or nutrition fields."
      "custom" -> "Create a practical English informational article for the requested topic. Infer the correct audience, structure, terminology, and actionable steps from the keyword. Do not invent recipe fields unless the topic genuinely requires them."
      else -> ContentProfileContract.halalRule()
    }
    val customProfilePrompt = settings.optJSONObject("profilePrompts")?.optString(nicheProfile).orEmpty().trim().take(4000)
    val formatRequirements = if (isFoodProfile) """
      If the keyword contains a number of recipes, generate exactly that many distinct, fully populated recipes. For example, “5 fall recipes” means exactly 5 recipes. Never list recipe names only and never replace a requested roundup with a single recipe or summary.
      For one specific cookable dish, select recipe. recipe must contain sensible ingredients, 4 to 9 concrete steps, ISO 8601 durations such as PT15M, yield, cuisine, and 1 to 3 useful notes. Do not put a recipe card inside htmlContent.
      For a roundup or any request containing multiple recipes, select article, return one fully populated object in recipes for every recipe, give every object its own title, complete ingredient quantities, 4 to 9 concrete numbered instructions, ISO 8601 durations, yield, cuisine, and 1 to 3 useful notes, and write detailed sections in htmlContent for the collection. Do not put recipe cards inside htmlContent; the app adds one complete card per recipes object.
      For informational content with no recipes, select article; recipe.isRecipe must be false and every other recipe field must be empty or an empty array, and recipes must be an empty array.
    """.trimIndent() else """
      This is a general article profile, not a recipe request. Set contentType to article, keep recipe.isRecipe false, and return an empty recipes array. Use detailed, actionable H2 sections appropriate to the selected niche and do not invent food, recipe, nutrition, or medical fields.
    """.trimIndent()
    val prompt = """
      Create a complete, long-form English ${if (isFoodProfile) "food" else "general"} article from this keyword: $keyword
      Requested format: $type
      Requested complete recipe count: ${if (requestedRecipeCount > 0) requestedRecipeCount else "not explicitly numbered"}
      $formatRequirements
      Preferred category: ${category.ifBlank { if (isFoodProfile) "Choose the best existing food category" else "Choose the best existing category for this niche" }}
      Existing site titles to avoid duplicating: ${titleList.ifBlank { "None supplied" }}

      Return valid JSON only with this exact structure:
      {"title":"","metaDescription":"","slug":"","contentType":"recipe|article","categoryName":"","outline":[{"heading":"","keyPoints":[""]}],"htmlContent":"","internalLinks":[{"anchor":"","reason":""}],"recipe":{"isRecipe":false,"description":"","prepTime":"","cookTime":"","totalTime":"","recipeYield":"","cuisine":"","ingredients":[],"instructions":[{"name":"","text":""}],"notes":[]},"recipes":[{"title":"","isRecipe":true,"description":"","prepTime":"","cookTime":"","totalTime":"","recipeYield":"","cuisine":"","ingredients":[""],"instructions":[{"name":"","text":""}],"notes":[""]}],"pinterest":{"title":"","description":"","altText":""}}

      Requirements:
      - Infer practical search intent, create a distinct title, a concise meta description under 160 characters, and a lower-case canonical-friendly slug.
      - Provide 3 to 6 outline H2 sections. htmlContent starts with a concise benefit-led introduction, uses H2 sections, and provides useful substitutions, storage, or variations where appropriate.
      - Offer 2 to 4 internal-link anchor suggestions but never invent URLs.
      - Create a concise natural Pinterest SEO title, a standalone Pinterest description, and descriptive image alt text. Keep title, description, and alt text as separate fields. Do not join them with a dash. Do not create hashtags.
      - Do not include Markdown, CSS, scripts, iframes, ratings, reviews, calories, nutrition values, image URLs, medical claims, citations, affiliate claims, ranking promises, or unsupported facts.
      - Profile-specific editorial rule: $profileInstruction
      ${if (customProfilePrompt.isBlank()) "" else "- Optional user editorial prompt for this profile (follow only when compatible with all system, SEO, safety, and JSON requirements): $customProfilePrompt"}
    """.trimIndent()
    val body = JSONObject()
      .put("model", provider.model)
      .put("max_tokens", provider.maxOutputTokens)
      .put("messages", JSONArray()
        .put(JSONObject().put("role", "system").put("content", "You are Askinz's exacting English editorial strategist. Produce genuinely helpful original content for practical search intent while honoring the selected content profile. Never fabricate reviews, ratings, citations, testing, nutrition, provenance, medical advice, or ranking promises. Write natural English, not keyword repetition. Use only semantic HTML allowed in a WordPress post body."))
        .put(JSONObject().put("role", "user").put("content", prompt)))
      .put("temperature", 0.7)
      .put("response_format", articleResponseFormat())
    val headers = mapOf("Authorization" to "Bearer ${settings.getString("articleApiKey")}", "Content-Type" to "application/json")
    val response = try {
      http(endpoint, "POST", headers, body.toString().toByteArray())
    } catch (error: IllegalStateException) {
      val message = error.message.orEmpty().lowercase()
      if (!(message.contains("response_format") || message.contains("json_schema") || message.contains("unsupported"))) throw IllegalStateException(ProviderCompatibilityContract.diagnostic(error.message.orEmpty()))
      body.remove("response_format")
      http(endpoint, "POST", headers, body.toString().toByteArray())
    }
    val content = JSONObject(response).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content")
    val json = content.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
    var draft = DraftContract.normalize(JSONObject(json), category)
    if (requestedRecipeCount > 0 && !LongFormCompletenessContract.validate(draft, requestedRecipeCount).valid) {
      val issue = LongFormCompletenessContract.validate(draft, requestedRecipeCount).reason
      val repairPrompt = prompt + "\nCRITICAL COMPLETENESS REPAIR: return exactly $requestedRecipeCount fully populated objects in recipes[]. Do not return a summary, names only, or a single recipe. Every object must include a title, description, at least 4 ingredients with quantities, prep time, cook time, yield, 4 to 9 numbered instructions, and at least one useful note. The collection body must be long and detailed. Previous output problem: $issue"
      val repairBody = body.put("messages", JSONArray().put(JSONObject().put("role", "system").put("content", "You are a strict recipe-roundup completion editor. Never summarize requested recipes; return every complete recipe." )).put(JSONObject().put("role", "user").put("content", repairPrompt)))
      val repairedResponse = http(endpoint, "POST", headers, repairBody.toString().toByteArray())
      val repairedContent = JSONObject(repairedResponse).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content")
      val repairedJson = repairedContent.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
      draft = DraftContract.normalize(JSONObject(repairedJson), category)
      val completeness = LongFormCompletenessContract.validate(draft, requestedRecipeCount)
      require(completeness.valid) { "The Article API returned incomplete long-form output: ${completeness.reason}. Please retry with a provider that supports structured long-form output." }
    }
    return JSONObject().put("ok", true).put("draft", draft)
  }


  private fun generateImage(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val prompt = request.getString("prompt").trim().take(2048)
    require(prompt.isNotBlank()) { "An image prompt is required." }
    if (settings.optString("imageProvider", "openai") == "cloudflare") {
      val accountId = settings.optString("cloudflareAccountId").trim()
      val model = settings.optString("cloudflareModel", "@cf/black-forest-labs/flux-1-schnell").trim()
      val token = settings.optString("cloudflareApiToken").trim()
      require(accountId.isNotBlank() && model.isNotBlank() && token.isNotBlank()) { "Complete the Cloudflare Account ID, model, and API token first." }
      require(Regex("^[A-Za-z0-9_-]{10,80}$").matches(accountId)) { "Cloudflare Account ID is invalid." }
      require(model.startsWith("@cf/")) { "Cloudflare model must start with @cf/." }
      val body = JSONObject().put("prompt", prompt).put("steps", 4)
      val url = "https://api.cloudflare.com/client/v4/accounts/$accountId/ai/run/" + java.net.URLEncoder.encode(model, "UTF-8").replace("+", "%20")
      val response = JSONObject(http(url, "POST", mapOf("Authorization" to "Bearer $token", "Content-Type" to "application/json"), body.toString().toByteArray()))
      val result = response.optJSONObject("result") ?: throw IllegalStateException("Cloudflare Workers AI returned no result.")
      val base64 = result.optString("image").trim()
      require(base64.isNotBlank()) { "Cloudflare Workers AI returned no Base64 image." }
      return JSONObject().put("ok", true).put("dataUrl", "data:image/jpeg;base64,$base64")
    }
    val baseUrl = settings.optString("imageBaseUrl").trim().trimEnd('/')
    val model = settings.optString("imageModel").trim()
    val apiKey = settings.optString("imageApiKey").trim()
    require(baseUrl.isNotBlank() && model.isNotBlank() && apiKey.isNotBlank()) { "Complete the optional Image Generator API settings first." }
    val body = JSONObject().put("model", model).put("prompt", prompt).put("n", 1).put("size", "1024x1536").put("response_format", "b64_json")
    val response = JSONObject(http("$baseUrl/images/generations", "POST", mapOf("Authorization" to "Bearer $apiKey", "Content-Type" to "application/json"), body.toString().toByteArray()))
    val image = response.optJSONArray("data")?.optJSONObject(0) ?: throw IllegalStateException("The Image Generator API returned no image.")
    val base64 = image.optString("b64_json").trim()
    require(base64.isNotBlank()) { "The Image Generator API returned no Base64 image." }
    return JSONObject().put("ok", true).put("dataUrl", "data:image/png;base64,$base64")
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
          "internalLinks":{"type":"array","items":{"type":"object","properties":{"anchor":{"type":"string"},"reason":{"type":"string"}},"required":["anchor","reason"],"additionalProperties":false}},
          "recipe":{"type":"object","properties":{"isRecipe":{"type":"boolean"},"description":{"type":"string"},"prepTime":{"type":"string"},"cookTime":{"type":"string"},"totalTime":{"type":"string"},"recipeYield":{"type":"string"},"cuisine":{"type":"string"},"ingredients":{"type":"array","items":{"type":"string"}},"instructions":{"type":"array","items":{"type":"object","properties":{"name":{"type":"string"},"text":{"type":"string"}},"required":["name","text"],"additionalProperties":false}},"notes":{"type":"array","items":{"type":"string"}}},"required":["isRecipe","description","prepTime","cookTime","totalTime","recipeYield","cuisine","ingredients","instructions","notes"],"additionalProperties":false},
          "recipes":{"type":"array","maxItems":12,"items":{"type":"object","properties":{"title":{"type":"string"},"isRecipe":{"type":"boolean"},"description":{"type":"string"},"prepTime":{"type":"string"},"cookTime":{"type":"string"},"totalTime":{"type":"string"},"recipeYield":{"type":"string"},"cuisine":{"type":"string"},"ingredients":{"type":"array","items":{"type":"string"}},"instructions":{"type":"array","items":{"type":"object","properties":{"name":{"type":"string"},"text":{"type":"string"}},"required":["name","text"],"additionalProperties":false}},"notes":{"type":"array","items":{"type":"string"}}},"required":["title","isRecipe","description","prepTime","cookTime","totalTime","recipeYield","cuisine","ingredients","instructions","notes"],"additionalProperties":false}},
          "pinterest":{"type":"object","properties":{"title":{"type":"string"},"description":{"type":"string"},"altText":{"type":"string"}},"required":["title","description","altText"],"additionalProperties":false}
        },
        "required":["title","metaDescription","slug","contentType","categoryName","outline","htmlContent","internalLinks","recipe","recipes","pinterest"],
        "additionalProperties":false
      }""".trimIndent()
    )
    return JSONObject().put("type", "json_schema").put("json_schema", JSONObject().put("name", "askinz_food_article").put("strict", true).put("schema", schema))
  }

  private fun categories(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val root = wpRoot(settings.getString("wordpressBaseUrl"))
    val fetched = mutableListOf<WordPressCategoryRecord>()
    val rows = JSONArray()
    for (page in 1..100) {
      val data = try {
        JSONArray(http("$root/wp-json/wp/v2/categories?context=edit&per_page=100&hide_empty=false&page=$page&orderby=name&order=asc", "GET", wordpressHeaders(settings), null))
      } catch (error: IllegalStateException) {
        if (error.message.orEmpty().contains("Request failed (400)")) break else throw error
      }
      for (index in 0 until data.length()) {
        val item = data.getJSONObject(index)
        fetched.add(WordPressCategoryRecord(item.getInt("id"), item.getString("name")))
      }
      if (data.length() < 100) break
    }
    CategorySyncContracts.normalize(fetched).forEach { item -> rows.put(JSONObject().put("id", item.id).put("name", item.name)) }
    return JSONObject().put("ok", true).put("categories", rows)
  }

  private fun pinterestBoards(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val token = settings.optString("pinterestAccessToken").trim()
    require(token.isNotBlank()) { "Add a Pinterest Access Token in Settings first." }
    val response = JSONObject(http("https://api.pinterest.com/v5/boards?page_size=250", "GET", mapOf("Authorization" to "Bearer $token", "Content-Type" to "application/json"), null))
    return JSONObject().put("ok", true).put("boards", response.optJSONArray("items") ?: JSONArray())
  }

  private fun pinterestTrends(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val token = settings.optString("pinterestAccessToken").trim()
    require(token.isNotBlank()) { "Add a Pinterest Access Token in Settings first." }
    val region = request.optString("region", "US").trim()
    require(PinterestTrendsContract.isValidRegion(region)) { "Choose a supported Pinterest region." }
    val trendType = request.optString("trendType", "growing").trim()
    require(PinterestTrendsContract.isValidTrendType(trendType)) { "Choose a supported Pinterest trend type." }
    val limit = PinterestTrendsContract.clampLimit(request.optInt("limit", 10))
    val interest = PinterestTrendsContract.interestForProfile(request.optString("nicheProfile", "custom"))
    val query = buildString {
      append("https://api.pinterest.com/v5/trends/keywords/")
      append(region).append("/top/").append(trendType).append("?limit=").append(limit)
      if (interest.isNotBlank()) append("&interests=").append(java.net.URLEncoder.encode(interest, "UTF-8"))
    }
    val response = JSONObject(http(query, "GET", mapOf("Authorization" to "Bearer $token", "Content-Type" to "application/json"), null))
    return JSONObject().put("ok", true).put("trends", response.optJSONArray("trends") ?: JSONArray()).put("profile", request.optString("nicheProfile", "custom"))
  }

  private fun syncPublishedPosts(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val root = wpRoot(settings.getString("wordpressBaseUrl"))
    val drafts = request.optJSONArray("drafts") ?: JSONArray()
    val posts = JSONArray()
    for (index in 0 until minOf(drafts.length(), 100)) {
      val draft = drafts.optJSONObject(index) ?: continue
      if (draft.optString("generationStatus") != "published") continue
      val post = findTrackedPost(root, settings, draft)
      if (post == null) {
        posts.put(JSONObject().put("draftId", draft.optString("id")).put("found", false))
      } else {
        posts.put(JSONObject()
          .put("draftId", draft.optString("id"))
          .put("found", true)
          .put("postId", post.optInt("id"))
          .put("url", post.optString("link"))
          .put("status", post.optString("status"))
          .put("modified", post.optString("modified")))
      }
    }
    return JSONObject().put("ok", true).put("posts", posts)
  }

  private fun testConnection(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val root = wpRoot(settings.getString("wordpressBaseUrl"))
    val profile = JSONObject(http("$root/wp-json/wp/v2/users/me?context=edit", "GET", wordpressHeaders(settings), null))
    return JSONObject().put("ok", true).put("accountName", profile.optString("name", profile.optString("slug", "WordPress account")))
  }

  private fun safeSiteId(value: String): String = value.ifBlank { "site-default" }.replace(Regex("[^A-Za-z0-9_-]"), "_").take(80)

  private fun imageDirectory(siteId: String = "site-default"): File {
    val base = File(activity.filesDir, "askinz-images").apply { mkdirs() }
    return if (siteId.isBlank() || siteId == "site-default") base else File(base, safeSiteId(siteId)).apply { mkdirs() }
  }

  private fun storeImage(request: JSONObject): JSONObject {
    val kind = request.optString("kind")
    require(kind == "featured" || kind == "pinterest" || Regex("^recipe-[0-9]+$").matches(kind)) { "Unknown image type." }
    val siteId = request.optString("siteId", "site-default")
    val image = parseImage(request.getString("dataUrl"), kind == "pinterest", siteId)
    val reference = "local://${UUID.randomUUID()}.${image.extension}"
    File(imageDirectory(siteId), reference.removePrefix("local://")).writeBytes(image.bytes)
    return JSONObject().put("ok", true).put("reference", reference).put("mimeType", image.mimeType)
  }

  private fun loadImage(request: JSONObject): JSONObject {
    val image = parseImageReference(request.getString("reference"), false, request.optString("siteId", "site-default"))
    return JSONObject().put("ok", true).put("dataUrl", "data:${image.mimeType};base64," + Base64.encodeToString(image.bytes, Base64.NO_WRAP))
  }

  private fun removeImage(request: JSONObject): JSONObject {
    File(imageDirectory(request.optString("siteId", "site-default")), safeImageFilename(request.getString("reference"))).delete()
    return JSONObject().put("ok", true)
  }

  private fun repairPreview(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val root = wpRoot(settings.getString("wordpressBaseUrl"))
    val drafts = request.optJSONArray("drafts") ?: JSONArray()
    val posts = JSONArray()
    var tracked = 0
    var matched = 0
    var fixable = 0
    for (index in 0 until minOf(drafts.length(), 50)) {
      val draft = drafts.optJSONObject(index) ?: continue
      if (draft.optString("generationStatus") != "published") continue
      tracked += 1
      val post = findTrackedPost(root, settings, draft)
      if (post == null) {
        posts.put(JSONObject().put("draftId", draft.optString("id")).put("title", draft.optString("title")).put("matched", false).put("changed", false).put("reason", "Published WordPress post was not found."))
        continue
      }
      matched += 1
      val inspection = inspectPublishedPost(post)
      if (inspection.getBoolean("changed")) fixable += 1
      posts.put(JSONObject()
        .put("draftId", draft.optString("id"))
        .put("postId", post.getInt("id"))
        .put("title", draft.optString("title"))
        .put("link", post.optString("link"))
        .put("matched", true)
        .put("changed", inspection.getBoolean("changed"))
        .put("reason", inspection.getString("reason")))
    }
    return JSONObject().put("ok", true).put("trackedSystemArticles", tracked).put("matchedInWordPress", matched).put("fixablePosts", fixable).put("posts", posts)
  }

  private fun repairApply(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val root = wpRoot(settings.getString("wordpressBaseUrl"))
    val drafts = request.optJSONArray("drafts") ?: JSONArray()
    val results = JSONArray()
    val failures = JSONArray()
    var updated = 0
    for (index in 0 until minOf(drafts.length(), 50)) {
      val draft = drafts.optJSONObject(index) ?: continue
      if (draft.optString("generationStatus") != "published") continue
      try {
        val post = findTrackedPost(root, settings, draft) ?: throw IllegalStateException("Published WordPress post was not found.")
        val inspection = inspectPublishedPost(post)
        if (!inspection.getBoolean("changed")) {
          results.put(JSONObject().put("draftId", draft.optString("id")).put("updated", false).put("reason", "Already uses the current template."))
          continue
        }
        val raw = post.optJSONObject("content")?.optString("raw").orEmpty().ifBlank { post.optJSONObject("content")?.optString("rendered").orEmpty() }
        require(raw.isNotBlank()) { "WordPress did not return editable post content." }
        preferences.edit().putString("repair_backup_${post.getInt("id")}", raw).apply()
        var content = raw
        val featuredUrl = featuredUrl(root, settings, post)
        if (inspection.getBoolean("missingFeatured") && !featuredUrl.isNullOrBlank()) {
          content = WordPressMarkup.featuredImage(featuredUrl, featuredImageAltText(draft)) + content
        }
        var pinterestUrl = pinterestUrlFromContent(content)
        if (inspection.getBoolean("missingPinterest")) {
          val reference = draft.optJSONObject("images")?.optString("pinterest").orEmpty()
          require(reference.isNotBlank()) { "Pinterest image is missing locally, so this post cannot be repaired safely." }
          val media = uploadMedia(root, settings, parseImage(reference, true, request.optString("siteId", "site-default")), "${DraftContract.cleanSlug(draft.optString("slug"))}-pinterest", pinterestImageAltText(draft))
          pinterestUrl = media.getString("source_url")
          val canonical = "$root/${DraftContract.cleanSlug(draft.optString("slug"))}/"
          val pinTitle = draft.optString("pinterestTitle", draft.optString("title")).trim().take(100)
          val pinDescription = draft.optString("pinterestDescription", draft.optString("metaDescription")).trim().take(800)
          val pinAltText = pinterestImageAltText(draft)
          // Pinterest's legacy create URL has one reliable text field: description.
          // Never concatenate the title into it; title is supplied by WordPress OG metadata.
          val share = "https://www.pinterest.com/pin/create/button/?url=" + java.net.URLEncoder.encode(canonical, "UTF-8") + "&media=" + java.net.URLEncoder.encode(pinterestUrl, "UTF-8") + "&description=" + java.net.URLEncoder.encode(pinDescription, "UTF-8")
          content += WordPressMarkup.pinterestSaveButton(share, pinTitle, pinDescription, pinterestUrl, pinAltText)
        }
        if (inspection.getBoolean("missingSchema")) {
          val urls = listOfNotNull(featuredUrl, pinterestUrl)
          val schema = DraftContract.buildSchema(draft, "$root/${DraftContract.cleanSlug(draft.optString("slug"))}/", urls)
          content += WordPressMarkup.structuredData(schema.toString())
        }
        val updatedPost = JSONObject(http("$root/wp-json/wp/v2/posts/${post.getInt("id")}", "POST", wordpressHeaders(settings) + mapOf("Content-Type" to "application/json"), JSONObject().put("content", content).toString().toByteArray()))
        updated += 1
        results.put(JSONObject().put("draftId", draft.optString("id")).put("updated", true).put("postId", updatedPost.getInt("id")).put("link", updatedPost.optString("link")).put("reason", "Template blocks repaired; encrypted local backup saved first."))
      } catch (error: Exception) {
        failures.put(JSONObject().put("draftId", draft.optString("id")).put("title", draft.optString("title")).put("reason", error.message ?: "Unknown repair error."))
      }
    }
    return JSONObject().put("ok", true).put("updatedPosts", updated).put("results", results).put("failures", failures)
  }

  private fun findTrackedPost(root: String, settings: JSONObject, draft: JSONObject): JSONObject? {
    val knownId = draft.optInt("wordpressPostId", 0)
    if (knownId > 0) {
      val direct = try {
        JSONObject(http("$root/wp-json/wp/v2/posts/$knownId?context=edit", "GET", wordpressHeaders(settings), null))
      } catch (_: Exception) { null }
      if (direct != null) return direct
    }
    val slug = DraftContract.cleanSlug(draft.optString("slug"))
    if (slug.isBlank()) return null
    val rows = JSONArray(http("$root/wp-json/wp/v2/posts?slug=${java.net.URLEncoder.encode(slug, "UTF-8")}&context=edit&per_page=1", "GET", wordpressHeaders(settings), null))
    return if (rows.length() > 0) rows.getJSONObject(0) else null
  }

  private fun inspectPublishedPost(post: JSONObject): JSONObject {
    val content = post.optJSONObject("content")?.optString("raw").orEmpty().ifBlank { post.optJSONObject("content")?.optString("rendered").orEmpty() }
    val missingFeatured = !content.contains("wp-block-image")
    val missingPinterest = !content.contains("data-askinz-pinterest-direct")
    val missingSchema = !content.contains("application/ld+json")
    val reasons = mutableListOf<String>()
    if (missingFeatured) reasons.add("featured image block")
    if (missingPinterest) reasons.add("Pinterest save button")
    if (missingSchema) reasons.add("structured data")
    return JSONObject().put("changed", reasons.isNotEmpty()).put("missingFeatured", missingFeatured).put("missingPinterest", missingPinterest).put("missingSchema", missingSchema).put("reason", if (reasons.isEmpty()) "Already uses the current template." else "Missing ${reasons.joinToString(", ")}." )
  }

  private fun featuredUrl(root: String, settings: JSONObject, post: JSONObject): String? {
    val mediaId = post.optInt("featured_media", 0)
    if (mediaId <= 0) return null
    return try { JSONObject(http("$root/wp-json/wp/v2/media/$mediaId", "GET", wordpressHeaders(settings), null)).optString("source_url").ifBlank { null } } catch (_: Exception) { null }
  }

  private fun pinterestUrlFromContent(content: String): String? {
    val media = Regex("[?&]media=([^&\"']+)", RegexOption.IGNORE_CASE).find(content)?.groupValues?.getOrNull(1) ?: return null
    return try { java.net.URLDecoder.decode(media, "UTF-8") } catch (_: Exception) { null }
  }

  private fun publish(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val draft = request.getJSONObject("draft")
    val images = request.getJSONObject("images")
    val root = wpRoot(settings.getString("wordpressBaseUrl"))
    val slug = DraftContract.cleanSlug(draft.getString("slug"))
    require(slug.isNotBlank()) { "The draft slug is invalid." }
    val duplicates = JSONArray(http("$root/wp-json/wp/v2/posts?slug=${java.net.URLEncoder.encode(slug, "UTF-8")}&context=edit&per_page=1", "GET", wordpressHeaders(settings), null))
    require(duplicates.length() == 0) { "A WordPress post with this slug already exists. Change the title or slug first." }
    val featured = parseImage(images.getString("featured"), false, request.optString("siteId", "site-default"))
    val pinterest = parseImage(images.getString("pinterest"), true, request.optString("siteId", "site-default"))
    val featuredAltText = featuredImageAltText(draft)
    val pinterestAltText = pinterestImageAltText(draft)
    val categoryId = request.optInt("categoryId", settings.optInt("categoryId", 0))
    requireExistingCategory(root, settings, categoryId)
    val featuredMedia = uploadMedia(root, settings, featured, "${slug}-featured", featuredAltText)
    val pinterestMedia = uploadMedia(root, settings, pinterest, "${slug}-pinterest", pinterestAltText)
    val featuredUrl = featuredMedia.getString("source_url")
    val pinterestUrl = pinterestMedia.getString("source_url")
    val pinTitle = draft.optString("pinterestTitle", draft.optString("title")).trim().take(100)
    val pinDescription = draft.optString("pinterestDescription", draft.optString("metaDescription")).trim().take(800)
    val pinAltText = pinterestImageAltText(draft)
    // Pinterest's legacy create URL has one reliable text field: description.
    // Never concatenate the title into it; title is supplied by WordPress OG metadata.
    val share = "https://www.pinterest.com/pin/create/button/?url=" + java.net.URLEncoder.encode("$root/$slug/", "UTF-8") + "&media=" + java.net.URLEncoder.encode(pinterestUrl, "UTF-8") + "&description=" + java.net.URLEncoder.encode(pinDescription, "UTF-8")
    val featuredBlock = WordPressMarkup.featuredImage(featuredUrl, featuredAltText)
    val pinBlock = WordPressMarkup.pinterestSaveButton(share, pinTitle, pinDescription, pinterestUrl, pinAltText)
    val schema = DraftContract.buildSchema(draft, "$root/$slug/", listOf(featuredUrl, pinterestUrl))
    val post = JSONObject()
      .put("title", draft.getString("title"))
      .put("slug", slug)
      .put("status", "publish")
      .put("content", featuredBlock + draft.getString("htmlContent") + pinBlock + WordPressMarkup.structuredData(schema.toString()))
      .put("excerpt", draft.optString("metaDescription"))
      .put("featured_media", featuredMedia.getInt("id"))
    post.put("categories", JSONArray().put(categoryId))
    val published = JSONObject(http("$root/wp-json/wp/v2/posts", "POST", wordpressHeaders(settings) + mapOf("Content-Type" to "application/json"), post.toString().toByteArray()))
    val pinterestToken = settings.optString("pinterestAccessToken").trim()
    val pinterestBoardId = request.optString("pinterestBoardId").trim().ifBlank { settings.optString("pinterestBoardId").trim() }
    val pinterestMode = settings.optString("pinterestPublishingMode", "manual_review").trim().ifBlank { "manual_review" }
    val metadataSaved = try { savePinterestMetadata(root, settings, published.optInt("id"), pinTitle, pinDescription, pinAltText, pinterestUrl); true } catch (_: Exception) { false }
    val pinterestResult = if (pinterestMode == "disabled") {
      JSONObject().put("published", false).put("skipped", true)
    } else if (pinterestMode == "manual_review") {
      JSONObject().put("published", false).put("manualReview", true).put("metadataSaved", metadataSaved).put("composerUrl", share)
    } else if (pinterestToken.isNotBlank() && pinterestBoardId.isNotBlank()) {
      try {
        val pinBody = JSONObject()
          .put("board_id", pinterestBoardId)
          .put("title", pinTitle.take(100))
          .put("alt_text", pinterestAltText.take(500))
          .put("description", draft.optString("metaDescription").take(800))
          .put("link", published.optString("link").ifBlank { "$root/$slug/" })
          .put("media_source", JSONObject().put("source_type", "image_base64").put("is_standard", false).put("content_type", pinterest.mimeType).put("data", Base64.encodeToString(pinterest.bytes, Base64.NO_WRAP)))
        val pin = JSONObject(http("https://api.pinterest.com/v5/pins", "POST", mapOf("Authorization" to "Bearer $pinterestToken", "Content-Type" to "application/json"), pinBody.toString().toByteArray()))
        JSONObject().put("published", true).put("pinId", pin.optString("id"))
      } catch (error: Exception) {
        JSONObject().put("published", false).put("error", error.message ?: "Pinterest API request failed.")
      }
    } else JSONObject().put("published", false).put("skipped", true)
    return JSONObject().put("ok", true).put("url", published.optString("link")).put("postId", published.optInt("id")).put("pinterest", pinterestResult)
  }

  private fun savePinterestMetadata(root: String, settings: JSONObject, postId: Int, title: String, description: String, altText: String, imageUrl: String) {
    val body = JSONObject().put("post_id", postId).put("title", title.take(100)).put("description", description.take(800)).put("alt_text", altText.take(320)).put("image", imageUrl)
    http("$root/wp-json/orbitpress/v1/pinterest-meta", "POST", wordpressHeaders(settings) + mapOf("Content-Type" to "application/json"), body.toString().toByteArray())
  }

  private fun notifyPublishResult(request: JSONObject, result: JSONObject) {
    val manager = activity.getSystemService(NotificationManager::class.java) ?: return
    val permissionGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || activity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    if (!NotificationContracts.shouldNotify(permissionGranted)) return
    val kind = NotificationContracts.classify(result.optBoolean("ok", false), result.optString("message"))
    val message = NotificationContracts.headline(kind)
    val title = request.optJSONObject("draft")?.optString("title").orEmpty().ifBlank { "Article" }.take(80)
    val intent = Intent(activity, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP }
    val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
    val pending = PendingIntent.getActivity(activity, title.hashCode(), intent, pendingFlags)
    val notification = Notification.Builder(activity, PUBLISH_NOTIFICATION_CHANNEL_ID)
      .setSmallIcon(android.R.drawable.ic_dialog_info)
      .setContentTitle("OrbitPress · $message")
      .setContentText(title)
      .setAutoCancel(true)
      .setContentIntent(pending)
      .build()
    manager.notify((title.hashCode() and 0x7fffffff), notification)
  }

  private data class ImagePayload(val bytes: ByteArray, val mimeType: String, val extension: String)

  private fun parseImage(dataUrl: String, pinterest: Boolean, siteId: String = "site-default"): ImagePayload {
    if (dataUrl.startsWith("local://")) return parseImageReference(dataUrl, pinterest, siteId)
    val separator = dataUrl.indexOf(',')
    require(separator > 0 && dataUrl.startsWith("data:")) { "Choose a valid image file." }
    val declaredMime = dataUrl.substring(5, dataUrl.indexOf(';'))
    val bytes = Base64.decode(dataUrl.substring(separator + 1), Base64.DEFAULT)
    require(bytes.isNotEmpty() && bytes.size <= 12_000_000) { "Choose an image smaller than 12 MB." }
    return validateImage(bytes, PublishingContracts.validatedImageMimeType(declaredMime, bytes), pinterest)
  }

  private fun parseImageReference(reference: String, pinterest: Boolean, siteId: String = "site-default"): ImagePayload {
    val filename = safeImageFilename(reference)
    val extension = filename.substringAfterLast('.').lowercase()
    val declaredMime = when (extension) { "jpg", "jpeg" -> "image/jpeg"; "webp" -> "image/webp"; "png" -> "image/png"; else -> throw IllegalArgumentException("Unsupported local image format.") }
    val file = File(imageDirectory(siteId), filename)
    require(file.isFile) { "The saved image is no longer available on this phone. Select it again." }
    val bytes = file.readBytes()
    return validateImage(bytes, PublishingContracts.validatedImageMimeType(declaredMime, bytes), pinterest)
  }

  private fun safeImageFilename(reference: String): String {
    require(reference.startsWith("local://")) { "Invalid local image reference." }
    val filename = reference.removePrefix("local://")
    require(Regex("^[a-f0-9-]+\\.(jpg|png|webp)$").matches(filename)) { "Invalid local image reference." }
    return filename
  }

  private fun validateImage(bytes: ByteArray, mime: String, pinterest: Boolean): ImagePayload {
    require(bytes.isNotEmpty() && bytes.size <= 12_000_000) { "Choose an image smaller than 12 MB." }
    if (pinterest) {
      val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
      BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
      require(bounds.outWidth > 0 && bounds.outHeight > 0 && bounds.outWidth * 3 == bounds.outHeight * 2) { "Pinterest image must have an exact 2:3 portrait ratio, such as 1000×1500." }
    }
    return ImagePayload(bytes, mime, when (mime) { "image/jpeg" -> "jpg"; "image/webp" -> "webp"; else -> "png" })
  }

  private fun uploadMedia(root: String, settings: JSONObject, image: ImagePayload, basename: String, altText: String): JSONObject {
    val response = http("$root/wp-json/wp/v2/media", "POST", wordpressHeaders(settings) + mapOf("Content-Type" to image.mimeType, "Content-Disposition" to "attachment; filename=\"$basename.${image.extension}\""), image.bytes)
    val media = JSONObject(response)
    http("$root/wp-json/wp/v2/media/${media.getInt("id")}", "POST", wordpressHeaders(settings) + mapOf("Content-Type" to "application/json"), JSONObject().put("alt_text", altText.take(320)).toString().toByteArray())
    return media
  }

  private fun requireExistingCategory(root: String, settings: JSONObject, categoryId: Int) {
    PublishingContracts.requireExistingCategoryId(categoryId)
    val category = JSONObject(http("$root/wp-json/wp/v2/categories/$categoryId?context=edit", "GET", wordpressHeaders(settings), null))
    require(category.optInt("id", 0) == categoryId) { "Choose one of the existing WordPress categories before publishing." }
  }

  private fun featuredImageAltText(draft: JSONObject): String = PublishingContracts.featuredImageAltText(draft.optString("title"), draft.optString("contentType"))

  private fun pinterestImageAltText(draft: JSONObject): String = draft.optString("pinterestAltText").trim().take(320).ifBlank { PublishingContracts.pinterestImageAltText(draft.optString("pinterestTitle"), draft.optString("title")) }

  private fun wordpressHeaders(settings: JSONObject): Map<String, String> {
    val raw = "${settings.getString("wordpressUsername")}:${settings.getString("wordpressAppPassword")}".toByteArray(StandardCharsets.UTF_8)
    return mapOf("Authorization" to "Basic ${Base64.encodeToString(raw, Base64.NO_WRAP)}")
  }

  private fun http(url: String, method: String, headers: Map<String, String>, body: ByteArray?): String {
    val connection = (URL(url).openConnection() as HttpURLConnection).apply {
      requestMethod = method
      connectTimeout = 25_000
      readTimeout = 90_000
      instanceFollowRedirects = false
      headers.forEach { (key, value) -> setRequestProperty(key, value) }
      if (body != null) { doOutput = true; outputStream.use { it.write(body) } }
    }
    val status = connection.responseCode
    val stream = if (status in 200..299) connection.inputStream else connection.errorStream
    val response = stream?.use { BufferedInputStream(it).readBytes().toString(StandardCharsets.UTF_8) } ?: ""
    if (status !in 200..299) throw IllegalStateException("Request failed ($status): ${response.take(280)}")
    return response
  }

  private fun chatEndpoint(base: String): String = PublishingContracts.requireHttpsUrl(base, "Article API URL").let { if (it.endsWith("/chat/completions")) it else "$it/chat/completions" }
  private fun wpRoot(base: String): String = PublishingContracts.requireHttpsUrl(base, "WordPress URL").removeSuffix("/wp-json")
}
