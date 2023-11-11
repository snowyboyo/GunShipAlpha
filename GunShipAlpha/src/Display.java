public class Display {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    public static void main(String[] args) {
        GameWindow gameWindow = new GameWindow("My Game App", WIDTH, HEIGHT);
        gameWindow.display();
        int wave = 1;
        initializeGame(gameWindow.getGameCanvas());
    }

    public static void initializeGame(Game gameCanvas) {
        gameCanvas.spawnEnemies(5);
    }
}

