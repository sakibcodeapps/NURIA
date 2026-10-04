package com.example.core.calendar

import com.example.core.model.AppLanguage
import com.example.core.model.IslamicEvent
import java.util.Calendar
import java.util.Date
import kotlin.math.floor

object HijriCalendarHelper {

    data class HijriDate(val day: Int, val month: Int, val year: Int)

    private val MONTH_NAMES_EN = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    private val MONTH_NAMES_BN = listOf(
        "মুহাররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি",
        "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শাবান",
        "রমজান", "শাওয়াল", "জিলকদ", "জিলহজ"
    )

    private val MONTH_NAMES_AR = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الثاني",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    fun getMonthName(monthNumber: Int, lang: AppLanguage): String {
        val index = (monthNumber - 1).coerceIn(0, 11)
        return when (lang) {
            AppLanguage.BANGLA -> MONTH_NAMES_BN[index]
            AppLanguage.ENGLISH -> MONTH_NAMES_EN[index]
            AppLanguage.ARABIC -> MONTH_NAMES_AR[index]
        }
    }

    fun gregorianToHijri(date: Date): HijriDate {
        val cal = Calendar.getInstance().apply { time = date }
        var y = cal.get(Calendar.YEAR)
        var m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)

        if (m <= 2) {
            y -= 1
            m += 12
        }

        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        val jd = floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + d + b - 1524.5

        val z = jd + 0.5
        val l = z - 1948440 + 10632
        val n = floor((l - 1) / 10631.0)
        val lPrime = l - 10631 * n + 354
        val j = (floor((10985 - lPrime) / 5316.0)) * (floor((50 * lPrime) / 17719.0)) +
                (floor(lPrime / 5670.0)) * (floor((43 * lPrime) / 15238.0))
        val lDoublePrime = lPrime - (floor((30 - j) / 15.0)) * (floor((17719 * j) / 50.0)) -
                (floor(j / 16.0)) * (floor((15238 * j) / 43.0)) + 29
        val hMonth = floor((24 * lDoublePrime) / 709.0).toInt()
        val hDay = (lDoublePrime - floor((709 * hMonth) / 24.0)).toInt()
        val hYear = (30 * n + j - 30).toInt()

        return HijriDate(
            day = hDay.coerceIn(1, 30),
            month = (hMonth + 1).coerceIn(1, 12),
            year = hYear
        )
    }

    fun getFormattedHijriDate(date: Date, lang: AppLanguage = AppLanguage.BANGLA): String {
        val hijri = gregorianToHijri(date)
        val monthName = getMonthName(hijri.month, lang)
        return when (lang) {
            AppLanguage.BANGLA -> "${toBanglaDigits(hijri.day)} $monthName, ${toBanglaDigits(hijri.year)} হিজরি"
            AppLanguage.ENGLISH -> "${hijri.day} $monthName ${hijri.year} AH"
            AppLanguage.ARABIC -> "${hijri.day} $monthName ${hijri.year} هـ"
        }
    }

    fun toBanglaDigits(number: Int): String {
        val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        val str = number.toString()
        val sb = StringBuilder()
        for (ch in str) {
            if (ch in '0'..'9') {
                sb.append(banglaDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    val ISLAMIC_EVENTS = listOf(
        IslamicEvent(
            id = "event_1",
            hijriDay = 1,
            hijriMonth = 1,
            titleBn = "হিজরি নববর্ষ",
            titleEn = "Islamic New Year",
            titleAr = "رأس السنة الهجرية",
            descriptionBn = "নতুন হিজরি বর্ষের প্রথম দিন। রাসুলুল্লাহ (সা.)-এর মক্কা থেকে মদিনায় হিজরতের ঐতিহাসিক স্মৃতিবাহী।",
            descriptionEn = "First day of the Islamic lunar calendar year, commemorating the Hijrah to Madinah.",
            descriptionAr = "بداية العام الهجري الجديد واستذكار هجرة النبي صلى الله عليه وسلم."
        ),
        IslamicEvent(
            id = "event_2",
            hijriDay = 10,
            hijriMonth = 1,
            titleBn = "পবিত্র আশুরা",
            titleEn = "Day of Ashura",
            titleAr = "يوم عاشوراء",
            descriptionBn = "মহিমান্বিত আশুরার দিন। মুসা (আ.) ও বনী ইসরাঈলকে ফেরাউনের কবল থেকে মুক্তির দিন এবং কারবালার মর্মান্তিক ঘটনা।",
            descriptionEn = "Tenth day of Muharram, marking the salvation of Musa (AS) and the martyrdom of Hussain (RA).",
            descriptionAr = "اليوم العاشر من شهر محرم، نجاة موسى عليه السلام وذكرى استشهاد الحسين رضي الله عنه."
        ),
        IslamicEvent(
            id = "event_3",
            hijriDay = 12,
            hijriMonth = 3,
            titleBn = "ঈদে মিলাদুন্নবী (সা.)",
            titleEn = "Mawlid an-Nabi",
            titleAr = "المولد النبوي الشريف",
            descriptionBn = "মানবতার মুক্তির দূত বিশ্বনবী হযরত মুহাম্মদ (সা.)-এর শুভাগমন দিবস।",
            descriptionEn = "Observance of the birth of the Prophet Muhammad (peace be upon him).",
            descriptionAr = "ذكرى مولد خير البرية نبينا محمد صلى الله عليه وسلم."
        ),
        IslamicEvent(
            id = "event_4",
            hijriDay = 27,
            hijriMonth = 7,
            titleBn = "শবে মেরাজ",
            titleEn = "Isra and Mi'raj",
            titleAr = "الإسراء والمعراج",
            descriptionBn = "রাসুলুল্লাহ (সা.)-এর মক্কা থেকে বায়তুল মুকাদ্দাস ও উর্ধ্বাকাশে পরিভ্রমণ এবং পাঁচ ওয়াক্ত নামাজের উপহার লাভ।",
            descriptionEn = "The miraculous Night Journey and Ascension of the Prophet Muhammad (pbuh).",
            descriptionAr = "معجزة الإسراء بالنبي إلى بيت المقدس وعروجه إلى السماوات العلى."
        ),
        IslamicEvent(
            id = "event_5",
            hijriDay = 15,
            hijriMonth = 8,
            titleBn = "শবে বরাত",
            titleEn = "Shab-e-Barat (Mid-Sha'ban)",
            titleAr = "ليلة النصف من شعبان",
            descriptionBn = "মহিমান্বিত ক্ষমার রাত ও অধিক নফল ইবাদতের বিশেষ রজনী।",
            descriptionEn = "The night of forgiveness and spiritual devotion in mid-Sha'ban.",
            descriptionAr = "ليلة النصف من شعبان، ليلة الاستغفار والعبادة والدعاء."
        ),
        IslamicEvent(
            id = "event_6",
            hijriDay = 1,
            hijriMonth = 9,
            titleBn = "পবিত্র রমজানুল মুবারক শুরু",
            titleEn = "Start of Ramadan",
            titleAr = "بداية شهر رمضان المبارك",
            descriptionBn = "রহমত, মাগফিরাত ও নাজাতের মাস এবং ফরজ সিয়াম সাধনার সূচনা।",
            descriptionEn = "First day of fasting for the blessed month of Ramadan.",
            descriptionAr = "غرة شهر رمضان المبارك، شهر الصيام والقرآن والرحمة."
        ),
        IslamicEvent(
            id = "event_7",
            hijriDay = 27,
            hijriMonth = 9,
            titleBn = "লাইলাতুল কদর",
            titleEn = "Laylat al-Qadr",
            titleAr = "ليلة القدر",
            descriptionBn = "হাজার মাসের চেয়েও উত্তম বরকতময় রজনী, যাতে পবিত্র কুরআন অবতীর্ণ হয়েছিল।",
            descriptionEn = "The Night of Decree, better than a thousand months, when the Quran was revealed.",
            descriptionAr = "ليلة مباركة خير من ألف شهر، نزل فيها القرآن الكريم."
        ),
        IslamicEvent(
            id = "event_8",
            hijriDay = 1,
            hijriMonth = 10,
            titleBn = "পবিত্র ঈদুল ফিতর",
            titleEn = "Eid ul-Fitr",
            titleAr = "عيد الفطر المبارك",
            descriptionBn = "রমজানের সিয়াম সাধনা সমাপ্তির পর বিশ্ব মুসলিমের প্রধান আনন্দ উৎসব।",
            descriptionEn = "The joyous celebration concluding the sacred month of Ramadan.",
            descriptionAr = "عيد الفطر السعيد، فرحة إتمام الصيام وجائزة العابدين."
        ),
        IslamicEvent(
            id = "event_9",
            hijriDay = 9,
            hijriMonth = 12,
            titleBn = "পবিত্র হজের দিন (আরাফাহ)",
            titleEn = "Day of Arafah",
            titleAr = "يوم عرفة",
            descriptionBn = "হজের মূল রুকন ও ক্ষমার শ্রেষ্ঠতম দিন। আরাফার ময়দানে সমবেত হওয়া ও নফল রোজা রাখা।",
            descriptionEn = "The pinnacle of the Hajj pilgrimage and a day of immense forgiveness and fasting.",
            descriptionAr = "أفضل أيام العام، ركن الحج الأعظم ويوم استجابة الدعاء ومغفرة الذنوب."
        ),
        IslamicEvent(
            id = "event_10",
            hijriDay = 10,
            hijriMonth = 12,
            titleBn = "পবিত্র ঈদুল আজহা",
            titleEn = "Eid ul-Adha",
            titleAr = "عيد الأضحى المبارك",
            descriptionBn = "আল্লাহর সন্তুষ্টির উদ্দেশ্যে সর্বোচ্চ ত্যাগের মহিমায় পশু কোরবানির মহান উৎসব।",
            descriptionEn = "Feast of the Sacrifice commemorating the devotion of Prophet Ibrahim (AS).",
            descriptionAr = "عيد النحر والتضحية، استذكاراً لامتثال نبي الله إبراهيم عليه السلام."
        )
    )
}
