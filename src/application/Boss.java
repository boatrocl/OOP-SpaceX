package application;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Boss {
    private ImageView sprite;
    private Pane gamePane;
    private boolean movingRight = true;
    private Random random = new Random();
    private int hp = 15;
    private int movementSpeed = 2; // Boss movement speed in pixels per frame

    private AnimationTimer movementTimer;
    private int framesSinceLastShot = 0; // Tracks frames since the last bullet was fired
    private int firingCooldownFrames; // Cooldown in frames for boss firing

    // UI
    private ProgressBar hpBar;
    private Text hpText;

    public Boss(Pane gamePane, int firingRatePerSecond, int gameFPS) {
        this.gamePane = gamePane;
        this.firingCooldownFrames = gameFPS / firingRatePerSecond; // Calculate cooldown in frames
        initializeSprite();
        initializeHpBar();
        startBossBehavior();
    }

    private void initializeSprite() {
        Image bossImage = new Image(getClass().getResourceAsStream("/resources/boss.png")); // Ensure the file exists
        sprite = new ImageView(bossImage);
        sprite.setFitWidth(128);
        sprite.setFitHeight(128);
        sprite.setX(gamePane.getWidth() / 2 - sprite.getFitWidth() / 2);
        sprite.setY(20);
        gamePane.getChildren().add(sprite);
    }

    private void initializeHpBar() {
        hpBar = new ProgressBar(1.0);
        hpBar.setPrefWidth(200);
        hpBar.setStyle("-fx-accent: red;");

        hpText = new Text("Boss HP: 15");
        hpText.setStyle("-fx-font-size: 16; -fx-fill: white");

        HBox hpContainer = new HBox(10);
        hpContainer.getChildren().addAll(new Text("Boss"), hpBar, hpText);
        hpContainer.setLayoutX(250);
        hpContainer.setLayoutY(10);
        gamePane.getChildren().add(hpContainer);
    }

    private void startBossBehavior() {
        // Move boss left and right
        movementTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (movingRight) {
                    sprite.setX(sprite.getX() + movementSpeed);
                    if (sprite.getX() + sprite.getFitWidth() > gamePane.getWidth()) {
                        movingRight = false;
                    }
                } else {
                    sprite.setX(sprite.getX() - movementSpeed);
                    if (sprite.getX() < 0) {
                        movingRight = true;
                    }
                }
            }
        };
        movementTimer.start();
    }

    public void update() {
        handleFiring();
    }

    private void handleFiring() {
        framesSinceLastShot++;

        // Fire a bullet if the cooldown has elapsed
        if (framesSinceLastShot >= firingCooldownFrames) {
            shootBullet();
            framesSinceLastShot = 0; // Reset the cooldown counter
        }
    }

    private void shootBullet() {
        new BossBullet(gamePane, sprite.getX() + sprite.getFitWidth() / 2, sprite.getY() + sprite.getFitHeight());
    }

    public void takeDamage(int damage) {
		hp -= damage;
		if(this.hp <= 0) {
			hp = 0;
		}
		System.out.println("Boss HP: " + hp);
		hpBar.setProgress((double) hp / 15);
		hpText.setText("Boss HP: " + hp);
	}

    private void triggerVictory() {
        // Transition to a victory screen or trigger a win state
        // Example: gamePane.getChildren().clear(); // Clear the game pane
        // Example: gamePane.getChildren().add(new Text("You Win!")); // Display victory message
    }

    public ImageView getSprite() {
        return sprite;
    }

    public int getHp() {
        return hp;
    }

    public void CloseTimer() {
        if (movementTimer != null) {
            movementTimer.stop();
        }
    }
}