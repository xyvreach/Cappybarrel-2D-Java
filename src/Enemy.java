import java.awt.*;
import javax.swing.*;

public class Enemy {
    private float x, y; // Position
    private float speed; // Speed
    private ImageIcon sprite;

    public Enemy(float startX, float startY, float speed, ImageIcon sprite) {
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.sprite = sprite;
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(sprite.getImage(), (int) x, (int) y, panel); // Cast to int for drawing
    }

    public void moveTowardsPlayer(float playerX, float playerY) {
        float deltaX = playerX - x;
        float deltaY = playerY - y;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (distance > 0) {
            x += (deltaX / distance) * speed;
            y += (deltaY / distance) * speed;
        }
    }

    // Getters for position
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
