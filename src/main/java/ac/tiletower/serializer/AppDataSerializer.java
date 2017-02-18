package ac.tiletower.serializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * Default implementation of AppData serializer. 
 * @author asilkaratas
 *
 */
public class AppDataSerializer {
	private static final Logger logger = Logger.getLogger(AppDataSerializer.class);
	
	public void serialize(AppData appData, String filePath) {
		try {
	         FileOutputStream fileOut = new FileOutputStream(filePath);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(appData);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized into " + filePath);
	      }catch(IOException i) {
	         i.printStackTrace();
	      }
	}
	
	public AppData deserialize(String filePath) {
		AppData appData = null;
		try {
	         FileInputStream fileIn = new FileInputStream(filePath);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         appData = (AppData) in.readObject();
	         
	         in.close();
	         fileIn.close();
	      }catch(IOException i) {
	         i.printStackTrace();
	      }catch(ClassNotFoundException c) {
	         c.printStackTrace();
	      }
		
		return appData;
	}
}
