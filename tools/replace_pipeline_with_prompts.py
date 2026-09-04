from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/app/src/main/assets/index.html')
s=p.read_text()
# Remove the full pipeline screen.
start=s.find('<section class="screen" id="screen-pipeline">')
end=s.find('<section class="screen" id="screen-settings">', start)
if start < 0 or end < 0: raise SystemExit('pipeline screen markers not found')
pipeline=s[start:end]
s=s[:start]+s[end:]
# Extract the existing profile-prompt card from Settings.
marker='<section class="card"><div class="connection-line"><h2>Articles Prompts by Profile</h2>'
start=s.find(marker)
if start < 0: raise SystemExit('prompt settings card not found')
end=s.find('</section>', start)+len('</section>')
prompt_card=s[start:end]
s=s[:start]+s[end:]
# Make the card a dedicated Article Prompts screen with explanatory heading.
prompt_screen='''<section class="screen" id="screen-prompts"><p class="eyebrow">Article generation</p><h1 class="screen-title">Article Prompts</h1><p class="screen-subtitle">Optional profile instructions for article generation. The built-in SEO and safety rules always remain active.</p>'''+prompt_card+'''</section>'''
settings_pos=s.find('<section class="screen" id="screen-settings">')
s=s[:settings_pos]+prompt_screen+s[settings_pos:]
# Replace menu item.
old='<button class="menu-item" data-screen="pipeline" type="button"><span class="menu-icon">◈</span>Editorial Pipeline</button>'
new='<button class="menu-item" data-screen="prompts" type="button"><span class="menu-icon">✎</span>Article Prompts</button>'
if old not in s: raise SystemExit('pipeline menu item not found')
s=s.replace(old,new,1)
# Remove event bindings for deleted pipeline controls from initialize.
s=s.replace(";$('createBrief').onclick=createContentBrief;$('briefKeyword').onchange=renderBriefPreview;$('saveCluster').onclick=saveKeywordCluster;$('addCalendarItem').onclick=addPipelineCalendarItem",'')
# Update visible docs wording where it exists in this asset.
s=s.replace('Editorial Pipeline','Article Prompts')
p.write_text(s)

for f in [Path('/home/ubuntu/orbitpress5/README.md'), *Path('/home/ubuntu/orbitpress5/docs').glob('*.md')]:
    if f.exists():
        t=f.read_text()
        t=t.replace('Editorial Pipeline','Article Prompts').replace('Editorial pipeline','Article Prompts')
        f.write_text(t)
