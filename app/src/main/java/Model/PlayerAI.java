package Model;

/**
 * An AI Player in a GamePlay which communicates with it's LogicAI component.
 */
public class PlayerAI extends Player {

    //private static AILogic AI;

    /**
     * PlayerAI constructor
     * Sets the Game that the Player is in
     *
     * @param	Id       	Id of the Player
     * @param	name       	Name of the Player
     */
    public PlayerAI(int Id, String name){

        super(Id, name);


    }

    /**
     * Quick stepping - PlayerAI does the calculation and steps
     *
     * @return 	boolean     True as automatic stepping has been conducted
     */
    @Override
    public boolean ExecuteStep() {

        //int[]position = this.AI.Predict(this.gameplay.GetPlayground(), this);
        //this.gameplay.StepRequest(position[0], position[1]);

        return true;

    }

}
