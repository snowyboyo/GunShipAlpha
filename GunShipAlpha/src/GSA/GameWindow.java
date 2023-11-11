package GSA;

import GSA.Game;

import javax.swing.*;
import java.awt.*;

public class GameWindow {
    private final JFrame frame;
    private final Game gameCanvas;
    private JButton restartButton;
    private JButton helpButton;

    public GameWindow(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height));
        gameCanvas = new Game();
        gameCanvas.setBounds(0, 0, width, height);
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setBounds(300, 20, 200, 50);
        restartButton.addActionListener(e -> gameCanvas.restartGame());
       helpButton = new JButton("?");
       helpButton.setFont(new Font("Arial", Font.BOLD, 20));
        helpButton.setBounds(500, 20, 40, 50);
        helpButton.addActionListener(e -> gameCanvas.startSplashScreenTimer());
        layeredPane.add(gameCanvas, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(restartButton, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(helpButton, JLayeredPane.PALETTE_LAYER);
        frame.add(layeredPane);
        frame.pack();
        frame.setMinimumSize(new Dimension(width, height));
        frame.setVisible(true);
    }
}