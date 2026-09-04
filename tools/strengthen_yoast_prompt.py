from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/app/src/main/java/com/askinz/publisher/MainActivity.kt')
s=p.read_text()
s=s.replace('''    val category = request.optString("categoryName").trim()
    val existingTitles''','''    val category = request.optString("categoryName").trim()
    val siteBaseUrl = settings.optString("wordpressBaseUrl").trim().trimEnd('/')
    val existingTitles''')
s=s.replace('''Existing site titles to avoid duplicating: ${titleList.ifBlank { "None supplied" }}''','''Existing site titles to avoid duplicating: ${titleList.ifBlank { "None supplied" }}
      Approved site homepage for internal linking: ${siteBaseUrl.ifBlank { "No site URL supplied; do not invent URLs" }}''')
s=s.replace('''"internalLinks":[{"anchor":"","reason":""}]''','''"internalLinks":[{"anchor":"","url":"","reason":""}]''')
s=s.replace('''      - Infer practical search intent, create a distinct title, a concise meta description under 160 characters, and a lower-case canonical-friendly slug.
      - Provide 3 to 6 outline H2 sections. htmlContent starts with a concise benefit-led introduction, uses H2 sections, and provides useful substitutions, storage, or variations where appropriate.
      - Offer 2 to 4 internal-link anchor suggestions but never invent URLs.''','''      - Infer practical search intent and create a distinct article title. The SEO title must be 50-60 characters, never exceed 60 characters, and begin with the exact focus keyphrase.
      - The SEO meta description must be 120-160 characters, contain the exact focus keyphrase once, and communicate a clear benefit and reason to click.
      - Use the exact focus keyphrase naturally in the first paragraph, the SEO title, the SEO meta description, the slug, at least one H2 or H3, image alt text, and at least 3 total times in the body for a long-form article. Use synonyms elsewhere; never keyword-stuff.
      - Provide 3 to 6 outline H2 sections. htmlContent must start with a clear 2-3 sentence introduction that contains the exact focus keyphrase in its first sentence, then use descriptive H2/H3 headings. Do not put the title in an H1 because WordPress supplies it.
      - Include at least one genuine internal HTML link in htmlContent. Use only the approved site homepage URL supplied above when no article URL list is supplied; never invent a path or URL. Return the same link in internalLinks with its anchor, url, and reason.
      - Include at least one relevant image placeholder-free paragraph context where the WordPress featured image can be referenced; the app supplies the actual images and alt text.
      - Keep paragraphs short, use transition words, active voice, and answer the search intent immediately. Do not repeat the keyphrase unnaturally.''')
s=s.replace('''"internalLinks":{"type":"array","items":{"type":"object","properties":{"anchor":{"type":"string"},"reason":{"type":"string"}},"required":["anchor","reason"],"additionalProperties":false}}''','''"internalLinks":{"type":"array","items":{"type":"object","properties":{"anchor":{"type":"string"},"url":{"type":"string"},"reason":{"type":"string"}},"required":["anchor","url","reason"],"additionalProperties":false}}''')
p.write_text(s)
