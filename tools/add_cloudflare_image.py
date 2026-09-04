from pathlib import Path

main = Path('/home/ubuntu/orbitpress_review/app/src/main/java/com/askinz/publisher/MainActivity.kt')
s = main.read_text()
s = s.replace('''      .put("imageMode", saved.optString("imageMode", "manual"))
      .put("imageBaseUrl", saved.optString("imageBaseUrl"))
      .put("imageModel", saved.optString("imageModel"))''','''      .put("imageMode", saved.optString("imageMode", "manual"))
      .put("imageProvider", saved.optString("imageProvider", "openai"))
      .put("imageBaseUrl", saved.optString("imageBaseUrl"))
      .put("imageModel", saved.optString("imageModel"))
      .put("cloudflareAccountId", saved.optString("cloudflareAccountId"))
      .put("cloudflareModel", saved.optString("cloudflareModel", "@cf/black-forest-labs/flux-1-schnell"))''')
s = s.replace('''      .put("imageConfigured", saved.optString("imageApiKey").isNotBlank())''','''      .put("imageConfigured", (saved.optString("imageProvider", "openai") == "cloudflare" && saved.optString("cloudflareAccountId").isNotBlank() && saved.optString("cloudflareApiToken").isNotBlank() && saved.optString("cloudflareModel").isNotBlank()) || saved.optString("imageApiKey").isNotBlank())''')
old = '''  private fun generateImage(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val baseUrl = settings.optString("imageBaseUrl").trim().trimEnd('/')
    val model = settings.optString("imageModel").trim()
    val apiKey = settings.optString("imageApiKey").trim()
    require(baseUrl.isNotBlank() && model.isNotBlank() && apiKey.isNotBlank()) { "Complete the optional Image Generator API settings first." }
    val prompt = request.getString("prompt").trim().take(4000)
    require(prompt.isNotBlank()) { "An image prompt is required." }
    val body = JSONObject().put("model", model).put("prompt", prompt).put("n", 1).put("size", "1024x1536").put("response_format", "b64_json")
    val response = JSONObject(http("$baseUrl/images/generations", "POST", mapOf("Authorization" to "Bearer $apiKey", "Content-Type" to "application/json"), body.toString().toByteArray()))
    val image = response.optJSONArray("data")?.optJSONObject(0) ?: throw IllegalStateException("The Image Generator API returned no image.")
    val base64 = image.optString("b64_json").trim()
    require(base64.isNotBlank()) { "The Image Generator API returned no Base64 image." }
    return JSONObject().put("ok", true).put("dataUrl", "data:image/png;base64,$base64")
  }'''
new = '''  private fun generateImage(request: JSONObject): JSONObject {
    val settings = requireStoredSettings(request)
    val prompt = request.getString("prompt").trim().take(2048)
    require(prompt.isNotBlank()) { "An image prompt is required." }
    if (settings.optString("imageProvider", "openai") == "cloudflare") {
      val accountId = settings.optString("cloudflareAccountId").trim()
      val model = settings.optString("cloudflareModel", "@cf/black-forest-labs/flux-1-schnell").trim()
      val token = settings.optString("cloudflareApiToken").trim()
      require(accountId.isNotBlank() && model.isNotBlank() && token.isNotBlank()) { "Complete the Cloudflare Account ID, model, and API token first." }
      require(Regex("^[A-Za-z0-9_-]{10,80}$").matches(accountId)) { "Cloudflare Account ID is invalid." }
      require(model.startsWith("@cf/")) { "Cloudflare model must start with @cf/." }
      val body = JSONObject().put("prompt", prompt).put("steps", 4)
      val url = "https://api.cloudflare.com/client/v4/accounts/$accountId/ai/run/" + java.net.URLEncoder.encode(model, "UTF-8").replace("+", "%20")
      val response = JSONObject(http(url, "POST", mapOf("Authorization" to "Bearer $token", "Content-Type" to "application/json"), body.toString().toByteArray()))
      val result = response.optJSONObject("result") ?: throw IllegalStateException("Cloudflare Workers AI returned no result.")
      val base64 = result.optString("image").trim()
      require(base64.isNotBlank()) { "Cloudflare Workers AI returned no Base64 image." }
      return JSONObject().put("ok", true).put("dataUrl", "data:image/jpeg;base64,$base64")
    }
    val baseUrl = settings.optString("imageBaseUrl").trim().trimEnd('/')
    val model = settings.optString("imageModel").trim()
    val apiKey = settings.optString("imageApiKey").trim()
    require(baseUrl.isNotBlank() && model.isNotBlank() && apiKey.isNotBlank()) { "Complete the optional Image Generator API settings first." }
    val body = JSONObject().put("model", model).put("prompt", prompt).put("n", 1).put("size", "1024x1536").put("response_format", "b64_json")
    val response = JSONObject(http("$baseUrl/images/generations", "POST", mapOf("Authorization" to "Bearer $apiKey", "Content-Type" to "application/json"), body.toString().toByteArray()))
    val image = response.optJSONArray("data")?.optJSONObject(0) ?: throw IllegalStateException("The Image Generator API returned no image.")
    val base64 = image.optString("b64_json").trim()
    require(base64.isNotBlank()) { "The Image Generator API returned no Base64 image." }
    return JSONObject().put("ok", true).put("dataUrl", "data:image/png;base64,$base64")
  }'''
if old not in s: raise SystemExit('generateImage marker not found')
main.write_text(s.replace(old,new))

sp = Path('/home/ubuntu/orbitpress_review/app/src/main/java/com/askinz/publisher/SettingsPersistenceContract.kt')
s = sp.read_text()
s = s.replace('''"articleBaseUrl", "articleModel", "wordpressBaseUrl", "wordpressUsername", "categoryId", "imageMode", "imageBaseUrl", "imageModel", "pinterestBoardId"''','''"articleBaseUrl", "articleModel", "wordpressBaseUrl", "wordpressUsername", "categoryId", "imageMode", "imageProvider", "imageBaseUrl", "imageModel", "cloudflareAccountId", "cloudflareModel", "pinterestBoardId"''')
s = s.replace('''"articleApiKey", "wordpressAppPassword", "imageApiKey", "pinterestAccessToken"''','''"articleApiKey", "wordpressAppPassword", "imageApiKey", "cloudflareApiToken", "pinterestAccessToken"''')
sp.write_text(s)

html = Path('/home/ubuntu/orbitpress_review/app/src/main/assets/index.html')
s = html.read_text()
s = s.replace('''<label for="imageMode">Image mode</label><select id="imageMode"><option value="manual">Manual images</option><option value="automatic">Automatic images</option></select><label for="imageBaseUrl">Image API URL</label>''','''<label for="imageMode">Image mode</label><select id="imageMode"><option value="manual">Manual images</option><option value="automatic">Automatic images</option></select><label for="imageProvider">Automatic image provider</label><select id="imageProvider"><option value="openai">OpenAI-compatible Images API</option><option value="cloudflare">Cloudflare Workers AI</option></select><label for="imageBaseUrl">Image API URL</label>''')
s = s.replace('''<label for="imageApiKey">Image API key</label><input id="imageApiKey" type="password" autocomplete="off" placeholder="Paste a new key, or leave blank to keep it"></section>''','''<label for="imageApiKey">Image API key</label><input id="imageApiKey" type="password" autocomplete="off" placeholder="Paste a new key, or leave blank to keep it"><label for="cloudflareAccountId">Cloudflare Account ID</label><input id="cloudflareAccountId" placeholder="Required for Cloudflare Workers AI"><label for="cloudflareModel">Cloudflare model</label><input id="cloudflareModel" placeholder="@cf/black-forest-labs/flux-1-schnell"><label for="cloudflareApiToken">Cloudflare API token</label><input id="cloudflareApiToken" type="password" autocomplete="off" placeholder="Paste a new token, or leave blank to keep it"><p class="helper">Cloudflare Workers AI returns a Base64 image. The token is stored encrypted and used only when Automatic images is enabled.</p></section>''')
s = s.replace('''imageMode:$('imageMode')?.value||'manual',imageBaseUrl:''','''imageMode:$('imageMode')?.value||'manual',imageProvider:$('imageProvider')?.value||'openai',imageBaseUrl:''')
s = s.replace('''imageApiKey:$('imageApiKey')?.value.trim()||'',pinterestAccessToken:''','''imageApiKey:$('imageApiKey')?.value.trim()||'',cloudflareAccountId:$('cloudflareAccountId')?.value.trim()||'',cloudflareModel:$('cloudflareModel')?.value.trim()||'',cloudflareApiToken:$('cloudflareApiToken')?.value.trim()||'',pinterestAccessToken:''')
s = s.replace('''$('imageMode').value=s.imageMode||'manual';$('imageBaseUrl')''','''$('imageMode').value=s.imageMode||'manual';$('imageProvider').value=s.imageProvider||'openai';$('imageBaseUrl')''')
s = s.replace('''$('imageApiKey').value='';$('pinterestAccessToken').value='' ''','''$('imageApiKey').value='';$('cloudflareApiToken').value='';$('pinterestAccessToken').value='' ''')
s = s.replace("'imageMode','imageBaseUrl','imageModel','imageApiKey','pinterestAccessToken'", "'imageMode','imageProvider','imageBaseUrl','imageModel','imageApiKey','cloudflareAccountId','cloudflareModel','cloudflareApiToken','pinterestAccessToken'")
html.write_text(s)
print('Cloudflare Workers AI integration added')
