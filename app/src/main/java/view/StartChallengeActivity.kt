package view

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.WindowManager
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import hu.bme.aut.android.chain_reaction.R
import model.db.DbDefaults
import model.db.challenge.ChallengeDatabase
import model.db.challenge.ChallengeLevel
import presenter.LevelSlidePagerAdapter
import presenter.ViewPagerPageChangeListener
import view.subclass.AdActivity

/**
 * Activity of starting a challenge game play
 */
class StartChallengeActivity : AdActivity() {

    companion object {
        private const val CHALLENGE_GAME = 2

        private const val NORMAL_MODE = 1
        private const val DYNAMIC_MODE = 2
    }

    private var gameType = CHALLENGE_GAME
    private var gameMode = NORMAL_MODE

    /**
     * The pager adapter
     */
    private lateinit var pagerAdapter: LevelSlidePagerAdapter

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous and next fragments
     */
    private lateinit var mPager: ViewPager

    /**
     * The pager titles
     */
    private lateinit var mPagerTitles: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_start_challenge)

        //Instantiate a ViewPager
        mPager = findViewById(R.id.levelsPager)
        //bring to front to make snack bar visible even when ad is shown
        mPager.bringToFront()
        mPager.addOnPageChangeListener(ViewPagerPageChangeListener)
        //Instantiate the titles shown in the ViewPager
        mPagerTitles = findViewById(R.id.levelPagerTitles)
        mPagerTitles.setupWithViewPager(mPager)

        initActivityAd(findViewById(R.id.startChallengeAdView))

        challengeDatabaseReader()

    }

    /**
     * Reads the challenge database, saves it into a list, then passes it to the view
     */
    private fun challengeDatabaseReader() {

        val dbChallenge = Room.databaseBuilder(applicationContext, ChallengeDatabase::class.java, "db_challenge").build()
        var challengeLevels : MutableList<ChallengeLevel>

        Thread {

            //adapter start position to show
            var startAdapterPosition = 0

            challengeLevels = dbChallenge.challengeLevelsDAO().getAll().toMutableList()

            for(level in challengeLevels){
                if(level.Completed){
                    startAdapterPosition++
                }
            }

            if(challengeLevels.isEmpty()){
                val defaultCampaignStates = DbDefaults.challengeDatabaseDefaults()

                for(level in defaultCampaignStates){
                    dbChallenge.challengeLevelsDAO().insert(level)
                }

                challengeLevels = dbChallenge.challengeLevelsDAO().getAll().toMutableList()
            }

            dbChallenge.close()

            runOnUiThread {
                // The pager adapter, which provides the pages to the view pager widget
                pagerAdapter = LevelSlidePagerAdapter(challengeLevels, this, supportFragmentManager)
                mPager.adapter = pagerAdapter
                mPager.currentItem = startAdapterPosition
                mPager.setPageTransformer(true, CubeOutTransformer())
            }

        }.start()

    }

    /**
     * Step back to the type activity
     */
    override fun onBackPressed() {
        startActivity(Intent(this, TypeActivity::class.java))
        super.onBackPressed()
    }

}