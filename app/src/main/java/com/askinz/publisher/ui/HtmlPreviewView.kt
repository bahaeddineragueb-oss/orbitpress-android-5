package com.askinz.publisher.ui

import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun HtmlPreviewView(
  htmlContent: String,
  modifier: Modifier = Modifier,
  minHeight: Dp = 400.dp
) {
  val styledHtml = """
    <!DOCTYPE html>
    <html>
    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <style>
        body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Georgia, serif; line-height: 1.75; color: #2B2F38; padding: 12px; margin: 0; background: #FFFDF9; font-size: 15px; }
        h1, h2, h3 { color: #172033; font-family: Georgia, serif; }
        h2 { margin-top: 1.5rem; border-bottom: 1px solid #EAE6DF; padding-bottom: 0.3rem; }
        .askinz-recipe-card { border: 1px solid #EADFCD; border-radius: 14px; background: #FFFAF2; padding: 16px; margin: 20px 0; }
        .askinz-table-of-contents { background: #F1F7EC; border: 1px solid #D8E5CC; border-radius: 12px; padding: 12px; margin: 15px 0; }
        .askinz-recipe-actions a { display: inline-block; padding: 6px 12px; background: #315D37; color: #FFF; text-decoration: none; border-radius: 20px; font-weight: bold; margin-right: 8px; font-size: 13px; }
        img { max-width: 100%; height: auto; border-radius: 8px; }
        ul, ol { padding-left: 20px; }
        li { margin-bottom: 6px; }
      </style>
    </head>
    <body>
      $htmlContent
    </body>
    </html>
  """.trimIndent()

  AndroidView(
    modifier = modifier.fillMaxWidth().height(minHeight),
    factory = { context ->
      WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )
        settings.apply {
          javaScriptEnabled = false // Sandboxed read-only preview!
          allowFileAccess = false
          allowContentAccess = false
          domStorageEnabled = false
          cacheMode = WebSettings.LOAD_NO_CACHE
        }
        webViewClient = WebViewClient()
      }
    },
    update = { webView ->
      webView.loadDataWithBaseURL(null, styledHtml, "text/html", "UTF-8", null)
    }
  )
}
