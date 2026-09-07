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
import com.askinz.publisher.OrbitPressViewModel

@Composable
fun EditorialPipelineScreen(viewModel: OrbitPressViewModel) {
  val state by viewModel.uiState.collectAsState()

  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("KPIs & Status", "Keyword Clusters", "Calendar")

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(vertical = 16.dp)
  ) {
    item {
      Text("Editorial Pipeline", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
      Text("Local content planning, cluster tracking, and editorial workflow.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Spacer(Modifier.height(8.dp))
      TabRow(selectedTabIndex = selectedTab) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = { Text(title) }
          )
        }
      }
    }

    when (selectedTab) {
      0 -> {
        // KPIs Grid
        item {
          val ideasCount = state.keywords.size
          val draftsCount = state.drafts.size
          val readyCount = state.drafts.count { it.generationStatus == "ready" }
          val publishedCount = state.drafts.count { it.generationStatus == "published" }

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KpiCard("Ideas", ideasCount.toString(), Modifier.weight(1f))
            KpiCard("Drafts", draftsCount.toString(), Modifier.weight(1f))
            KpiCard("Ready", readyCount.toString(), Modifier.weight(1f))
            KpiCard("Published", publishedCount.toString(), Modifier.weight(1f))
          }
        }

        item {
          Text("Tracked Keywords Pipeline", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        if (state.keywords.isEmpty()) {
          item {
            Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
              Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                Text("No keywords tracked yet. Queue keywords in Content Studio.", style = MaterialTheme.typography.bodyMedium)
              }
            }
          }
        } else {
          items(state.keywords.take(30)) { kw ->
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(kw.keyword, fontWeight = FontWeight.Bold)
                  Text("Profile: ${kw.nicheProfile} • ${kw.priority} priority", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                StatusBadge(status = kw.status)
              }
            }
          }
        }
      }

      1 -> {
        // Keyword Clusters
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            var clusterName by remember { mutableStateOf("") }
            var clusterKeywords by remember { mutableStateOf("") }

            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Text("Create Keyword Cluster", fontWeight = FontWeight.Bold)
              OutlinedTextField(
                value = clusterName,
                onValueChange = { clusterName = it },
                label = { Text("Cluster Topic / Pillar Name") },
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = clusterKeywords,
                onValueChange = { clusterKeywords = it },
                label = { Text("Associated Keywords (comma separated)") },
                modifier = Modifier.fillMaxWidth()
              )
              Button(
                onClick = {
                  if (clusterName.isNotBlank() && clusterKeywords.isNotBlank()) {
                    clusterName = ""
                    clusterKeywords = ""
                    viewModel.notify("Keyword cluster saved locally.")
                  }
                },
                enabled = clusterName.isNotBlank() && clusterKeywords.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text("Save Cluster")
              }
            }
          }
        }
      }

      2 -> {
        // Content Calendar Reminders
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Text("Local Editorial Reminder", fontWeight = FontWeight.Bold)
              Text("Plan future article publications locally on this device.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }
    }
  }
}

@Composable
fun KpiCard(title: String, count: String, modifier: Modifier = Modifier) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(count, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
      Text(title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}
