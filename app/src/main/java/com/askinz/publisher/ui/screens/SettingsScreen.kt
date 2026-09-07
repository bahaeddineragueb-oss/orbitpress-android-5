package com.askinz.publisher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.askinz.publisher.OrbitPressViewModel
import com.askinz.publisher.SiteSettings
import com.askinz.publisher.ui.theme.ThemePreset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  viewModel: OrbitPressViewModel,
  onExportBackup: () -> Unit,
  onImportBackup: () -> Unit
) {
  val state by viewModel.uiState.collectAsState()
  val currentSettings = state.currentSettings

  var articleBaseUrl by remember(state.activeSiteId) { mutableStateOf(currentSettings.articleBaseUrl) }
  var articleModel by remember(state.activeSiteId) { mutableStateOf(currentSettings.articleModel) }
  var articleApiKey by remember(state.activeSiteId) { mutableStateOf(currentSettings.articleApiKey) }
  var showArticleKey by remember { mutableStateOf(false) }

  var wpBaseUrl by remember(state.activeSiteId) { mutableStateOf(currentSettings.wordpressBaseUrl) }
  var wpUsername by remember(state.activeSiteId) { mutableStateOf(currentSettings.wordpressUsername) }
  var wpAppPassword by remember(state.activeSiteId) { mutableStateOf(currentSettings.wordpressAppPassword) }
  var showWpPass by remember { mutableStateOf(false) }
  var selectedCategoryId by remember(state.activeSiteId) { mutableStateOf(currentSettings.categoryId) }

  var imageMode by remember(state.activeSiteId) { mutableStateOf(currentSettings.imageMode) }
  var imageProvider by remember(state.activeSiteId) { mutableStateOf(currentSettings.imageProvider) }
  var imageBaseUrl by remember(state.activeSiteId) { mutableStateOf(currentSettings.imageBaseUrl) }
  var imageModel by remember(state.activeSiteId) { mutableStateOf(currentSettings.imageModel) }
  var imageApiKey by remember(state.activeSiteId) { mutableStateOf(currentSettings.imageApiKey) }
  var cfAccountId by remember(state.activeSiteId) { mutableStateOf(currentSettings.cloudflareAccountId) }
  var cfModel by remember(state.activeSiteId) { mutableStateOf(currentSettings.cloudflareModel) }
  var cfToken by remember(state.activeSiteId) { mutableStateOf(currentSettings.cloudflareApiToken) }

  var pinToken by remember(state.activeSiteId) { mutableStateOf(currentSettings.pinterestAccessToken) }
  var pinBoardId by remember(state.activeSiteId) { mutableStateOf(currentSettings.pinterestBoardId) }
  var pinMode by remember(state.activeSiteId) { mutableStateOf(currentSettings.pinterestPublishingMode) }

  var showNewSiteDialog by remember { mutableStateOf(false) }
  var showPinSetupDialog by remember { mutableStateOf(false) }

  fun currentSnapshot(): SiteSettings = SiteSettings(
    articleBaseUrl = articleBaseUrl,
    articleModel = articleModel,
    articleApiKey = articleApiKey,
    wordpressBaseUrl = wpBaseUrl,
    wordpressUsername = wpUsername,
    wordpressAppPassword = wpAppPassword,
    categoryId = selectedCategoryId,
    imageMode = imageMode,
    imageProvider = imageProvider,
    imageBaseUrl = imageBaseUrl,
    imageModel = imageModel,
    imageApiKey = imageApiKey,
    cloudflareAccountId = cfAccountId,
    cloudflareModel = cfModel,
    cloudflareApiToken = cfToken,
    pinterestAccessToken = pinToken,
    pinterestBoardId = pinBoardId,
    pinterestPublishingMode = pinMode,
    profilePrompts = currentSettings.profilePrompts
  )

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
  ) {
    // 🎨 App Appearance & Theme Studio Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
              Text("Theme & Visual Style", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
          }

          Text(
            "Select your favorite aesthetic theme palette:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          // Theme Presets Swatches
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(ThemePreset.entries) { preset ->
              val isSelected = state.themePreset.equals(preset.id, ignoreCase = true)
              Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                modifier = Modifier
                  .width(135.dp)
                  .clickable { viewModel.setThemePreset(preset.id) }
              ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(Modifier.size(16.dp).clip(CircleShape).background(preset.primaryLight))
                    Box(Modifier.size(16.dp).clip(CircleShape).background(preset.secondaryColor))
                    Box(Modifier.size(16.dp).clip(CircleShape).background(preset.accentColor))
                  }
                  Text(preset.displayName, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                  Text(preset.subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                }
              }
            }
          }

          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

          // Dark Mode & AMOLED switches
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Night / Dark Mode", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
              Text("Switch between Light and Dark interface", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(
              checked = state.isNightMode,
              onCheckedChange = { viewModel.toggleNightMode() }
            )
          }

          if (state.isNightMode) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text("Pure AMOLED Pitch Black", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                Text("True #000000 background for OLED battery savings", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
              Switch(
                checked = state.isAmoled,
                onCheckedChange = { viewModel.toggleAmoled() }
              )
            }
          }
        }
      }
    }

    // 🌐 Website Profiles Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
              Text("Website Profiles", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
            FilledTonalButton(
              onClick = { showNewSiteDialog = true },
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(Modifier.width(4.dp))
              Text("Add Site", fontSize = 12.sp)
            }
          }

          // Horizontal list of site chips
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(state.siteProfiles) { profile ->
              val isSelected = profile.id == state.activeSiteId
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.clickable { viewModel.switchSite(profile.id) }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                  }
                  Text(
                    profile.name,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                  )
                }
              }
            }
          }
        }
      }
    }

    // 🤖 Article API Credentials Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text("Article AI Engine", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          }
          Text("Compatible with OpenAI, OpenRouter, DeepSeek, Groq & Claude bridges.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

          OutlinedTextField(
            value = articleBaseUrl,
            onValueChange = { articleBaseUrl = it },
            label = { Text("Base URL (HTTPS)") },
            placeholder = { Text("https://api.openai.com/v1") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = articleModel,
            onValueChange = { articleModel = it },
            label = { Text("Model Name") },
            placeholder = { Text("gpt-4o / claude-3-5-sonnet") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = articleApiKey,
            onValueChange = { articleApiKey = it },
            label = { Text("API Secret Key") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (showArticleKey) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
              IconButton(onClick = { showArticleKey = !showArticleKey }) {
                Icon(if (showArticleKey) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
              }
            },
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }

    // 🌐 WordPress Credentials Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Public, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text("WordPress REST Publishing", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          }

          OutlinedTextField(
            value = wpBaseUrl,
            onValueChange = { wpBaseUrl = it },
            label = { Text("WordPress Site URL (HTTPS)") },
            placeholder = { Text("https://yourblog.com") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = wpUsername,
            onValueChange = { wpUsername = it },
            label = { Text("WordPress Username") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = wpAppPassword,
            onValueChange = { wpAppPassword = it },
            label = { Text("Application Password") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (showWpPass) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
              IconButton(onClick = { showWpPass = !showWpPass }) {
                Icon(if (showWpPass) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
              }
            },
            modifier = Modifier.fillMaxWidth()
          )

          // Default Category dropdown
          var catExpanded by remember { mutableStateOf(false) }
          ExposedDropdownMenuBox(
            expanded = catExpanded,
            onExpandedChange = { catExpanded = it },
            modifier = Modifier.fillMaxWidth()
          ) {
            val catName = state.categories.find { it.id.toString() == selectedCategoryId }?.name ?: "Select Default Category"
            OutlinedTextField(
              value = catName,
              onValueChange = {},
              readOnly = true,
              label = { Text("Default Category") },
              shape = RoundedCornerShape(12.dp),
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
              modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(
              expanded = catExpanded,
              onDismissRequest = { catExpanded = false }
            ) {
              state.categories.forEach { cat ->
                DropdownMenuItem(
                  text = { Text(cat.name) },
                  onClick = {
                    selectedCategoryId = cat.id.toString()
                    catExpanded = false
                  }
                )
              }
            }
          }

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
              onClick = {
                viewModel.saveSettings(currentSnapshot())
                viewModel.testConnection()
              },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(6.dp))
              Text("Test & Sync", maxLines = 1)
            }
            Button(
              onClick = { viewModel.saveSettings(currentSnapshot()) },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(6.dp))
              Text("Save Settings", maxLines = 1)
            }
          }
        }
      }
    }

    // 🖼️ Image Generator & AI Setup Card (Fixed Full-Width Responsive Toggle)
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text("Image Generation Mode", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          }

          // Full-Width Segmented Option Selector (Prevents any vertical wrapping!)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (imageMode == "manual") MaterialTheme.colorScheme.primary else Color.Transparent,
              modifier = Modifier
                .weight(1f)
                .clickable { imageMode = "manual" }
            ) {
              Row(
                modifier = Modifier.padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.AddPhotoAlternate,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = if (imageMode == "manual") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(6.dp))
                Text(
                  "Manual Upload",
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 13.sp,
                  maxLines = 1,
                  color = if (imageMode == "manual") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (imageMode == "automatic") MaterialTheme.colorScheme.primary else Color.Transparent,
              modifier = Modifier
                .weight(1f)
                .clickable { imageMode = "automatic" }
            ) {
              Row(
                modifier = Modifier.padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.AutoAwesome,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = if (imageMode == "automatic") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(6.dp))
                Text(
                  "Auto AI Images",
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 13.sp,
                  maxLines = 1,
                  color = if (imageMode == "automatic") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          if (imageMode == "automatic") {
            Text("AI Image Provider:", fontSize = 12.sp, fontWeight = FontWeight.Bold)

            // Provider Switcher (Full Width)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (imageProvider == "cloudflare") MaterialTheme.colorScheme.secondary else Color.Transparent,
                modifier = Modifier.weight(1f).clickable { imageProvider = "cloudflare" }
              ) {
                Row(
                  modifier = Modifier.padding(vertical = 8.dp),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    "Cloudflare Flux AI",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    maxLines = 1,
                    color = if (imageProvider == "cloudflare") MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (imageProvider == "openai") MaterialTheme.colorScheme.secondary else Color.Transparent,
                modifier = Modifier.weight(1f).clickable { imageProvider = "openai" }
              ) {
                Row(
                  modifier = Modifier.padding(vertical = 8.dp),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    "OpenAI DALL-E 3",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    maxLines = 1,
                    color = if (imageProvider == "openai") MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }

            if (imageProvider == "cloudflare") {
              OutlinedTextField(
                value = cfAccountId,
                onValueChange = { cfAccountId = it },
                label = { Text("Cloudflare Account ID") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = cfModel,
                onValueChange = { cfModel = it },
                label = { Text("Cloudflare Model") },
                placeholder = { Text("@cf/black-forest-labs/flux-1-schnell") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = cfToken,
                onValueChange = { cfToken = it },
                label = { Text("Cloudflare API Token") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
              )
            } else {
              OutlinedTextField(
                value = imageBaseUrl,
                onValueChange = { imageBaseUrl = it },
                label = { Text("Image API Base URL") },
                placeholder = { Text("https://api.openai.com/v1") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = imageModel,
                onValueChange = { imageModel = it },
                label = { Text("Image Model Name") },
                placeholder = { Text("dall-e-3") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = imageApiKey,
                onValueChange = { imageApiKey = it },
                label = { Text("Image API Secret Key") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
              )
            }
          }
        }
      }
    }

    // 📌 Pinterest Integration Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.PushPin, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text("Pinterest API Integration", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          }

          OutlinedTextField(
            value = pinToken,
            onValueChange = { pinToken = it },
            label = { Text("Pinterest Access Token (v5)") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
          )

          Button(
            onClick = {
              viewModel.saveSettings(currentSnapshot())
              viewModel.loadPinterestBoards()
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Fetch Pinterest Boards (${state.pinterestBoards.size} Loaded)")
          }
        }
      }
    }

    // 🔒 Device Security & PIN Lock Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text("Device Security & PIN Protection", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          }
          Text("Protect settings, API keys, and passwords on this phone with a 4–12 digit salted PIN code.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

          FilledTonalButton(
            onClick = { showPinSetupDialog = true },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(if (state.isPinLockEnabled) "Modify or Remove PIN Lock" else "Enable Security PIN Lock")
          }
        }
      }
    }

    // 💾 Workspace Backup & Restore Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.FolderZip, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text("Encrypted Backup & Restore", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          }
          Text("Export or import all drafts, queues, and activity logs via Android Storage Access Framework (SAF).", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
              onClick = onExportBackup,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(6.dp))
              Text("Export JSON", maxLines = 1)
            }
            OutlinedButton(
              onClick = onImportBackup,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.FileUpload, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(6.dp))
              Text("Import JSON", maxLines = 1)
            }
          }
        }
      }
    }
  }

  // New Site Dialog
  if (showNewSiteDialog) {
    var siteName by remember { mutableStateOf("") }
    AlertDialog(
      onDismissRequest = { showNewSiteDialog = false },
      title = { Text("Add Website Profile", fontWeight = FontWeight.Bold) },
      text = {
        OutlinedTextField(
          value = siteName,
          onValueChange = { siteName = it },
          label = { Text("Website Name") },
          placeholder = { Text("e.g. Askinz Recipes") },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        )
      },
      confirmButton = {
        Button(
          onClick = {
            if (siteName.isNotBlank()) {
              viewModel.addSite(siteName)
              showNewSiteDialog = false
            }
          }
        ) {
          Text("Add Website")
        }
      },
      dismissButton = {
        TextButton(onClick = { showNewSiteDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  // PIN Setup Dialog
  if (showPinSetupDialog) {
    var newPin by remember { mutableStateOf("") }
    AlertDialog(
      onDismissRequest = { showPinSetupDialog = false },
      title = { Text(if (state.isPinLockEnabled) "Manage PIN Lock" else "Set Security PIN", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Enter 4–12 digits, or leave blank to remove PIN lock.")
          OutlinedTextField(
            value = newPin,
            onValueChange = { if (it.length <= 12 && it.all { c -> c.isDigit() }) newPin = it },
            label = { Text("New PIN (blank to disable)") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.setPinLock(newPin)
            showPinSetupDialog = false
          }
        ) {
          Text("Save PIN")
        }
      },
      dismissButton = {
        TextButton(onClick = { showPinSetupDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
