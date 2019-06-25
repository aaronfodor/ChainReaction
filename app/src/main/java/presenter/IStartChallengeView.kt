package presenter

/**
 * MVP view
 * StartCustomPresenter calls the view via this interface
 */
interface IStartChallengeView {

    /**
     * Reads the challenge database, saves it into a list, then passes it to the view
     */
    fun challengeDatabaseReader()

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
     * Shows the user the current level
     *
     * @param    value          Current level name
     */
    fun updateCurrentLevelText(value: String)

    /**
     * Reminds the user that the current level is not completed
     */
    fun showLockedReminder()

}