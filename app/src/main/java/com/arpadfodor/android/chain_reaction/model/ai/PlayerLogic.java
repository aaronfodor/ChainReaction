package com.arpadfodor.android.chain_reaction.model.ai;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * AI logic to control AI Players
 * Uses DeepLearning4J
 */
public class PlayerLogic {

    /**
     * DeepLearning4J Neural Network
     * Pre-built hu.bme.aut.android.chain_reaction.model
     * input layer: 9 neurons
     * output layer: 9 neurons
     */
    private static MultiLayerNetwork NeuralNetwork;

    /**
     * Goodness value of an empty corner
     */
    private static Double EMPTY_CORNER_WEIGHT = 1.0;

    /**
     * Sets the global Neural Network object to be available to the class instances
     *
     * @param	network	    The Neural Network object
     */
    public static void SetNeuralNetwork(MultiLayerNetwork network){
        if(NeuralNetwork == null){
            NeuralNetwork = network;
        }
    }

    /**
     * Calculates the next step based on the given inputs
     * Breaks the playground into 3x3 pieces to feed the Neural Network with
     * Does a valid step if the neural network output is invalid
     *
     * @param	playground_state	    [i] is Y coordinate, [][j] is X coordinate, [][][0] is the Id of the owner, [][][1] is the number of elements on the Field, [][][2] is the number of elements can be placed onto the Field before explosion
     * @param	player_Id_to_step_for	Player Id to calculate the step for
     * @return 	Integer[]               Global coordinates of the Field to step on: [0] is Y, [1] is X coordinate
     */
    public Integer[] CalculateStep(int[][][] playground_state, int player_Id_to_step_for) {

        Integer[] coordinates = new Integer[2];
        coordinates[0] = 0;
        coordinates[1] = 0;
        Double max_prediction = 0.0;

        Double[] emptyStep = EmptyCornerStep(playground_state);
        if(emptyStep[2] > max_prediction){
            coordinates[0] = (emptyStep[0].intValue());
            coordinates[1] = (emptyStep[1].intValue());
            max_prediction = emptyStep[2];
        }

        //playground pre-processing
        for(int actual_height = 1; actual_height < playground_state.length-1; actual_height++){

            for(int actual_width = 1; actual_width < playground_state[actual_height].length-1; actual_width++){

                int[] current_field = {actual_height, actual_width};
                Double[][] selected_matrix = RetrieveSelectedSubMatrix(current_field, playground_state, player_Id_to_step_for);
                //feeding the Neural Network with the selected 3x3 matrix
                Double[] result = NeuralNetworkStep(selected_matrix);
                result = translateToGlobalCoordinates(result, actual_height, actual_width);
                result = weightDecisionGlobally(result, playground_state);

                if(result[2] > max_prediction){
                    coordinates[0] = (result[0].intValue());
                    coordinates[1] = (result[1].intValue());
                    max_prediction = result[2];
                }

            }

        }

        int selected_field_owner_Id = playground_state[coordinates[0]][coordinates[1]][0];
        //Check whether the current player can step onto the selected field
        if(selected_field_owner_Id == 0 || selected_field_owner_Id == player_Id_to_step_for){
            return coordinates;
        }

        //Protection against wrong neural network output - when the current player cannot step onto the selected field, find a valid field to step on
        else{

            Integer[] backup_step = new Integer[2];
            for(int actual_height = 0; actual_height < playground_state.length; actual_height++){

                for(int actual_width = 0; actual_width < playground_state[actual_height].length; actual_width++){

                    int owner = playground_state[actual_height][actual_width][0];
                    if(owner == player_Id_to_step_for || owner == 0){
                        backup_step[0] = actual_height;
                        backup_step[1] = actual_width;
                    }

                }
            }

            return backup_step;

        }

    }

    /**
     * Translates the Neural Network selection coordinates to global coordinates
     *
     * @param	result          Local info of the Field to step on the input matrix: [0] is Y, [1] is X coordinate, [2] is the prediction value - 1 is the greatest
     * @param	height          Height of the centre of the sub-matrix
     * @param	width           Width of the centre of the sub-matrix
     * @return 	Double[]        Global info of the Field to step on the matrix: [0] is Y, [1] is X coordinate, [2] is the prediction value - 1 is the greatest
     */
    private Double[] translateToGlobalCoordinates(Double[] result, int height, int width) {
        result[0] = result[0] + (height - 1);
        result[1] = result[1] + (width - 1);
        return result;
    }

    /**
     * Weights the sureness of the selected step with the global goodness value of it
     * Only does weighting when the selected step leads to explosion
     *
     * @param  result                   Local info of the Field to step on the input matrix: [0] is Y, [1] is X coordinate, [2] is the prediction value - 1 is the greatest
     * @param	playground_state        [i] is Y coordinate, [][j] is X coordinate, [][][0] is the Id of the owner, [][][1] is the number of elements on the Field, [][][2] is the number of elements can be placed onto the Field before explosion
     * @return 	Double[]                Global weighted info of the Field to step on the matrix: [0] is Y, [1] is X coordinate, [2] is the weighted prediction value
     */
    private Double[] weightDecisionGlobally(Double[] result, int[][][] playground_state) {

        //if the selection leads to explosion
        if(playground_state[result[0].intValue()][result[1].intValue()][2] == 0){

            int player = playground_state[result[0].intValue()][result[1].intValue()][0];

            int counter = result[0].intValue();
            while(counter-1 >= 0){
                double neighbor = 1-(0.25*(playground_state[counter-1][result[1].intValue()][2]));
                if(player != 0 && neighbor == 1){
                    if(player != playground_state[counter-1][result[1].intValue()][0]){
                        result[2] += neighbor;
                    }
                }
                else{
                    if(playground_state[counter-1][result[1].intValue()][0] != 0 && playground_state[counter-1][result[1].intValue()][0] != player){
                        result[2] += neighbor;
                    }
                    break;
                }
                counter--;
            }

            counter = result[0].intValue();
            while(counter+1 < playground_state.length){
                double neighbor = 1-(0.25*(playground_state[counter+1][result[1].intValue()][2]));
                if(player != 0 && neighbor == 1){
                    if(player != playground_state[counter+1][result[1].intValue()][0]){
                        result[2] += neighbor;
                    }
                }
                else{
                    if(playground_state[counter+1][result[1].intValue()][0] != 0 && playground_state[counter+1][result[1].intValue()][0] != player){
                        result[2] += neighbor;
                    }
                    break;
                }
                counter++;
            }

            counter = result[1].intValue();
            while(counter-1 >= 0){
                double neighbor = 1-(0.25*(playground_state[result[0].intValue()][counter-1][2]));
                if(player != 0 && neighbor == 1){
                    if(player != playground_state[result[0].intValue()][counter-1][0]){
                        result[2] += neighbor;
                    }
                }
                else{
                    if(playground_state[result[0].intValue()][counter-1][0] != 0 && playground_state[result[0].intValue()][counter-1][0] != player){
                        result[2] += neighbor;
                    }
                    break;
                }
                counter--;
            }

            counter = result[1].intValue();
            while(counter+1 < playground_state[0].length){
                double neighbor = 1-(0.25*(playground_state[result[0].intValue()][counter+1][2]));
                if(player != 0 && neighbor == 1){
                    if(player != playground_state[result[0].intValue()][counter+1][0]){
                        result[2] += neighbor;
                    }
                }
                else{
                    if(playground_state[result[0].intValue()][counter+1][0] != 0 && playground_state[result[0].intValue()][counter+1][0] != player){
                        result[2] += neighbor;
                    }
                    break;
                }
                counter++;
            }

        }

        //the selection does not lead to explosion - do not weight the selection
        else{
            //result[2] *= 1-(0.25*(playground_state[result[0].intValue()][result[1].intValue()][2]));
        }

        return result;
    }

    /**
     * Feeds the Neural Network with the given 3x3 matrix
     *
     * @param	input_matrix    [i] is Y coordinate, [][j] is X coordinate, [i][j] is the double value: minus value means AI Player cannot step onto that Field; the bigger value is the better
     * @return 	Double[]        Local info of the Field to step on the input matrix: [0] is Y, [1] is X coordinate, [2] is the prediction value - 1 is the greatest
     */
    private Double[] NeuralNetworkStep(Double[][] input_matrix){

        Double[] result = new Double[3];
        INDArray actualInput = Nd4j.zeros(1,input_matrix.length * input_matrix[0].length);

        //building-up DeepLearning4J-specific input
        for(int i = 0; i < input_matrix.length; i++){

            for(int j = 0; j < input_matrix[i].length; j++){
                actualInput.putScalar(new int[]{0,(i*3) + j }, input_matrix[i][j]);
            }

        }

        //getting DeepLearning4J-specific output
        INDArray actualOutput = NeuralNetwork.output(actualInput);
        int maximum_prediction_index = 0;
        double maximum_prediction = 0.0;

        //evaluating the output - the greatest value is the one to step on
        for(int i = 0; i < actualOutput.columns(); i++){

            if(actualOutput.getDouble(i) > maximum_prediction && input_matrix[i/3][i%3] >= 0){
                maximum_prediction_index = i;
                maximum_prediction = actualOutput.getDouble(i);
            }

        }

        //translating output neuron index to local Field coordinates
        result[0] = (maximum_prediction_index / 3) + 0.0;
        result[1] = (maximum_prediction_index % 3) + 0.0;
        result[2] = maximum_prediction;
        return result;

    }

    /**
     * Tries to step onto an empty corner Field
     *
     * @param	playground_state	        [i] is Y coordinate, [][j] is X coordinate, [][][0] is the Id of the owner, [][][1] is the number of elements of the Field, [][][2] is the number of elements can be placed onto the Field before explosion
     * @return 	Double[]                    Global coordinates of the Field to step on: [0] is Y, [1] is X coordinate; null means there is no empty corners left
     */
    private Double[] EmptyCornerStep(int[][][] playground_state){

        Double[] emptyStep = new Double[3];
        emptyStep[0] = 0.0;
        emptyStep[1] = 0.0;
        emptyStep[2] = 0.0;

        if(playground_state[0][0][1] == 0){
            emptyStep[0] = 0.0;
            emptyStep[1] = 0.0;
            emptyStep[2] = EMPTY_CORNER_WEIGHT;
            return emptyStep;
        }

        if(playground_state[playground_state.length-1][0][1] == 0){
            emptyStep[0] = playground_state.length-1.0;
            emptyStep[1] = 0.0;
            emptyStep[2] = EMPTY_CORNER_WEIGHT;
            return emptyStep;
        }

        if(playground_state[0][playground_state[0].length-1][1] == 0){
            emptyStep[0] = 0.0;
            emptyStep[1] = playground_state[0].length-1.0;
            emptyStep[2] = EMPTY_CORNER_WEIGHT;
            return emptyStep;
        }

        if(playground_state[playground_state.length-1][playground_state[playground_state.length-1].length-1][1] == 0){
            emptyStep[0] = playground_state.length-1.0;
            emptyStep[1] = playground_state[playground_state.length-1].length-1.0;
            emptyStep[2] = EMPTY_CORNER_WEIGHT;
            return emptyStep;
        }

        return emptyStep;

    }

    /**
     * Retrieves the selected sub-matrix
     *
     * @param	most_promising_field	Coordinates of the most promising Field, which is the centre of the sub-matrix: [0] is Y coordinate, [1] is X coordinate
     * @param	playground_state	    [i] is Y coordinate, [][j] is X coordinate, [][][0] is the Id of the owner, [][][1] is the number of elements of the Field, [][][2] is the number of elements can be placed onto the Field before explosion
     * @param	player_Id_to_step_for	Id of the Player to step for
     * @return 	Double[][]              The selected 3x3 sub-matrix: [i] is Y coordinate, [][j] is X coordinate, [i][j] is the double value: minus value means AI Player cannot step onto that Field; the bigger value is the better
     */
    private Double[][] RetrieveSelectedSubMatrix(int[] most_promising_field, int[][][] playground_state, int player_Id_to_step_for){

        Double[][] selected_matrix = new Double[3][3];
        //normalization multiplier for neural network
        double foreToken = 0.25;

        //selection of the 3x3 sub-matrix
        for(int i = most_promising_field[0]-1; i <= most_promising_field[0]+1; i++){

            for(int j = most_promising_field[1]-1; j <= most_promising_field[1]+1; j++){

                //the AI Player can step onto the Field
                if(playground_state[i][j][0] == player_Id_to_step_for || playground_state[i][j][0] == 0){
                    selected_matrix[ i - (most_promising_field[0]-1) ][ j - (most_promising_field[1]-1) ] = (1 - (playground_state[i][j][2] * foreToken));
                }
                //AI Player cannot step onto the Field
                else{
                    selected_matrix[ i - (most_promising_field[0]-1) ][ j - (most_promising_field[1]-1) ] = ((-1) + (playground_state[i][j][2] * foreToken));
                }

            }

        }

        return selected_matrix;

    }

    /**
     * Neural Network state checker
     *
     * @return 	Boolean     True if loaded, false if not
     */
    public static Boolean isNeuralNetworkLoaded(){
        return NeuralNetwork != null;
    }

}