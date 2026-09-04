from pathlib import Path

p=Path('/home/ubuntu/orbitpress5/app/src/main/java/com/askinz/publisher/SettingsPersistenceContract.kt')
s=p.read_text().replace('''"cloudflareAccountId", "cloudflareModel", "pinterestBoardId"''','''"cloudflareAccountId", "cloudflareModel", "pinterestBoardId", "pinterestPublishingMode"''')
p.write_text(s)

p=Path('/home/ubuntu/orbitpress5/app/src/main/java/com/askinz/publisher/MainActivity.kt')
s=p.read_text()
s=s.replace('''.put("pinterestBoardId", saved.optString("pinterestBoardId"))''','''.put("pinterestBoardId", saved.optString("pinterestBoardId"))
      .put("pinterestPublishingMode", saved.optString("pinterestPublishingMode", "manual_review"))''')
old='''    val pinterestResult = if (pinterestToken.isNotBlank() && pinterestBoardId.isNotBlank()) {
      try {'''
new='''    val pinterestMode = settings.optString("pinterestPublishingMode", "manual_review").trim().ifBlank { "manual_review" }
    val pinterestResult = if (pinterestMode == "disabled") {
      JSONObject().put("published", false).put("skipped", true)
    } else if (pinterestMode == "manual_review") {
      JSONObject().put("published", false).put("manualReview", true).put("composerUrl", share)
    } else if (pinterestToken.isNotBlank() && pinterestBoardId.isNotBlank()) {
      try {'''
if old not in s: raise SystemExit('pinterest branch marker missing')
s=s.replace(old,new)
p.write_text(s)

p=Path('/home/ubuntu/orbitpress5/app/src/main/assets/index.html')
s=p.read_text()
s=s.replace('''pinterestBoardId:$('pinterestBoardId')?.value.trim()||'',profilePrompts:''','''pinterestBoardId:$('pinterestBoardId')?.value.trim()||'',pinterestPublishingMode:$('pinterestPublishingMode')?.value||'manual_review',profilePrompts:''')
s=s.replace('''<label for="pinterestBoardId">Default Pinterest Board ID</label><input id="pinterestBoardId"''','''<label for="pinterestPublishingMode">Pinterest publishing mode</label><select id="pinterestPublishingMode"><option value="manual_review">Manual review in Pinterest (recommended)</option><option value="direct_api">Publish directly via Pinterest API</option><option value="disabled">WordPress only — do not create a Pin</option></select><p class="helper">Manual review publishes WordPress first, then opens Pinterest Composer so you can review the title, description, image, and board before pressing Publish.</p><label for="pinterestBoardId">Default Pinterest Board ID</label><input id="pinterestBoardId"''')
s=s.replace("$('imageMode').value=s.imageMode||'manual';$('imageProvider')", "$('imageMode').value=s.imageMode||'manual';$('pinterestPublishingMode').value=s.pinterestPublishingMode||'manual_review';$('imageProvider')")
s=s.replace("draft.pinterestStatus=result.pinterest?.published?'published':result.pinterest?.skipped?'not_configured':'failed';", "draft.pinterestStatus=result.pinterest?.manualReview?'manual_review':result.pinterest?.published?'published':result.pinterest?.skipped?'not_configured':'failed';if(result.pinterest?.manualReview&&result.pinterest?.composerUrl){notice('WordPress published. Opening Pinterest for your manual review…','good');setTimeout(()=>{window.location.href=result.pinterest.composerUrl},350)}")
s=s.replace("result.pinterest?.published?'Article published to WordPress and Pinterest.':result.pinterest?.skipped?'Article published to WordPress. Pinterest is optional and not configured.':'Article published to WordPress, but Pinterest needs attention.", "result.pinterest?.manualReview?'Article published to WordPress. Review and publish the Pin in Pinterest.':result.pinterest?.published?'Article published to WordPress and Pinterest.':result.pinterest?.skipped?'Article published to WordPress. Pinterest is disabled or not configured.':'Article published to WordPress, but Pinterest needs attention.")
s=s.replace("'imageApiKey','cloudflareAccountId'", "'imageApiKey','pinterestPublishingMode','cloudflareAccountId'")
p.write_text(s)
print('manual Pinterest review mode added')
