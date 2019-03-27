package presenter.task;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import hu.bme.aut.android.chainreaction.R;
import model.ai.PlayerLogic;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.IOException;
import java.io.InputStream;

public class LongOperation extends AsyncTask<Void, Integer, Boolean> {

    Context context;
    View rootView;

    /**
     * GameLogicTask constructor
     *
     * @param   context         Context of the UI thread
     * @param   rootView        View of the UI thread
     */
    public LongOperation(Context context, View rootView){

        this.context = context;
        this.rootView = rootView;

    }

    /**
     * Loads and sets the Neural Network of PlayerLogic
     *
     * @param   params       none
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

        int i = 0;
        while (i <= 90) {
            try {
                Thread.sleep(100);
                publishProgress(i);
                i++;
            }
            catch (Exception e) {

            }
        }

        return true;

    }

    /**
     * Accesses the UI thread - notifies the user that AI component loading was successful
     *
     * @param   result      Result of doInBackground
     */
    @Override
    protected void onPostExecute(Boolean result) {

        /*Toast.makeText(
                context,
                hu.bme.aut.android.chainreaction.R.string.ai_loaded,
                Toast.LENGTH_LONG
        ).show();*/

        Button btn = rootView.findViewById(R.id.buttonNewGame);
        btn.setText(R.string.button_newgame);
        btn.setEnabled(true);

    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate();

        Button btn = rootView.findViewById(R.id.buttonNewGame);

        if(values[0] % 3 == 1){

            btn.setText("Loading AI ..");

        }

        else if (values[0] % 3 == 2){

            btn.setText("Loading AI. .");

        }

        else if (values[0] % 3 == 0){

            btn.setText("Loading AI.. ");

        }

    }

}