package com.aaronfodor.android.chain_reaction.model.task

import android.os.AsyncTask
import com.aaronfodor.android.chain_reaction.presenter.GamePresenter
import com.aaronfodor.android.chain_reaction.presenter.IGameModel
import com.aaronfodor.android.chain_reaction.model.AudioService

/**
 * Async task to execute model computations
 */
class GameLogicTask
/**
 * GameLogicTask constructor
 *
 * @param   model                       model of the game play
 * @param   presenter                   presenter of the game play - the UI thread
 * @param   showPropagation             is UI refresh allowed while propagating
 * @param   timeLimitMode               Time limit mode - if true, false click means next Player comes
 */
    (
    /**
     * hu.bme.aut.android.chain_reaction.model object where the computation and state change occurs
     */
    private val model: IGameModel,
    /**
     * presenter object which is on the UI thread
     */
    private val presenter: GamePresenter,
    /**
     * Whether to show propagation states or not
     */
    private val showPropagation: Boolean?,
    /**
     * Represents whether time limit mode is on - if yes, false click means next Player comes
     */
    private val timeLimitMode: Boolean?,
    /**
     * Time delay between two UI refresh events in milliseconds
     */
    private val REFRESH_RATE_MILLISECONDS: Int) : AsyncTask<Int, Int, Boolean>() {

    companion object {
        /**
         * Constant to represent current state display intent
         */
        private const val SHOW_CURRENT_PLAYGROUND_STATE = -1
        /**
         * Constant to represent an unsuccessful step
         */
        private const val STEP_UNSUCCESSFUL = 0
        /**
         * Static cancel flag of all GameLogicTask instances
         */
        internal var cancelTask = true
    }

    init {
        cancelTask = false
    }

    /**
     * Does the model calculation and refreshes UI periodically via publish events
     *
     * @param   params      The selected coordinates where the Player wants to step; if empty, the game will be started
     * @return  Boolean     True when the calculation has finished
     */
    override fun doInBackground(vararg params: Int?): Boolean? {

        //do if the task is not cancelled
        if (!cancelTask) {

            var coordinates: Array<Int>? = null
            var estimated = 0

            if (params.isEmpty()) {
                propagationDisplayManager(SHOW_CURRENT_PLAYGROUND_STATE)

                val startTime: Long = System.currentTimeMillis()
                coordinates = model.autoCoordinates
                val estimatedTime: Long = System.currentTimeMillis() - startTime
                estimated = estimatedTime.toInt()
            }
            else if (params.size == 1) {
                model.addCurrentPlayerWaitingTime(params[0]!!)
                propagationDisplayManager(SHOW_CURRENT_PLAYGROUND_STATE)
                model.stepToNextPlayer()

                val startTime: Long = System.currentTimeMillis()
                coordinates = model.autoCoordinates
                val estimatedTime: Long = System.currentTimeMillis() - startTime!!
                estimated = estimatedTime.toInt()

                if(coordinates != null){
                    Thread.sleep(REFRESH_RATE_MILLISECONDS.toLong())
                }
            }
            else {
                coordinates = arrayOf(0, 0)

                val startTime: Long = System.currentTimeMillis()
                val autoCoordinates: Array<Int>? = model.autoCoordinates
                val estimatedTime: Long = System.currentTimeMillis() - startTime
                estimated = estimatedTime.toInt()

                //if auto coordinates are empty, this tap is interpreted as a step for the human player
                if (autoCoordinates == null) {
                    coordinates[0] = params[0]!!
                    coordinates[1] = params[1]!!
                    estimated = params[2]!!
                }
                //if auto coordinates are not empty, this tap only triggers a game start, and the computer player decides its step
                else {
                    coordinates[0] = autoCoordinates[0]
                    coordinates[1] = autoCoordinates[1]
                }
            }

            while (coordinates != null) {

                if (cancelTask) {
                    break
                }

                try {
                    propagationDisplayManager(coordinates[0], coordinates[1], estimated)
                    Thread.sleep((REFRESH_RATE_MILLISECONDS*2).toLong())
                }
                catch (e: InterruptedException) {
                    e.printStackTrace()
                    handleOnCancelled()
                }

                val startTime = System.currentTimeMillis()
                coordinates = model.autoCoordinates
                val estimatedTime = System.currentTimeMillis() - startTime
                estimated = estimatedTime.toInt()

            }
            //step request happened
            //time is up, player is not allowed to step, next player comes

        }

        return true

    }

    /**
     * Manages the display of the propagation
     *
     * @param   values      The selected coordinates (two parameters) where the Player wants to step; if one parameter passed, the current state is displayed
     */
    private fun propagationDisplayManager(vararg values: Int) {

        if (!cancelTask) {

            if (values.size == 1) {
                publishProgress(SHOW_CURRENT_PLAYGROUND_STATE)
            }
            else {

                model.addCurrentPlayerWaitingTime(values[2])

                if (model.stepRequest(values[0], values[1]) == STEP_UNSUCCESSFUL && timeLimitMode!!) {
                    model.stepToNextPlayer()
                }
                else if (showPropagation!!) {

                    model.historyPlaygroundBuilder()
                    val propagationDepth = model.getReactionPropagationDepth()

                    //starts from state 2 as the previously displayed end state is the current start state too
                    for (i in 2 until propagationDepth) {

                        if (cancelTask) {
                            break
                        }

                        try {

                            if (!model.isCurrentHistoryStateEmpty(i)) {
                                AudioService.soundParticle()
                                publishProgress(i)
                                Thread.sleep(REFRESH_RATE_MILLISECONDS.toLong())
                            }

                        }
                        catch (e: InterruptedException) {
                            e.printStackTrace()
                            handleOnCancelled()
                        }

                    }

                    model.resetReactionPropagationDepth()

                }

                AudioService.soundParticle()
                publishProgress(SHOW_CURRENT_PLAYGROUND_STATE)

            }

        }

    }

    /**
     * If the current calculation has finished, informs the presenter
     */
    override fun onPostExecute(result: Boolean?) {
        presenter.notifyCalculationFinished()
    }

    override fun onPreExecute() {
        if (!cancelTask) {
        }
    }

    override fun onCancelled() {
        handleOnCancelled()
    }

    override fun onCancelled(result: Boolean?) {
        handleOnCancelled()
    }

    /**
     * Sets the static cancel task flag
     */
    private fun handleOnCancelled() {
        cancelTask = true
        presenter.notifyCalculationFinished()
    }

    /**
     * Refreshes the UI
     *
     * @param   values      The playground state index to show
     */
    override fun onProgressUpdate(vararg values: Int?) {

        //do if the task is not cancelled
        if (!cancelTask) {

            if (values[0] == SHOW_CURRENT_PLAYGROUND_STATE) {
                presenter.refreshPlayground(SHOW_CURRENT_PLAYGROUND_STATE)
            } else {
                //the current state to display
                presenter.refreshPlayground(values[0]!!)
            }

        }

    }

}
