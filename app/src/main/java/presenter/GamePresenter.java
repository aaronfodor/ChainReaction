package presenter;

import android.os.AsyncTask;
import model.GamePlay;
import org.jetbrains.annotations.NotNull;
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
     * Type of the HUMAN Players
     */
    private final int HUMAN = 1;

    /**
     * Time limit in milliseconds
     */
    private int timeLimit = 4000;

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
        this.RefreshPlayground(SHOW_CURRENT_PLAYGROUND_STATE);
        view.ShowStart(Integer.valueOf(players.get(0)[0]));

        game_task = new GameLogicTask(model, this, showPropagation, limitedTimeMode);
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
        game_task = new GameLogicTask(model, this, showPropagation, limitedTimeMode);
        game_task.execute(pos_y, pos_x, duration);
    }

    /**
     * Time is up handling
     * Sets the current Player to the next one
     *
     * @param	duration   Duration of waiting which is going to be recorded
     */
    public void playerTimeIsUp(int duration){
        game_task = new GameLogicTask(model, this, showPropagation, limitedTimeMode);
        game_task.execute(duration);
    }

    /**
     * Refreshes the Playground and tells the view to draw it
     *
     * @param   propagation_depth     Number of propagation states
     */
    public void RefreshPlayground(int propagation_depth){

        int[] dimension = model.GetDimension();
        int[][][] state_matrix = new int[dimension[0]][dimension[1]][];

        if(model.IsGameEnded() && propagation_depth == SHOW_CURRENT_PLAYGROUND_STATE){
            state_matrix = model.ActualPlaygroundInfo();
            view.ShowCurrentPlayer(Math.abs(model.getActualPlayerId()));
            view.ShowResult(Math.abs(model.getActualPlayerId()), model.getPlayersData());
        }

        else if(propagation_depth >= 0 && propagation_depth < model.GetReactionPropagationDepth()){
            state_matrix = model.HistoryPlaygroundInfoAt(propagation_depth);
        }

        else{
            state_matrix = model.ActualPlaygroundInfo();
            view.ShowCurrentPlayer(Math.abs(model.getActualPlayerId()));
        }

        for(int actual_height = 0; actual_height < dimension[0]; actual_height++){

            for(int actual_width = 0; actual_width < dimension[1]; actual_width++){
                view.RefreshPlayground(actual_height, actual_width,
                        state_matrix[actual_height][actual_width][0],
                        state_matrix[actual_height][actual_width][1]);
            }

        }

        if(timerTask != null){
            timerTask.cancel();
        }
        view.RefreshProgressBar(0);

        //when limited time mode is enabled, it only applies to human players when the game is not over
        if(!model.IsGameEnded() && limitedTimeMode && model.getActualPlayerType() == HUMAN){

            //show the current state, not the propagation states
            if(propagation_depth <= 0){

                timerTask = new TimerTask() {

                    int counter = 0;

                    @Override
                    public void run() {

                        if(counter < 100){
                            view.RefreshProgressBar(counter);
                            counter++;
                        }

                        else if(counter == 100){
                            view.RefreshProgressBar(0);
                            playerTimeIsUp(timeLimit);
                        }

                        else{
                            Thread.currentThread().interrupt();
                            view.RefreshProgressBar(0);
                        }

                    }
                };

                Timer timer = new Timer("MyTimer");
                timer.scheduleAtFixedRate(timerTask, 50, timeLimit/100);
                timerTask.run();

            }

        }

    }

    /**
     * Returns the game AsyncTask
     *
     * @return 	AsyncTask     Returns the game AsyncTask. If empty, returns null
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