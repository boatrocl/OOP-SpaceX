package application;

import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game {
	private MediaPlayer mediaPlayer;
    private Stage primaryStage;
    private Pane gamePane;
    private Player player;
    private Boss boss;
    private boolean isBossRushMode;
    private double monsterSpawnRate = 5.0;
    private Text scoreText;
    private Text fpsText; // Text node to display FPS
    private StarBackground starBackground;

    private AnimationTimer gameLoop;
    private int framesSinceLastSpawn = 0; // Tracks frames since the last monster was spawned
    private int spawnCooldownFrames; // Cooldown in frames for monster spawning

    private static final int GAME_FPS = 60; // Game runs at 60 FPS
    private static final long FRAME_DURATION = 15_000_000; // 16.67 milliseconds in nanoseconds
    private long lastUpdateTime = 0; // Tracks the last time the game was updated

    // FPS tracking variables
    private int frameCount = 0; // Tracks the number of frames rendered
    private long lastFpsUpdateTime = 0; // Tracks the last time FPS was updated

    public Game(Stage primaryStage, boolean isBossRushMode) {
        this.primaryStage = primaryStage;
        this.isBossRushMode = isBossRushMode;
        this.spawnCooldownFrames = (int) (GAME_FPS / monsterSpawnRate); // Calculate cooldown in frames
    }

    public void startGame() {
        playBackgroundMusic();
        initializeGameScene();
        setupPlayer();
        setupScoreText();
        setupFpsText(); // Initialize FPS text
        setupBoss();
        startGameLoop();
    }
    
    private void playBackgroundMusic() {
        try {
            String musicFile = getClass().getResource("/resources/Night_of_Nights_Game.wav").toExternalForm();
            Media sound = new Media(musicFile);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Error Sound");
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private void initializeGameScene() {
        gamePane = new Pane();
        gamePane.getChildren().clear();

        Monster.clearMonsters();
        Bullet.clearBullets();
        BossBullet.clearBullets();

        if (player != null) {
            player.cleanup();
        }

        Scene gameScene = new Scene(gamePane, 800, 600);
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Space Invaders - " + (isBossRushMode ? "Boss Rush Mode" : "Classic Mode"));

        gamePane.setStyle("-fx-background-color: black");
        starBackground = new StarBackground(gamePane);
        gameScene.setCursor(Cursor.NONE);
    }

    private void setupPlayer() {
        int firingRatePerSecond = 5; // Player fires 5 bullets per second
        player = new Player(gamePane, firingRatePerSecond, GAME_FPS);
        System.out.println("Player isDefeated: " + player.isDefeated());
    }

    private void setupScoreText() {
        scoreText = new Text(10, 20, "Score: 0");
        scoreText.setStyle("-fx-font-size: 20; -fx-fill: white;");
        gamePane.getChildren().add(scoreText);
    }

    private void setupFpsText() {
        fpsText = new Text(700, 20, "FPS: 0");
        fpsText.setStyle("-fx-font-size: 20; -fx-fill: white;");
        gamePane.getChildren().add(fpsText);
    }

    private void setupBoss() {
        if (isBossRushMode) {
            int bossFiringRatePerSecond = 2; // Boss fires 2 bullets per second
            boss = new Boss(gamePane, bossFiringRatePerSecond, GAME_FPS);
        }
    }

    private void startGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Calculate elapsed time since the last update
                long elapsedTime = now - lastUpdateTime;

                // Only update the game if enough time has passed for the next frame
                if (elapsedTime >= FRAME_DURATION) {
                    update();
                    lastUpdateTime = now; // Update the last update time

                    // Update FPS counter
                    updateFps(now);
                }
            }
        };
        gameLoop.start();
        System.out.println("Game loop start");
    }

    public void update() {
        player.update();
        starBackground.moveStars();

        if (isBossRushMode) {
            boss.update(); // Update boss behavior (movement and firing)
            BossBullet.updateAll(gamePane, player);
            Monster.updateAll(gamePane, player);
            Bullet.updateAll(gamePane, boss);
        } else {
            Monster.updateAll(gamePane, player);
            Bullet.updateAll(gamePane, null);
        }

        scoreText.setText("Score: " + player.getScore());

        handleMonsterSpawning();

        if (player.isDefeated()) {
            endGame();
            stopBackgroundMusic();
        }
        if (isBossRushMode) {
		    if (boss.getHp() <= 0) {
		    	gameLoop.stop();
		        if (boss != null) {
		            boss.CloseTimer();
		        }
		        showVictoryScreen(player.getScore());
		        stopBackgroundMusic();
		    }
		}
    }

    private void handleMonsterSpawning() {
        framesSinceLastSpawn++;

        // Spawn a monster if the cooldown has elapsed
        if (framesSinceLastSpawn >= spawnCooldownFrames) {
            new Monster(gamePane, false);
            framesSinceLastSpawn = 0; // Reset the cooldown counter
        }
    }

    private void updateFps(long now) {
        frameCount++;

        // Calculate FPS every second
        if (now - lastFpsUpdateTime >= 1_000_000_000) { // 1 second in nanoseconds
            int fps = frameCount;
            fpsText.setText("FPS: " + fps);

            // Reset counters
            frameCount = 0;
            lastFpsUpdateTime = now;
        }
    }

    private void endGame() {
        System.out.println("Game Over! Final Score: " + player.getScore());
        gameLoop.stop();
        if (boss != null) {
            boss.CloseTimer();
        }
        showGameOverScreen(player.getScore());
    }

    private void showGameOverScreen(int score) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameOverScreen.fxml"));
            Parent root = loader.load();
            Scene gameOverScene = new Scene(root);

            GameOverController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setScore(score);
            controller.setBossRushMode(isBossRushMode);
            controller.setGame(this);

            primaryStage.setScene(gameOverScene);
        } catch (Exception e) {
            System.out.println("Error loading GameOverScreen");
            e.printStackTrace();
        }
    }
    private void showVictoryScreen(int score) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("VictoryScreen.fxml"));
			Parent root = loader.load();
			Scene victoryScene = new Scene(root);
			
			VictoryController controller = (VictoryController) loader.getController();
			controller.setPrimaryStage(primaryStage);
			controller.setScore(score);
			controller.setBossRushMode(isBossRushMode);
			controller.setGame(this);
			primaryStage.setScene(victoryScene);
			
		} catch(Exception e) {
			System.out.println("Error VictoryScreen");
			e.printStackTrace();
		}
		
	}
}