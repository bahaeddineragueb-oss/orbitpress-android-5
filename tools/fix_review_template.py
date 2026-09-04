from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/app/src/main/assets/index.html')
s=p.read_text()
old='''</section><section id="preflight" class="notice"></section>"`<section id="seoAudit" class="seo-audit"></section>`:''}'''
new='''</section><section id="preflight" class="notice"></section><section id="seoAudit" class="seo-audit"></section>`:''}'''
if old not in s: raise SystemExit('broken template fragment not found')
p.write_text(s.replace(old,new,1))
