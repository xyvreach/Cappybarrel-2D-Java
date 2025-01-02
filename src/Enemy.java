import java.awt.*;
import javax.swing.*;

public class Enemy {
    private float x, y; // Position
    private float speed; // Speed
    private ImageIcon sprite;

    public Enemy(float startX, float startY, float speed, ImageIcon sprite, int width, int height) {
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.sprite = scaleImage(sprite, width, height);
    }

    private ImageIcon scaleImage(ImageIcon originalImage, int width, int height) {
        Image scaledImage = originalImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public void draw(Graphics g, JPanel panel) {
        g.drawImage(sprite.getImage(), (int) x, (int) y, panel); // Cast to int for drawing
    }


    public void moveTowardsClosestPlayer(Player... players) {
        Player closestPlayer = null;
        float shortestDistance = Float.MAX_VALUE;
    
        for (Player player : players) {
            if (player != null) { // Ensure the player exists (e.g., in single-player mode)
                float deltaX = player.getX() - x;
                float deltaY = player.getY() - y;
                float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    closestPlayer = player;
                }
            }
        }
    
        if (closestPlayer != null) {
            float deltaX = closestPlayer.getX() - x;
            float deltaY = closestPlayer.getY() - y;
            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            if (distance > 0) {
                x += (deltaX / distance) * speed;
                y += (deltaY / distance) * speed;
            }
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
