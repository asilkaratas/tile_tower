package ac.tiletower.controller;

import ac.tiletower.App;
import javafx.fxml.FXML;

/**
 * Main controller that contains other controllers.
 * 
 * @author asilkaratas
 *
 */
public class MainController {

	@FXML GridController gridController;
	@FXML ControlsController controlsController;
	@FXML MenuBarController menuBarController;
	@FXML StatsController statsController;
	@FXML GridControlsController gridControlsController;
	
	
	/**
	 * Passes ac.tiletower.App object to child controllers.
	 * 
	 * @param app ac.tiletower.App facade.
	 */
	public void setApp(App app) {
		gridController.setApp(app);
		controlsController.setApp(app);
		menuBarController.setApp(app);
		statsController.setApp(app);
		gridControlsController.setApp(app);
	}
}
