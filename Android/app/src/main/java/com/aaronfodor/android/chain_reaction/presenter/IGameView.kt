package com.aaronfodor.android.chain_reaction.presenter

/**
 * MVP view
 * GamePresenter calls the view via this interface
 */
interface IGameView {

    /**
     * Draws the selected Playground Field if a new image is required to be set
     *
     * @param    pos_y                  Y coordinate
     * @param    pos_x                  X coordinate
     * @param    color                  Color of the Field
     * @param    number                 elements of the Field
     * @param    isCloseToExplosion     maximum allowed elements on the Field
     * @param    gifEnabled             whether moving image is enabled or not
     * @return   boolean                True if succeed, false otherwise
     */
    fun refreshPlaygroundFieldAt(pos_y: Int, pos_x: Int, color: Int, number: Int, isCloseToExplosion: Boolean, gifEnabled: Boolean): Boolean

    /**
     * Shows whose turn is now
     * Starts time counting from screen refresh
     *
     * @param       Id                          Id of the current Player
     * @param       isAI                        True means the current is an AI Player, false means human
     * @param       refreshBackgroundDelayMs    Milliseconds to wait before refreshing the background
     * @return      boolean                     True if succeed, false otherwise
     */
    fun showCurrentPlayer(Id: Int, isAI: Boolean, refreshBackgroundDelayMs: Int): Boolean

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
     * @param     winnerId          Id of the winner
     * @param     playersData       Players data. [i] is the Player index, [][0] is Player Id, [][1] is the average step time of Player, [][2] is the number of rounds of Player, [][3] is the type of the Player (1:human, 2:AI)
     * @param     humanVsAiGame     True is human and AI played in the game, false otherwise
     * @param     isWinnerAI        True means the winner is an AI Player
     * @return    boolean           True if succeed, false otherwise
     */
    fun showResult(winnerId: Int, playersData: Array<IntArray>, humanVsAiGame: Boolean, isWinnerAI: Boolean): Boolean

    /**
     * Shows the start text from the Presenter
     *
     * @param       Id          Id of the current Player
     * @param       showAI      True means the current is an AI Player, false means human
     * @return      boolean     True if succeed, false otherwise
     */
    fun showStart(Id: Int, showAI: Boolean): Boolean

    /**
     * Refresh the progress bar state with the given value
     *
     * @param    value       The progress bar state value - between 0 and 100
     */
    fun refreshProgressBar(value: Int)

    /**
     * Updates statistics the database
     * Increments the overall number of victories of the winner's type and saves it, increments all games counter
     * Updates campaign database if the game was a campaign game
     *
     * @param     playerType    Type of the winner. 1 means human, 2 means AI
     * @param     humanVsAiGame True is human and AI played in the game, false otherwise
     */
    fun statisticsDatabaseUpdater(playerType: Int, humanVsAiGame: Boolean)

    /**
     * Updates challenge database
     *
     * @param     challengeLevelId   Challenge level Id to save that it has been completed
     */
    fun challengeDatabaseUpdater(challengeLevelId: Int)

    /**
     * Shows the game over message
     *
     * @param     restartOffer      True means restart action will be offered
     */
    fun showEndOfGameMessage(restartOffer: Boolean)

    /**
     * Shows the restart campaign message
     */
    fun showRestartChallengeLevelMessage()

    /**
     * Shows the next campaign message
     */
    fun showNextChallengeLevelMessage()

    /**
     * Shows the all campaigns completed message
     */
    fun showAllChallengesCompletedMessage()

}
