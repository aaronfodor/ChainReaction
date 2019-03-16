package Presenter;

import Model.GamePlay;
import java.util.ArrayList;

/**
 * Presenter of a game play
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
     * Task to execute model computations
     */
    private StepRequestTask task;

    /**
     * GamePresenter constructor
     * Sets the view, the Players, the model, starts the game play
     *
     * @param   view 	    IGameView object that holds the GamePresenter
     * @param   height      height of the Playground
     * @param   width 	    width of the Playground
     */
    public GamePresenter(IGameView view, int height, int width){

        this.view = view;

        ArrayList<String[]> players = new ArrayList<String[]>();

        String[] player1 = new String[]{"1","SanyiHooman","human"};
        String[] player2 = new String[]{"2","RobiRobot","human"};

        players.add(player1);
        players.add(player2);

        this.model = new GamePlay(height,width,players);
        this.model.StartGame();

        int starter_player_Id = Integer.valueOf(players.get(0)[0]);
        this.RefreshPlayground(starter_player_Id);

    }

    /**
     * Requests a step to the Field described with the input parameters in a new task
     * Refreshes the view if the step is valid
     *
     * @param   pos_y	   Y coordinate
     * @param   pos_x      X coordinate
     */
    public void StepRequest(int pos_y, int pos_x){

        task = new StepRequestTask(model, pos_y, pos_x);
        int current_player_Id = task.doInBackground();

        if(current_player_Id == 0){

            view.ShowMessage("Invalid click!");
            return;

        }

        this.RefreshPlayground(current_player_Id);

        if(current_player_Id < 0){

            view.ShowResult("Player " + Math.abs(current_player_Id) + " is the winner!");

        }

    }

    /**
     * Refreshes the Playground and tells the view to draw it
     *
     * @param   current_player_Id     Id of the current Player
     */
    private void RefreshPlayground(int current_player_Id){

        current_player_Id = Math.abs(current_player_Id);

        view.ShowCurrentPlayer(current_player_Id);

        int[][][] state_matrix = model.ActualPlaygroundInfo();
        int[] dimension = model.GetDimension();

        for(int actual_height = 0; actual_height < dimension[0]; actual_height++){

            for(int actual_width = 0; actual_width < dimension[1]; actual_width++){

                view.RefreshPlayground(actual_height, actual_width,
                        PlayerColor.GetColorById(state_matrix[actual_height][actual_width][0]),
                        state_matrix[actual_height][actual_width][1]);

            }

        }

    }

}