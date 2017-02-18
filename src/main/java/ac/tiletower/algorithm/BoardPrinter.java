package ac.tiletower.algorithm;

/**
 * Created by asilkaratas on 12/3/16.
 */
public class BoardPrinter {

    public void print(final Board board) {
        final int[][] data = new int[board.getHeight()][board.getWidth()];

        Placement lastPlacement = board.getLastPlacement();
        System.out.println("BoardPrinter lastPlacement:" + lastPlacement);
        while (lastPlacement != null) {

            final Bitmap bitmap = lastPlacement.getBitmap();
            //System.out.println("placement x:" + lastPlacement.getX() + " y:" + lastPlacement.getY());

            for(int y = bitmap.getHeight() - 1; y >= 0; y--) {
                for(int x = bitmap.getWidth() -1; x >= 0; x--) {
                    //System.out.println("x:" + x + " y:" + y);
                    data[y + lastPlacement.getY()][x + lastPlacement.getX()] = bitmap.getValue(x, y);
                }
            }
            lastPlacement = lastPlacement.getParent();
        }

        final int boardWidth = board.getWidth();
        final int boardHeight = board.getHeight();
        final StringBuilder stringBuilder = new StringBuilder();
        for(int y = boardHeight-1; y >= 0; y--) {
            for(int x = 0; x < boardWidth; x++) {
                stringBuilder.append(data[y][x]);
                stringBuilder.append(' ');
            }
            stringBuilder.append('\n');
        }

        System.out.println(stringBuilder.toString());
    }
}
