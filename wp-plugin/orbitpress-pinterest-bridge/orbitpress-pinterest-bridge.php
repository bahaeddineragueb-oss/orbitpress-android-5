<?php
/**
 * Plugin Name: OrbitPress Pinterest Bridge
 * Description: Stores OrbitPress Pinterest metadata separately and exposes it through Open Graph/Twitter tags.
 * Version: 1.0.0
 * Requires at least: 6.0
 * Requires PHP: 7.4
 * Author: OrbitPress
 * License: GPL-2.0-or-later
 */
if (!defined('ABSPATH')) { exit; }

const ORBITPRESS_PIN_META = [
  '_orbitpress_pinterest_title',
  '_orbitpress_pinterest_description',
  '_orbitpress_pinterest_alt_text',
  '_orbitpress_pinterest_image',
];

function orbitpress_pin_register_meta() {
  foreach (ORBITPRESS_PIN_META as $key) {
    register_post_meta('post', $key, [
      'type' => 'string', 'single' => true, 'show_in_rest' => true,
      'sanitize_callback' => 'sanitize_text_field', 'auth_callback' => function() { return current_user_can('edit_posts'); },
    ]);
  }
}
add_action('init', 'orbitpress_pin_register_meta');

function orbitpress_pin_rest_routes() {
  register_rest_route('orbitpress/v1', '/pinterest-meta', [
    'methods' => 'POST', 'permission_callback' => function() { return current_user_can('edit_posts'); },
    'callback' => function(WP_REST_Request $request) {
      $post_id = absint($request->get_param('post_id'));
      $post = get_post($post_id);
      if (!$post || $post->post_type !== 'post') return new WP_Error('invalid_post', 'A valid post_id is required.', ['status' => 400]);
      $values = [
        '_orbitpress_pinterest_title' => sanitize_text_field($request->get_param('title')),
        '_orbitpress_pinterest_description' => sanitize_textarea_field($request->get_param('description')),
        '_orbitpress_pinterest_alt_text' => sanitize_text_field($request->get_param('alt_text')),
        '_orbitpress_pinterest_image' => esc_url_raw($request->get_param('image')),
      ];
      foreach ($values as $key => $value) update_post_meta($post_id, $key, $value);
      return rest_ensure_response(['ok' => true, 'post_id' => $post_id, 'meta' => $values]);
    },
    'args' => [
      'post_id' => ['required' => true, 'sanitize_callback' => 'absint'],
      'title' => ['required' => true], 'description' => ['required' => true],
      'alt_text' => ['required' => true], 'image' => ['required' => true],
    ],
  ]);
}
add_action('rest_api_init', 'orbitpress_pin_rest_routes');

function orbitpress_pin_head_tags() {
  if (!is_singular('post')) return;
  global $post;
  $title = get_post_meta($post->ID, '_orbitpress_pinterest_title', true);
  $description = get_post_meta($post->ID, '_orbitpress_pinterest_description', true);
  $image = get_post_meta($post->ID, '_orbitpress_pinterest_image', true);
  if (!$title && !$description && !$image) return;
  $title = $title ?: get_the_title($post);
  $description = $description ?: get_the_excerpt($post);
  $image = $image ?: get_the_post_thumbnail_url($post, 'full');
  $url = get_permalink($post);
  echo "\n<!-- OrbitPress Pinterest Bridge -->\n";
  foreach ([
    'og:title' => $title, 'og:description' => $description, 'og:image' => $image,
    'og:url' => $url, 'og:type' => 'article',
  ] as $property => $content) {
    if ($content) printf("<meta property=\"%s\" content=\"%s\" />\n", esc_attr($property), esc_attr($content));
  }
  printf("<meta name=\"twitter:card\" content=\"summary_large_image\" />\n");
  printf("<meta name=\"twitter:title\" content=\"%s\" />\n", esc_attr($title));
  printf("<meta name=\"twitter:description\" content=\"%s\" />\n", esc_attr($description));
  if ($image) printf("<meta name=\"twitter:image\" content=\"%s\" />\n", esc_attr($image));
}
add_action('wp_head', 'orbitpress_pin_head_tags', 5);

function orbitpress_pin_meta_box() { add_meta_box('orbitpress-pinterest-bridge', 'OrbitPress Pinterest', 'orbitpress_pin_meta_box_html', 'post', 'side'); }
add_action('add_meta_boxes', 'orbitpress_pin_meta_box');
function orbitpress_pin_meta_box_html($post) {
  wp_nonce_field('orbitpress_pin_save', 'orbitpress_pin_nonce');
  foreach ([['title','Title',100],['description','Description',800],['alt_text','Image alt text',320]] as $field) {
    $key = '_orbitpress_pinterest_'.$field[0]; $value = get_post_meta($post->ID, $key, true);
    echo '<p><label><strong>'.esc_html($field[1]).'</strong><br><textarea style="width:100%" rows="'.$field[2] / 100 .'" maxlength="'.$field[2].'" name="'.$key.'">'.esc_textarea($value).'</textarea></label></p>';
  }
}
function orbitpress_pin_save_meta($post_id) {
  if (!isset($_POST['orbitpress_pin_nonce']) || !wp_verify_nonce($_POST['orbitpress_pin_nonce'], 'orbitpress_pin_save') || defined('DOING_AUTOSAVE') || !current_user_can('edit_post', $post_id)) return;
  foreach (ORBITPRESS_PIN_META as $key) if (isset($_POST[$key])) update_post_meta($post_id, $key, $key === '_orbitpress_pinterest_image' ? esc_url_raw($_POST[$key]) : sanitize_textarea_field($_POST[$key]));
}
add_action('save_post_post', 'orbitpress_pin_save_meta');
