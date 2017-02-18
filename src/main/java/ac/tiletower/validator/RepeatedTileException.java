package ac.tiletower.validator;

import ac.tiletower.algorithm.Tile;

import java.util.List;

/**
 * After parsing input file data it is time to create Tile objects. After creation of Tile object, 
 * if there are duplicated rotations this exception is fired. It carry information about which
 * tiles are repeating in the input file.
 * @author asilkaratas
 *
 */
public class RepeatedTileException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private List<List<Tile>> allRepeatedTiles;
	
	public RepeatedTileException(List<List<Tile>> allRepeatedTiles) {
		this.allRepeatedTiles = allRepeatedTiles;
	}
	
	public List<List<Tile>> getAllRepeatedTiles() {
		return allRepeatedTiles;
	}
	
	@Override
	public String getMessage() {
		return toString();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("There are %d tile%s repeating!\n", allRepeatedTiles.size(), allRepeatedTiles.size()>0?"s":""));
		int count = 1;
		for(List<Tile> repeatedTiles : allRepeatedTiles) {
			builder.append(String.format("Repeatation:%d\n", count));
			for(Tile tile : repeatedTiles) {
				builder.append(tile.getOriginalBitmap());
				builder.append('\n');
			}
			
			count++;
		}
		return builder.toString();
	}
}
