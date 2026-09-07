package com.askinz.publisher

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.askinz.publisher.ui.PinLockDialog
import com.askinz.publisher.ui.screens.*
import com.askinz.publisher.ui.theme.OrbitPressTheme
import kotlinx.coroutines.launch
import java.io.InputStream

private const val PUBLISH_NOTIFICATION_CHANNEL_ID = "publish_results"

class MainActivity : ComponentActivity() {
  private val viewModel: OrbitPressViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    createNotificationChannel()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
      checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 7002)
    }

    setContent {
      val uiState by viewModel.uiState.collectAsState()
      OrbitPressTheme(darkTheme = uiState.isNightMode) {
        OrbitPressApp(viewModel = viewModel)
      }
    }
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        PUBLISH_NOTIFICATION_CHANNEL_ID,
        "Publish results",
        NotificationManager.IMPORTANCE_DEFAULT
      ).apply {
        description = "Results from publish actions started inside OrbitPress"
      }
      getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrbitPressApp(viewModel: OrbitPressViewModel) {
  val context = LocalContext.current
  val state by viewModel.uiState.collectAsState()
  val scope = rememberCoroutineScope()
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val snackbarHostState = remember { SnackbarHostState() }

  // Snackbars
  LaunchedEffect(state.snackbarMessage) {
    state.snackbarMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.dismissSnackbar()
    }
  }

  // Handle Android Back Navigation gracefully
  BackHandler(enabled = state.currentScreen != Screen.STUDIO || drawerState.isOpen) {
    if (drawerState.isOpen) {
      scope.launch { drawerState.close() }
    } else {
      viewModel.showScreen(Screen.STUDIO)
    }
  }

  // SAF Launchers for Images and Files
  val featuredPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
    if (uri != null && state.selectedDraftId != null) {
      val mime = context.contentResolver.getType(uri) ?: "image/jpeg"
      context.contentResolver.openInputStream(uri)?.use { stream ->
        viewModel.storeImageForDraft(state.selectedDraftId!!, "featured", stream, mime)
      }
    }
  }

  val pinterestPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
    if (uri != null && state.selectedDraftId != null) {
      val mime = context.contentResolver.getType(uri) ?: "image/jpeg"
      context.contentResolver.openInputStream(uri)?.use { stream ->
        viewModel.storeImageForDraft(state.selectedDraftId!!, "pinterest", stream, mime)
      }
    }
  }

  val keywordsFilePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
    if (uri != null) {
      context.contentResolver.openInputStream(uri)?.use { stream ->
        val text = stream.bufferedReader().readText()
        viewModel.importKeywords(text, ContentProfileContract.FOOD, 0, "")
      }
    }
  }

  val exportBackupLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.CreateDocument("application/json")
  ) { uri: Uri? ->
    if (uri != null) {
      try {
        val json = viewModel.exportBackup()
        context.contentResolver.openOutputStream(uri)?.use { out ->
          out.write(json.toByteArray(Charsets.UTF_8))
        }
        viewModel.notify("Backup exported successfully to device storage.")
      } catch (e: Exception) {
        viewModel.notify("Export failed: ${e.message}")
      }
    }
  }

  val importBackupLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.OpenDocument()
  ) { uri: Uri? ->
    if (uri != null) {
      try {
        context.contentResolver.openInputStream(uri)?.use { stream ->
          val json = stream.bufferedReader().readText()
          viewModel.importBackup(json)
        }
      } catch (e: Exception) {
        viewModel.notify("Import failed: ${e.message}")
      }
    }
  }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet {
        Spacer(Modifier.height(16.dp))
        Row(
          modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Publish, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
          Spacer(Modifier.width(10.dp))
          Column {
            Text("OrbitPress 5.0", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text("Native Android Content Studio", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
        Divider(modifier = Modifier.padding(vertical = 12.dp))

        NavigationDrawerItem(
          icon = { Icon(Icons.Default.Home, contentDescription = null) },
          label = { Text("Content Studio") },
          selected = state.currentScreen == Screen.STUDIO,
          onClick = { viewModel.showScreen(Screen.STUDIO); scope.launch { drawerState.close() } }
        )
        NavigationDrawerItem(
          icon = { Icon(Icons.Default.EditNote, contentDescription = null) },
          label = { Text("Article Prompts") },
          selected = state.currentScreen == Screen.PROMPTS,
          onClick = { viewModel.showScreen(Screen.PROMPTS); scope.launch { drawerState.close() } }
        )
        NavigationDrawerItem(
          icon = { Icon(Icons.Default.Article, contentDescription = null) },
          label = { Text("Drafts & Articles (${state.drafts.size})") },
          selected = state.currentScreen == Screen.DRAFTS,
          onClick = { viewModel.showScreen(Screen.DRAFTS); scope.launch { drawerState.close() } }
        )
        NavigationDrawerItem(
          icon = { Icon(Icons.Default.ViewTimeline, contentDescription = null) },
          label = { Text("Editorial Pipeline") },
          selected = state.currentScreen == Screen.PIPELINE,
          onClick = { viewModel.showScreen(Screen.PIPELINE); scope.launch { drawerState.close() } }
        )
        NavigationDrawerItem(
          icon = { Icon(Icons.Default.History, contentDescription = null) },
          label = { Text("Activity Log") },
          selected = state.currentScreen == Screen.ACTIVITY,
          onClick = { viewModel.showScreen(Screen.ACTIVITY); scope.launch { drawerState.close() } }
        )
        NavigationDrawerItem(
          icon = { Icon(Icons.Default.TrendingUp, contentDescription = null) },
          label = { Text("Pinterest Trends") },
          selected = state.currentScreen == Screen.TRENDS,
          onClick = { viewModel.showScreen(Screen.TRENDS); scope.launch { drawerState.close() } }
        )
        NavigationDrawerItem(
          icon = { Icon(Icons.Default.Build, contentDescription = null) },
          label = { Text("Template Repair") },
          selected = state.currentScreen == Screen.REPAIR,
          onClick = { viewModel.showScreen(Screen.REPAIR); scope.launch { drawerState.close() } }
        )

        Divider(modifier = Modifier.padding(vertical = 12.dp))

        NavigationDrawerItem(
          icon = { Icon(Icons.Default.Settings, contentDescription = null) },
          label = { Text("Settings") },
          selected = state.currentScreen == Screen.SETTINGS,
          onClick = { viewModel.showScreen(Screen.SETTINGS); scope.launch { drawerState.close() } }
        )
        NavigationDrawerItem(
          icon = { Icon(if (state.isNightMode) Icons.Default.LightMode else Icons.Default.DarkMode, contentDescription = null) },
          label = { Text(if (state.isNightMode) "Day Mode" else "Night Mode") },
          selected = false,
          onClick = { viewModel.toggleNightMode() }
        )
      }
    }
  ) {
    Scaffold(
      topBar = {
        TopAppBar(
          title = {
            Column {
              Text(
                when (state.currentScreen) {
                  Screen.STUDIO -> "Content Studio"
                  Screen.PROMPTS -> "Article Prompts"
                  Screen.DRAFTS -> "Drafts"
                  Screen.REVIEW -> "Review & Publish"
                  Screen.PIPELINE -> "Editorial Pipeline"
                  Screen.ACTIVITY -> "Activity Log"
                  Screen.TRENDS -> "Pinterest Trends"
                  Screen.REPAIR -> "Template Repair"
                  Screen.SETTINGS -> "Settings"
                },
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
              )
              val activeProfile = state.siteProfiles.find { it.id == state.activeSiteId }?.name ?: "Askinz"
              Text(activeProfile, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          },
          navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
              Icon(Icons.Default.Menu, contentDescription = "Open Menu")
            }
          },
          actions = {
            IconButton(onClick = { viewModel.syncCategories() }) {
              Icon(Icons.Default.Sync, contentDescription = "Sync WordPress")
            }
          },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
          )
        )
      },
      snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
      Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        when (state.currentScreen) {
          Screen.STUDIO -> ContentStudioScreen(
            viewModel = viewModel,
            onPickKeywordsFile = { keywordsFilePicker.launch("text/*") },
            onOpenSettings = { viewModel.showScreen(Screen.SETTINGS) }
          )
          Screen.PROMPTS -> ArticlePromptsScreen(viewModel = viewModel)
          Screen.DRAFTS -> DraftsScreen(viewModel = viewModel)
          Screen.REVIEW -> ReviewPublisherScreen(
            viewModel = viewModel,
            onPickFeaturedImage = { featuredPicker.launch("image/*") },
            onPickPinterestImage = { pinterestPicker.launch("image/*") },
            onBack = { viewModel.showScreen(Screen.DRAFTS) }
          )
          Screen.PIPELINE -> EditorialPipelineScreen(viewModel = viewModel)
          Screen.ACTIVITY -> ActivityLogScreen(viewModel = viewModel)
          Screen.TRENDS -> PinterestTrendsScreen(viewModel = viewModel)
          Screen.REPAIR -> TemplateRepairScreen(viewModel = viewModel)
          Screen.SETTINGS -> SettingsScreen(
            viewModel = viewModel,
            onExportBackup = { exportBackupLauncher.launch("orbitpress-backup.json") },
            onImportBackup = { importBackupLauncher.launch(arrayOf("application/json", "text/*")) }
          )
        }

        // Global Loading Overlay
        if (state.isLoading) {
          AlertDialog(
            onDismissRequest = {},
            title = { Text("Processing…", fontWeight = FontWeight.Bold) },
            text = {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(8.dp)
              ) {
                CircularProgressIndicator()
                Text(state.loadingMessage.ifBlank { "Please wait…" })
              }
            },
            confirmButton = {}
          )
        }

        // PIN Lock Dialog
        if (state.showPinDialog) {
          PinLockDialog(
            onUnlock = { pin -> viewModel.unlockSettings(pin) },
            onDismiss = { viewModel.dismissPinDialog() }
          )
        }
      }
    }
  }
}
