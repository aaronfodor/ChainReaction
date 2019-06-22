package model.game;

import presenter.IGameModel;

import java.util.ArrayList;

/**
 * The class that controls the whole GamePlay
 *
 */
public class GamePlayDynamic extends GamePlay implements IGameModel {

    /**
     * GamePlayDynamic constructor
     * Creates the Playground, sets the Players, initializes the state machine
     *
     * @param	height	        Height of the Playground
     * @param	width	        Width of the Playground
     * @param	players_raw	    Players represented by ArrayList of String arrays of 3 elements
     */
    public GamePlayDynamic(int height, int width, ArrayList<String[]> players_raw){
        super(height, width, players_raw);
    }

}
