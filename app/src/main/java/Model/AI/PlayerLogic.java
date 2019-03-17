package Model.AI;

public class PlayerLogic {

    private static final int SUBMATRIX_LIMIT_ABOVE_CORNERS_PRIORISED = 10;

    public PlayerLogic(){



    }

    public Integer[] CalculateStep(int[][][] playground_state, int player_Id_to_step_for) {

        int[] most_promising_field = new int[3];
        most_promising_field[0] = 1;
        most_promising_field[1] = 1;
        most_promising_field[2] = 100;

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

        if(most_promising_field[2] > SUBMATRIX_LIMIT_ABOVE_CORNERS_PRIORISED){

            coordinates = EmptyCornerStep(playground_state);

            if(coordinates != null){

                return coordinates;

            }

        }

        Integer[][] selected_matrix = new Integer[3][3];

        for(int i = most_promising_field[0]-1; i <= most_promising_field[0]+1; i++){

            for(int j = most_promising_field[1]-1; j <= most_promising_field[1]+1; j++){

                selected_matrix[ i - (most_promising_field[0]-1) ][ j - (most_promising_field[1]-1) ] = playground_state[i][j][2];

            }

        }

        coordinates = NeuralNetworkStep(selected_matrix);

        coordinates[0] += most_promising_field[0]-1;
        coordinates[1] += most_promising_field[1]-1;

        return coordinates;

    }

    private Integer[] NeuralNetworkStep(Integer[][] input_matrix){

        Integer[] coordinates = new Integer[2];

        coordinates[0] = 1;
        coordinates[1] = 1;

        return coordinates;

    }

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
