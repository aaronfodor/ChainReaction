package com.arpadfodor.android.chain_reaction.model.game;

import com.arpadfodor.android.chain_reaction.model.ai.PlayerLogic;

/**
 * An AI Player in a GamePlay which communicates with it's LogicAI component.
 */
public class PlayerAI extends Player {

    private static PlayerLogic AI;

    /**
     * PlayerAI constructor
     * Sets the Game that the Player is in
     *
     * @param	Id       	Id of the Player
     * @param	name       	Name of the Player
     */
    public PlayerAI(int Id, String name){
        super(Id, name, 2);
        AI = new PlayerLogic();
    }

    /**
     * Quick stepping - AI does the calculation and steps
     *
     * @return 	Integer[]     Coordinates of the selected Field to step on by AI
     */
    @Override
    public Integer[] executeStep() {
        return AI.CalculateStep(this.gamePlay.currentPlaygroundInfo(), this.getId());
    }

}
