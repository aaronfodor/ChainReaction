package presenter

/**
 * MVP view
 * StartCustomPresenter calls the view via this interface
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
     * Shows the user that random generating happened
     */
    fun randomGenerated()

}