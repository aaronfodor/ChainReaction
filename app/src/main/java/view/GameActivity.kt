package view

import presenter.GamePresenter
import presenter.IGameView
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import hu.bme.aut.android.chainreaction.R

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
    private lateinit var tableLayoutPlayground: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val showPropagation = settings.getBoolean("show_propagation", true)

        val players = ArrayList<String>()

        val extras = intent.extras
        if (extras != null) {

            val number = extras.getInt("number_of_players")

            for(i in 1..number)

            players.add(extras.getString(i.toString()))
        }

        setContentView(hu.bme.aut.android.chainreaction.R.layout.activity_game)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        create7x5Game(players, showPropagation)

    }

    /**
     * Creates the presenter with an intent of a 7x5 dimensional Playground, and sets the onClickListeners on the Fields
     */
    private fun create7x5Game(players: ArrayList<String>, showPropagation: Boolean){

        tableLayoutPlayground = findViewById(hu.bme.aut.android.chainreaction.R.id.TableLayoutPlayground)
        tableLayoutPlayground.setBackgroundColor(Color.BLACK)

        val root = tableLayoutPlayground.rootView
        root.setBackgroundColor(Color.BLACK)

        for (i in 0..6) {

            val row = tableLayoutPlayground.getChildAt(i) as TableRow

            for (j in 0..4) {

                val Field = row.getChildAt(j) as ImageView
                Field.setOnClickListener(this)

            }

        }

        presenter = GamePresenter(this, 7, 5, players, showPropagation)

    }

    /**
     * original onClick override
     *
     * @param    v      Current View object
     */
    override fun onClick(v: View?) {

        if(v != null){

            val name = v.tag.toString()
            val number_x = Integer.valueOf(name.get(4).toString())
            val number_y = Integer.valueOf(name.get(6).toString())
            onPlaygroudElementClicked(number_x, number_y)

        }

    }

    /**
     * Element click listener
     *
     * @param    pos_y               Y coordinate
     * @param    pos_x               X coordinate
     * @return   OnClickListener     Listener of the given object, or null
     */
    private fun onPlaygroudElementClicked(pos_y: Int, pos_x: Int): View.OnClickListener? {

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

        val row = tableLayoutPlayground.getChildAt(pos_y) as TableRow
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
                1 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.green_dot1)
                2 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.green_dot2)
                3 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.green_dot3)
                else -> { // Note the block
                    field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
                }

            }
            2 -> when (number) {
                1 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.blue_dot1)
                2 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.blue_dot2)
                3 -> field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.blue_dot3)
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
            1 -> tableLayoutPlayground.setBackgroundColor(Color.RED)
            2 -> tableLayoutPlayground.setBackgroundColor(Color.BLUE)
            3 -> tableLayoutPlayground.setBackgroundColor(Color.GREEN)
            4 -> tableLayoutPlayground.setBackgroundColor(Color.YELLOW)
            //orange
            5 -> tableLayoutPlayground.setBackgroundColor(Color.rgb(255,165,0))
            6 -> tableLayoutPlayground.setBackgroundColor(Color.MAGENTA)
            //brown
            7 -> tableLayoutPlayground.setBackgroundColor(Color.rgb(210,180,140))
            8 -> tableLayoutPlayground.setBackgroundColor(Color.LTGRAY)
            else -> { // Note the block
            }
        }

        tableLayoutPlayground.invalidate()

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
     * Shows the result of the game play
     *
     * @param     msg         Message
     * @return    boolean     True if succeed, false otherwise
     */
    override fun ShowResult(msg: String): Boolean {

        val infoText = findViewById<TextView>(hu.bme.aut.android.chainreaction.R.id.textViewInfo)
        infoText.text = msg

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

        presenter.stopPresenter()
        super.onPause()

    }

}
