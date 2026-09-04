# Pinterest API integration notes

Source review date: 2026-09-03.

The official Create Pin API is documented at https://developers.pinterest.com/docs/api/v5/pins-create/. It uses POST `/v5/pins`, requires Pinterest OAuth scopes `boards:read`, `boards:write`, `pins:read`, and `pins:write`, and accepts `board_id`, optional `board_section_id`, `title` (up to 100 characters), `alt_text` (up to 500), `description` (up to 800), `link` (up to 2048), and an image Base64 `media_source` with `source_type=image_base64` and a MIME `content_type`.

The official List Boards API is documented at https://developers.pinterest.com/docs/api/v5/boards-list/. It uses GET `/v5/boards`, requires `boards:read`, supports pagination, and returns board `id` and `name` values for the board selector.

The official OAuth setup guide is documented at https://developers.pinterest.com/docs/getting-started/set-up-authentication-and-authorization/. It describes an OAuth 2.0 authorization-code flow with a registered app ID, client secret, redirect URI, and scopes. Access tokens are sent as `Authorization: Bearer ...`; the documentation states that newer apps use continuously refreshable refresh tokens. The current local-app implementation intentionally keeps Pinterest optional and supports an encrypted user-provided access token, avoiding background execution.

Product behavior decision: Pinterest is invoked only as part of the user's explicit WordPress publish action. If Pinterest is not configured, WordPress publishing remains available. If Pinterest fails after WordPress succeeds, the result is recorded separately so a Pinterest problem does not falsely report that the WordPress post failed.
