package Presenter;

import android.os.AsyncTask;

/**
 * Async task to execute model computations
 */
public class StepRequestTask extends AsyncTask<Void, Void, Integer> {

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
     * Constructor of StepRequestTask
     *
     * @param   model	   Initialized IGameModel object to update
     * @param   pos_y	   Y coordinate
     * @param   pos_x      X coordinate
     */
    protected StepRequestTask(IGameModel model, int pos_y, int pos_x){

        this.model = model;
        this.pos_y = pos_y;
        this.pos_x = pos_x;

    }

    /**
     * Executes the query and the computation on the Model
     *
     * @param   parameters	    Empty (Void) input
     * @return  Integer         Id of the next Player
     */
    @Override
    protected Integer doInBackground(Void... parameters) {

        return model.StepRequest(pos_y, pos_x);

    }

}
