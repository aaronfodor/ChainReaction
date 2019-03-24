package model.ai;

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
     * Pre-built model
     * input layer: 9 neurons
     * output layer: 9 neurons
     */
    private static MultiLayerNetwork NeuralNetwork;

    /**
     * Value above corner acquisition is prioritised
     */
    private static int SUBMATRIX_LIMIT_ABOVE_CORNERS_PRIORISED = 16;

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
     * If limit reached, prioritises corner stepping
     * Does playground pre-processing to evaluate which 3x3 part is worth to step on
     * Breaks the playground into 3x3 piece to feed the Neural Network with
     *
     * @param	playground_state	    [i] is Y coordinate, [][j] is X coordinate, [][][0] is the Id of the owner, [][][1] is the number of elements of the Field, [][][2] is the number of elements can be placed onto the Field before explosion
     * @param	player_Id_to_step_for	Player Id to calculate the step for
     * @return 	Integer[]               Global coordinates of the Field to step on: [0] is Y, [1] is X coordinate
     */
    public Integer[] CalculateStep(int[][][] playground_state, int player_Id_to_step_for) {

        int[] most_promising_field = new int[3];
        most_promising_field[0] = 1;
        most_promising_field[1] = 1;
        most_promising_field[2] = 100;

        //playground pre-processing
        for(int actual_height = 1; actual_height < playground_state.length-1; actual_height++){

            for(int actual_width = 1; actual_width < playground_state[actual_width].length-1; actual_width++){

                int current_most_promising_value = 0;
                int fields_clickable = 0;

                for(int i = actual_height-1; i <= actual_height+1; i++){

                    for(int j = actual_width-1; j <= actual_width+1; j++){

                        current_most_promising_value += playground_state[i][j][2];

                        int current_owner_Id = playground_state[i][j][0];

                        if(current_owner_Id == 0 || current_owner_Id == player_Id_to_step_for){

                            fields_clickable++;

                        }

                    }

                }

                if(current_most_promising_value < most_promising_field[2] && fields_clickable > 0){

                    most_promising_field[0] = actual_height;
                    most_promising_field[1] = actual_width;
                    most_promising_field[2] = current_most_promising_value;

                }

            }

        }

        Integer[] coordinates;

        //if limit reached, prioritise corner stepping
        if(most_promising_field[2] > SUBMATRIX_LIMIT_ABOVE_CORNERS_PRIORISED){

            coordinates = EmptyCornerStep(playground_state);

            if(coordinates != null){

                return coordinates;

            }

        }

        Integer[][][] selected_matrix = new Integer[3][3][2];

        //selection of the 3x3 sub-matrix
        for(int i = most_promising_field[0]-1; i <= most_promising_field[0]+1; i++){

            for(int j = most_promising_field[1]-1; j <= most_promising_field[1]+1; j++){

                Integer stepable = 0;

                if(playground_state[i][j][0] == player_Id_to_step_for || playground_state[i][j][0] == 0){

                    stepable = 1;

                }

                selected_matrix[ i - (most_promising_field[0]-1) ][ j - (most_promising_field[1]-1) ][0] = playground_state[i][j][2];
                selected_matrix[ i - (most_promising_field[0]-1) ][ j - (most_promising_field[1]-1) ][1] = stepable;

            }

        }

        //feeding the Neural Network with the selected 3x3 matrix
        coordinates = NeuralNetworkStep(selected_matrix);

        //translates the Neural Network selection coordinates to global coordinates
        coordinates[0] += most_promising_field[0]-1;
        coordinates[1] += most_promising_field[1]-1;

        return coordinates;

    }

    /**
     * Feeds the Neural Network with the given 3x3 matrix
     *
     * @param	input_matrix    [i] is Y coordinate, [][j] is X coordinate, [][][0] is the Id of the owner, [][][1] is the number of elements of the Field, [][][2] is the number of elements can be placed onto the Field before explosion
     * @return 	Integer[]       Local coordinates of the Field to step on the input matrix: [0] is Y, [1] is X coordinate
     */
    private Integer[] NeuralNetworkStep(Integer[][][] input_matrix){

        Integer[] coordinates = new Integer[2];

        INDArray actualInput = Nd4j.zeros(1,input_matrix.length * input_matrix[0].length);

        //building-up DeepLearning4J-specific input
        for(int i = 0; i < input_matrix.length; i++){

            for(int j = 0; j < input_matrix[i].length; j++){

                actualInput.putScalar(new int[]{0,(i*3) + j }, input_matrix[i][j][0].doubleValue() * 0.25);

            }

        }

        //getting DeepLearning4J-specific output
        INDArray actualOutput = NeuralNetwork.output(actualInput);

        Integer maximum_prediction_index = 0;
        Double maximum_prediction = 0.0;

        //evaluating the output - the greatest value is the one to step on
        for(int i = 0; i < actualOutput.columns(); i++){

            if(actualOutput.getDouble(i) > maximum_prediction && input_matrix[i/3][i%3][1] == 1){

                maximum_prediction_index = i;

            }

        }

        //translating output neuron index to local Field coordinates
        coordinates[0] = maximum_prediction_index / 3;
        coordinates[1] = maximum_prediction_index % 3;

        return coordinates;

    }

    /**
     * Tries to step onto an empty corner Field
     *
     * @param	playground_state	    [i] is Y coordinate, [][j] is X coordinate, [][][0] is the Id of the owner, [][][1] is the number of elements of the Field, [][][2] is the number of elements can be placed onto the Field before explosion
     * @return 	Integer[]               Global coordinates of the Field to step on: [0] is Y, [1] is X coordinate; null means there is no empty corners left
     */
    private Integer[] EmptyCornerStep(int[][][] playground_state){

        if(playground_state[0][0][1] == 0){

            Integer[] coordinates = new Integer[2];
            coordinates[0] = 0;
            coordinates[1] = 0;
            return coordinates;

        }

        if(playground_state[playground_state.length-1][0][1] == 0){

            Integer[] coordinates = new Integer[2];
            coordinates[0] = playground_state.length-1;
            coordinates[1] = 0;
            return coordinates;

        }

        if(playground_state[0][playground_state[0].length-1][1] == 0){

            Integer[] coordinates = new Integer[2];
            coordinates[0] = 0;
            coordinates[1] = playground_state[0].length-1;
            return coordinates;

        }

        if(playground_state[playground_state.length-1][playground_state[playground_state.length-1].length-1][1] == 0){

            Integer[] coordinates = new Integer[2];
            coordinates[0] = playground_state.length-1;
            coordinates[1] = playground_state[playground_state.length-1].length-1;
            return coordinates;

        }

        return null;

    }

}
