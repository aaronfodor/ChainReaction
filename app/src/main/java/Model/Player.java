package Model;

/**
 * An abstract Player who can step and therefore takes part in a GamePlay.
 */
public abstract class Player {

    /**
     * Player's Id
     */
    private int Id;

    /**
     * Player's name
     */
    private String name;

    /**
     * GamePlay that the Player is in
     */
    protected GamePlay gameplay;

    /**
     * Player constructor
     * Sets the Game that the Player is in
     *
     * @param	Id       	Id of the Player
     * @param	name       	Name of the Player
     */
    public Player(int Id, String name){

        this.Id = Id;
        this.name = name;

    }

    /**
     * Automatic stepping
     *
     * @return 	boolean     Returns false by default
     */
    public boolean ExecuteStep() {

        return false;

    }


    /**
     * Executing a step on the Field described with the input parameters
     *
     * @param	pos_y       Y coordinate
     * @param	pos_x       X coordinate
     * @return 	boolean     True if succeed, false otherwise
     */
    protected boolean ExecuteStep(int pos_y, int pos_x){

        return this.gameplay.GetPlayground().GetFieldAt(pos_y, pos_x).ElementAdd(this);

    }

    /**
     * GamePlay setter method
     *
     * @param 	gameplay 	GamePlay the Player is in
     */
    public void SetGameplay(GamePlay gameplay){

        this.gameplay= gameplay;

    }

    /**
     * Player's Id getter method
     *
     * @return 	int     Id of the Player
     */
    public int GetId(){

        return this.Id;

    }

}
