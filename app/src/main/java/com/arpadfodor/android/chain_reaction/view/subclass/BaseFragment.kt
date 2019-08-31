package com.arpadfodor.android.chain_reaction.view.subclass

/**
 * Base fragment of the app
 */
abstract class BaseFragment : androidx.fragment.app.Fragment() {

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