package com.askinz.publisher

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.json.JSONArray
import org.json.JSONObject

class SecureStorage(private val context: Context) {
  private val preferences: SharedPreferences by lazy {
    try {
      val key = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
      EncryptedSharedPreferences.create(
        context,
        "askinz_secure_settings",
        key,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
      )
    } catch (_: Exception) {
      // Fallback for devices with corrupted Android KeyStore
      context.getSharedPreferences("askinz_settings_fallback", Context.MODE_PRIVATE)
    }
  }

  // --- PIN Lock ---
  fun isPinLockEnabled(): Boolean =
    preferences.getString("settingsLockHash", "").orEmpty().isNotBlank()

  fun savePinLock(pin: String) {
    val normalized = SettingsLockContract.normalizePin(pin)
    val editor = preferences.edit()
    if (normalized.isBlank()) {
      editor.remove("settingsLockHash").apply()
      return
    }
    require(SettingsLockContract.isValidPin(normalized)) { "Settings PIN must contain 4 to 12 digits." }
    editor.putString("settingsLockHash", SettingsLockContract.hashPin(normalized)).apply()
  }

  fun verifyPinLock(pin: String): Boolean = try {
    SettingsLockContract.matches(pin, preferences.getString("settingsLockHash", "").orEmpty())
  } catch (_: Exception) {
    false
  }

  // --- Settings per Site ---
  fun loadSiteSettings(siteId: String): SiteSettings {
    val canonicalSiteId = SettingsPersistenceContract.canonicalSiteId(siteId)
    val profilesJson = try { JSONObject(preferences.getString("settingsBySite", "{}") ?: "{}") } catch (_: Exception) { JSONObject() }
    val siteJson = profilesJson.optJSONObject(canonicalSiteId) ?: try { JSONObject(preferences.getString("settings", "{}") ?: "{}") } catch (_: Exception) { JSONObject() }
    return SiteSettings.fromJson(siteJson)
  }

  fun saveSiteSettings(siteId: String, settings: SiteSettings) {
    val canonicalSiteId = SettingsPersistenceContract.canonicalSiteId(siteId)
    val profilesJson = try { JSONObject(preferences.getString("settingsBySite", "{}") ?: "{}") } catch (_: Exception) { JSONObject() }
    val existing = profilesJson.optJSONObject(canonicalSiteId) ?: JSONObject()
    val merged = SettingsPersistenceContract.merge(existing, settings.toJson())
    profilesJson.put(canonicalSiteId, merged)
    preferences.edit().putString("settingsBySite", profilesJson.toString()).apply()
  }

  // --- Workspace Persistence ---
  fun loadWorkspaceJson(): String = preferences.getString("workspace", "{}") ?: "{}"

  fun saveWorkspaceJson(json: String) {
    require(json.length <= 5_000_000) { "Workspace is too large." }
    preferences.edit().putString("workspace", json).apply()
  }

  fun exportBackupJson(workspace: JSONObject): String {
    val payload = JSONObject()
      .put("format", "OrbitPress local workspace backup")
      .put("version", 2)
      .put("exportedAt", System.currentTimeMillis())
      .put("workspace", workspace)
    return payload.toString(2)
  }

  fun importBackupJson(backupContent: String): JSONObject {
    val payload = JSONObject(backupContent)
    return payload.optJSONObject("workspace") ?: payload
  }
}
