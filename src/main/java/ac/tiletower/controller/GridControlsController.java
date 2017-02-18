package ac.tiletower.controller;

import ac.tiletower.App;
import ac.tiletower.algorithm.ProblemSolverThread;
import ac.tiletower.algorithm.StatsInfo;
import ac.tiletower.model.AppModel;
import ac.tiletower.model.ControlState;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Shows simple statistical information for current problem.
 * @author asilkaratas
 *
 */
public class GridControlsController implements Observer {

	@FXML Parent container;
	@FXML Slider zoomSlider;
	@FXML Button resetGridPositionButton;
	@FXML ComboBox<String> selectBoardComboBox;

	private App app;

	@FXML
	public void initialize() {
		zoomSlider.valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(javafx.beans.Observable arg0) {
				if(app != null) {
					app.getAppModel().setZoom((int)zoomSlider.valueProperty().get());
				}
			}
		});


		selectBoardComboBox.getItems().add("All");
		selectBoardComboBox.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(javafx.beans.Observable observable) {
				if(app != null) {
					app.getAppModel().setSelectedBoard(selectBoardComboBox.getSelectionModel().getSelectedIndex());
				}
			}
		});

		selectBoardComboBox.getSelectionModel().select(0);
	}

	@FXML
	public void onResetGridPosition(ActionEvent e) {
		app.getAppModel().resetGridPosition();
	}


	public void setApp(App app) {
		this.app = app;
		app.getAppModel().addObserver(this);

		updateUI();
	}

	public void updateUI() {
		zoomSlider.setValue(app.getAppModel().getZoom());
		final ControlState controlState = app.getAppModel().getControlState();

		container.disableProperty().set(controlState == ControlState.CLEAR);
	}

	@Override
	public void update(final Observable o, final Object arg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				updateUI();

				handleBacktrac(o, arg);
			}
		});
	}

	private void handleBacktrac(final Observable o, final Object arg) {
		if(o == app.getAppModel() && arg != null && arg == AppModel.BACKTRACK) {
			List<String> items = new ArrayList<>();
			items.add("All");
			for(int i = 0; i < app.getAppModel().getBacktrack(); i++) {
				items.add("Board " + (i + 1));
			}

			selectBoardComboBox.getItems().clear();
			selectBoardComboBox.getItems().addAll(items);
			selectBoardComboBox.getSelectionModel().select(0);
		}
	}


}
