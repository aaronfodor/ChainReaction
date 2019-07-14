package view

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import com.google.android.gms.ads.AdView
import hu.bme.aut.android.chain_reaction.R
import presenter.AboutSlidePagerAdapter
import presenter.AdPresenter

class AboutActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_about)

        //Instantiate a ViewPager
        mPager = findViewById(R.id.aboutPager)
        //Instantiate the titles shown in the ViewPager
        mPagerTitles = findViewById(R.id.aboutPagerTitles)
        mPagerTitles.setupWithViewPager(mPager)

        // The pager adapter, which provides the pages to the view pager widget
        val pagerAdapter = AboutSlidePagerAdapter(supportFragmentManager, getString(R.string.how_to_play_title), getString(R.string.app_info_title))
        mPager.adapter = pagerAdapter
        mPager.setPageTransformer(true, CubeOutTransformer())

        mAdView = this.findViewById(R.id.aboutAdView)
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