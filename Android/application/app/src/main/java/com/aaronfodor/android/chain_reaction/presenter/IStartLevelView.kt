package com.aaronfodor.android.chain_reaction.presenter

/**
 * MVP hu.bme.aut.android.chain_reaction.view
 * StartCustomPresenter calls the hu.bme.aut.android.chain_reaction.view via this interface
 */
interface IStartLevelView {

    /**
     * Shows the user that level can be played
     */
    fun showUnlocked()

    /**
     * Shows the user that level is locked
     */
    fun showLocked()

    /**
     * Shows the user that level has been completed
     */
    fun showCompleted()

    /**
     * Shows the user that level is not completed
     */
    fun showUncompleted()

    /**
     * Shows the user the current height
     *
     * @param    value          Height value to show
     */
    fun updateHeightText(value: Int)

    /**
     * Shows the user the current width
     *
     * @param    value          Width value to show
     */
    fun updateWidthText(value: Int)

    /**
     * Reminds the user that the current level is not completed
     */
    fun showLockedReminder()

}