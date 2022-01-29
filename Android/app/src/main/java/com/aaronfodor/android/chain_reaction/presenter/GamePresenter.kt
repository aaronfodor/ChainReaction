package com.aaronfodor.android.chain_reaction.presenter

import com.aaronfodor.android.chain_reaction.model.game.GamePlay
import com.aaronfodor.android.chain_reaction.model.game.GamePlayDynamic
import com.aaronfodor.android.chain_reaction.presenter.task.GameLogicTask

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

/**
 * hu.bme.aut.android.chain_reaction.presenter of a game play
 * Controls what and how to be displayed on the hu.bme.aut.android.chain_reaction.view
 * Mediator between hu.bme.aut.android.chain_reaction.model and hu.bme.aut.android.chain_reaction.view
 */
class GamePresenter
/**
 * GamePresenter constructor
 * Sets the hu.bme.aut.android.chain_reaction.view, the Players, the hu.bme.aut.android.chain_reaction.model, starts the game play
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
     * MVP hu.bme.aut.android.chain_reaction.view
     */
    private val view: IGameView, height: Int, width: Int, players_input: ArrayList<String>,
    /**
     * Game property flags
     */
    private val showPropagation: Boolean, private val gifEnabled: Boolean, private val limitedTimeMode: Boolean,
    /**
     * Game type flag
     */
    private val gameType: Int,
    /**
     * Game mode and campaign level flags
     */
    private val gameMode: Int, private val challengeLevel: Int
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
        private const val AI = 2

        /**
         * Game type constants
         */
        private const val CUSTOM_GAME = 1
        private const val CHALLENGE_GAME = 2

        /**
         * Game mode constants
         */
        private const val NORMAL_MODE = 1
        private const val DYNAMIC_MODE = 2
    }

    /**
     * MVP hu.bme.aut.android.chain_reaction.model
     */
    private val model: IGameModel

    /**
     * AsyncTask to execute hu.bme.aut.android.chain_reaction.model computations
     */
    private var gameTask: GameLogicTask? = null

    /**
     * Time limit in milliseconds
     */
    private val timeLimit = 4000

    /**
     * Has the click been calculated and handled
     */
    private var clickResultCalculated: Boolean = true

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

        //classic game type starts a classic game, dynamic type starts dynamic game, otherwise classic game will be started
        when (gameType) {
            NORMAL_MODE -> this.model = GamePlay(height, width, players)
            DYNAMIC_MODE -> this.model = GamePlayDynamic(height, width, players)
            else -> this.model = GamePlay(height, width, players)
        }

        this.refreshPlayground(SHOW_CURRENT_PLAYGROUND_STATE)
        view.showStart(Integer.valueOf(players[0][0]!!), model.actualPlayerType == AI)

        gameTask = GameLogicTask(model, this, showPropagation, limitedTimeMode)
        gameTask!!.cancel(true)

    }

    /**
     * Requests a step calculation to the Field described with the input parameters in a new task
     * Returns if a calculation has not finished yet and an another step is required - filters too early clicks
     * Otherwise, refreshes the hu.bme.aut.android.chain_reaction.view if the step is valid
     *
     * @param   pos_y      Y coordinate
     * @param   pos_x      X coordinate
     * @param   duration   Duration of waiting
     */
    fun stepRequest(pos_y: Int, pos_x: Int, duration: Int) {

        if(clickResultCalculated){
            clickResultCalculated = false
            gameTask = GameLogicTask(model, this, showPropagation, limitedTimeMode)
            gameTask!!.execute(pos_y, pos_x, duration)
        }

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
     * Refreshes the Playground and tells the hu.bme.aut.android.chain_reaction.view to draw it
     * If the result has been displayed, refresh nothing
     * Handles game over
     * If the current state has been shown, sets calculation done flag to enable handling another clicks
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
            handleGameOver()
        }
        else if (propagation_depth >= 0 && propagation_depth < model.getReactionPropagationDepth()) {
            stateMatrix = model.historyPlaygroundInfoAt(propagation_depth)
            refreshPlaygroundFields(dimension, stateMatrix)
        }
        else {
            stateMatrix = model.currentPlaygroundInfo()
            refreshPlaygroundFields(dimension, stateMatrix)
            view.showCurrentPlayer(Math.abs(model.actualPlayerId!!), model.actualPlayerType == AI)
        }

        timeLeftCalculator(propagation_depth)

    }

    /**
     * Shows game over state properly
     * Updates statistics database
     * Updates campaign database if the gameMode flag is CAMPAIGN_GAME and the winner is a human
     * Sets resultDisplayed flag true
     */
    private fun handleGameOver() {

        val isWinnerAI = model.actualPlayerType == AI

        val stateMatrix = model.currentPlaygroundInfo()
        view.showCurrentPlayer(Math.abs(model.actualPlayerId!!), isWinnerAI)
        view.showResult(Math.abs(model.actualPlayerId!!), model.playersData!!, model.isAiVsHumanGame(), isWinnerAI)
        refreshPlaygroundFields(model.getDimension(), stateMatrix)

        //update the statistics database
        view.statisticsDatabaseUpdater(model.playersData!![model.playersData!!.size - 1][3], model.isAiVsHumanGame())

        if(gameType == CUSTOM_GAME){

            if (model.actualPlayerType == HUMAN) {
                view.showEndOfGameMessage(false)
            }

            else{
                view.showEndOfGameMessage(true)
            }

        }

        else if(gameType == CHALLENGE_GAME){

            //update the challenge database if the game was a challenge game and human Player is the winner
            if (model.actualPlayerType == HUMAN) {
                view.challengeDatabaseUpdater(challengeLevel)
            }
            else{
                view.showRestartChallengeLevelMessage()
            }

        }

        resultDisplayed = true

    }

    /**
     * Refreshes all Playground Fields and draws it, sets progressbar value to zero
     *
     * @param   dimension     Playground dimension info - [0] is the width, [1] is the height
     * @param   stateMatrix   First index is the Y coordinate of Field, second index is the X coordinate of Field,
     * Third index is the Field specific information: [0] is the owner Player's Id, [1] is the number of elements on the Field, [2] is the number of residual elements left before explosion
     */
    private fun refreshPlaygroundFields(dimension: IntArray, stateMatrix: Array<Array<IntArray>>) {

        for (actual_height in 0 until dimension[0]) {
            for (actual_width in 0 until dimension[1]) {

                val numOfElements = stateMatrix[actual_height][actual_width][1]
                val numLeftBeforeExplosion = stateMatrix[actual_height][actual_width][2]

                view.refreshPlaygroundFieldAt(
                    actual_height, actual_width,
                    stateMatrix[actual_height][actual_width][0],
                    numOfElements,
                    numLeftBeforeExplosion == 0,
                    gifEnabled
                )
            }
        }

        progressBarReset()

    }

    /**
     * Starts the timerTask and refreshes the time left progress bar when the current state is displayed
     * Calculates when limited time mode is enabled, only applies to human players when the game is not over
     *
     * @param   propagationDepth     Current propagation state
     */
    private fun timeLeftCalculator(propagationDepth: Int) {

        progressBarReset()

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
     * Resets the progress bar timer task, sets progressBar value to zero
     */
    private fun progressBarReset(){
        if (progressBarTimerTask != null) {
            progressBarTimerTask!!.cancel()
        }
        view.refreshProgressBar(0)
    }

    /**
     * Tells the hu.bme.aut.android.chain_reaction.presenter that calculation has finished
     */
    fun notifyCalculationFinished(){
        clickResultCalculated = true
    }

    /**
     * Result displayed getter method
     *
     * @return    Boolean       True if the result has been displayed, false otherwise
     */
    fun isResultDisplayed(): Boolean {
        return resultDisplayed
    }

    /**
     * A new challenge level unlocked notification
     */
    fun newCampaignLevelUnlocked(){
        view.showNextChallengeLevelMessage()
    }

    /**
     * All challenge levels completed notification
     */
    fun allChallengesCompleted(){
        view.showAllChallengesCompletedMessage()
    }

    /**
     * Stops the game hu.bme.aut.android.chain_reaction.presenter calculations
     */
    fun stop(){
        this.gameTask?.cancel(true)
        progressBarTimerTask?.cancel()
    }

}