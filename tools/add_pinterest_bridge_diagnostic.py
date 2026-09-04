from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/app/src/main/assets/index.html')
s=p.read_text()
old="""if(result.pinterest?.manualReview&&result.pinterest?.composerUrl){notice('WordPress published. Opening Pinterest for your manual review…','good');setTimeout(()=>{window.location.href=result.pinterest.composerUrl},350)}"""
new="""if(result.pinterest?.manualReview&&result.pinterest?.composerUrl){if(result.pinterest.metadataSaved){notice('WordPress published and Pinterest metadata saved. Opening Pinterest for manual review…','good')}else{notice('WordPress published, but OrbitPress Pinterest Bridge was not detected. Install/activate the plugin, then use the WordPress post URL to review.','bad')}setTimeout(()=>{window.location.href=result.pinterest.composerUrl},700)}"""
if old not in s: raise SystemExit('diagnostic marker not found')
p.write_text(s.replace(old,new))
