package com.askinz.publisher.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.askinz.publisher.DraftRecord
import com.askinz.publisher.ImageManager
import com.askinz.publisher.OrbitPressViewModel
import com.askinz.publisher.ui.HtmlPreviewView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewPublisherScreen(
  viewModel: OrbitPressViewModel,
  onPickFeaturedImage: () -> Unit,
  onPickPinterestImage: () -> Unit,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  val state by viewModel.uiState.collectAsState()
  val draft = state.drafts.find { it.id == state.selectedDraftId }

  if (draft == null) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("No draft selected.", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onBack) { Text("Back to Drafts") }
      }
    }
    return
  }

  var title by remember(draft.id) { mutableStateOf(draft.title) }
  var slug by remember(draft.id) { mutableStateOf(draft.slug) }
  var focusKeyphrase by remember(draft.id) { mutableStateOf(draft.focusKeyphrase) }
  var metaDescription by remember(draft.id) { mutableStateOf(draft.metaDescription) }
  var pinTitle by remember(draft.id) { mutableStateOf(draft.pinterestTitle) }
  var pinDescription by remember(draft.id) { mutableStateOf(draft.pinterestDescription) }
  var pinAltText by remember(draft.id) { mutableStateOf(draft.pinterestAltText) }

  val auditResult = remember(draft, title, metaDescription, slug, focusKeyphrase) {
    viewModel.computeSeoAudit(
      draft.copy(
        title = title,
        slug = slug,
        metaDescription = metaDescription,
        focusKeyphrase = focusKeyphrase
      )
    )
  }

  // Load Bitmaps for previews
  val featuredBitmap = remember(draft.images["featured"]) {
    draft.images["featured"]?.let { ImageManager.loadBitmap(context, it, state.activeSiteId, 400) }
  }
  val pinterestBitmap = remember(draft.images["pinterest"]) {
    draft.images["pinterest"]?.let { ImageManager.loadBitmap(context, it, state.activeSiteId, 400) }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(vertical = 16.dp)
  ) {
    // Navigation / Header Row
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedButton(onClick = onBack) {
          Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(Modifier.width(4.dp))
          Text("Drafts")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          FilledTonalButton(onClick = { viewModel.saveDraftSnapshot(draft.id) }) {
            Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("Snapshot")
          }
          Button(
            onClick = {
              viewModel.updateDraft(
                draft.copy(
                  title = title,
                  slug = slug,
                  focusKeyphrase = focusKeyphrase,
                  metaDescription = metaDescription,
                  pinterestTitle = pinTitle,
                  pinterestDescription = pinDescription,
                  pinterestAltText = pinAltText
                )
              )
              viewModel.notify("Draft edits saved locally.")
            }
          ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("Save")
          }
        }
      }
    }

    // SEO & Pinterest Readiness Score Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("SEO & Pinterest Readiness", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
              Text("Guidance checks before publishing", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (auditResult.score >= 80) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer)
                .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text(
                "${auditResult.score}%",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = if (auditResult.score >= 80) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onTertiaryContainer
              )
            }
          }

          Divider(modifier = Modifier.padding(vertical = 4.dp))

          auditResult.checks.forEach { check ->
            Row(
              modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(
                  if (check.ok) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                  contentDescription = null,
                  tint = if (check.ok) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(check.label, style = MaterialTheme.typography.bodyMedium)
              }
              Text(check.detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }
    }

    // Images Section Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
          Text("Article Images", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

          // Featured Image Slot
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Featured Image", fontWeight = FontWeight.SemiBold)
              Text("WordPress hero image (16:9 or 4:3)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            OutlinedButton(onClick = onPickFeaturedImage) {
              Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(Modifier.width(6.dp))
              Text(if (draft.images.containsKey("featured")) "Replace" else "Select")
            }
          }
          if (featuredBitmap != null) {
            Image(
              bitmap = featuredBitmap.asImageBitmap(),
              contentDescription = "Featured Preview",
              modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(10.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
            )
          }

          Divider()

          // Pinterest Image Slot
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Pinterest Image (2:3 Vertical)", fontWeight = FontWeight.SemiBold)
              Text("Exact 2:3 ratio, e.g. 1000×1500 px", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            OutlinedButton(onClick = onPickPinterestImage) {
              Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(Modifier.width(6.dp))
              Text(if (draft.images.containsKey("pinterest")) "Replace" else "Select")
            }
          }
          if (pinterestBitmap != null) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
              Image(
                bitmap = pinterestBitmap.asImageBitmap(),
                contentDescription = "Pinterest Preview",
                modifier = Modifier.width(160.dp).height(240.dp).clip(RoundedCornerShape(10.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
              )
            }
          }
        }
      }
    }

    // Article Content & SEO Editor Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Article & SEO Metadata", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Article Title") },
            supportingText = { Text("${title.length} chars") },
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = slug,
            onValueChange = { slug = it },
            label = { Text("Canonical Slug") },
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = focusKeyphrase,
            onValueChange = { focusKeyphrase = it },
            label = { Text("Focus Keyphrase") },
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = metaDescription,
            onValueChange = { metaDescription = it },
            label = { Text("SEO Meta Description") },
            supportingText = { Text("${metaDescription.length}/160 chars") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4
          )
        }
      }
    }

    // Pinterest Metadata Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Pinterest Rich Metadata", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

          OutlinedTextField(
            value = pinTitle,
            onValueChange = { if (it.length <= 100) pinTitle = it },
            label = { Text("Pin Title") },
            supportingText = { Text("${pinTitle.length}/100 chars") },
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = pinDescription,
            onValueChange = { if (it.length <= 800) pinDescription = it },
            label = { Text("Pin Description") },
            supportingText = { Text("${pinDescription.length}/800 chars") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 5
          )

          OutlinedTextField(
            value = pinAltText,
            onValueChange = { if (it.length <= 320) pinAltText = it },
            label = { Text("Pin Image Alt Text") },
            supportingText = { Text("${pinAltText.length}/320 chars") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }

    // Sandboxed Read-Only Article HTML Preview Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Rendered Article Preview", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          Text("Sandboxed view of article, table of contents, and recipe cards", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
          ) {
            HtmlPreviewView(
              htmlContent = draft.htmlContent,
              modifier = Modifier.fillMaxWidth(),
              minHeight = 450.dp
            )
          }
        }
      }
    }

    // Publish Action Button Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("WordPress & Pinterest Publishing", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          Text("Manual publish uploads media, sets category & JSON-LD schema, and creates the post.", style = MaterialTheme.typography.bodySmall)

          Button(
            onClick = {
              viewModel.updateDraft(
                draft.copy(
                  title = title,
                  slug = slug,
                  focusKeyphrase = focusKeyphrase,
                  metaDescription = metaDescription,
                  pinterestTitle = pinTitle,
                  pinterestDescription = pinDescription,
                  pinterestAltText = pinAltText
                )
              )
              viewModel.publishCurrentDraft(draft.id)
            },
            enabled = !state.isLoading && draft.images.containsKey("featured") && draft.images.containsKey("pinterest"),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Publish, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(if (state.isLoading) "Publishing…" else if (draft.generationStatus == "published") "Republish to WordPress" else "Publish Article to WordPress")
          }

          if (!draft.images.containsKey("featured") || !draft.images.containsKey("pinterest")) {
            Text(
              "Attach both Featured and Pinterest images before publishing.",
              color = MaterialTheme.colorScheme.error,
              style = MaterialTheme.typography.bodySmall
            )
          }
        }
      }
    }
  }
}
