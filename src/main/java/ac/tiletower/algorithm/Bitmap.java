package ac.tiletower.algorithm;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Bitmap class stores rotated tile information. 
 * 
 * @author asilkaratas
 *
 */
public final class Bitmap implements Serializable {
	
	private final int[][] data;
	private final int width;
	private final int height;
	private final int[] heights;
	private final int[] holes;
	private final int filled;
	private final int[] rowFills;
	private final int middleHoles;

	public Bitmap(final int width, final int height, final int[] data) {
		this.width = width;
		this.height = height;
		this.data = new int[height][width];
		this.heights = new int[width];
		this.holes = new int[width];
		this.rowFills = new int[height];
		this.filled = copyData(data);

		calculateHeights();
		calculateHoles();
		calculateRowFills();
		this.middleHoles = calculateMiddleHoles();
	}
	
	/**
	 * Returns width of the bitmap.
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns height of the bitmap.
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	public int[] getHeights() {
		return heights;
	}

	public int[] getHoles() {
		return holes;
	}

	public int[] getRowFills() {
		return rowFills;
	}

	public int getFilled() {
		return filled;
	}

	public int getMiddleHoles() {
		return middleHoles;
	}

	private int copyData(final int[] data) {
		int filledCount = 0;
		for(int y = height-1; y >= 0; y--) {
			for(int x = width-1; x >= 0; x--) {
				int index = (height-y-1)*width + x;
				int value = data[index];
				if(value != 0) {
					this.data[y][x] = value;
					filledCount++;
				}
			}
		}
		return filledCount;
	}

	private int copyData(final int[][] data) {
		int filledCount = 0;
		for(int y = height-1; y >= 0; y--) {
			for(int x = width-1; x >= 0; x--) {
				int value = data[y][x];
				if(value != 0) {
					this.data[y][x] = value;
					filledCount++;
				}
			}
		}
		return filledCount;
	}

	public int getValue(final int x, final int y) {
		return data[y][x];
	}
	
	private void calculateHeights() {
		for(int x = 0; x < width; x++) {
			for(int y = height-1; y >= 0; y--) {
				if(data[y][x] != 0) {
					heights[x] = y+1;
					break;
				}
			}
		}
	}

	private void calculateHoles() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(data[y][x] != 0) {
					holes[x] = y;
					break;
				}
			}
		}
	}

	private void calculateRowFills() {
		for(int y = 0; y < height; y++) {
			int count = 0;
			for(int x = 0; x < width; x++) {
				if(data[y][x] != 0) {
					count++;
				}
			}
			rowFills[y] = count;
		}
	}

	private int calculateMiddleHoles() {
		final int[] columnFills = calculateColumnFills();
		int count = 0;
		for(int x = 0; x < width; x++) {
			count += (heights[x] - columnFills[x] -holes[x]);
		}

		return count;
	}

	private int[] calculateColumnFills() {
		final int[] columnFills = new int[width];
		for(int x = 0; x < width; x++) {
			int count = 0;
			for(int y = 0; y < height; y++) {
				if(data[y][x] != 0) {
					count++;
				}
			}
			columnFills[x] = count;
		}
		return columnFills;
	}

	public Bitmap rotate() {
		final int[] rotatedData = new int[width * height];
		for(int y = height-1; y >= 0; y--) {
			for(int x = width-1; x >= 0; x--) {
				final  int index = (x) * height + y;
				rotatedData[index] = data[y][x];
			}
		}
		final Bitmap rotatedBitmap = new Bitmap(height, width, rotatedData);
		return rotatedBitmap;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Bitmap bitmap = (Bitmap) o;

		if (width != bitmap.width) return false;
		if (height != bitmap.height) return false;
		if (filled != bitmap.filled) return false;
		if (!Arrays.deepEquals(data, bitmap.data)) return false;
		if (!Arrays.equals(heights, bitmap.heights)) return false;
		return Arrays.equals(holes, bitmap.holes);

	}

	@Override
	public int hashCode() {
		int result = Arrays.deepHashCode(data);
		result = 31 * result + width;
		result = 31 * result + height;
		result = 31 * result + Arrays.hashCode(heights);
		result = 31 * result + Arrays.hashCode(holes);
		result = 31 * result + filled;
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Bitmap\n");
		for (int y = height-1; y >= 0; y--) {
			for (int x = width-1; x >= 0; x--) {
				builder.append(data[y][width-x-1]);
				builder.append(' ');
			}
			builder.append('\n');
		}
		return builder.toString();
	}
}
