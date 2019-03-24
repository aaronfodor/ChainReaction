package Presenter;

/**
 * Player Id and Color connection
 */
public class PlayerColor {

    /**
     * Returns the Color of the given Player
     *
     * @param   Id 	    Id of the Player
     * @return 	Color	Color of the Player
     */
    public static int GetColorById(int Id){

        switch (Id){

            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
            case 4: return 4;
            case 5: return 5;
            case 6: return 6;
            case 7: return 7;
            case 8: return 8;
            default: return 0;

        }

    }

}
