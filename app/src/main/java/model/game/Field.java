package model.game;

import java.util.ArrayList;
import java.util.EnumMap;

/**
 * A Field on the Playground
 * The Playground is made up of these Fields
 * Contains Particles
 */
public class Field {

    /**
     * Id of the Field
     */
    private String Id;

    /**
     * Number of the atoms on this Field
     */
    private Particle particle;

    /**
     * Playground that contains this Field
     */
    private Playground playground;

    /**
     * Neighbours of the Field in every Direction
     */
    private EnumMap<Direction, Field> neighbors;

    /**
     * State history of the Field
     * [0] is the depth value
     * [1] is the Id of the owner Player
     * [2] is the current size
     * [3] is the number left before explosion
     */
    private ArrayList<int[]> state_history;

    /**
     * Field constructor
     * Sets the Field to know its container Playground,
     * Creates the Map containing the Field's neighbours
     *
     * @param   playground 	Playground that contains the Field
     * @param   max_atoms 	Maximum allowed atoms
     * @param   Id       	Name of the Field
     */
    public Field(Playground playground, int max_atoms, String Id){
        this.playground = playground;
        this.neighbors = new EnumMap<Direction, Field>(Direction.class);
        this.Id = Id;
        this.state_history = new ArrayList<int[]>();
        this.particle = new Particle(this, null, max_atoms);
    }

    /**
     * Called when a Player tries to increase it's value
     * Only that Player can place to whom this Field belongs
     *
     * @param	adder	    Player who tries to increase
     * @return 	boolean     True if the action was successful, false otherwise
     */
    public boolean ElementAdd(Player adder){
        return this.getParticle().react(adder);
    }

    /**
     * Neighbor setter method
     * Sets the received Field as a Neighbour in the received Direction
     *
     * @param 	f		neighbouring Field
     * @param 	d 		Direction of the received neighbour
     */
    public void setNeighbor(Field f, Direction d){
        this.neighbors.put(d,f);
    }

    /**
     * Called to get neighboring Field in the given Direction
     *
     * @param	dir 	    Direction where the neighboring Field is searched
     * @return 	Field       Neighboring Field in the given Direction
     */
    public Field getNeighborAt(Direction dir){
        return neighbors.get(dir);
    }

    /**
     * Called to get Particle of the Field
     *
     * @return 	Particle    Particle of the Field
     */
    public Particle getParticle(){
        return particle;
    }

    /**
     * Called to get the Id of it's particles's Player
     *
     * @return 	int    Id of the Player
     */
    public int getPlayerIdOnField(){
        return particle.getOwnerId();
    }

    /**
     * Called to decide whether the game is over or not
     *
     * @return 	boolean     True means Game over, False otherwise
     */
    protected boolean isGameEnded(){
        return this.playground.isGameEnded();
    }

    /**
     * State adder method. Adds the state to the Field's state history
     *
     * @param 	propagation_depth           Depth value to add
     * @param 	Id 		                    Id of the owner Player
     * @param 	current_size                Current size
     * @param 	numberLeftBeforeExplosion   Number left before explosion
     */
    public void addStateToHistory(int propagation_depth, int Id, int current_size, int numberLeftBeforeExplosion) {

        int[] new_state = new int[4];
        new_state[0] = propagation_depth;
        new_state[1] = Id;
        new_state[2] = current_size;
        new_state[3] = numberLeftBeforeExplosion;

        this.state_history.add(new_state);

        if(propagation_depth > this.playground.getReactionPropagationDepth()){
            this.playground.setReactionPropagationDepth(propagation_depth);
        }

    }

    /**
     * Returns the number of states of the Field
     *
     * @return 	int     Number of states of the Field
     */
    public int numberOfStates() {

        if(this.state_history.isEmpty()){
            return 0;
        }

        else{
            return this.state_history.size();
        }

    }

    /**
     * Returns the selected state representation
     *
     * @param	propagation_time 	    The selected state index
     * @return 	int[]                   The state representation: [0] is the Id of the owner Player, [1] is the current size, [2] is the number left before explosion
     */
    public int[] getStateAt(int propagation_time) {

        if(propagation_time < this.state_history.get(0)[0]){
            return stateProvider(0);
        }
        else if(propagation_time > this.state_history.get(this.state_history.size()-1)[0]){
            return stateProvider(this.state_history.size()-1);
        }

        for(int i = 0; i < this.state_history.size(); i++){

            int current_time_in_history = this.state_history.get(i)[0];

            if(propagation_time == current_time_in_history){
                return stateProvider(i);
            }

            else if(state_history.get(i)[0] < propagation_time && propagation_time < state_history.get(i+1)[0]){
                return stateProvider(i);
            }

        }

        return stateProvider(0);

    }

    /**
     * Deletes the whole state history of the Field
     */
    public void clearHistory() {
        this.state_history.removeAll(state_history);
    }

    /**
     * Returns the indexed state representation from the state history
     *
     * @param	index       The index of the selected state history element
     * @return 	int[]       The state representation: [0] is the Id of the owner Player, [1] is the current size, [2] is the number left before explosion
     */
    private int[] stateProvider(int index){
        int[] state = this.state_history.get(index);
        int[] current_state = new int[3];
        current_state[0] = state[1];
        current_state[1] = state[2];
        current_state[2] = state[3];

        return current_state;
    }

}