package ac.tiletower.algorithm;

import java.io.Serializable;
import java.util.List;


/**
 * A container object that stores current state of the problem. It can be serialized.
 * 
 * @author asilkaratas
 *
 */
public class Problem implements Serializable {

	private static final long serialVersionUID = 740571992017479001L;

	private final Tile[] tiles;
	private final int backtrack;
	private final int boardWidth;
	private final int[] tileCounts;
	private final int tileCount;
	private final int maxTileHeight;

	public Problem(Tile[] tiles, int[] tileCounts, int boardWidth, int backtrack) {
		this.tiles = tiles;
		this.backtrack = backtrack;
		this.boardWidth = boardWidth;
		this.tileCounts = tileCounts;
		this.tileCount = calculateTileCount();
		this.maxTileHeight = calculateMaxTileHeight();
	}

	private int calculateTileCount() {
		int totalCount = 0;
		for(int count : tileCounts) {
			totalCount += count;
		}
		return totalCount;
	}

	/**
	 * Calculates max tile height
	 * @return max tile height
	 */
	private int calculateMaxTileHeight() {
		int maxHeight = 0;
		for(final Tile tile : tiles) {
			if(tileCounts[tile.getId()] > 0) {
				for(final Bitmap bitmap : tile.getBitmaps()) {
					if(bitmap.getHeight() > maxHeight) {
						maxHeight = bitmap.getHeight();
					}
				}
			}
		}
		return maxHeight;
	}

	public int getTileCount() {
		return tileCount;
	}
	
	public int getBacktrack() {
		return backtrack;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	public int[] getTileCounts() {
		return tileCounts;
	}

	public int getBoardWidth() {
		return boardWidth;
	}

	public int getMaxTileHeight() {
		return maxTileHeight;
	}
}
