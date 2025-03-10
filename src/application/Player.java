package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Player {
    private ImageView sprite;
    private Pane gamePane;
    private int score = 0;
    private boolean isDefeated = false;
    private int firingCooldownFrames; // Firing cooldown in frames
    private int framesSinceLastShot = 0; // Tracks frames since the last bullet was fired

    public Player(Pane gamePane, int firingRatePerSecond, int gameFPS) {
        this.gamePane = gamePane;
        this.firingCooldownFrames = gameFPS / firingRatePerSecond; // Calculate cooldown in frames
        initializeSprite();
        initializeEventHandlers();
    }

    private void initializeSprite() {
        Image playerImage = new Image(getClass().getResourceAsStream("/resources/player.png")); // Ensure the file exists
        sprite = new ImageView(playerImage);
        sprite.setFitWidth(32);
        sprite.setFitHeight(32);
        gamePane.getChildren().add(sprite);

        // Set initial position (center bottom of the screen)
        sprite.setX((gamePane.getWidth() - sprite.getFitWidth()) / 2);
        sprite.setY(gamePane.getHeight() - sprite.getFitHeight() - 50); // 50 pixels from the bottom
    }

    private void initializeEventHandlers() {
        gamePane.setOnMouseMoved(this::followMouse);
    }

    private void followMouse(MouseEvent e) {
        // Center the player sprite on the mouse cursor
        sprite.setX(e.getX() - sprite.getFitWidth() / 2);
        sprite.setY(e.getY() - sprite.getFitHeight() / 2);
    }

    public void update() {
        checkCollisions();
        handleFiring();
    }

    private void checkCollisions() {
        if (Monster.checkCollision(sprite) || BossBullet.checkCollision(sprite)) {
            isDefeated = true;
        }
    }

    private void handleFiring() {
        framesSinceLastShot++;

        // Fire a bullet if the cooldown has elapsed
        if (framesSinceLastShot >= firingCooldownFrames) {
            fireBullet();
            framesSinceLastShot = 0; // Reset the cooldown counter
        }
    }

    private void fireBullet() {
        new Bullet(gamePane, sprite.getX() + sprite.getFitWidth() / 2, sprite.getY(), this);
    }

    public void cleanup() {
        gamePane.setOnMouseMoved(null);
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean isDefeated) {
        this.isDefeated = isDefeated;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int points) {
        score += points;
    }

    public void resetScore() {
        score = 0;
    }

    public ImageView getSprite() {
        return sprite;
    }
}