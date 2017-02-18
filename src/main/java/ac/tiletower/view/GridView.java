package ac.tiletower.view;

import java.io.IOException;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import org.apache.log4j.Logger;

import ac.tiletower.controller.GridController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Manages complex cell drawing, zooming, moving. It gives unlimited drawing area.
 * It has 2 canvas. 1st for drawing empty cells that is redrawn only when zoom changes.
 * 2th for drawing colorful cells.
 * 
 * @author asilkaratas
 *
 */
public class GridView extends StackPane {
	private static final Logger logger = Logger.getLogger(GridView.class);
	
	@FXML StackPane canvasContainer;
	@FXML ResizableCanvas canvasBg;
	@FXML ResizableCanvas canvas;
	
	private Point2D gridPoint;
	private Point2D clickedPoint;
	private int cellSize;
	private int filledCellSize;
	private int borderSize;
	private Color cellColor;
	private GraphicsContext context;
	private Point2D min;
	private Point2D max;
	
	private GridViewDrawCallback drawCallback;
	
	public GridView() {
		loadFXML();
        
		drawCallback = null;
		cellSize = 10;
		cellColor = Color.WHITE;
		clickedPoint = Point2D.ZERO;
		gridPoint = Point2D.ZERO;
		min = Point2D.ZERO;
		max = Point2D.ZERO;
		
		setMinSize(0, 0);
		setAlignment(Pos.CENTER);

		final InvalidationListener invalidationListener = new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				drawBg();
				draw();
			}
		};
		canvasBg.widthProperty().bind(widthProperty().subtract(10));
		canvasBg.heightProperty().bind(heightProperty().subtract(10));
		canvas.widthProperty().bind(widthProperty().subtract(10));
		canvas.heightProperty().bind(heightProperty().subtract(10));
		canvas.heightProperty().addListener(invalidationListener);
		canvas.widthProperty().addListener(invalidationListener);
		context = canvas.getGraphicsContext2D();
	}
	
	private void loadFXML() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/grid_view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	@FXML
	public void canvas_onMousePressed(MouseEvent e) {
		clickedPoint = new Point2D(e.getX(), e.getY());
		canvas.getScene().setCursor(Cursor.CLOSED_HAND);
	}
	
	@FXML
	public void canvas_onMouseReleased(MouseEvent e) {
		canvas.getScene().setCursor(Cursor.OPEN_HAND);
	}
	
	@FXML
	public void canvas_onMouseEntered(MouseEvent e) {
		canvas.getScene().setCursor(Cursor.OPEN_HAND);
	}
	
	@FXML
	public void canvas_onMouseExited(MouseEvent e) {
		canvas.getScene().setCursor(Cursor.DEFAULT);
	}
	
	@FXML
	public void canvas_onMouseDragged(MouseEvent e) {
		Point2D point = new Point2D(e.getX(), e.getY());
		setGridPoint(point);
	}
	
	private void setGridPoint(Point2D point) {
		Point2D deltaPoint = clickedPoint.subtract(point);
		clickedPoint = point;
		gridPoint = gridPoint.add(deltaPoint);
		draw();
	}
	
	public void setDrawCallback(GridViewDrawCallback drawCallback) {
		this.drawCallback = drawCallback;
	}
	
	public GridViewDrawCallback getDrawCallback() {
		return drawCallback;
	}
	
	public void setCellSize(int cellSize) {
		preserveCenter(this.cellSize, cellSize);
		this.cellSize = cellSize;
		calculateFilledSize();
	}
	
	private void preserveCenter(int oldCellSize, int cellSize) {
		double centerX = (gridPoint.getX() + canvas.getWidth()/2)/oldCellSize;
		double centerY = (gridPoint.getY() + canvas.getHeight()/2)/oldCellSize;
		
		double posX = centerX * cellSize - canvas.getWidth()/2;
		double posY = centerY * cellSize - canvas.getHeight()/2;
		
		gridPoint = new Point2D(posX, posY);
	}
	
	private void calculateFilledSize() {
		filledCellSize = cellSize - borderSize;
	}
	
	public int getCellSize() {
		return cellSize;
	}
	
	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
		calculateFilledSize();
	}
	
	public int getBorderSize() {
		return borderSize;
	}
	
	public void drawBg() {
		GraphicsContext graphics = canvasBg.getGraphicsContext2D();
		graphics.clearRect(0, 0, canvasBg.getWidth(), canvasBg.getHeight());
		graphics.setFill(cellColor);
		
		int sizeX = (int)Math.ceil(canvasBg.getWidth()/cellSize);
		int sizeY = (int)Math.ceil(canvasBg.getHeight()/cellSize);
		
		for(int y = 0; y < sizeY; y++) {
			for(int x = 0; x < sizeX; x++) {
				graphics.fillRect(x * cellSize, y * cellSize, filledCellSize, filledCellSize);
			}
		}
	}
	
	public void move(Point2D point) {
		gridPoint = new Point2D(-point.getX()*cellSize, -point.getY()*cellSize);
	}
	
	private void calculateOffsetPoint() {
		int offsetX = (int)gridPoint.getX();
		int offsetY = (int)gridPoint.getY();
		
		int sizeX = (int)Math.ceil(canvas.getWidth()/cellSize);
		int sizeY = (int)Math.ceil(canvas.getHeight()/cellSize);
		
		offsetX = offsetX - offsetX % cellSize;
		offsetY = offsetY - offsetY % cellSize;
		
		offsetX /= cellSize;
		offsetY /= cellSize;
		
		min = new Point2D(offsetX, offsetY);
		max = new Point2D(offsetX + sizeX, offsetY + sizeY);
	}
	
	public void moveCenter() {
		gridPoint = new Point2D(-canvas.getWidth()/2, -canvas.getHeight()/2);
	}
	
	public void draw() {
		context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		calculateOffsetPoint();
		if(drawCallback != null) {
			drawCallback.onDraw(this);
		}
	}
	
	public void setFill(Color color) {
		context.setFill(color);
	}
	
	public void fillCell(int x, int y) {
		if(x >= min.getX() && x <= max.getX() && y >= min.getY() && y <= max.getY()) {
			//logger.info("draw");
			context.fillRect((-min.getX() + x) * cellSize, (-min.getY() + y) * cellSize, filledCellSize, filledCellSize);
		}
	}
	
	public static interface GridViewDrawCallback {
		void onDraw(GridView gridView);
	}
}
