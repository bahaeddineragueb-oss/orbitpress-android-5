from pathlib import Path
p=Path('/home/ubuntu/orbitpress5/wp-plugin/orbitpress-pinterest-bridge/orbitpress-pinterest-bridge.php')
s=p.read_text()
marker="""function orbitpress_pin_tools_page() {"""
insert="""function orbitpress_pin_fix_one_url($actions, $post) {
  if ($post->post_type === 'post' && current_user_can('edit_post', $post->ID)) {
    $url = wp_nonce_url(admin_url('admin-post.php?action=orbitpress_fix_one&post_id='.$post->ID), 'orbitpress_fix_one_'.$post->ID);
    $actions['orbitpress_pin_fix'] = '<a href="'.esc_url($url).'">Fix Pinterest</a>';
  }
  return $actions;
}
add_filter('post_row_actions', 'orbitpress_pin_fix_one_url', 10, 2);
function orbitpress_pin_fix_one_handler() {
  $post_id = absint($_GET['post_id'] ?? 0);
  if (!$post_id || !current_user_can('edit_post', $post_id) || !check_admin_referer('orbitpress_fix_one_'.$post_id)) wp_die('You are not allowed to fix this post.');
  foreach (orbitpress_pin_fallback_values($post_id) as $key => $value) if (!get_post_meta($post_id, $key, true) && $value) update_post_meta($post_id, $key, $value);
  wp_safe_redirect(add_query_arg(['post'=>$post_id, 'action'=>'edit', 'message'=>'orbitpress_pin_fixed'], admin_url('post.php')));
  exit;
}
add_action('admin_post_orbitpress_fix_one', 'orbitpress_pin_fix_one_handler');
function orbitpress_pin_admin_notice() {
  if (($_GET['message'] ?? '') === 'orbitpress_pin_fixed') echo '<div class="notice notice-success is-dismissible"><p>OrbitPress: Pinterest metadata fixed for this article.</p></div>';
}
add_action('admin_notices', 'orbitpress_pin_admin_notice');

"""
if marker not in s: raise SystemExit('tools marker missing')
p.write_text(s.replace(marker,insert+marker,1))
