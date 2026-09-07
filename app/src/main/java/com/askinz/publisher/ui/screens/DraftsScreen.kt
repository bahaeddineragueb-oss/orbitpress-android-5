package com.askinz.publisher.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
  var draftToDelete by remember { mutableStateOf<DraftRecord?>(null) }
  var versionsDraft by remember { mutableStateOf<DraftRecord?>(null) }

  val filteredDrafts = remember(state.drafts, selectedFilter) {
    when (selectedFilter) {
      "ready" -> state.drafts.filter { it.generationStatus == "ready" }
      "published" -> state.drafts.filter { it.generationStatus == "published" }
      else -> state.drafts
    }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(vertical = 16.dp)
  ) {
    // Header & Filter Chips
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Drafts & Articles (${filteredDrafts.size})", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
      }
      Spacer(Modifier.height(8.dp))
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
          selected = selectedFilter == "all",
          onClick = { selectedFilter = "all" },
          label = { Text("All (${state.drafts.size})") }
        )
        FilterChip(
          selected = selectedFilter == "ready",
          onClick = { selectedFilter = "ready" },
          label = { Text("Ready (${state.drafts.count { it.generationStatus == "ready" }})") }
        )
        FilterChip(
          selected = selectedFilter == "published",
          onClick = { selectedFilter = "published" },
          label = { Text("Published (${state.drafts.count { it.generationStatus == "published" }})") }
        )
      }
    }

    if (filteredDrafts.isEmpty()) {
      item {
        Box(
          modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.outline)
            Spacer(Modifier.height(8.dp))
            Text("No drafts found.", fontWeight = FontWeight.SemiBold)
            Text("Generate keywords from Content Studio to see full drafts here.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(dateStr, style = MaterialTheme.typography.bodyMedium)
                FilledTonalButton(
                  onClick = {
                    viewModel.restoreDraftSnapshot(ver)
                    versionsDraft = null
                  }
                ) {
                  Text("Restore")
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
        Text(draft.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        StatusBadge(status = draft.generationStatus)
      }

      Text("Slug: ${draft.slug}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Profile: ${draft.nicheProfile}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Created: $dateStr", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      if (draft.publishedUrl.isNotBlank()) {
        Text("URL: ${draft.publishedUrl}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row {
          IconButton(onClick = onShowVersions) {
            Icon(Icons.Default.History, contentDescription = "History")
          }
          IconButton(onClick = onDelete) {
            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
          }
        }
        Button(onClick = onOpen) {
          Icon(Icons.Default.Article, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(Modifier.width(6.dp))
          Text(if (draft.generationStatus == "published") "View Record" else "Review & Publish")
        }
      }
    }
  }
}
