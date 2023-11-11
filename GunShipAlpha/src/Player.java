import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Player {
    private int health = 10;
    private JLabel endGame;

    public void reduceHealth(){
        System.out.println("reducing health");
        health--;
        isDead();
    }
    public boolean isDead(){
        boolean dead = health <= 0;
        return dead;
    }
    public void endGame(){

    }

}
