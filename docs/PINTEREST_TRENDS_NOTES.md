# Pinterest Trends integration notes

Source review date: 2026-09-04.

Official overview: https://developers.pinterest.com/docs/analytics-and-reports/trends/

Pinterest documents a Trends API for agencies, Enterprise clients, and partner platforms. The API is limited compared with trends.pinterest.com, returns up to 50 results for a request, and provides today's data rather than arbitrary historical dates. Its primary endpoint is List trending keywords.

Official endpoint: https://developers.pinterest.com/docs/api/v5/trending_keywords-list/

The endpoint is GET `/v5/trends/keywords/{region}/top/{trend_type}`. Required values are a region and trend type. Supported trend types include `growing`, `monthly`, `yearly`, and `seasonal`; `limit` can be 1–50. The API supports optional `interests`, including `gardening`, `home_decor`, `food_and_drinks`, `diy_and_crafts`, `travel`, and other categories. The endpoint documentation lists OAuth scope `user_accounts:read`.

Implementation decision: the Trends screen is read-only. It maps OrbitPress profiles to Pinterest interests (`food` -> `food_and_drinks`, `gardening` -> `gardening`, `home-decor` -> `home_decor`, and custom -> no interest filter), lets the user choose region, trend type, and result limit, and requires a Pinterest token. Suggestions are displayed for manual selection; nothing is automatically queued or published.

If a Pinterest account/token lacks the required Trends permission or access tier, the UI reports that limitation and leaves the existing manual keyword workflow available.
