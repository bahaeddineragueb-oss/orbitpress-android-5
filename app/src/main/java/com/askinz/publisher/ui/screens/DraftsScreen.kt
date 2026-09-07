package com.askinz.publisher.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.askinz.publisher.DraftRecord
import com.askinz.publisher.DraftVersionRecord
import com.askinz.publisher.OrbitPressViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftsScreen(
  viewModel: OrbitPressViewModel
) {
  val state by viewModel.uiState.collectAsState()
  var selectedFilter by remember { mutableStateOf("all") }
  var searchQuery by remember { mutableStateOf("") }
  var draftToDelete by remember { mutableStateOf<DraftRecord?>(null) }
  var versionsDraft by remember { mutableStateOf<DraftRecord?>(null) }

  val filteredDrafts = remember(state.drafts, selectedFilter, searchQuery) {
    state.drafts.filter { draft ->
      val matchesFilter = when (selectedFilter) {
        "ready" -> draft.generationStatus == "ready"
        "published" -> draft.generationStatus == "published"
        else -> true
      }
      val matchesSearch = searchQuery.isBlank() ||
        draft.title.contains(searchQuery, ignoreCase = true) ||
        draft.slug.contains(searchQuery, ignoreCase = true) ||
        draft.nicheProfile.contains(searchQuery, ignoreCase = true)
      matchesFilter && matchesSearch
    }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
  ) {
    // 🔍 Modern Instagram/Pinterest Style Search & Cadre Filters
    item {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search drafts, topics, niches…") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
          trailingIcon = {
            if (searchQuery.isNotBlank()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Close, contentDescription = "Clear")
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedBorderColor = MaterialTheme.colorScheme.primary
          ),
          modifier = Modifier.fillMaxWidth()
        )

        // 🏷️ Cadres / Pill Tab Row (Instagram / Pinterest Category Style)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf(
            "all" to "All Articles (${state.drafts.size})",
            "ready" to "Ready (${state.drafts.count { it.generationStatus == "ready" }})",
            "published" to "Published (${state.drafts.count { it.generationStatus == "published" }})"
          ).forEach { (key, label) ->
            val isSelected = selectedFilter == key
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
              border = BorderStroke(
                1.5.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
              ),
              modifier = Modifier
                .weight(1f)
                .clickable { selectedFilter = key }
            ) {
              Box(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  label,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                  fontSize = 11.5.sp,
                  maxLines = 1,
                  color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }
    }

    if (filteredDrafts.isEmpty()) {
      item {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = MaterialTheme.colorScheme.surface,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
        ) {
          Box(
            modifier = Modifier.padding(36.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Box(
                modifier = Modifier
                  .size(60.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Article, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
              }
              Text(
                if (searchQuery.isNotBlank()) "No articles match your search." else "No drafts found.",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
              Text(
                "Generate articles from Content Studio to see them here.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    } else {
      items(filteredDrafts, key = { it.id }) { draft ->
        DraftItemCard(
          draft = draft,
          onOpen = { viewModel.openDraft(draft.id) },
          onShowVersions = { versionsDraft = draft },
          onDelete = { draftToDelete = draft }
        )
      }
    }
  }

  // Delete Confirmation Dialog
  if (draftToDelete != null) {
    AlertDialog(
      onDismissRequest = { draftToDelete = null },
      title = { Text("Delete Draft", fontWeight = FontWeight.Bold) },
      text = { Text("Are you sure you want to delete \"${draftToDelete!!.title}\"? All attached local images will be permanently removed.") },
      confirmButton = {
        Button(
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
          shape = RoundedCornerShape(10.dp),
          onClick = {
            viewModel.deleteDraft(draftToDelete!!.id)
            draftToDelete = null
          }
        ) {
          Text("Delete")
        }
      },
      dismissButton = {
        TextButton(onClick = { draftToDelete = null }) {
          Text("Cancel")
        }
      }
    )
  }

  // Versions History Dialog
  if (versionsDraft != null) {
    val draftId = versionsDraft!!.id
    val versions = state.draftVersions.filter { it.draftId == draftId }
    AlertDialog(
      onDismissRequest = { versionsDraft = null },
      title = { Text("Revision History", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          if (versions.isEmpty()) {
            Text("No saved snapshots for this draft yet. Save revisions from the review screen.")
          } else {
            versions.forEach { ver ->
              val dateStr = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(Date(ver.createdAt))
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(12.dp).fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(dateStr, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                  FilledTonalButton(
                    onClick = {
                      viewModel.restoreDraftSnapshot(ver)
                      versionsDraft = null
                    },
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Text("Restore")
                  }
                }
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { versionsDraft = null }) {
          Text("Close")
        }
      }
    )
  }
}

@Composable
fun DraftItemCard(
  draft: DraftRecord,
  onOpen: () -> Unit,
  onShowVersions: () -> Unit,
  onDelete: () -> Unit
) {
  val dateStr = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(draft.createdAt))

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
        Text(
          draft.title,
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.weight(1f),
          maxLines = 2
        )
        Spacer(Modifier.width(10.dp))
        StatusBadge(status = draft.generationStatus)
      }

      Text(
        if (draft.metaDescription.isNotBlank()) draft.metaDescription else "Slug: ${draft.slug}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2
      )

      // Metadata Tag Badges
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
          Text(
            draft.nicheProfile.replaceFirstChar { it.uppercase() },
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        if (draft.images.isNotEmpty()) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondaryContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
          ) {
            Text(
              "${draft.images.size} Images Attached",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              color = MaterialTheme.colorScheme.secondary
            )
          }
        }
        Text("• $dateStr", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      if (draft.publishedUrl.isNotBlank()) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            "🔗 ${draft.publishedUrl}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            maxLines = 1
          )
        }
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

      // Action Footer
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          IconButton(onClick = onShowVersions) {
            Icon(Icons.Default.History, contentDescription = "History", tint = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          IconButton(onClick = onDelete) {
            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
          }
        }
        Button(
          onClick = onOpen,
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(Modifier.width(6.dp))
          Text(if (draft.generationStatus == "published") "View & Update" else "Review & Publish", fontWeight = FontWeight.Bold, maxLines = 1)
        }
      }
    }
  }
}
