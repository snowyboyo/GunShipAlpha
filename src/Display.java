import javax.swing.*;

public class Display {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    public static void main(String[] args) {
        JFrame frame = new JFrame("My Game App");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game canvas = new Game();
        frame.add(canvas);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        initializeGame(canvas);
    }

    public static void initializeGame(Game canvas) {
        canvas.spawnBehemoths(10);
    }
}

