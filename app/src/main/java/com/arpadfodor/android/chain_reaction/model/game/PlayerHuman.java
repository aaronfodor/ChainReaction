package com.arpadfodor.android.chain_reaction.model.game;

/**
 * A human Player in a GamePlay.
 */
public class PlayerHuman extends Player {

    /**
     * PlayerHuman constructor
     * Sets the Game that the Player is in
     *
     * @param	Id       	Id of the Player
     * @param	name       	Name of the Player
     */
    public PlayerHuman(int Id, String name){
        super(Id, name, 1);
    }

}
