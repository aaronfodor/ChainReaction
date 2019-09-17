package com.arpadfodor.android.chain_reaction.presenter

/**
 * MVP hu.bme.aut.android.chain_reaction.view
 * StartCustomPresenter calls the hu.bme.aut.android.chain_reaction.view via this interface
 */
interface IStartCustomView {

    /**
     * Shows the user that a Player has been added
     *
     * @param   playerNumber    Id of the Player
     */
    fun playerAdded(playerNumber: Int)

    /**
     * Shows the user that Players list is full
     */
    fun playersFull()

    /**
     * Shows the user that Players list has been cleared
     */
    fun playersCleared()

    /**
     * Shows the user that there are not enough Players to start
     */
    fun notEnoughPlayer()

    /**
     * Shows the user that maximum size has been reached
     */
    fun maximumSizeReached()

    /**
     * Shows the user that minimum size has been reached
     */
    fun minimumSizeReached()

    /**
     * Shows the user the current dimension values
     *
     * @param    dimension1     1st dimension value
     * @param    dimension2     2nd dimension value
     */
    fun updateDimensionText(dimension1: Int, dimension2: Int)

    /**
     * Shows the user that random generating happened
     */
    fun randomGenerated()

}