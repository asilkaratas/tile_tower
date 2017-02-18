package ac.tiletower.controller;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import ac.tiletower.model.ControlState;
import ac.tiletower.validator.RepeatedTileException;
import org.apache.log4j.Logger;

import ac.tiletower.App;
import ac.tiletower.io.InputFileParserException;
import ac.tiletower.view.helper.FileChooserHelper;
import ac.tiletower.view.helper.WindowHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;

/**
 * MenuBar controller manages user actions that generated from menubar.
 * 
 * @author asilkaratas
 *
 */
public class MenuBarController implements Observer {
	private static final Logger logger = Logger.getLogger(MenuBarController.class);
	
	@FXML MenuBar menuBar;
	@FXML Menu fileMenu;
	@FXML MenuItem loadInputFileButton;
	@FXML MenuItem saveProgramStateButton;
	@FXML MenuItem loadProgramStateButton;
	@FXML MenuItem exitButton;
	
	@FXML Menu viewMenu;
	@FXML MenuItem showTileBrowserButton;
	@FXML MenuItem showDensityChartButton;
	
	private App app;
	
	/*
	 * Sets tile tower. Binds UI elements.
	 */
	
	public void setApp(App app) {
		this.app = app;
		app.getAppModel().addObserver(this);
		updateUI();
	}
	
	
	/**
	 * Input file is loaded or error message is show in case of exception.
	 * @param e Event
	 */
	@FXML
	public void onLoadInputFile(ActionEvent e) {
		FileChooser fileChooser = FileChooserHelper.createInputFileChooser("Load Input File");
		File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
		
		boolean tilesLoaded = false;
		if(file != null) {
			try {
				app.loadTiles(file);
				logger.info("Tiles Loaded!");
				tilesLoaded = true;
			} catch (RepeatedTileException exception) {
				logger.info("e:" + exception.getMessage());
				WindowHelper.showAlert(exception.getMessage(), menuBar.getScene());
			} catch (InputFileParserException exception) {
				logger.info("e:" + exception.getMessage());
				WindowHelper.showAlert(exception.getMessage(), menuBar.getScene());
			}
		}
		
		if(tilesLoaded) {
			app.getAppModel().setControlState(ControlState.STOPPED);
			showTileBrowser();
		} else {
			app.getAppModel().setControlState(ControlState.CLEAR);
		}
	}
	
	@FXML
	public void onLoadProgramState(ActionEvent e) {
		FileChooser fileChooser = FileChooserHelper.createAppDataChooser("Load Program State");
		File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
		
		if(file != null) {
			app.loadAppData(file.getPath());
		}
	}
	
	@FXML
	public void onSaveProgramState(ActionEvent e) {
		FileChooser fileChooser = FileChooserHelper.createAppDataChooser("Save Program State");
		File file = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
		
		if(file != null) {
			app.saveAppData(file.getPath());
		}
		
	}
	
	@FXML
	public void onShowTileBrowser(ActionEvent e) {
		showTileBrowser();
	}
	
	@FXML
	public void onShowDensityChart(ActionEvent e) {
		app.showDensityChart();
	}
	
	@FXML
	public void onExit(ActionEvent e) {
		Platform.exit();
	}
	
	/**
	 * Shows tile broser
	 */
	private void showTileBrowser() {
		FXMLLoader loader = WindowHelper.showModal("fxml/tile_browser.fxml", "Tile Browser", menuBar.getScene());
		TileBrowserController controller = loader.getController();
		controller.setAppModel(app.getAppModel());
		
		controller.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent arg0) {
				//tileTower.calculateTileCount();
			}
		});
	}

	private void updateUI() {
		final ControlState controlState = app.getAppModel().getControlState();
		loadInputFileButton.disableProperty().set(controlState == ControlState.RUNNING || controlState == ControlState.PAUSED);
		saveProgramStateButton.disableProperty().set(controlState == ControlState.CLEAR);
		loadProgramStateButton.disableProperty().set(controlState == ControlState.RUNNING || controlState == ControlState.PAUSED);
		exitButton.disableProperty().set(controlState == ControlState.RUNNING || controlState == ControlState.PAUSED);
		
		showTileBrowserButton.disableProperty().set(controlState != ControlState.STOPPED);
		final boolean disabled = controlState != ControlState.STOPPED || app.getProblemSolverThread().getSolution() == null;
		showDensityChartButton.disableProperty().set(disabled);
	}
	@Override
	public void update(Observable o, Object arg) {
		updateUI();
	}
	
	
}
