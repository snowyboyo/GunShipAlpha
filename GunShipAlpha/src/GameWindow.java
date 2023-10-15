import javax.swing.*;

public class GameWindow {
    private final JFrame frame;
    private final Game gameCanvas;

    public GameWindow(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        gameCanvas = new Game();
        frame.add(gameCanvas);
        frame.setVisible(true);
    }

    public Game getGameCanvas() {
        return gameCanvas;
    }
}