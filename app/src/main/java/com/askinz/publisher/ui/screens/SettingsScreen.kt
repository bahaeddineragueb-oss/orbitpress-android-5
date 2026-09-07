package com.askinz.publisher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.askinz.publisher.OrbitPressViewModel
import com.askinz.publisher.SiteSettings

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
    contentPadding = PaddingValues(vertical = 16.dp)
  ) {
    // Website Profiles Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Website Profile", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { showNewSiteDialog = true }) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(4.dp))
              Text("Add Website")
            }
          }

          var siteExpanded by remember { mutableStateOf(false) }
          ExposedDropdownMenuBox(
            expanded = siteExpanded,
            onExpandedChange = { siteExpanded = it },
            modifier = Modifier.fillMaxWidth()
          ) {
            val activeName = state.siteProfiles.find { it.id == state.activeSiteId }?.name ?: "Default Website"
            OutlinedTextField(
              value = activeName,
              onValueChange = {},
              readOnly = true,
              label = { Text("Active Website") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = siteExpanded) },
              modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(
              expanded = siteExpanded,
              onDismissRequest = { siteExpanded = false }
            ) {
              state.siteProfiles.forEach { profile ->
                DropdownMenuItem(
                  text = { Text(profile.name) },
                  onClick = {
                    viewModel.switchSite(profile.id)
                    siteExpanded = false
                  }
                )
              }
            }
          }
        }
      }
    }

    // Article API Credentials Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Article API (OpenAI Compatible)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

          OutlinedTextField(
            value = articleBaseUrl,
            onValueChange = { articleBaseUrl = it },
            label = { Text("Base URL (HTTPS)") },
            placeholder = { Text("https://api.openai.com/v1") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = articleModel,
            onValueChange = { articleModel = it },
            label = { Text("Model Name") },
            placeholder = { Text("gpt-4o / claude-3-5-sonnet") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = articleApiKey,
            onValueChange = { articleApiKey = it },
            label = { Text("API Key") },
            singleLine = true,
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

    // WordPress Credentials Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("WordPress REST API", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

          OutlinedTextField(
            value = wpBaseUrl,
            onValueChange = { wpBaseUrl = it },
            label = { Text("WordPress Site URL (HTTPS)") },
            placeholder = { Text("https://example.com") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = wpUsername,
            onValueChange = { wpUsername = it },
            label = { Text("WordPress Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = wpAppPassword,
            onValueChange = { wpAppPassword = it },
            label = { Text("Application Password") },
            singleLine = true,
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
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(6.dp))
              Text("Test & Sync")
            }
            Button(
              onClick = { viewModel.saveSettings(currentSnapshot()) },
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(6.dp))
              Text("Save Settings")
            }
          }
        }
      }
    }

    // Image Generator / Cloudflare AI Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Image Generation", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
              selected = imageMode == "manual",
              onClick = { imageMode = "manual" },
              label = { Text("Manual Image Upload") }
            )
            FilterChip(
              selected = imageMode == "automatic",
              onClick = { imageMode = "automatic" },
              label = { Text("Auto AI Images") }
            )
          }

          if (imageMode == "automatic") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              FilterChip(
                selected = imageProvider == "openai",
                onClick = { imageProvider = "openai" },
                label = { Text("OpenAI Compatible") }
              )
              FilterChip(
                selected = imageProvider == "cloudflare",
                onClick = { imageProvider = "cloudflare" },
                label = { Text("Cloudflare Workers AI") }
              )
            }

            if (imageProvider == "cloudflare") {
              OutlinedTextField(
                value = cfAccountId,
                onValueChange = { cfAccountId = it },
                label = { Text("Cloudflare Account ID") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = cfModel,
                onValueChange = { cfModel = it },
                label = { Text("Cloudflare Model") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = cfToken,
                onValueChange = { cfToken = it },
                label = { Text("Cloudflare API Token") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
              )
            } else {
              OutlinedTextField(
                value = imageBaseUrl,
                onValueChange = { imageBaseUrl = it },
                label = { Text("Image API Base URL") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = imageModel,
                onValueChange = { imageModel = it },
                label = { Text("Image Model (e.g. dall-e-3)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = imageApiKey,
                onValueChange = { imageApiKey = it },
                label = { Text("Image API Key") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
              )
            }
          }
        }
      }
    }

    // Pinterest Integration Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Pinterest Integration", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

          OutlinedTextField(
            value = pinToken,
            onValueChange = { pinToken = it },
            label = { Text("Pinterest Access Token") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
          )

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
              onClick = {
                viewModel.saveSettings(currentSnapshot())
                viewModel.loadPinterestBoards()
              },
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(Modifier.width(6.dp))
              Text("Load Boards")
            }
          }
        }
      }
    }

    // Security & PIN Lock Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Device Security & PIN Lock", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          Text("Optionally protect publishing settings on this phone with a 4–12 digit PIN code.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

          Button(
            onClick = { showPinSetupDialog = true },
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(if (state.isPinLockEnabled) "Change / Remove PIN Lock" else "Enable Settings PIN Lock")
          }
        }
      }
    }

    // Local Backup & Restore Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Workspace Backup & Restore", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          Text("Export or import your drafts, keywords, and logs safely using Android Storage Access Framework (SAF). Secrets are excluded from export.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onExportBackup, modifier = Modifier.weight(1f)) {
              Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(6.dp))
              Text("Export JSON")
            }
            OutlinedButton(onClick = onImportBackup, modifier = Modifier.weight(1f)) {
              Icon(Icons.Default.FileUpload, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(6.dp))
              Text("Import JSON")
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
      title = { Text(if (state.isPinLockEnabled) "Manage PIN Lock" else "Set Settings PIN", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Enter 4–12 digits, or leave blank to remove PIN lock.")
          OutlinedTextField(
            value = newPin,
            onValueChange = { if (it.length <= 12 && it.all { c -> c.isDigit() }) newPin = it },
            label = { Text("New PIN (blank to remove)") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
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
