package com.arpadfodor.android.chain_reaction.view

import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import android.view.WindowManager
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import com.arpadfodor.android.chain_reaction.R
import com.arpadfodor.android.chain_reaction.presenter.StatisticsSlidePagerAdapter
import com.arpadfodor.android.chain_reaction.presenter.ViewPagerPageChangeListener
import com.arpadfodor.android.chain_reaction.view.subclass.AdActivity

class StatisticsActivity : AdActivity() {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous and next fragments
     */
    private lateinit var mPager: androidx.viewpager.widget.ViewPager

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

        // The pager adapter, which provides the pages to the hu.bme.aut.android.chain_reaction.view pager widget
        val pagerAdapter = StatisticsSlidePagerAdapter(supportFragmentManager, getString(R.string.custom), getString(R.string.challenge))
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
        super.onBackPressed()
    }

}