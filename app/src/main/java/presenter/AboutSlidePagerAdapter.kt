package presenter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import view.AppInfoFragment
import view.HowToPlayFragment

/**
 * A pager adapter that represents 2 Fragment objects (HowToPlayFragment, AppInfoFragment) in sequence
 */
class AboutSlidePagerAdapter(fm: FragmentManager, howToPlayLabel: String, appInfoLabel: String) : FragmentStatePagerAdapter(fm) {

    private val howToPlayLabel = howToPlayLabel
    private val appInfoLabel = appInfoLabel

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {

        when(position){
            //show the how to play fragment
            0 -> return HowToPlayFragment()
            //show the app info fragment
            1 -> return AppInfoFragment()
        }

        return HowToPlayFragment()

    }

    override fun getPageTitle(position: Int): CharSequence {

        when(position){
            0 -> return howToPlayLabel
            1 -> return appInfoLabel
        }

        return ""

    }

}