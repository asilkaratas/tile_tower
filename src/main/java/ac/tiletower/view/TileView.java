package ac.tiletower.view;

import java.io.IOException;

import ac.tiletower.algorithm.Bitmap;
import ac.tiletower.algorithm.Tile;
import org.apache.log4j.Logger;


import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * TileView draws a given tile to a canvas.
 * It resizes cell size depends of size of tile.
 * 
 * It has amount text field that updates written value to the amount of tile object.
 * @author asilkaratas
 *
 */
public class TileView extends VBox {
	private static final Logger logger = Logger.getLogger(TileView.class);
	
	@FXML StackPane canvasContainer;
	@FXML ResizableCanvas canvas;
	@FXML NumericalTextField amountField;
	
	private Tile tile;
	private int count;
	private CountListener countListener;
	
	public TileView() {
		tile = null;
		count = 1;
		countListener = null;

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/tile_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);


        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	@FXML
	public void initialize() {
		canvasContainer.setMinSize(0, 0);
		canvasContainer.setAlignment(Pos.CENTER);
		
		this.autosize();
		
		amountField.setMinValue(0);
		amountField.setMaxValue(10000);
		amountField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				int intValue = 0;
				try {
            		intValue = Integer.valueOf(amountField.getText());
            	} catch(NumberFormatException e) {} 
				
				if(tile != null) {
					count = intValue;
					if(countListener != null) {
						countListener.onUpdate(tile, count);
					}
				}
			}
		});
		
		canvas.widthProperty().bind(canvasContainer.widthProperty().subtract(1));
		canvas.heightProperty().bind(canvasContainer.heightProperty().subtract(1));
		
		InvalidationListener invalidationListener = new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				updateUI();
			}
		};
		
		canvas.heightProperty().addListener(invalidationListener);
	}
	
	public void setTile(final Tile tile, final int count) {
		this.tile = tile;
		this.count = count;
		updateUI();
	}
	
	private void updateUI() {
		if(tile == null) return;
		
		amountField.setText(String.valueOf(count));
		
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		
		Bitmap bitmap = tile.getOriginalBitmap();
		int max = Math.max(bitmap.getWidth(), bitmap.getHeight());
		
		int cellSize = (int)(canvas.getWidth()/max);
		int filledCellSize = cellSize - 1;
		int offsetX = (int)((canvas.getWidth() - bitmap.getWidth() * cellSize) / 2);
		int offsetY = (int)((canvas.getHeight() - bitmap.getHeight() * cellSize) / 2);
		//logger.info("Canvas:" + canvas.getWidth() + " h:" + canvas.getHeight() + " offsetX:" + offsetX);
		
		graphics.clearRect(0, 0, canvas.getHeight(), canvas.getHeight());
		
		
		for(int y = 0; y < bitmap.getHeight(); y++) {
			for(int x = 0; x < bitmap.getWidth(); x++) {
				if(bitmap.getValue(x, y) == 1) {
					graphics.setFill(Color.GRAY);
				} else {
					//graphics.setFill(Color.web("#c8c8c8"));
					graphics.setFill(Color.WHITE);
				}
				graphics.fillRect(x * cellSize + offsetX,  (bitmap.getHeight()-y-1) * cellSize + offsetY, filledCellSize, filledCellSize);
				
			}
		}
	}

	public void setCountListener(CountListener countListener) {
		this.countListener = countListener;
	}

	public interface CountListener {
		void onUpdate(Tile tile, int count);
	}
}
