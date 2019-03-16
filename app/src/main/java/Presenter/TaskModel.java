package Presenter;

import android.os.AsyncTask;

/**
 * Async task to execute model computations
 */
public class TaskModel extends AsyncTask<Void, Void, Void> {

    /**
     * Model object where the computation and state change occurs
     */
    IGameModel model;

    /**
     * Parameters where the Field of the requested step concluded by this task is
     */
    int pos_y;
    int pos_x;

    /**
     * Constructor of TaskModel
     *
     * @param   model	   Initialized IGameModel object to update
     */
    protected TaskModel(IGameModel model){

        this.model = model;

    }

    /**
     * Executes the query and the computation on the Model
     *
     * @param   pos_y	   Y coordinate
     * @param   pos_x      X coordinate
     * @return  Integer    Id of the next Player
     */
    protected Integer StepRequest(int pos_y, int pos_x) {

        return model.StepRequest(pos_y, pos_x);

    }

    /**
     * Starts the game
     *
     * @return  Integer     Id of the next Player
     */
    protected Integer StartGame() {

        return model.StartGame();

    }

    @Override
    protected Void doInBackground(Void... voids) {

        return null;

    }
}
