package presenter

/**
 * MVP view
 * LevelSlidePagerAdapter (acts as a presenter) calls the view via this interface
 */
interface IStartChallengeView {

    /**
     * Reads the challenge database, saves it into a list, then passes it to the view
     */
    fun challengeDatabaseReader()

}