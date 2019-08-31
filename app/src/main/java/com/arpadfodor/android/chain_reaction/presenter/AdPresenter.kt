package com.arpadfodor.android.chain_reaction.presenter

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

object AdPresenter{

    /**
     * Initializes the Mobile Ads SDK with an AdMob App ID
     *
     * @param    context            Context of the mobile ads to initialize with
     */
    fun initMobileAds(context: Context){
        MobileAds.initialize(context, "ca-app-pub-3940256099942544~3347511713")
    }

    /**
     * Loads the advertisement banner content
     *
     * @param    advertisement      AdView element to load
     */
    fun loadAd(advertisement: AdView){

        // Create an ad request. If you're running this on a physical device, check your logcat to
        // learn how to enable test ads for it. Look for a line like this one:
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        val adRequest = AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .build()

        // Start loading the ad in the background.
        advertisement.loadAd(adRequest)

        val adRequestMyPhone = AdRequest.Builder()
            .addTestDevice("AA90319700D9608A079CEB541F122F83")
            .build()

        advertisement.loadAd(adRequestMyPhone)

    }

}