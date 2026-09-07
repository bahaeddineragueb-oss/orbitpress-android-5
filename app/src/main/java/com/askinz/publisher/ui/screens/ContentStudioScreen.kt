package com.askinz.publisher.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
    // 🚀 Connection Status Hero Card (Cadre Style)
    item {
      Surface(
        color = if (isConfigured) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.2.dp, if (isConfigured) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
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

    // 💡 Add Keyword Form Card (Cadre Style)
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
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
            shape = RoundedCornerShape(14.dp),
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
            shape = RoundedCornerShape(14.dp)
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
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = MaterialTheme.colorScheme.surface,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
          Box(
            modifier = Modifier.padding(32.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Queue, contentDescription = null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
              }
              Text("Your queue is ready for ideas.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
              Text("Add a keyword above to start generating SEO articles.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
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
          },
          shape = RoundedCornerShape(10.dp)
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
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surface,
    border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
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
          color = MaterialTheme.colorScheme.surfaceVariant,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
          Text(
            keyword.nicheProfile.replaceFirstChar { it.uppercase() },
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        if (keyword.categoryName.isNotBlank()) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
          ) {
            Text(
              keyword.categoryName,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              color = MaterialTheme.colorScheme.primary
            )
          }
        }
      }

      if (keyword.errorDetails.isNotBlank()) {
        Text(keyword.errorDetails, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

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
            Text("Open Draft", fontWeight = FontWeight.Bold, maxLines = 1)
          }
        } else {
          Button(
            onClick = onGenerate,
            shape = RoundedCornerShape(12.dp),
            enabled = keyword.status != "generating"
          ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(if (keyword.status == "generating") "Generating..." else "Generate Article", fontWeight = FontWeight.Bold, maxLines = 1)
          }
        }
      }
    }
  }
}

@Composable
fun StatusBadge(status: String) {
  val (bgColor, textColor, borderColor, label) = when (status) {
    "published" -> Quadruple(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer, MaterialTheme.colorScheme.secondary, "Published")
    "ready" -> Quadruple(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer, MaterialTheme.colorScheme.primary, "Draft Ready")
    "generating" -> Quadruple(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer, MaterialTheme.colorScheme.tertiary, "Generating…")
    "failed" -> Quadruple(MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer, MaterialTheme.colorScheme.error, "Failed")
    else -> Quadruple(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant, MaterialTheme.colorScheme.outline, "Queued")
  }

  Surface(
    shape = RoundedCornerShape(10.dp),
    color = bgColor,
    border = BorderStroke(1.dp, borderColor.copy(alpha = 0.5f))
  ) {
    Text(
      label,
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
      maxLines = 1
    )
  }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
