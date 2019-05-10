package view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import android.widget.TextView
import hu.bme.aut.android.chainreaction.R
import presenter.PlayerListAdapter
import presenter.PlayerListData
import kotlinx.android.synthetic.main.activity_start.*
import presenter.PlayerVisualRepresentation
import java.util.*

/**
 * Activity of settings of game play
 */
class StartActivity : AppCompatActivity() {

    companion object {
        private const val MAXIMUM_SIZE = 30
        private const val MINIMUM_SIZE = 3
        private const val MAXIMUM_ALLOWED_PLAYER_NUMBER = 8
        private const val MINIMUM_PLAYER_NUMBER_TO_START = 2
    }

    private var playGroundHeight = 7
    private var playGroundWidth = 5
    private var playerListData = ArrayList<PlayerListData>()
    private lateinit var adapter: PlayerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_start)

        val addHumanPlayerButton = findViewById<Button>(R.id.buttonAddHumanPlayer)
        addHumanPlayerButton.setOnClickListener {

            if(adapter.itemCount < MAXIMUM_ALLOWED_PLAYER_NUMBER){
                adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"human", imageAdder(adapter.itemCount+1)))
                Snackbar.make(recyclerViewPlayers, getString(R.string.player_added, (adapter.itemCount)), Snackbar.LENGTH_SHORT).show()
            }

            else{
                Snackbar.make(recyclerViewPlayers, R.string.maximum_reached, Snackbar.LENGTH_LONG).show()
            }

        }

        val addAIPlayerButton = findViewById<Button>(R.id.buttonAddAIPlayer)
        addAIPlayerButton.setOnClickListener {

            if(adapter.itemCount < MAXIMUM_ALLOWED_PLAYER_NUMBER){
                adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"AI", imageAdder(adapter.itemCount+1)))
                Snackbar.make(recyclerViewPlayers, getString(R.string.player_added, (adapter.itemCount)), Snackbar.LENGTH_SHORT).show()
            }

            else{
                Snackbar.make(recyclerViewPlayers, R.string.maximum_reached, Snackbar.LENGTH_LONG).show()
            }

        }

        val clearPlayersButton = findViewById<Button>(R.id.buttonClearPlayers)
        clearPlayersButton.setOnClickListener {
            adapter.clear()
            Snackbar.make(recyclerViewPlayers, R.string.list_clear, Snackbar.LENGTH_LONG).show()
        }

        val startGameButton = findViewById<Button>(R.id.buttonStartGame)
        startGameButton.setOnClickListener {

            if(adapter.itemCount >= MINIMUM_PLAYER_NUMBER_TO_START){

                val myIntent = Intent(this, GameActivity::class.java)
                myIntent.putExtra("number_of_players", adapter.itemCount)
                myIntent.putExtra("PlayGroundHeight", playGroundHeight)
                myIntent.putExtra("PlayGroundWidth", playGroundWidth)

                for(i in 0 until adapter.itemCount){
                    myIntent.putExtra((i+1).toString(), adapter.stringElementAt(i))
                }

                startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

            }

            else{
                Snackbar.make(recyclerViewPlayers, R.string.not_enough_player, Snackbar.LENGTH_LONG).show()
            }

        }

        adapter = PlayerListAdapter(applicationContext, playerListData)
        adapter.addItem(PlayerListData("Player 1", "human", imageAdder(1)))
        adapter.addItem(PlayerListData("Player 2", "AI", imageAdder(2)))

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPlayers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val heightTextView = findViewById<TextView>(R.id.tvHeight)
        heightTextView.text = getString(R.string.height_show, playGroundHeight)

        val widthTextView = findViewById<TextView>(R.id.tvWidth)
        widthTextView.text = getString(R.string.width_show, playGroundWidth)

        val heightPlusButton = findViewById<Button>(R.id.buttonHeightPlus)
        heightPlusButton.setOnClickListener {

            if(playGroundHeight == MAXIMUM_SIZE){
                Snackbar.make(recyclerViewPlayers, R.string.maximum_size, Snackbar.LENGTH_SHORT).show()
            }

            else{
                playGroundHeight++
                heightTextView.text = getString(R.string.height_show, playGroundHeight)
            }

        }

        val heightMinusButton = findViewById<Button>(R.id.buttonHeightMinus)
        heightMinusButton.setOnClickListener {

            if(playGroundHeight == MINIMUM_SIZE){
                Snackbar.make(recyclerViewPlayers, R.string.minimum_size, Snackbar.LENGTH_SHORT).show()
            }

            else{
                playGroundHeight--
                heightTextView.text = getString(R.string.height_show, playGroundHeight)
            }

        }

        val widthPlusButton = findViewById<Button>(R.id.buttonWidthPlus)
        widthPlusButton.setOnClickListener {

            if(playGroundWidth == MAXIMUM_SIZE){
                Snackbar.make(recyclerViewPlayers, R.string.maximum_size, Snackbar.LENGTH_SHORT).show()
            }

            else{
                playGroundWidth++
                widthTextView.text = getString(R.string.width_show, playGroundWidth)
            }

        }

        val widthMinusButton = findViewById<Button>(R.id.buttonWidthMinus)
        widthMinusButton.setOnClickListener {

            if(playGroundWidth == MINIMUM_SIZE){
                Snackbar.make(recyclerViewPlayers, R.string.minimum_size, Snackbar.LENGTH_SHORT).show()
            }

            else{
                playGroundWidth--
                widthTextView.text = getString(R.string.width_show, playGroundWidth)
            }

        }

    }

    private fun imageAdder(Id: Int): Int {
        return PlayerVisualRepresentation.getDotsImageIdByColorAndNumber(Id, 1)
    }

}