package view

import android.arch.persistence.room.Room
import presenter.GamePresenter
import presenter.PlayerVisualRepresentation
import presenter.IGameView
import presenter.AdPresenter
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
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
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdView
import hu.bme.aut.android.chainreaction.R
import model.db.PlayerTypeStat
import model.db.PlayerTypeStatsDatabase

/**
 * Activity of a game play
 */
class GameActivity : AppCompatActivity(), IGameView, View.OnClickListener {

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
    private var previousClickTime: Long = 0
    private var currentClickTime: Long  = 0

    /**
     * Default values to generate PlayGround
     */
    private var height = 7
    private var width = 5

    /**
     * Advertisement of the activity
     */
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val showPropagation = settings.getBoolean("show_propagation", true)
        val gifEnabled = settings.getBoolean("gif_enabled", true)
        val timeLimit = settings.getBoolean("time_limit", false)
        val players = ArrayList<String>()
        val extras = intent.extras

        if (extras != null) {
            height = extras.getInt("PlayGroundHeight")
            width = extras.getInt("PlayGroundWidth")
            val number = extras.getInt("number_of_players")
            for(i in 1..number)
            players.add(extras.getString(i.toString())!!)
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
        createNxMGame(players, showPropagation, gifEnabled, timeLimit, height, width)

        mAdView = findViewById(R.id.gameAdView)
        //loading the advertisement
        AdPresenter.loadAd(mAdView)

    }

    /**
     * Creates the presenter with an intent of a NxM dimensional Playground, and sets the onClickListeners on the Fields
     */
    private fun createNxMGame(players: ArrayList<String>, showPropagation: Boolean, gifEnabled: Boolean, timeLimit: Boolean, height: Int, width: Int){

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

        presenter = GamePresenter(this, height, width, players, showPropagation, gifEnabled, timeLimit)
        //start waiting time counting
        previousClickTime = System.currentTimeMillis()

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
        val waiting = currentClickTime - previousClickTime
        presenter.stepRequest(pos_y, pos_x, waiting.toInt())
        previousClickTime = currentClickTime
        return null
    }

    /**
     * Draws the selected Playground Field if a new image is required to be set
     *
     * @param    pos_y       Y coordinate
     * @param    pos_x       X coordinate
     * @param    color       Color of the Field
     * @param    number      elements of the Field
     * @param    gifEnabled  whether moving image is enabled or not
     * @return   boolean     True if succeed, false otherwise
     */
    override fun refreshPlayground(pos_y: Int, pos_x: Int, color: Int, number: Int, gifEnabled: Boolean): Boolean {

        val row = tableLayoutPlayGround.getChildAt(pos_y) as TableRow
        val field = row.getChildAt(pos_x) as ImageView
        val imageToSet = PlayerVisualRepresentation.getDotsImageIdByColorAndNumber(color, number, gifEnabled)
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
     *
     * @param       Id          Id of the current Player
     * @return      boolean     True if succeed, false otherwise
     */
    override fun showCurrentPlayer(Id: Int): Boolean {

        val infoText = findViewById<TextSwitcher>(R.id.textViewInfo)
        infoText.setText(getString(R.string.player_turn, Id))
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
     *
     * @param       Id          Id of the current Player
     * @return      boolean     True if succeed, false otherwise
     */
    override fun showStart(Id: Int): Boolean {
        val infoText = findViewById<TextSwitcher>(R.id.textViewInfo)
        infoText.setText(getString(R.string.player_turn, Id))
        Snackbar.make(tableLayoutPlayGround, R.string.start_game, Snackbar.LENGTH_SHORT).show()
        return true
    }

    /**
     * Shows the result of the game play, displays GameOverFragment, writes the database
     *
     * @param     winnerId          Id of the winner
     * @param     playersData       Players data. [i] is the Player index, [][0] is Player Id, [][1] is the average step time of Player, [][2] is the number of rounds of Player, [][3] is the type of the Player (1:human, 2:AI)
     * @param     humanVsAiGame     True is human and AI played in the game, false otherwise
     * @return    boolean           True if succeed, false otherwise
     */
    override fun showResult(winnerId: Int, playersData: Array<IntArray>, humanVsAiGame: Boolean): Boolean {

        val infoText = findViewById<TextSwitcher>(R.id.textViewInfo)
        val text = getString(R.string.winner_text, winnerId)

        val currentText = infoText.currentView as TextView

        if(currentText.text != text){

            infoText.setText(text)

            val snackBar = Snackbar.make(tableLayoutPlayGround, R.string.game_over, Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction("LEAVE") {
                this.finish()
            }
            snackBar.show()

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

            //update the database
            databaseUpdater(playersData[playersNumber-1][3], humanVsAiGame)

        }

        return true

    }

    /**
     * Updates the database
     * Increments the overall number of victories of the winner's type and saves it, increments all games counter
     * @param     playerType    Type of the winner. 1 means human, 2 means AI
     * @param     humanVsAiGame True is human and AI played in the game, false otherwise
     */
    private fun databaseUpdater(playerType: Int, humanVsAiGame: Boolean){

        val db = Room.databaseBuilder(applicationContext, PlayerTypeStatsDatabase::class.java, "db").build()

        Thread {

            val playerTypeStats = db.playerTypeStatDAO().getAll().toMutableList()

            if(playerTypeStats.isEmpty()){
                playerTypeStats.add(0, PlayerTypeStat(1,"human",0))
                playerTypeStats.add(0, PlayerTypeStat(2,"ai",0))
                playerTypeStats.add(0, PlayerTypeStat(3,"all_games",0))
                db.playerTypeStatDAO().insert(playerTypeStats[0])
                db.playerTypeStatDAO().insert(playerTypeStats[1])
                db.playerTypeStatDAO().insert(playerTypeStats[2])
            }

            if(humanVsAiGame){
                when (playerType) {
                    1 -> {
                        db.playerTypeStatDAO().update(playerTypeStats[0].NumberOfVictories + 1, "human")
                    }
                    2 -> {
                        db.playerTypeStatDAO().update(playerTypeStats[1].NumberOfVictories + 1, "ai")
                    }
                }
            }

            db.playerTypeStatDAO().update(playerTypeStats[2].NumberOfVictories + 1, "all_games")
            db.close()

        }.start()

    }

    /**
     * Returns to the MainActivity, stops the current game instance
     */
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

    /**
     * Called when leaving the activity - stops the presenter calculations too
     */
    override fun onPause() {
        mAdView.pause()
        presenter.task?.cancel(true)
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