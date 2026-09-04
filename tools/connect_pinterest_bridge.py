from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/app/src/main/java/com/askinz/publisher/MainActivity.kt')
s=p.read_text()
marker='''  private fun notifyPublishResult(request: JSONObject, result: JSONObject) {'''
helper='''  private fun savePinterestMetadata(root: String, settings: JSONObject, postId: Int, title: String, description: String, altText: String, imageUrl: String) {
    val body = JSONObject().put("post_id", postId).put("title", title.take(100)).put("description", description.take(800)).put("alt_text", altText.take(320)).put("image", imageUrl)
    http("$root/wp-json/orbitpress/v1/pinterest-meta", "POST", wordpressHeaders(settings) + mapOf("Content-Type" to "application/json"), body.toString().toByteArray())
  }

'''
if marker not in s: raise SystemExit('notify marker missing')
s=s.replace(marker,helper+marker,1)
old='''    val pinterestMode = settings.optString("pinterestPublishingMode", "manual_review").trim().ifBlank { "manual_review" }
    val pinterestResult = if (pinterestMode == "disabled") {'''
new='''    val pinterestMode = settings.optString("pinterestPublishingMode", "manual_review").trim().ifBlank { "manual_review" }
    val metadataSaved = try { savePinterestMetadata(root, settings, published.optInt("id"), pinTitle, pinDescription, pinAltText, pinterestUrl); true } catch (_: Exception) { false }
    val pinterestResult = if (pinterestMode == "disabled") {'''
if old not in s: raise SystemExit('mode marker missing')
s=s.replace(old,new,1)
s=s.replace('''JSONObject().put("published", false).put("manualReview", true).put("composerUrl", share)''','''JSONObject().put("published", false).put("manualReview", true).put("metadataSaved", metadataSaved).put("composerUrl", share)''',1)
p.write_text(s)
