package ac.tiletower.view;

import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * General purpose Pane view. It has border and title. It gives very good visual.
 * @author asilkaratas
 *
 */
public class BorderedTitledPane extends StackPane {

	@FXML Label titleLabel;
	@FXML StackPane container;
	
	private Parent content;
	
	public BorderedTitledPane() {
		content = null;
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/bordered_titled_pane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	@FXML
	public void initialize() {
		container.setMinSize(0, 0);
    	container.setAlignment(Pos.CENTER);
    	container.autosize();
	}
	
	public String getTitle() {
        return textProperty().get();
    }

    public void setTitle(String value) {
        textProperty().set(value);
    }

    public StringProperty textProperty() {
        return titleLabel.textProperty();
    }
    
    public Parent getContent() {
    	return content;
    }
    
    public void setContent(Parent content) {
    	this.content = content;
    	
    	container.getChildren().clear();
    	container.getChildren().add(content);
    	content.getStyleClass().add("bordered-titled-content");
    }

    @FXML
    protected void doSomething() {
        System.out.println("The button was clicked!");
    }

}
