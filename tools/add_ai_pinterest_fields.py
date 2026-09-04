from pathlib import Path

p=Path('/home/ubuntu/orbitpress5/app/src/main/java/com/askinz/publisher/MainActivity.kt')
s=p.read_text()
s=s.replace('''"pinterest":{"title":"","altText":""}}''','''"pinterest":{"title":"","description":"","altText":""}}''')
s=s.replace('''      - Create only a concise natural Pinterest SEO title and image alt text. Do not create a Pinterest description or hashtags.''','''      - Create a concise natural Pinterest SEO title, a standalone Pinterest description, and descriptive image alt text. Keep title, description, and alt text as separate fields. Do not join them with a dash. Do not create hashtags.''')
s=s.replace('''val pinDescription = draft.optString("pinterestDescription", draft.optString("metaDescription")).trim().take(800)''','''val pinDescription = draft.optString("pinterestDescription", draft.optString("metaDescription")).trim().take(800)''')
s=s.replace('''private fun pinterestImageAltText(draft: JSONObject): String = PublishingContracts.pinterestImageAltText(draft.optString("pinterestTitle"), draft.optString("title"))''','''private fun pinterestImageAltText(draft: JSONObject): String = draft.optString("pinterestAltText").trim().take(320).ifBlank { PublishingContracts.pinterestImageAltText(draft.optString("pinterestTitle"), draft.optString("title")) }''')
p.write_text(s)

p=Path('/home/ubuntu/orbitpress5/app/src/main/java/com/askinz/publisher/DraftContract.kt')
s=p.read_text()
s=s.replace('''    val pinterestTitle = pinterestSource?.optString("title")?.trim()?.take(100).orEmpty().ifBlank { title.take(100) }
    val pinterestAltText = pinterestSource?.optString("altText")?.trim()?.take(320).orEmpty().ifBlank { title.take(320) }''','''    val pinterestTitle = pinterestSource?.optString("title")?.trim()?.take(100).orEmpty().ifBlank { title.take(100) }
    val pinterestDescription = pinterestSource?.optString("description")?.trim()?.take(800).orEmpty().ifBlank { raw.optString("metaDescription").trim().take(800) }
    val pinterestAltText = pinterestSource?.optString("altText")?.trim()?.take(320).orEmpty().ifBlank { title.take(320) }''')
s=s.replace('''.put("pinterest", JSONObject().put("title", pinterestTitle).put("altText", pinterestAltText))
      .put("pinterestTitle", pinterestTitle)
      .put("pinterestAltText", pinterestAltText)''','''.put("pinterest", JSONObject().put("title", pinterestTitle).put("description", pinterestDescription).put("altText", pinterestAltText))
      .put("pinterestTitle", pinterestTitle)
      .put("pinterestDescription", pinterestDescription)
      .put("pinterestAltText", pinterestAltText)''')
p.write_text(s)

p=Path('/home/ubuntu/orbitpress5/app/src/main/assets/index.html')
s=p.read_text()
s=s.replace('''pinterestTitle:draft.pinterestTitle,images:draft.images''','''pinterestTitle:draft.pinterestTitle,pinterestDescription:draft.pinterestDescription,pinterestAltText:draft.pinterestAltText,images:draft.images''')
p.write_text(s)
print('AI Pinterest title, description, and alt fields enabled')
