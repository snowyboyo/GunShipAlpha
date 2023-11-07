package GSA;

import GSA.Game;

import javax.swing.*;
import java.awt.*;

public class GameWindow {
    private final JFrame frame;
    private final Game gameCanvas;

    public GameWindow(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        gameCanvas = new Game();
        gameCanvas.setPreferredSize(new Dimension(width, height));
        frame.add(gameCanvas);
        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setVisible(true);
    }
}