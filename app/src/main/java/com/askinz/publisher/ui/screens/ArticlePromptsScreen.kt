package com.askinz.publisher.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.askinz.publisher.ContentProfileContract
import com.askinz.publisher.OrbitPressViewModel

@Composable
fun ArticlePromptsScreen(viewModel: OrbitPressViewModel) {
  val state by viewModel.uiState.collectAsState()
  val currentPrompts = state.currentSettings.profilePrompts

  var foodPrompt by remember(state.activeSiteId) { mutableStateOf(currentPrompts[ContentProfileContract.FOOD] ?: "") }
  var gardeningPrompt by remember(state.activeSiteId) { mutableStateOf(currentPrompts[ContentProfileContract.GARDENING] ?: "") }
  var homeDecorPrompt by remember(state.activeSiteId) { mutableStateOf(currentPrompts[ContentProfileContract.HOME_DECOR] ?: "") }
  var customPrompt by remember(state.activeSiteId) { mutableStateOf(currentPrompts[ContentProfileContract.CUSTOM] ?: "") }

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
  ) {
    item {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(Icons.Default.EditNote, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text("Article Prompts by Niche", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
      }
      Text("Optional custom editorial instructions. Built-in SEO and safety rules always remain active.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Food & Recipes Prompt", fontWeight = FontWeight.Bold)
          OutlinedTextField(
            value = foodPrompt,
            onValueChange = { foodPrompt = it },
            placeholder = { Text("e.g. Focus on budget-friendly 30-minute meals with common pantry ingredients.") },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 6
          )

          Text("Gardening Prompt", fontWeight = FontWeight.Bold)
          OutlinedTextField(
            value = gardeningPrompt,
            onValueChange = { gardeningPrompt = it },
            placeholder = { Text("e.g. Provide USDA hardiness zone advice and organic pest prevention tips.") },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 6
          )

          Text("Home Decor Prompt", fontWeight = FontWeight.Bold)
          OutlinedTextField(
            value = homeDecorPrompt,
            onValueChange = { homeDecorPrompt = it },
            placeholder = { Text("e.g. Focus on modern minimalist and cozy scandinavian aesthetic.") },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 6
          )

          Text("Custom Niche Prompt", fontWeight = FontWeight.Bold)
          OutlinedTextField(
            value = customPrompt,
            onValueChange = { customPrompt = it },
            placeholder = { Text("Custom guidelines for general or specialized niche topics.") },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 6
          )

          Button(
            onClick = {
              val newMap = mapOf(
                ContentProfileContract.FOOD to foodPrompt,
                ContentProfileContract.GARDENING to gardeningPrompt,
                ContentProfileContract.HOME_DECOR to homeDecorPrompt,
                ContentProfileContract.CUSTOM to customPrompt
              )
              viewModel.saveSettings(state.currentSettings.copy(profilePrompts = newMap))
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Save Editorial Prompts", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
