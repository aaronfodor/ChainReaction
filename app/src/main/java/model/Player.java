package model;

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
     * Waiting time of the Player
     */
    private int waitingTime;

    /**
     * Rounds of the Player
     */
    private int numberOfRounds;

    /**
     * Type Id of the Player
     */
    private int typeId;

    /**
     * Player constructor
     *
     * @param	Id       	Id of the Player
     * @param	name       	Name of the PlayerType Id of the Player
     * @param	typeId      Type Id of the Player
     */
    public Player(int Id, String name, int typeId){
        this.Id = Id;
        this.name = name;
        this.typeId = typeId;
        this.waitingTime = 0;
        this.numberOfRounds = 0;
    }

    /**
     * Automatic stepping
     *
     * @return 	Integer[]     Returns null by default
     */
    public Integer[] ExecuteStep() {
        return null;
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
    public void SetGamePlay(GamePlay gameplay){
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

    /**
     * Waiting time adder method
     *
     * @param 	duration 	Waiting duration of the current Player
     */
    public void addWaitingTime(int duration){
        this.waitingTime += duration;
        this.numberOfRounds++;
    }

    /**
     * Average waiting time getter method
     *
     * @return 	int        Average waiting time of the Player
     */
    public int getAvgWaitingTime(){
        return (this.waitingTime / this.numberOfRounds);
    }

    /**
     * Number of rounds Player had
     *
     * @return 	int        Number of rounds
     */
    public int getNumberOfRounds(){
        return this.numberOfRounds;
    }

    /**
     * Player type Id
     *
     * @return 	int        Type Id of the Player
     */
    public int getTypeId(){
        return this.typeId;
    }

}
