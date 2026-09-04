# رؤية OrbitPress Pro ومرجع UI/UX

## 1. الهدف العام

OrbitPress Pro ليس مجرد مولد مقالات أو أداة نشر، بل منصة احترافية لإدارة دورة المحتوى كاملة من الفكرة والكلمة المفتاحية إلى البحث، والتوليد، والمراجعة، وتحسين SEO، وإدارة الصور، والنشر إلى WordPress وPinterest، ثم تحديث المقالات وتحليلها.

المسار الأساسي المقترح هو:

```text
Keyword → Research → Brief → Article → SEO → Images → Review → WordPress → Pinterest → Tracking → Refresh
```

المبدأ الأساسي هو أن التطبيق يقترح ويشرح ويعرض المعاينة، ولا يفرض تعديلًا أو نشرًا أو حذفًا دون اختيار واضح من المستخدم.

## 2. جميع الميزات المحفوظة للتنفيذ

### مساحة المحتوى

ينبغي أن تحتوي المنصة على مساحة عمل متعددة المواقع تعرض الكلمات المفتاحية، والمجموعات الموضوعية، والمسودات، والمقالات المنشورة، والمقالات التي تحتاج إلى تحديث، والمقالات التي تفتقد بيانات SEO أو الصور أو الروابط الداخلية.

تستخدم المقالات حالات واضحة: Idea، Brief، Draft، Review، Ready، Published، Needs Update.

### محرك SEO مستقل

يجب ألا يعتمد النظام على Prompt فقط. بعد إنشاء المقال، يفحص التطبيق فعليًا العنوان، والكلمة المفتاحية، والوصف، والـ slug، والمقدمة، والعناوين، والكثافة الطبيعية، والروابط، والصور، وAlt Text، وSchema، وOpen Graph، وCanonical URL، وقابلية القراءة.

يعرض التطبيق درجة مفهومة مثل:

```text
SEO Readiness: 92/100
Technical SEO: 100/100
On-page SEO: 88/100
Readability: 91/100
Images: 100/100
Internal linking: 75/100
```

كل مشكلة يجب أن تحتوي على شرح، ومعاينة للتعديل، وزري Apply وUndo.

### تكامل Yoast

يحفظ النظام حقول Yoast الرسمية، ومنها Focus keyphrase، وSEO title، وMeta description، ويزامنها مع WordPress. يجب دعم مزامنة Open Graph، وSocial title، وSocial description، وSocial image، وPinterest metadata، وCanonical URL عندما تكون مدعومة.

### الروابط الداخلية

يجلب التطبيق المقالات المنشورة وعناوينها وروابطها من WordPress، ويبني فهرسًا محليًا للمقالات، ثم يقترح روابط حقيقية وAnchors مناسبة. يمنع اختراع الروابط. يراجع المستخدم الاقتراحات قبل إدراجها.

### تحديث المقالات القديمة

يوفر النظام Content Refresh للمقالات المنشورة، مع مقارنة Before وAfter، واقتراح تحسينات للكلمات المفتاحية والعناوين والمقدمة والروابط والصور والوصف، مع إنشاء نسخة احتياطية قبل التطبيق وإمكانية Restore.

### Pinterest Pro

يدعم النظام Direct Pinterest API، وManual review، وWordPress only. يحافظ على فصل Pinterest title وdescription وalt text وboard وboard section وdestination URL وimage. يحفظ Pin ID، ويمنع التكرار، ويدعم إعادة المحاولة وسجل الحالة.

### Image Studio

يدعم Featured image، وPinterest image بنسبة 2:3، وصور المقال، والضغط، وWebP، وAlt Text، وCaption، وفحص الأبعاد والحجم، ومنع الصور المكررة، مع اقتراح Prompts للصور دون فرض استخدامها.

### تعدد المواقع

كل موقع يملك إعدادات مستقلة لـ Article API، وWordPress، وPinterest، وPrompts، وقواعد SEO، وقوالب المقالات، وإعدادات الصور. تبديل الموقع يجب أن يكون واضحًا ولا يخلط البيانات بين المواقع.

### قوالب المحتوى

يدعم النظام قوالب مثل Recipe، How-to، Listicle، Product comparison، Gardening guide، وHome decor guide. يحتوي كل قالب على بنية، وقواعد SEO، وSchema، وعدد أقسام، وتعليمات صور وروابط وPrompt خاص.

### المراجعة والموافقة

تتكون شاشة Review من أقسام مستقلة:

```text
Article Review
SEO Review
Pinterest Review
Image Review
Schema Review
Internal Links Review
Publishing Review
```

لكل قسم Preview وApply وUndo وApprove. يجب أن يتمكن المستخدم من اختيار شروط النشر، مثل اشتراط وجود عنوان SEO أو Meta description أو صورة أو Alt Text أو روابط داخلية.

### السجل والنسخ الاحتياطية

يسجل النظام كل عملية: التوليد، والتحرير، وتعديل SEO، واستبدال الصورة، والمزامنة، والنشر، وإنشاء Pin، والأخطاء. يدعم Export backup وImport backup وRestore previous version.

### التحديثات

يجب الحفاظ على applicationId ومفتاح التوقيع وبنية البيانات، وإضافة Migrations لكل تحديث. لا يجوز أن يؤدي تحديث APK إلى حذف التطبيق أو المسودات أو الإعدادات أو المواقع المحفوظة.

## 3. التصور العام للواجهة

### المبدأ

الواجهة يجب أن تكون هادئة، واضحة، وموجهة للعمل. لا تعرض كل الخيارات دفعة واحدة، ولا تجعل المستخدم يتنقل بين شاشات كثيرة للوصول إلى المقال.

المسار الرئيسي للمستخدم هو:

```text
Home → Content → Review → Publish → Results
```

أما الإعدادات المتقدمة والتحليلات والأدوات فتكون في مسارات ثانوية واضحة.

### شريط التنقل الرئيسي

يقترح استخدام خمس وجهات رئيسية فقط:

1. **Home**: ملخص العمل والتنبيهات.
2. **Content**: الكلمات المفتاحية والمسودات والمقالات.
3. **Review**: المراجعة الحالية للمقال.
4. **Insights**: SEO، Pinterest، وتحديثات المحتوى.
5. **Settings**: إعدادات المواقع والموصلات.

تظهر أدوات مثل Article Prompts وTemplates وBackups داخل Content أو Settings حسب طبيعتها، ولا تظهر كعناصر كثيرة مستقلة في القائمة الرئيسية.

### الشاشة الرئيسية Home

تعرض:

- الموقع النشط.
- عدد المسودات.
- المقالات الجاهزة للنشر.
- المقالات التي تحتاج إصلاحًا.
- المقالات التي تفتقد SEO.
- آخر عمليات النشر.
- زر واضح: Create article.

يجب أن تكون الأولوية للعمل، لا للأرقام الزخرفية.

### شاشة Content

تعرض بطاقات أو قائمة للمقالات، مع فلترة حسب الحالة والموقع والتصنيف. لكل مقال عنوان مختصر، وحالته، وSEO readiness، والصورة، وآخر تحديث، وأزرار Review وRefresh وPublish.

### شاشة Review

تستخدم تخطيطًا عموديًا منظمًا على الهاتف، وبطاقات قابلة للفتح والإغلاق:

```text
1. Article content
2. SEO readiness
3. Pinterest metadata
4. Images
5. Internal links
6. Schema
7. Publishing
```

في أعلى الشاشة يظهر شريط ملخص:

```text
Draft · Food · SEO 86/100 · 2 issues · Images ready
```

لا تكون النتيجة المنخفضة حاجزًا للنشر إلا إذا اختار المستخدم ذلك.

### بطاقة SEO

تحتوي على Focus keyphrase، وSEO title، وMeta description، وSlug، ومؤشرات طول واضحة، وقائمة فحص صغيرة. اللون الأخضر يستخدم للنجاح الحقيقي، والأصفر للتحسين، والأحمر للمشكلة التي تحتاج إجراء.

### بطاقة Pinterest

تعرض Pinterest title وdescription وAlt Text والصورة واللوحة في بطاقة مستقلة، مع معاينة لا تخلط العنوان والوصف. خيارات النشر تكون واضحة:

```text
WordPress only
Manual review
Direct API
```

### شاشة Settings

تنظم الإعدادات في بطاقات منفصلة، ولكل بطاقة زر حفظ خاص بها. لا يوجد حفظ جماعي صامت.

الأقسام المقترحة:

- Active website.
- Article API.
- Image Generator.
- Pinterest publishing.
- WordPress publishing.
- Security and backup.

بعد تعديل أي بطاقة يظهر بوضوح:

```text
Unsaved changes
Save this section
```

### Article Prompts

تبقى شاشة مستقلة باسم Article Prompts، تحتوي على Prompts حسب نوع المحتوى، مع توضيح أن قواعد SEO والسلامة الأساسية لا يمكن تعطيلها. لكل التعديلات زر حفظ مستقل.

## 4. الهوية البصرية المقترحة

الهوية يجب أن تكون احترافية وهادئة، مع خلفية محايدة، وبطاقات واضحة، ولون رئيسي واحد للأفعال، ولون أخضر للنجاح، وأصفر للتحذير، وأحمر للأخطاء.

يجب تجنب كثرة الألوان، والظلال الثقيلة، والنصوص الصغيرة، والرموز غير الواضحة. تعتمد الواجهة على مسافات مريحة، وعناوين قصيرة، ووصف مختصر أسفل كل خيار.

## 5. قواعد UX غير قابلة للتفاوض

- لا حذف أو استبدال أو نشر دون فعل واضح من المستخدم.
- كل عملية مهمة تعرض نتيجتها بوضوح.
- كل تعديل AI يعرض Before وAfter عندما يغير محتوى المقال.
- كل تعديل قابل للتراجع.
- كل قسم إعدادات يحفظ بصورة مستقلة.
- لا تختلط بيانات المواقع.
- لا تعرض الأسرار بعد حفظها.
- لا تمنع نتيجة SEO المنخفضة النشر تلقائيًا.
- لا تستخدم روابط داخلية مخترعة.
- لا تخلط Pinterest title مع description.
- تحفظ التحديثات المسودات والإعدادات والنسخ السابقة.

## 6. مراحل التنفيذ المقترحة

### المرحلة الأولى: Pro Foundation

تحسين Review، ومحرك SEO، ومزامنة Yoast، وإصلاح المقالات القديمة، والنسخ الاحتياطية، وسجل العمليات.

### المرحلة الثانية: Pro Advanced

فهرس الروابط الداخلية، وContent Refresh، وTopic clusters، وقوالب المحتوى، وImage Studio، وPinterest API.

### المرحلة الثالثة: Pro Platform

لوحة التقارير، وGoogle Search Console، وPinterest Analytics، والجدولة، والمستخدمون والصلاحيات، والمراجعة الجماعية، وAPI وWebhooks.

## 7. قرار التصميم قبل التنفيذ

قبل إضافة الميزات، يجب اعتماد:

1. بنية التنقل الرئيسية.
2. شكل بطاقة Review.
3. ألوان النجاح والتحذير والخطأ.
4. طريقة عرض SEO readiness.
5. سياسة Apply وUndo.
6. طريقة تبديل المواقع.
7. مكان Article Prompts وTemplates وBackups.

بعد اعتماد هذه العناصر، ينفذ التطوير على مراحل صغيرة قابلة للاختبار، مع الحفاظ على التحديثات فوق النسخة الحالية.


## 8. OrbitPress Pro كتطبيق عام متعدد المجالات

OrbitPress Pro يجب أن يكون تطبيق نشر عام، وليس تطبيقًا خاصًا بالطعام أو الوصفات أو مجال واحد. يبدأ المستخدم باختيار المجال أو إنشاء Profile مخصص، ثم يحدد نوع المحتوى المطلوب، مثل مقال معلوماتي، دليل عملي، مراجعة، مقارنة، قائمة، How-to، دراسة حالة، مقال محلي، أو محتوى متخصص.

لا يجوز أن تظهر لغة Food أو Recipe كافتراضية في الواجهة العامة. يمكن الاحتفاظ بقوالب الطعام ضمن قوالب اختيارية، مثل أي مجال آخر، دون أن تفرض بنية الوصفة على المقالات العامة.

### المجالات المقترحة

يدعم النظام Profiles عامة مثل:

- Business and entrepreneurship.
- Technology.
- Health and wellness مع قواعد السلامة وعدم تقديم ادعاءات طبية غير موثقة.
- Finance مع قواعد واضحة لعدم تقديم استشارات مالية شخصية أو وعود.
- Travel.
- Home and lifestyle.
- Gardening.
- Education.
- Food and recipes.
- Custom professional profile.

يملك كل Profile قواعده التحريرية وSchema المناسب ونوع القارئ وبنية المقال، لكن جميعها تستخدم محرك SEO العام نفسه.

## 9. Professional Article Prompt Engine

لا يعتمد النظام على Prompt بسيط من نوع اكتب مقالًا عن الكلمة المفتاحية. يجب أن يعمل Prompt Engine كـ Editorial Brief احترافي يحدد قبل التوليد:

- Search intent.
- Audience.
- User problem.
- Content type.
- Desired outcome.
- Information depth.
- Required sections.
- Primary and secondary keywords.
- Editorial tone.
- Evidence and source policy.
- Internal linking policy.
- Image requirements.
- Schema requirements.
- SEO title and meta description constraints.
- Readability target.
- Prohibited claims.

### أسلوب المقال

الهدف هو إنتاج كتابة طبيعية، دقيقة، مفيدة، ومتنوعة الأسلوب، تشبه عمل محرر وخبير SEO محترف. يحقق ذلك عبر:

- بدء المقال بإجابة واضحة بدل مقدمة عامة.
- اختلاف أطوال الجمل والفقرات بصورة طبيعية.
- استخدام أمثلة عملية وسياق حقيقي عندما تتوفر معلومات موثوقة.
- تجنب العبارات النمطية المتكررة مثل In today’s fast-paced world وWhether you are a beginner أو This comprehensive guide.
- عدم تكرار العناوين والانتقالات بنفس الصيغة.
- استخدام لغة مباشرة وصوت نشط.
- إضافة حدود الموضوع والاستثناءات والمفاضلات.
- عدم اختلاق الخبرة أو التجارب أو الأرقام أو المصادر.
- عدم استخدام حشو الكلمات المفتاحية.
- عدم استخدام لغة تسويقية مبالغ فيها أو وعود غير قابلة للإثبات.
- عدم إضافة علامات أو ادعاءات تهدف إلى خداع أدوات التقييم.

> المعيار هو جودة تحريرية حقيقية وقيمة للقارئ، وليس محاولة التحايل على أدوات الكشف. لا يمكن ضمان نتيجة أي أداة للكشف عن النصوص المولدة، لكن يمكن جعل النص مفيدًا وطبيعيًا ومهنيًا وقابلًا للتحرير البشري.

### مراحل التوليد المقترحة

يعمل المحرك داخليًا على مراحل:

1. تحليل الكلمة المفتاحية ونية البحث.
2. إنشاء Editorial Brief.
3. إنشاء Outline منطقي.
4. إنشاء مسودة أولى منظمة.
5. فحص SEO والقراءة والادعاءات والتكرار.
6. إصلاح المشكلات المحددة فقط.
7. عرض النتيجة في Review قبل الحفظ أو النشر.

المستخدم لا يحتاج إلى رؤية التعقيد الداخلي، لكنه يرى الملخص والمشكلات والتعديلات المقترحة.

## 10. SEO شامل لكل مجال

يجب أن يطلب النظام ويقيس:

- Primary focus keyphrase.
- Secondary keywords and synonyms.
- Search intent match.
- SEO title ضمن العرض المناسب.
- Meta description ضمن الطول المناسب وتحتوي على الكلمة المفتاحية طبيعيًا.
- Canonical-friendly slug.
- الكلمة المفتاحية في المقدمة عند ملاءمتها.
- الكلمة المفتاحية أو مرادفاتها في H2/H3.
- كثافة طبيعية دون حشو.
- روابط داخلية حقيقية.
- روابط خارجية موثوقة عند الحاجة.
- Alt Text وصفي للصور.
- Featured image وSocial image.
- Article أو HowTo أو FAQ أو Recipe Schema حسب نوع المقال، دون إضافة Schema غير مناسب.
- Open Graph وTwitter metadata.
- Readability، طول الفقرات، الجمل، والانتقالات.
- أسئلة المستخدم ذات الصلة وFAQ عندما تكون مفيدة فعلًا.

يجب أن يعرض محرك SEO سبب كل توصية، ولا يغير النص تلقائيًا إلا بعد موافقة المستخدم أو اختيار وضع Apply.

## 11. Image Generation UX البسيط

لا نريد شاشة إعدادات مليئة بعشرات الحقول. تكون واجهة الصور بسيطة:

```text
Image source
( ) Manual images
( ) OpenAI
( ) Cloudflare Workers AI
```

### Manual images

عند اختيار Manual images، تظهر فقط:

- Choose featured image.
- Choose Pinterest image.
- Optional article images.

ولا تظهر أي حقول API.

### OpenAI

عند اختيار OpenAI، تظهر فقط الحقول الخاصة به:

- OpenAI-compatible Base URL عند الحاجة.
- Model.
- API key.
- Save OpenAI settings.

تختفي حقول Cloudflare تمامًا.

### Cloudflare Workers AI

عند اختيار Cloudflare Workers AI، تظهر فقط:

- Cloudflare Account ID.
- Cloudflare model.
- Cloudflare API token.
- Save Cloudflare settings.

تختفي حقول OpenAI تمامًا.

### قواعد التفاعل

- التبديل بين المزودين لا يحذف الإعدادات المحفوظة للمزود الآخر، لكنه يخفيها.
- كل مزود يملك زر حفظ مستقل.
- لا يتم استدعاء API إلا عند اختيار المزود والضغط على Generate image.
- Manual images لا تحتاج أي API أو مفتاح.
- يظهر اختبار الاتصال أو التوليد فقط عند الحاجة.
- تعرض الواجهة نسبة الصورة المطلوبة، مثل 2:3 لـ Pinterest، قبل اختيار أو توليد الصورة.

## 12. الشكل العام للتطبيق العام

يجب أن تستخدم الواجهة لغة محايدة:

```text
Create article
Article type
Content profile
SEO review
Images
Publishing
```

بدل ربط الواجهة بالطعام أو الوصفات. تظهر القوالب المتخصصة داخل Content Profiles وTemplates، ولا تفرض نفسها على المستخدم.

## 13. قرار المنتج

OrbitPress Pro هو:

```text
General-purpose professional publishing desk
+ Editorial brief engine
+ SEO audit and optimization
+ WordPress and Pinterest publishing
+ Simple conditional image providers
```

وليس:

```text
Food-only generator
+ Generic AI text box
+ Crowded settings form
+ Uncontrolled one-click publishing
```
