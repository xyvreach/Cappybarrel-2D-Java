import java.awt.*;
import javax.swing.*;

public class Player {
    private float x, y; // Use float for position
    private float speed; // Use float for speed
    private ImageIcon sprite;

    public Player(float startX, float startY, float speed, ImageIcon sprite) {
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.sprite = sprite;
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(sprite.getImage(), (int) x, (int) y, panel); // Cast to int for drawing
    }

    // Movement methods
    public void moveUp() {
        y -= speed;
    }

    public void moveDown() {
        y += speed;
    }

    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    // Getter and setter for speed
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    // Getters for position
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
