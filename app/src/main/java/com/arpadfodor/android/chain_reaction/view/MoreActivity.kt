package com.arpadfodor.android.chain_reaction.view

import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import android.view.WindowManager
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import com.arpadfodor.android.chain_reaction.R
import com.arpadfodor.android.chain_reaction.presenter.MoreSlidePagerAdapter
import com.arpadfodor.android.chain_reaction.presenter.ViewPagerPageChangeListener
import com.arpadfodor.android.chain_reaction.view.subclass.AdActivity

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

        // The pager adapter, which provides the pages to the hu.bme.aut.android.chain_reaction.view pager widget
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