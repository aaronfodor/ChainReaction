package presenter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import model.db.challenge.ChallengeLevel
import view.LevelFragment

/**
 * A pager adapter that represents all level Fragments in sequence
 */
class LevelSlidePagerAdapter(
    /**
     * MVP view
     */
    levels: MutableList<ChallengeLevel>,
    val context: Context,
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

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

    override fun getItem(position: Int): Fragment {
        val fragment = LevelFragment()
        fragment.addLevelData(challengeLevels[position])
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return challengeLevels[position].LevelName
    }

}