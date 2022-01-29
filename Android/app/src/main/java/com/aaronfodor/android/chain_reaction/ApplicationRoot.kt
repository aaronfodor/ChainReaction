package com.aaronfodor.android.chain_reaction

import android.app.Application

class ApplicationRoot : Application() {

    override fun onCreate() {
        super.onCreate()
        // Time consuming loading on a different thread
        Thread{
            val mappedByteBuffer = TFLiteService.loadModelFile(assets)
            TFLiteService.model = TFLiteService.buildModel(mappedByteBuffer, TFLiteService.NUM_THREADS)
        }.start()
    }

}