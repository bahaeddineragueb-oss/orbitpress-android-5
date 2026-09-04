from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/wp-plugin/orbitpress-pinterest-bridge/orbitpress-pinterest-bridge.php')
s=p.read_text()
old="""function orbitpress_pin_meta_box_html($post) {
  wp_nonce_field('orbitpress_pin_save', 'orbitpress_pin_nonce');
  foreach ([['title','Title',100],['description','Description',800],['alt_text','Image alt text',320]] as $field) {
    $key = '_orbitpress_pinterest_'.$field[0]; $value = get_post_meta($post->ID, $key, true);
    echo '<p><label><strong>'.esc_html($field[1]).'</strong><br><textarea style=\"width:100%\" rows=\"'.$field[2] / 100 .'\" maxlength=\"'.$field[2].'\" name=\"'.$key.'\">'.esc_textarea($value).'</textarea></label></p>';
  }
}
function orbitpress_pin_save_meta($post_id) {
  if (!isset($_POST['orbitpress_pin_nonce']) || !wp_verify_nonce($_POST['orbitpress_pin_nonce'], 'orbitpress_pin_save') || defined('DOING_AUTOSAVE') || !current_user_can('edit_post', $post_id)) return;
  foreach (ORBITPRESS_PIN_META as $key) if (isset($_POST[$key])) update_post_meta($post_id, $key, $key === '_orbitpress_pinterest_image' ? esc_url_raw($_POST[$key]) : sanitize_textarea_field($_POST[$key]));
}
add_action('save_post_post', 'orbitpress_pin_save_meta');"""
new="""function orbitpress_pin_meta_box_html($post) {
  wp_nonce_field('orbitpress_pin_save', 'orbitpress_pin_nonce');
  foreach ([['title','Title',100],['description','Description',800],['alt_text','Image alt text',320]] as $field) {
    $key = '_orbitpress_pinterest_'.$field[0]; $value = get_post_meta($post->ID, $key, true);
    echo '<p><label><strong>'.esc_html($field[1]).'</strong><br><textarea style=\"width:100%\" rows=\"'.($field[2] / 100).'\" maxlength=\"'.$field[2].'\" name=\"'.$key.'\">'.esc_textarea($value).'</textarea></label></p>';
  }
  echo '<p><button type=\"submit\" class=\"button\" name=\"orbitpress_pin_fix\" value=\"1\">Fix Pinterest metadata</button></p>';
  echo '<p class=\"description\">Fills missing fields from this article without changing its content. Use Update to save.</p>';
}
function orbitpress_pin_fallback_values($post_id) {
  $post = get_post($post_id);
  $title = get_post_meta($post_id, '_orbitpress_pinterest_title', true) ?: get_the_title($post_id);
  $description = get_post_meta($post_id, '_orbitpress_pinterest_description', true) ?: wp_trim_words(wp_strip_all_tags($post->post_excerpt ?: $post->post_content), 40, '...');
  $image = get_post_meta($post_id, '_orbitpress_pinterest_image', true) ?: get_the_post_thumbnail_url($post_id, 'full');
  $alt = get_post_meta($post_id, '_orbitpress_pinterest_alt_text', true) ?: ($title . ' Pinterest image');
  return ['_orbitpress_pinterest_title'=>sanitize_text_field($title), '_orbitpress_pinterest_description'=>sanitize_textarea_field($description), '_orbitpress_pinterest_alt_text'=>sanitize_text_field($alt), '_orbitpress_pinterest_image'=>esc_url_raw($image ?: '')];
}
function orbitpress_pin_save_meta($post_id) {
  if (!isset($_POST['orbitpress_pin_nonce']) || !wp_verify_nonce($_POST['orbitpress_pin_nonce'], 'orbitpress_pin_save') || defined('DOING_AUTOSAVE') || !current_user_can('edit_post', $post_id)) return;
  if (isset($_POST['orbitpress_pin_fix'])) foreach (orbitpress_pin_fallback_values($post_id) as $key => $value) update_post_meta($post_id, $key, $value);
  foreach (ORBITPRESS_PIN_META as $key) if (isset($_POST[$key])) update_post_meta($post_id, $key, $key === '_orbitpress_pinterest_image' ? esc_url_raw($_POST[$key]) : sanitize_textarea_field($_POST[$key]));
}
add_action('save_post_post', 'orbitpress_pin_save_meta');

function orbitpress_pin_tools_page() {
  if (!current_user_can('edit_posts')) return;
  $fixed = 0;
  if (isset($_POST['orbitpress_fix_all']) && check_admin_referer('orbitpress_fix_all')) {
    foreach (get_posts(['post_type'=>'post','post_status'=>'publish','numberposts'=>-1,'fields'=>'ids']) as $post_id) {
      $values = orbitpress_pin_fallback_values($post_id); $changed = false;
      foreach ($values as $key => $value) if (!get_post_meta($post_id, $key, true) && $value) { update_post_meta($post_id, $key, $value); $changed = true; }
      if ($changed) $fixed++;
    }
  }
  echo '<div class=\"wrap\"><h1>OrbitPress Pinterest Fix</h1><p>Repairs missing Pinterest metadata for published articles. It does not publish Pins or alter article content.</p>';
  if (isset($_POST['orbitpress_fix_all'])) echo '<div class=\"notice notice-success\"><p>Fixed metadata for '.intval($fixed).' article(s).</p></div>';
  echo '<form method=\"post\">'; wp_nonce_field('orbitpress_fix_all'); echo '<p><button class=\"button button-primary\" name=\"orbitpress_fix_all\" value=\"1\">Fix all missing Pinterest metadata</button></p></form></div>';
}
function orbitpress_pin_tools_menu() { add_management_page('OrbitPress Pinterest Fix', 'OrbitPress Pinterest Fix', 'edit_posts', 'orbitpress-pinterest-fix', 'orbitpress_pin_tools_page'); }
add_action('admin_menu', 'orbitpress_pin_tools_menu');"""
if old not in s: raise SystemExit('plugin block not found')
p.write_text(s.replace(old,new))
