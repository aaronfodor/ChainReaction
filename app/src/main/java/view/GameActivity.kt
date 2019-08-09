package view

import android.arch.persistence.room.Room
import presenter.GamePresenter
import presenter.PlayerVisualRepresentation
import presenter.IGameView
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.TextView
import com.bumptech.glide.Glide
import hu.bme.aut.android.chain_reaction.R
import model.db.stats.PlayerTypeStatsDatabase
import android.widget.TextSwitcher
import model.db.DbDefaults
import model.db.challenge.ChallengeDatabase
import presenter.AudioPresenter
import view.subclass.AdActivity
import view.subclass.BaseDialog
import android.content.res.Configuration
import kotlinx.android.synthetic.main.activity_game.*

/**
 * Activity of a game play
 */
class GameActivity : AdActivity(), IGameView, View.OnClickListener {

    /**
     * presenter of the view
     */
    private lateinit var presenter: GamePresenter

    /**
     * layout of the Playground
     */
    private lateinit var tableLayoutPlayGround: TableLayout

    /**
     * Current times in milliseconds to calculate the duration of the waiting time of the Player
     */
    private var previousScreenRefreshTime: Long = 0
    private var currentClickTime: Long  = 0

    /**
     * Default size values to generate PlayGround
     */
    private var height = 7
    private var width = 5

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val showPropagation = settings.getBoolean("show_propagation", true)
        val gifEnabled = settings.getBoolean("gif_enabled", true)
        val timeLimit = settings.getBoolean("time_limit", false)
        val players = ArrayList<String>()
        var gameType = 1
        var gameMode = 1
        var challengeLevelId = 0

        val extras = intent.extras
        if (extras != null) {

            height = extras.getInt("PlayGroundHeight")
            width = extras.getInt("PlayGroundWidth")
            gameType = extras.getInt("GameType")
            gameMode = extras.getInt("GameMode")
            challengeLevelId = extras.getInt("ChallengeLevelId")

            val number = extras.getInt("number_of_players")
            for(i in 1..number){
                players.add(extras.getString(i.toString())!!)
            }

        }

        setContentView(R.layout.activity_game)

        val textSwitcher = findViewById<TextSwitcher>(R.id.textViewInfo)
        textSwitcher.setFactory {
            val textView = TextView(this@GameActivity)
            textView.gravity = Gravity.CENTER_HORIZONTAL
            textView.setTextColor(resources.getColor(R.color.colorMessage))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            textView
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        createNxMGame(players, showPropagation, gifEnabled, timeLimit, height, width, gameType, gameMode, challengeLevelId)

        initActivityAd(findViewById(R.id.gameAdView))

    }

    /**
     * Creates the presenter with an intent of a NxM dimensional Playground, and sets the onClickListeners on the Fields
     */
    private fun createNxMGame(players: ArrayList<String>, showPropagation: Boolean, gifEnabled: Boolean, timeLimit: Boolean,
                              height: Int, width: Int, gameType: Int, gameMode: Int, challengeLevelId: Int){

        tableLayoutPlayGround = findViewById(R.id.TableLayoutPlayGround)
        tableLayoutPlayGround.setBackgroundColor(Color.BLACK)

        val tableLayout = tableLayoutPlayGround.rootView
        tableLayout.setBackgroundColor(Color.BLACK)

        for (i in 0 .. (height-1)) {

            val tableRow = TableRow(this)
            tableRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)

            for(j in 0 .. (width-1)){

                val ivField = ImageView(this)
                val defaultImage = R.drawable.nothing

                Glide
                    .with(applicationContext)
                    .load(defaultImage)
                    .placeholder(defaultImage)
                    .into(ivField)

                ivField.setTag(R.string.KEY_CURRENT_IMAGE ,"$defaultImage")
                ivField.setTag(R.string.KEY_COORDINATES,"img-$i-$j")
                ivField.adjustViewBounds = true
                ivField.setOnClickListener(this)

                val lp = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT
                )
                lp.setMargins(1, 1, 1, 1)
                ivField.layoutParams = lp

                tableRow.addView(ivField)

            }

            tableLayoutPlayGround.addView(tableRow)

        }

        presenter = GamePresenter(this, height, width, players, showPropagation, gifEnabled, timeLimit, gameType, gameMode, challengeLevelId)

    }

    /**
     * original onClick override
     *
     * @param    v      Current View object
     */
    override fun onClick(v: View?) {

        if(v != null){
            val values = v.getTag(R.string.KEY_COORDINATES).toString().split("-").toTypedArray()
            val numberX = Integer.valueOf(values[1])
            val numberY = Integer.valueOf(values[2])
            onPlayGroundElementClicked(numberX, numberY)
        }

    }

    /**
     * Element click listener
     *
     * @param    pos_y               Y coordinate
     * @param    pos_x               X coordinate
     * @return   OnClickListener     Listener of the given object, or null
     */
    private fun onPlayGroundElementClicked(pos_y: Int, pos_x: Int): View.OnClickListener? {

        currentClickTime = System.currentTimeMillis()
        val waiting = currentClickTime - previousScreenRefreshTime
        presenter.stepRequest(pos_y, pos_x, waiting.toInt())

        return null

    }

    /**
     * Draws the selected Playground Field if a new image is required to be set
     *
     * @param    pos_y                  Y coordinate
     * @param    pos_x                  X coordinate
     * @param    color                  Color of the Field
     * @param    number                 elements of the Field
     * @param    isCloseToExplosion     is the particle close to the explosion
     * @param    gifEnabled             whether moving image is enabled or not
     * @return   boolean                True if succeed, false otherwise
     */
    override fun refreshPlaygroundFieldAt(pos_y: Int, pos_x: Int, color: Int, number: Int, isCloseToExplosion: Boolean, gifEnabled: Boolean): Boolean {

        val row = tableLayoutPlayGround.getChildAt(pos_y) as TableRow
        val field = row.getChildAt(pos_x) as ImageView
        val imageToSet = PlayerVisualRepresentation.getDotsImageIdByColorAndNumber(color, number, isCloseToExplosion, gifEnabled)
        val currentImageId = Integer.valueOf(field.getTag(R.string.KEY_CURRENT_IMAGE).toString())

        //if the image to set is not the same as the current image, change it and store it's Id as tag value
        if(currentImageId != imageToSet){

            Glide
                .with(applicationContext)
                .load(imageToSet)
                .placeholder(R.drawable.nothing)
                .into(field)

            field.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in))

            field.setTag(R.string.KEY_CURRENT_IMAGE, "$imageToSet")
            field.invalidate()

        }

        return true

    }

    /**
     * Shows whose turn is now
     * Starts time counting from screen refresh
     *
     * @param       Id          Id of the current Player
     * @param       showAI      True means the current is an AI Player, false means human
     * @return      boolean     True if succeed, false otherwise
     */
    override fun showCurrentPlayer(Id: Int, showAI: Boolean): Boolean {

        val infoText = findViewById<TextSwitcher>(R.id.textViewInfo)

        if(showAI){
            infoText.setText(getString(R.string.player_ai_turn, Id))
        }

        else{
            infoText.setText(getString(R.string.player_turn, Id))
        }

        //start waiting time counting
        previousScreenRefreshTime = System.currentTimeMillis()

        tableLayoutPlayGround.setBackgroundColor(PlayerVisualRepresentation.getColorById(Id))
        tableLayoutPlayGround.invalidate()
        return true

    }

    /**
     * Refresh the progress bar state with the given value
     *
     * @param	value       The progress bar state value - between 0 and 100
     */
    override fun refreshProgressBar(value: Int) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBarPlayerTime)
        progressBar.progress = value
    }

    /**
     * Shows a message from the Presenter
     *
     * @param       msg         Message
     * @return      boolean     True if succeed, false otherwise
     */
    override fun showMessage(msg: String): Boolean {
        val infoText = findViewById<TextSwitcher>(R.id.textViewInfo)
        infoText.setText(msg)
        return true
    }

    /**
     * Shows the start text from the Presenter
     * Starts time counting from screen refresh
     *
     * @param       Id          Id of the current Player
     * @param       showAI      True means the current is an AI Player, false means human
     * @return      boolean     True if succeed, false otherwise
     */
    override fun showStart(Id: Int, showAI: Boolean): Boolean {

        val infoText = findViewById<TextSwitcher>(R.id.textViewInfo)
        if(showAI){
            infoText.setText(getString(R.string.player_ai_turn, Id))
        }
        else{
            infoText.setText(getString(R.string.player_turn, Id))
        }

        Snackbar.make(tableLayoutPlayGround, R.string.start_game, Snackbar.LENGTH_SHORT).show()

        //start waiting time counting
        previousScreenRefreshTime = System.currentTimeMillis()

        return true

    }

    /**
     * Shows the result of the game play, displays GameOverFragment
     *
     * @param     winnerId          Id of the winner
     * @param     playersData       Players data. [i] is the Player index, [][0] is Player Id, [][1] is the average step time of Player, [][2] is the number of rounds of Player, [][3] is the type of the Player (1:human, 2:AI)
     * @param     humanVsAiGame     True is human and AI played in the game, false otherwise
     * @param     humanVsAiGame     True is human and AI played in the game, false otherwise
     * @return    boolean           True if succeed, false otherwise
     */
    override fun showResult(winnerId: Int, playersData: Array<IntArray>, humanVsAiGame: Boolean): Boolean {

        val infoText = findViewById<TextSwitcher>(R.id.textViewInfo)
        val text = getString(R.string.winner_text, winnerId)

        val currentText = infoText.currentView as TextView

        if(currentText.text != text){

            infoText.setText(text)

            val playersNumber = playersData.size
            val bundle = Bundle()
            bundle.putInt("playersNumber", playersNumber)

            for (i in 1..playersNumber) {
                bundle.putInt((i-1).toString()+"Id", playersData[i-1][0])
                bundle.putInt((i-1).toString()+"AvgStepTime", playersData[i-1][1])
                bundle.putInt((i-1).toString()+"Rounds", playersData[i-1][2])
                bundle.putInt((i-1).toString()+"TypeId", playersData[i-1][3])
            }

            //show the fragment
            val fragment = GameOverFragment()
            fragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom)
            transaction.replace(R.id.viewGame, fragment)
            //transaction.commit() can lead to IllegalStateException as state loss can happen here
            //fragment state is not important for us, state loss is acceptable here
            transaction.commitAllowingStateLoss()

        }

        return true

    }

    /**
     * Updates statistics the database
     * Increments the overall number of victories of the winner's type and saves it, increments all games counter
     * Updates campaign database if the game was a campaign game
     * Calls play end game sound function
     *
     * @param     playerType    Type of the winner. 1 means human, 2 means AI
     * @param     humanVsAiGame True is human and AI played in the game, false otherwise
     */
    override fun statisticsDatabaseUpdater(playerType: Int, humanVsAiGame: Boolean){

        playEndGameSound(playerType)

        val dbStats = Room.databaseBuilder(applicationContext, PlayerTypeStatsDatabase::class.java, "db_stats").build()

        Thread {

            var playerTypeStats = dbStats.playerTypeStatDAO().getAll().toMutableList()

            if(playerTypeStats.isEmpty()){
                val defaultStats = DbDefaults.statsDatabaseDefaults()
                for(stat in defaultStats){
                    dbStats.playerTypeStatDAO().insert(stat)
                }
                playerTypeStats = dbStats.playerTypeStatDAO().getAll().toMutableList()
            }

            if(humanVsAiGame){
                when (playerType) {
                    1 -> {
                        dbStats.playerTypeStatDAO().update(playerTypeStats[0].NumberOfVictories + 1, "human")
                    }
                    2 -> {
                        dbStats.playerTypeStatDAO().update(playerTypeStats[1].NumberOfVictories + 1, "ai")
                    }
                }
            }

            dbStats.playerTypeStatDAO().update(playerTypeStats[2].NumberOfVictories + 1, "all_games")
            dbStats.close()

        }.start()

    }

    /**
     * Updates challenge database
     *
     * @param     challengeLevelId   Challenge level Id to save that it has been completed
     */
    override fun challengeDatabaseUpdater(challengeLevelId: Int) {

        val dbChallenge = Room.databaseBuilder(applicationContext, ChallengeDatabase::class.java, "db_challenge").build()

        Thread {

            val challengeLevels = dbChallenge.challengeLevelsDAO().getAll().toMutableList()

            if(challengeLevels.isEmpty()){
                val defaultChallengeStates = DbDefaults.challengeDatabaseDefaults()
                for(level in defaultChallengeStates){
                    dbChallenge.challengeLevelsDAO().insert(level)
                }
            }
            dbChallenge.challengeLevelsDAO().completeLevel(true, challengeLevelId.toLong())

            //if the last campaign level is not reached
            if(challengeLevels.last().Id > challengeLevelId){
                dbChallenge.challengeLevelsDAO().unlockLevel(true, challengeLevelId.toLong()+1)
                runOnUiThread {
                    presenter.newCampaignLevelUnlocked()
                }
            }

            else{
                runOnUiThread {
                    presenter.allChallengesCompleted()
                }
            }

            dbChallenge.close()

        }.start()

    }

    /**
     * Plays the corresponding end game sound
     *
     * @param     playerType    Type of the winner. 1 means human, 2 means AI
     */
    private fun playEndGameSound(playerType: Int){

        when(playerType){
            1 -> {
                AudioPresenter.soundHumanVictory()
                AudioPresenter.soundConfetti()
            }
            else -> {
                AudioPresenter.soundAIVictory()
            }
        }

    }

    /**
     * Displays a dialog if the game is running or returns to the MainActivity and stops the current game instance if the game is over
     */
    override fun onBackPressed() {

        if(presenter.isResultDisplayed()){
            startActivity(Intent(this, MainActivity::class.java))
            super.onBackPressed()
        }
        else{
            leaveDialog()
        }

    }

    /**
     * Displays the leave dialog to confirm exit
     */
    private fun leaveDialog(){

        val leaveDialog = BaseDialog(this, getString(R.string.confirm_leave_game), getString(R.string.confirm_leave_game_description), resources.getDrawable(R.drawable.warning))

        leaveDialog.setPositiveButton {
            //leaving the current game play
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }
        leaveDialog.show()

    }

    /**
     * Shows the game over message
     *
     * @param     restartOffer      True means restart action will be offered
     */
    override fun showEndOfGameMessage(restartOffer: Boolean) {

        val snackBar = Snackbar.make(tableLayoutPlayGround, R.string.game_over, Snackbar.LENGTH_INDEFINITE)

        if(restartOffer){

            snackBar.setAction("RESTART") {
                AudioPresenter.soundButtonClick()
                //restart the current game play
                val intent = intent
                this.finish()
                startActivity(intent)
            }

        }
        else{

            snackBar.setAction("LEAVE") {
                AudioPresenter.soundButtonClick()
                //leaving the current game play
                startActivity(Intent(this, MainActivity::class.java))
                this.finish()
            }

        }

        snackBar.show()

    }

    /**
     * Shows the restart campaign message
     */
    override fun showRestartChallengeLevelMessage() {

        val snackBar = Snackbar.make(tableLayoutPlayGround, R.string.challenge_level_failed, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction("RESTART") {
            AudioPresenter.soundButtonClick()
            //restart the current game play
            val intent = intent
            this.finish()
            startActivity(intent)
        }
        snackBar.show()

    }

    /**
     * Shows the next campaign message
     */
    override fun showNextChallengeLevelMessage() {

        val snackBar = Snackbar.make(tableLayoutPlayGround, R.string.challenge_level_completed, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction("NEXT") {
            AudioPresenter.soundButtonClick()
            //going to the next level
            startActivity(Intent(this, StartChallengeActivity::class.java))
            this.finish()
        }
        snackBar.show()

    }

    /**
     * Shows the all campaigns completed message
     */
    override fun showAllChallengesCompletedMessage() {

        val snackBar = Snackbar.make(tableLayoutPlayGround, R.string.all_challenges_completed, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction("LEAVE") {
            AudioPresenter.soundButtonClick()
            //leaving the current game play
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }
        snackBar.show()

    }

    /**
     * Called when leaving the activity
     */
    override fun onPause() {
        presenter.task?.cancel(true)
        super.onPause()
    }

    /**
     * Stops the presenter calculations of the activity
     */
    override fun finish() {
        super.finish()
        this.presenter.stop()
    }

}