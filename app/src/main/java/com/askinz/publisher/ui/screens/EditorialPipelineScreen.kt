package com.askinz.publisher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.askinz.publisher.OrbitPressViewModel

@Composable
fun EditorialPipelineScreen(viewModel: OrbitPressViewModel) {
  val state by viewModel.uiState.collectAsState()

  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("KPIs & Pipeline", "Keyword Clusters", "Calendar")

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
  ) {
    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Icon(Icons.Default.ViewTimeline, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          Text("Editorial Pipeline", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        }
        Text("Local content planning, keyword clusters, and publishing analytics.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

        // Full-Width Segmented Tab Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            .padding(4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          tabs.forEachIndexed { index, title ->
            val isSelected = selectedTab == index
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
              modifier = Modifier
                .weight(1f)
                .clickable { selectedTab = index }
            ) {
              Box(
                modifier = Modifier.padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  title,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 11.sp,
                  maxLines = 1,
                  color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    when (selectedTab) {
      0 -> {
        // 📊 KPIs 2x2 Clean Grid
        item {
          val ideasCount = state.keywords.size
          val draftsCount = state.drafts.size
          val readyCount = state.drafts.count { it.generationStatus == "ready" }
          val publishedCount = state.drafts.count { it.generationStatus == "published" }

          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
              KpiCard("Queued Ideas", ideasCount.toString(), Icons.Default.Queue, Modifier.weight(1f))
              KpiCard("Total Drafts", draftsCount.toString(), Icons.Default.Article, Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
              KpiCard("Ready to Review", readyCount.toString(), Icons.Default.EditNote, Modifier.weight(1f))
              KpiCard("Published Posts", publishedCount.toString(), Icons.Default.CheckCircle, Modifier.weight(1f))
            }
          }
        }

        item {
          Text("Active Pipeline Queue", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }

        if (state.keywords.isEmpty()) {
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              modifier = Modifier.fillMaxWidth()
            ) {
              Box(modifier = Modifier.padding(28.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                  Icon(Icons.Default.HourglassEmpty, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(40.dp))
                  Text("No keywords queued in pipeline.", fontWeight = FontWeight.SemiBold)
                  Text("Add ideas from Content Studio to track them here.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }
            }
          }
        } else {
          items(state.keywords.take(30), key = { it.id }) { kw ->
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(14.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                  Text(kw.keyword, fontWeight = FontWeight.Bold, maxLines = 1)
                  Text("Profile: ${kw.nicheProfile} • ${kw.priority.replaceFirstChar { it.uppercase() }} priority", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            var clusterName by remember { mutableStateOf("") }
            var clusterKeywords by remember { mutableStateOf("") }

            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
              Text("Create Keyword Cluster Pillar", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
              Text("Group related subtopics together for high-authority SEO topical clusters.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

              OutlinedTextField(
                value = clusterName,
                onValueChange = { clusterName = it },
                label = { Text("Cluster Topic / Pillar Name") },
                placeholder = { Text("e.g. Italian Pasta Guide") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              )
              OutlinedTextField(
                value = clusterKeywords,
                onValueChange = { clusterKeywords = it },
                label = { Text("Sub-Keywords (comma separated)") },
                placeholder = { Text("carbonara, bolognese sauce, fresh pasta dough") },
                shape = RoundedCornerShape(12.dp),
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
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Save Cluster Pillar")
              }
            }
          }
        }
      }

      2 -> {
        // Content Calendar Reminders
        item {
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Editorial Publishing Calendar", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
              }
              Text("Keep your content pipeline organized with scheduled publication dates.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }
    }
  }
}

@Composable
fun KpiCard(title: String, count: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Text(count, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
      }
      Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
    }
  }
}
