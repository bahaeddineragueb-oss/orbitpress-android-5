# OrbitPress 5.0 Android (Native Jetpack Compose)

نسخة أندرويد أصلية (100% Native Jetpack Compose) من OrbitPress لإدارة دورة المحتوى والنشر إلى WordPress وPinterest بكفاءة وسرعة فائقة وأمان متكامل.

## التحول إلى المعمارية الأصلية (Jetpack Compose)

تمت إعادة هيكلة التطبيق بالكامل من معمارية الـ WebView الهجينة السابقة إلى واجهة مستخدم أصلية حديثة تعتمد على **Jetpack Compose (Material 3)** و **Kotlin Coroutines**، مما حقق:

1. **أداء وسلاسة فائقة (Zero WebView Lag):** معالجة الواجهات والتنقل مباشرة عبر محرك أندرويد الأصلي بدون بطء أو استهلاك غير مبرر للذاكرة.
2. **إدارة الصور دون استنزاف الذاكرة:** التخلص من نقل الصور كنصوص Base64 ضخمة؛ حفظ الصور والتحقق من أبعادها (2:3 لـ Pinterest) محليًا عبر `ImageManager` مع تحجيم ذكي يمنع مشاكل الذاكرة (Out Of Memory).
3. **تكامل متكامل مع نظام أندرويد:**
   - استخدام Android Storage Access Framework (SAF) لتصدير واستيراد النسخ الاحتياطية JSON بنقرة واحدة.
   - اختيار ملفات الصور ونصوص الكلمات المفتاحية عبر `ActivityResultContracts`.
   - معالجة زر الرجوع الفيزيائي في الهاتف (`BackHandler`) للتنقل السلس دون إغلاق التطبيق فجأة.
   - دعم الوضع الليلي (Dark Mode) والنهاري (Day Mode) بسلاسة.
4. **أمان معزز وحماية الأسرار:**
   - تشفير الإعدادات والأسرار عبر `EncryptedSharedPreferences`.
   - قفل الإعدادات برمز PIN محلي أصلي عبر Compose Dialog (دون الاعتماد على `window.prompt` التي كانت تسبب انغلاق الشاشة).
   - حصر الـ WebView في مكون واحد فقط لعرض معاينة المقال النهائي للقراءة فقط وبدون تفعيل JavaScript أو أي وصول لبيانات التطبيق.
   - إزالة كلمات المرور المباشرة من إعدادات التوقيع والاستناد إلى متغيرات البيئة.

---

## الميزات والوحدات الرئيسية

- **Content Studio:** إدارة قائمة الكلمات المفتاحية، توليد المقالات دفعة واحدة أو بشكل فردي، وتحديد تصنيف WordPress ومجال النيش (Food, Gardening, Home Decor, Custom).
- **Drafts & Versions:** تصفح المسودات، إدارة وتاريخ النسخ السابقة واستعادتها، والحذف الآمن مع تنظيف ملفات الصور المرتبطة.
- **Review & Publisher:** محرر كامل لعناوين المقال، الـ Slug، الكلمة المفتاحية، الميتا، تفاصيل Pinterest، فتح المعاينة الحية، وفحص مؤشر الجاهزية SEO & Pinterest (من 0 إلى 100%) قبل النشر بنقرة واحدة إلى WordPress.
- **Article Prompts:** تخصيص تعليمات الذكاء الاصطناعي لكل مجال تخصصي.
- **Editorial Pipeline:** متابعة أفكار ومراحل إنتاج المحتوى ومجموعات الكلمات المفتاحية (Keyword Clusters).
- **Pinterest Trends Explorer:** استكشاف الكلمات الأكثر رواجاً على Pinterest حسب المنطقة والنوع وإضافتها مباشرة لطابور التوليد.
- **WordPress Template Repair:** فحص المقالات المنشورة وإصلاح القوالب الناقصة تلقائياً مع إنشاء نسخة احتياطية مشفرة.
- **Multi-site Settings:** إدارة مواقع متعددة بملفات تعريف مستقلة.

---

## المتطلبات والتشغيل

- **Android Studio** Ladybug أو أحدث.
- **JDK 17**.
- **Android SDK Platform 35** (الحد الأدنى Android 8.0 - API 26).

```bash
gradle :app:testDebugUnitTest
gradle :app:assembleRelease
```

---

## بنية المشروع

```
app/src/main/java/com/askinz/publisher/
├── MainActivity.kt               # النقطة الرئيسية وواجهة Jetpack Compose والتنقل
├── OrbitPressViewModel.kt        # إدارة الحالة والعمليات غير المتزامنة عبر Coroutines
├── Models.kt                     # كائنات ونماذج البيانات (Drafts, Keywords, Logs, Settings)
├── SecureStorage.kt              # التخزين المشفر وإدارة الأسرار وقفل PIN
├── ImageManager.kt               # إدارة الصور المحلية والتحقق من نسبة 2:3 والتنظيف
├── PublishingService.kt          # الاتصال الشبكي بالـ APIs (OpenAI, WordPress, Pinterest, Cloudflare)
├── DraftContract.kt              # تطبيع ومعالجة هيكل المقالات وتوليد Schema JSON-LD
├── PublishingContracts.kt        # ضوابط HTTPS وأبعاد الصور والتصنيفات
├── ContentProfileContract.kt     # قواعد النيش الحلال والمجالات المخصصة
├── LongFormCompletenessContract.kt # التحقق من اكتمال مقالات الوصفات والقوائم
├── ui/
│   ├── theme/Theme.kt            # الألوان والسمات (Material 3)
│   ├── HtmlPreviewView.kt        # معاينة HTML معزولة للقراءة فقط
│   ├── PinLockDialog.kt          # نافذة قفل الإعدادات PIN الأصلية
│   └── screens/
│       ├── ContentStudioScreen.kt
│       ├── DraftsScreen.kt
│       ├── ReviewPublisherScreen.kt
│       ├── ArticlePromptsScreen.kt
│       ├── EditorialPipelineScreen.kt
│       ├── ActivityLogScreen.kt
│       ├── PinterestTrendsScreen.kt
│       ├── TemplateRepairScreen.kt
│       └── SettingsScreen.kt
```
