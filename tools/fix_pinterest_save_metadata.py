from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/app/src/main/java/com/askinz/publisher/WordPressMarkup.kt')
s=p.read_text()
s=s.replace('''  fun pinterestSaveButton(shareUrl: String): String =
    "<p data-askinz-pinterest-direct=\\"true\\"><a href=\\"${escape(shareUrl)}\\" target=\\"_blank\\" rel=\\"noopener\\">Save on Pinterest</a></p>"''','''  fun pinterestSaveButton(shareUrl: String, title: String, description: String, mediaUrl: String, altText: String): String =
    "<p data-askinz-pinterest-direct=\\"true\\"><a href=\\"${escape(shareUrl)}\\" data-pin-do=\\"buttonPin\\" data-pin-media=\\"${escape(mediaUrl)}\\" data-pin-description=\\"${escape(description)}\\" data-pin-title=\\"${escape(title)}\\" aria-label=\\"Save ${escape(title)} to Pinterest\\" target=\\"_blank\\" rel=\\"noopener\\">Save on Pinterest</a></p>"''')
p.write_text(s)

p=Path('/home/ubuntu/orbitpress5/app/src/main/java/com/askinz/publisher/MainActivity.kt')
s=p.read_text()
old='''          val canonical = "$root/${DraftContract.cleanSlug(draft.optString("slug"))}/"
          val share = "https://www.pinterest.com/pin/create/button/?url=" + java.net.URLEncoder.encode(canonical, "UTF-8") + "&media=" + java.net.URLEncoder.encode(pinterestUrl, "UTF-8") + "&description=" + java.net.URLEncoder.encode(draft.optString("pinterestTitle", draft.optString("title")), "UTF-8")
          content += WordPressMarkup.pinterestSaveButton(share)'''
new='''          val canonical = "$root/${DraftContract.cleanSlug(draft.optString("slug"))}/"
          val pinTitle = draft.optString("pinterestTitle", draft.optString("title")).trim().take(100)
          val pinDescription = draft.optString("pinterestDescription", draft.optString("metaDescription")).trim().take(800)
          val pinAltText = pinterestImageAltText(draft)
          val shareDescription = listOf(pinTitle, pinDescription).filter { it.isNotBlank() }.joinToString(" — ")
          val share = "https://www.pinterest.com/pin/create/button/?url=" + java.net.URLEncoder.encode(canonical, "UTF-8") + "&media=" + java.net.URLEncoder.encode(pinterestUrl, "UTF-8") + "&description=" + java.net.URLEncoder.encode(shareDescription, "UTF-8") + "&title=" + java.net.URLEncoder.encode(pinTitle, "UTF-8")
          content += WordPressMarkup.pinterestSaveButton(share, pinTitle, pinDescription, pinterestUrl, pinAltText)'''
if old not in s: raise SystemExit('repair block not found')
s=s.replace(old,new)
old='''    val pinTitle = draft.optString("pinterestTitle", draft.optString("title")).trim()
    val share = "https://www.pinterest.com/pin/create/button/?url=" + java.net.URLEncoder.encode("$root/$slug/", "UTF-8") + "&media=" + java.net.URLEncoder.encode(pinterestUrl, "UTF-8") + "&description=" + java.net.URLEncoder.encode(pinTitle, "UTF-8")
    val featuredBlock = WordPressMarkup.featuredImage(featuredUrl, featuredAltText)
    val pinBlock = WordPressMarkup.pinterestSaveButton(share)'''
new='''    val pinTitle = draft.optString("pinterestTitle", draft.optString("title")).trim().take(100)
    val pinDescription = draft.optString("pinterestDescription", draft.optString("metaDescription")).trim().take(800)
    val pinAltText = pinterestImageAltText(draft)
    val shareDescription = listOf(pinTitle, pinDescription).filter { it.isNotBlank() }.joinToString(" — ")
    val share = "https://www.pinterest.com/pin/create/button/?url=" + java.net.URLEncoder.encode("$root/$slug/", "UTF-8") + "&media=" + java.net.URLEncoder.encode(pinterestUrl, "UTF-8") + "&description=" + java.net.URLEncoder.encode(shareDescription, "UTF-8") + "&title=" + java.net.URLEncoder.encode(pinTitle, "UTF-8")
    val featuredBlock = WordPressMarkup.featuredImage(featuredUrl, featuredAltText)
    val pinBlock = WordPressMarkup.pinterestSaveButton(share, pinTitle, pinDescription, pinterestUrl, pinAltText)'''
if old not in s: raise SystemExit('publish block not found')
s=s.replace(old,new)
p.write_text(s)
