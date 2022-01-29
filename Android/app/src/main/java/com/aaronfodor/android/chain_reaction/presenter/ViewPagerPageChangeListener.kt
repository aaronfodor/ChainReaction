package com.aaronfodor.android.chain_reaction.presenter

object ViewPagerPageChangeListener : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
    }
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }
    override fun onPageSelected(position: Int) {
        AudioPresenter.soundSwipe()
    }
}