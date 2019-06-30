package presenter

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import model.db.challenge.ChallengeLevel
import view.LevelFragment

/**
 * A pager adapter that represents all level Fragments in sequence
 */
class LevelSlidePagerAdapter(
    /**
     * MVP view
     */
    view: IStartChallengeView,
    val context: Context,
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    init{
        view.challengeDatabaseReader()
    }

    /**
     * the actual list of ChallengeLevel objects
     */
    private var challengeLevels = mutableListOf<ChallengeLevel>()

    fun setChallengeLevels(levelsList : MutableList<ChallengeLevel>){
        challengeLevels = levelsList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return challengeLevels.size
    }

    override fun getItem(position: Int): Fragment {

        if(position < 0 || position >= challengeLevels.size){
            return LevelFragment()
        }

        //show the current level fragment
        val fragment = LevelFragment()
        fragment.addLevelData(challengeLevels[position])
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return challengeLevels[position].LevelName
    }

}