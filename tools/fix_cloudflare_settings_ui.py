from pathlib import Path
p=Path('/home/ubuntu/orbitpress_review/app/src/main/assets/index.html')
s=p.read_text()
s=s.replace("$('imageBaseUrl').value=s.imageBaseUrl||'';$('imageModel').value=s.imageModel||'';$('pinterestBoardId')", "$('imageBaseUrl').value=s.imageBaseUrl||'';$('imageModel').value=s.imageModel||'';$('cloudflareAccountId').value=s.cloudflareAccountId||'';$('cloudflareModel').value=s.cloudflareModel||'@cf/black-forest-labs/flux-1-schnell';$('cloudflareApiToken').value='';$('pinterestBoardId')")
p.write_text(s)
print('Cloudflare settings rendering fixed')
