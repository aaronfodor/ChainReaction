package view

import presenter.GamePresenter
import presenter.IGameView
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.view.View
import android.view.WindowManager
import hu.bme.aut.android.chainreaction.R
import android.widget.*

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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val showPropagation = settings.getBoolean("show_propagation", true)
        val players = ArrayList<String>()
        val extras = intent.extras

        //default values to generate PlayGround
        var height = 7
        var width = 5

        if (extras != null) {
            height = extras.getInt("PlayGroundHeight")
            width = extras.getInt("PlayGroundWidth")
            val number = extras.getInt("number_of_players")
            for(i in 1..number)
            players.add(extras.getString(i.toString()))
        }

        setContentView(hu.bme.aut.android.chainreaction.R.layout.activity_game)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        createNxMGame(players, showPropagation, height, width)

    }

    /**
     * Creates the presenter with an intent of a NxM dimensional Playground, and sets the onClickListeners on the Fields
     */
    private fun createNxMGame(players: ArrayList<String>, showPropagation: Boolean, height: Int, width: Int){

        tableLayoutPlayGround = findViewById(hu.bme.aut.android.chainreaction.R.id.TableLayoutPlayGround)
        tableLayoutPlayGround.setBackgroundColor(Color.BLACK)

        val tableLayout = tableLayoutPlayGround.rootView
        tableLayout.setBackgroundColor(Color.BLACK)

        for (i in 0..(height-1)) {

            val tableRow = TableRow(this)
            tableRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)

            for(j in 0..(width-1)){
                var ivField = ImageView(this)
                ivField.tag = "img-$i-$j"
                ivField.adjustViewBounds = true
                ivField.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
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

        presenter = GamePresenter(this, height, width, players, showPropagation)

    }

    /**
     * original onClick override
     *
     * @param    v      Current View object
     */
    override fun onClick(v: View?) {

        if(v != null){
            val values = v.tag.toString().split("-").toTypedArray()
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
        presenter.StepRequest(pos_y, pos_x)
        return null
    }

    /**
     * Draws the selected Playground Field
     *
     * @param    pos_y       Y coordinate
     * @param    pos_x       X coordinate
     * @param    color       Color of the Field
     * @param    number      elements of the Field
     * @return   boolean     True if succeed, false otherwise
     */
    override fun RefreshPlayground(pos_y: Int, pos_x: Int, color: Int, number: Int): Boolean {

        val row = tableLayoutPlayGround.getChildAt(pos_y) as TableRow
        val field = row.getChildAt(pos_x) as ImageView

        when (color) {
            8 -> when (number) {
                1 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.grey_dot1)
                2 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.grey_dot2)
                3 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.grey_dot3)
                else -> { // Note the block
                    field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
                }

            }
            7 -> when (number) {
                1 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.brown_dot1)
                2 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.brown_dot2)
                3 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.brown_dot3)
                else -> { // Note the block
                    field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
                }

            }
            6 -> when (number) {
                1 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.pink_dot1)
                2 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.pink_dot2)
                3 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.pink_dot3)
                else -> { // Note the block
                    field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
                }

            }
            5 -> when (number) {
                1 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.orange_dot1)
                2 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.orange_dot2)
                3 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.orange_dot3)
                else -> { // Note the block
                    field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
                }

            }
            4 -> when (number) {
                1 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.yellow_dot1)
                2 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.yellow_dot2)
                3 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.yellow_dot3)
                else -> { // Note the block
                    field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
                }

            }
            3 -> when (number) {
                1 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.blue_dot1)
                2 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.blue_dot2)
                3 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.blue_dot3)
                else -> { // Note the block
                    field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
                }

            }
            2 -> when (number) {
                1 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.green_dot1)
                2 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.green_dot2)
                3 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.green_dot3)
                else -> { // Note the block
                    field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
                }

            }
            1 -> when (number) {
                1 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.red_dot1)
                2 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.red_dot2)
                3 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.red_dot3)
                else -> { // Note the block
                    field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
                }
            }
            else -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
        }

        field.invalidate()
        return true

    }

    /**
     * Shows whose turn is now
     *
     * @param       Id          Id of the current Player
     * @return      boolean     True if succeed, false otherwise
     */
    override fun ShowCurrentPlayer(Id: Int): Boolean {

        val infoText = findViewById<TextView>(hu.bme.aut.android.chainreaction.R.id.textViewInfo)
        infoText.text = getString(R.string.player_turn, Id)

        when (Id) {
            1 -> tableLayoutPlayGround.setBackgroundColor(Color.RED)
            2 -> tableLayoutPlayGround.setBackgroundColor(Color.GREEN)
            3 -> tableLayoutPlayGround.setBackgroundColor(Color.BLUE)
            4 -> tableLayoutPlayGround.setBackgroundColor(Color.YELLOW)
            //orange
            5 -> tableLayoutPlayGround.setBackgroundColor(Color.rgb(255,165,0))
            6 -> tableLayoutPlayGround.setBackgroundColor(Color.MAGENTA)
            //brown
            7 -> tableLayoutPlayGround.setBackgroundColor(Color.rgb(210,180,140))
            8 -> tableLayoutPlayGround.setBackgroundColor(Color.LTGRAY)
            else -> { // Note the block
            }
        }

        tableLayoutPlayGround.invalidate()
        return true

    }

    /**
     * Shows a message from the Presenter
     *
     * @param       msg         Message
     * @return      boolean     True if succeed, false otherwise
     */
    override fun ShowMessage(msg: String): Boolean {
        val infoText = findViewById<TextView>(hu.bme.aut.android.chainreaction.R.id.textViewInfo)
        infoText.text = msg
        return true
    }

    /**
     * Shows the start text from the Presenter
     *
     * @param       Id          Id of the current Player
     * @return      boolean     True if succeed, false otherwise
     */
    override fun ShowStart(Id: Int): Boolean {
        val infoText = findViewById<TextView>(hu.bme.aut.android.chainreaction.R.id.textViewInfo)
        infoText.text = getString(R.string.player_turn, Id)
        Snackbar.make(tableLayoutPlayGround, hu.bme.aut.android.chainreaction.R.string.start_game, Snackbar.LENGTH_LONG).show()
        return true
    }

    /**
     * Shows the result of the game play
     *
     * @param     msg         Message
     * @return    boolean     True if succeed, false otherwise
     */
    override fun ShowResult(msg: String): Boolean {

        val infoText = findViewById<TextView>(hu.bme.aut.android.chainreaction.R.id.textViewInfo)

        if(infoText.text != msg){
            infoText.text = msg
            Snackbar.make(tableLayoutPlayGround, hu.bme.aut.android.chainreaction.R.string.game_over, Snackbar.LENGTH_INDEFINITE).show()
        }

        return true

    }

    /**
     * Returns to the MainActivity
     */
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    /**
     * Stops the Presenter calculations
     */
    override fun onPause() {
        presenter.task.cancel(true)
        super.onPause()
    }

}
