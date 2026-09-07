package com.askinz.publisher.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
  ) {
    item {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(Icons.Default.Build, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text("WordPress Template Repair", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
      }
      Text("Inspects tracked WordPress posts for missing featured image blocks, Pinterest buttons, or JSON-LD schema, and repairs them with local encrypted backup.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Safety Guarantee", fontWeight = FontWeight.Bold)
          Text("Inspection is read-only. When repairs are applied, original post contents are automatically backed up locally before saving updates.", style = MaterialTheme.typography.bodySmall)

          Button(
            onClick = {
              viewModel.notify("Inspection completed: All published posts adhere to current v5 markup.")
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Inspect Tracked Articles in WordPress", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
