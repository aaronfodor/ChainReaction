package presenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import model.db.challenge.ChallengeLevel
import java.util.ArrayList

/**
 * presenter of level fragment
 * Controls the Players list and it's adapter
 */
class LevelPresenter (
    /**
     * MVP view
     */
    private val view: IStartLevelView,
    /**
     * the context of the adapter
     */
    context: Context,
    private val level: ChallengeLevel
) : RecyclerView.AdapterDataObserver() {

    companion object {
        private const val MAXIMUM_ALLOWED_PLAYER_NUMBER = 8
        private const val MINIMUM_PLAYER_NUMBER_TO_START = 2
    }

    private var playGroundHeight = 0
    private var playGroundWidth = 0
    private var playerListData = ArrayList<PlayerListData>()
    private var adapter: PlayerListAdapter

    init {
        adapter = PlayerListAdapter(context, playerListData)
        adapter.registerAdapterDataObserver(this)
        setDisplayedLevelTo(level)
    }

    private fun setDisplayedLevelTo(levelInfo: ChallengeLevel){

        playGroundHeight = levelInfo.Height
        playGroundWidth = levelInfo.Width

        view.updateHeightText(playGroundHeight)
        view.updateWidthText(playGroundWidth)

        clearPlayers()

        for(i in 0..(levelInfo.PlayerNumber-1)){

            if((levelInfo.HumanPlayerOrder-1) == i){
                addHumanPlayer()
            }

            else{
                addAIPlayer()
            }

        }

        if(levelInfo.Playable){
            view.showUnlocked()
        }
        else{
            view.showLocked()
        }

        if(levelInfo.Completed){
            view.showCompleted()
        }
        else{
            view.showUncompleted()
        }

    }

    /**
     * Adds a human Player to the list if maximum is not reached
     */
    private fun addHumanPlayer(){
        if(adapter.itemCount < MAXIMUM_ALLOWED_PLAYER_NUMBER){
            adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"human", imageAdder(adapter.itemCount+1)))
        }
    }

    /**
     * Adds an AI Player to the list if maximum is not reached
     */
    private fun addAIPlayer(){
        if(adapter.itemCount < MAXIMUM_ALLOWED_PLAYER_NUMBER){
            adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"AI", imageAdder(adapter.itemCount+1)))
        }
    }

    /**
     * Clears the Player list
     */
    private fun clearPlayers(){
        adapter.clear()
    }

    /**
     * Checks whether the game can be started or not based on the current state
     *
     * @return  Boolean	    True means the game state is valid and can be started, false otherwise
     */
    fun canGameBeStarted() : Boolean {

        return if (adapter.itemCount >= MINIMUM_PLAYER_NUMBER_TO_START && level.Playable){
            true
        }
        else{
            view.showLockedReminder()
            false
        }

    }

    /**
     * Returns the adapter
     *
     * @return  PlayerListAdapter	    Adapter
     */
    fun getAdapter() : PlayerListAdapter {
        return adapter
    }

    /**
     * Returns the number of Players in the list
     *
     * @return  Int	    Number of Players in the list
     */
    fun getPlayerCount() : Int {
        return adapter.itemCount
    }

    /**
     * Returns the String representation of the indexed list element
     *
     * @param   idx     Index of the list element
     * @return  String	"<Player type>-<Player name>" format
     */
    fun getPlayerStringElementAt(idx: Int): String {
        return adapter.stringElementAt(idx)
    }

    /**
     * Returns the height of the Playground
     *
     * @return  Int	    Height of the Playground
     */
    fun getPlayGroundHeight() : Int {
        return playGroundHeight
    }

    /**
     * Returns the width of the Playground
     *
     * @return  Int	    Width of the Playground
     */
    fun getPlayGroundWidth() : Int {
        return playGroundWidth
    }

    /**
     * Returns the current challenge level Id
     *
     * @return  Int	    Id of the current challenge level
     */
    fun getChallengeLevelId() : Int {
        return level.Id.toInt()
    }

    /**
     * Returns the image Id based on the color Id
     *
     * @param   Id          Color Id
     * @return  Int 	    Image Id to display
     */
    private fun imageAdder(Id: Int): Int {
        return PlayerVisualRepresentation.getDotsImageIdByColorAndNumber(Id, 1,
            isCloseToExplosion = false,
            gifEnabled = false
        )
    }

}