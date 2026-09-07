# تقرير الفحص الشامل والتحول إلى Jetpack Compose — OrbitPress 5.0

## 1. ملخص الفحص والمشاكل المكتشفة

تم إجراء تدقيق برمجي وأمني شامل لمشروع OrbitPress 5.0 للهاتف، وتم حصر المشاكل السابقة في النقاط التالية:

| المشكلة السابقة | التصنيف | التأثير |
|---|---|---|
| انغلاق تام لشاشة الإعدادات عبر `window.prompt` | خطأ واجهة وبرمجة | حظر المستخدم من فتح الإعدادات بعد تفعيل قفل PIN لغياب `onJsPrompt` في أندرويد. |
| استهلاك مفرط للذاكرة وانهيارات OOM بسبب Base64 في الـ WebView | أداء ومعمارية | تحويل الصور الكبيرة (10–12 MB) لنصوص Base64 يرهق ذاكرة WebView ويسبب انهيار التطبيق. |
| فشل تصدير النسخ الاحتياطية (Blob Download) | خطأ نظام أندرويد | الـ WebView لا تدعم روابط Blob لتنزيل الملفات تلقائيًا، فكانت العملية تفشل بصمت. |
| خيوط غير مقيدة `Thread {}.start()` وتسريب الذاكرة | أداء ومعمارية | إنشاء خيوط غير خاضعة لإدارة دورة الحياة وتسريب مرجع النشاط `Activity Context Leak`. |
| إغلاق التطبيق عند الضغط على زر الرجوع | تجربة هاتف | عدم التعامل مع زر الرجوع للتنقل بين الشاشات السابقة. |
| تسريب ملف التوقيع وكلمات المرور في ملف البناء | أمان | كتابة كلمات المرور صراحة في `app/build.gradle.kts`. |
| تفاوت أبعاد Pinterest (نسبة 2:3) | تكامل وقيود | الرفض الصارم بدون هامش تقريب للصور الشائعة (مثل 735x1102 px). |
| حظر اتصالات WordPress بدون User-Agent وتوقف إعادة التوجيه | شبكة واتصال | فشل الطلبات مع المواقع المحمية بـ WAF أو التي تستخدم تحويلات canonical 301. |

---

## 2. التحول إلى المعمارية الأصلية الأفضل لأندرويد (Jetpack Compose)

تمت إعادة كتابة واجهة التطبيق ومنطق التفاعل بالكامل لتصبح **تطبيق أندرويد أصلي بنسبة 100%**:

1. **واجهة Material 3 أصلية:**
   - استبدال الـ WebView بالكامل في جميع الشاشات (Studio، Prompts، Drafts، Review، Pipeline، Activity، Trends، Repair، Settings) بـ Composables حديثة وسلسة.
   - حصر الـ WebView في أداة واحدة فقط هي `HtmlPreviewView` المخصصة لمعاينة المقال المنسق كقراءة فقط (`JavaScript disabled`) وبدون أي جسر أمني.

2. **إدارة الحالة والخيوط بـ Kotlin Coroutines & StateFlow:**
   - إدارة العمليات غير المتزامنة داخل `OrbitPressViewModel` باستخدام `viewModelScope` وخيوط `Dispatchers.IO`.
   - التخلص من أي `Thread {}.start()` عشوائي، ومنع تسريب الذاكرة.

3. **إدارة الصور المحلية الذكية (ImageManager):**
   - حفظ الصور مباشرة في مجلد التطبيق الداخلي وتوليد نسخ مصغرة (Thumbnails) عبر `BitmapFactory` مع `inSampleSize` ذكي.
   - إضافة خوارزمية تنظيف الصور المحذوفة تلقائياً عند حذف المسودات.
   - السماح بنسبة تفاوت مقبولة (1%) لأبعاد 2:3 لصور Pinterest.

4. **تكامل متقدم مع ملفات الهاتف (SAF):**
   - تصدير واستيراد النسخ الاحتياطية بصيغة JSON باستخدام `ActivityResultContracts.CreateDocument` و `OpenDocument`.
   - اختيار الصور ونصوص الكلمات المفتاحية عبر `ActivityResultContracts.GetContent`.

5. **أمان معزز:**
   - نافذة PIN أصلية مبنية بـ Jetpack Compose (`PinLockDialog`).
   - استخراج كلمات سر التوقيع للاعتماد على متغيرات البيئة.
   - إضافة `User-Agent: OrbitPress/5.0 (Android; Mobile)` ودعم تحويلات الـ Redirects الآمنة.

---

## 3. التحقق والاختبارات

- جميع اختبارات العقود (`DraftContractTest`, `ContentProfileContractTest`, `LongFormCompletenessContractTest`, `PinterestTrendsContractTest`, `ProviderCompatibilityContractTest`, `RecipeRequestContractTest`, `SettingsLockContractsTest`, `SettingsPersistenceContractTest`, `PublishingContractsTest`) مكتملة ومحدثة.
