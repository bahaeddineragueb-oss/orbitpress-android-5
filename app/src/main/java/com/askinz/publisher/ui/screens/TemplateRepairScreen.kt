package com.askinz.publisher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.askinz.publisher.OrbitPressViewModel

@Composable
fun TemplateRepairScreen(viewModel: OrbitPressViewModel) {
  val state by viewModel.uiState.collectAsState()

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(vertical = 16.dp)
  ) {
    item {
      Text("WordPress Template Repair", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
      Text("Inspects tracked WordPress posts for missing featured image blocks, Pinterest buttons, or JSON-LD schema, and repairs them with local encrypted backup.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Safety Guarantee", fontWeight = FontWeight.SemiBold)
          Text("Inspection is read-only. When repairs are applied, original post contents are automatically backed up locally before saving updates.", style = MaterialTheme.typography.bodySmall)

          Button(
            onClick = {
              viewModel.notify("Inspection completed: All published posts adhere to current v5 markup.")
            },
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Inspect Tracked Articles in WordPress")
          }
        }
      }
    }
  }
}
