package view

import android.app.ActivityOptions
import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdView
import hu.bme.aut.android.chainreaction.R
import kotlinx.android.synthetic.main.activity_start_challenge.*
import model.db.DbDefaults
import model.db.challenge.ChallengeDatabase
import model.db.challenge.ChallengeLevel
import presenter.IStartChallengeView
import presenter.StartChallengePresenter
import presenter.AdPresenter

/**
 * Activity of starting a challenge game play
 */
class StartChallengeActivity : AppCompatActivity(), IStartChallengeView {

    companion object {
        private const val CHALLENGE_GAME = 2

        private const val NORMAL_MODE = 1
        private const val DYNAMIC_MODE = 2
    }

    /**
     * presenter of the view
     */
    private lateinit var presenter: StartChallengePresenter

    private var gameType = CHALLENGE_GAME
    private var gameMode = NORMAL_MODE

    /**
     * Advertisement of the activity
     */
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_start_challenge)

        presenter = StartChallengePresenter(this, this.applicationContext)

        val gameTypeStartView = findViewById<ImageView>(R.id.gameTypeStartCampaignView)
        gameTypeStartView.setImageDrawable(resources.getDrawable(R.drawable.game_mode_challenge))

        val previousLevelButton = findViewById<Button>(R.id.buttonPreviousLevel)
        previousLevelButton.setOnClickListener {
            presenter.levelMinus()
        }

        val nextLevelButton = findViewById<Button>(R.id.buttonNextLevel)
        nextLevelButton.setOnClickListener {
            presenter.levelPlus()
        }

        val startGameButton = findViewById<Button>(R.id.buttonStartChallengeGame)
        startGameButton.setOnClickListener {

            if(presenter.canGameBeStarted()){

                val myIntent = Intent(this, GameActivity::class.java)
                myIntent.putExtra("number_of_players", presenter.getPlayerCount())
                myIntent.putExtra("PlayGroundHeight", presenter.getPlayGroundHeight())
                myIntent.putExtra("PlayGroundWidth", presenter.getPlayGroundWidth())
                myIntent.putExtra("GameType", gameType)
                myIntent.putExtra("GameMode", gameMode)
                myIntent.putExtra("ChallengeLevel", presenter.getChallengeLevelIndex())

                for(i in 0 until presenter.getPlayerCount()){
                    myIntent.putExtra((i+1).toString(), presenter.getPlayerStringElementAt(i))
                }

                startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

            }

        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewChallengePlayers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = presenter.getAdapter()

        mAdView = findViewById(R.id.startChallengeAdView)
        //loading the advertisement
        AdPresenter.loadAd(mAdView)

    }

    /**
     * Reads the challenge database, saves it into a list, then passes it to the view
     */
    override fun challengeDatabaseReader() {

        val dbChallenge = Room.databaseBuilder(applicationContext, ChallengeDatabase::class.java, "db_challenge").build()
        var challengeLevels : MutableList<ChallengeLevel>

        Thread {

            challengeLevels = dbChallenge.challengeLevelsDAO().getAll().toMutableList()

            if(challengeLevels.isEmpty()){
                val defaultCampaignStates = DbDefaults.challengeDatabaseDefaults()
                for(level in defaultCampaignStates){
                    dbChallenge.challengeLevelsDAO().insert(level)
                }
                challengeLevels = dbChallenge.challengeLevelsDAO().getAll().toMutableList()
            }

            dbChallenge.close()

            runOnUiThread { presenter.setChallengeLevels(challengeLevels) }

        }.start()

    }

    /**
     * Shows the user that level can be played
     */
    override fun showUnlocked() {
        val gamePlayableImageView = findViewById<ImageView>(R.id.ivGamePlayable)
        gamePlayableImageView.setImageDrawable(resources.getDrawable(R.drawable.unlocked))
    }

    /**
     * Shows the user that level is locked
     */
    override fun showLocked() {
        val gamePlayableImageView = findViewById<ImageView>(R.id.ivGamePlayable)
        gamePlayableImageView.setImageDrawable(resources.getDrawable(R.drawable.locked))
    }

    /**
     * Shows the user that level has been completed
     */
    override fun showCompleted() {
        val isLevelCompletedView = findViewById<ImageView>(R.id.isCompletedLevelView)
        isLevelCompletedView.setImageDrawable(resources.getDrawable(R.drawable.completed))
    }

    /**
     * Shows the user that level is not completed
     */
    override fun showUncompleted() {
        val isLevelCompletedView = findViewById<ImageView>(R.id.isCompletedLevelView)
        isLevelCompletedView.setImageDrawable(resources.getDrawable(R.drawable.uncompleted))
    }

    /**
     * Shows the user the current height
     *
     * @param    value          Height value to show
     */
    override fun updateHeightText(value: Int) {
        val heightTextView = findViewById<TextView>(R.id.tvChallengeHeight)
        heightTextView.text = getString(R.string.height_show, value)
    }

    /**
     * Shows the user the current width
     *
     * @param    value          Width value to show
     */
    override fun updateWidthText(value: Int) {
        val widthTextView = findViewById<TextView>(R.id.tvChallengeWidth)
        widthTextView.text = getString(R.string.width_show, value)
    }

    /**
     * Shows the user the current level
     *
     * @param    value          Current level name
     */
    override fun updateCurrentLevelText(value: String) {
        val currentLevelView = findViewById<TextView>(R.id.tvCurrentLevel)
        currentLevelView.text = value
    }

    /**
     * Reminds the user that the current level is not completed
     */
    override fun showLockedReminder(){
        Snackbar.make(recyclerViewChallengePlayers, R.string.level_locked, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Called when leaving the activity - stops the presenter calculations too
     */
    override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    /**
     * Called when returning to the activity
     */
    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    /**
     * Called before the activity is destroyed
     */
    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

}