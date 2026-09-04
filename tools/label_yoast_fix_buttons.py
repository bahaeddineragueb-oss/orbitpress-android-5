from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/wp-plugin/orbitpress-pinterest-bridge/orbitpress-pinterest-bridge.php')
s=p.read_text()
s=s.replace('>Fix Pinterest metadata</button>', '>Fix Pinterest + Yoast SEO</button>')
s=s.replace('Repairs missing Pinterest metadata for published articles.', 'Repairs missing Pinterest and Yoast SEO metadata for published articles.')
s=s.replace("if (isset($_POST['orbitpress_fix_all'])) echo '<div class=\"notice notice-success\"><p>Fixed metadata for '.intval($fixed).' article(s).</p></div>';", "if (isset($_POST['orbitpress_fix_all']) || isset($_POST['orbitpress_fix_yoast'])) echo '<div class=\"notice notice-success\"><p>Fixed Pinterest and Yoast metadata for '.intval($fixed).' article(s).</p></div>';" )
s=s.replace('>Fix all missing Pinterest metadata</button>', '>Fix all missing Pinterest + Yoast metadata</button>')
p.write_text(s)
