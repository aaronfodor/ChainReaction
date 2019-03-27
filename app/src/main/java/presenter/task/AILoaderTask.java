package presenter.task;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import model.ai.PlayerLogic;
import android.content.Context;
import android.os.AsyncTask;
import hu.bme.aut.android.chainreaction.R;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Async task to load AI component
 */
public class AILoaderTask extends AsyncTask<Void, Integer, Boolean> {

    /**
     * Context of the UI thread
     */
    Context context;

    /**
     * View of the UI thread
     */
    View rootView;

    /**
     * GameLogicTask constructor
     *
     * @param   context         Context of the UI thread
     * @param   rootView        View of the UI thread
     */
    public AILoaderTask(Context context, View rootView){

        this.context = context;
        this.rootView = rootView;

    }

    /**
     * Loads and sets the Neural Network of PlayerLogic
     *
     * @param   params      none
     * @return 	Boolean 	True when AI component loading is finished
     */
    @Override
    protected Boolean doInBackground(Void... params) {

        InputStream inStream = context.getResources().openRawResource(R.raw.player_neural_network);

        try {

            MultiLayerNetwork network = ModelSerializer.restoreMultiLayerNetwork(inStream);
            PlayerLogic.SetNeuralNetwork(network);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return true;

    }

    /**
     * Accesses the UI thread - notifies the user that AI loading was successful
     *
     * @param   result      Result of doInBackground
     */
    @Override
    protected void onPostExecute(Boolean result) {

        Button btn = rootView.findViewById(R.id.buttonNewGame);
        btn.setText(R.string.button_newgame);
        btn.setEnabled(true);

    }

    @Override
    protected void onPreExecute() {

            Button btn = rootView.findViewById(R.id.buttonNewGame);
            btn.setText(R.string.button_loading);
            btn.setEnabled(false);

    }

    @Override
    protected void onProgressUpdate(Integer... values) {}

}
