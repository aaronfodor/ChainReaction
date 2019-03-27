package view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.support.v7.widget.LinearLayoutManager
import presenter.PlayerListAdapter
import presenter.PlayerListData
import android.widget.Toast

/**
 * Activity of settings of game play
 */
class StartActivity : AppCompatActivity() {

    private val MAXIMUM_ALLOWED_PLAYER_NUMBER = 8
    private val MINIMUM_PLAYER_NUMBER_TO_START = 2
    var playerListData = ArrayList<PlayerListData>()
    private lateinit var adapter: PlayerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(hu.bme.aut.android.chainreaction.R.layout.activity_start)

        val addHumanPlayerButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonAddHumanPlayer)
        addHumanPlayerButton.setOnClickListener {

            if(adapter.itemCount < MAXIMUM_ALLOWED_PLAYER_NUMBER){

                adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"human", imageAdder(adapter.itemCount+1)))

            }

            else{

                Toast.makeText(
                    this,
                    hu.bme.aut.android.chainreaction.R.string.maximumreached,
                    Toast.LENGTH_LONG
                ).show()

            }

        }

        val addAIPlayerButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonAddAIPlayer)
        addAIPlayerButton.setOnClickListener {

            if(adapter.itemCount < MAXIMUM_ALLOWED_PLAYER_NUMBER){

                adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"AI", imageAdder(adapter.itemCount+1)))

            }

            else{

                Toast.makeText(
                    this,
                    hu.bme.aut.android.chainreaction.R.string.maximumreached,
                    Toast.LENGTH_LONG
                ).show()

            }

        }

        val clearPlayersButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonClearPlayers)
        clearPlayersButton.setOnClickListener {
            adapter.Clear()

                Toast.makeText(
                    this,
                    hu.bme.aut.android.chainreaction.R.string.listclear,
                    Toast.LENGTH_LONG
                ).show()

        }

        val startGameButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonStartGame)
        startGameButton.setOnClickListener {

            if(adapter.itemCount >= MINIMUM_PLAYER_NUMBER_TO_START){

                var myIntent = Intent(this, GameActivity::class.java)

                myIntent.putExtra("number_of_players", adapter.itemCount)

                for(i in 0..adapter.itemCount-1){

                    myIntent.putExtra((i+1).toString(), adapter.StringElementAt(i))

                }

                startActivity(myIntent)

            }

            else{

                Toast.makeText(
                    this,
                    hu.bme.aut.android.chainreaction.R.string.notenoughplayer,
                    Toast.LENGTH_LONG
                ).show()

            }

        }

        adapter = PlayerListAdapter(playerListData)
        adapter.addItem(PlayerListData("Player 1", "human", imageAdder(1)))
        adapter.addItem(PlayerListData("Player 2", "AI", imageAdder(2)))

        var recyclerView = findViewById<RecyclerView>(hu.bme.aut.android.chainreaction.R.id.recyclerViewPlayers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    private fun imageAdder(Id: Int): Int {

        return when (Id) {
            1 -> hu.bme.aut.android.chainreaction.R.drawable.red_dot1
            2 -> hu.bme.aut.android.chainreaction.R.drawable.blue_dot1
            3 -> hu.bme.aut.android.chainreaction.R.drawable.green_dot1
            4 -> hu.bme.aut.android.chainreaction.R.drawable.yellow_dot1
            5 -> hu.bme.aut.android.chainreaction.R.drawable.orange_dot1
            6 -> hu.bme.aut.android.chainreaction.R.drawable.pink_dot1
            7 -> hu.bme.aut.android.chainreaction.R.drawable.brown_dot1
            8 -> hu.bme.aut.android.chainreaction.R.drawable.grey_dot1
            else -> { // Note the block
                hu.bme.aut.android.chainreaction.R.drawable.nothing
            }
        }

    }

}