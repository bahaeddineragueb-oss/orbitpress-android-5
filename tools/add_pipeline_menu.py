from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/app/src/main/assets/index.html')
s=p.read_text()
marker='<button class="menu-item" data-screen="drafts" type="button">'
item='<button class="menu-item" data-screen="pipeline" type="button"><span class="menu-icon">◈</span>Editorial Pipeline</button>'
if 'data-screen="pipeline"' not in s.split('<script>')[0]:
    if marker not in s: raise SystemExit('menu marker not found')
    s=s.replace(marker,item+marker,1)
p.write_text(s)
print('Pipeline menu item added')
