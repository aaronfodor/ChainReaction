package model;

import presenter.GamePresenter;
import presenter.IGameModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class that controls the whole GamePlay
 *
 */
public class GamePlay implements IGameModel {

    /**
     * Playground in the GamePlay
     */
    private Playground playground;

    /**
     * List of the Players
     */
    private ArrayList<Player> players;

    /**
     * Current Player's position in players
     * State machine status which Player to come next
     */
    private int current_player_index;

    /**
     * the winner Player
     * State machine status whether the GamePlay has ended or not
     */
    private int winner_Id = 0;

    /**
     * Minimum players needed to start the GamePlay
     */
    private int min_players_to_start = 2;

    /**
     * Until the end of the first round no one can win
     */
    private boolean first_round_ended = false;

    /**
     * MVP presenter
     */
    private GamePresenter presenter;

    int[][][][] history_matrix;

    /**
     * GamePlay constructor
     * Creates the Playground, sets the Players, initializes the state machine
     *
     * @param	height	        Height of the Playground
     * @param	width	        Width of the Playground
     * @param	players_raw	    Players represented by ArrayList of String arrays of 3 elements
     */
    public GamePlay(GamePresenter presenter, int height, int width, ArrayList<String[]> players_raw){

        this.presenter = presenter;

        ArrayList<Player> players = new ArrayList<Player>();

        for(String[] raw_player_element : players_raw){

            if(raw_player_element[2].equals("human")){

                players.add(new PlayerHuman(Integer.valueOf(raw_player_element[0]),raw_player_element[1]));

            }

            else{

                players.add(new PlayerAI(Integer.valueOf(raw_player_element[0]),raw_player_element[1]));

            }

        }

        this.players = players;
        this.current_player_index = 0;
        this.winner_Id = 0;
        this.first_round_ended = false;
        this.playground = new Playground(this, height, width);

        for(Player player : this.players){

            player.SetGamePlay(this);

        }

    }

    /**
     * Starts the actual GamePlay
     *
     * @return 	int     Id of the Player to start the game with
     */
    public int StartGame(){

        Integer[] coordinates = players.get(current_player_index).ExecuteStep();

        if(coordinates != null){

            players.get(current_player_index).ExecuteStep(coordinates[0], coordinates[1]);

            return StepToNextPlayer();

        }

        else{

            return players.get(current_player_index).GetId();

        }

    }

    /**
     * Triggers the AI Players to step in their order
     * Sets the current Player Id to the actual Player's Id
     *
     * @return 	int     minus value is the (-1)*Id of the winner; positive value is the Id of the current Player
     */
    public int StepToNextPlayer(){

        if(current_player_index >= players.size()-1){

            current_player_index = 0;

            if(!first_round_ended){

                first_round_ended = true;

            }

        }
        else{

            current_player_index++;

        }

        return players.get(current_player_index).GetId();

    }

    /**
     * Requesting a step on behalf of the current player based on the state machine status
     *
     * @param	pos_y       Y coordinate
     * @param	pos_x       X coordinate
     * @return 	int         minus value is the (-1)*Id of the winner; positive value is the Id of the current Player; 0 means step was unsuccessful
     */
    public int StepRequest(int pos_y, int pos_x){

        if(winner_Id != 0){

            return (-1)*winner_Id;

        }

        if(players.get(current_player_index).ExecuteStep(pos_y,pos_x)){

            if(this.IsGameEnded()){

                return (-1)*winner_Id;

            }

        }
        else{

            return 0;

        }

        return StepToNextPlayer();

    }

    /**
     * Updating the players list based on the current Playground status
     * Removes the Player from the players if no territory belongs to it anymore
     * Does not remove a Player during the first round
     * Sets winner_Id if there is a winner
     *
     * @return 	boolean     True means there was modification in the players list, false means there was no change
     */
    private boolean UpdatePlayersInGame(){

        boolean removed_flag = false;

        if(!first_round_ended){

            return removed_flag;

        }

        //key is the Id of the Player, value is the number of Fields it owns
        HashMap<Integer,Integer> field_number_of_players = new HashMap<Integer, Integer>();

        for(Player player : players) {

            field_number_of_players.put(player.GetId(),0);

        }

        for(int actual_height = 0; actual_height < this.playground.GetHeight(); actual_height++){

            for(int actual_width = 0; actual_width < this.playground.GetWidth(); actual_width++){

                int temp_Id = this.playground.GetFieldAt(actual_height,actual_width).GetPlayerIdOnField();

                if(temp_Id != 0){

                    field_number_of_players.put(temp_Id, field_number_of_players.get(temp_Id)+1);

                }
            }

        }

        ArrayList<Player> players_to_remove = new ArrayList<Player>();

        for(HashMap.Entry<Integer,Integer> entry : field_number_of_players.entrySet()){

            if(entry.getValue() == 0){

                for(Player player : players){

                    if(player.GetId() == entry.getKey()){

                        players_to_remove.add(player);

                    }

                }

            }

        }

        if(players_to_remove.size() > 0){

            removed_flag = true;

        }

        for(Player player : players_to_remove){

            if(players.indexOf(player) < current_player_index){

                current_player_index--;

            }

            players.remove(player);

        }

        if(players.size() == 1){

            winner_Id = players.get(current_player_index).GetId();

        }

        return removed_flag;

    }

    /**
     * Provides the actual Playground info - Player and it's elements on Field
     * First index is the Y coordinate of Field
     * Second index is the X coordinate of Field
     * Third index is the Field specific information: [0] is the owner Player's Id, [1] is the number of elements on the Field, [2] is the number of residual elements left before explosion
     *
     * @return 	int[][][]   Field information matrix
     */
    public int[][][] ActualPlaygroundInfo() {

        int[] dim = this.GetDimension();

        int[][][] state_matrix = new int[dim[0]][dim[1]][3];

        for(int actual_height = 0; actual_height < dim[0]; actual_height++){

            for(int actual_width = 0; actual_width < dim[1]; actual_width++){

                state_matrix[actual_height][actual_width][0] = playground.GetFieldAt(actual_height, actual_width).GetPlayerIdOnField();
                state_matrix[actual_height][actual_width][1] = playground.GetFieldAt(actual_height, actual_width).GetParticle().GetSize();
                state_matrix[actual_height][actual_width][2] = playground.GetFieldAt(actual_height, actual_width).GetParticle().GetNumberLeftBeforeExplosion();

            }

        }

        return state_matrix;
    }

    /**
     * Provides the actual Playground info - Player and it's elements on Field
     * First index is the Y coordinate of Field
     * Second index is the X coordinate of Field
     * Third index is the Field specific information: [0] is the owner Player's Id, [1] is the number of elements on the Field, [2] is the number of residual elements left before explosion
     *
     * @return 	int[][][]   Field information matrix
     */
    public int[][][] HistoryPlaygroundInfoAt(int propagation_depth) {

        if(this.history_matrix == null){

            return this.ActualPlaygroundInfo();

        }

        int[] dim = this.GetDimension();

        int[][][] state_matrix = new int[dim[0]][dim[1]][3];

        for(int actual_height = 0; actual_height < dim[0]; actual_height++){

            for(int actual_width = 0; actual_width < dim[1]; actual_width++){

                int[] actual_field_state = this.history_matrix[actual_height][actual_width][propagation_depth];

                state_matrix[actual_height][actual_width] = actual_field_state;

            }

        }

        return state_matrix;
    }

    /**
     * Provides the actual Playground info - Player and it's elements on Field
     * First index is the Y coordinate of Field
     * Second index is the X coordinate of Field
     * Third index contains the states during reaction propagation
     * Fourth index is the Field specific information: [0] is the current reaction propagation time, [1] is the owner Player's Id, [2] is the number of elements on the Field, [3] is the number of residual elements left before explosion
     *
     * @return 	int[][][][]   Field information matrix
     */
    public void HistoryPlaygroundBuilder() {

        int[] dim = this.GetDimension();

        int propagation_depth = this.playground.GetReactionPropagationDepth();

        int[][][][] state_matrix = new int[dim[0]][dim[1]][propagation_depth][];

        for(int actual_height = 0; actual_height < dim[0]; actual_height++){

            for(int actual_width = 0; actual_width < dim[1]; actual_width++){

                for(int propagation_time = 0; propagation_time < playground.GetFieldAt(actual_height, actual_width).NumberOfStates(); propagation_time++){

                    state_matrix[actual_height][actual_width][propagation_time] = playground.GetFieldAt(actual_height, actual_width).GetStateAt(propagation_time);

                }

                playground.GetFieldAt(actual_height, actual_width).Clear();

            }

        }

        //this.playground.ResetReactionPropagationDepth();

        this.history_matrix = state_matrix;
    }

    /**
     * Provides the dimensions of the Playground
     * [0] is the width of the Playground
     * [1] is the height of the Playground
     *
     * @return 	int[]   Playground dimension info
     */
    public int[] GetDimension() {

        int[] dim = new int[2];

        dim[0] = playground.GetHeight();
        dim[1] = playground.GetWidth();

        return dim;

    }

    /**
     * GamePlay's Playground getter method
     *
     * @return  playground  Playground in the GamePlay
     */
    protected Playground GetPlayground() {

        return this.playground;

    }

    /**
     * Called to decide whether the GamePlay is over or not
     *
     * @return 	boolean     True means Game over, False otherwise
     */
    protected boolean IsGameEnded(){

        this.UpdatePlayersInGame();
        return winner_Id != 0;

    }

    /**
     * Returns the type of the winner
     *
     * @return 	String     AI or human
     */
    public String WinnerType(){

        if(players.get(winner_Id).ExecuteStep() != null){

            return "AI";

        }

        else{

            return "human";

        }

    }

    /**
     * Returns the Id of the current Player
     *
     * @return 	Integer     Id of the current Player
     */
    public Integer getActualPlayerId(){

        return players.get(current_player_index).GetId();

    }

    /**
     * Returns the Id of the current Player
     *
     * @return 	Integer[]     Automatic stepping coordinates of the current Player: [0] is the y coordinate, [1] is the x coordinate. If not available, returns null
     */
    public Integer[] getAutoCoordinates(){

        return players.get(current_player_index).ExecuteStep();

    }

    /**
     * Reaction propagation depth getter method
     *
     * @return 	int     Reaction propagation depth
     */
    public int GetReactionPropagationDepth(){

        return this.playground.GetReactionPropagationDepth();

    }

}
