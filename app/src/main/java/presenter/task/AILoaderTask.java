package presenter.task;

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
public class AILoaderTask extends AsyncTask<Void, Void, Void> {

    /**
     * Loads and sets the Neural Network of PlayerLogic
     *
     * @param  context   Context
     */
    public void LoadNeuralNetwork(Context context){

        InputStream inStream = context.getResources().openRawResource(R.raw.player_neural_network);

        try {

            MultiLayerNetwork network = ModelSerializer.restoreMultiLayerNetwork(inStream);
            PlayerLogic.SetNeuralNetwork(network);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    @Override
    protected Void doInBackground(Void... voids) {

        return null;

    }
}
