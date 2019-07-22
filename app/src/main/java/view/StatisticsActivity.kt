package view

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import hu.bme.aut.android.chain_reaction.R
import com.google.android.gms.ads.AdView
import presenter.AdPresenter
import android.support.v4.view.ViewPager
import presenter.StatisticsSlidePagerAdapter
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import presenter.AudioPresenter
import presenter.ViewPagerPageChangeListener

class StatisticsActivity : AppCompatActivity() {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous and next fragments
     */
    private lateinit var mPager: ViewPager

    /**
     * The pager titles
     */
    private lateinit var mPagerTitles: TabLayout

    /**
     * Advertisement of the activity
     */
    lateinit var mAdView : AdView

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

        mAdView = this.findViewById(R.id.statsAdView)
        //loading the advertisement
        AdPresenter.loadAd(mAdView)

    }

    /**
     * Called when leaving the activity
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