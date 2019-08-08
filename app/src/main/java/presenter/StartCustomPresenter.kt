package presenter

import android.content.Context
import java.util.ArrayList
import kotlin.random.Random.Default.nextBoolean

/**
 * presenter of start custom game
 * Controls the Players list and it's adapter
 * Makes random configuration if needed
 */
class StartCustomPresenter (
    /**
     * MVP view
     */
    private val view: IStartCustomView,
    /**
     * the context of the adapter
     */
    context: Context
) {

    companion object {
        private const val MAXIMUM_SIZE = 20
        private const val MINIMUM_SIZE = 3
        private const val MAXIMUM_ALLOWED_PLAYER_NUMBER = 8
        private const val MINIMUM_PLAYER_NUMBER_TO_START = 2
    }

    private var playGroundHeight = 7
    private var playGroundWidth = 5
    private var playersListData = ArrayList<PlayerListData>()
    private var adapter: PlayerListAdapter

    init {

        adapter = PlayerListAdapter(context, playersListData)
        adapter.addItem(PlayerListData("Player 1", "human", imageAdder(1)))
        adapter.addItem(PlayerListData("Player 2", "AI", imageAdder(2)))

    }

    /**
     * Adds a human Player to the list if maximum is not reached
     */
    fun addHumanPlayer(){

        if(adapter.itemCount < MAXIMUM_ALLOWED_PLAYER_NUMBER){
            adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"human", imageAdder(adapter.itemCount+1)))
            view.playerAdded(getPlayerCount())
        }

        else{
            view.playersFull()
        }

    }

    /**
     * Adds an AI Player to the list if maximum is not reached
     */
    fun addAIPlayer(){

        if(adapter.itemCount < MAXIMUM_ALLOWED_PLAYER_NUMBER){
            adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"AI", imageAdder(adapter.itemCount+1)))
            view.playerAdded(getPlayerCount())
        }

        else{
            view.playersFull()
        }

    }

    /**
     * Clears the Player list
     */
    fun clearPlayers(){
        adapter.clear()
        view.playersCleared()
    }

    /**
     * Checks whether the game can be started or not based on the current state
     *
     * @return  Boolean	    True means the game state is valid and can be started, false otherwise
     */
    fun canGameBeStarted() : Boolean {

        return if(adapter.itemCount >= MINIMUM_PLAYER_NUMBER_TO_START){
            true
        }

        else{
            view.notEnoughPlayer()
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
     * Increments the height if maximum is not reached
     */
    fun heightPlus(){

        if(playGroundHeight == MAXIMUM_SIZE){
            view.maximumSizeReached()
        }

        else{
            playGroundHeight++
            view.updateHeightText(playGroundHeight)
        }

    }

    /**
     * Decrements the height if minimum is not reached
     */
    fun heightMinus(){

        if(playGroundHeight == MINIMUM_SIZE){
            view.minimumSizeReached()
        }

        else{
            playGroundHeight--
            view.updateHeightText(playGroundHeight)
        }

    }

    /**
     * Increments the width if maximum is not reached
     */
    fun widthPlus(){

        if(playGroundWidth == MAXIMUM_SIZE){
            view.maximumSizeReached()
        }

        else{
            playGroundWidth++
            view.updateWidthText(playGroundWidth)
        }

    }

    /**
     * Decrements the width if minimum is not reached
     */
    fun widthMinus(){

        if(playGroundWidth == MINIMUM_SIZE){
            view.minimumSizeReached()
        }

        else{
            playGroundWidth--
            view.updateWidthText(playGroundWidth)
        }

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

    /**
     * Configures the game with random Players and playground size
     */
    fun randomConfig(){

        val randomPlayerNumber = (MINIMUM_PLAYER_NUMBER_TO_START..MAXIMUM_ALLOWED_PLAYER_NUMBER).random()
        val randomHeight = (MINIMUM_SIZE..MAXIMUM_SIZE).random()
        val randomWidth = (MINIMUM_SIZE..MAXIMUM_SIZE).random()

        adapter.clear()

        for (i in 1..randomPlayerNumber) {

            val isHumanPlayer = nextBoolean()

            if(isHumanPlayer){
                adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"human", imageAdder(adapter.itemCount+1)))
            }
            else{
                adapter.addItem(PlayerListData("Player " + (adapter.itemCount+1).toString(),"AI", imageAdder(adapter.itemCount+1)))
            }

        }

        playGroundHeight = randomHeight
        view.updateHeightText(playGroundHeight)
        playGroundWidth = randomWidth
        view.updateWidthText(playGroundWidth)

        view.randomGenerated()

    }

}