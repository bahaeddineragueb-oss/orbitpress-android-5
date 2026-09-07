package com.askinz.publisher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
    // 🔍 Search Bar & Stats Header
    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search drafts by title, keyword, niche…") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          trailingIcon = {
            if (searchQuery.isNotBlank()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Close, contentDescription = "Clear")
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        )

        // Full-Width Segmented Filter Selector
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            .padding(4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          listOf(
            "all" to "All (${state.drafts.size})",
            "ready" to "Ready (${state.drafts.count { it.generationStatus == "ready" }})",
            "published" to "Published (${state.drafts.count { it.generationStatus == "published" }})"
          ).forEach { (key, label) ->
            val isSelected = selectedFilter == key
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
              modifier = Modifier
                .weight(1f)
                .clickable { selectedFilter = key }
            ) {
              Box(
                modifier = Modifier.padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  label,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 12.sp,
                  maxLines = 1,
                  color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    if (filteredDrafts.isEmpty()) {
      item {
        Box(
          modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Article, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.outline)
            Text(if (searchQuery.isNotBlank()) "No drafts matching search." else "No drafts found.", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
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
        Text(draft.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f), maxLines = 2)
        Spacer(Modifier.width(8.dp))
        StatusBadge(status = draft.generationStatus)
      }

      Text(
        if (draft.metaDescription.isNotBlank()) draft.metaDescription else "Slug: ${draft.slug}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ) {
          Text(
            draft.nicheProfile.replaceFirstChar { it.uppercase() },
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        if (draft.images.isNotEmpty()) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
          ) {
            Text(
              "${draft.images.size} Images Attached",
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              color = MaterialTheme.colorScheme.onSecondaryContainer
            )
          }
        }
        Text("• $dateStr", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.CenterVertically))
      }

      if (draft.publishedUrl.isNotBlank()) {
        Text("🔗 ${draft.publishedUrl}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, maxLines = 1)
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

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
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(Modifier.width(6.dp))
          Text(if (draft.generationStatus == "published") "View & Update" else "Review & Publish", maxLines = 1)
        }
      }
    }
  }
}
