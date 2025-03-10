package application;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class MainMenuController {
	private Stage primaryStage;
	private Scene mainMenuScene;
	private Scene levelSelectionScene;
	private Boolean isBossRushMode= false;
	
	@FXML private void handlePlayButton() {
	//primaryStage.setScene(levelSelectionScene);
		if(modeComboBox.getValue()=="Boss Rush Mode") {
			isBossRushMode = true;
		}
		Game game = new Game(primaryStage, isBossRushMode);
		game.startGame();
	
	}
	@FXML private void handleQuitButton() {
		primaryStage.close();
	}
	
	 @FXML
	    private ComboBox<String> modeComboBox; // Linked to the ComboBox in FXML

	    @FXML
	    public void initialize() {
	        // Add options to the ComboBox
	        modeComboBox.getItems().addAll("Endless Mode", "Boss Rush Mode");

	        // Set a default selected item (optional)
	        modeComboBox.setValue("Endless Mode");
	   
	    }
	
	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	
 public boolean getGameMode() {
	 return isBossRushMode;
 }
	
	
	public void setStageAndScenes(Stage primaryStage, Scene mainMenuScene, Scene levelSelectionScene) {
		this.primaryStage = primaryStage;
		this.mainMenuScene = mainMenuScene;
		this.levelSelectionScene = levelSelectionScene;
	}


}
