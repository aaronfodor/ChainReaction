package View

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.support.v7.widget.LinearLayoutManager
import Presenter.PlayerListAdapter
import Presenter.PlayerListData
import android.widget.Toast

/**
 * Activity of settings of game play
 */
class StartActivity : AppCompatActivity() {

    private val MAXIMUM_ALLOWED_PLAYER_NUMBER = 8
    private val MINIMUM_PLAYER_NUMBER_TO_START = 2
    var PlayerListData = ArrayList<PlayerListData>()
    lateinit var adapter: PlayerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(hu.bme.aut.android.chainreaction.R.layout.activity_start)

        val AddHumanPlayerButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonAddHumanPlayer)
        AddHumanPlayerButton.setOnClickListener {

            if(adapter.itemCount < MAXIMUM_ALLOWED_PLAYER_NUMBER){

                adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"human", imageAdder(adapter.itemCount+1)))

                Toast.makeText(
                    this,
                    hu.bme.aut.android.chainreaction.R.string.humanplayeradd,
                    Toast.LENGTH_LONG
                ).show()

            }

            else{

                Toast.makeText(
                    this,
                    hu.bme.aut.android.chainreaction.R.string.maximumreached,
                    Toast.LENGTH_LONG
                ).show()

            }

        }

        val AddAIPlayerButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonAddAIPlayer)
        AddAIPlayerButton.setOnClickListener {

            if(adapter.itemCount < MAXIMUM_ALLOWED_PLAYER_NUMBER){

                adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"AI", imageAdder(adapter.itemCount+1)))

                Toast.makeText(
                    this,
                    hu.bme.aut.android.chainreaction.R.string.aiplayeradd,
                    Toast.LENGTH_LONG
                ).show()

            }

            else{

                Toast.makeText(
                    this,
                    hu.bme.aut.android.chainreaction.R.string.maximumreached,
                    Toast.LENGTH_LONG
                ).show()

            }

        }

        val ClearPlayersButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonClearPlayers)
        ClearPlayersButton.setOnClickListener {
            adapter.Clear()

                Toast.makeText(
                    this,
                    hu.bme.aut.android.chainreaction.R.string.listclear,
                    Toast.LENGTH_LONG
                ).show()

        }

        val StartGameButton = findViewById<Button>(hu.bme.aut.android.chainreaction.R.id.buttonStartGame)
        StartGameButton.setOnClickListener {

            if(adapter.itemCount >= MINIMUM_PLAYER_NUMBER_TO_START){

                val myIntent = Intent(this, GameActivity::class.java)

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

        adapter = PlayerListAdapter(PlayerListData)
        adapter.addItem(PlayerListData("Player 1", "human", imageAdder(1)))
        adapter.addItem(PlayerListData("Player 2", "AI", imageAdder(2)))

        var recyclerView = findViewById<RecyclerView>(hu.bme.aut.android.chainreaction.R.id.recyclerViewPlayers)
        //recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    fun imageAdder(Id: Int): Int {

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