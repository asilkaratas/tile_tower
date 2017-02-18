package ac.tiletower.controller;

import java.util.Observable;
import java.util.Observer;

import ac.tiletower.model.AppModel;
import ac.tiletower.model.ControlState;
import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import ac.tiletower.App;
import ac.tiletower.view.NumericalTextField;

/**
 * AppController control user interface.
 * It has glue code between application model and control user interface.
 * 
 * @author asilkaratas
 *
 */
public class ControlsController implements Observer {
	private static final Logger logger = Logger.getLogger(ControlsController.class);
	
	private App app;
	
	@FXML Parent container;
	
	@FXML Label backtrackLabel;
	@FXML NumericalTextField backtrackField;
	
	@FXML Button startButton;
	@FXML Button stopButton;
	@FXML Button pauseButton;
	@FXML Button resumeButton;
	
	@FXML Button nextStepButton;
	
	@FXML Label stepLabel;
	@FXML NumericalTextField stepField;
	@FXML Button goStepButton;
	

	
	public ControlsController() {
	}
	
	@FXML 
	public void initialize() {
		
		// This listeners make sure input text is a number in range
		// TODO : Refactor this methods in an object.
		backtrackField.setMinValue(1);
		backtrackField.setMaxValue(1000);
		backtrackField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				int intValue = 0;
				try {
            		intValue = Integer.valueOf(backtrackField.getText());
            	} catch(NumberFormatException e) {} 
				
				if(app != null) {
					app.getAppModel().setBacktrack(intValue);
				}
			}
		});
		
		stepField.setMinValue(1);
		stepField.setMaxValue(1000);
		

	
	}
	
	public void setApp(App app) {
		this.app = app;
		app.getAppModel().addObserver(this);
		
		updateUI();
	}
	
	@FXML
	public void onStop(ActionEvent e) {
		app.getAppController().stop();
	}
	
	@FXML
	public void onStart(ActionEvent e) {
		app.getAppController().start();
	}
	
	@FXML
	public void onPause(ActionEvent e) {
		app.getAppController().pause();
	}
	
	@FXML
	public void onResume(ActionEvent e) {
		app.getAppController().resume();
	}
	
	@FXML
	public void onNextStep(ActionEvent e) {
		app.getAppController().goToNextStep();
	}
	
	@FXML
	public void onGoStep(ActionEvent e) {
		int step = 0;
		
		try {
			step = Integer.valueOf(stepField.getText());
		} catch(NumberFormatException ex) {
			
		}
		
		app.getAppController().goToStep(step);
	}
	

	private void updateUI() {
		final AppModel appModel = app.getAppModel();
		final ControlState controlState = appModel.getControlState();
	
		container.disableProperty().set(controlState == ControlState.CLEAR);
		backtrackField.disableProperty().set(controlState != ControlState.STOPPED);
		backtrackField.textProperty().set(String.valueOf(appModel.getBacktrack()));
		startButton.disableProperty().set(controlState != ControlState.STOPPED);
		stopButton.disableProperty().set(controlState == ControlState.STOPPED);
		resumeButton.disableProperty().set(controlState != ControlState.PAUSED);
		pauseButton.disableProperty().set(controlState != ControlState.RUNNING);
		
		nextStepButton.disableProperty().set(controlState != ControlState.PAUSED);
		
		
		stepLabel.disableProperty().set(controlState != ControlState.PAUSED);
		stepField.disableProperty().set(controlState != ControlState.PAUSED);
		goStepButton.disableProperty().set(controlState != ControlState.PAUSED);
	
	}

	@Override
	public void update(Observable o, Object arg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				updateUI();
			}
		});
	}
}
