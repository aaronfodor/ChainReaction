package presenter;

/**
 * MVP model
 * GamePresenter calls the model via this interface
 */
public interface IGameModel {

    /**
     * Starts the actual GamePlay
     *
     * @return 	int     Id of the Player to start the game with
     */
    int StartGame();

    /**
     * Requesting a step on behalf of the current player based on the state machine status
     *
     * @param	pos_y       Y coordinate
     * @param	pos_x       X coordinate
     * @return 	int         minus value is the (-1)*Id of the winner; positive value is the Id of the current Player; 0 means step was unsuccessful
     */
    int StepRequest(int pos_y, int pos_x);

    /**
     * Provides the actual Playground info - Player and it's elements on Field
     * First index is the Y coordinate of Field
     * Second index is the X coordinate of Field
     * Third index is the Field specific information: [0] is the owner Player's Id, [1] is the number of elements on the Field
     *
     * @return 	int[][][]   Field information matrix
     */
    int[][][] ActualPlaygroundInfo();

    /**
     * Provides the dimensions of the Playground
     * [0] is the width of the Playground
     * [1] is the height of the Playground
     *
     * @return 	int[]   Playground dimension info
     */
    int[] GetDimension();

    /**
     * Returns the Id of the current Player
     *
     * @return 	Integer     Id of the current Player
     */
    Integer getActualPlayerId();

    /**
     * Returns the Id of the current Player
     *
     * @return 	Integer[]     Automatic stepping coordinates of the current Player: [0] is the y coordinate, [1] is the x coordinate. If not available, returns null
     */
    Integer[] getAutoCoordinates();
}
