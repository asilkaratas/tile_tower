package ac.tiletower.view;

import javafx.scene.canvas.Canvas;

/**
 * Canvas that is resizable. It is used in GridView.
 * 
 * @author asilkaratas
 *
 */
public class ResizableCanvas extends Canvas {

	public ResizableCanvas() {
		
	}
	
	@Override
    public boolean isResizable() {
      return true;
    }
 
    @Override
    public double prefWidth(double height) {
      return getWidth();
    }
 
    @Override
    public double prefHeight(double width) {
      return getHeight();
    }
}
