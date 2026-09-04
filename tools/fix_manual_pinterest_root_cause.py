from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/app/src/main/java/com/askinz/publisher/MainActivity.kt')
s=p.read_text()
s=s.replace('''          val shareDescription = listOf(pinTitle, pinDescription).filter { it.isNotBlank() }.joinToString(" — ")
          val share = "https://www.pinterest.com/pin/create/button/?url=" + java.net.URLEncoder.encode(canonical, "UTF-8") + "&media=" + java.net.URLEncoder.encode(pinterestUrl, "UTF-8") + "&description=" + java.net.URLEncoder.encode(shareDescription, "UTF-8") + "&title=" + java.net.URLEncoder.encode(pinTitle, "UTF-8")''','''          // Pinterest's legacy create URL has one reliable text field: description.
          // Never concatenate the title into it; title is supplied by WordPress OG metadata.
          val share = "https://www.pinterest.com/pin/create/button/?url=" + java.net.URLEncoder.encode(canonical, "UTF-8") + "&media=" + java.net.URLEncoder.encode(pinterestUrl, "UTF-8") + "&description=" + java.net.URLEncoder.encode(pinDescription, "UTF-8")''')
s=s.replace('''    val shareDescription = listOf(pinTitle, pinDescription).filter { it.isNotBlank() }.joinToString(" — ")
    val share = "https://www.pinterest.com/pin/create/button/?url=" + java.net.URLEncoder.encode("$root/$slug/", "UTF-8") + "&media=" + java.net.URLEncoder.encode(pinterestUrl, "UTF-8") + "&description=" + java.net.URLEncoder.encode(shareDescription, "UTF-8") + "&title=" + java.net.URLEncoder.encode(pinTitle, "UTF-8")''','''    // Pinterest's legacy create URL has one reliable text field: description.
    // Never concatenate the title into it; title is supplied by WordPress OG metadata.
    val share = "https://www.pinterest.com/pin/create/button/?url=" + java.net.URLEncoder.encode("$root/$slug/", "UTF-8") + "&media=" + java.net.URLEncoder.encode(pinterestUrl, "UTF-8") + "&description=" + java.net.URLEncoder.encode(pinDescription, "UTF-8")''')
# Return metadata diagnostics in manual mode; the save itself remains non-blocking.
s=s.replace('''JSONObject().put("published", false).put("manualReview", true).put("metadataSaved", metadataSaved).put("composerUrl", share)''','''JSONObject().put("published", false).put("manualReview", true).put("metadataSaved", metadataSaved).put("composerUrl", share)''')
p.write_text(s)
print('legacy Pinterest title-description concatenation removed')
