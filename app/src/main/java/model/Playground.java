package model;

/**
 * The Playground in the GamePlay
 * The collection of Fields form the Playground
 */
public class Playground {

    /**
     * GamePlay that the Playground is part of
     */
    private GamePlay gameplay;

    /**
     * Matrix of Fields in the Warehouse
     */
    private Field[][] fields;

    /**
     * Width of the Warehouse
     */
    private int width;

    /**
     * Height of the Warehouse
     */
    private int height;

    /**
     * limit before element on the Field does not explode
     */
    private static int corner_element_limit = 1;
    private static int edge_element_limit = 2;
    private static int inner_element_limit = 3;

    /**
     * Playground constructor
     * Sets the GamePlay that the Playground is in
     *
     * @param	game	    GamePlay that the Playground is in
     * @param	height  	Height of the Playground
     * @param	width	    Width of the Playground
     */
    protected Playground(GamePlay game, int height, int width){

        this.gameplay = game;
        this.width = width;
        this.height = height;

        fields = new Field[height][];

        for(int actual_height = 0; actual_height < height; actual_height++){

            fields[actual_height] = new Field[width];

            for(int actual_width = 0; actual_width < width; actual_width++){

                //limit before element on the Field does not explode
                int max_value;

                if(     (actual_height == 0 && actual_width == 0)       || (actual_height == height-1 && actual_width == width-1) ||
                        (actual_height == 0 && actual_width == width-1) || (actual_height == height-1 && actual_width == 0)){
                    max_value = corner_element_limit;
                }
                else if(actual_height == 0 || actual_width == 0 || actual_height == height-1 || actual_width == width-1){
                    max_value = edge_element_limit;
                }
                else{
                    max_value = inner_element_limit;
                }

                String current_Id = "Field " + actual_height + "_" + actual_width;
                fields[actual_height][actual_width] = new Field(this, max_value, current_Id);

            }

        }

        SetNeighborsOnPlayground();

    }

    /**
     * Sets all neighbors on the playground
     */
    private void SetNeighborsOnPlayground(){

        for(int actual_height = 0; actual_height < this.height; actual_height++){

            for(int actual_width = 0; actual_width < this.width; actual_width++){

                if(actual_height+1 < this.height){
                    fields[actual_height][actual_width].SetNeighbor(fields[actual_height+1][actual_width],Direction.Down);
                }

                if(actual_width-1 >= 0){
                    fields[actual_height][actual_width].SetNeighbor(fields[actual_height][actual_width-1],Direction.Left);
                }

                if(actual_height-1 >= 0){
                    fields[actual_height][actual_width].SetNeighbor(fields[actual_height-1][actual_width],Direction.Up);
                }

                if(actual_width+1 < this.width){
                    fields[actual_height][actual_width].SetNeighbor(fields[actual_height][actual_width+1],Direction.Right);
                }

            }

        }

    }

    /**
     * Returns the Field described with the coordinates
     *
     * @param	pos_y       Y coordinate
     * @param	pos_x       X coordinate
     * @return 	Field       The result Field
     */
    protected Field GetFieldAt(int pos_y, int pos_x){

        return fields[pos_y][pos_x];

    }

    /**
     * Width getter method
     *
     * @return 	width   Width of the Playground
     */
    protected int GetWidth(){

        return this.width;

    }

    /**
     * Height getter method
     *
     * @return 	height   Height of the Playground
     */
    protected int GetHeight(){

        return this.height;

    }

    /**
     * Called to decide whether the game is over or not
     *
     * @return 	boolean     True means Game over, False otherwise
     */
    protected boolean IsGameEnded(){

        return this.gameplay.IsGameEnded();

    }

}
