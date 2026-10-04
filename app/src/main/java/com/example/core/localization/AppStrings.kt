package com.example.core.localization

import com.example.core.model.AppLanguage

object AppStrings {

    fun get(key: String, lang: AppLanguage): String {
        return strings[key]?.get(lang) ?: strings[key]?.get(AppLanguage.BANGLA) ?: key
    }

    private val strings = mapOf(
        // General / Nav
        "app_name" to mapOf(
            AppLanguage.BANGLA to "নুরিয়া",
            AppLanguage.ENGLISH to "NURIA",
            AppLanguage.ARABIC to "نوريا"
        ),
        "tagline" to mapOf(
            AppLanguage.BANGLA to "নামাজ • কুরআন • কিবলা",
            AppLanguage.ENGLISH to "Prayer • Quran • Qibla",
            AppLanguage.ARABIC to "صلاة • قرآن • قبلة"
        ),
        "nav_home" to mapOf(
            AppLanguage.BANGLA to "হোম",
            AppLanguage.ENGLISH to "Home",
            AppLanguage.ARABIC to "الرئيسية"
        ),
        "nav_quran" to mapOf(
            AppLanguage.BANGLA to "কুরআন",
            AppLanguage.ENGLISH to "Quran",
            AppLanguage.ARABIC to "القرآن"
        ),
        "nav_qibla" to mapOf(
            AppLanguage.BANGLA to "কিবলা",
            AppLanguage.ENGLISH to "Qibla",
            AppLanguage.ARABIC to "القبلة"
        ),
        "nav_dua" to mapOf(
            AppLanguage.BANGLA to "দুয়া",
            AppLanguage.ENGLISH to "Dua",
            AppLanguage.ARABIC to "الدعاء"
        ),
        "nav_more" to mapOf(
            AppLanguage.BANGLA to "অন্যান্য",
            AppLanguage.ENGLISH to "More",
            AppLanguage.ARABIC to "المزيد"
        ),

        // Home Screen
        "next_prayer_in" to mapOf(
            AppLanguage.BANGLA to "পরবর্তী ওয়াক্তের বাকি",
            AppLanguage.ENGLISH to "Next prayer in",
            AppLanguage.ARABIC to "الصلاة القادمة بعد"
        ),
        "today_prayers" to mapOf(
            AppLanguage.BANGLA to "আজকের নামাজের সময়সূচি",
            AppLanguage.ENGLISH to "Today's Prayer Schedule",
            AppLanguage.ARABIC to "مواقيت صلاة اليوم"
        ),
        "current_prayer" to mapOf(
            AppLanguage.BANGLA to "বর্তমান ওয়াক্ত",
            AppLanguage.ENGLISH to "Current Prayer",
            AppLanguage.ARABIC to "الوقت الحالي"
        ),
        "suhoor_ends" to mapOf(
            AppLanguage.BANGLA to "সেহরির শেষ সময়",
            AppLanguage.ENGLISH to "Suhoor Ends",
            AppLanguage.ARABIC to "نهاية السحور"
        ),
        "iftar_time" to mapOf(
            AppLanguage.BANGLA to "ইফতারের সময়",
            AppLanguage.ENGLISH to "Iftar Time",
            AppLanguage.ARABIC to "وقت الإفطار"
        ),
        "quick_shortcuts" to mapOf(
            AppLanguage.BANGLA to "প্রয়োজনীয় ফিচার",
            AppLanguage.ENGLISH to "Quick Shortcuts",
            AppLanguage.ARABIC to "الوصول السريع"
        ),
        "announcements" to mapOf(
            AppLanguage.BANGLA to "গুরুত্বপূর্ণ বার্তা ও নোটিশ",
            AppLanguage.ENGLISH to "Announcements & Reminders",
            AppLanguage.ARABIC to "الإعلانات والتذكيرات"
        ),
        "daily_inspiration" to mapOf(
            AppLanguage.BANGLA to "দৈনিক আয়াত ও হাদিস",
            AppLanguage.ENGLISH to "Daily Ayah & Hadith",
            AppLanguage.ARABIC to "آية وحديث اليوم"
        ),
        "daily_ayah_text" to mapOf(
            AppLanguage.BANGLA to "“নিশ্চয় নামাজের নির্দিষ্ট সময় মুমিনদের ওপর ফরজ।”",
            AppLanguage.ENGLISH to "“Indeed, prayer has been decreed upon the believers a decree of specified times.”",
            AppLanguage.ARABIC to "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًا مَوْقُوتًا"
        ),
        "daily_ayah_ref" to mapOf(
            AppLanguage.BANGLA to "সূরা আন-নিসা, আয়াত ১০৩",
            AppLanguage.ENGLISH to "Surah An-Nisa, Ayah 103",
            AppLanguage.ARABIC to "سورة النساء، آية ١٠٣"
        ),

        // Prayers
        "fajr" to mapOf(AppLanguage.BANGLA to "ফজর", AppLanguage.ENGLISH to "Fajr", AppLanguage.ARABIC to "الفجر"),
        "sunrise" to mapOf(AppLanguage.BANGLA to "সূর্যোদয়", AppLanguage.ENGLISH to "Sunrise", AppLanguage.ARABIC to "الشروق"),
        "dhuhr" to mapOf(AppLanguage.BANGLA to "যোহর", AppLanguage.ENGLISH to "Dhuhr", AppLanguage.ARABIC to "الظهر"),
        "asr" to mapOf(AppLanguage.BANGLA to "আসর", AppLanguage.ENGLISH to "Asr", AppLanguage.ARABIC to "العصر"),
        "maghrib" to mapOf(AppLanguage.BANGLA to "মাগরিব", AppLanguage.ENGLISH to "Maghrib", AppLanguage.ARABIC to "المغرب"),
        "isha" to mapOf(AppLanguage.BANGLA to "ইশা", AppLanguage.ENGLISH to "Isha", AppLanguage.ARABIC to "العشاء"),

        // Qibla Screen
        "qibla_compass" to mapOf(
            AppLanguage.BANGLA to "কিবলা কম্পাস",
            AppLanguage.ENGLISH to "Qibla Compass",
            AppLanguage.ARABIC to "بوصلة القبلة"
        ),
        "align_compass" to mapOf(
            AppLanguage.BANGLA to "ফোনটি সমতলে রেখে কাবার প্রতীকের সাথে মিলিয়ে নিন",
            AppLanguage.ENGLISH to "Keep device flat and rotate to align with Kaaba icon",
            AppLanguage.ARABIC to "ضع الهاتف أفقياً وقم بتدويره للمحاذاة مع الكعبة"
        ),
        "distance_to_kaaba" to mapOf(
            AppLanguage.BANGLA to "পবিত্র কাবা শরিফের দূরত্ব",
            AppLanguage.ENGLISH to "Distance to Kaaba",
            AppLanguage.ARABIC to "المسافة إلى الكعبة المشرفة"
        ),
        "qibla_angle" to mapOf(
            AppLanguage.BANGLA to "কিবলার দিককোণ",
            AppLanguage.ENGLISH to "Qibla Bearing",
            AppLanguage.ARABIC to "اتجاه القبلة"
        ),
        "current_heading" to mapOf(
            AppLanguage.BANGLA to "বর্তমান দিক",
            AppLanguage.ENGLISH to "Current Heading",
            AppLanguage.ARABIC to "الاتجاه الحالي"
        ),
        "sensor_warning" to mapOf(
            AppLanguage.BANGLA to "আপনার ডিভাইসে প্রয়োজনীয় কম্পাস সেন্সর সমর্থিত নয়।",
            AppLanguage.ENGLISH to "Your device does not support the required compass sensor.",
            AppLanguage.ARABIC to "جهازك لا يدعم مستشعر البوصلة المطلوب."
        ),
        "calibrate_tip" to mapOf(
            AppLanguage.BANGLA to "আপনার কম্পাসটি ক্যালিব্রেট করতে ফোনটি হাত দিয়ে ৮ (আট) আকৃতিতে ঘোরান।",
            AppLanguage.ENGLISH to "Please calibrate your compass by moving your phone in a figure-8 motion.",
            AppLanguage.ARABIC to "يرجى معايرة البوصلة بتحريك هاتفك على شكل رقم 8 في الهواء."
        ),

        // Quran Screen
        "holy_quran" to mapOf(
            AppLanguage.BANGLA to "আল-কুরআনুল কারীম",
            AppLanguage.ENGLISH to "The Holy Quran",
            AppLanguage.ARABIC to "القرآن الكريم"
        ),
        "search_surah" to mapOf(
            AppLanguage.BANGLA to "সূরা খুঁজুন (নাম বা নম্বর দিয়ে)...",
            AppLanguage.ENGLISH to "Search surah by name or number...",
            AppLanguage.ARABIC to "ابحث عن سورة بالاسم أو الرقم..."
        ),
        "last_read" to mapOf(
            AppLanguage.BANGLA to "সর্বশেষ পঠিত স্থান",
            AppLanguage.ENGLISH to "Last Read",
            AppLanguage.ARABIC to "آخر قراءة"
        ),
        "continue_reading" to mapOf(
            AppLanguage.BANGLA to "পড়া চালিয়ে যান",
            AppLanguage.ENGLISH to "Continue Reading",
            AppLanguage.ARABIC to "متابعة القراءة"
        ),
        "all_surahs" to mapOf(
            AppLanguage.BANGLA to "সকল সূরা",
            AppLanguage.ENGLISH to "All Surahs",
            AppLanguage.ARABIC to "جميع السور"
        ),
        "bookmarks" to mapOf(
            AppLanguage.BANGLA to "বুকমার্ক",
            AppLanguage.ENGLISH to "Bookmarks",
            AppLanguage.ARABIC to "الإشارات المرجعية"
        ),
        "verses" to mapOf(
            AppLanguage.BANGLA to "আয়াত",
            AppLanguage.ENGLISH to "Verses",
            AppLanguage.ARABIC to "آيات"
        ),
        "font_size" to mapOf(
            AppLanguage.BANGLA to "ফন্টের আকার",
            AppLanguage.ENGLISH to "Font Size",
            AppLanguage.ARABIC to "حجم الخط"
        ),
        "translation_bn" to mapOf(
            AppLanguage.BANGLA to "বাংলা অনুবাদ",
            AppLanguage.ENGLISH to "Bengali Translation",
            AppLanguage.ARABIC to "الترجمة البنغالية"
        ),
        "translation_en" to mapOf(
            AppLanguage.BANGLA to "ইংরেজি অনুবাদ",
            AppLanguage.ENGLISH to "English Translation",
            AppLanguage.ARABIC to "الترجمة الإنجليزية"
        ),
        "bismillah" to mapOf(
            AppLanguage.BANGLA to "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            AppLanguage.ENGLISH to "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            AppLanguage.ARABIC to "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
        ),

        // Dua Screen
        "hisnul_muslim" to mapOf(
            AppLanguage.BANGLA to "হিসনুল মুসলিম ও জরুরি দুয়া",
            AppLanguage.ENGLISH to "Supplications & Dua",
            AppLanguage.ARABIC to "حصن المسلم والأدعية"
        ),
        "search_dua" to mapOf(
            AppLanguage.BANGLA to "দুয়া অনুসন্ধান করুন...",
            AppLanguage.ENGLISH to "Search Duas...",
            AppLanguage.ARABIC to "ابحث في الأدعية..."
        ),
        "all_categories" to mapOf(
            AppLanguage.BANGLA to "সকল ক্যাটাগরি",
            AppLanguage.ENGLISH to "All Categories",
            AppLanguage.ARABIC to "جميع الأقسام"
        ),
        "favorites" to mapOf(
            AppLanguage.BANGLA to "পছন্দের দুয়া",
            AppLanguage.ENGLISH to "Favorites",
            AppLanguage.ARABIC to "المفضلة"
        ),
        "copy_dua" to mapOf(
            AppLanguage.BANGLA to "কপি করুন",
            AppLanguage.ENGLISH to "Copy",
            AppLanguage.ARABIC to "نسخ"
        ),
        "share_dua" to mapOf(
            AppLanguage.BANGLA to "শেয়ার করুন",
            AppLanguage.ENGLISH to "Share",
            AppLanguage.ARABIC to "مشاركة"
        ),
        "copied_clipboard" to mapOf(
            AppLanguage.BANGLA to "ক্লিপবোর্ডে কপি করা হয়েছে",
            AppLanguage.ENGLISH to "Copied to clipboard",
            AppLanguage.ARABIC to "تم النسخ إلى الحافظة"
        ),

        // Azkar Screen
        "azkar_title" to mapOf(
            AppLanguage.BANGLA to "দৈনিক আজকার ও জিকির",
            AppLanguage.ENGLISH to "Daily Azkar & Dhikr",
            AppLanguage.ARABIC to "الأذكار اليومية"
        ),
        "morning_azkar" to mapOf(
            AppLanguage.BANGLA to "সকালের আজকার",
            AppLanguage.ENGLISH to "Morning Azkar",
            AppLanguage.ARABIC to "أذكار الصباح"
        ),
        "evening_azkar" to mapOf(
            AppLanguage.BANGLA to "সন্ধ্যার আজকার",
            AppLanguage.ENGLISH to "Evening Azkar",
            AppLanguage.ARABIC to "أذكار المساء"
        ),
        "after_prayer_azkar" to mapOf(
            AppLanguage.BANGLA to "নামাজের পরের আজকার",
            AppLanguage.ENGLISH to "After Prayer Azkar",
            AppLanguage.ARABIC to "أذكار بعد الصلاة"
        ),
        "sleep_azkar" to mapOf(
            AppLanguage.BANGLA to "ঘুমানোর পূর্বে আজকার",
            AppLanguage.ENGLISH to "Sleep Azkar",
            AppLanguage.ARABIC to "أذكار النوم"
        ),
        "completed_count" to mapOf(
            AppLanguage.BANGLA to "সম্পন্ন হয়েছে! মাশাআল্লাহ",
            AppLanguage.ENGLISH to "Completed! MashaAllah",
            AppLanguage.ARABIC to "تم الإكمال! ما شاء الله"
        ),

        // Tasbih
        "tasbih_title" to mapOf(
            AppLanguage.BANGLA to "ডিজিটাল তাসবিহ",
            AppLanguage.ENGLISH to "Digital Tasbih",
            AppLanguage.ARABIC to "المسبحة الإلكترونية"
        ),
        "tap_to_count" to mapOf(
            AppLanguage.BANGLA to "গণনা করতে চাপুন",
            AppLanguage.ENGLISH to "Tap anywhere to count",
            AppLanguage.ARABIC to "اضغط للعد"
        ),
        "reset" to mapOf(
            AppLanguage.BANGLA to "রিসেট",
            AppLanguage.ENGLISH to "Reset",
            AppLanguage.ARABIC to "إعادة ضبط"
        ),
        "target" to mapOf(
            AppLanguage.BANGLA to "টার্গেট",
            AppLanguage.ENGLISH to "Target",
            AppLanguage.ARABIC to "الهدف"
        ),
        "vibration" to mapOf(
            AppLanguage.BANGLA to "ভাইব্রেশন",
            AppLanguage.ENGLISH to "Vibration",
            AppLanguage.ARABIC to "الاهتزاز"
        ),
        "sound" to mapOf(
            AppLanguage.BANGLA to "ক্লিক সাউন্ড",
            AppLanguage.ENGLISH to "Click Sound",
            AppLanguage.ARABIC to "الصوت"
        ),

        // Calendar
        "islamic_calendar" to mapOf(
            AppLanguage.BANGLA to "হিজরি ইসলামিক ক্যালেন্ডার",
            AppLanguage.ENGLISH to "Islamic Hijri Calendar",
            AppLanguage.ARABIC to "التقويم الهجري الإسلامي"
        ),
        "islamic_events" to mapOf(
            AppLanguage.BANGLA to "ইসলামিক গুরুত্বপূর্ণ দিনসমূহ",
            AppLanguage.ENGLISH to "Key Islamic Dates & Events",
            AppLanguage.ARABIC to "المناسبات والأعياد الإسلامية"
        ),

        // Fasting / Ramadan
        "fasting_title" to mapOf(
            AppLanguage.BANGLA to "রোজা ও রমজান",
            AppLanguage.ENGLISH to "Fasting & Ramadan",
            AppLanguage.ARABIC to "الصيام ورمضان"
        ),
        "ramadan_countdown" to mapOf(
            AppLanguage.BANGLA to "পবিত্র মাহে রমজানের বাকি",
            AppLanguage.ENGLISH to "Ramadan Countdown",
            AppLanguage.ARABIC to "العد التنازلي لشهر رمضان"
        ),
        "suhoor_dua" to mapOf(
            AppLanguage.BANGLA to "রোজার নিয়ত",
            AppLanguage.ENGLISH to "Intention for Fasting (Niyyah)",
            AppLanguage.ARABIC to "نية الصيام"
        ),
        "iftar_dua" to mapOf(
            AppLanguage.BANGLA to "ইফতারের দুয়া",
            AppLanguage.ENGLISH to "Dua for Breaking Fast (Iftar)",
            AppLanguage.ARABIC to "دعاء الإفطار"
        ),

        // Settings
        "settings_title" to mapOf(
            AppLanguage.BANGLA to "সেটিংস",
            AppLanguage.ENGLISH to "Settings",
            AppLanguage.ARABIC to "الإعدادات"
        ),
        "appearance" to mapOf(
            AppLanguage.BANGLA to "চেহারা ও থিম",
            AppLanguage.ENGLISH to "Appearance",
            AppLanguage.ARABIC to "المظهر"
        ),
        "language_setting" to mapOf(
            AppLanguage.BANGLA to "ভাষা নির্বাচন (Language)",
            AppLanguage.ENGLISH to "App Language",
            AppLanguage.ARABIC to "لغة التطبيق"
        ),
        "prayer_settings" to mapOf(
            AppLanguage.BANGLA to "নামাজের সময় ও হিসাব পদ্ধতি",
            AppLanguage.ENGLISH to "Prayer Calculation Settings",
            AppLanguage.ARABIC to "إعدادات حساب الصلاة"
        ),
        "calc_method" to mapOf(
            AppLanguage.BANGLA to "হিসাব পদ্ধতি",
            AppLanguage.ENGLISH to "Calculation Method",
            AppLanguage.ARABIC to "طريقة الحساب"
        ),
        "madhab" to mapOf(
            AppLanguage.BANGLA to "মাযহাব (আসর ওয়াক্ত)",
            AppLanguage.ENGLISH to "Asr Juristic Method (Madhab)",
            AppLanguage.ARABIC to "المذهب الفقهي (العصر)"
        ),
        "notifications" to mapOf(
            AppLanguage.BANGLA to "নামাজের নোটিফিকেশন ও আজান",
            AppLanguage.ENGLISH to "Prayer Reminders & Adhan",
            AppLanguage.ARABIC to "إشعارات الصلاة والأذان"
        ),
        "enable_all_notifications" to mapOf(
            AppLanguage.BANGLA to "সকল নামাজের নোটিফিকেশন চালু করুন",
            AppLanguage.ENGLISH to "Enable all prayer notifications",
            AppLanguage.ARABIC to "تفعيل إشعارات جميع الصلوات"
        ),
        "location_settings" to mapOf(
            AppLanguage.BANGLA to "অবস্থান নির্ধারণ",
            AppLanguage.ENGLISH to "Location",
            AppLanguage.ARABIC to "الموقع الجغرافي"
        ),
        "use_gps" to mapOf(
            AppLanguage.BANGLA to "স্বয়ংক্রিয় জিপিএস অবস্থান ব্যবহার করুন",
            AppLanguage.ENGLISH to "Use Automatic GPS Location",
            AppLanguage.ARABIC to "استخدام موقع GPS التلقائي"
        ),
        "select_city" to mapOf(
            AppLanguage.BANGLA to "শহর নির্বাচন করুন",
            AppLanguage.ENGLISH to "Select City Manually",
            AppLanguage.ARABIC to "اختيار المدينة يدوياً"
        ),
        "about_app" to mapOf(
            AppLanguage.BANGLA to "নুরিয়া সম্পর্কে",
            AppLanguage.ENGLISH to "About NURIA",
            AppLanguage.ARABIC to "حول تطبيق نوريا"
        ),
        "version" to mapOf(
            AppLanguage.BANGLA to "ভার্সন",
            AppLanguage.ENGLISH to "Version",
            AppLanguage.ARABIC to "الإصدار"
        ),
        "privacy_policy" to mapOf(
            AppLanguage.BANGLA to "গোপনীয়তা নীতি",
            AppLanguage.ENGLISH to "Privacy Policy",
            AppLanguage.ARABIC to "سياسة الخصوصية"
        ),
        "terms_of_service" to mapOf(
            AppLanguage.BANGLA to "ব্যবহারের শর্তাবলী",
            AppLanguage.ENGLISH to "Terms of Service",
            AppLanguage.ARABIC to "شروط الاستخدام"
        ),

        // Permissions & Onboarding
        "onboarding_welcome_title" to mapOf(
            AppLanguage.BANGLA to "নুরিয়াতে স্বাগতম",
            AppLanguage.ENGLISH to "Welcome to NURIA",
            AppLanguage.ARABIC to "أهلاً بك في نوريا"
        ),
        "onboarding_welcome_desc" to mapOf(
            AppLanguage.BANGLA to "আপনার আধুনিক ইসলামিক সঙ্গী। সঠিক সময়ে নামাজ, বিশুদ্ধ কুরআন তিলাওয়াত ও নিখুঁত কিবলা নির্দেশক।",
            AppLanguage.ENGLISH to "Your premium Islamic companion for exact prayer timings, Holy Quran, and precise Qibla direction.",
            AppLanguage.ARABIC to "رفيقك الإسلامي اليومي لأوقات الصلاة الدقيقة، والقرآن الكريم، واتجاه القبلة الموثوق."
        ),
        "onboarding_location_title" to mapOf(
            AppLanguage.BANGLA to "সঠিক নামাজের ওয়াক্তের জন্য অবস্থান",
            AppLanguage.ENGLISH to "Location for Accurate Prayer Times",
            AppLanguage.ARABIC to "الموقع لحساب دقيق لمواقيت الصلاة"
        ),
        "onboarding_location_desc" to mapOf(
            AppLanguage.BANGLA to "আপনার স্থানীয় সূর্যোদয়, সূর্যাস্ত ও কিবলার সঠিক কোণ নির্ধারণ করতে লোকেশন অনুমতি প্রয়োজন। এটি সম্পূর্ণ সুরক্ষিত এবং শুধুমাত্র এই উদ্দেশ্যেই ব্যবহৃত হয়।",
            AppLanguage.ENGLISH to "We need your location to calculate exact sunrise, sunset, and solar angles for your city. Your data stays private.",
            AppLanguage.ARABIC to "نحتاج إلى موقعك لحساب مواقيت الشروق والغروب وزاوية القبلة بدقة لمدينتك. تظل بياناتك خاصة وآمنة."
        ),
        "onboarding_notification_title" to mapOf(
            AppLanguage.BANGLA to "নামাজের সময়মতো স্মরণ করিয়ে দেওয়া",
            AppLanguage.ENGLISH to "Never Miss a Prayer",
            AppLanguage.ARABIC to "لا تفوت أي صلاة"
        ),
        "onboarding_notification_desc" to mapOf(
            AppLanguage.BANGLA to "প্রতিটি নামাজের ওয়াক্ত শুরু হলে মধুর আজান ও নোটিফিকেশনের মাধ্যমে অবহিত থাকতে অনুমতি প্রদান করুন।",
            AppLanguage.ENGLISH to "Allow notifications so NURIA can remind you on time with gentle reminders and Adhan call.",
            AppLanguage.ARABIC to "اسمح بالإشعارات ليقوم تطبيق نوريا بتذكيرك في موعد كل صلاة بالتنبيهات والأذان."
        ),
        "allow_location" to mapOf(
            AppLanguage.BANGLA to "অবস্থান অনুমতি দিন",
            AppLanguage.ENGLISH to "Allow Location Access",
            AppLanguage.ARABIC to "السماح بالوصول للموقع"
        ),
        "allow_notifications" to mapOf(
            AppLanguage.BANGLA to "নোটিফিকেশন চালু করুন",
            AppLanguage.ENGLISH to "Enable Notifications",
            AppLanguage.ARABIC to "تفعيل الإشعارات"
        ),
        "continue_btn" to mapOf(
            AppLanguage.BANGLA to "পরবর্তী",
            AppLanguage.ENGLISH to "Continue",
            AppLanguage.ARABIC to "متابعة"
        ),
        "skip" to mapOf(
            AppLanguage.BANGLA to "পরে করব",
            AppLanguage.ENGLISH to "Skip for now",
            AppLanguage.ARABIC to "تخطي الآن"
        ),
        "get_started" to mapOf(
            AppLanguage.BANGLA to "শুরু করুন",
            AppLanguage.ENGLISH to "Get Started",
            AppLanguage.ARABIC to "ابدأ الآن"
        ),

        // Common
        "save" to mapOf(AppLanguage.BANGLA to "সংরক্ষণ", AppLanguage.ENGLISH to "Save", AppLanguage.ARABIC to "حفظ"),
        "cancel" to mapOf(AppLanguage.BANGLA to "বাতিল", AppLanguage.ENGLISH to "Cancel", AppLanguage.ARABIC to "إلغاء"),
        "close" to mapOf(AppLanguage.BANGLA to "বন্ধ করুন", AppLanguage.ENGLISH to "Close", AppLanguage.ARABIC to "إغلاق"),
        "retry" to mapOf(AppLanguage.BANGLA to "পুনরায় চেষ্টা করুন", AppLanguage.ENGLISH to "Retry", AppLanguage.ARABIC to "إعادة المحاولة"),
        "no_data" to mapOf(AppLanguage.BANGLA to "কোনো তথ্য পাওয়া যায়নি", AppLanguage.ENGLISH to "No data available", AppLanguage.ARABIC to "لا توجد بيانات متاحة"),
        "offline_notice" to mapOf(
            AppLanguage.BANGLA to "আপনি বর্তমানে অফলাইনে আছেন। সংরক্ষিত তথ্য প্রদর্শিত হচ্ছে।",
            AppLanguage.ENGLISH to "You are offline. Showing cached Islamic data.",
            AppLanguage.ARABIC to "أنت في وضع عدم الاتصال. يتم عرض البيانات المحفوظة."
        ),
        "maintenance_title" to mapOf(
            AppLanguage.BANGLA to "অ্যাপ উন্নয়ন চলছে",
            AppLanguage.ENGLISH to "Scheduled Maintenance",
            AppLanguage.ARABIC to "أعمال الصيانة المجدولة"
        )
    )
}
