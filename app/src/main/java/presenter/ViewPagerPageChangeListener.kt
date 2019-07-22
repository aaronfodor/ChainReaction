package presenter

import android.support.v4.view.ViewPager

object ViewPagerPageChangeListener : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
    }
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }
    override fun onPageSelected(position: Int) {
        AudioPresenter.soundSwipe()
    }
}