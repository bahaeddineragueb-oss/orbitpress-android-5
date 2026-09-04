from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/app/src/main/assets/index.html')
s=p.read_text()
# Add one save button to each settings card by heading.
buttons={
'Article API':'<button id="saveArticleSettings" class="button secondary" type="button">Save Article API</button>',
'Image Generator API':'<button id="saveImageSettings" class="button secondary" type="button">Save Image Generator</button>',
'Pinterest publishing':'',
'WordPress publishing':''
}
for heading,button in buttons.items():
    if not button: continue
    pos=s.find('<h2>'+heading+'</h2>')
    if pos<0: raise SystemExit('heading missing: '+heading)
    end=s.find('</section>',pos)
    if end<0: raise SystemExit('section end missing: '+heading)
    s=s[:end]+button+s[end:]
# Add a dedicated save button to Article Prompts card.
pos=s.find('<h2>Articles Prompts by Profile</h2>')
if pos<0: raise SystemExit('prompt heading missing')
end=s.find('</section>',pos)
s=s[:end]+'<button id="saveArticlePrompts" class="button secondary" type="button">Save Article Prompts</button>'+s[end:]
# Replace misleading autosave copy globally.
s=s.replace('Changes are saved locally as you type.','Changes are not saved until you press the Save button for that section.')
s=s.replace('Each profile has its own optional editorial prompt. Leave a field blank to keep the original OrbitPress prompt and SEO safeguards.','Each profile has its own optional prompt. Press Save Article Prompts to save only these prompt fields; the built-in SEO and safety safeguards remain active.')
# Insert subset save functions before saveAndTest.
marker='    async function saveAndTest()'
functions='''    function saveSettingsSubset(keys,message){const all=settingsForm(),payload={};keys.forEach(key=>{if(key==='profilePrompts')payload[key]=all[key];else if(Object.prototype.hasOwnProperty.call(all,key)&&String(all[key]||'').trim())payload[key]=all[key]});try{bridge.saveSettings(JSON.stringify(payload),activeSiteId());state.settingsSummary={...state.settingsSummary,...payload};renderSettings();notice(message,'good')}catch(error){notice(error.message||'Could not save this settings section.','bad')}}
    function saveArticleSettings(){saveSettingsSubset(['articleBaseUrl','articleModel','articleApiKey'],'Article API settings saved for this site.')}
    function saveImageSettings(){saveSettingsSubset(['imageMode','imageProvider','imageBaseUrl','imageModel','imageApiKey','cloudflareAccountId','cloudflareModel','cloudflareApiToken'],'Image Generator settings saved for this site.')}
    function saveArticlePrompts(){saveSettingsSubset(['profilePrompts'],'Article Prompts saved for this site.')}
'''
if marker not in s: raise SystemExit('saveAndTest marker missing')
s=s.replace(marker,functions+marker,1)
# Stop every input from triggering a combined save. Keep change notice only.
old="['articleBaseUrl','articleModel','articleApiKey','wordpressBaseUrl','wordpressUsername','wordpressAppPassword','imageMode','imageProvider','imageBaseUrl','imageModel','imageApiKey','pinterestPublishingMode','cloudflareAccountId','cloudflareModel','cloudflareApiToken','pinterestAccessToken','pinterestBoardId','promptFood','promptGardening','promptHomeDecor','promptCustom'].forEach(id=>$(id).addEventListener('input',scheduleSettingsAutoSave));$('defaultCategory').addEventListener('change',scheduleSettingsAutoSave);"
if old not in s: raise SystemExit('autosave listener block missing')
s=s.replace(old,"['articleBaseUrl','articleModel','articleApiKey','wordpressBaseUrl','wordpressUsername','wordpressAppPassword','imageMode','imageProvider','imageBaseUrl','imageModel','imageApiKey','pinterestPublishingMode','cloudflareAccountId','cloudflareModel','cloudflareApiToken','pinterestAccessToken','pinterestBoardId','promptFood','promptGardening','promptHomeDecor','promptCustom'].forEach(id=>$(id).addEventListener('input',()=>{const e=$('settingsAutoSave');if(e)e.textContent='Unsaved changes — press the Save button for this section.'}));$('defaultCategory').addEventListener('change',()=>{const e=$('settingsAutoSave');if(e)e.textContent='Unsaved changes — press the Save button for this section.'});"
)
# Wire buttons in initialize.
s=s.replace("$('savePlan').onclick=savePlan;", "$('savePlan').onclick=savePlan;$('saveArticleSettings').onclick=saveArticleSettings;$('saveImageSettings').onclick=saveImageSettings;$('saveArticlePrompts').onclick=saveArticlePrompts;")
p.write_text(s)
