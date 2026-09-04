# OrbitPress 5.0 Android

نسخة مستقلة من OrbitPress لإدارة دورة المحتوى كاملة داخل تطبيق Android أصلي.

## الميزات الجديدة

- Editorial Pipeline مستقل عن شاشة Content Studio.
- Content Calendar للتخطيط والتذكير المحلي دون نشر تلقائي.
- Content Brief لكل كلمة مفتاحية، محفوظ محليًا ولا يطلق API تلقائيًا.
- Keyword Clusters لتجميع المواضيع والكلمات المرتبطة ومنع التكرار التحريري.
- Pipeline Records لمتابعة الحالات من الفكرة إلى المسودة والمراجعة والنشر.
- تدقيق SEO وPinterest للعرض فقط؛ لا يفرض تعديلًا ولا يمنع النشر مهما كانت النتيجة.
- Pinterest Rich Pins باستخدام Open Graph وSchema.org القياسي، دون اختراع Pinterest Schema غير رسمي.
- دعم OpenAI-compatible Images API وCloudflare Workers AI.

## ما بقي ثابتًا

النشر إلى WordPress يدوي فقط. لم تتم إضافة نشر خلفي أو جدولة تلقائية. بقيت حواجز HTTPS، منع slug المكرر، فحص MIME للصور، نسبة Pinterest exact 2:3، التخزين المشفر، وتنظيف HTML الآمن.

## المتطلبات والتشغيل

- Android Studio حديث.
- JDK 17.
- Android SDK Platform 35.

```bash
gradle :app:testDebugUnitTest
gradle :app:assembleRelease
```

يحتوي `.github/workflows/android.yml` على بناء واختبار تلقائيين ورفع APK كـ Artifact.

## الأمان

لا توجد مفاتيح API أو كلمات مرور داخل المصدر. تُدخل مفاتيح Article API وWordPress وPinterest وCloudflare من Settings وتُحفظ الأسرار عبر EncryptedSharedPreferences. لا تضف `local.properties` أو ملفات التوقيع إلى Git.


## سياسة التحديث

OrbitPress 5 يستخدم applicationId ثابتًا (`com.askinz.publisher.v5`) ومفتاح توقيع ثابتًا. لذلك تحتفظ تحديثات 5.x بالمسودات والإعدادات وبيانات Editorial Pipeline تلقائيًا ولا تتطلب حذف التطبيق أو إعادة إدخال البيانات. النسخة الأولى الموقعة بالمفتاح الثابت هي ترقية انتقالية واحدة؛ بعد تثبيتها تصبح كل تحديثات 5.x فوقية.
