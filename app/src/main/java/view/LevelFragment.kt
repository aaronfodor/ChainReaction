package view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import hu.bme.aut.android.chainreaction.R
import kotlinx.android.synthetic.main.fragment_level.*
import model.db.challenge.ChallengeLevel
import presenter.IStartLevelView
import presenter.LevelPresenter

class LevelFragment: Fragment(), IStartLevelView {

    companion object {
        private const val CHALLENGE_GAME = 2

        private const val NORMAL_MODE = 1
        private const val DYNAMIC_MODE = 2
    }

    /**
     * presenter of the view
     */
    private lateinit var presenter: LevelPresenter

    private var myView: View? = null

    private var level = ChallengeLevel()

    private var gameType = CHALLENGE_GAME
    private var gameMode = NORMAL_MODE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_level, container, false)

        presenter = LevelPresenter(this, this.context!!, level)

        val startGameButton = myView?.findViewById<Button>(R.id.buttonStartChallengeGame)
        startGameButton?.setOnClickListener {

            if(presenter.canGameBeStarted()){

                val myIntent = Intent(activity, GameActivity::class.java)
                myIntent.putExtra("number_of_players", presenter.getPlayerCount())
                myIntent.putExtra("PlayGroundHeight", presenter.getPlayGroundHeight())
                myIntent.putExtra("PlayGroundWidth", presenter.getPlayGroundWidth())
                myIntent.putExtra("GameType", gameType)
                myIntent.putExtra("GameMode", gameMode)
                myIntent.putExtra("ChallengeLevelId", presenter.getChallengeLevelId())

                for(i in 0 until presenter.getPlayerCount()){
                    myIntent.putExtra((i+1).toString(), presenter.getPlayerStringElementAt(i))
                }

                startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())

            }

        }

        val recyclerView = myView?.findViewById<RecyclerView>(R.id.recyclerViewChallengePlayers)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = presenter.getAdapter()

        return myView
    }

    fun addLevelData(levelData: ChallengeLevel){
        level = levelData
    }

    /**
     * Shows the user that level can be played
     */
    override fun showUnlocked() {
        val gamePlayableImageView = myView?.findViewById<ImageView>(R.id.ivGamePlayable)
        gamePlayableImageView?.setImageDrawable(resources.getDrawable(R.drawable.unlocked))
    }

    /**
     * Shows the user that level is locked
     */
    override fun showLocked() {
        val gamePlayableImageView = myView?.findViewById<ImageView>(R.id.ivGamePlayable)
        gamePlayableImageView?.setImageDrawable(resources.getDrawable(R.drawable.locked))
    }

    /**
     * Shows the user that level has been completed
     */
    override fun showCompleted() {
        val isLevelCompletedView = myView?.findViewById<ImageView>(R.id.isCompletedLevelView)
        isLevelCompletedView?.setImageDrawable(resources.getDrawable(R.drawable.completed))
    }

    /**
     * Shows the user that level is not completed
     */
    override fun showUncompleted() {
        val isLevelCompletedView = myView?.findViewById<ImageView>(R.id.isCompletedLevelView)
        isLevelCompletedView?.setImageDrawable(resources.getDrawable(R.drawable.uncompleted))
    }

    /**
     * Shows the user the current height
     *
     * @param    value          Height value to show
     */
    override fun updateHeightText(value: Int) {
        val heightTextView = myView?.findViewById<TextView>(R.id.tvChallengeHeight)
        heightTextView?.text = getString(R.string.height_show, value)
    }

    /**
     * Shows the user the current width
     *
     * @param    value          Width value to show
     */
    override fun updateWidthText(value: Int) {
        val widthTextView = myView?.findViewById<TextView>(R.id.tvChallengeWidth)
        widthTextView?.text = getString(R.string.width_show, value)
    }

    /**
     * Reminds the user that the current level is not completed
     */
    override fun showLockedReminder(){
        Snackbar.make(levelFragmentCoordinatorLayout, R.string.level_locked, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.getAdapter().unregisterAdapterDataObserver(presenter)
    }

}