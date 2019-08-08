package view.subclass

import com.google.android.gms.ads.AdView
import presenter.AdPresenter

/**
 * Activity having advertisement
 */
abstract class AdActivity : BaseActivity(){

    /**
     * Advertisement of the activity
     */
    private lateinit var mAdView : AdView

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