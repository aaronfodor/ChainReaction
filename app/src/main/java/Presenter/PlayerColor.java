package Presenter;

import android.graphics.Color;

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
            default: return 0;

        }

    }

}
