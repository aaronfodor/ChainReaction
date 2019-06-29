package presenter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import view.ChallengeFragment
import view.StatisticsFragment

/**
     * A pager adapter that represents 2 Fragment objects (StatisticsFragment, ChallengeFragment) in sequence
     */
    class StatisticsSlidePagerAdapter(fm: FragmentManager, statsLabel: String, challengeLabel: String) : FragmentStatePagerAdapter(fm) {

        private val statsLabel = statsLabel
        private val challengeLabel = challengeLabel

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
                0 -> return statsLabel
                1 -> return challengeLabel
            }

            return ""

        }

    }