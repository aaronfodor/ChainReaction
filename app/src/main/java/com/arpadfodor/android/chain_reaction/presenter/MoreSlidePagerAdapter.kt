package com.arpadfodor.android.chain_reaction.presenter

import com.arpadfodor.android.chain_reaction.view.AboutFragment
import com.arpadfodor.android.chain_reaction.view.HowToPlayFragment

/**
 * A pager adapter that represents 2 Fragment objects (HowToPlayFragment, AboutFragment) in sequence
 */
class MoreSlidePagerAdapter(fm: androidx.fragment.app.FragmentManager, howToPlayLabel: String, appInfoLabel: String) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {

    private val howToPlayLabel = howToPlayLabel
    private val appInfoLabel = appInfoLabel

    override fun getCount(): Int = 2

    override fun getItem(position: Int): androidx.fragment.app.Fragment {

        when(position){
            //show the how to play fragment
            0 -> return HowToPlayFragment()
            //show the app info fragment
            1 -> return AboutFragment()
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