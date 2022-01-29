public class DataGenerator {

    public DataGenerator(GUI gui){

        int[][] matrix = new int[3][3];

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                matrix[i][j] = -3;
            }
        }

        int num = 0;

        for(int i = 0; i < 9; i++){
            for(int state = -3; state <= 3; state++){

                matrix[i/3][i%3] = state;

                for(int j = 0; j <= i; j++){
                    for(int inner_state = -3; inner_state <= state; inner_state++){

                        matrix[j/3][j%3] = inner_state;
                        gui.modifyGUI(matrix);
                        gui.saveChoice(0);
                        try {
                            Thread.sleep(50);
                            num++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }

        System.out.println(num);

    }

}
