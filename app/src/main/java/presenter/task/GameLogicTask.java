package presenter.task;

import android.os.AsyncTask;
import presenter.GamePresenter;
import presenter.IGameModel;

/**
 * Async task to execute model computations
 */
public class GameLogicTask extends AsyncTask<Integer, Integer, Boolean> {

    /**
     * model object where the computation and state change occurs
     */
    private IGameModel model;

    /**
     * presenter object which is on the UI thread
     */
    private GamePresenter presenter;

    /**
     * Time delay between two UI refresh events in milliseconds
     */
    private Integer refresh_rate_milliseconds = 300;

    /**
     * Whether to show propagation states or not
     */
    private Boolean showPropagation;

    private static final int SHOW_CURRENT_PLAYGROUND_STATE = -1;

    /**
     * GameLogicTask constructor
     *
     * @param   model                       model of the game play
     * @param   presenter                   presenter of the game play - the UI thread
     * @param   showPropagation             is UI refresh allowed while propagating
     */
    public GameLogicTask(IGameModel model, GamePresenter presenter, Boolean showPropagation){

        this.model = model;
        this.presenter = presenter;
        this.showPropagation = showPropagation;

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

            PropagationDisplayManager(SHOW_CURRENT_PLAYGROUND_STATE);

            Integer[] coordinates = model.getAutoCoordinates();

            while(coordinates != null){

                if(isCancelled()){

                    break;

                }

                try {

                    PropagationDisplayManager(coordinates[0], coordinates[1]);
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

            while(coordinates != null) {

                if(isCancelled()){

                    break;

                }

                try {

                    PropagationDisplayManager(coordinates[0], coordinates[1]);
                    Thread.sleep(refresh_rate_milliseconds);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                coordinates = model.getAutoCoordinates();

            }

        }

        return true;

    }

    private void PropagationDisplayManager(Integer... values) {

        if(values.length == 1){

            publishProgress(SHOW_CURRENT_PLAYGROUND_STATE);

        }

        else{

            model.StepRequest(values[0], values[1]);

            if(showPropagation){

                model.HistoryPlaygroundBuilder();

                int propagation_depth = model.GetReactionPropagationDepth();

                for(int i = propagation_depth-2; i > 0; i--){

                    if(isCancelled()){

                        break;

                    }

                    try {

                        publishProgress(model.getLastPlayerId(), i);
                        Thread.sleep(refresh_rate_milliseconds);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                }

                model.ResetReactionPropagationDepth();

            }

            publishProgress(SHOW_CURRENT_PLAYGROUND_STATE);

        }

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

        if(isCancelled()){

            return;

        }

        if(values.length == 1){

            presenter.RefreshPlayground(model.getActualPlayerId(), values[0]);

        }

        else{

            presenter.RefreshPlayground(values[0], values[1]);

        }

    }

}
