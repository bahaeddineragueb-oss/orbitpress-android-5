from pathlib import Path

html_path = Path('/home/ubuntu/orbitpress_review/app/src/main/assets/index.html')
html = html_path.read_text()

css_marker = '</style>'
css = '''
    .seo-audit{margin-top:13px;padding:14px;border:1px solid #c6d6f5;border-radius:14px;background:#f7fbff}.seo-audit-head{display:flex;align-items:center;justify-content:space-between;gap:10px}.seo-audit-score{display:grid;place-items:center;min-width:58px;height:58px;border-radius:50%;background:#e4eaff;color:#3153bd;font:800 16px Georgia,serif}.seo-audit-list{display:grid;gap:6px;margin:12px 0 0;padding:0;list-style:none}.seo-audit-item{display:flex;gap:7px;align-items:flex-start;color:#38506e;font-size:11px;line-height:1.45}.seo-audit-item b{color:#168064}.seo-audit-item.warn b{color:#ad7620}.seo-audit-note{margin:10px 0 0;color:#63708a;font-size:10px;line-height:1.45}@media(max-width:390px){.seo-audit-head{align-items:flex-start}.seo-audit-score{min-width:52px;height:52px}}
'''
if '.seo-audit{' not in html:
    html = html.replace(css_marker, css + css_marker, 1)

old_preflight = "function preflight(draft,keyword,images){const errors=[],warnings=[];if(!draft.slug)errors.push('Missing slug');if(!draft.htmlContent)errors.push('Missing article HTML');if(!keyword?.categoryId)errors.push('A WordPress category is required');if(!images.featured)errors.push('Featured image is required');if(!images.pinterest)errors.push('Pinterest image is required');if(draft.metaDescription&&draft.metaDescription.length>160)warnings.push('Meta description is longer than 160 characters');const recipeSets=draft.recipes?.length?draft.recipes:(draft.contentType==='recipe'?[draft.recipe]:[]);if(recipeSets.some(item=>!item?.ingredients?.length||!item?.instructions?.length))warnings.push('One or more recipe ingredient or instruction sets are incomplete');return {errors,warnings}}"
new_preflight = old_preflight + "    function seoAudit(draft,keyword,images){const checks=[];const add=(ok,label,detail)=>checks.push({ok,label,detail});const title=(draft.title||'').trim();const meta=(draft.metaDescription||'').trim();const slug=(draft.slug||'').trim();const headings=(draft.htmlContent||'').match(/<h2\\b/gi)||[];add(title.length>=30&&title.length<=65,'Title length',title?`${title.length} characters`:'Missing title');add(meta.length>0&&meta.length<=160,'Meta description',meta?`${meta.length}/160 characters`:'Missing meta description');add(/^[a-z0-9]+(?:-[a-z0-9]+)*$/.test(slug),'Canonical slug',slug||'Missing or invalid slug');add(headings.length>=3&&headings.length<=8,'H2 structure',`${headings.length} H2 sections`);add((draft.internalLinks||[]).length>=2,'Internal-link suggestions',`${(draft.internalLinks||[]).length} anchor suggestions`);add(!!images.featured,'Featured image',images.featured?'Ready':'Not selected');add(!!images.pinterest,'Pinterest image',images.pinterest?'Ready and locally validated':'Not selected');const recipeSets=draft.recipes?.length?draft.recipes:(draft.contentType==='recipe'?[draft.recipe]:[]);add(!recipeSets.length||recipeSets.every(item=>item?.ingredients?.length&&item?.instructions?.length),'Recipe completeness',recipeSets.length?`${recipeSets.length} complete set(s) checked`:'Not applicable');add((draft.schema&&draft.schema['@type'])==='Article'||(draft.schema&&draft.schema['@type'])==='Recipe','Structured data',draft.schema&&draft.schema['@type']?`${draft.schema['@type']} JSON-LD ready`:'Missing JSON-LD');const score=Math.round(checks.reduce((sum,item)=>sum+(item.ok?1:0),0)*100/checks.length);return {checks,score}}"
if old_preflight not in html:
    raise SystemExit('preflight function marker not found')
html = html.replace(old_preflight, new_preflight, 1)

old_section = "${!published?`<section id=\"preflight\" class=\"notice\"></section>`:''}${published?"
new_section = "${!published?`<section id=\"preflight\" class=\"notice\"></section><section id=\"seoAudit\" class=\"seo-audit\"></section>`:''}${published?"
if old_section not in html:
    raise SystemExit('review section marker not found')
html = html.replace(old_section, new_section, 1)

old_render = "const checks=preflight(draft,keyword,images),preflightBox=$('preflight');if(preflightBox)preflightBox.innerHTML=`<strong>${checks.errors.length?'Action needed':'Ready to review'}</strong>${checks.errors.length?`<div>${checks.errors.map(escapeHtml).join(' · ')}</div>`:'All required article, category, and image checks passed.'}${checks.warnings.length?`<div class=\"helper\">Warnings: ${checks.warnings.map(escapeHtml).join(' · ')}</div>`:''}`;const preview=$('articlePreview');"
new_render = "const checks=preflight(draft,keyword,images),preflightBox=$('preflight');if(preflightBox)preflightBox.innerHTML=`<strong>${checks.errors.length?'Action needed':'Ready to review'}</strong>${checks.errors.length?`<div>${checks.errors.map(escapeHtml).join(' · ')}</div>`:'All required article, category, and image checks passed.'}${checks.warnings.length?`<div class=\"helper\">Warnings: ${checks.warnings.map(escapeHtml).join(' · ')}</div>`:''}`;const audit=seoAudit(draft,keyword,images),auditBox=$('seoAudit');if(auditBox){auditBox.innerHTML=`<div class=\"seo-audit-head\"><div><h3>SEO & Pinterest readiness</h3><p class=\"helper\">Display-only guidance. A low score never blocks or changes publishing.</p></div><span class=\"seo-audit-score\">${audit.score}/100</span></div><ul class=\"seo-audit-list\">${audit.checks.map(item=>`<li class=\"seo-audit-item ${item.ok?'':'warn'}\"><b>${item.ok?'✓':'△'}</b><span><strong>${escapeHtml(item.label)}</strong> — ${escapeHtml(item.detail)}</span></li>`).join('')}</ul><p class=\"seo-audit-note\">Pinterest Rich Pins use Open Graph and recognized Schema.org data on the WordPress page; this app does not invent a non-standard “Pinterest schema”. The existing Article/Recipe JSON-LD includes the published images.</p>`}const preview=$('articlePreview');"
if old_render not in html:
    raise SystemExit('render marker not found')
html = html.replace(old_render, new_render, 1)
html_path.write_text(html)
print('SEO display-only audit added')
