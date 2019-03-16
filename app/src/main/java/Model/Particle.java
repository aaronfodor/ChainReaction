package Model;

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
    protected boolean React(Player owner){

        if((this.owner == owner) || (this.owner == null)){

            this.Increase(owner);
            return true;

        }

        else{

            return false;

        }

    }

    /**
     * Called to tell a Particle to increase it's value
     * If maximum value has been reached, it explodes
     *
     * @param	owner   Player who increases it
     */
    private void Increase(Player owner){

        this.owner = owner;

        if(current_size == max_size){

            this.Explode();

        }

        else{

            current_size++;

        }

    }

    /**
     * Called to react with the neighboring Particles
     * Does nothing if the game is over
     * It sets its value to zero, and sets its owner to null
     */
    private void Explode(){

        if(IsGameEnded()){

            return;

        }

        Player exploder = this.owner;

        this.current_size = 0;
        this.owner = null;

        for (Direction dir : Direction.values()) {

            if(field.GetNeighborAt(dir) != null){

                field.GetNeighborAt(dir).GetParticle().Increase(exploder);

            }

        }

    }

    /**
     * Called to get the Id of the owner Player
     *
     * @return 	int    Id of the owner, or 0 if there is no owner
     */
    protected int GetOwnerId(){

        if(owner != null){

            return owner.GetId();

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
    protected int GetSize(){

        return this.current_size;

    }

    /**
     * Called to decide whether the game is over or not
     *
     * @return 	boolean     True means Game is over - computation is not necessary, False otherwise
     */
    private boolean IsGameEnded(){

        return this.field.IsGameEnded();

    }

}
