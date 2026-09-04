from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/app/src/main/assets/index.html')
s=p.read_text()
css='''.review-metadata-grid{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:16px;margin:18px 0}.metadata-card{margin:0}.metadata-card label{display:block;margin-top:12px}.metadata-card input,.metadata-card textarea{margin-top:6px}.metadata-counts{display:flex;justify-content:space-between;color:var(--muted);font-size:12px;margin-top:6px}.metadata-preview{margin-top:16px;padding:12px;border:1px solid var(--line);border-radius:12px;background:var(--surface-2)}.metadata-preview h3{margin:7px 0;font-size:18px}.metadata-preview p{margin:0 0 8px}.metadata-preview small{color:var(--muted)}@media(max-width:800px){.review-metadata-grid{grid-template-columns:1fr}}'''
if css not in s:
    s=s.replace('</style>',css+'</style>',1)
p.write_text(s)
