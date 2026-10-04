package com.example.core.data

import com.example.core.model.TasbihPreset

object TasbihRepository {

    val defaultPresets: List<TasbihPreset> = listOf(
        TasbihPreset("preset_subhanallah", "سُبْحَانَ اللَّهِ", "সুবহানাল্লাহ", "SubhanAllah", 33),
        TasbihPreset("preset_alhamdulillah", "الْحَمْدُ لِلَّهِ", "আলহামদুলিল্লাহ", "Alhamdulillah", 33),
        TasbihPreset("preset_allahuakbar", "اللَّهُ أَكْبَرُ", "আল্লাহু আকবার", "Allahu Akbar", 34),
        TasbihPreset("preset_lailahaillallah", "لَا إِلَٰهَ إِلَّا اللَّهُ", "লা ইলাহা ইল্লাল্লাহ", "La ilaha illallah", 100),
        TasbihPreset("preset_astaghfirullah", "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ", "আস্তাগফিরুল্লাহ", "Astaghfirullah", 100),
        TasbihPreset("preset_salawat", "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ", "দরুদ শরিফ", "Salawat", 100),
        TasbihPreset("preset_lahawla", "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ", "লা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহ", "La hawla wa la quwwata", 33)
    )
}
