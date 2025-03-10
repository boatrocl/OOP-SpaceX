package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VictoryController {
	@FXML private Text scoreText;
	private Stage primaryStage;
	private int score;
	private boolean isBossRushMode;
	private Game game;
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void setScore(int score) {
		this.score = score;
		scoreText.setText("Score : " + score);
	}
	
	public void setBossRushMode(boolean isBossRushMode) {
		this.isBossRushMode = isBossRushMode;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	@FXML private void handleRestartButton() {
		//Game game = new Game(primaryStage, isBossRushMode);
		
		game.startGame();
	}
	
	@FXML private void handleLevelSelectionButton() {
		try {
			//FXMLLoader loader = new FXMLLoader(getClass().getResource("LevelSelection.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			
			/*LevelSelectionController levelSelectionController = loader.getController();
			levelSelectionController.setStage(primaryStage);*/
			
			MainMenuController mainmenucontroller = loader.getController();
			mainmenucontroller.setStage(primaryStage);
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Space Invaders - MainMenu");
			primaryStage.show();
		} catch(Exception e) {
			System.out.print("Error levelSelection");
			e.printStackTrace();
		}
	}
}
