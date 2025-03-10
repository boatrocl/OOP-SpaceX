package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Load the main menu FXML and get the controller
        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent mainMenu = mainMenuLoader.load();
        Scene mainMenuScene = new Scene(mainMenu);

        // Get the controller from the loader
        MainMenuController mainMenuController = mainMenuLoader.getController();

        // Load the level selection FXML and get the controller
        FXMLLoader levelSelectionLoader = new FXMLLoader(getClass().getResource("LevelSelection.fxml"));
        Parent levelSelection = levelSelectionLoader.load();
        Scene levelSelectionScene = new Scene(levelSelection);

        // Get the controller from the loader
        LevelSelectionController levelSelectionController = levelSelectionLoader.getController();

        // Pass the stage and scenes to the main menu controller
        mainMenuController.setStageAndScenes(primaryStage, mainMenuScene, levelSelectionScene);

        // Pass the stage to the level selection controller
        levelSelectionController.setStage(primaryStage);

        // Set the main menu scene
        primaryStage.setTitle("Space Invader");
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
