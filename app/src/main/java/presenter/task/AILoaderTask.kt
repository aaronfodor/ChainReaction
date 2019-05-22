package presenter.task

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Button
import model.ai.PlayerLogic
import android.content.Context
import android.os.AsyncTask
import hu.bme.aut.android.chainreaction.R
import org.deeplearning4j.util.ModelSerializer

import java.io.IOException

/**
 * Async task to load AI component
 */
class AILoaderTask
/**
 * GameLogicTask constructor
 *
 * @param   context         Context of the UI thread
 * @param   rootView        View of the UI thread
 */
    (
    /**
     * Context of the UI thread
     */
    internal var context: Context,
    /**
     * View of the UI thread
     */
    private var rootView: View
) : AsyncTask<Void, Int, Boolean>() {

    /**
     * Loads and sets the Neural Network of PlayerLogic
     *
     * @param   params      none
     * @return  Boolean 	True when AI component loading is finished
     */
    override fun doInBackground(vararg params: Void): Boolean? {

        val inStream = context.resources.openRawResource(R.raw.player_neural_network)

        try {
            val network = ModelSerializer.restoreMultiLayerNetwork(inStream)
            PlayerLogic.SetNeuralNetwork(network)
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

        return true

    }

    /**
     * Accesses the UI thread - notifies the user about AI loading result
     *
     * @param   result      Result of doInBackground
     */
    override fun onPostExecute(result: Boolean) {

        val btn = rootView.findViewById<Button>(R.id.buttonNewGame)

        if(result){
            btn.setText(R.string.button_new_game)
            btn.isEnabled = true
            Snackbar.make(rootView, R.string.ai_loaded, Snackbar.LENGTH_LONG).show()
        }

        else{
            btn.setText(R.string.ai_load_error)
            btn.isEnabled = false
            Snackbar.make(rootView, R.string.ai_load_error, Snackbar.LENGTH_LONG).show()
        }

    }

    override fun onPreExecute() {
        val btn = rootView.findViewById<Button>(R.id.buttonNewGame)
        btn.setText(R.string.button_loading)
        btn.isEnabled = false
    }

    override fun onProgressUpdate(vararg values: Int?) {}

}
