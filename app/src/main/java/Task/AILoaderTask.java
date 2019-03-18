package Task;

import Model.AI.PlayerLogic;
import Presenter.IGameModel;
import android.content.Context;
import android.os.AsyncTask;
import hu.bme.aut.android.chainreaction.R;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Async task to execute model computations
 */
public class AILoaderTask extends AsyncTask<Void, Void, Void> {

    /**
     * Model object where the computation and state change occurs
     */
    IGameModel model;

    /**
     * Constructor of InteractModelTask
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
