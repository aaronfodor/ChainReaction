package com.aaronfodor.android.chain_reaction

import android.app.Application
import com.aaronfodor.android.chain_reaction.model.ai.TFLiteService
import com.aaronfodor.android.chain_reaction.model.AdService
import com.aaronfodor.android.chain_reaction.model.AudioService

class ApplicationRoot : Application() {

    override fun onCreate() {
        super.onCreate()

        //initializing the global presenter objects
        AudioService.init(applicationContext)
        AdService.initMobileAds(applicationContext)

        // Time consuming loading on a different thread
        Thread{
            val mappedByteBuffer = TFLiteService.loadModelFile(assets)
            TFLiteService.model = TFLiteService.buildModel(mappedByteBuffer, TFLiteService.NUM_THREADS)
        }.start()
    }

}