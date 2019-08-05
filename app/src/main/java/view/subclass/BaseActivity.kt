package view.subclass

import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.AdView
import presenter.AdPresenter
import presenter.AudioPresenter

/**
 * Base activity of the app
 */
abstract class BaseActivity : AppCompatActivity(){

    /**
     * Advertisement of the activity
     */
    lateinit var mAdView : AdView

    /**
     * Initialize the advertisement of the Activity
     *
     *  @param   adView     AdView to initialize
     */
    fun initActivityAd(adView: AdView) {
        mAdView = adView
        //loading the advertisement
        AdPresenter.loadAd(mAdView)
    }

    /**
     * Play sound when back is pressed
     */
    override fun onBackPressed() {
        super.onBackPressed()
        AudioPresenter.soundBackClick()
    }

    /**
     * Called when leaving the activity - stops the ad
     */
    override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    /**
     * Called when returning to the activity - starts the ad
     */
    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    /**
     * Called before the activity is destroyed - destroys the ad
     */
    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

}