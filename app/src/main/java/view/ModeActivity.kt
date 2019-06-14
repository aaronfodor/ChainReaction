package view

import android.app.ActivityOptions
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.google.android.gms.ads.AdView
import hu.bme.aut.android.chainreaction.R
import kotlinx.android.synthetic.main.activity_mode.*
import presenter.AdPresenter

/**
 * Main Activity - entry point
 */
class ModeActivity : AppCompatActivity() {

    companion object {
        private const val CLASSIC_GAME = 1
        private const val DYNAMIC_GAME = 2
        private const val CAMPAIGN_GAME = 3
    }

    /**
     * Advertisement of the activity
     */
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_mode)

        buttonCampaignGame.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            intent.putExtra("GameType", CAMPAIGN_GAME)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonClassicGame.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            intent.putExtra("GameType", CLASSIC_GAME)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonDynamicGame.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            intent.putExtra("GameType", DYNAMIC_GAME)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        mAdView = findViewById(R.id.modeAdView)
        //loading the advertisement
        AdPresenter.loadAd(mAdView)

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
