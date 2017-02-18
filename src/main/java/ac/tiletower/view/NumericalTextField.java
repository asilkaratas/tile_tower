package ac.tiletower.view;

import org.apache.log4j.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * A text field that can have min and max numerical value.
 * 
 * @author asilkaratas
 *
 */
public class NumericalTextField extends TextField {
	private static final Logger logger = Logger.getLogger(NumericalTextField.class);
	
	private int minValue;
	private int maxValue;
	
	public NumericalTextField() {
		super();
		
		textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				int intValue = minValue;
				
				try {
            		intValue = Integer.valueOf(newValue);
            	} catch(NumberFormatException e) {}
				
				
				if(intValue < minValue) {
					intValue = minValue;
            	} else if(intValue > maxValue) {
            		intValue = maxValue;
            	}
				
				setText(String.valueOf(intValue));
				positionCaret(getText().length());
			}
		});
	}
	
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	
}
