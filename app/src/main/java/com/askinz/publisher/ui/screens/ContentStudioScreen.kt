package com.askinz.publisher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.askinz.publisher.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentStudioScreen(
  viewModel: OrbitPressViewModel,
  onPickKeywordsFile: () -> Unit,
  onOpenSettings: () -> Unit
) {
  val state by viewModel.uiState.collectAsState()

  var keywordText by remember { mutableStateOf("") }
  var nicheProfile by remember { mutableStateOf(ContentProfileContract.FOOD) }
  var contentType by remember { mutableStateOf("article") }
  var priority by remember { mutableStateOf("normal") }
  var selectedCategoryId by remember { mutableIntStateOf(0) }
  var selectedCategoryName by remember { mutableStateOf("") }
  var selectedBoardId by remember { mutableStateOf("") }
  var showBatchImportDialog by remember { mutableStateOf(false) }

  val isConfigured = state.currentSettings.isConfigured()

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(vertical = 16.dp)
  ) {
    // Connection Status Card
    item {
      Card(
        colors = CardDefaults.cardColors(
          containerColor = if (isConfigured) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (isConfigured) "Publishing is Ready" else "Settings Required",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.titleMedium,
              color = if (isConfigured) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
              text = if (isConfigured) {
                if (state.connectedAccountName != null) "Connected as ${state.connectedAccountName} (${state.categories.size} categories)" else "Settings saved. Ready to generate and publish."
              } else {
                "Configure Article API and WordPress credentials in Settings to start."
              },
              style = MaterialTheme.typography.bodySmall,
              color = if (isConfigured) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer
            )
          }
          if (!isConfigured) {
            Button(
              onClick = onOpenSettings,
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
              modifier = Modifier.padding(start = 8.dp)
            ) {
              Text("Settings")
            }
          }
        }
      }
    }

    // Add Keyword Form Card
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
            Text("Queue Content Idea", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { showBatchImportDialog = true }) {
              Icon(Icons.Default.FileUpload, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(Modifier.width(4.dp))
              Text("Batch Import")
            }
          }

          OutlinedTextField(
            value = keywordText,
            onValueChange = { keywordText = it },
            label = { Text("Article Keyword / Topic") },
            placeholder = { Text("e.g. 5 easy chicken dinner recipes") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          // Niche Profile Selector
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            var profileExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
              expanded = profileExpanded,
              onExpandedChange = { profileExpanded = it },
              modifier = Modifier.weight(1f)
            ) {
              OutlinedTextField(
                value = when (nicheProfile) {
                  ContentProfileContract.FOOD -> "Food & Recipes"
                  ContentProfileContract.GARDENING -> "Gardening"
                  ContentProfileContract.HOME_DECOR -> "Home Decor"
                  else -> "Custom Niche"
                },
                onValueChange = {},
                readOnly = true,
                label = { Text("Niche Profile") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = profileExpanded) },
                modifier = Modifier.menuAnchor()
              )
              ExposedDropdownMenu(
                expanded = profileExpanded,
                onDismissRequest = { profileExpanded = false }
              ) {
                DropdownMenuItem(
                  text = { Text("Food & Recipes") },
                  onClick = { nicheProfile = ContentProfileContract.FOOD; profileExpanded = false }
                )
                DropdownMenuItem(
                  text = { Text("Gardening") },
                  onClick = { nicheProfile = ContentProfileContract.GARDENING; contentType = "article"; profileExpanded = false }
                )
                DropdownMenuItem(
                  text = { Text("Home Decor") },
                  onClick = { nicheProfile = ContentProfileContract.HOME_DECOR; contentType = "article"; profileExpanded = false }
                )
                DropdownMenuItem(
                  text = { Text("Custom") },
                  onClick = { nicheProfile = ContentProfileContract.CUSTOM; profileExpanded = false }
                )
              }
            }

            // Category Dropdown
            var catExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
              expanded = catExpanded,
              onExpandedChange = { catExpanded = it },
              modifier = Modifier.weight(1f)
            ) {
              OutlinedTextField(
                value = selectedCategoryName.ifBlank { "Category" },
                onValueChange = {},
                readOnly = true,
                label = { Text("WP Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                modifier = Modifier.menuAnchor()
              )
              ExposedDropdownMenu(
                expanded = catExpanded,
                onDismissRequest = { catExpanded = false }
              ) {
                if (state.categories.isEmpty()) {
                  DropdownMenuItem(
                    text = { Text("No categories synced") },
                    onClick = { catExpanded = false }
                  )
                } else {
                  state.categories.forEach { cat ->
                    DropdownMenuItem(
                      text = { Text(cat.name) },
                      onClick = {
                        selectedCategoryId = cat.id
                        selectedCategoryName = cat.name
                        catExpanded = false
                      }
                    )
                  }
                }
              }
            }
          }

          Button(
            onClick = {
              if (keywordText.isNotBlank()) {
                viewModel.addKeyword(
                  keyword = keywordText,
                  contentType = contentType,
                  nicheProfile = nicheProfile,
                  priority = priority,
                  categoryId = selectedCategoryId,
                  categoryName = selectedCategoryName,
                  pinterestBoardId = selectedBoardId
                )
                keywordText = ""
              }
            },
            enabled = keywordText.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Add to Generation Queue")
          }
        }
      }
    }

    // Queue List Header & Cards
    item {
      Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Content Queue (${state.keywords.size})", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
      }
    }

    if (state.keywords.isEmpty()) {
      item {
        Box(
          modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Queue, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.outline)
            Spacer(Modifier.height(8.dp))
            Text("Your queue is ready for ideas.", fontWeight = FontWeight.SemiBold)
            Text("Add a keyword above to start generating SEO articles.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    } else {
      items(state.keywords, key = { it.id }) { kw ->
        KeywordQueueCard(
          keyword = kw,
          onGenerate = { viewModel.generateDraft(kw.id) },
          onOpenDraft = { if (kw.draftId != null) viewModel.openDraft(kw.draftId) },
          onDelete = { viewModel.deleteKeyword(kw.id) }
        )
      }
    }
  }

  // Batch Import Dialog
  if (showBatchImportDialog) {
    var importText by remember { mutableStateOf("") }
    AlertDialog(
      onDismissRequest = { showBatchImportDialog = false },
      title = { Text("Batch Import Keywords", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Paste keywords (one per line or comma-separated), or select a text/CSV file.")
          OutlinedTextField(
            value = importText,
            onValueChange = { importText = it },
            placeholder = { Text("best indoor plants\nhow to prune tomatoes\neasy pasta recipes") },
            modifier = Modifier.fillMaxWidth().height(150.dp),
            maxLines = 8
          )
          OutlinedButton(
            onClick = {
              showBatchImportDialog = false
              onPickKeywordsFile()
            },
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.FileOpen, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Select File (.txt / .csv)")
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (importText.isNotBlank()) {
              viewModel.importKeywords(importText, nicheProfile, selectedCategoryId, selectedCategoryName)
              showBatchImportDialog = false
            }
          }
        ) {
          Text("Import Lines")
        }
      },
      dismissButton = {
        TextButton(onClick = { showBatchImportDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
fun KeywordQueueCard(
  keyword: KeywordRecord,
  onGenerate: () -> Unit,
  onOpenDraft: () -> Unit,
  onDelete: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(keyword.keyword, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        StatusBadge(status = keyword.status)
      }

      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        if (keyword.categoryName.isNotBlank()) {
          Text("Category: ${keyword.categoryName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("Profile: ${keyword.nicheProfile}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      if (keyword.errorDetails.isNotBlank()) {
        Text(keyword.errorDetails, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onDelete) {
          Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
        }
        if (keyword.draftId != null) {
          FilledTonalButton(onClick = onOpenDraft) {
            Icon(Icons.Default.EditDocument, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Open Draft")
          }
        } else {
          Button(
            onClick = onGenerate,
            enabled = keyword.status != "generating"
          ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(if (keyword.status == "generating") "Generating..." else "Generate")
          }
        }
      }
    }
  }
}

@Composable
fun StatusBadge(status: String) {
  val (bgColor, textColor, label) = when (status) {
    "published" -> Triple(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer, "Published")
    "ready" -> Triple(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer, "Draft Ready")
    "generating" -> Triple(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer, "Generating…")
    "failed" -> Triple(MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer, "Failed")
    else -> Triple(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant, "Queued")
  }

  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(bgColor)
      .padding(horizontal = 8.dp, vertical = 4.dp)
  ) {
    Text(label, color = textColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
  }
}
