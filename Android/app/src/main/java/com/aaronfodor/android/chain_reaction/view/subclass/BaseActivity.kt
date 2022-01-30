package com.aaronfodor.android.chain_reaction.view.subclass

import androidx.appcompat.app.AppCompatActivity
import com.aaronfodor.android.chain_reaction.model.AudioService

/**
 * Base activity of the app
 */
abstract class BaseActivity : AppCompatActivity(){

    /**
     * Buttons of the activity
     */
    var activityButtons = ArrayList<BaseButton>()

    /**
     * Add a button to the BaseButton register of the activity
     *
     * @param    button        Button to add
     */
    fun addButtonToRegister(button: BaseButton){
        activityButtons.add(button)
    }

    /**
     * Play sound when back is pressed
     */
    override fun onBackPressed() {
        AudioService.soundBackClick()
        super.onBackPressed()
    }

    /**
     * Called when returning to the activity - starts BaseButton appearing animation
     */
    override fun onResume() {
        super.onResume()
        for(button in activityButtons){
            button.startAppearingAnimation()
        }
    }

}