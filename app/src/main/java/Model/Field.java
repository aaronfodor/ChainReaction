package Model;

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

        return this.GetParticle().React(adder);

    }

    /**
     * Neighbor setter method
     * Sets the received Field as a Neighbour in the received Direction
     *
     * @param 	f		neighbouring Field
     * @param 	d 		Direction of the received neighbour
     * @return 	void
     */
    public void SetNeighbor(Field f, Direction d){

        this.neighbors.put(d,f);

    }

    /**
     * Called to get neighboring Field in the given Direction
     *
     * @param	dir 	    Direction where the neighboring Field is searched
     * @return 	Field       Neighboring Field in the given Direction
     */
    public Field GetNeighborAt(Direction dir){

        return neighbors.get(dir);

    }

    /**
     * Called to get Particle of the Field
     *
     * @return 	Particle    Particle of the Field
     */
    public Particle GetParticle(){

        return particle;

    }

    /**
     * Called to get the Id of it's particles's Player
     *
     * @return 	int    Id of the Player
     */
    public int GetPlayerIdOnField(){

        return particle.GetOwnerId();

    }

    /**
     * Called to decide whether the game is over or not
     *
     * @return 	boolean     True means Game over, False otherwise
     */
    protected boolean IsGameEnded(){

        return this.playground.IsGameEnded();

    }

}
