package com.askinz.publisher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.askinz.publisher.OrbitPressViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ActivityLogScreen(viewModel: OrbitPressViewModel) {
  val state by viewModel.uiState.collectAsState()

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp),
    contentPadding = PaddingValues(vertical = 16.dp)
  ) {
    item {
      Text("Activity & Event Log (${state.logs.size})", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
      Text("Complete record of generations, image checks, and publishing events.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    if (state.logs.isEmpty()) {
      item {
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp), contentAlignment = Alignment.Center) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.ListAlt, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.outline)
            Spacer(Modifier.height(8.dp))
            Text("No activity recorded yet.")
          }
        }
      }
    } else {
      items(state.logs, key = { it.id }) { log ->
        val dateStr = SimpleDateFormat("MMM d, yyyy HH:mm:ss", Locale.getDefault()).format(Date(log.createdAt))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(log.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
              Text(log.action, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
            Text(dateStr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            if (log.publishedUrl.isNotBlank()) {
              Text("Published URL: ${log.publishedUrl}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            if (log.errorDetails.isNotBlank()) {
              Text("Error: ${log.errorDetails}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            }
          }
        }
      }
    }
  }
}
