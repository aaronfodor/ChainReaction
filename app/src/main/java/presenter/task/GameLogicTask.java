package presenter.task;

import android.os.AsyncTask;
import presenter.GamePresenter;
import presenter.IGameModel;

/**
 * Async task to execute model computations
 */
public class GameLogicTask extends AsyncTask<Integer, Integer, Boolean> {

    /**
     * Model object where the computation and state change occurs
     */
    IGameModel model;

    /**
     * Presenter object which is on the UI thread
     */
    GamePresenter presenter;

    /**
     * Time delay between two UI refresh events in milliseconds
     */
    Integer refresh_rate_milliseconds;

    /**
     * GameLogicTask constructor
     *
     * @param   model                       Model of the game play
     * @param   presenter                   Presenter of the game play - the UI thread
     * @param   refresh_rate_milliseconds   Time delay between two UI refresh events in milliseconds
     */
    public GameLogicTask(IGameModel model, GamePresenter presenter, Integer refresh_rate_milliseconds){

        this.model = model;
        this.presenter = presenter;

    }

    /**
     * Does the model calculation and refreshes UI periodically via publish events
     *
     * @param   params      The selected coordinates where the Player wants to step; if empty, the game will be started
     * @return 	Boolean     True when the calculation has finished
     */
    @Override
    protected Boolean doInBackground(Integer... params) {

        if(params.length == 0){

            publishProgress();

            Integer[] coordinates = model.getAutoCoordinates();

            while(coordinates != null){

                try {

                    if(isCancelled()){

                        break;

                    }

                    publishProgress(coordinates);
                    Thread.sleep(refresh_rate_milliseconds);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

                coordinates = model.getAutoCoordinates();

            }


        }

        else{

            Integer[] coordinates = new Integer[2];

            Integer[] auto_coordinates = model.getAutoCoordinates();

            if(auto_coordinates == null){

                coordinates[0] = params[0];
                coordinates[1] = params[1];

            }

            else{

                coordinates[0] = auto_coordinates[0];
                coordinates[1] = auto_coordinates[1];

            }

            while(coordinates != null){

                try {

                    if(isCancelled()){

                        break;

                    }

                    publishProgress(coordinates);
                    Thread.sleep(refresh_rate_milliseconds);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

                coordinates = model.getAutoCoordinates();

            }


        }

        return true;

    }

    @Override
    protected void onPostExecute(Boolean result) {}

    @Override
    protected void onPreExecute() {}

    /**
     * Refreshes the UI
     *
     * @param   values      The coordinates where actual Player steps - if empty, just draws the playground
     */
    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate();

        if(values.length == 0){

            presenter.RefreshPlayground(model.getActualPlayerId());

        }

        else{

            presenter.RefreshPlayground(model.StepRequest(values[0], values[1]));

        }

    }

}