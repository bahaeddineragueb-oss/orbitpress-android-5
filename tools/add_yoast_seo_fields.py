from pathlib import Path

p=Path('/home/ubuntu/orbitpress5/app/src/main/java/com/askinz/publisher/MainActivity.kt')
s=p.read_text()
s=s.replace('''      - Create a concise natural Pinterest SEO title, a standalone Pinterest description, and descriptive image alt text. Keep title, description, and alt text as separate fields. Do not join them with a dash. Do not create hashtags.''','''      - Create a concise natural Pinterest SEO title, a standalone Pinterest description, and descriptive image alt text. Keep title, description, and alt text as separate fields. Do not join them with a dash. Do not create hashtags.
      - Create an SEO object with focusKeyphrase, title, and metaDescription. The focus keyphrase must be a natural 2-5 word phrase from the keyword. The SEO title must begin with the focus keyphrase and be under 60 characters. The SEO metaDescription must contain the focus keyphrase and be 120-160 characters. Use the focus keyphrase naturally in the article introduction and at least one H2, without keyword stuffing.''')
s=s.replace('''"pinterest":{"title":"","description":"","altText":""}}''','''"pinterest":{"title":"","description":"","altText":""},"seo":{"focusKeyphrase":"","title":"","metaDescription":""}}''')
s=s.replace('''          "pinterest":{"type":"object","properties":{"title":{"type":"string"},"description":{"type":"string"},"altText":{"type":"string"}},"required":["title","description","altText"],"additionalProperties":false}''','''          "pinterest":{"type":"object","properties":{"title":{"type":"string"},"description":{"type":"string"},"altText":{"type":"string"}},"required":["title","description","altText"],"additionalProperties":false},
          "seo":{"type":"object","properties":{"focusKeyphrase":{"type":"string"},"title":{"type":"string"},"metaDescription":{"type":"string"}},"required":["focusKeyphrase","title","metaDescription"],"additionalProperties":false}''')
s=s.replace('''"recipes","pinterest"]''','''"recipes","pinterest","seo"]''')
old='''val body = JSONObject().put("post_id", postId).put("title", title.take(100)).put("description", description.take(800)).put("alt_text", altText.take(320)).put("image", imageUrl)'''
new='''val body = JSONObject().put("post_id", postId).put("title", title.take(100)).put("description", description.take(800)).put("alt_text", altText.take(320)).put("image", imageUrl)
    val seo = argumentsSeo
    body.put("focus_keyphrase", seo.optString("focusKeyphrase").take(80)).put("seo_title", seo.optString("title").take(160)).put("seo_description", seo.optString("metaDescription").take(160))'''
# avoid complex helper signature: use a local field set in call by adding optional parameter.
old2='''private fun savePinterestMetadata(root: String, settings: JSONObject, postId: Int, title: String, description: String, altText: String, imageUrl: String) {
    val body = JSONObject().put("post_id", postId).put("title", title.take(100)).put("description", description.take(800)).put("alt_text", altText.take(320)).put("image", imageUrl)
    http'''
new2='''private fun savePinterestMetadata(root: String, settings: JSONObject, postId: Int, title: String, description: String, altText: String, imageUrl: String, seo: JSONObject = JSONObject()) {
    val body = JSONObject().put("post_id", postId).put("title", title.take(100)).put("description", description.take(800)).put("alt_text", altText.take(320)).put("image", imageUrl)
    body.put("focus_keyphrase", seo.optString("focusKeyphrase").take(80)).put("seo_title", seo.optString("title").take(160)).put("seo_description", seo.optString("metaDescription").take(160))
    http'''
if old2 not in s: raise SystemExit('helper block missing')
s=s.replace(old2,new2)
s=s.replace('''pinAltText, pinterestUrl); true''','''pinAltText, pinterestUrl, draft.optJSONObject("seo") ?: JSONObject()); true''')
p.write_text(s)

p=Path('/home/ubuntu/orbitpress5/app/src/main/java/com/askinz/publisher/DraftContract.kt')
s=p.read_text()
s=s.replace('''    val pinterestSource = raw.optJSONObject("pinterest")''','''    val seoSource = raw.optJSONObject("seo")
    val focusKeyphrase = seoSource?.optString("focusKeyphrase")?.trim()?.take(80).orEmpty().ifBlank { title.split(" ").take(4).joinToString(" ").lowercase() }
    val seoTitle = seoSource?.optString("title")?.trim()?.take(160).orEmpty().ifBlank { title.take(60) }
    val seoDescription = seoSource?.optString("metaDescription")?.trim()?.take(160).orEmpty().ifBlank { raw.optString("metaDescription").trim().take(160) }
    val pinterestSource = raw.optJSONObject("pinterest")''')
s=s.replace('''      .put("metaDescription", raw.optString("metaDescription").trim().take(160))''','''      .put("metaDescription", seoDescription.ifBlank { raw.optString("metaDescription").trim().take(160) })
      .put("seo", JSONObject().put("focusKeyphrase", focusKeyphrase).put("title", seoTitle).put("metaDescription", seoDescription))''')
p.write_text(s)

p=Path('/home/ubuntu/orbitpress5/wp-plugin/orbitpress-pinterest-bridge/orbitpress-pinterest-bridge.php')
s=p.read_text()
s=s.replace("''_orbitpress_pinterest_image' => esc_url_raw($request->get_param('image')),", "''_orbitpress_pinterest_image' => esc_url_raw($request->get_param('image')),\n        '_yoast_wpseo_focuskw' => sanitize_text_field($request->get_param('focus_keyphrase')),\n        '_yoast_wpseo_title' => sanitize_text_field($request->get_param('seo_title')),\n        '_yoast_wpseo_metadesc' => sanitize_textarea_field($request->get_param('seo_description'))," )
s=s.replace("'alt_text' => ['required' => true], 'image' => ['required' => true],", "'alt_text' => ['required' => true], 'image' => ['required' => true],\n      'focus_keyphrase' => ['required' => false], 'seo_title' => ['required' => false], 'seo_description' => ['required' => false],")
p.write_text(s)
