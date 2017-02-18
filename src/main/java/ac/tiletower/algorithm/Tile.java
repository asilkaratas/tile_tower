package ac.tiletower.algorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Tile Stores original tile bitmap and rotation of this bitmap
 */
public class Tile implements Serializable {

    private final int id;
    private final Bitmap originalBitmap;
    private final Bitmap[] bitmaps;

    public Tile(int id, int width, int height, int[] data) {
        this.id = id;
        originalBitmap = new Bitmap(width, height, data);
        bitmaps = createRotatedBitmaps();
    }

    /**
     * Creates rotated bitmaps
     * @return rotated bitmaps
     */
    private Bitmap[] createRotatedBitmaps() {
        final List<Bitmap> bitmapList = new ArrayList<>();
        bitmapList.add(originalBitmap);
        Bitmap lastBitmap = originalBitmap;

        for(int i = 2; i >= 0; i--) {
            final Bitmap rotatedBitmap = lastBitmap.rotate();
            if(!bitmapList.contains(rotatedBitmap)) {
                bitmapList.add(rotatedBitmap);
            }
            lastBitmap = rotatedBitmap;
        }
        return bitmapList.toArray(new Bitmap[bitmapList.size()]);
    }

    /**
     * 
     * @return original bitmap
     */
    public Bitmap getOriginalBitmap() {
        return originalBitmap;
    }

    /**
     * 
     * @return all bitmaps including original bitmap
     */
    public Bitmap[] getBitmaps() {
        return bitmaps;
    }

    /**
     * 
     * @return unique id of bitmap. It is used for retrieving amount of tile.
     */
    public int getId() {
        return id;
    }
}
