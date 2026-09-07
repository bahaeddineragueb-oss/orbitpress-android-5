package com.askinz.publisher

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.util.UUID

enum class Screen {
  STUDIO,
  PROMPTS,
  DRAFTS,
  REVIEW,
  PIPELINE,
  ACTIVITY,
  TRENDS,
  REPAIR,
  SETTINGS
}

data class SeoAuditCheck(val label: String, val detail: String, val ok: Boolean)
data class SeoAuditResult(val score: Int, val checks: List<SeoAuditCheck>)

data class UiState(
  val currentScreen: Screen = Screen.STUDIO,
  val activeSiteId: String = SettingsPersistenceContract.DEFAULT_SITE_ID,
  val siteProfiles: List<SiteProfileRecord> = listOf(SiteProfileRecord("site-default", "Askinz")),
  val keywords: List<KeywordRecord> = emptyList(),
  val drafts: List<DraftRecord> = emptyList(),
  val draftVersions: List<DraftVersionRecord> = emptyList(),
  val logs: List<ActivityLogRecord> = emptyList(),
  val categories: List<WordPressCategoryRecord> = emptyList(),
  val pinterestBoards: List<PinterestBoardRecord> = emptyList(),
  val contentBriefs: List<ContentBriefRecord> = emptyList(),
  val keywordClusters: List<KeywordClusterRecord> = emptyList(),
  val calendarReminders: List<CalendarReminderRecord> = emptyList(),
  val currentSettings: SiteSettings = SiteSettings(),
  val selectedDraftId: String? = null,
  val isSettingsUnlocked: Boolean = false,
  val isPinLockEnabled: Boolean = false,
  val showPinDialog: Boolean = false,
  val isLoading: Boolean = false,
  val loadingMessage: String = "",
  val snackbarMessage: String? = null,
  val connectedAccountName: String? = null,
  val trends: List<PinterestTrendRecord> = emptyList(),
  val inspectionResults: List<WordPressPostInspection> = emptyList(),
  val isNightMode: Boolean = false
)

data class WordPressPostInspection(
  val draftId: String,
  val postId: Int,
  val title: String,
  val link: String,
  val matched: Boolean,
  val changed: Boolean,
  val missingFeatured: Boolean,
  val missingPinterest: Boolean,
  val missingSchema: Boolean,
  val reason: String
)

class OrbitPressViewModel(application: Application) : AndroidViewModel(application) {
  private val storage = SecureStorage(application)
  private val publishingService = PublishingService(application)

  private val _uiState = MutableStateFlow(UiState())
  val uiState: StateFlow<UiState> = _uiState.asStateFlow()

  init {
    loadWorkspace()
    loadSettingsForActiveSite()
  }

  fun showScreen(screen: Screen) {
    if (screen == Screen.SETTINGS && storage.isPinLockEnabled() && !_uiState.value.isSettingsUnlocked) {
      _uiState.update { it.copy(showPinDialog = true) }
      return
    }
    _uiState.update { it.copy(currentScreen = screen) }
  }

  fun dismissPinDialog() {
    _uiState.update { it.copy(showPinDialog = false) }
  }

  fun unlockSettings(pin: String): Boolean {
    val matches = storage.verifyPinLock(pin)
    if (matches) {
      _uiState.update { it.copy(isSettingsUnlocked = true, showPinDialog = false, currentScreen = Screen.SETTINGS) }
      notify("Settings unlocked.")
    } else {
      notify("Incorrect PIN.")
    }
    return matches
  }

  fun setPinLock(pin: String) {
    storage.savePinLock(pin)
    _uiState.update { it.copy(isPinLockEnabled = storage.isPinLockEnabled(), isSettingsUnlocked = true) }
    notify(if (pin.isBlank()) "PIN lock removed." else "PIN lock enabled.")
  }

  fun toggleNightMode() {
    _uiState.update { it.copy(isNightMode = !it.isNightMode) }
    persistWorkspace()
  }

  // --- Site Profiles ---
  fun switchSite(siteId: String) {
    _uiState.update { it.copy(activeSiteId = siteId, selectedDraftId = null, connectedAccountName = null) }
    loadSettingsForActiveSite()
    persistWorkspace()
  }

  fun addSite(name: String) {
    val cleanName = name.trim()
    if (cleanName.isBlank()) return
    val newId = "site-${System.currentTimeMillis()}"
    val newProfiles = _uiState.value.siteProfiles + SiteProfileRecord(newId, cleanName)
    _uiState.update { it.copy(siteProfiles = newProfiles, activeSiteId = newId) }
    loadSettingsForActiveSite()
    persistWorkspace()
    notify("Website profile added: $cleanName")
  }

  fun renameSite(id: String, newName: String) {
    val cleanName = newName.trim()
    if (cleanName.isBlank()) return
    val newProfiles = _uiState.value.siteProfiles.map {
      if (it.id == id) it.copy(name = cleanName) else it
    }
    _uiState.update { it.copy(siteProfiles = newProfiles) }
    persistWorkspace()
    notify("Website renamed.")
  }

  fun archiveSite(id: String) {
    if (_uiState.value.siteProfiles.size <= 1) {
      notify("Cannot delete the only remaining website profile.")
      return
    }
    val newProfiles = _uiState.value.siteProfiles.filter { it.id != id }
    val nextActive = if (_uiState.value.activeSiteId == id) newProfiles.first().id else _uiState.value.activeSiteId
    _uiState.update { it.copy(siteProfiles = newProfiles, activeSiteId = nextActive) }
    loadSettingsForActiveSite()
    persistWorkspace()
    notify("Website archived locally.")
  }

  // --- Workspace Persistence ---
  private fun loadWorkspace() {
    try {
      val raw = storage.loadWorkspaceJson()
      val json = JSONObject(raw)
      val siteId = json.optString("activeSiteId", SettingsPersistenceContract.DEFAULT_SITE_ID)

      val profiles = mutableListOf<SiteProfileRecord>()
      val pArr = json.optJSONArray("siteProfiles")
      if (pArr != null) {
        for (i in 0 until pArr.length()) {
          val p = pArr.getJSONObject(i)
          profiles.add(SiteProfileRecord(p.getString("id"), p.getString("name")))
        }
      }
      if (profiles.isEmpty()) profiles.add(SiteProfileRecord("site-default", "Askinz"))

      val keywords = mutableListOf<KeywordRecord>()
      json.optJSONArray("keywords")?.let { arr ->
        for (i in 0 until arr.length()) keywords.add(KeywordRecord.fromJson(arr.getJSONObject(i)))
      }

      val drafts = mutableListOf<DraftRecord>()
      json.optJSONArray("drafts")?.let { arr ->
        for (i in 0 until arr.length()) drafts.add(DraftRecord.fromJson(arr.getJSONObject(i)))
      }

      val logs = mutableListOf<ActivityLogRecord>()
      json.optJSONArray("logs")?.let { arr ->
        for (i in 0 until arr.length()) logs.add(ActivityLogRecord.fromJson(arr.getJSONObject(i)))
      }

      val categories = mutableListOf<WordPressCategoryRecord>()
      json.optJSONArray("categories")?.let { arr ->
        for (i in 0 until arr.length()) {
          val c = arr.getJSONObject(i)
          categories.add(WordPressCategoryRecord(c.getInt("id"), c.getString("name")))
        }
      }

      val boards = mutableListOf<PinterestBoardRecord>()
      json.optJSONArray("pinterestBoards")?.let { arr ->
        for (i in 0 until arr.length()) {
          val b = arr.getJSONObject(i)
          boards.add(PinterestBoardRecord(b.getString("id"), b.getString("name")))
        }
      }

      _uiState.update {
        it.copy(
          activeSiteId = siteId,
          siteProfiles = profiles,
          keywords = keywords,
          drafts = drafts,
          logs = logs,
          categories = categories,
          pinterestBoards = boards,
          isPinLockEnabled = storage.isPinLockEnabled(),
          isNightMode = json.optString("theme") == "night"
        )
      }
    } catch (_: Exception) {}
  }

  private fun persistWorkspace() {
    try {
      val s = _uiState.value
      val json = JSONObject()
        .put("activeSiteId", s.activeSiteId)
        .put("theme", if (s.isNightMode) "night" else "day")

      val pArr = JSONArray()
      s.siteProfiles.forEach { pArr.put(JSONObject().put("id", it.id).put("name", it.name)) }
      json.put("siteProfiles", pArr)

      val kwArr = JSONArray()
      s.keywords.forEach { kwArr.put(it.toJson()) }
      json.put("keywords", kwArr)

      val dArr = JSONArray()
      s.drafts.forEach { dArr.put(it.toJson()) }
      json.put("drafts", dArr)

      val logArr = JSONArray()
      s.logs.take(300).forEach { logArr.put(it.toJson()) }
      json.put("logs", logArr)

      val catArr = JSONArray()
      s.categories.forEach { catArr.put(JSONObject().put("id", it.id).put("name", it.name)) }
      json.put("categories", catArr)

      val bArr = JSONArray()
      s.pinterestBoards.forEach { bArr.put(JSONObject().put("id", it.id).put("name", it.name)) }
      json.put("pinterestBoards", bArr)

      storage.saveWorkspaceJson(json.toString())
    } catch (_: Exception) {}
  }

  private fun loadSettingsForActiveSite() {
    val settings = storage.loadSiteSettings(_uiState.value.activeSiteId)
    _uiState.update { it.copy(currentSettings = settings) }
  }

  fun saveSettings(settings: SiteSettings) {
    storage.saveSiteSettings(_uiState.value.activeSiteId, settings)
    _uiState.update { it.copy(currentSettings = settings) }
    notify("Settings saved securely on device.")
  }

  // --- Keywords & Generation ---
  fun addKeyword(
    keyword: String,
    contentType: String,
    nicheProfile: String,
    priority: String,
    categoryId: Int,
    categoryName: String,
    pinterestBoardId: String
  ) {
    val clean = keyword.trim()
    if (clean.length < 2) {
      notify("Enter a valid keyword.")
      return
    }
    val item = KeywordRecord(
      id = "kw-${System.currentTimeMillis()}-${(100..999).random()}",
      keyword = clean,
      contentType = contentType,
      nicheProfile = nicheProfile,
      priority = priority,
      categoryId = categoryId,
      categoryName = categoryName,
      pinterestBoardId = pinterestBoardId,
      siteId = _uiState.value.activeSiteId
    )
    _uiState.update { it.copy(keywords = listOf(item) + it.keywords) }
    addLog("keyword_added", clean)
    persistWorkspace()
    notify("Keyword queued.")
  }

  fun importKeywords(text: String, nicheProfile: String, categoryId: Int, categoryName: String) {
    val lines = text.split("\n", ",").map { it.trim() }.filter { it.length in 2..160 }
    if (lines.isEmpty()) {
      notify("No valid keywords found.")
      return
    }
    val newItems = lines.map { kw ->
      KeywordRecord(
        id = "kw-${System.currentTimeMillis()}-${(1000..9999).random()}",
        keyword = kw,
        nicheProfile = nicheProfile,
        categoryId = categoryId,
        categoryName = categoryName,
        siteId = _uiState.value.activeSiteId
      )
    }
    _uiState.update { it.copy(keywords = newItems + it.keywords) }
    addLog("keywords_imported", "${lines.size} keywords imported")
    persistWorkspace()
    notify("Imported ${lines.size} keywords.")
  }

  fun deleteKeyword(id: String) {
    val kw = _uiState.value.keywords.find { it.id == id } ?: return
    _uiState.update { it.copy(keywords = it.keywords.filter { it.id != id }) }
    addLog("keyword_deleted", kw.keyword)
    persistWorkspace()
    notify("Keyword removed.")
  }

  fun generateDraft(keywordId: String) {
    val kw = _uiState.value.keywords.find { it.id == keywordId } ?: return
    val settings = _uiState.value.currentSettings
    if (!settings.isConfigured()) {
      showScreen(Screen.SETTINGS)
      notify("Configure Article API and WordPress settings first.")
      return
    }

    _uiState.update { state ->
      state.copy(
        isLoading = true,
        loadingMessage = "Generating article for: ${kw.keyword}...",
        keywords = state.keywords.map { if (it.id == keywordId) it.copy(status = "generating") else it }
      )
    }

    viewModelScope.launch {
      try {
        val existingTitles = _uiState.value.drafts.map { it.title }
        var draft = publishingService.generateArticle(
          settings = settings,
          keyword = kw.keyword,
          contentType = kw.contentType,
          nicheProfile = kw.nicheProfile,
          categoryName = kw.categoryName,
          existingTitles = existingTitles
        ).copy(
          keywordId = kw.id,
          siteId = _uiState.value.activeSiteId
        )

        // Auto Image Generation if enabled
        if (settings.imageMode == "automatic" && settings.isImageConfigured()) {
          val autoImages = draft.images.toMutableMap()
          for (kind in listOf("featured", "pinterest")) {
            try {
              val isPin = kind == "pinterest"
              val prompt = "${draft.title}. ${if (isPin) "Vertical Pinterest 2:3 editorial food/lifestyle image. " else ""}${if (isPin) draft.pinterestAltText else draft.title}"
              val (bytes, mime) = publishingService.generateImageBytes(settings, prompt, isPin)
              val (ref, _) = ImageManager.storeImageFromBytes(getApplication(), bytes, mime, isPin, _uiState.value.activeSiteId)
              autoImages[kind] = ref
            } catch (_: Exception) {}
          }
          draft = draft.copy(images = autoImages)
        }

        _uiState.update { state ->
          state.copy(
            isLoading = false,
            drafts = listOf(draft) + state.drafts,
            keywords = state.keywords.map { if (it.id == keywordId) it.copy(status = "ready", draftId = draft.id) else it },
            selectedDraftId = draft.id,
            currentScreen = Screen.REVIEW
          )
        }
        addLog("draft_generated", draft.title, articleId = draft.id)
        persistWorkspace()
        notify("Article generated! Review and attach images before publishing.")
      } catch (e: Exception) {
        _uiState.update { state ->
          state.copy(
            isLoading = false,
            keywords = state.keywords.map { if (it.id == keywordId) it.copy(status = "failed", errorDetails = e.message ?: "Failed") else it }
          )
        }
        addLog("generation_failed", kw.keyword, errorDetails = e.message ?: "Error")
        persistWorkspace()
        notify("Generation failed: ${e.message}")
      }
    }
  }

  // --- Draft Review & Editing ---
  fun openDraft(draftId: String) {
    _uiState.update { it.copy(selectedDraftId = draftId, currentScreen = Screen.REVIEW) }
  }

  fun updateDraft(updated: DraftRecord) {
    _uiState.update { state ->
      state.copy(drafts = state.drafts.map { if (it.id == updated.id) updated else it })
    }
    persistWorkspace()
  }

  fun saveDraftSnapshot(draftId: String) {
    val draft = _uiState.value.drafts.find { it.id == draftId } ?: return
    val version = DraftVersionRecord(
      id = "ver-${System.currentTimeMillis()}",
      siteId = _uiState.value.activeSiteId,
      draftId = draft.id,
      createdAt = System.currentTimeMillis(),
      snapshotJson = draft.toJson().toString()
    )
    _uiState.update { it.copy(draftVersions = listOf(version) + it.draftVersions.take(50)) }
    persistWorkspace()
    notify("Version saved locally.")
  }

  fun restoreDraftSnapshot(version: DraftVersionRecord) {
    try {
      val json = JSONObject(version.snapshotJson)
      val restored = DraftRecord.fromJson(json)
      _uiState.update { state ->
        state.copy(drafts = state.drafts.map { if (it.id == restored.id) restored else it })
      }
      persistWorkspace()
      notify("Draft restored to selected version.")
    } catch (_: Exception) {
      notify("Failed to restore version.")
    }
  }

  fun deleteDraft(id: String) {
    val draft = _uiState.value.drafts.find { it.id == id } ?: return
    // Clean up stored image files for this draft
    draft.images.values.forEach { ref ->
      ImageManager.deleteImage(getApplication(), ref, _uiState.value.activeSiteId)
    }
    _uiState.update { state ->
      state.copy(
        drafts = state.drafts.filter { it.id != id },
        keywords = state.keywords.map { if (it.draftId == id) it.copy(status = "queued", draftId = null) else it },
        selectedDraftId = if (state.selectedDraftId == id) null else state.selectedDraftId
      )
    }
    addLog("draft_deleted", draft.title)
    persistWorkspace()
    notify("Draft deleted.")
  }

  // --- Image Handling ---
  fun storeImageForDraft(draftId: String, kind: String, inputStream: InputStream, declaredMime: String) {
    try {
      val isPin = kind == "pinterest"
      val (ref, _) = ImageManager.storeImageFromStream(
        getApplication(),
        inputStream,
        declaredMime,
        isPin,
        _uiState.value.activeSiteId
      )
      val draft = _uiState.value.drafts.find { it.id == draftId } ?: return
      val newImages = draft.images.toMutableMap().apply { put(kind, ref) }
      updateDraft(draft.copy(images = newImages))
      notify(if (isPin) "Pinterest image saved." else "Featured image saved.")
    } catch (e: Exception) {
      notify(e.message ?: "Could not save image.")
    }
  }

  // --- Publishing ---
  fun publishCurrentDraft(draftId: String) {
    val draft = _uiState.value.drafts.find { it.id == draftId } ?: return
    val settings = _uiState.value.currentSettings
    if (!settings.isConfigured()) {
      showScreen(Screen.SETTINGS)
      notify("Complete WordPress settings before publishing.")
      return
    }

    if (!draft.images.containsKey("featured") || !draft.images.containsKey("pinterest")) {
      notify("Featured and Pinterest images are required before publishing.")
      return
    }

    _uiState.update { it.copy(isLoading = true, loadingMessage = "Publishing to WordPress...") }

    viewModelScope.launch {
      try {
        val (postId, postUrl, pinResult) = publishingService.publishArticle(
          settings = settings,
          draft = draft,
          siteId = _uiState.value.activeSiteId
        )

        val pinStatus = when {
          pinResult.optBoolean("manualReview") -> "manual_review"
          pinResult.optBoolean("published") -> "published"
          pinResult.optBoolean("skipped") -> "not_configured"
          else -> "failed"
        }

        val updatedDraft = draft.copy(
          generationStatus = "published",
          publishedUrl = postUrl,
          wordpressPostId = postId,
          publishedAt = System.currentTimeMillis(),
          pinterestPinId = pinResult.optString("pinId", ""),
          pinterestStatus = pinStatus
        )

        _uiState.update { state ->
          state.copy(
            isLoading = false,
            drafts = state.drafts.map { if (it.id == draftId) updatedDraft else it },
            keywords = state.keywords.map { if (it.id == draft.keywordId) it.copy(status = "published") else it }
          )
        }

        addLog("published", draft.title, articleId = draft.id, wordpressStatus = "published", pinterestStatus = pinStatus, publishedUrl = postUrl)
        persistWorkspace()
        notify("Published successfully to WordPress!")
      } catch (e: Exception) {
        _uiState.update { it.copy(isLoading = false) }
        val isDuplicate = e.message.orEmpty().contains("already exists", ignoreCase = true)
        addLog(if (isDuplicate) "duplicate_prevented" else "publish_failed", draft.title, errorDetails = e.message.orEmpty())
        notify("Publish failed: ${e.message}")
      }
    }
  }

  // --- WordPress Sync & Connection ---
  fun testConnection() {
    val settings = _uiState.value.currentSettings
    if (!settings.isConfigured()) {
      notify("Fill in WordPress URL, username, and application password.")
      return
    }

    _uiState.update { it.copy(isLoading = true, loadingMessage = "Testing WordPress connection...") }
    viewModelScope.launch {
      try {
        val account = publishingService.testWordPressConnection(settings)
        val cats = publishingService.fetchCategories(settings)
        _uiState.update {
          it.copy(
            isLoading = false,
            connectedAccountName = account,
            categories = cats
          )
        }
        persistWorkspace()
        notify("Connected as: $account (${cats.size} categories synced)")
      } catch (e: Exception) {
        _uiState.update { it.copy(isLoading = false) }
        notify("Connection failed: ${e.message}")
      }
    }
  }

  fun syncCategories() {
    val settings = _uiState.value.currentSettings
    if (!settings.isConfigured()) return
    viewModelScope.launch {
      try {
        val cats = publishingService.fetchCategories(settings)
        _uiState.update { it.copy(categories = cats) }
        persistWorkspace()
        notify("Synced ${cats.size} categories.")
      } catch (_: Exception) {}
    }
  }

  // --- Pinterest Boards & Trends ---
  fun loadPinterestBoards() {
    val token = _uiState.value.currentSettings.pinterestAccessToken
    if (token.isBlank()) {
      notify("Add Pinterest Access Token in Settings first.")
      return
    }
    _uiState.update { it.copy(isLoading = true, loadingMessage = "Loading Pinterest boards...") }
    viewModelScope.launch {
      try {
        val boards = publishingService.fetchPinterestBoards(token)
        _uiState.update { it.copy(isLoading = false, pinterestBoards = boards) }
        persistWorkspace()
        notify("Loaded ${boards.size} boards.")
      } catch (e: Exception) {
        _uiState.update { it.copy(isLoading = false) }
        notify("Failed: ${e.message}")
      }
    }
  }

  fun loadTrends(region: String = "US", trendType: String = "growing", limit: Int = 10, profile: String = "food") {
    val token = _uiState.value.currentSettings.pinterestAccessToken
    if (token.isBlank()) {
      notify("Add Pinterest Access Token in Settings first.")
      return
    }
    _uiState.update { it.copy(isLoading = true, loadingMessage = "Loading Pinterest trends...") }
    viewModelScope.launch {
      try {
        val trends = publishingService.fetchPinterestTrends(token, region, trendType, limit, profile)
        _uiState.update { it.copy(isLoading = false, trends = trends) }
        notify("Loaded ${trends.size} trends.")
      } catch (e: Exception) {
        _uiState.update { it.copy(isLoading = false) }
        notify("Trends error: ${e.message}")
      }
    }
  }

  // --- Backup Export / Import ---
  fun exportBackup(): String {
    val s = _uiState.value
    val json = JSONObject()
      .put("activeSiteId", s.activeSiteId)
      .put("siteProfiles", JSONArray(s.siteProfiles.map { JSONObject().put("id", it.id).put("name", it.name) }))
      .put("keywords", JSONArray(s.keywords.map { it.toJson() }))
      .put("drafts", JSONArray(s.drafts.map { it.toJson() }))
      .put("logs", JSONArray(s.logs.map { it.toJson() }))
      .put("categories", JSONArray(s.categories.map { JSONObject().put("id", it.id).put("name", it.name) }))
    return storage.exportBackupJson(json)
  }

  fun importBackup(content: String) {
    try {
      val json = storage.importBackupJson(content)
      val keywords = mutableListOf<KeywordRecord>()
      json.optJSONArray("keywords")?.let { arr ->
        for (i in 0 until arr.length()) keywords.add(KeywordRecord.fromJson(arr.getJSONObject(i)))
      }
      val drafts = mutableListOf<DraftRecord>()
      json.optJSONArray("drafts")?.let { arr ->
        for (i in 0 until arr.length()) drafts.add(DraftRecord.fromJson(arr.getJSONObject(i)))
      }
      _uiState.update { it.copy(keywords = keywords + it.keywords, drafts = drafts + it.drafts) }
      persistWorkspace()
      notify("Backup imported successfully.")
    } catch (e: Exception) {
      notify("Import failed: ${e.message}")
    }
  }

  // --- SEO Audit Calculation ---
  fun computeSeoAudit(draft: DraftRecord): SeoAuditResult {
    val checks = mutableListOf<SeoAuditCheck>()
    val title = draft.title.trim()
    val meta = draft.metaDescription.trim()
    val slug = draft.slug.trim()
    val h2Count = Regex("<h2\\b", RegexOption.IGNORE_CASE).findAll(draft.htmlContent).count()
    val internalLinks = try { JSONArray(draft.internalLinksJson).length() } catch (_: Exception) { 0 }

    checks.add(SeoAuditCheck("Title Length (30–65 chars)", "${title.length} chars", title.length in 30..65))
    checks.add(SeoAuditCheck("Meta Description (1–160 chars)", "${meta.length}/160 chars", meta.isNotEmpty() && meta.length <= 160))
    checks.add(SeoAuditCheck("Canonical Clean Slug", slug.ifBlank { "Missing" }, Regex("^[a-z0-9]+(?:-[a-z0-9]+)*$").matches(slug)))
    checks.add(SeoAuditCheck("H2 Sections Structure", "$h2Count H2 headings", h2Count in 3..8))
    checks.add(SeoAuditCheck("Internal Link Suggestions", "$internalLinks anchors", internalLinks >= 1))
    checks.add(SeoAuditCheck("Featured Image Attached", if (draft.images.containsKey("featured")) "Ready" else "Missing", draft.images.containsKey("featured")))
    checks.add(SeoAuditCheck("Pinterest Image (2:3) Attached", if (draft.images.containsKey("pinterest")) "Ready" else "Missing", draft.images.containsKey("pinterest")))

    val passed = checks.count { it.ok }
    val score = if (checks.isEmpty()) 0 else (passed * 100) / checks.size
    return SeoAuditResult(score, checks)
  }

  private fun addLog(action: String, title: String, articleId: String = "", wordpressStatus: String = "", pinterestStatus: String = "", publishedUrl: String = "", errorDetails: String = "") {
    val log = ActivityLogRecord(
      id = "log-${System.currentTimeMillis()}-${(100..999).random()}",
      siteId = _uiState.value.activeSiteId,
      action = action,
      title = title,
      articleId = articleId,
      wordpressStatus = wordpressStatus,
      pinterestStatus = pinterestStatus,
      publishedUrl = publishedUrl,
      errorDetails = errorDetails
    )
    _uiState.update { it.copy(logs = listOf(log) + it.logs.take(299)) }
  }

  fun notify(message: String) {
    _uiState.update { it.copy(snackbarMessage = message) }
  }

  fun dismissSnackbar() {
    _uiState.update { it.copy(snackbarMessage = null) }
  }
}
