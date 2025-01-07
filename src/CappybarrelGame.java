import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class CappybarrelGame {

    /* ------------------------------------------------------
     *                   ASSET PATH CONSTANTS
     * ------------------------------------------------------ */
    private static final String PLAYER1_IMAGE_PATH = "src\\assets\\images\\kennycapy_hitbox.png";
    private static final String PLAYER2_IMAGE_PATH = "src\\assets\\images\\joeycapy_pistol.png";
    private static final String ENEMY_IMAGE_PATH   = "src\\assets\\images\\enemy_1.png";

    /* ------------------------------------------------------
     *                 GAME OBJECT REFERENCES
     * ------------------------------------------------------ */
    private static ImageIcon kennyImage;
    private static ImageIcon joeyImage;

    private static Player player1;
    private static Player player2;
    private static Enemy enemy;

    // Tracks whose input was last recognized (not fully utilized yet, but available for expansions).
    private static int lastInputPlayer = 1; // 1 for Kenny, 2 for Joey

    /* ------------------------------------------------------
     *                 KEY PRESS TRACKERS
     * ------------------------------------------------------ */
    // Player 1 controls (WASD)
    private static boolean wPressed;
    private static boolean aPressed;
    private static boolean sPressed;
    private static boolean dPressed;

    // Player 2 controls (arrow keys)
    private static boolean upPressed;
    private static boolean leftPressed;
    private static boolean downPressed;
    private static boolean rightPressed;

    /* ------------------------------------------------------
     *                       MAIN METHOD
     * ------------------------------------------------------ */
    public static void main(String[] args) {
        loadAssets();
        createStartMenu();
    }

    /* ------------------------------------------------------
     *                       ASSET LOADING
     * ------------------------------------------------------ */
    /**
     * Loads image assets for the players.
     */
    private static void loadAssets() {
        kennyImage = new ImageIcon(PLAYER1_IMAGE_PATH);
        joeyImage  = new ImageIcon(PLAYER2_IMAGE_PATH);
    }

    /* ------------------------------------------------------
     *                START MENU CREATION
     * ------------------------------------------------------ */
    /**
     * Creates the start menu frame with Single Player and Multiplayer options.
     */
    private static void createStartMenu() {
        JFrame startMenu = createFrame("Cappybarrel - Start Menu", 400, 200, false);
        startMenu.setLayout(new GridLayout(2, 1));

        JButton singlePlayerButton = new JButton("Single Player");
        JButton multiPlayerButton  = new JButton("Multiplayer");

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

    /* ------------------------------------------------------
     *                     GAME SETUP
     * ------------------------------------------------------ */
    /**
     * Initializes the game window, players, and enemies. Starts the game loop.
     *
     * @param isSinglePlayer true if single-player mode, false if multiplayer
     */
    private static void startGame(boolean isSinglePlayer) {
        // Create the main game window
        JFrame gameWindow = createFrame("Cappybarrel Game", 800, 600, true);

        // Create players
        player1 = new Player(100, 100, 2.75f, kennyImage);
        if (!isSinglePlayer) {
            player2 = new Player(200, 100, 2.75f, joeyImage);
        }

        // Create enemy
        enemy = new Enemy(400, 300, 1.5f, new ImageIcon(ENEMY_IMAGE_PATH), 50, 50);

        // Create a custom JPanel to draw game objects
        JPanel gamePanel = createGamePanel(isSinglePlayer);

        // Add the game loop (timer) to handle updates
        Timer timer = new Timer();
        addGameLoop(timer, gamePanel, isSinglePlayer);

        // Add the game panel to the window and display it
        gameWindow.add(gamePanel);
        gameWindow.setVisible(true);
    }

    /**
     * Creates the main game panel responsible for all painting.
     *
     * @param isSinglePlayer Flag to indicate if only one player is present
     * @return A JPanel with a custom paintComponent method
     */
    private static JPanel createGamePanel(boolean isSinglePlayer) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw Player 1
                player1.draw(g, this);

                // Draw Player 2 (if not single-player)
                if (!isSinglePlayer) {
                    if (lastInputPlayer == 1) {
                        player2.draw(g, this);
                    } else {
                        player1.draw(g, this);
                    }
                }

                // Draw Enemy
                enemy.draw(g, this);
            }
        };
    }

    /* ------------------------------------------------------
     *                    GAME LOOP (TIMER)
     * ------------------------------------------------------ */
    /**
     * Schedules a repeating task to update game state and repaint the panel.
     *
     * @param timer          The Timer object used for scheduling
     * @param gamePanel      The JPanel to be repainted
     * @param isSinglePlayer Whether the game is in single-player mode
     */
    private static void addGameLoop(Timer timer, JPanel gamePanel, boolean isSinglePlayer) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Update player movements for Player 1
                updatePlayerMovement(player1, wPressed, aPressed, sPressed, dPressed);

                // Update player movements for Player 2 if in multiplayer mode
                if (!isSinglePlayer) {
                    updatePlayerMovement(player2, upPressed, leftPressed, downPressed, rightPressed);
                }

                // Enemy moves toward the closest player
                enemy.moveTowardsClosestPlayer(
                    isSinglePlayer ? player1 : player1, // if single player, chase player1 only
                    player2
                );

                // Repaint the game panel to reflect updates
                gamePanel.repaint();
            }
        }, 0, 16); // ~60 FPS
    }

    /* ------------------------------------------------------
     *               PLAYER MOVEMENT UPDATING
     * ------------------------------------------------------ */
    /**
     * Updates a player’s movement based on which keys are pressed.
     *
     * @param player The player to move
     * @param up     Whether the 'up' key is pressed
     * @param left   Whether the 'left' key is pressed
     * @param down   Whether the 'down' key is pressed
     * @param right  Whether the 'right' key is pressed
     * @return true if any movement occurred, false otherwise
     */
    private static boolean updatePlayerMovement(Player player, boolean up, boolean left,
                                                boolean down, boolean right) {
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

    /* ------------------------------------------------------
     *                 FRAME AND KEY LISTENERS
     * ------------------------------------------------------ */
    /**
     * Creates a JFrame with specified properties.
     *
     * @param title      The title of the JFrame
     * @param width      The width of the JFrame
     * @param height     The height of the JFrame
     * @param isGameFrame If true, adds key listeners for gameplay
     * @return The created JFrame
     */
    private static JFrame createFrame(String title, int width, int height, boolean isGameFrame) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        if (isGameFrame) {
            addKeyListeners(frame);
        }
        return frame;
    }

    /**
     * Adds key listeners to a frame to handle keyboard input for players.
     *
     * @param frame The JFrame to which key listeners will be added
     */
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

    /* ------------------------------------------------------
     *                 KEY PRESSED/RELEASED
     * ------------------------------------------------------ */
    /**
     * Handles actions when a key is pressed.
     *
     * @param key The key code
     */
    private static void handleKeyPressed(int key) {
        switch (key) {
            // Player 1 Controls (WASD)
            case KeyEvent.VK_W -> wPressed = true;
            case KeyEvent.VK_A -> aPressed = true;
            case KeyEvent.VK_S -> sPressed = true;
            case KeyEvent.VK_D -> dPressed = true;
            case KeyEvent.VK_H -> playSound("src\\assets\\sounds\\pistol_shot.wav");

            // Player 2 Controls (Arrow Keys)
            case KeyEvent.VK_UP -> upPressed = true;
            case KeyEvent.VK_LEFT -> leftPressed = true;
            case KeyEvent.VK_DOWN -> downPressed = true;
            case KeyEvent.VK_RIGHT -> rightPressed = true;
            // Play a pistol shot sound (only if player2 is not null)
            case KeyEvent.VK_ENTER -> {
                if (player2 != null) {
                    playSound("src\\assets\\sounds\\pistol_shot.wav");
                }
            }
            // Toggle Player 1 hitbox
            case KeyEvent.VK_R -> player1.toggleHitbox();
            // Toggle Enemy hitbox
            case KeyEvent.VK_E -> enemy.toggleHitbox();
            // Toggle Player 2 hitbox
            case KeyEvent.VK_T -> {
                if (player2 != null) {
                    player2.toggleHitbox();
                }
            }
        }
    }

    /**
     * Handles actions when a key is released.
     *
     * @param key The key code
     */
    private static void handleKeyReleased(int key) {
        switch (key) {
            // Player 1 Controls (WASD)
            case KeyEvent.VK_W -> wPressed = false;
            case KeyEvent.VK_A -> aPressed = false;
            case KeyEvent.VK_S -> sPressed = false;
            case KeyEvent.VK_D -> dPressed = false;

            // Player 2 Controls (Arrow Keys)
            case KeyEvent.VK_UP -> upPressed = false;
            case KeyEvent.VK_LEFT -> leftPressed = false;
            case KeyEvent.VK_DOWN -> downPressed = false;
            case KeyEvent.VK_RIGHT -> rightPressed = false;
        }
    }

    /* ------------------------------------------------------
     *                        SOUNDS
     * ------------------------------------------------------ */
    /**
     * Plays a sound from a given file path.
     *
     * @param soundFilePath The path to the sound file
     */
    private static void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
