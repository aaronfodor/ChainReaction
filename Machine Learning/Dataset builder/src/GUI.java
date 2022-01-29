import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GUI {

    JFrame frame;
    GridLayout grid;
    JButton[][] buttons = new JButton[3][3];
    int[][] matrix = new int[3][3];

    Image nothing;
    Image red_dot1;
    Image red_dot2;
    Image red_dot3;
    Image blue_dot1;
    Image blue_dot2;
    Image blue_dot3;

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() {

        //Create and set up the window.
        frame = new JFrame("Chain Reaction Teacher Data Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            nothing = ImageIO.read(getClass().getResource("resources/nothing.png"));
            red_dot1 = ImageIO.read(getClass().getResource("resources/red_dot1.png"));
            red_dot2 = ImageIO.read(getClass().getResource("resources/red_dot2.png"));
            red_dot3 = ImageIO.read(getClass().getResource("resources/red_dot3.png"));
            blue_dot1 = ImageIO.read(getClass().getResource("resources/blue_dot1.png"));
            blue_dot2 = ImageIO.read(getClass().getResource("resources/blue_dot2.png"));
            blue_dot3 = ImageIO.read(getClass().getResource("resources/blue_dot3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        grid = new GridLayout(3,3);
        frame.setLayout(grid);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                JButton button = new JButton(new ImageIcon(nothing));
                button.setName(String.valueOf((3*i)+j));
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {

                        //left mouse click
                        if(e.getButton() == MouseEvent.BUTTON1) {
                            saveChoice(Integer.valueOf(((JButton)e.getSource()).getName()));
                        }

                        //right mouse click
                        if(e.getButton() == MouseEvent.BUTTON3) {
                            int matrix_idx = Integer.valueOf(((JButton)e.getSource()).getName());

                            int current_value = matrix[matrix_idx/3][matrix_idx%3];

                            if(current_value >= 3){
                                current_value = -3;
                            }
                            else{
                                current_value++;
                            }

                            matrix[matrix_idx/3][matrix_idx%3] = current_value;
                            modifyGUI(matrix);

                        }

                    }
                });
                buttons[i][j] = button;
                frame.add(buttons[i][j]);
            }
        }

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void modifyGUI(int[][] matrix){

        this.matrix = matrix;

        for(int i = 0; i < buttons.length; i++){

            for(int j = 0; j < buttons[i].length; j++){

                Image image;
                int image_idx = this.matrix[i][j];

                switch (image_idx){
                    case -3: image = blue_dot3; break;
                    case -2: image = blue_dot2; break;
                    case -1: image = blue_dot1; break;
                    case 0: image = nothing; break;
                    case 1: image = red_dot1; break;
                    case 2: image = red_dot2; break;
                    case 3: image = red_dot3; break;
                    default: image = nothing; break;
                }

                buttons[i][j].setIcon(new ImageIcon(image));

            }

        }

        frame.revalidate();

    }

    public void saveChoice(int index){

        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){

                double current_value = matrix[i][j];

                if(current_value >= 0){
                    current_value *= 0.25;
                    current_value += 0.25;
                }

                else{
                    current_value *= 0.25;
                    current_value -= 0.25;
                }

                System.out.print(current_value + "\t");
            }
        }

        System.out.print(">\t");

        for(int i = 0; i < 9; i++){

            if(i == index){
                System.out.print("1\t");
            }

            else{
                System.out.print("0\t");
            }

        }

        System.out.println();

    }

}
