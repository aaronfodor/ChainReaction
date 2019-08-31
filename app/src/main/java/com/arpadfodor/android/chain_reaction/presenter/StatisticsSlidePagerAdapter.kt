package com.arpadfodor.android.chain_reaction.presenter

import com.arpadfodor.android.chain_reaction.view.StatisticsChallengeFragment
import com.arpadfodor.android.chain_reaction.view.StatisticsCustomFragment

/**
 * A pager adapter that represents 2 Fragment objects (StatisticsCustomFragment, StatisticsChallengeFragment) in sequence
 */
class StatisticsSlidePagerAdapter(fm: androidx.fragment.app.FragmentManager, statsLabel: String, challengeLabel: String) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {

    private val statsLabel = statsLabel
    private val challengeLabel = challengeLabel

    override fun getCount(): Int = 2

    override fun getItem(position: Int): androidx.fragment.app.Fragment {

        when(position){
            //show the statistics fragment
            0 -> return StatisticsCustomFragment()
            //show the challenge fragment
            1 -> return StatisticsChallengeFragment()
        }

        return StatisticsCustomFragment()

    }

    override fun getPageTitle(position: Int): CharSequence {

        when(position){
            0 -> return statsLabel
            1 -> return challengeLabel
        }

        return ""

    }

}