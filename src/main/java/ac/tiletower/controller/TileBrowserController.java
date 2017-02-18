package ac.tiletower.controller;


import ac.tiletower.algorithm.Tile;
import ac.tiletower.model.AppModel;
import ac.tiletower.view.NumericalTextField;
import ac.tiletower.view.TileView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

import java.util.Arrays;

/**
 * AppController tile browser.
 * 
 * @author asilkaratas
 *
 */
public class TileBrowserController {
	//private static final Logger logger = Logger.getLogger(TileBrowserController.class);
	
	@FXML TilePane tilePane;
	@FXML Button okButton;
	
	@FXML Button setAllButton;
	@FXML NumericalTextField setAllField;

	private AppModel appModel;
	private final TileView.CountListener countListener;

	public TileBrowserController() {
		countListener = new TileView.CountListener() {
			@Override
			public void onUpdate(final Tile tile, final int count) {
				appModel.getTileCounts()[tile.getId()] = count;
			}
		};
	}

	@FXML
	public void initialize() {
		tilePane.autosize();
		
		setAllField.setMinValue(0);
		setAllField.setMaxValue(10000);
	}
	
	/*
	 * Sets tile tower. Creates tile views for tiles.
	 */
	public void setAppModel(AppModel appModel) {
		this.appModel = appModel;
		updateUI();
	}
	
	public Scene getScene() {
		return tilePane.getScene();
	}
	
	@FXML
	public void onSetAll(ActionEvent e) {
		int intValue = 0;
		try {
    		intValue = Integer.valueOf(setAllField.getText());
    	} catch(NumberFormatException ex) {} 
		
		if(intValue >= 0) {
			Arrays.fill(appModel.getTileCounts(), intValue);
			updateUI();
		}
	}
	
	private void updateUI() {
		tilePane.getChildren().clear();
		final int[] tileCounts = appModel.getTileCounts();
		for(final Tile tile : appModel.getInputData().getTiles()) {
			TileView tileView = new TileView();
			tileView.setTile(tile, tileCounts[tile.getId()]);
			tileView.setCountListener(countListener);
			tilePane.getChildren().add(tileView);
		}
	}
}
