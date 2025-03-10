package application;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LevelSelectionController {
	private Stage primaryStage;
	
	@FXML private void handleClassicButton() {
		System.out.println("Starting Classic Mode");
		startGame(false);
	}
	@FXML private void handleBossRushButton() {
		System.out.println("Starting Boss Rush Mode");
		startGame(true);
	}
	
	private void startGame(boolean isBossRushMode) {
		Game game = new Game(primaryStage, isBossRushMode);
		game.startGame();
	}
	
	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
}
