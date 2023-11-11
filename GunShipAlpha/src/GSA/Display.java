package GSA;

import javax.swing.*;

public class Display {
    public static final int WIDTH = 1800;
    public static final int HEIGHT = 1000;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow gameWindow = new GameWindow("GunShipAlpha", WIDTH, HEIGHT);
        });
    }

}

