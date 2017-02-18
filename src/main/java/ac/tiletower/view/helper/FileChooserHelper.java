package ac.tiletower.view.helper;

import java.io.File;

import javafx.stage.FileChooser;

/**
 * General purpose file chooser helper.
 * 
 * @author asilkaratas
 *
 */
public class FileChooserHelper {

	public static FileChooser createInputFileChooser(String title) {
		return createFileChooser(title, "Input File", "*.*");
	}
	
	public static FileChooser createAppDataChooser(String title) {
		return createFileChooser(title, "ac.tiletower.App Data", "*.*");
	}
	
	private static FileChooser createFileChooser(String title, String fileType, String extention) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File("."));  
        
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(fileType, extention)
        );
		
        return fileChooser;
	}
}
