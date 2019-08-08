package view.subclass

import android.support.v4.app.Fragment

/**
 * Base fragment of the app
 */
abstract class BaseFragment : Fragment() {

    /**
     * Buttons of the fragment
     */
    var fragmentButtons = ArrayList<BaseButton?>()

    /**
     * Add a button to the BaseButton register of the fragment
     */
    fun addButtonToRegister(button: BaseButton?){
        fragmentButtons.add(button)
    }

    /**
     * Called when returning to the fragment - starts BaseButton appearing animation
     */
    override fun onResume() {
        super.onResume()
        for(button in fragmentButtons){
            button?.startAppearingAnimation()
        }
    }

}