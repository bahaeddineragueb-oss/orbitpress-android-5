package com.askinz.publisher

object WordPressMarkup {
  fun featuredImage(url: String, altText: String): String =
    "<figure class=\"wp-block-image size-large\"><img src=\"${escape(url)}\" alt=\"${escape(altText)}\" /></figure>"

  fun pinterestSaveButton(shareUrl: String, title: String, description: String, mediaUrl: String, altText: String): String =
    "<p data-askinz-pinterest-direct=\"true\"><a href=\"${escape(shareUrl)}\" data-pin-do=\"buttonPin\" data-pin-media=\"${escape(mediaUrl)}\" data-pin-description=\"${escape(description)}\" data-pin-title=\"${escape(title)}\" aria-label=\"Save ${escape(title)} to Pinterest\" target=\"_blank\" rel=\"noopener\">Save on Pinterest</a></p>"

  fun structuredData(json: String): String =
    "<script type=\"application/ld+json\">$json</script>"

  private fun escape(value: String): String = value.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;").replace(">", "&gt;")
}
