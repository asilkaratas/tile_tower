package ac.tiletower.algorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import javafx.scene.paint.Color;

/**
 * Default implementation of a ColorFactory.
 * @author asilkaratas
 *
 */
public class ColorModel {
	
	private static final long serialVersionUID = 4653245952689645622L;
	private List<Color> colors;
	private Color mix;
	
	public ColorModel(Color mix) {
		this.mix = mix;
		colors = new ArrayList<Color>();
	}
	
	/**
	 * Creates colors
	 * @param count amount of colors to be created.
	 */
	public void createColors(int count) {
		colors.clear();
		
		while(count > 0) {
			Color color = getUniqueColor();
			colors.add(color);
			count--;
		}
	}
	
	/**
	 * 
	 * @param index of a color
	 * @return a color at index
	 */
	public Color getColor(int index) {
		if(index < 0 || index > colors.size()) {
			return Color.BLACK;
		}
		
		return colors.get(index);
	}
	
	/**
	 * 
	 * @return a unique color
	 */
	private Color getUniqueColor() {
		Color color = null;
		
		while(true) {
			color = generateRandomColor();
			if(!colors.contains(color)) {
				if(color.getBlue() < 0.9f && color.getGreen() < 0.9f && color.getRed() < 0.9f)
				break;
			}
		}
		
		return color;
	}

	/**
	 * 
	 * @return a random color
	 */
	private Color generateRandomColor() {
	    Random random = new Random();
	    int red = random.nextInt(256);
	    int green = random.nextInt(256);
	    int blue = random.nextInt(256);

	    // mix the color
	    if (mix != null) {
	        red = (red + (int)mix.getRed() * 255) / 2;
	        green = (green + (int)mix.getGreen() * 255) / 2;
	        blue = (blue + (int)mix.getBlue() * 255) / 2;
	    }

	    Color color = new Color(red/255.0, green/255.0, blue/255.0, 1);
	    return color;
	}
}
