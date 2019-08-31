package com.arpadfodor.android.chain_reaction.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.arpadfodor.android.chain_reaction.R
import kotlinx.android.synthetic.main.activity_type.*
import com.arpadfodor.android.chain_reaction.view.subclass.AdActivity

/**
 * Type Activity - selects the game type to play
 */
class TypeActivity : AdActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_type)

        //adding buttons to the activity register to animate all of them
        this.addButtonToRegister(buttonChallengeGame)
        this.addButtonToRegister(buttonCustomGame)

        buttonChallengeGame.setOnClickEvent {
            val intent = Intent(this, StartChallengeActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        buttonCustomGame.setOnClickEvent {
            val intent = Intent(this, StartCustomActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        initActivityAd(findViewById(R.id.modeAdView))

    }

    /**
     * Step back to the main activity
     */
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        super.onBackPressed()
    }

}
