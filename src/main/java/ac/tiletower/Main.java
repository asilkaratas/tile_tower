package ac.tiletower;


import ac.tiletower.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point of the application. ac.tiletower.App facade and controllers are linked here.
 * 
 * @author asilkaratas
 *
 */
public class Main extends Application {
	public static final String CSS = "css/style.css";
	
	/*
	 * Starting point of application and configuratian of controllers.
	 */
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getClassLoader().getResource("fxml/main.fxml"));
		Parent root = loader.load();
    
		Scene scene = new Scene(root, 1200, 700);
		scene.getStylesheets().add(Main.CSS);
        stage.setTitle("Tile Tower");
        stage.setScene(scene);
        stage.show();

		App app = new App(scene);
		app.start();
		MainController mainController = loader.getController();
		mainController.setApp(app);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
