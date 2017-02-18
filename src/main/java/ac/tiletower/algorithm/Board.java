package ac.tiletower.algorithm;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

public class Board implements Serializable {

	private final int width;
	private final int[] heights;
	private final BestPlacements bestPlacements;
	private final PlacementCalculator placementCalculator;
	private final ScoreCalculator scoreCalculator;
	private final Tile[] tiles;

	private Placement lastPlacement;
	private Placement waitingPlacement;
	private int placementCount;
	private final int[] rowFills;
	private int rowFillsSum;
	private int rowFillsY;
	private int maxRowFillsY;
	private int height;
	private int minHeight;
	private int filled;
	private int[] tileCounts;
	private final String name;

	
	public Board(final String name, final int width, final int backtrack,
				 final Tile[] tiles, final int[] tileCounts, final int maxTileHeight) {
		this.name = name;
		this.width = width;
		this.heights = new int[width];
		this.tiles = tiles;
		this.tileCounts = tileCounts;
		this.filled = 0;
		this.rowFillsY = 0;
		this.placementCount = 0;
		this.rowFills = new int[maxTileHeight * 3];
		this.lastPlacement = null;
		this.bestPlacements = new BestPlacements(backtrack);
		this.placementCalculator = new PlacementCalculator(this);
		this.scoreCalculator = new ScoreCalculator(placementCalculator);
	}

	public PlacementCalculator getPlacementCalculator() {
		return placementCalculator;
	}

	public ScoreCalculator getScoreCalculator() {
		return scoreCalculator;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int[] getHeights() {
		return heights;
	}

	public int[] getRowFills() {
		return rowFills;
	}

	public int getRowFillsY() {
		return rowFillsY;
	}

	public int getMaxRowFillsY() {
		return maxRowFillsY;
	}

	public void setMaxRowFillsY(int maxRowFillsY) {
		this.maxRowFillsY = maxRowFillsY;
	}

	public int getRowFillsSum() {
		return rowFillsSum;
	}

	public int getFilled() {
		return filled;
	}

	public int place(final Placement placement) {
		//System.out.println("board:" + name +" place:" + placement.getTile().getId() + " " + placement.getBitmap());
		int y = addBitmap(placement.getBitmap(), placement.getX());
		placement.setY(y);
		placement.setParent(lastPlacement);
		lastPlacement = placement;

		tileCounts[placement.getTile().getId()]--;
		placement.setIndex(placementCount);
		placementCount ++;
		//System.out.println("board:" + name + " rowFillsY:" + rowFillsY + " placement:" + placement);

		waitingPlacement = null;
		return y;
	}

	public Placement getLastPlacement() {
		return lastPlacement;
	}

	private int addBitmap(final Bitmap bitmap, final int x) {
		placementCalculator.calculateHoles(bitmap, x);

		final int[] bitmapHeights = bitmap.getHeights();
		final int[] bitmapHoles = bitmap.getHoles();
		final int[] holes = placementCalculator.getHoles();
		final int bitmapWidth = bitmap.getWidth();
		int columnHeight;
		int index;
		minHeight = Integer.MAX_VALUE;
		for(int i = bitmapWidth-1; i >= 0; i--) {
			index = x + i;
			columnHeight = heights[index] + holes[index]
					- bitmapHoles[i] + bitmapHeights[i];
			heights[index] = columnHeight;
			if(columnHeight > height) {
				height = columnHeight;
			}

			if(columnHeight < minHeight) {
				minHeight = columnHeight;
			}
		}

		System.arraycopy(heights, x, placementCalculator.getHeights(), x, bitmap.getWidth());
		filled += bitmap.getFilled();

		int y = heights[x] - bitmapHeights[0];

		if(height - rowFillsY > rowFills.length/2) {
			int shift = height-rowFillsY-rowFills.length/2;
			if(rowFillsY + shift>minHeight) {
				shift = minHeight - rowFillsY;
			}
			shiftRowFills(shift);
		}

		addRowFills(bitmap, y);
		return y;
	}

	private void shiftRowFills(final int shift) {

		// decrementing rowFillsSum for removed rows
		for(int i = 0; i < shift; i++) {
			rowFillsSum -= rowFills[i];
		}

		// shifting rowFills array up. Removing first rows.
		System.arraycopy(rowFills, shift, rowFills, 0, rowFills.length-shift);
		Arrays.fill(rowFills, rowFills.length-shift, rowFills.length, 0);
		rowFillsY += shift;

		System.arraycopy(rowFills, 0, placementCalculator.getRowFills(), 0, rowFills.length);
	}

	private void addRowFills(final Bitmap bitmap, final int y) {
		final int[] bitmapRowFills = bitmap.getRowFills();
		final int fillsStart = -rowFillsY + y;
		//System.out.println("fillsStart:" + fillsStart + " rowFillsY:" + rowFillsY + " y:" + y);
		for(int i = 0; i < bitmapRowFills.length; i++) {
			rowFills[fillsStart + i] += bitmapRowFills[i];
		}

		rowFillsSum += bitmap.getFilled();
		System.arraycopy(rowFills, fillsStart, placementCalculator.getRowFills(), fillsStart, bitmapRowFills.length);
	}


	public LinkedList<Placement> nextStep(final double bestLowestScore) {
		bestPlacements.clear();
		double lowestScore = bestLowestScore;
		if(waitingPlacement != null) {
			place(waitingPlacement);
		}

		int index = 0;
		for(int i : tileCounts) {
			if(i > 0) {
				Tile tile = tiles[index];
				for(final Bitmap bitmap : tile.getBitmaps()) {
					for(int x = width-bitmap.getWidth(); x >= 0; x--) {
						final double score = scoreCalculator.calculateScore(bitmap, x);
						if(score >= lowestScore) {
							final Placement placement = new Placement(tile, bitmap, x, score);
							placement.setRowFillsY(placementCalculator.getRowFillsY());
							bestPlacements.add(placement);
							lowestScore = bestPlacements.getBestLowestScore();
						}
					}
				}
			}

			index++;
		}

		// Setting owner board
		LinkedList<Placement> placements = bestPlacements.toLinkedList();
		for(Placement placement : placements) {
			placement.setBoard(this);
		}

		return placements;
	}

	public void setWaitingPlacement(Placement waitingPlacement) {
		this.waitingPlacement = waitingPlacement;
	}

	public double getDensity() {
		if(height == 0) return 0;
		return (double)filled/ (width * height);
	}

	public int getPlacementCount() {
		return placementCount;
	}

	public void copyFrom(final Board board) {
		this.lastPlacement = board.lastPlacement;
		this.filled = board.filled;
		this.height = board.height;
		this.minHeight = board.minHeight;
		this.placementCount = board.placementCount;
		this.rowFillsY = board.rowFillsY;
		this.rowFillsSum = board.rowFillsSum;
		this.maxRowFillsY = board.maxRowFillsY;

		System.arraycopy(board.tileCounts, 0, tileCounts, 0, tileCounts.length);
		System.arraycopy(board.heights, 0, heights, 0, heights.length);
		System.arraycopy(board.rowFills, 0, rowFills, 0, rowFills.length);
		System.arraycopy(board.placementCalculator.getHeights(), 0, placementCalculator.getHeights(), 0, heights.length);
		System.arraycopy(board.placementCalculator.getRowFills(), 0, placementCalculator.getRowFills(), 0, rowFills.length);
	}

}
