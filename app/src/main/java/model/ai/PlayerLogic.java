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
    private static int SUBMATRIX_LIMIT_ABOVE_CORNERS_PRIORISED = 14;

    /**
     * Number of the Player's Fields below the Player starts to escape
     */
    private static int NUMBER_OF_FIELDS_LIMIT_ESCAPING_BELOW = 2;

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
     * @param	playground_state	    [i] is Y coordinate, [][j] is X coordinate, [][][0] is the Id of the owner, [][][1] is the number of elements on the Field, [][][2] is the number of elements can be placed onto the Field before explosion
     * @param	player_Id_to_step_for	Player Id to calculate the step for
     * @return 	Integer[]               Global coordinates of the Field to step on: [0] is Y, [1] is X coordinate
     */
    public Integer[] CalculateStep(int[][][] playground_state, int player_Id_to_step_for) {

        int[] most_promising_field = new int[3];
        most_promising_field[0] = 1;
        most_promising_field[1] = 1;
        most_promising_field[2] = 100;

        int[] least_promising_field = new int[3];
        least_promising_field[0] = 1;
        least_promising_field[1] = 1;
        least_promising_field[2] = 0;

        int fieldsOfPlayer = 0;

        //playground pre-processing
        for(int actual_height = 1; actual_height < playground_state.length-1; actual_height++){

            for(int actual_width = 1; actual_width < playground_state[actual_width].length-1; actual_width++){

                int current_most_promising_value = 0;
                int current_least_promising_value = 100;
                int fields_clickable = 0;

                for(int i = actual_height-1; i <= actual_height+1; i++){

                    for(int j = actual_width-1; j <= actual_width+1; j++){

                        current_most_promising_value += playground_state[i][j][2];
                        current_least_promising_value += playground_state[i][j][2];

                        int current_owner_Id = playground_state[i][j][0];

                        if(current_owner_Id == 0 || current_owner_Id == player_Id_to_step_for){

                            fields_clickable++;

                        }

                        if(current_owner_Id == player_Id_to_step_for){

                            fieldsOfPlayer++;

                        }

                    }

                }

                if(current_most_promising_value < most_promising_field[2] && fields_clickable > 0){

                    most_promising_field[0] = actual_height;
                    most_promising_field[1] = actual_width;
                    most_promising_field[2] = current_most_promising_value;

                }

                if(current_least_promising_value > least_promising_field[2] && fields_clickable > 0){

                    least_promising_field[0] = actual_height;
                    least_promising_field[1] = actual_width;
                    least_promising_field[2] = current_least_promising_value;

                }

            }

        }

        Double[][] selected_matrix;
        Integer[] coordinates;

        //if limit reached, prioritise corner stepping
        if(most_promising_field[2] > SUBMATRIX_LIMIT_ABOVE_CORNERS_PRIORISED){

            coordinates = EmptyCornerStep(playground_state);

            if(coordinates != null){

                return coordinates;

            }

        }

        //if limit reached, step to the least promising sub-matrix
        if(fieldsOfPlayer < NUMBER_OF_FIELDS_LIMIT_ESCAPING_BELOW){

            selected_matrix = RetrieveSelectedSubMatrix(least_promising_field, playground_state, player_Id_to_step_for);

            //feeding the Neural Network with the selected 3x3 matrix
            coordinates = NeuralNetworkStep(selected_matrix);

            //translates the Neural Network selection coordinates to global coordinates
            coordinates[0] += least_promising_field[0]-1;
            coordinates[1] += least_promising_field[1]-1;

        }

        //step to the most promising sub-matrix
        else{

            selected_matrix = RetrieveSelectedSubMatrix(most_promising_field, playground_state, player_Id_to_step_for);

            //feeding the Neural Network with the selected 3x3 matrix
            coordinates = NeuralNetworkStep(selected_matrix);

            //translates the Neural Network selection coordinates to global coordinates
            coordinates[0] += most_promising_field[0]-1;
            coordinates[1] += most_promising_field[1]-1;

        }

        return coordinates;

    }

    /**
     * Feeds the Neural Network with the given 3x3 matrix
     *
     * @param	input_matrix    [i] is Y coordinate, [][j] is X coordinate, [i][j] is the double value: minus value means AI Player cannot step onto that Field; the bigger value is the better
     * @return 	Integer[]       Local coordinates of the Field to step on the input matrix: [0] is Y, [1] is X coordinate
     */
    private Integer[] NeuralNetworkStep(Double[][] input_matrix){

        Integer[] coordinates = new Integer[2];

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

                    selected_matrix[ i - (most_promising_field[0]-1) ][ j - (most_promising_field[1]-1) ] = ((-1) * (playground_state[i][j][1] * foreToken));

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
