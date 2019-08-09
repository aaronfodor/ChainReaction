package view

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.WindowManager
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import hu.bme.aut.android.chain_reaction.R
import presenter.MoreSlidePagerAdapter
import presenter.ViewPagerPageChangeListener
import view.subclass.AdActivity

class MoreActivity : AdActivity() {

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
        setContentView(R.layout.activity_more)

        //Instantiate a ViewPager
        mPager = findViewById(R.id.morePager)
        //Instantiate the titles shown in the ViewPager
        mPagerTitles = findViewById(R.id.morePagerTitles)
        mPagerTitles.setupWithViewPager(mPager)

        // The pager adapter, which provides the pages to the view pager widget
        val pagerAdapter = MoreSlidePagerAdapter(supportFragmentManager, getString(R.string.how_to_play_title), getString(R.string.about_title))
        mPager.adapter = pagerAdapter
        mPager.addOnPageChangeListener(ViewPagerPageChangeListener)
        mPager.setPageTransformer(true, CubeOutTransformer())

        initActivityAd(findViewById(R.id.moreAdView))

    }

    /**
     * Step back to the main activity
     */
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        super.onBackPressed()
    }

}