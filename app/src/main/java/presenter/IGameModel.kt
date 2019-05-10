package presenter

/**
 * MVP model
 * GamePresenter calls the model via this interface
 */
interface IGameModel {

    /**
     * Returns the Id of the current Player
     *
     * @return    Integer     Id of the current Player
     */
    val actualPlayerId: Int?

    /**
     * Returns the Type of the current Player
     *
     * @return    Integer     Type of the current Player - 1 means human, 2 means AI
     */
    val actualPlayerType: Int?

    /**
     * Returns the Id of the last Player
     *
     * @return    Integer     Id of the last Player
     */
    val lastPlayerId: Int?

    /**
     * Returns the automatic stepping coordinates
     *
     * @return    Integer[]     Automatic stepping coordinates of the current Player: [0] is the y coordinate, [1] is the x coordinate. If not available, returns null
     */
    val autoCoordinates: Array<Int>?

    /**
     * Average waiting time getter method from the current Player
     *
     * @return    int[][]     Players data. [i] is the Player index, [][0] is Player Id, [][1] is the average step time of Player, [][2] is the number of rounds of Player
     */
    val playersData: Array<IntArray>?

    /**
     * Starts the actual GamePlay
     *
     * @return    int     Id of the Player to start the game with
     */
    fun startGame(): Int

    /**
     * Requesting a step on behalf of the current player based on the state machine status
     *
     * @param    pos_y       Y coordinate
     * @param    pos_x       X coordinate
     * @return    int         minus value is the (-1)*Id of the winner; positive value is the Id of the current Player; 0 means step was unsuccessful
     */
    fun stepRequest(pos_y: Int, pos_x: Int): Int

    /**
     * Provides the actual Playground info - Player and it's elements on Field
     * First index is the Y coordinate of Field
     * Second index is the X coordinate of Field
     * Third index is the Field specific information: [0] is the owner Player's Id, [1] is the number of elements on the Field, [2] is the number of residual elements left before explosion
     *
     * @return    int[][][]   Field information matrix
     */
    fun actualPlaygroundInfo(): Array<Array<IntArray>>

    /**
     * History playground info builder
     */
    fun historyPlaygroundBuilder()

    /**
     * Provides the actual Playground info - Player and it's elements on Field
     * First index is the Y coordinate of Field
     * Second index is the X coordinate of Field
     * Third index is the Field specific information: [0] is the owner Player's Id, [1] is the number of elements on the Field, [2] is the number of residual elements left before explosion
     *
     * @return    int[][][]   Field information matrix
     */
    fun historyPlaygroundInfoAt(propagation_depth: Int): Array<Array<IntArray>>

    /**
     * Provides the dimensions of the Playground
     * [0] is the width of the Playground
     * [1] is the height of the Playground
     *
     * @return    int[]   Playground dimension info
     */
    fun getDimension(): IntArray

    /**
     * Waiting time adder method to the current Player
     *
     * @param    duration    Waiting duration of the current Player
     */
    fun addCurrentPlayerWaitingTime(duration: Int)

    /**
     * Reaction propagation depth getter method
     *
     * @return    int     Reaction propagation depth
     */
    fun getReactionPropagationDepth(): Int

    /**
     * Reaction propagation depth reset method
     */
    fun resetReactionPropagationDepth()

    /**
     * Called to decide whether the GamePlay is over or not
     *
     * @return    boolean     True means Game over, False otherwise
     */
    fun isGameEnded(): Boolean

    /**
     * Returns the type of the winner
     *
     * @return    String     AI or human
     */
    fun winnerType(): String

    /**
     * Triggers the AI Players to step in their order
     * Sets the current Player Id to the actual Player's Id
     *
     * @return    int     minus value is the (-1)*Id of the winner; positive value is the Id of the current Player
     */
    fun stepToNextPlayer(): Int

    /**
     * Returns whether the current depth state is empty or not
     *
     * @param    propagation_depth   The index of the examined depth
     * @return    boolean             True if the indexed depth is empty, false otherwise
     */
    fun isCurrentHistoryStateEmpty(propagation_depth: Int): Boolean

}
