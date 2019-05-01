package presenter;

import android.os.AsyncTask;
import model.GamePlay;
import presenter.task.GameLogicTask;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * presenter of a game play
 * Controls what and how to be displayed on the view
 * Mediator between model and view
 */
public class GamePresenter {

    /**
     * MVP model
     */
    private IGameModel model;

    /**
     * MVP view
     */
    private IGameView view;

    /**
     * AsyncTask to execute model computations
     */
    private GameLogicTask game_task;

    /**
     * TimerTask to execute limited time counting
     */
    private static TimerTask timerTask;

    /**
     * Game mode flags
     */
    private Boolean showPropagation;
    private Boolean limitedTimeMode;

    /**
     * Constant to represent current state display intent
     */
    private static final int SHOW_CURRENT_PLAYGROUND_STATE = -1;

    /**
     * GamePresenter constructor
     * Sets the view, the Players, the model, starts the game play
     *
     * @param   view 	            IGameView object that holds the GamePresenter
     * @param   height              height of the Playground
     * @param   width 	            width of the Playground
     * @param   players_input       ArrayList of Strings; "<Player type>-<Player name>" format
     * @param   showPropagation     Show propagation flag
     * @param   limitedTimeMode     Limited time mode flag
     */
    public GamePresenter(IGameView view, int height, int width, ArrayList<String> players_input, Boolean showPropagation, Boolean limitedTimeMode){

        this.view = view;
        this.showPropagation = showPropagation;
        this.limitedTimeMode = limitedTimeMode;
        int number_of_players = players_input.size();
        ArrayList<String[]> players = new ArrayList<String[]>();

        for(int i = 0; i < number_of_players; i++){

            String[] input_data = players_input.get(i).split("-");
            String[] player_data = new String[3];
            player_data[0] = String.valueOf(i+1);
            player_data[1] = input_data[1];
            player_data[2] = input_data[0];

            players.add(player_data);

        }

        this.model = new GamePlay(this, height, width, players);
        this.RefreshPlayground(Integer.valueOf(players.get(0)[0]), 0);
        view.ShowStart(Integer.valueOf(players.get(0)[0]));

        game_task = new GameLogicTask(model, this, showPropagation);
        game_task.cancel(true);

    }

    /**
     * Requests a step to the Field described with the input parameters in a new task
     * Refreshes the view if the step is valid
     *
     * @param   pos_y	   Y coordinate
     * @param   pos_x      X coordinate
     * @param	duration   Duration of waiting
     */
    public void StepRequest(int pos_y, int pos_x, int duration){
        game_task = new GameLogicTask(model, this, showPropagation);
        game_task.execute(pos_y, pos_x, duration);
    }

    /**
     * Refreshes the Playground and tells the view to draw it
     *
     * @param   current_player_Id     Id of the current Player
     * @param   propagation_depth     Number of propagation states
     */
    public void RefreshPlayground(int current_player_Id, int propagation_depth){

        int[] dimension = model.GetDimension();
        int[][][] state_matrix = new int[dimension[0]][dimension[1]][];

        if(model.IsGameEnded() && propagation_depth == SHOW_CURRENT_PLAYGROUND_STATE){
            view.ShowResult(Math.abs(model.getActualPlayerId()), model.getPlayersData());
        }

        else{
            view.ShowCurrentPlayer(Math.abs(model.getActualPlayerId()));
        }

        if(propagation_depth >= 0 && propagation_depth < model.GetReactionPropagationDepth()){
            state_matrix = model.HistoryPlaygroundInfoAt(propagation_depth);
        }

        else{
            state_matrix = model.ActualPlaygroundInfo();
        }

        for(int actual_height = 0; actual_height < dimension[0]; actual_height++){

            for(int actual_width = 0; actual_width < dimension[1]; actual_width++){
                view.RefreshPlayground(actual_height, actual_width,
                        PlayerColor.GetColorById(state_matrix[actual_height][actual_width][0]),
                        state_matrix[actual_height][actual_width][1]);
            }

        }

        if(!model.IsGameEnded() && limitedTimeMode){

            if(timerTask != null){
                timerTask.cancel();
            }

            if(propagation_depth <= 0){

                timerTask = new TimerTask() {

                    int counter = 0;

                    @Override
                    public void run() {

                        if(counter < 100){
                            view.RefreshProgressBar(counter);
                            counter++;
                        }

                        else{
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                };

                Timer timer = new Timer("MyTimer");
                timer.scheduleAtFixedRate(timerTask, 20, 50);
                timerTask.run();

            }

        }

    }

    /**
     * Returns the AsyncTask
     *
     * @return 	AsyncTask     Returns the AsyncTask. If empty, returns null
     */
    public AsyncTask getTask(){

        if(game_task != null){
            return game_task;
        }
        else{
            return null;
        }

    }

}