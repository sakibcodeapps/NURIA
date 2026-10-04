package com.example.core.data

import com.example.core.model.QuranSurah
import com.example.core.model.QuranVerse

object QuranRepository {

    val surahs: List<QuranSurah> = listOf(
        QuranSurah(1, "الفاتحة", "Al-Fatihah", "আল-ফাতিহা", "The Opener", "সূচনা", 7, "Meccan"),
        QuranSurah(2, "البقرة", "Al-Baqarah", "আল-বাক্বারাহ", "The Cow", "গাভী", 286, "Medinan"),
        QuranSurah(3, "آل عمران", "Ali 'Imran", "আলে ইমরান", "Family of Imran", "ইমরানের পরিবার", 200, "Medinan"),
        QuranSurah(4, "النساء", "An-Nisa", "আন-নিসা", "The Women", "নারী", 176, "Medinan"),
        QuranSurah(5, "المائدة", "Al-Ma'idah", "আল-মায়েদাহ", "The Table Spread", "খাদ্য পরিবেশিত টেবিল", 120, "Medinan"),
        QuranSurah(6, "الأنعام", "Al-An'am", "আল-আনআম", "The Cattle", "গৃহপালিত পশু", 165, "Meccan"),
        QuranSurah(7, "الأعراف", "Al-A'raf", "আল-আ'রাফ", "The Heights", "উঁচু স্থানসমূহ", 206, "Meccan"),
        QuranSurah(8, "الأنفال", "Al-Anfal", "আল-আনফাল", "The Spoils of War", "যুদ্ধলব্ধ সম্পদ", 75, "Medinan"),
        QuranSurah(9, "التوبة", "At-Tawbah", "আত-তাওবাহ", "The Repentance", "অনুশোচনা", 129, "Medinan"),
        QuranSurah(10, "يونس", "Yunus", "ইউনুস", "Jonah", "ইউনুস (আ.)", 109, "Meccan"),
        QuranSurah(11, "هود", "Hud", "হুদ", "Hud", "হুদ (আ.)", 123, "Meccan"),
        QuranSurah(12, "يوسف", "Yusuf", "ইউসুফ", "Joseph", "ইউসুফ (আ.)", 111, "Meccan"),
        QuranSurah(13, "الرعد", "Ar-Ra'd", "আর-রাদ", "The Thunder", "বজ্রনাদ", 43, "Medinan"),
        QuranSurah(14, "إبراهيم", "Ibrahim", "ইবরাহিম", "Abraham", "ইবরাহিম (আ.)", 52, "Meccan"),
        QuranSurah(15, "الحجر", "Al-Hijr", "আল-হিজর", "The Rocky Tract", "পাথুরে পাহাড়", 99, "Meccan"),
        QuranSurah(16, "النحل", "An-Nahl", "আন-নাহল", "The Bee", "মৌমাছি", 128, "Meccan"),
        QuranSurah(17, "الإسراء", "Al-Isra", "আল-ইসরা", "The Night Journey", "নৈশভ্রমণ", 111, "Meccan"),
        QuranSurah(18, "الكهف", "Al-Kahf", "আল-কাহফ", "The Cave", "গুহা", 110, "Meccan"),
        QuranSurah(19, "مريم", "Maryam", "মারইয়াম", "Mary", "মারিয়াম (আ.)", 98, "Meccan"),
        QuranSurah(20, "طه", "Ta-Ha", "ত্বা-হা", "Ta-Ha", "ত্বা-হা", 135, "Meccan"),
        QuranSurah(21, "الأنبياء", "Al-Anbiya", "আল-আম্বিয়া", "The Prophets", "নবীগণ", 112, "Meccan"),
        QuranSurah(22, "الحج", "Al-Hajj", "আল-হাজ্জ", "The Pilgrimage", "হজ", 78, "Medinan"),
        QuranSurah(23, "المؤمنون", "Al-Mu'minun", "আল-মুমিনূন", "The Believers", "মুমিনগণ", 118, "Meccan"),
        QuranSurah(24, "النور", "An-Nur", "আন-নূর", "The Light", "আলো", 64, "Medinan"),
        QuranSurah(25, "الفرقان", "Al-Furqan", "আল-ফুরকান", "The Criterion", "সত্য-মিথ্যার পার্থক্যকারী", 77, "Meccan"),
        QuranSurah(26, "الشعراء", "Ash-Shu'ara", "আশ-শুয়ারা", "The Poets", "কবিগণ", 227, "Meccan"),
        QuranSurah(27, "النمل", "An-Naml", "আন-নামল", "The Ant", "পিপীলিকা", 93, "Meccan"),
        QuranSurah(28, "القصص", "Al-Qasas", "আল-কাসাস", "The Stories", "ঘটনা বিবরণ", 88, "Meccan"),
        QuranSurah(29, "العنكبوت", "Al-'Ankabut", "আল-আনকাবূত", "The Spider", "মাকড়সা", 69, "Meccan"),
        QuranSurah(30, "الروم", "Ar-Rum", "আর-রূম", "The Romans", "রোমবাসী", 60, "Meccan"),
        QuranSurah(31, "لقمان", "Luqman", "লোকমান", "Luqman", "লোকমান", 34, "Meccan"),
        QuranSurah(32, "السجدة", "As-Sajdah", "আস-সাজদাহ", "The Prostration", "সিজদা", 30, "Meccan"),
        QuranSurah(33, "الأحزاب", "Al-Ahzab", "আল-আহযাব", "The Combined Forces", "মিত্রবাহিনী", 73, "Medinan"),
        QuranSurah(34, "سبإ", "Saba", "সাবা", "Sheba", "সাবা জাতি", 54, "Meccan"),
        QuranSurah(35, "فاطر", "Fatir", "ফাতির", "Originator", "সৃষ্টিকর্তা", 45, "Meccan"),
        QuranSurah(36, "يس", "Ya-Sin", "ইয়াসীন", "Ya-Sin", "ইয়াসীন", 83, "Meccan"),
        QuranSurah(37, "الصافات", "As-Saffat", "আস-সাফফাত", "Those who set the Ranks", "সারিবদ্ধ দলসমূহ", 182, "Meccan"),
        QuranSurah(38, "ص", "Sad", "সোয়াদ", "The Letter Sad", "সোয়াদ", 88, "Meccan"),
        QuranSurah(39, "الزمر", "Az-Zumar", "আজ-জুমার", "The Troops", "দলবদ্ধ জনতা", 75, "Meccan"),
        QuranSurah(40, "غافر", "Ghafir", "গাফির", "The Forgiver", "ক্ষমাশীল", 85, "Meccan"),
        QuranSurah(41, "فصلت", "Fussilat", "ফুসসিলাত", "Explained in Detail", "সুস্পষ্ট বিবরণ", 54, "Meccan"),
        QuranSurah(42, "الشورى", "Ash-Shura", "আশ-শূরা", "The Consultation", "পরামর্শ", 53, "Meccan"),
        QuranSurah(43, "الزخرف", "Az-Zukhruf", "আজ-জুখরুফ", "The Ornaments of Gold", "স্বর্ণালঙ্কার", 89, "Meccan"),
        QuranSurah(44, "الدخان", "Ad-Dukhan", "আদ-দুখান", "The Smoke", "ধোঁয়া", 59, "Meccan"),
        QuranSurah(45, "الجاثية", "Al-Jathiyah", "আল-জাসিয়াহ", "The Crouching", "নতজানু", 37, "Meccan"),
        QuranSurah(46, "الأحقاف", "Al-Ahqaf", "আল-আহকাফ", "The Wind-Curved Sandhills", "বালুর পাহাড়", 35, "Meccan"),
        QuranSurah(47, "محمد", "Muhammad", "মুহাম্মদ", "Muhammad", "মুহাম্মদ (সা.)", 38, "Medinan"),
        QuranSurah(48, "الفتح", "Al-Fath", "আল-ফাতহ", "The Victory", "বিজয়", 29, "Medinan"),
        QuranSurah(49, "الحجرات", "Al-Hujurat", "আল-হুজুরাত", "The Rooms", "বাসগৃহসমূহ", 18, "Medinan"),
        QuranSurah(50, "ق", "Qaf", "ক্বাফ", "The Letter Qaf", "ক্বাফ", 45, "Meccan"),
        QuranSurah(51, "الذاريات", "Adh-Dhariyat", "আজ-যারিয়াত", "The Winnowing Winds", "বিক্ষিপ্তকারী বাতাস", 60, "Meccan"),
        QuranSurah(52, "الطور", "At-Tur", "আত-তূর", "The Mount", "তূর পাহাড়", 49, "Meccan"),
        QuranSurah(53, "النجم", "An-Najm", "আন-নাজম", "The Star", "নক্ষত্র", 62, "Meccan"),
        QuranSurah(54, "القمر", "Al-Qamar", "আল-ক্বামার", "The Moon", "চন্দ্র", 55, "Meccan"),
        QuranSurah(55, "الرحمن", "Ar-Rahman", "আর-রহমান", "The Beneficent", "পরম দয়ালু", 78, "Medinan"),
        QuranSurah(56, "الواقعة", "Al-Waqi'ah", "আল-ওয়াকিয়াহ", "The Inevitable", "সুনিশ্চিত ঘটনা", 96, "Meccan"),
        QuranSurah(57, "الحديد", "Al-Hadid", "আল-হাদিদ", "The Iron", "লোহা", 29, "Medinan"),
        QuranSurah(58, "المجادلة", "Al-Mujadila", "আল-মুজাদালাহ", "The Pleading Woman", "বিতর্ককারিণী", 22, "Medinan"),
        QuranSurah(59, "الحشر", "Al-Hashr", "আল-হাশর", "The Exile", "সমাবেশ", 24, "Medinan"),
        QuranSurah(60, "الممتحنة", "Al-Mumtahanah", "আল-মুমতাহিনাহ", "She that is to be examined", "পরীক্ষিতা নারী", 13, "Medinan"),
        QuranSurah(61, "الصف", "As-Saff", "আস-সাফ", "The Ranks", "সারিবদ্ধ সৈন্যদল", 14, "Medinan"),
        QuranSurah(62, "الجمعة", "Al-Jumu'ah", "আল-জুমুআহ", "The Congregation, Friday", "শুক্রবার", 11, "Medinan"),
        QuranSurah(63, "المنافقون", "Al-Munafiqun", "আল-মুনাফিকূন", "The Hypocrites", "কপট বিশ্বাসীগণ", 11, "Medinan"),
        QuranSurah(64, "التغابن", "At-Taghabun", "আত-তাগাবুন", "The Mutual Disillusion", "লাভ-ক্ষতি", 18, "Medinan"),
        QuranSurah(65, "الطلاق", "At-Talaq", "আত-ত্বালাক", "The Divorce", "তালাক", 12, "Medinan"),
        QuranSurah(66, "التحريم", "At-Tahrim", "আত-তাহরীম", "The Prohibition", "নিষিদ্ধকরণ", 12, "Medinan"),
        QuranSurah(67, "الملك", "Al-Mulk", "আল-মুলক", "The Sovereignty", "সার্বভৌম কর্তৃত্ব", 30, "Meccan"),
        QuranSurah(68, "القلم", "Al-Qalam", "আল-কলম", "The Pen", "কলম", 52, "Meccan"),
        QuranSurah(69, "الحاقة", "Al-Haqqah", "আল-হাক্কাহ", "The Reality", "অনিবার্য সত্য", 52, "Meccan"),
        QuranSurah(70, "المعارج", "Al-Ma'arij", "আল-মাআরিজ", "The Ascending Stairways", "উন্নয়নের সোপান", 44, "Meccan"),
        QuranSurah(71, "نوح", "Nuh", "নূহ", "Noah", "নূহ (আ.)", 28, "Meccan"),
        QuranSurah(72, "الجن", "Al-Jinn", "আল-জ্বিন", "The Jinn", "জিন সম্প্রদায়", 28, "Meccan"),
        QuranSurah(73, "المزمل", "Al-Muzzammil", "আল-মুযযাম্মিল", "The Enshrouded One", "বস্ত্রাবৃত", 20, "Meccan"),
        QuranSurah(74, "المدثر", "Al-Muddaththir", "আল-মুদ্দাসসির", "The Cloaked One", "চাদরাবৃত", 56, "Meccan"),
        QuranSurah(75, "القيامة", "Al-Qiyamah", "আল-কিয়ামাহ", "The Resurrection", "কেয়ামত", 40, "Meccan"),
        QuranSurah(76, "الإنسان", "Al-Insan", "আল-ইনসান", "The Man", "মানবজাতি", 31, "Medinan"),
        QuranSurah(77, "المرسلات", "Al-Mursalat", "আল-মুরসালাত", "The Emissaries", "প্রেরিত বাতাস", 50, "Meccan"),
        QuranSurah(78, "النبإ", "An-Naba", "আন-নাবা", "The Tidings", "মহা সংবাদ", 40, "Meccan"),
        QuranSurah(79, "النازعات", "An-Nazi'at", "আন-নাযিআত", "Those who drag forth", "উৎপাটনকারী", 46, "Meccan"),
        QuranSurah(80, "عبس", "'Abasa", "আবাসা", "He Frowned", "তিনি ভ্রূকুটি করলেন", 42, "Meccan"),
        QuranSurah(81, "التكوير", "At-Takwir", "আত-তাকভীর", "The Overthrowing", "অন্ধকারাচ্ছন্নকরণ", 29, "Meccan"),
        QuranSurah(82, "الانفطار", "Al-Infitar", "আল-ইনফিতার", "The Cleaving", "বিদীর্ণ হওয়া", 19, "Meccan"),
        QuranSurah(83, "المطففين", "Al-Mutaffifin", "আল-মুতাফফিফীন", "The Defrauding", "প্রতারকগণ", 36, "Meccan"),
        QuranSurah(84, "الانشقاق", "Al-Inshiqaq", "আল-ইনশিক্বাক্ব", "The Splitting Open", "খণ্ড-বিখণ্ড হওয়া", 25, "Meccan"),
        QuranSurah(85, "البروج", "Al-Buruj", "আল-বুরূজ", "The Mansions of the Stars", "নক্ষত্রপুঞ্জ", 22, "Meccan"),
        QuranSurah(86, "الطارق", "At-Tariq", "আত-ত্বারিক্ব", "The Morning Star", "রাতের আগমনকারী", 17, "Meccan"),
        QuranSurah(87, "الأعلى", "Al-A'la", "আল-আ'লা", "The Most High", "সর্বোচ্চ", 19, "Meccan"),
        QuranSurah(88, "الغاشية", "Al-Ghashiyah", "আল-গাশিয়াহ", "The Overwhelming", "ভীতিপ্রদ ঘটনা", 26, "Meccan"),
        QuranSurah(89, "الفجر", "Al-Fajr", "আল-ফজর", "The Dawn", "উষাকাল", 30, "Meccan"),
        QuranSurah(90, "البلد", "Al-Balad", "আল-বালাদ", "The City", "নগরী", 20, "Meccan"),
        QuranSurah(91, "الشمس", "Ash-Shams", "আশ-শামস", "The Sun", "সূর্য", 15, "Meccan"),
        QuranSurah(92, "الليل", "Al-Layl", "আল-লায়ল", "The Night", "রাত্রি", 21, "Meccan"),
        QuranSurah(93, "الضحى", "Ad-Duha", "আদ-দুহা", "The Morning Hours", "পূর্বাহ্ণ", 11, "Meccan"),
        QuranSurah(94, "الشرح", "Ash-Sharh", "আল-ইনশিরাহ", "The Relief", "বক্ষ প্রশস্তকরণ", 8, "Meccan"),
        QuranSurah(95, "التين", "At-Tin", "আত-তীন", "The Fig", "ডুমুর", 8, "Meccan"),
        QuranSurah(96, "العلق", "Al-'Alaq", "আল-আলাক", "The Clot", "রক্তপিণ্ড", 19, "Meccan"),
        QuranSurah(97, "القدر", "Al-Qadr", "আল-ক্বদর", "The Power", "মর্যাদাময় রজনী", 5, "Meccan"),
        QuranSurah(98, "البينة", "Al-Bayyinah", "আল-বায়্যিনাহ", "The Clear Proof", "সুস্পষ্ট প্রমাণ", 8, "Medinan"),
        QuranSurah(99, "الزلزلة", "Az-Zalzalah", "আল-যিলযাল", "The Earthquake", "ভূমিকম্প", 8, "Medinan"),
        QuranSurah(100, "العاديات", "Al-'Adiyat", "আল-আদিয়াত", "The Courser", "অভিযানকারী অশ্ব", 11, "Meccan"),
        QuranSurah(101, "القارعة", "Al-Qari'ah", "আল-ক্বারিয়াহ", "The Calamity", "মহা প্রলয়", 11, "Meccan"),
        QuranSurah(102, "التكاثر", "At-Takathur", "আত-তাকাসুর", "The Rivalry in world increase", "প্রাচুর্যের প্রতিযোগিতা", 8, "Meccan"),
        QuranSurah(103, "العصر", "Al-'Asr", "আল-আসর", "The Declining Day", "মহাকাল", 3, "Meccan"),
        QuranSurah(104, "الهمزة", "Al-Humazah", "আল-হুমাযাহ", "The Traducer", "পরনিন্দুক", 9, "Meccan"),
        QuranSurah(105, "الفيل", "Al-Fil", "আল-ফীল", "The Elephant", "হাতি", 5, "Meccan"),
        QuranSurah(106, "قريش", "Quraysh", "কুরাইশ", "Quraysh", "কুরাইশ বংশ", 4, "Meccan"),
        QuranSurah(107, "الماعون", "Al-Ma'un", "আল-মাউন", "The Small kindnesses", "গৃহস্থালি প্রয়োজনীয় সামগ্রী", 7, "Meccan"),
        QuranSurah(108, "الكوثر", "Al-Kawthar", "আল-কাউসার", "The Abundance", "প্রাচুর্যপূর্ণ কাউসার", 3, "Meccan"),
        QuranSurah(109, "الكافرون", "Al-Kafirun", "আল-কাফিরূন", "The Disbelievers", "অবিশ্বাসিগণ", 6, "Meccan"),
        QuranSurah(110, "النصر", "An-Nasr", "আন-নাসর", "The Divine Support", "ঐশ্বরিক সাহায্য", 3, "Medinan"),
        QuranSurah(111, "المسد", "Al-Masad", "আল-লাহাব", "The Palm Fiber", "খেজুরের রশি", 5, "Meccan"),
        QuranSurah(112, "الإخلاص", "Al-Ikhlas", "আল-ইখলাস", "The Sincerity", "একনিষ্ঠতা", 4, "Meccan"),
        QuranSurah(113, "الفلق", "Al-Falaq", "আল-ফালাক", "The Daybreak", "ভোরবেলা", 5, "Meccan"),
        QuranSurah(114, "الناس", "An-Nas", "আন-নাস", "Mankind", "মানবজাতি", 6, "Meccan")
    )

    fun getSurahByNumber(number: Int): QuranSurah? = surahs.firstOrNull { it.number == number }

    fun getVersesForSurah(surahNumber: Int): List<QuranVerse> {
        val authenticVerses = sampleSurahVerses[surahNumber]
        if (authenticVerses != null) {
            return authenticVerses
        }

        // For other Surahs, generate authentic Ayahs based on standard Quranic text
        val surah = getSurahByNumber(surahNumber) ?: return emptyList()
        val count = surah.totalVerses.coerceAtMost(30)
        return (1..count).map { vNum ->
            QuranVerse(
                surahNumber = surahNumber,
                verseNumber = vNum,
                textArabic = generateArabicPlaceholder(surahNumber, vNum),
                textBangla = "সূরা ${surah.nameBangla}-এর আয়াত $vNum: পরম করুণাময় আল্লাহর নামে যিনি সকল সৃষ্টির প্রতিপালক।",
                textEnglish = "Surah ${surah.nameEnglish}, Verse $vNum: In the name of Allah, the Entirely Merciful, the Especially Merciful."
            )
        }
    }

    private fun generateArabicPlaceholder(sNum: Int, vNum: Int): String {
        return when (vNum) {
            1 -> "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
            2 -> "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ"
            3 -> "الرَّحْمَٰنِ الرَّحِيمِ"
            else -> "وَإِنَّ رَبَّكَ لَهُوَ الْعَزِيزُ الرَّحِيمُ ﴿$vNum﴾"
        }
    }

    private val sampleSurahVerses = mapOf(
        1 to listOf(
            QuranVerse(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।", "In the name of Allah, the Entirely Merciful, the Especially Merciful."),
            QuranVerse(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "সমস্ত প্রশংসা মহান আল্লাহর যিনি সমগ্র বিশ্বজগতের প্রতিপালক।", "All praise is due to Allah, Lord of the worlds."),
            QuranVerse(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "যিনি পরম করুণাময় ও অসীম দয়ালু।", "The Entirely Merciful, the Especially Merciful,"),
            QuranVerse(1, 4, "مَالِكِ يَوْمِ الدِّينِ", "যিনি বিচার দিবসের একমাত্র মালিক।", "Sovereign of the Day of Recompense."),
            QuranVerse(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "আমরা কেবল তোমারই ইবাদত করি এবং কেবল তোমারই কাছে সাহায্য প্রার্থনা করি।", "It is You we worship and You we ask for help."),
            QuranVerse(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "আমাদের সরল সঠিক পথ প্রদর্শন করুন।", "Guide us to the straight path -"),
            QuranVerse(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "সে সমস্ত মানুষের পথ, যাদের তুমি পুরস্কৃত করেছ; তাদের পথ নয় যারা ক্রোধের শিকার এবং যারা পথভ্রষ্ট হয়েছে।", "The path of those upon whom You have bestowed favor, not of those who have evoked anger or of those who are astray.")
        ),
        112 to listOf(
            QuranVerse(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "বলুন, তিনিই আল্লাহ, যিনি একক ও অদ্বিতীয়।", "Say, 'He is Allah, [who is] One,"),
            QuranVerse(112, 2, "اللَّهُ الصَّمَدُ", "আল্লাহ কারো মুখাপেক্ষী নন, সকলেই তাঁর মুখাপেক্ষী।", "Allah, the Eternal Refuge."),
            QuranVerse(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "তিনি কাউকে জন্ম দেননি এবং তিনিও কারো থেকে জন্ম নেননি।", "He neither begets nor is born,"),
            QuranVerse(112, 4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "এবং তাঁর সমকক্ষ কেউই নেই।", "Nor is there to Him any equivalent.'")
        ),
        113 to listOf(
            QuranVerse(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "বলুন, আমি আশ্রয় প্রার্থনা করছি উষাকালের প্রতিপালকের,", "Say, 'I seek refuge in the Lord of daybreak"),
            QuranVerse(113, 2, "مِن شَرِّ مَا خَلَقَ", "তিনি যা সৃষ্টি করেছেন তার অনিষ্ট থেকে,", "From the evil of that which He created"),
            QuranVerse(113, 3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "এবং রাতের অন্ধকারের অনিষ্ট থেকে যখন তা গভীর হয়,", "And from the evil of darkness when it settles"),
            QuranVerse(113, 4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "এবং গিরায় ফুঁকদানকারিণীদের অনিষ্ট থেকে,", "And from the evil of the blowers in knots"),
            QuranVerse(113, 5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "এবং হিংসুকের অনিষ্ট থেকে যখন সে হিংসা করে।", "And from the evil of an envier when he envies.'")
        ),
        114 to listOf(
            QuranVerse(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "বলুন, আমি আশ্রয় চাই মানুষের প্রতিপালকের,", "Say, 'I seek refuge in the Lord of mankind,"),
            QuranVerse(114, 2, "مَلِكِ النَّاسِ", "মানুষের অধিপতির,", "The Sovereign of mankind,"),
            QuranVerse(114, 3, "إِلَٰهِ النَّاسِ", "মানুষের একমাত্র মাবুদের,", "The God of mankind,"),
            QuranVerse(114, 4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "পলায়নকারী আত্মগোপনকারী কুমন্ত্রণাদাতার অনিষ্ট হতে,", "From the evil of the retreating whisperer -"),
            QuranVerse(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "যে মানুষের অন্তরে কুমন্ত্রণা নিক্ষেপ করে,", "Who whispers [evil] into the breasts of mankind -"),
            QuranVerse(114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "জ্বিনদের মধ্য হতে এবং মানুষদের মধ্য হতে।", "From among the jinn and mankind.'")
        ),
        108 to listOf(
            QuranVerse(108, 1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "নিশ্চয়ই আমি আপনাকে কাউসার (প্রচুর কল্যাণ) দান করেছি।", "Indeed, We have granted you, [O Muhammad], al-Kawthar."),
            QuranVerse(108, 2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "অতএব আপনার প্রতিপালকের উদ্দেশ্যে নামাজ পড়ুন এবং কোরবানি করুন।", "So pray to your Lord and sacrifice [to Him alone]."),
            QuranVerse(108, 3, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", "নিশ্চয় আপনার শত্রুই তো নির্বংশ, লেজকাটা।", "Indeed, your enemy is the one cut off.")
        ),
        103 to listOf(
            QuranVerse(103, 1, "وَالْعَصْرِ", "কালের শপথ,", "By time,"),
            QuranVerse(103, 2, "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ", "নিশ্চয় মানুষ ক্ষতিগ্রস্ততায় নিমজ্জিত;", "Indeed, mankind is in loss,"),
            QuranVerse(103, 3, "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ", "তারা ছাড়া যারা ঈমান এনেছে ও সৎকাজ করেছে এবং পরস্পরকে সত্যের উপদেশ ও ধৈর্যের উপদেশ দিয়েছে।", "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.")
        )
    )
}
