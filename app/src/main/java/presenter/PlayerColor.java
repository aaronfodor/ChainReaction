package presenter;

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
            case 1: return Color.RED;
            case 2: return Color.GREEN;
            case 3: return Color.BLUE;
            case 4: return Color.YELLOW;
            case 5: return Color.rgb(255,165,0);
            case 6: return Color.MAGENTA;
            case 7: return Color.rgb(210,180,140);
            case 8: return Color.LTGRAY;
            default: return 0;
        }

    }

}
