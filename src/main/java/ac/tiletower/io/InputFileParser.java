package ac.tiletower.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import ac.tiletower.algorithm.Tile;

/**
 * Default implementation of InputFileParser interface. 
 * It parses input data from given File object.
 * 
 * @author asilkaratas
 *
 */
public class InputFileParser {

	private Tile parseTile(final BufferedReader reader, final int id, final int width, final int height)
			throws IOException, InputFileParserException {
		final int[] data = new int[width*height];

		for(int y = 0; y < height; y++) {
			final String line = reader.readLine();
			if(line.isEmpty()) {
				throw new IOException("Wrong tile height!");
			}
			final int[] values = parseToIntArray(line);
			if(values.length != width) {
				throw new IOException("Wrong tile width!");
			}
			for(int x = 0; x < width; x++) {
				final int index = y * width + x;
				data[index] = values[x];
			}
		}

		final Tile tile = new Tile(id, width, height, data);
		return tile;
	}

	public InputData parse(File file) throws InputFileParserException {
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new InputFileParserException(e.getMessage());
		}
		
		int boardWidth = 0;
		int tileCount = -1;
		int tileId = 0;
		final LinkedList<Tile> tiles = new LinkedList<>();
		
		boolean firstLine = true;
		String line;
		
		try {
			while((line = reader.readLine()) != null && !line.isEmpty()) {
				if(firstLine) {
					firstLine = false;
					final int[] values = parseToIntArray(line);
					boardWidth = values[0];
					if(values.length == 2) {
						tileCount = values[1];
					}
				} else {
					final int[] values = parseToIntArray(line);
					if(values.length != 2 || values[0] <= 0 || values[1] <= 0) {
						throw new InputFileParserException("Wrong tile width/height!");
					}

					final Tile tile = parseTile(reader, tileId, values[0], values[1]);
					tiles.add(tile);
					tileId++;
				}
			}

			reader.close();
		} catch (IOException e) {
			throw new InputFileParserException(e.getMessage());
		}
		
		if(tileCount != -1 && tileCount != tiles.size()) {
			throw new InputFileParserException("Tile count does not match!");
		}

		final InputData inputData = new InputData(boardWidth, tiles.toArray(new Tile[tiles.size()]));
		return inputData;
	}
	
	private int[] parseToIntArray(String line) throws InputFileParserException {
		final String[] stringValues = line.split(" ");
		final int[] intValues = new int[stringValues.length];
		
		int index = 0;
		
		try {
			for(final String stringValue : stringValues) {
				int intValue = Integer.valueOf(stringValue);
				intValues[index] = intValue;
				
				index++;
			}
		} catch (NumberFormatException e) {
			throw new InputFileParserException("Int parse error:" + e.getMessage());
		}
		
		return intValues;
	}

}
