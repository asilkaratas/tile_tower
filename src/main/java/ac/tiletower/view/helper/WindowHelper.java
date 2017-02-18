package ac.tiletower.view.helper;

import java.io.IOException;

import ac.tiletower.Main;
import ac.tiletower.view.AlertWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * General purpose Window helper. it is used for showing model and alert windows.
 * @author asilkaratas
 *
 */
public class WindowHelper {
	
	/*
	 * Loads fxml and shows it as a modal window.
	 */
	public static FXMLLoader showModal(String fxml, String title, Scene owner) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ClassLoader.getSystemClassLoader().getResource(fxml));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
		WindowHelper.showModal(root, title, owner);
	    
	    return loader;
	}
	
	/*
	 * Shows a view as a modal window.
	 */
	public static void showModal(Parent root, String title, Scene owner) {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(Main.CSS);
		
		Stage stage = new Stage();
		stage.resizableProperty().set(false);
		stage.setScene(scene);
	    stage.setTitle(title);
	    stage.initStyle(StageStyle.UTILITY);
	    stage.initModality(Modality.WINDOW_MODAL);
	    stage.initOwner(owner.getWindow());
	    stage.show();
	}
	
	/*
	 * Shows alert window with a message.
	 */
	public static void showAlert(String message, Scene owner) {
		AlertWindow alertWindow = new AlertWindow(message);
		showModal(alertWindow, "Error", owner);
	}
}
