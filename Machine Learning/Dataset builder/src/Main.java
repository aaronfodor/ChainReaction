import java.io.File;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args) {

        try {
            System.setOut(new PrintStream(new File("data.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        GUI gui = new GUI();
        gui.createAndShowGUI();

    }

}
