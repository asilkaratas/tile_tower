
package ac.tiletower.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * General purpose AlertWindow.
 * @author asilkaratas
 *
 */
public class AlertWindow extends VBox {
	
	public AlertWindow(String message) {
		getStyleClass().add("alert-window");
		setAlignment(Pos.TOP_CENTER);
		setSpacing(10);
		
		TextArea textArea = new TextArea(message);
		ScrollPane scrollPane = new ScrollPane(textArea);
		
		Button okButton = new Button("Ok");
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				Stage stage = (Stage)getScene().getWindow();
				stage.close();
			}
		});
		
		getChildren().addAll(scrollPane, okButton);
	}
	
}
