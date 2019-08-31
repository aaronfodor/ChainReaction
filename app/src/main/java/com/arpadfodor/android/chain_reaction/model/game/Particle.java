package com.arpadfodor.android.chain_reaction.model.game;

/**
 * An element on a Field which has an owner and a state. It is able to explode.
 */
public class Particle {

    /**
     * Current size of this Particle
     */
    private int current_size;

    /**
     * Maximum size of this Particle before exploding
     */
    private int max_size;

    /**
     * Field where the Particle is
     */
    private Field field;

    /**
     * The Player to whom the Particle belongs
     */
    private Player owner;

    /**
     * Particle constructor
     * Sets the Particle based on the inputs
     *
     * @param	field	Field where the Particle is standing
     * @param	owner	Player to whom the Particle belongs
     * @param	max_size	Maximum size of the Particle before exploding
     */
    protected Particle(Field field, Player owner, int max_size){
        this.field = field;
        this.owner = owner;
        this.max_size = max_size;
        current_size = 0;
    }

    /**
     * Called when a Player tries to increase it's value
     * Only that Player can place to whom this Particle belongs
     *
     * @param	owner	    Player who tries to increase
     * @return 	boolean     True if the action was successful, false otherwise
     */
    protected boolean react(Player owner){

        if((this.owner == owner) || (this.owner == null)){
            int propagation_depth = 1;
            this.owner = owner;
            this.increase(owner, propagation_depth);
            return true;
        }

        else{
            return false;
        }

    }

    /**
     * Called to tell a Particle to increase it's value
     * If maximum value has been reached, it explodes
     * If the state history of the particle is empty, adds the start state
     *
     * @param	owner   Player who increases it
     */
    private void increase(Player owner, int propagation_depth){

        addStartStateToHistory();

        this.owner = owner;

        if(current_size == max_size){
            this.field.addStateToHistory(propagation_depth, this.getOwnerId(), this.getSize(), this.getNumberLeftBeforeExplosion());
            this.explode(propagation_depth);
        }

        else{
            current_size+=1;
            this.field.addStateToHistory(propagation_depth, this.getOwnerId(), this.getSize(), this.getNumberLeftBeforeExplosion());
        }


    }

    /**
     * Adds the start state to the state history on the Field where the Particle is
     */
    private void addStartStateToHistory() {
        if(this.field.numberOfStates() == 0){
            this.field.addStateToHistory(0, this.getOwnerId(), this.getSize(), this.getNumberLeftBeforeExplosion());
        }
    }

    /**
     * Called to react with the neighboring Particles
     * Does nothing if the game is over
     * It sets its value to zero, and sets its owner to null
     */
    private void explode(int propagation_depth){

        if(isGameEnded()){
            return;
        }

        Player exploder = this.owner;
        this.current_size = 0;
        this.owner = null;

        this.field.addStateToHistory(propagation_depth+1, this.getOwnerId(), 0, max_size);

        for (Direction dir : Direction.values()) {

            if(field.getNeighborAt(dir) != null){
                field.getNeighborAt(dir).getParticle().increase(exploder, propagation_depth+1);
            }

        }

    }

    /**
     * Called to get the Id of the owner Player
     *
     * @return 	int    Id of the owner, or 0 if there is no owner
     */
    protected int getOwnerId(){

        if(owner != null){
            return owner.getId();
        }

        else{
            return 0;
        }

    }

    /**
     * Called to get size of the Particle
     *
     * @return 	int    Current size of the Particle
     */
    protected int getSize(){
        return this.current_size;
    }

    /**
     * Called to get the number of residual elements left before explosion
     *
     * @return 	int    Number of elements before explosion
     */
    protected int getNumberLeftBeforeExplosion(){
        return (max_size - current_size);
    }

    /**
     * Called to decide whether the game is over or not
     *
     * @return 	boolean     True means Game is over - computation is not necessary, False otherwise
     */
    private boolean isGameEnded(){
        return this.field.isGameEnded();
    }

}
