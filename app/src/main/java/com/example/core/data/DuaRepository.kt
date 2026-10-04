package com.example.core.data

import com.example.core.model.DuaCategory
import com.example.core.model.DuaItem

object DuaRepository {

    val categories: List<DuaCategory> = listOf(
        DuaCategory("daily", "Daily Life", "দৈনন্দিন জীবন", "الحياة اليومية", "wb_sunny"),
        DuaCategory("salah", "Salah & Adhan", "নামাজ ও আজান", "الصلاة والأذان", "mosque"),
        DuaCategory("forgiveness", "Forgiveness (Tawbah)", "ক্ষমা ও তাওবা", "الاستغفار والتوبة", "favorite"),
        DuaCategory("protection", "Protection & Distress", "বিপদ ও নিরাপত্তা", "الحماية والشدائد", "shield"),
        DuaCategory("morning_evening", "Morning & Evening", "সকাল-সন্ধ্যা", "الصباح والمساء", "nights_stay"),
        DuaCategory("fasting", "Fasting & Ramadan", "রোজা ও রমজান", "الصيام ورمضان", "water_drop"),
        DuaCategory("travel", "Travel & Journeys", "সফর ও যাত্রা", "السفر", "flight"),
        DuaCategory("health", "Health & Healing", "রোগমুক্তি ও সুস্থতা", "الشفاء والصحة", "healing")
    )

    val duas: List<DuaItem> = listOf(
        DuaItem(
            id = "dua_sleep_waking",
            categoryId = "daily",
            titleBn = "ঘুম থেকে ওঠার পর দুয়া",
            titleEn = "Upon waking up from sleep",
            titleAr = "دعاء الاستيقاظ من النوم",
            contentAr = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
            contentBn = "সমস্ত প্রশংসা আল্লাহর জন্য, যিনি আমাদের মৃত্যুর (ঘুমের) পর পুনর্জীবিত করলেন এবং তাঁরই কাছে সবার পুনরুত্থান।",
            contentEn = "All praise is for Allah who gave us life after having taken it from us and unto Him is the resurrection.",
            reference = "সহীহ বুখারী ৬৩১২, সহীহ মুসলিম ২৭১১"
        ),
        DuaItem(
            id = "dua_sleeping",
            categoryId = "daily",
            titleBn = "ঘুমানোর পূর্বে দুয়া",
            titleEn = "Before going to sleep",
            titleAr = "دعاء النوم",
            contentAr = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
            contentBn = "হে আল্লাহ! আপনার নামেই আমি মৃত্যুবরণ করি (ঘুমাই) এবং আপনার নামেই জীবন ধারণ করি (জাগ্রত হই)।",
            contentEn = "In Your name, O Allah, I die and I live.",
            reference = "সহীহ বুখারী ৬৩২৪"
        ),
        DuaItem(
            id = "dua_eating_start",
            categoryId = "daily",
            titleBn = "খাবার শুরুর দুয়া",
            titleEn = "Before eating meals",
            titleAr = "الدعاء قبل الأكل",
            contentAr = "بِسْمِ اللَّهِ",
            contentBn = "আল্লাহর নামে শুরু করছি। (ভুলে গেলে: 'বিসমিল্লাহি আউওয়ালাহু ওয়া আখিরাহু')",
            contentEn = "In the name of Allah. (If forgotten: In the name of Allah at its beginning and end).",
            reference = "সুনানে আবু দাউদ ৩৭৬৭, তিরমিযী ১৮৫৮"
        ),
        DuaItem(
            id = "dua_eating_end",
            categoryId = "daily",
            titleBn = "খাবার শেষের দুয়া",
            titleEn = "After finishing meals",
            titleAr = "الدعاء بعد الفراغ من الطعام",
            contentAr = "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنِي هَٰذَا وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍ",
            contentBn = "সমস্ত প্রশংসা আল্লাহর জন্য যিনি আমাকে এটি খাওয়ালেন এবং আমার কোনো শক্তি ও সামর্থ্য ছাড়াই এটি দান করলেন।",
            contentEn = "Praise belongs to Allah who fed me this and provided it for me without any power or strength on my part.",
            reference = "সুনানে তিরমিযী ৩৪৫৮, আবু দাউদ ৪০২৩"
        ),
        DuaItem(
            id = "dua_home_exit",
            categoryId = "daily",
            titleBn = "ঘর থেকে বের হওয়ার দুয়া",
            titleEn = "When leaving the house",
            titleAr = "الدعاء عند الخروج من المنزل",
            contentAr = "بِسْمِ اللَّهِ ، تَوَكَّلْتُ عَلَى اللَّهِ ، وَلا حَوْلَ وَلا قُوَّةَ إِلاَّ بِاللَّهِ",
            contentBn = "আল্লাহর নামে বের হচ্ছি, আল্লাহর ওপর ভরসা করলাম। আল্লাহর সাহায্য ছাড়া কোনো অনিষ্ট প্রতিহত করার বা সৎকাজ করার শক্তি নেই।",
            contentEn = "In the name of Allah, I trust in Allah; there is no might and no power but in Allah.",
            reference = "সুনানে আবু দাউদ ৫০৯৫, তিরমিযী ৩৪২৬"
        ),
        DuaItem(
            id = "dua_mosque_enter",
            categoryId = "salah",
            titleBn = "মসজিদে প্রবেশের দুয়া",
            titleEn = "Entering the mosque",
            titleAr = "دعاء دخول المسجد",
            contentAr = "اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ",
            contentBn = "হে আল্লাহ! আমার জন্য আপনার রহমতের দরজাসমূহ উন্মুক্ত করে দিন।",
            contentEn = "O Allah, open the gates of Your mercy for me.",
            reference = "সহীহ মুসলিম ৭১৩"
        ),
        DuaItem(
            id = "dua_mosque_exit",
            categoryId = "salah",
            titleBn = "মসজিদ থেকে বের হওয়ার দুয়া",
            titleEn = "Leaving the mosque",
            titleAr = "دعاء الخروج من المسجد",
            contentAr = "اللَّهُمَّ إِنِّي أَسْأَلُكَ مِنْ فَضْلِكَ",
            contentBn = "হে আল্লাহ! আমি আপনার অনুগ্রহ প্রার্থনা করছি।",
            contentEn = "O Allah, I ask You from Your favor and grace.",
            reference = "সহীহ মুসলিম ৭১৩"
        ),
        DuaItem(
            id = "dua_sayyidul_istighfar",
            categoryId = "forgiveness",
            titleBn = "সাইয়েদুল ইস্তিগফার (শ্রেষ্ঠ ক্ষমা প্রার্থনা)",
            titleEn = "Sayyid al-Istighfar (Chief of Prayers for Forgiveness)",
            titleAr = "سيد الاستغفار",
            contentAr = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ",
            contentBn = "হে আল্লাহ! আপনি আমার রব, আপনি ছাড়া কোনো সত্য উপাস্য নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা। আমি আমার সাধ্য অনুযায়ী আপনার অঙ্গীকার ও প্রতিশ্রুতির ওপর আছি। আমি আমার কৃতকর্মের অনিষ্ট থেকে আপনার কাছে আশ্রয় চাই। আমার ওপর আপনার যে নিয়ামত রয়েছে তা স্বীকার করছি এবং আমার গুনাহের কথা স্বীকার করছি। অতএব আপনি আমাকে ক্ষমা করুন, কেননা আপনি ছাড়া আর কেউ গুনাহ ক্ষমা করতে পারে না।",
            contentEn = "O Allah, You are my Lord, none has the right to be worshiped but You. You created me and I am Your servant, and I adhere to Your covenant and promise as best as I can. I seek refuge in You from the evil of what I have done. I acknowledge Your blessing upon me, and I acknowledge my sin. So forgive me, for none forgives sins but You.",
            reference = "সহীহ বুখারী ৬৩০৬"
        ),
        DuaItem(
            id = "dua_distress_yunus",
            categoryId = "protection",
            titleBn = "ইউনুস (আ.)-এর দুয়া (বিপদ মুক্তির শ্রেষ্ঠ দুয়া)",
            titleEn = "Dua of Prophet Yunus (In distress)",
            titleAr = "دعاء يونس عليه السلام (دعاء الكرب)",
            contentAr = "لَا إِلَٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ",
            contentBn = "আপনি ছাড়া কোনো সত্য উপাস্য নেই, আপনি পরম পবিত্র! নিশ্চয়ই আমি অপরাধীদের অন্তর্ভুক্ত হয়ে গেছি।",
            contentEn = "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.",
            reference = "সূরা আল-আম্বিয়া, আয়াত ৮৭; জামে তিরমিযী ৩৫০৫"
        ),
        DuaItem(
            id = "dua_anxiety_debt",
            categoryId = "protection",
            titleBn = "উদ্বেগ, পেরেশানি ও ঋণমুক্তির দুয়া",
            titleEn = "Relief from anxiety, sorrow, and debt",
            titleAr = "دعاء الهم والحزن والديْن",
            contentAr = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ، وَغَلَبَةِ الرِّجَالِ",
            contentBn = "হে আল্লাহ! নিশ্চয়ই আমি আপনার আশ্রয় নিচ্ছি চিন্তা-ভাবনা ও শোক-দুঃখ থেকে, অপারগতা ও অলসতা থেকে, কৃপণতা ও কাপুরুষতা থেকে, ঋণের বোঝা এবং মানুষের জুলুম ও আধিপত্য থেকে।",
            contentEn = "O Allah, I seek refuge in You from grief and sadness, from weakness and laziness, from miserliness and cowardice, from being overcome by debt and from being overpowered by men.",
            reference = "সহীহ বুখারী ২৮৯৩"
        ),
        DuaItem(
            id = "dua_iftar",
            categoryId = "fasting",
            titleBn = "ইফতারের দুয়া",
            titleEn = "Dua for breaking the fast (Iftar)",
            titleAr = "دعاء الإفطار",
            contentAr = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
            contentBn = "পিপাসা নিবৃত্ত হলো, শিরা-উপশিরা সিক্ত হলো এবং আল্লাহর ইচ্ছায় সওয়াব নিশ্চিত হলো।",
            contentEn = "The thirst is gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
            reference = "সুনানে আবু দাউদ ২৩৫৭"
        ),
        DuaItem(
            id = "dua_travel",
            categoryId = "travel",
            titleBn = "বাহনে আরোহণ ও সফরের দুয়া",
            titleEn = "Dua for embarking on travel",
            titleAr = "دعاء ركوب الدابة والسفر",
            contentAr = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنْقَلِبُونَ",
            contentBn = "পবিত্র সেই সত্তা যিনি এটিকে আমাদের বশীভূত করে দিয়েছেন, অথচ আমরা একে বশীভূত করতে সক্ষম ছিলাম না। এবং আমরা আমাদের প্রতিপালকের দিকেই প্রত্যাবর্তনকারী।",
            contentEn = "Glory to Him who has brought this under our control, though we were unable to subdue it by ourselves. And indeed, to our Lord we will surely return.",
            reference = "সূরা যুখরুফ, আয়াত ১৩-১৪; সহীহ মুসলিম ১৩৪২"
        )
    )
}
