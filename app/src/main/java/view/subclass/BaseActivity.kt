package view.subclass

import android.support.v7.app.AppCompatActivity
import presenter.AudioPresenter

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
     * Finishes the activity
     */
    override fun onBackPressed() {
        AudioPresenter.soundBackClick()
        super.onBackPressed()
        this.finish()
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