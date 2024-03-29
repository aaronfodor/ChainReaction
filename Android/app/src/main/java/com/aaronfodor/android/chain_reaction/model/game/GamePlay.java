package com.aaronfodor.android.chain_reaction.model.game;

import org.jetbrains.annotations.NotNull;
import com.aaronfodor.android.chain_reaction.presenter.IGameModel;

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
    private final Playground playground;

    /**
     * List of the Players
     */
    private final ArrayList<Player> players;

    /**
     * List of the defeated Players
     */
    private final ArrayList<Player> defeatedPlayers;

    /**
     * Current Player's position in players
     * State machine status which Player to come next
     */
    private int current_player_index;

    /**
     * Last Player's position in players
     */
    private int last_player_index;

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
     * True if AI and human Players also play, false otherwise
     */
    private boolean is_ai_vs_human_game = false;

    /**
     * History matrix
     * First index is the Y coordinate of Field
     * Second index is the X coordinate of Field
     * Third index contains the states during reaction propagation
     * Fourth index is the Field specific information: [0] is the current reaction propagation time, [1] is the owner Player's Id, [2] is the number of elements on the Field, [3] is the number of residual elements left before explosion
     */
    private int[][][][] history_matrix;

    /**
     * GamePlay constructor
     * Creates the Playground, sets the Players, initializes the state machine
     *
     * @param	height	        Height of the Playground
     * @param	width	        Width of the Playground
     * @param	players_raw	    Players represented by ArrayList of String arrays of 3 elements
     */
    public GamePlay(int height, int width, ArrayList<String[]> players_raw){

        this.players = new ArrayList<>();
        this.defeatedPlayers = new ArrayList<>();

        boolean humanPlayerPlays = false;
        boolean aiPlayerPlays = false;

        for(String[] raw_player_element : players_raw){

            if(raw_player_element[2].equals("human")){
                players.add(new PlayerHuman(Integer.parseInt(raw_player_element[0]),raw_player_element[1]));
                humanPlayerPlays = true;
            }

            else{
                players.add(new PlayerAI(Integer.parseInt(raw_player_element[0]),raw_player_element[1]));
                aiPlayerPlays = true;
            }

        }

        if(humanPlayerPlays && aiPlayerPlays){
            this.is_ai_vs_human_game = true;
        }

        this.current_player_index = 0;
        this.last_player_index = 0;
        this.winner_Id = 0;
        this.first_round_ended = false;
        this.playground = new Playground(this, height, width);

        for(Player player : this.players){
            player.setGamePlay(this);
        }

    }

    /**
     * Starts the actual GamePlay
     *
     * @return 	int     Id of the Player to start the game with
     */
    public int startGame(){

        Integer[] coordinates = players.get(current_player_index).executeStep();

        if(coordinates != null){
            players.get(current_player_index).executeStep(coordinates[0], coordinates[1]);
            return stepToNextPlayer();
        }

        else{
            return players.get(current_player_index).getId();
        }

    }

    /**
     * Triggers the AI Players to step in their order
     * Sets the current Player Id to the actual Player's Id
     * Nothing happens if the game is over
     *
     * @return 	int     minus value is the (-1)*Id of the winner; positive value is the Id of the current Player
     */
    public int stepToNextPlayer(){

        if(winner_Id != 0){
            return (-1)*winner_Id;
        }

        this.last_player_index = current_player_index;

        if(current_player_index >= players.size()-1){

            current_player_index = 0;

            if(!first_round_ended){
                first_round_ended = true;
            }

        }
        else{
            current_player_index++;
        }

        return players.get(current_player_index).getId();

    }

    /**
     * Requesting a step on behalf of the current player based on the state machine status
     *
     * @param	pos_y       Y coordinate
     * @param	pos_x       X coordinate
     * @return 	int         minus value is the (-1)*Id of the winner; positive value is the Id of the current Player; 0 means step was unsuccessful
     */
    public int stepRequest(int pos_y, int pos_x){

        if(winner_Id != 0){
            return (-1)*winner_Id;
        }

        if(players.get(current_player_index).executeStep(pos_y, pos_x)){

            if(this.gameStateRefresh()){
                return (-1)*winner_Id;
            }

        }

        else{
            return 0;
        }

        return stepToNextPlayer();

    }

    /**
     * Updating the players list based on the current Playground status
     * Removes the Player from the players if no territory belongs to it anymore
     * Does not remove a Player during the first round
     * Sets winner_Id if there is a winner
     * Nothing happens if the game has already ended
     */
    private void updatePlayersInGame(){

        if(!first_round_ended){
            return;
        }
        if(isGameEnded()){
            return;
        }

        //key is the Id of the Player, value is the number of Fields it owns
        HashMap<Integer,Integer> field_number_of_players = new HashMap<>();

        for(Player player : players) {
            field_number_of_players.put(player.getId(),0);
        }

        for(int actual_height = 0; actual_height < this.playground.getHeight(); actual_height++){

            for(int actual_width = 0; actual_width < this.playground.getWidth(); actual_width++){
                int temp_Id = this.playground.getFieldAt(actual_height,actual_width).getPlayerIdOnField();
                if(temp_Id != 0){

                    int field_number = 0;
                    try{
                        field_number = field_number_of_players.get(temp_Id);
                    }
                    catch (NullPointerException e){
                        field_number = 0;
                    }

                    field_number_of_players.put(temp_Id, field_number+1);
                }
            }

        }

        ArrayList<Player> players_to_remove = new ArrayList<>();

        for(HashMap.Entry<Integer,Integer> entry : field_number_of_players.entrySet()){

            if(entry.getValue() == 0){

                for(Player player : players){

                    if(player.getId() == entry.getKey()){
                        players_to_remove.add(player);
                        defeatedPlayers.add(player);
                    }

                }

            }

        }

        for(Player player : players_to_remove){

            if(players.indexOf(player) < current_player_index){
                current_player_index--;
            }

            players.remove(player);

        }

        if(players.size() == 1){
            winner_Id = players.get(current_player_index).getId();
        }

    }

    /**
     * Provides the actual Playground info - Player and it's elements on Field
     * First index is the Y coordinate of Field
     * Second index is the X coordinate of Field
     * Third index is the Field specific information: [0] is the owner Player's Id, [1] is the number of elements on the Field, [2] is the number of residual elements left before explosion
     *
     * @return 	int[][][]   Field information matrix
     */
    @NotNull
    public int[][][] currentPlaygroundInfo() {

        int[] dim = this.getDimension();

        int[][][] state_matrix = new int[dim[0]][dim[1]][3];

        for(int current_height = 0; current_height < dim[0]; current_height++){

            for(int current_width = 0; current_width < dim[1]; current_width++){
                state_matrix[current_height][current_width][0] = playground.getFieldAt(current_height, current_width).getPlayerIdOnField();
                state_matrix[current_height][current_width][1] = playground.getFieldAt(current_height, current_width).getParticle().getSize();
                state_matrix[current_height][current_width][2] = playground.getFieldAt(current_height, current_width).getParticle().getNumberLeftBeforeExplosion();
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
     * @param	propagation_depth   The index of the examined depth
     * @return 	int[][][]           Field information matrix
     */
    @NotNull
    public int[][][] historyPlaygroundInfoAt(int propagation_depth) {

        if(this.history_matrix == null){
            return this.currentPlaygroundInfo();
        }

        int[] dim = this.getDimension();
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
     */
    public void historyPlaygroundBuilder() {

        int[] dim = this.getDimension();

        int propagation_depth = this.playground.getReactionPropagationDepth();

        if(propagation_depth == 0){
            propagation_depth++;
        }

        int[][][][] state_matrix = new int[dim[0]][dim[1]][propagation_depth][3];

        for(int current_height = 0; current_height < dim[0]; current_height++){

            for(int current_width = 0; current_width < dim[1]; current_width++){

                if(playground.getFieldAt(current_height, current_width).numberOfStates() == 0){

                    int[] current_state = new int[3];
                    current_state[0] = this.playground.getFieldAt(current_height, current_width).getParticle().getOwnerId();
                    current_state[1] = this.playground.getFieldAt(current_height, current_width).getParticle().getSize();
                    current_state[2] = this.playground.getFieldAt(current_height, current_width).getParticle().getNumberLeftBeforeExplosion();

                    for(int i = 0; i < propagation_depth; i++){
                        state_matrix[current_height][current_width][i] = current_state;
                    }

                }

                else{

                    for(int propagation_time = 0; propagation_time < propagation_depth; propagation_time++){
                        state_matrix[current_height][current_width][propagation_time] = playground.getFieldAt(current_height, current_width).getStateAt(propagation_time);
                    }

                }

                playground.getFieldAt(current_height, current_width).clearHistory();

            }

        }

        this.history_matrix = state_matrix;
    }

    /**
     * Returns whether the current depth state is empty or not
     *
     * @param	propagation_depth   The index of the examined depth
     * @return 	boolean             True if the indexed depth is empty, false otherwise
     */
    public boolean isCurrentHistoryStateEmpty(int propagation_depth){

        if(this.history_matrix == null){
            return true;
        }

        int[] dim = this.getDimension();

        for(int current_height = 0; current_height < dim[0]; current_height++){

            for(int current_width = 0; current_width < dim[1]; current_width++){

                int[] current_field_state = this.history_matrix[current_height][current_width][propagation_depth];
                if(current_field_state[1] > 0){
                    return false;
                }

            }

        }

        return true;

    }

    /**
     * Provides the dimensions of the Playground
     * [0] is the width of the Playground
     * [1] is the height of the Playground
     *
     * @return 	int[]   Playground dimension info
     */
    @NotNull
    public int[] getDimension() {
        int[] dim = new int[2];
        dim[0] = playground.getHeight();
        dim[1] = playground.getWidth();
        return dim;
    }

    /**
     * GamePlay's Playground getter method
     *
     * @return  playground  Playground in the GamePlay
     */
    protected Playground getPlayground() {
        return this.playground;
    }

    /**
     * Called to refresh the game state
     * Nothing happens if the game is over
     *
     * @return 	boolean     True means Game over, False otherwise
     */
    protected boolean gameStateRefresh(){
        if(isGameEnded()){
            return true;
        }
        this.updatePlayersInGame();
        return this.isGameEnded();
    }

    /**
     * Called to decide whether the GamePlay is over or not
     *
     * @return 	boolean     True means Game over, False otherwise
     */
    public boolean isGameEnded(){
        return winner_Id != 0;
    }

    /**
     * Returns the Id of the current Player
     *
     * @return 	Integer     Id of the current Player
     */
    public Integer getCurrentPlayerId(){
        return players.get(current_player_index).getId();
    }

    /**
     * Returns the Type of the current Player
     *
     * @return 	Integer     Type of the current Player: 1 means human, 2 means AI
     */
    public Integer getCurrentPlayerType(){
        return players.get(current_player_index).getTypeId();
    }

    /**
     * Returns the Id of the last Player
     *
     * @return 	Integer     Id of the last Player
     */
    public Integer getLastPlayerId(){

        if(players.size() == 1){
            return 0;
        }

        else{
            return players.get(last_player_index).getId();
        }

    }

    /**
     * Waiting time adder method to the current Player
     * Nothing happens is the game is over
     *
     * @param 	duration 	Waiting duration of the current Player
     */
    public void addCurrentPlayerWaitingTime(int duration){
        if(isGameEnded()){
            return;
        }
        players.get(current_player_index).addWaitingTime(duration);
    }

    /**
     * Average waiting time getter method from the current Player
     *
     * @return int[][]     [i] is the Player index, [][0] is Player Id, [][1] is the average step time of Player, [][2] is the number of rounds of Player, [][3] is the type of the Player (1:human, 2:AI)
     */
    public int[][] getPlayersData(){

        int[][] playerData = new int[defeatedPlayers.size()+1][3];

        for(int i = 0; i < defeatedPlayers.size(); i++){

            Player player = defeatedPlayers.get(i);
            int[] data = new int[4];
            data[0] = player.getId();
            data[1] = player.getAvgWaitingTime();
            data[2] = player.getNumberOfRounds();
            data[3] = player.getTypeId();
            playerData[i] = data;

        }

        for(int i = 0; i < players.size(); i++){

            Player player = players.get(i);
            int[] data = new int[4];
            data[0] = player.getId();
            data[1] = player.getAvgWaitingTime();
            data[2] = player.getNumberOfRounds();
            data[3] = player.getTypeId();
            playerData[defeatedPlayers.size()+i] = data;

        }

        return playerData;
    }

    /**
     * Returns the AI stepping coordinates if the current Player is an AI or the game has not ended
     *
     * @return 	Integer[]     Automatic stepping coordinates of the current Player: [0] is the y coordinate, [1] is the x coordinate. If not available or game has finished, returns null
     */
    public Integer[] getAutoCoordinates(){

        if(isGameEnded()){
            return null;
        }

        else{
            return players.get(current_player_index).executeStep();
        }

    }

    /**
     * Reaction propagation depth getter method
     *
     * @return 	int     Reaction propagation depth
     */
    public int getReactionPropagationDepth(){
        return this.playground.getReactionPropagationDepth();
    }

    /**
     * Reaction propagation depth reset method
     */
    public void resetReactionPropagationDepth(){
        this.playground.resetReactionPropagationDepth();
    }

    /**
     * Returns whether the current game has AI and human Players too
     *
     * @return    boolean       True if AI and human Players also play, false otherwise
     */
    public boolean isAiVsHumanGame() {
        return this.is_ai_vs_human_game;
    }

}
