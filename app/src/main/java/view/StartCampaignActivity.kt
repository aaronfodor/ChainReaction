package view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdView
import hu.bme.aut.android.chainreaction.R
import presenter.PlayerListAdapter
import presenter.PlayerListData
import presenter.AdPresenter
import presenter.PlayerVisualRepresentation
import java.util.*

/**
 * Activity of starting a campaign game play
 */
class StartCampaignActivity : AppCompatActivity() {

    companion object {
        private const val CAMPAIGN_GAME = 2

        private const val NORMAL_MODE = 1
        private const val DYNAMIC_MODE = 2
    }

    private var playGroundHeight = 7
    private var playGroundWidth = 5
    private var playerListData = ArrayList<PlayerListData>()
    private lateinit var adapter: PlayerListAdapter
    private var gameType = CAMPAIGN_GAME
    private var gameMode = NORMAL_MODE

    /**
     * Advertisement of the activity
     */
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_start_campaign)

        val gameTypeStartView = findViewById<ImageView>(R.id.gameTypeStartCampaignView)
        gameTypeStartView.setImageDrawable(resources.getDrawable(R.drawable.game_mode_campaign))

        val isLevelCompletedView = findViewById<ImageView>(R.id.isCompletedLevelView)
        isLevelCompletedView.setImageDrawable(resources.getDrawable(R.drawable.uncompleted))

        val startGameButton = findViewById<Button>(R.id.buttonStartCampaignGame)
        startGameButton.setOnClickListener {

            val myIntent = Intent(this, GameActivity::class.java)
            myIntent.putExtra("number_of_players", adapter.itemCount)
            myIntent.putExtra("PlayGroundHeight", playGroundHeight)
            myIntent.putExtra("PlayGroundWidth", playGroundWidth)
            myIntent.putExtra("GameType", gameType)
            myIntent.putExtra("GameMode", gameMode)
            //TODO start the selected level - DB communication?
            myIntent.putExtra("CampaignLevel", 1)

            for(i in 0 until adapter.itemCount){
                myIntent.putExtra((i+1).toString(), adapter.stringElementAt(i))
            }

            //startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            //this.finish()

        }

        adapter = PlayerListAdapter(applicationContext, playerListData)
        adapter.addItem(PlayerListData("Player 1", "human", imageAdder(1)))
        adapter.addItem(PlayerListData("Player 2", "AI", imageAdder(2)))
        adapter.addItem(PlayerListData("Player 3", "AI", imageAdder(3)))
        adapter.addItem(PlayerListData("Player 4", "AI", imageAdder(4)))
        adapter.addItem(PlayerListData("Player 5", "AI", imageAdder(5)))
        adapter.addItem(PlayerListData("Player 6", "AI", imageAdder(6)))
        adapter.addItem(PlayerListData("Player 7", "AI", imageAdder(7)))
        adapter.addItem(PlayerListData("Player 8", "AI", imageAdder(8)))

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCampaignPlayers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val heightTextView = findViewById<TextView>(R.id.tvCampaignHeight)
        heightTextView.text = getString(R.string.height_show, playGroundHeight)

        val widthTextView = findViewById<TextView>(R.id.tvCampaignWidth)
        widthTextView.text = getString(R.string.width_show, playGroundWidth)

        val currentLevelView = findViewById<TextView>(R.id.tvCurrentLevel)
        currentLevelView.text = "Level 1"

        val gamePlayableImageView = findViewById<ImageView>(R.id.ivGamePlayable)
        gamePlayableImageView.setImageDrawable(resources.getDrawable(R.drawable.locked))

        mAdView = findViewById(R.id.startCampaignAdView)
        //loading the advertisement
        AdPresenter.loadAd(mAdView)

    }

    private fun imageAdder(Id: Int): Int {
        return PlayerVisualRepresentation.getDotsImageIdByColorAndNumber(Id, 1, false)
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