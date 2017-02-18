package ac.tiletower.controller;


import java.util.*;

import ac.tiletower.algorithm.*;
import ac.tiletower.model.AppModel;
import ac.tiletower.model.ControlState;
import org.apache.log4j.Logger;

import ac.tiletower.App;
import ac.tiletower.view.GridView;
import ac.tiletower.view.GridView.GridViewDrawCallback;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;


/**
 * AppController Grid View. It has glue code and a callback function from GridView
 *  that defines how to draw boards to the GridView 
 * 
 * @author asilkaratas
 *
 */
public class GridController implements Observer, GridViewDrawCallback {
	private static final Logger logger = Logger.getLogger(GridController.class);
	
	@FXML GridView gridView;
	
	private App app;
	private ColorModel colorModel;
	private DrawInfo drawInfo;
	private final int space;
	
	public GridController() {
		drawInfo = null;
		space = 4;
	}
	
	@FXML
	public void initialize() {
		gridView.setBorderSize(1);
		gridView.setDrawCallback(this);
		resetGridPosition();
	}
	
	public void setApp(final App app) {
		this.app = app;
		this.colorModel = app.getColorModel();
		app.getAppModel().addObserver(this);
		app.getProblemSolverThread().setDrawListener(new ProblemSolverThread.DrawListener() {
			@Override
			public void onDraw(final DrawInfo info) {
				drawInfo = info;

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Collections.sort(drawInfo.getBoardInfos());
						gridView.draw();
					}
				});
			}
		});
	}
	
	private void updateUI() {
		gridView.disableProperty().set(app.getAppModel().getControlState() == ControlState.CLEAR);
	}

	@Override
	public void update(final Observable o, final Object arg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				updateUI(); 
				
				if(o == app.getAppModel()) {
					if(arg != null) {
						if(arg.equals(AppModel.ZOOM)) {
							gridView.setCellSize(app.getAppModel().getZoom());
							gridView.drawBg();
						} else if(arg.equals(AppModel.RESET_GRID_POSITION)) {
							resetGridPosition();
						} else if(arg.equals(AppModel.SELECTED_BOARD)) {
							resetGridPosition();
						}
					}
				}
				gridView.draw();
			}
		}); 
	}
	
	private void resetGridPosition() {
		if(drawInfo == null ||  drawInfo.getBoardInfos().isEmpty()) {
			return;
		}

		final int padding = 50;
		final int cellSize = gridView.getCellSize();
		final int boardWidth = drawInfo.getBoardInfos().get(0).getWidth();
		int totalWidth;
		if(app.getAppModel().showAllBoards()) {
			totalWidth = (boardWidth + space) * drawInfo.getBoardInfos().size() * cellSize;
		} else {
			totalWidth = (boardWidth + space) * cellSize;
		}
		final int gridWidth = (int)gridView.getWidth() - padding * 2;
		final int gridHeight = (int)gridView.getHeight();

		int moveX = totalWidth < gridWidth ? (gridWidth - totalWidth)/2 + padding : padding;
		//System.out.println("moveX:" + moveX + " gridWidth:" + gridWidth + " totalWidth:" + totalWidth);
		gridView.move(new Point2D(moveX/cellSize, gridHeight/cellSize - 10));

	}

	@Override
	public void onDraw(GridView gridView) {
		if(drawInfo == null || drawInfo.getBoardInfos().isEmpty()) return;

		final AppModel appModel = app.getAppModel();

		if(appModel.showAllBoards()) {
			int offset = 0;
			for(final BoardInfo boardInfo : drawInfo.getBoardInfos()) {
				drawBoard(gridView, boardInfo, offset);
				offset += boardInfo.getWidth() + space;
			}
		} else {
			final BoardInfo boardInfo = drawInfo.getBoardInfos().get(appModel.getSelectedBoard()-1);
			drawBoard(gridView, boardInfo, 0);
		}

	}
	
	private void drawBoard(final GridView gridView, final BoardInfo boardInfo, final int offset) {
		drawBardWalls(gridView, boardInfo, offset);

		gridView.fillCell(offset-2, -boardInfo.getRowFillsY());
		gridView.fillCell(offset-2, -boardInfo.getRowFillsY()-boardInfo.getRowFillsHeight());

		Placement lastPlacement = boardInfo.getLastPlacement();

		while(lastPlacement != null) {
			final Color color = colorModel.getColor(lastPlacement.getIndex());
			gridView.setFill(color);

			final Bitmap bitmap = lastPlacement.getBitmap();
			final int bitmapWidth = bitmap.getWidth();
			final int bitmapHeight = bitmap.getHeight();

			for(int y = bitmapHeight - 1; y >= 0; y--) {
				for(int x = bitmapWidth - 1; x >= 0; x--) {
					if(bitmap.getValue(x, y) != 0) {
						final int cellX = offset + x + lastPlacement.getX();
						final int cellY = -lastPlacement.getY()-y;
						gridView.fillCell(cellX, cellY);
					}
				}
			}


			lastPlacement = lastPlacement.getParent();
		}
	}
	
	private void drawBardWalls(final GridView gridView, final BoardInfo boardInfo, final int offset) {
		final int boardWidth = boardInfo.getWidth();
		final int boardHeight = boardInfo.getHeight() + 1;

		gridView.setFill(Color.BLACK);
		for(int i = 0; i < boardHeight; i++) {
			gridView.fillCell(offset - 1, -i+1);
			gridView.fillCell(offset + boardWidth, -i+1);
		}
		
		for(int i = 0; i < boardWidth; i++) {
			gridView.fillCell(offset + i, 1);
		}
	}
}
