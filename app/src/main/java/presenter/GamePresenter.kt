package presenter

import model.game.GamePlay
import presenter.task.GameLogicTask

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

/**
 * presenter of a game play
 * Controls what and how to be displayed on the view
 * Mediator between model and view
 */
class GamePresenter
/**
 * GamePresenter constructor
 * Sets the view, the Players, the model, starts the game play
 *
 * @param   view                IGameView object that holds the GamePresenter
 * @param   height              height of the Playground
 * @param   width               width of the Playground
 * @param   players_input       ArrayList of Strings; "<Player type>-<Player name>" format
 * @param   showPropagation     Show propagation flag
 * @param   limitedTimeMode     Limited time mode flag
 */
    (
    /**
     * MVP view
     */
    private val view: IGameView, height: Int, width: Int, players_input: ArrayList<String>,
    /**
     * Game mode flags
     */
    private val showPropagation: Boolean, private val gifEnabled: Boolean, private val limitedTimeMode: Boolean
) {

    companion object {
        /**
         * TimerTask to execute limited time counting
         */
        private var progressBarTimerTask: TimerTask? = null

        /**
         * Constant to represent current state display intent
         */
        private const val SHOW_CURRENT_PLAYGROUND_STATE = -1

        /**
         * Type of the HUMAN Players
         */
        private const val HUMAN = 1
    }

    /**
     * MVP model
     */
    private val model: IGameModel

    /**
     * AsyncTask to execute model computations
     */
    private var gameTask: GameLogicTask? = null

    /**
     * Time limit in milliseconds
     */
    private val timeLimit = 4000

    /**
     * Has the result been displayed flag
     */
    private var resultDisplayed: Boolean

    /**
     * Returns the game AsyncTask
     *
     * @return    AsyncTask     Returns the game AsyncTask. If empty, returns null
     */
    val task: GameLogicTask?
        get() = if (gameTask != null) {
            gameTask
        } else {
            null
        }

    init {
        val numberOfPlayers = players_input.size
        val players = ArrayList<Array<String?>>()
        resultDisplayed = false

        for (i in 0 until numberOfPlayers) {

            val inputData = players_input[i].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val playerData = arrayOfNulls<String>(3)
            playerData[0] = (i + 1).toString()
            playerData[1] = inputData[1]
            playerData[2] = inputData[0]

            players.add(playerData)

        }

        this.model = GamePlay(this, height, width, players)
        this.refreshPlayground(SHOW_CURRENT_PLAYGROUND_STATE)
        view.showStart(Integer.valueOf(players[0][0]!!))

        gameTask = GameLogicTask(model, this, showPropagation, limitedTimeMode)
        gameTask!!.cancel(true)

    }

    /**
     * Requests a step to the Field described with the input parameters in a new task
     * Refreshes the view if the step is valid
     *
     * @param   pos_y       Y coordinate
     * @param   pos_x      X coordinate
     * @param    duration   Duration of waiting
     */
    fun stepRequest(pos_y: Int, pos_x: Int, duration: Int) {
        gameTask = GameLogicTask(model, this, showPropagation, limitedTimeMode)
        gameTask!!.execute(pos_y, pos_x, duration)
    }

    /**
     * Time is up handling
     * Sets the current Player to the next one
     *
     * @param    duration   Duration of waiting which is going to be recorded
     */
    fun playerTimeIsUp(duration: Int) {
        gameTask = GameLogicTask(model, this, showPropagation, limitedTimeMode)
        gameTask!!.execute(duration)
    }

    /**
     * Refreshes the Playground and tells the view to draw it
     * If the result has been displayed, refresh nothing
     *
     * @param   propagation_depth     Current propagation state
     */
    fun refreshPlayground(propagation_depth: Int) {

        if(resultDisplayed){
            return
        }

        val dimension = model.getDimension()
        val stateMatrix: Array<Array<IntArray>>

        if (model.isGameEnded() && propagation_depth == SHOW_CURRENT_PLAYGROUND_STATE) {
            stateMatrix = model.currentPlaygroundInfo()
            view.showCurrentPlayer(Math.abs(model.actualPlayerId!!))
            view.showResult(Math.abs(model.actualPlayerId!!), model.playersData!!, model.isAiVsHumanGame())
            refreshPlaygroundFields(dimension, stateMatrix)
            resultDisplayed = true
        }
        else if (propagation_depth >= 0 && propagation_depth < model.getReactionPropagationDepth()) {
            stateMatrix = model.historyPlaygroundInfoAt(propagation_depth)
            refreshPlaygroundFields(dimension, stateMatrix)
        }
        else {
            stateMatrix = model.currentPlaygroundInfo()
            refreshPlaygroundFields(dimension, stateMatrix)
            view.showCurrentPlayer(Math.abs(model.actualPlayerId!!))
        }

        timeLeftCalculator(propagation_depth)

    }

    /**
     * Refreshes all Playground Fields and draws it
     *
     * @param   dimension     Playground dimension info - [0] is the width, [1] is the height
     * @param   stateMatrix   First index is the Y coordinate of Field, second index is the X coordinate of Field,
     * Third index is the Field specific information: [0] is the owner Player's Id, [1] is the number of elements on the Field, [2] is the number of residual elements left before explosion
     */
    private fun refreshPlaygroundFields(dimension: IntArray, stateMatrix: Array<Array<IntArray>>) {

        for (actual_height in 0 until dimension[0]) {
            for (actual_width in 0 until dimension[1]) {
                view.refreshPlayground(
                    actual_height, actual_width,
                    stateMatrix[actual_height][actual_width][0],
                    stateMatrix[actual_height][actual_width][1],
                    gifEnabled
                )
            }
        }

    }

    /**
     * Starts the timerTask and refreshes the time left progress bar when the current state is displayed
     * Calculates when limited time mode is enabled, only applies to human players when the game is not over
     *
     * @param   propagationDepth     Current propagation state
     */
    private fun timeLeftCalculator(propagationDepth: Int) {

        if (progressBarTimerTask != null) {
            progressBarTimerTask!!.cancel()
        }
        view.refreshProgressBar(0)

        //when limited time mode is enabled, it only applies to human players when the game is not over
        if (!model.isGameEnded() && limitedTimeMode && model.actualPlayerType == HUMAN) {

            if (propagationDepth == SHOW_CURRENT_PLAYGROUND_STATE){

                progressBarTimerTask = object : TimerTask() {

                    var counter = 0

                    override fun run() {

                        when {
                            counter < 100 -> {
                                view.refreshProgressBar(counter)
                                counter++
                            }
                            counter == 100 -> {
                                view.refreshProgressBar(0)
                                playerTimeIsUp(timeLimit)
                            }
                            else -> {
                                Thread.currentThread().interrupt()
                                view.refreshProgressBar(0)
                            }
                        }

                    }

                    override fun cancel(): Boolean {
                        view.refreshProgressBar(0)
                        return super.cancel()
                    }

                }

                val timer = Timer("ProgressBarTimer")
                timer.scheduleAtFixedRate(progressBarTimerTask, 50, (timeLimit / 100).toLong())
                progressBarTimerTask!!.run()

            }

        }

    }

    /**
     * Result displayed getter method
     *
     * @return    Boolean       True if the result has been displayed, false otherwise
     */
    fun isResultDisplayed(): Boolean {
        return resultDisplayed
    }

}