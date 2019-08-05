package view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import hu.bme.aut.android.chain_reaction.R
import kotlinx.android.synthetic.main.activity_type.*
import presenter.AudioPresenter
import view.subclass.BaseActivity

/**
 * Type Activity - selects the game type to play
 */
class TypeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_type)

        buttonChallengeGame.setOnClickListener {
            AudioPresenter.soundButtonClick()
            val intent = Intent(this, StartChallengeActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonCustomGame.setOnClickListener {
            AudioPresenter.soundButtonClick()
            val intent = Intent(this, StartCustomActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        initActivityAd(findViewById(R.id.modeAdView))

    }

}
