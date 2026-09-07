package com.askinz.publisher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.askinz.publisher.ContentProfileContract
import com.askinz.publisher.OrbitPressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinterestTrendsScreen(viewModel: OrbitPressViewModel) {
  val state by viewModel.uiState.collectAsState()

  var region by remember { mutableStateOf("US") }
  var trendType by remember { mutableStateOf("growing") }
  var nicheProfile by remember { mutableStateOf(ContentProfileContract.FOOD) }

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(vertical = 16.dp)
  ) {
    item {
      Text("Pinterest Trends Explorer", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
      Text("Discover trending search queries from Pinterest API and queue them directly.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
              value = region,
              onValueChange = { region = it.uppercase() },
              label = { Text("Region") },
              modifier = Modifier.weight(1f)
            )

            var typeExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
              expanded = typeExpanded,
              onExpandedChange = { typeExpanded = it },
              modifier = Modifier.weight(1f)
            ) {
              OutlinedTextField(
                value = trendType.replaceFirstChar { it.uppercase() },
                onValueChange = {},
                readOnly = true,
                label = { Text("Trend Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
              )
              ExposedDropdownMenu(
                expanded = typeExpanded,
                onDismissRequest = { typeExpanded = false }
              ) {
                listOf("growing", "monthly", "yearly", "seasonal").forEach { type ->
                  DropdownMenuItem(
                    text = { Text(type.replaceFirstChar { it.uppercase() }) },
                    onClick = { trendType = type; typeExpanded = false }
                  )
                }
              }
            }
          }

          Button(
            onClick = { viewModel.loadTrends(region, trendType, 15, nicheProfile) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.TrendingUp, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(if (state.isLoading) "Loading Trends…" else "Explore Pinterest Trends")
          }
        }
      }
    }

    if (state.trends.isEmpty()) {
      item {
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), contentAlignment = Alignment.Center) {
          Text("No trends loaded. Press Explore to fetch latest trends.", style = MaterialTheme.typography.bodyMedium)
        }
      }
    } else {
      items(state.trends) { trend ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(trend.keyword, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
              if (trend.pctGrowthYearOverYear != 0.0) {
                Text("YoY Growth: +${trend.pctGrowthYearOverYear.toInt()}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
              }
            }
            FilledTonalButton(
              onClick = {
                viewModel.addKeyword(
                  keyword = trend.keyword,
                  contentType = "article",
                  nicheProfile = nicheProfile,
                  priority = "normal",
                  categoryId = 0,
                  categoryName = "",
                  pinterestBoardId = ""
                )
              }
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(Modifier.width(4.dp))
              Text("Queue")
            }
          }
        }
      }
    }
  }
}
