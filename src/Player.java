import java.awt.*;
import javax.swing.*;

public class Player {
    private float x, y; // Use float for position
    private float speed; // Use float for speed
    private ImageIcon sprite;
    private boolean showHitbox = false; // Toggle for hitbox

    public Player(float startX, float startY, float speed, ImageIcon sprite) {
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.sprite = sprite;
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(sprite.getImage(), (int) x, (int) y, panel); // Cast to int for drawing

        if (showHitbox) {
            g.setColor(Color.RED);
            g.drawRect((int) x, (int) y, sprite.getIconWidth(), sprite.getIconHeight());
        }
    }

    // Movement methods
    public void moveUp() { y -= speed; }
    public void moveDown() { y += speed; }
    public void moveLeft() { x -= speed; }
    public void moveRight() { x += speed; }

    // Toggle hitbox visibility
    public void toggleHitbox() { showHitbox = !showHitbox; }

    // Getters for position
    public float getX() { return x; }
    public float getY() { return y; }
}
