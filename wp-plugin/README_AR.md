# OrbitPress Pinterest Bridge

هذه إضافة WordPress مستقلة تحفظ بيانات Pinterest التي يولدها OrbitPress في حقول منفصلة لكل مقال، ثم تعرضها في `wp_head` عبر Open Graph وTwitter Cards.

## التثبيت

1. ارفع ملف `orbitpress-pinterest-bridge.zip` من WordPress عبر **Plugins → Add New → Upload Plugin**.
2. اضغط **Activate**.
3. لا تحتاج إلى إدخال Token داخل الإضافة؛ المصادقة تتم من OrbitPress عبر WordPress Application Password.
4. في OrbitPress اختر **Manual review in Pinterest**.
5. عند النشر، ينشر OrbitPress المقال، ثم يرسل العنوان والوصف وAlt Text ورابط الصورة إلى الإضافة.

الإضافة لا تنشر Pin ولا تستدعي Pinterest API. هي فقط تحفظ metadata وتضعها في رأس صفحة المقال حتى يستطيع Pinterest قراءتها. النشر النهائي يتم يدويًا من Pinterest Composer.

## الحقول

- `_orbitpress_pinterest_title`
- `_orbitpress_pinterest_description`
- `_orbitpress_pinterest_alt_text`
- `_orbitpress_pinterest_image`

يوجد أيضًا مربع OrbitPress Pinterest داخل محرر المقال لتعديل القيم يدويًا عند الحاجة.
