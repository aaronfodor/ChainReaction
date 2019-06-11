package presenter.task

import android.os.AsyncTask
import presenter.GamePresenter
import presenter.IGameModel

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
     * model object where the computation and state change occurs
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
    private val timeLimitMode: Boolean?
) : AsyncTask<Int, Int, Boolean>() {

    companion object {
        /**
         * Constant to represent current state display intent
         */
        private const val SHOW_CURRENT_PLAYGROUND_STATE = -1

        /**
         * Static cancel flag of all GameLogicTask instances
         */
        internal var cancelTask = true
        /**
         * Time delay between two UI refresh events in milliseconds
         */
        private const val REFRESH_RATE_MILLISECONDS = 300
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

            if (params.isEmpty()) {

                propagationDisplayManager(SHOW_CURRENT_PLAYGROUND_STATE)

                var startTime: Long? = System.currentTimeMillis()
                var coordinates: Array<Int>? = model.autoCoordinates
                var estimatedTime: Long? = System.currentTimeMillis() - startTime!!
                var estimated = estimatedTime!!.toInt()

                while (coordinates != null) {

                    if (cancelTask) {
                        break
                    }

                    try {

                        propagationDisplayManager(coordinates[0], coordinates[1], estimated)
                        Thread.sleep(REFRESH_RATE_MILLISECONDS.toLong())

                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                        handleOnCancelled()
                    }

                    startTime = System.currentTimeMillis()
                    coordinates = model.autoCoordinates
                    estimatedTime = System.currentTimeMillis() - startTime
                    estimated = estimatedTime.toInt()

                }

            } else if (params.size == 1) {

                model.addCurrentPlayerWaitingTime(params[0]!!)
                propagationDisplayManager(SHOW_CURRENT_PLAYGROUND_STATE)
                model.stepToNextPlayer()

                var coordinates: Array<Int>?

                var startTime: Long? = System.currentTimeMillis()
                coordinates = model.autoCoordinates
                var estimatedTime: Long? = System.currentTimeMillis() - startTime!!
                var estimated = estimatedTime!!.toInt()

                if(coordinates != null){
                    Thread.sleep(REFRESH_RATE_MILLISECONDS.toLong())
                }

                while (coordinates != null) {

                    if (cancelTask) {
                        break
                    }

                    try {

                        propagationDisplayManager(coordinates[0], coordinates[1], estimated)
                        Thread.sleep(REFRESH_RATE_MILLISECONDS.toLong())

                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                        handleOnCancelled()
                    }

                    startTime = System.currentTimeMillis()
                    coordinates = model.autoCoordinates
                    estimatedTime = System.currentTimeMillis() - startTime
                    estimated = estimatedTime.toInt()

                }

            } else {

                var coordinates: Array<Int>? = arrayOf(0, 0)

                var startTime: Long? = System.currentTimeMillis()
                val autoCoordinates: Array<Int>? = model.autoCoordinates
                var estimatedTime: Long? = System.currentTimeMillis() - startTime!!
                var estimated = estimatedTime!!.toInt()

                if (autoCoordinates == null) {
                    coordinates?.set(0, params[0]!!)
                    coordinates?.set(1, params[1]!!)
                    estimated = params[2]!!
                } else {
                    coordinates?.set(0, autoCoordinates[0])
                    coordinates?.set(1, autoCoordinates[1])
                }

                while (coordinates != null) {

                    if (cancelTask) {
                        break
                    }

                    try {

                        propagationDisplayManager(coordinates[0], coordinates[1], estimated)
                        Thread.sleep(REFRESH_RATE_MILLISECONDS.toLong())

                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                        handleOnCancelled()
                    }

                    startTime = System.currentTimeMillis()
                    coordinates = model.autoCoordinates
                    estimatedTime = System.currentTimeMillis() - startTime
                    estimated = estimatedTime.toInt()

                }

            }
            //step request happened
            //time is up, Player is not allowed to step, next Player comes

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
            } else {

                model.addCurrentPlayerWaitingTime(values[2])

                if (model.stepRequest(values[0], values[1]) == 0 && timeLimitMode!!) {
                    model.stepToNextPlayer()
                } else if (showPropagation!!) {

                    model.historyPlaygroundBuilder()
                    val propagationDepth = model.getReactionPropagationDepth()

                    //start from state 2 to show propagation events without delay - the previously displayed end state is the start state too
                    for (i in 2 until propagationDepth) {

                        if (cancelTask) {
                            break
                        }

                        try {

                            if (!model.isCurrentHistoryStateEmpty(i)) {
                                publishProgress(model.lastPlayerId, i)
                                Thread.sleep(REFRESH_RATE_MILLISECONDS.toLong())
                            }

                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                            handleOnCancelled()
                        }

                    }

                    model.resetReactionPropagationDepth()

                }

                publishProgress(SHOW_CURRENT_PLAYGROUND_STATE)

            }

        }

    }

    override fun onPostExecute(result: Boolean?) {
        if (!cancelTask) {
        }
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
    }

    /**
     * Refreshes the UI
     *
     * @param   values      The coordinates where actual Player steps - if empty, just draws the playground
     */
    override fun onProgressUpdate(vararg values: Int?) {

        //do if the task is not cancelled
        if (!cancelTask) {

            if (values.size == 1) {
                presenter.refreshPlayground(values[0]!!)
            } else {
                //values[0] is the Player Id which is not used
                presenter.refreshPlayground(values[1]!!)
            }

        }

    }

}
