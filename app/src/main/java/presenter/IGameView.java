package presenter;

/**
 * MVP view
 * GamePresenter calls the view via this interface
 */
public interface IGameView {

    /**
     * Draws the selected Playground Field
     *
     * @param	pos_y       Y coordinate
     * @param	pos_x       X coordinate
     * @param	color       Color of the Field
     * @param	number      elements of the Field
     * @return 	boolean     True if succeed, false otherwise
     */
    boolean RefreshPlayground(int pos_y, int pos_x, int color, int number);

    /**
     * Shows whose turn is now
     *
     * @param	Id          Id of the current Player
     * @return 	boolean     True if succeed, false otherwise
     */
    boolean ShowCurrentPlayer(int Id);

    /**
     * Shows a message from the Presenter
     *
     * @param	msg         Message
     * @return 	boolean     True if succeed, false otherwise
     */
    boolean ShowMessage(String msg);

    /**
     * Shows the result of the game play, displays GameOverFragment
     *
     * @param     winnerId    Id of the winner
     * @param     playersData Players data. [i] is the Player index, [][0] is Player Id, [][1] is the average step time of Player, [][2] is the number of rounds of Player
     * @return    boolean     True if succeed, false otherwise
     */
    boolean ShowResult(int winnerId, int[][] playersData);

    /**
     * Shows the start text from the Presenter
     *
     * @param       Id          Id of the current Player
     * @return      boolean     True if succeed, false otherwise
     */
    boolean ShowStart(int Id);

}
