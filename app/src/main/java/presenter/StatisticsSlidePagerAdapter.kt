package presenter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import view.ChallengeFragment
import view.StatisticsFragment

/**
     * A pager adapter that represents 2 Fragment objects (StatisticsFragment, ChallengeFragment) in sequence
     */
    class StatisticsSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getCount(): Int = 2

        override fun getItem(position: Int): Fragment {

            when(position){
                //show the statistics fragment
                0 -> return StatisticsFragment()
                //show the challenge fragment
                1 -> return ChallengeFragment()
            }

            return StatisticsFragment()

        }

        override fun getPageTitle(position: Int): CharSequence {

            when(position){
                0 -> return "Statistics"
                1 -> return "Challenge"
            }

            return ""

        }

    }