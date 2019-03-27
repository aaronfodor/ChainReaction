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
        this.refresh_rate_milliseconds = refresh_rate_milliseconds;

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

            publishProgress(0);

            Integer[] coordinates = model.getAutoCoordinates();

            try{

                while(coordinates != null){

                    if(isCancelled()){

                        break;

                    }

                    publishProgress(coordinates);
                    Thread.sleep(refresh_rate_milliseconds);

                    /*for(int i = 0; i < model.GetReactionPropagationDepth(); i++){

                        if(isCancelled()){

                            break;

                        }

                        publishProgress(i);
                        //Thread.sleep(refresh_rate_milliseconds);

                    }*/

                    //int[][][][] state_matrix = model.HistoryPlaygroundInfo();

                    coordinates = model.getAutoCoordinates();

                }

            } catch (InterruptedException e) {

                e.printStackTrace();

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

            try{

                while(coordinates != null){

                    if(isCancelled()){

                        break;

                    }

                    publishProgress(coordinates);
                    Thread.sleep(refresh_rate_milliseconds);

                    /*for(int i = 0; i < model.GetReactionPropagationDepth(); i++){

                        if(isCancelled()){

                            break;

                        }

                        publishProgress(i);
                        //Thread.sleep(refresh_rate_milliseconds);

                    }*/

                    //int[][][][] state_matrix = model.HistoryPlaygroundInfo();

                    coordinates = model.getAutoCoordinates();

                }

            } catch (InterruptedException e) {

                e.printStackTrace();

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

        if(values.length == 1){

            presenter.RefreshPlayground(model.getActualPlayerId(), values[0]);

        }

        else{

            presenter.RefreshPlayground(model.StepRequest(values[0], values[1]), 0);

        }

    }

}
