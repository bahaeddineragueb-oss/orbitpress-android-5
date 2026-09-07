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
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
  ) {
    // 🚀 Connection Status Hero Card
    item {
      Card(
        colors = CardDefaults.cardColors(
          containerColor = if (isConfigured) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(18.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Icon(
                if (isConfigured) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (isConfigured) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer
              )
              Text(
                text = if (isConfigured) "Publishing Ready" else "Settings Required",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (isConfigured) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer
              )
            }
            Text(
              text = if (isConfigured) {
                if (state.connectedAccountName != null) "WordPress connected: ${state.connectedAccountName} (${state.categories.size} categories synced)" else "Settings active. Ready to generate and publish."
              } else {
                "Configure Article AI and WordPress credentials in Settings to start."
              },
              style = MaterialTheme.typography.bodySmall,
              color = if (isConfigured) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onErrorContainer
            )
          }
          if (!isConfigured) {
            Button(
              onClick = onOpenSettings,
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
              modifier = Modifier.padding(start = 8.dp)
            ) {
              Text("Setup", fontSize = 12.sp)
            }
          }
        }
      }
    }

    // 💡 Add Keyword Form Card
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
              Icon(Icons.Default.Queue, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
              Text("Queue Content Idea", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
            FilledTonalButton(
              onClick = { showBatchImportDialog = true },
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Icon(Icons.Default.FileUpload, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(Modifier.width(4.dp))
              Text("Batch Import", fontSize = 12.sp)
            }
          }

          OutlinedTextField(
            value = keywordText,
            onValueChange = { keywordText = it },
            label = { Text("Article Keyword / Topic") },
            placeholder = { Text("e.g. 5 quick high protein breakfast ideas") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          )

          // Niche Profile Selector & Category Dropdown
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
                shape = RoundedCornerShape(12.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = profileExpanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
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
                value = selectedCategoryName.ifBlank { "WP Category" },
                onValueChange = {},
                readOnly = true,
                label = { Text("WP Category") },
                shape = RoundedCornerShape(12.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
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
            Text("Add to Generation Queue", fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // 📋 Queue List Header & Cards
    item {
      Row(
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Content Queue (${state.keywords.size})", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
      }
    }

    if (state.keywords.isEmpty()) {
      item {
        Box(
          modifier = Modifier.fillMaxWidth().padding(vertical = 36.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Queue, contentDescription = null, modifier = Modifier.size(52.dp), tint = MaterialTheme.colorScheme.outline)
            Text("Your queue is ready for ideas.", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
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
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(140.dp),
            maxLines = 8
          )
          OutlinedButton(
            onClick = {
              showBatchImportDialog = false
              onPickKeywordsFile()
            },
            shape = RoundedCornerShape(12.dp),
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
          Text("Import")
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
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(keyword.keyword, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f), maxLines = 2)
        Spacer(Modifier.width(8.dp))
        StatusBadge(status = keyword.status)
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ) {
          Text(
            keyword.nicheProfile.replaceFirstChar { it.uppercase() },
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        if (keyword.categoryName.isNotBlank()) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
          ) {
            Text(
              keyword.categoryName,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              color = MaterialTheme.colorScheme.primary
            )
          }
        }
      }

      if (keyword.errorDetails.isNotBlank()) {
        Text(keyword.errorDetails, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onDelete) {
          Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
        }
        if (keyword.draftId != null) {
          FilledTonalButton(
            onClick = onOpenDraft,
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Open Draft", maxLines = 1)
          }
        } else {
          Button(
            onClick = onGenerate,
            shape = RoundedCornerShape(10.dp),
            enabled = keyword.status != "generating"
          ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(if (keyword.status == "generating") "Generating..." else "Generate Article", maxLines = 1)
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
      .padding(horizontal = 10.dp, vertical = 5.dp)
  ) {
    Text(label, color = textColor, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
  }
}
