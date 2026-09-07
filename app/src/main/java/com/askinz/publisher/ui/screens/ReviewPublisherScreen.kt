package com.askinz.publisher.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
      Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(52.dp), tint = MaterialTheme.colorScheme.outline)
        Text("No draft selected.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Button(onClick = onBack, shape = RoundedCornerShape(12.dp)) {
          Text("Back to Drafts")
        }
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
    draft.images["featured"]?.let { ImageManager.loadBitmap(context, it, state.activeSiteId, 500) }
  }
  val pinterestBitmap = remember(draft.images["pinterest"]) {
    draft.images["pinterest"]?.let { ImageManager.loadBitmap(context, it, state.activeSiteId, 500) }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
  ) {
    // Top Action Bar
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedButton(
          onClick = onBack,
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline)
        ) {
          Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(Modifier.width(4.dp))
          Text("Drafts", maxLines = 1)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          FilledTonalButton(
            onClick = { viewModel.saveDraftSnapshot(draft.id) },
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("Snapshot", maxLines = 1)
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
            },
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("Save", maxLines = 1)
          }
        }
      }
    }

    // 🏆 SEO & Content Readiness Audit Card (Cadre Style, 100% Zero-Wrapping Fix)
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Text("SEO & Publishing Audit", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
              }
              Text("Real-time content readiness score", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (auditResult.score >= 80) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
              border = BorderStroke(1.dp, if (auditResult.score >= 80) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary),
              modifier = Modifier.padding(start = 8.dp)
            ) {
              Text(
                "${auditResult.score}%",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                color = if (auditResult.score >= 80) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
              )
            }
          }

          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

          // Audit Items in Individual Cadres (Guaranteed No Squishing)
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            auditResult.checks.forEach { check ->
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (check.ok) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
                border = BorderStroke(
                  1.dp,
                  if (check.ok) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
                ),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                  ) {
                    Icon(
                      if (check.ok) Icons.Default.CheckCircle else Icons.Default.Cancel,
                      contentDescription = null,
                      tint = if (check.ok) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                      modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                      check.label,
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 13.sp,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }

                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (check.ok) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.padding(start = 8.dp)
                  ) {
                    Text(
                      check.detail,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = if (check.ok) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // 🔍 Google SERP Snippet Preview Card (Cadre Style)
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Text("Google SERP Preview", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          }

          Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                "https://${state.currentSettings.wordpressBaseUrl.removePrefix("https://").removePrefix("http://").ifBlank { "example.com" }}/${slug.ifBlank { "article-slug" }}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                title.ifBlank { "Untitled Article" },
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A0DAB),
                maxLines = 2
              )
              Text(
                metaDescription.ifBlank { "No meta description provided yet. Add a concise SEO description." },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        }
      }
    }

    // 🖼️ Article Imagery Section (Cadre Style)
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text("Article Imagery", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          }

          // Featured Image Slot
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Featured Image", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
              Text("WordPress hero banner", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            OutlinedButton(
              onClick = onPickFeaturedImage,
              shape = RoundedCornerShape(10.dp),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
              Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(Modifier.width(6.dp))
              Text(if (draft.images.containsKey("featured")) "Replace" else "Select", maxLines = 1)
            }
          }
          if (featuredBitmap != null) {
            Image(
              bitmap = featuredBitmap.asImageBitmap(),
              contentDescription = "Featured Preview",
              modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            )
          }

          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

          // Pinterest Image Slot
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Pinterest Image (2:3 Vertical)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
              Text("1000×1500px recommended", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            OutlinedButton(
              onClick = onPickPinterestImage,
              shape = RoundedCornerShape(10.dp),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
              Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(Modifier.width(6.dp))
              Text(if (draft.images.containsKey("pinterest")) "Replace" else "Select", maxLines = 1)
            }
          }
          if (pinterestBitmap != null) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
              Image(
                bitmap = pinterestBitmap.asImageBitmap(),
                contentDescription = "Pinterest Preview",
                modifier = Modifier
                  .width(160.dp)
                  .height(240.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .border(1.2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
              )
            }
          }
        }
      }
    }

    // ✍️ Article & SEO Metadata (Cadre Style)
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.EditNote, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text("Article & SEO Metadata", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          }

          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Article Title") },
            supportingText = { Text("${title.length} characters") },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = slug,
            onValueChange = { slug = it },
            label = { Text("Canonical Slug") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = focusKeyphrase,
            onValueChange = { focusKeyphrase = it },
            label = { Text("Focus Keyphrase") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = metaDescription,
            onValueChange = { metaDescription = it },
            label = { Text("SEO Meta Description") },
            supportingText = { Text("${metaDescription.length}/160 characters") },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4
          )
        }
      }
    }

    // 📌 Pinterest Metadata (Cadre Style)
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Bookmark, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text("Pinterest Rich Metadata", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          }

          OutlinedTextField(
            value = pinTitle,
            onValueChange = { if (it.length <= 100) pinTitle = it },
            label = { Text("Pin Title") },
            supportingText = { Text("${pinTitle.length}/100 chars") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = pinDescription,
            onValueChange = { if (it.length <= 800) pinDescription = it },
            label = { Text("Pin Description") },
            supportingText = { Text("${pinDescription.length}/800 chars") },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4
          )

          OutlinedTextField(
            value = pinAltText,
            onValueChange = { if (it.length <= 320) pinAltText = it },
            label = { Text("Pin Image Alt Text") },
            supportingText = { Text("${pinAltText.length}/320 chars") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }

    // 🌐 Sandboxed HTML Preview (Cadre Style)
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Visibility, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text("Rendered Article Preview", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          }
          Text("Sandboxed view of article, headings, ingredients and recipe cards", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .border(1.2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
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

    // 🚀 Publish Action Button
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("WordPress & Pinterest Publishing", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
          Text("Publishing automatically sets categories, formats JSON-LD schema, and uploads high-res media.", style = MaterialTheme.typography.bodySmall)

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
            shape = RoundedCornerShape(14.dp)
          ) {
            Icon(Icons.Default.Publish, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
              if (state.isLoading) "Publishing…" else if (draft.generationStatus == "published") "Republish to WordPress" else "Publish Article to WordPress",
              fontWeight = FontWeight.Bold,
              maxLines = 1
            )
          }

          if (!draft.images.containsKey("featured") || !draft.images.containsKey("pinterest")) {
            Text(
              "⚠️ Attach both Featured and Pinterest images before publishing.",
              color = MaterialTheme.colorScheme.error,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
