package Presenter.Task;

import Presenter.IGameModel;
import android.os.AsyncTask;

/**
 * Async task to execute model computations
 */
public class InteractModelTask extends AsyncTask<Void, Void, Void> {

    /**
     * Model object where the computation and state change occurs
     */
    IGameModel model;

    /**
     * Constructor of InteractModelTask
     *
     * @param   model	   Initialized IGameModel object to update
     */
    public InteractModelTask(IGameModel model){

        this.model = model;

    }

    /**
     * Executes the query and the computation on the Model
     *
     * @param   pos_y	   Y coordinate
     * @param   pos_x      X coordinate
     * @return  Integer    Id of the next Player
     */
    public Integer StepRequest(int pos_y, int pos_x) {

        return model.StepRequest(pos_y, pos_x);

    }

    /**
     * Starts the game
     *
     * @return  Integer     Id of the next Player
     */
    public Integer StartGame() {

        return model.StartGame();

    }

    @Override
    protected Void doInBackground(Void... voids) {

        return null;

    }
}
