package com.aaronfodor.android.chain_reaction.presenter

import android.content.Context
import com.aaronfodor.android.chain_reaction.model.db.challenge.ChallengeLevel
import com.aaronfodor.android.chain_reaction.view.LevelFragment

/**
 * A pager adapter that represents all level Fragments in sequence
 */
class LevelSlidePagerAdapter(
    /**
     * MVP hu.bme.aut.android.chain_reaction.view
     */
    levels: MutableList<ChallengeLevel>,
    val context: Context,
    fm: androidx.fragment.app.FragmentManager
) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {

    /**
     * the actual list of ChallengeLevel objects
     */
    private var challengeLevels = mutableListOf<ChallengeLevel>()

    init{
        challengeLevels = levels
    }

    override fun getCount(): Int {
        return challengeLevels.size
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        val fragment = LevelFragment()
        fragment.addLevelData(challengeLevels[position])
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return challengeLevels[position].LevelName
    }

}