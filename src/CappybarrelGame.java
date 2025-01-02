import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class CappybarrelGame {

    private static final String PLAYER1_IMAGE_PATH = "src\\assets\\images\\kennycapy_rifle.png";
    private static final String PLAYER2_IMAGE_PATH = "src\\assets\\images\\joeycapy_pistol.png";
    private static final String ENEMY_IMAGE_PATH = "src\\assets\\images\\enemy_1.png";

    private static ImageIcon kennyImage;
    private static ImageIcon joeyImage;

    private static Player player1;
    private static Player player2;
    private static Enemy enemy;

    private static int lastInputPlayer = 1; // 1 for Kenny, 2 for Joey

    private static boolean wPressed, aPressed, sPressed, dPressed;
    private static boolean upPressed, leftPressed, downPressed, rightPressed;

    public static void main(String[] args) {
        loadAssets();
        createStartMenu();
    }

    private static void loadAssets() {
        kennyImage = new ImageIcon(PLAYER1_IMAGE_PATH);
        joeyImage = new ImageIcon(PLAYER2_IMAGE_PATH);
    }

    private static void createStartMenu() {
        JFrame startMenu = createFrame("Cappybarrel - Start Menu", 400, 200, false);
        startMenu.setLayout(new GridLayout(2, 1));

        JButton singlePlayerButton = new JButton("Single Player");
        JButton multiPlayerButton = new JButton("Multiplayer");

        singlePlayerButton.addActionListener(e -> {
            startGame(true);
            startMenu.dispose();
        });

        multiPlayerButton.addActionListener(e -> {
            startGame(false);
            startMenu.dispose();
        });

        startMenu.add(singlePlayerButton);
        startMenu.add(multiPlayerButton);
        startMenu.setVisible(true);
    }

    private static void startGame(boolean isSinglePlayer) {
        JFrame gameWindow = createFrame("Cappybarrel Game", 800, 600, true);

        player1 = new Player(100, 100, 2.75f, kennyImage);
        if (!isSinglePlayer) {
            player2 = new Player(200, 100, 2.75f, joeyImage);
        }

        enemy = new Enemy(400, 300, 1.5f, new ImageIcon(ENEMY_IMAGE_PATH), 50, 50); // Adjust width and height as needed

        JPanel gamePanel = createGamePanel(isSinglePlayer);

        Timer timer = new Timer();
        addGameLoop(timer, gamePanel, isSinglePlayer);

        gameWindow.add(gamePanel);
        gameWindow.setVisible(true);
    }

    private static JPanel createGamePanel(boolean isSinglePlayer) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                player1.draw(g, this);

                if (!isSinglePlayer) {
                    if (lastInputPlayer == 1) {
                        player2.draw(g, this);
                    } else {
                        player1.draw(g, this);
                    }
                }

                enemy.draw(g, this);
            }
        };
    }

    private static void addGameLoop(Timer timer, JPanel gamePanel, boolean isSinglePlayer) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Update player movements
                updatePlayerMovement(player1, wPressed, aPressed, sPressed, dPressed);
                if (!isSinglePlayer) {
                    updatePlayerMovement(player2, upPressed, leftPressed, downPressed, rightPressed);
                }
    
                // Enemy moves toward the closest player
                enemy.moveTowardsClosestPlayer(isSinglePlayer ? player1 : player1, player2);
    
                // Repaint the game panel to reflect updates
                gamePanel.repaint();
            }
        }, 0, 16); // ~60 FPS
    }
    

    private static boolean updatePlayerMovement(Player player, boolean up, boolean left, boolean down, boolean right) {
        boolean moved = false;
        if (up) {
            player.moveUp();
            moved = true;
        }
        if (left) {
            player.moveLeft();
            moved = true;
        }
        if (down) {
            player.moveDown();
            moved = true;
        }
        if (right) {
            player.moveRight();
            moved = true;
        }
        return moved;
    }

    private static JFrame createFrame(String title, int width, int height, boolean isGameFrame) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        if (isGameFrame) {
            addKeyListeners(frame);
        }
        return frame;
    }

    private static void addKeyListeners(JFrame frame) {
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyReleased(e.getKeyCode());
            }
        });
    }

    private static void handleKeyPressed(int key) {
        switch (key) {
            case KeyEvent.VK_W -> wPressed = true;
            case KeyEvent.VK_A -> aPressed = true;
            case KeyEvent.VK_S -> sPressed = true;
            case KeyEvent.VK_D -> dPressed = true;
            case KeyEvent.VK_UP -> upPressed = true;
            case KeyEvent.VK_LEFT -> leftPressed = true;
            case KeyEvent.VK_DOWN -> downPressed = true;
            case KeyEvent.VK_RIGHT -> rightPressed = true;
        }
    }

    private static void handleKeyReleased(int key) {
        switch (key) {
            case KeyEvent.VK_W -> wPressed = false;
            case KeyEvent.VK_A -> aPressed = false;
            case KeyEvent.VK_S -> sPressed = false;
            case KeyEvent.VK_D -> dPressed = false;
            case KeyEvent.VK_UP -> upPressed = false;
            case KeyEvent.VK_LEFT -> leftPressed = false;
            case KeyEvent.VK_DOWN -> downPressed = false;
            case KeyEvent.VK_RIGHT -> rightPressed = false;
        }
    }
}
