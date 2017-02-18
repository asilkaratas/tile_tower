package ac.tiletower.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ac.tiletower.algorithm.Bitmap;
import ac.tiletower.algorithm.Tile;

/**
 * Validates if there are duplicated bitmaps.
 * @author asilkaratas
 *
 */
public class TileValidator {
	
	/**
	 * Checks if there are duplicated tiles in the given list.
	 * @param tiles Tiles to validate.
	 * @throws RepeatedTileException In case of duplicated tiles, exception thrown.
	 */
	public void validate(final Tile[] tiles) throws RepeatedTileException {
		final List<Tile> allTiles = new ArrayList<>();
		allTiles.addAll(Arrays.asList(tiles));
		
		final List<List<Tile>> allRepeatedTiles = new ArrayList<List<Tile>>();
		for(int i = 0; i < allTiles.size(); i++) {
			final Tile tile = allTiles.get(i);
			final List<Tile> repeatedTiles = findRepeatedTiles(tile, allTiles);
			if(!repeatedTiles.isEmpty()) {
				allTiles.removeAll(repeatedTiles);
				repeatedTiles.add(tile);
				allRepeatedTiles.add(repeatedTiles);
			}
		}
		
		if(!allRepeatedTiles.isEmpty()) {
			throw new RepeatedTileException(allRepeatedTiles);
		}
	}
	
	/**
	 * Finds duplications of a tile in the list.
	 * @param tileToFind Tile to find.
	 * @param tiles Tiles to check.
	 * @return
	 */
	private List<Tile> findRepeatedTiles(final Tile tileToFind, final List<Tile> tiles) {
		final List<Tile> repeatedTiles = new ArrayList<>();
		for(final Tile tile : tiles) {
			if(tile != tileToFind) {
				for(final Bitmap bitmapToFind : tileToFind.getBitmaps()) {
					for(final Bitmap bitmap : tile.getBitmaps()) {
						if(bitmap.equals(bitmapToFind)) {
							repeatedTiles.add(tile);
						}
					}
				}
			}
		}
		return repeatedTiles;
	}
}
