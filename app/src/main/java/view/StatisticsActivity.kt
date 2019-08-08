package view

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.WindowManager
import hu.bme.aut.android.chain_reaction.R
import android.support.v4.view.ViewPager
import presenter.StatisticsSlidePagerAdapter
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import presenter.ViewPagerPageChangeListener
import view.subclass.AdActivity

class StatisticsActivity : AdActivity() {

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
        setContentView(R.layout.activity_statistics)

        //Instantiate a ViewPager
        mPager = findViewById(R.id.statsPager)
        //Instantiate the titles shown in the ViewPager
        mPagerTitles = findViewById<TabLayout>(R.id.statsPagerTitles)
        mPagerTitles.setupWithViewPager(mPager)

        // The pager adapter, which provides the pages to the view pager widget
        val pagerAdapter = StatisticsSlidePagerAdapter(supportFragmentManager, getString(R.string.statistics), getString(R.string.challenge))
        mPager.adapter = pagerAdapter
        mPager.addOnPageChangeListener(ViewPagerPageChangeListener)
        mPager.setPageTransformer(true, CubeOutTransformer())

        initActivityAd(findViewById(R.id.statsAdView))

    }

    /**
     * Step back to the main activity
     */
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

}