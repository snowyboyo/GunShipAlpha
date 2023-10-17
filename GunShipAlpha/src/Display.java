import javax.swing.*;

public class Display {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow gameWindow = new GameWindow("My Game App", WIDTH, HEIGHT);
        });
    }

}

