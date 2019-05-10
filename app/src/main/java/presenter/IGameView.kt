package presenter

/**
 * MVP view
 * GamePresenter calls the view via this interface
 */
interface IGameView {

    /**
     * Draws the selected Playground Field
     *
     * @param    pos_y       Y coordinate
     * @param    pos_x       X coordinate
     * @param    color       Color of the Field
     * @param    number      elements of the Field
     * @return    boolean     True if succeed, false otherwise
     */
    fun refreshPlayground(pos_y: Int, pos_x: Int, color: Int, number: Int): Boolean

    /**
     * Shows whose turn is now
     *
     * @param    Id          Id of the current Player
     * @return    boolean     True if succeed, false otherwise
     */
    fun showCurrentPlayer(Id: Int): Boolean

    /**
     * Shows a message from the Presenter
     *
     * @param    msg         Message
     * @return    boolean     True if succeed, false otherwise
     */
    fun showMessage(msg: String): Boolean

    /**
     * Shows the result of the game play, displays GameOverFragment
     *
     * @param     winnerId    Id of the winner
     * @param     playersData Players data. [i] is the Player index, [][0] is Player Id, [][1] is the average step time of Player, [][2] is the number of rounds of Player
     * @return    boolean     True if succeed, false otherwise
     */
    fun showResult(winnerId: Int, playersData: Array<IntArray>): Boolean

    /**
     * Shows the start text from the Presenter
     *
     * @param       Id          Id of the current Player
     * @return      boolean     True if succeed, false otherwise
     */
    fun showStart(Id: Int): Boolean

    /**
     * Refresh the progress bar state with the given value
     *
     * @param    value       The progress bar state value - between 0 and 100
     */
    fun refreshProgressBar(value: Int)

}
