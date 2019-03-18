package View

import Presenter.GamePresenter
import Presenter.IGameView
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

class GameActivity : AppCompatActivity(), IGameView, View.OnClickListener {

    /**
     * presenter of the view
     */
    private lateinit var presenter: GamePresenter

    /**
     * layout of the Playground
     */
    private lateinit var TableLayoutPlayground: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(hu.bme.aut.android.chainreaction.R.layout.activity_game)

        /*var FieldView = ImageView(this)
        val bmImg = getResources().getDrawable(android.R.drawable.ic_lock_lock);
        FieldView.setImageDrawable(bmImg)*/
        //val bmImg = BitmapFactory.decodeFile("@drawable/blue_dot1")
        //FieldView.setImageBitmap(bmImg)

        /*var row = TableRow(this)
        row.addView(FieldView)
        row.addView(FieldView)
        row.addView(FieldView)

        TableLayoutPlayground.addView(row)*/

        Create7x5Game()

    }

    /**
     * Creates the presenter with an intent of a 7x5 dimensional Playground, and sets the onClickListeners on the Fields
     */
    private fun Create7x5Game(){

        TableLayoutPlayground = findViewById<TableLayout>(hu.bme.aut.android.chainreaction.R.id.TableLayoutPlayground)
        TableLayoutPlayground.setBackgroundColor(Color.BLACK)

        val root = TableLayoutPlayground.rootView
        root.setBackgroundColor(Color.BLACK)

        for (i in 0..6) {

            val row = TableLayoutPlayground.getChildAt(i) as TableRow

            for (j in 0..4) {

                val Field = row.getChildAt(j) as ImageView
                Field.setOnClickListener(this)

            }

        }

        presenter = GamePresenter(this, 7, 5)

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
    fun onPlaygroudElementClicked(pos_y: Int, pos_x: Int): View.OnClickListener? {

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

        var row = TableLayoutPlayground.getChildAt(pos_y) as TableRow
        var Field = row.getChildAt(pos_x) as ImageView

        if(color == 2){

            when (number) {
                1 -> Field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.blue_dot1)
                2 -> Field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.blue_dot2)
                3 -> Field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.blue_dot3)
                else -> { // Note the block
                    Field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
                }

            }
        }

        else if(color == 1){

            when (number) {
                1 -> Field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.red_dot1)
                2 -> Field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.red_dot2)
                3 -> Field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.red_dot3)
                else -> { // Note the block
                    Field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)
                }
            }

        }

        else{

            Field.setImageResource(hu.bme.aut.android.chainreaction.R.drawable.nothing)

        }

        Field.invalidate()

        return true

    }

    /**
     * Shows whose turn is now
     *
     * @param    Id          Id of the current Player
     * @return    boolean     True if succeed, false otherwise
     */
    override fun ShowCurrentPlayer(Id: Int): Boolean {

        var InfoText = findViewById<TextView>(hu.bme.aut.android.chainreaction.R.id.textViewInfo)
        InfoText.text = "Player " + Id + "'s turn"

        when (Id) {
            1 -> TableLayoutPlayground.setBackgroundColor(Color.RED)
            2 -> TableLayoutPlayground.setBackgroundColor(Color.BLUE)
            else -> { // Note the block
                TableLayoutPlayground.setBackgroundColor(Color.BLACK)
            }
        }

        TableLayoutPlayground.invalidate()

        return true

    }

    /**
     * Shows a message from the Presenter
     *
     * @param       msg         Message
     * @return      boolean     True if succeed, false otherwise
     */
    override fun ShowMessage(msg: String): Boolean {

        var InfoText = findViewById<TextView>(hu.bme.aut.android.chainreaction.R.id.textViewInfo)
        InfoText.text = msg

        return true

    }

    /**
     * Shows the result of the game play
     *
     * @param     msg         Message
     * @return    boolean     True if succeed, false otherwise
     */
    override fun ShowResult(msg: String): Boolean {

        var InfoText = findViewById<TextView>(hu.bme.aut.android.chainreaction.R.id.textViewInfo)
        InfoText.text = msg

        return true

    }

}
