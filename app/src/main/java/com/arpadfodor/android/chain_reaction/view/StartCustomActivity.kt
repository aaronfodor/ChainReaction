package com.arpadfodor.android.chain_reaction.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.arpadfodor.android.chain_reaction.R
import kotlinx.android.synthetic.main.activity_start_custom.*
import com.arpadfodor.android.chain_reaction.presenter.*
import com.arpadfodor.android.chain_reaction.view.subclass.AdActivity
import com.arpadfodor.android.chain_reaction.view.subclass.BaseButton
import com.arpadfodor.android.chain_reaction.view.subclass.MainButton

/**
 * Activity of creating a custom game play
 */
class StartCustomActivity : AdActivity(), IStartCustomView {

    companion object {
        private const val CUSTOM_GAME = 1
        private const val NORMAL_MODE = 1
    }

    /**
     * Presenter of the view
     */
    private lateinit var presenter: StartCustomPresenter

    private var gameType = CUSTOM_GAME
    private var gameMode = NORMAL_MODE

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_start_custom)

        presenter = StartCustomPresenter(this, this.applicationContext)

        val gameTypeStartView = findViewById<ImageView>(R.id.gameTypeStartCustomView)
        gameTypeStartView.setImageDrawable(resources.getDrawable(R.drawable.game_mode_custom))

        val addHumanPlayerButton = findViewById<BaseButton>(R.id.buttonAddHumanPlayer)
        val addAIPlayerButton = findViewById<BaseButton>(R.id.buttonAddAIPlayer)
        val clearPlayersButton = findViewById<BaseButton>(R.id.buttonClearPlayers)
        val startGameButton = findViewById<MainButton>(R.id.buttonStartGame)
        val heightPlusButton = findViewById<BaseButton>(R.id.buttonHeightPlus)
        val heightMinusButton = findViewById<BaseButton>(R.id.buttonHeightMinus)
        val widthPlusButton = findViewById<BaseButton>(R.id.buttonWidthPlus)
        val widthMinusButton = findViewById<BaseButton>(R.id.buttonWidthMinus)
        val randomButton = findViewById<BaseButton>(R.id.buttonRandom)

        //adding buttons to the activity register to animate all of them
        this.addButtonToRegister(addHumanPlayerButton)
        this.addButtonToRegister(addAIPlayerButton)
        this.addButtonToRegister(clearPlayersButton)
        this.addButtonToRegister(startGameButton)
        this.addButtonToRegister(heightPlusButton)
        this.addButtonToRegister(heightMinusButton)
        this.addButtonToRegister(widthPlusButton)
        this.addButtonToRegister(widthMinusButton)
        this.addButtonToRegister(randomButton)

        addHumanPlayerButton.setOnClickEvent {
            presenter.addHumanPlayer()
        }

        addAIPlayerButton.setOnClickEvent {
            presenter.addAIPlayer()
        }

        clearPlayersButton.setOnClickEvent {
            presenter.clearPlayers()
        }

        startGameButton.setOnClickEvent {

            if(presenter.canGameBeStarted()){

                val myIntent = Intent(this, GameActivity::class.java)
                myIntent.putExtra("number_of_players", presenter.getPlayerCount())
                myIntent.putExtra("PlayGroundHeight", presenter.getPlayGroundHeight())
                myIntent.putExtra("PlayGroundWidth", presenter.getPlayGroundWidth())
                myIntent.putExtra("GameType", gameType)
                myIntent.putExtra("GameMode", gameMode)
                myIntent.putExtra("ChallengeLevel", 0)

                for(i in 0 until presenter.getPlayerCount()){
                    myIntent.putExtra((i+1).toString(), presenter.getPlayerStringElementAt(i))
                }

                startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

            }

        }

        heightPlusButton.setOnClickEvent {
            presenter.dim1Plus()
        }

        heightMinusButton.setOnClickEvent {
            presenter.dim1Minus()
        }

        widthPlusButton.setOnClickEvent {
            presenter.dim2Plus()
        }

        widthMinusButton.setOnClickEvent {
            presenter.dim2Minus()
        }

        randomButton.setOnClickEvent {
            presenter.randomConfig()
        }

        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerViewPlayers)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter = presenter.getAdapter()

        updateDimensionText(presenter.getPlayGroundHeight(), presenter.getPlayGroundWidth())

        initActivityAd(findViewById(R.id.startCustomAdView))

    }

    /**
     * Shows the user that a Player has been added
     *
     * @param   playerNumber    Id of the Player
     */
    override fun playerAdded(playerNumber: Int){
        Snackbar.make(recyclerViewPlayers, getString(R.string.player_added, (playerNumber)), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Shows the user that Players list is full
     */
    override fun playersFull(){
        Snackbar.make(recyclerViewPlayers, R.string.maximum_reached, Snackbar.LENGTH_LONG).show()
    }

    /**
     * Shows the user that Players list has been cleared
     */
    override fun playersCleared(){
        Snackbar.make(recyclerViewPlayers, R.string.list_clear, Snackbar.LENGTH_LONG).show()
    }

    /**
     * Shows the user that there are not enough Players to start
     */
    override fun notEnoughPlayer(){
        Snackbar.make(recyclerViewPlayers, R.string.not_enough_player, Snackbar.LENGTH_LONG).show()
    }

    /**
     * Shows the user that maximum size has been reached
     */
    override fun maximumSizeReached(){
        Snackbar.make(recyclerViewPlayers, R.string.maximum_size, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Shows the user that minimum size has been reached
     */
    override fun minimumSizeReached(){
        Snackbar.make(recyclerViewPlayers, R.string.minimum_size, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Shows the user that random generating happened
     */
    override fun randomGenerated(){
        Snackbar.make(recyclerViewPlayers, R.string.random_generated, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Shows the user the current dimension values
     *
     * @param    dimension1     1st dimension value
     * @param    dimension2     2nd dimension value
     */
    override fun updateDimensionText(dimension1: Int, dimension2: Int){
        val dim1TextView = findViewById<TextView>(R.id.tvDim1)
        val dim2TextView = findViewById<TextView>(R.id.tvDim2)

        if(dimension1 >= dimension2){
            dim1TextView.text = getString(R.string.height_show, dimension1)
            dim2TextView.text = getString(R.string.width_show, dimension2)
        }
        else{
            dim1TextView.text = getString(R.string.width_show, dimension1)
            dim2TextView.text = getString(R.string.height_show, dimension2)
        }
    }

    /**
     * Step back to the type activity
     */
    override fun onBackPressed() {
        startActivity(Intent(this, TypeActivity::class.java))
        super.onBackPressed()
    }

}