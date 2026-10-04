package com.example.core.ads

import com.example.core.firebase.FirebaseManager
import com.example.core.model.AdsConfig
import kotlinx.coroutines.flow.StateFlow

class AdsService(private val firebaseManager: FirebaseManager) {

    val adsConfig: StateFlow<AdsConfig> = firebaseManager.adsConfigFlow

    fun isAdEnabled(): Boolean = adsConfig.value.isEnabled

    fun getBannerAdUnitId(): String = adsConfig.value.bannerAdUnitId

    fun getInterstitialAdUnitId(): String = adsConfig.value.interstitialAdUnitId

    fun canShowHomeBanner(): Boolean = isAdEnabled() && adsConfig.value.showBannerOnHome

    fun canShowDuaBanner(): Boolean = isAdEnabled() && adsConfig.value.showBannerOnDua

    fun canShowAzkarBanner(): Boolean = isAdEnabled() && adsConfig.value.showBannerOnAzkar
}
