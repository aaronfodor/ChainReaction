package com.aaronfodor.android.chain_reaction.presenter

import com.aaronfodor.android.chain_reaction.model.AudioService

object ViewPagerPageChangeListener : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
    }
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }
    override fun onPageSelected(position: Int) {
        AudioService.soundSwipe()
    }
}