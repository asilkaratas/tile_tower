package ac.tiletower.serializer;

import java.text.SimpleDateFormat;
import java.util.Date;

import ac.tiletower.App;
import ac.tiletower.model.ControlState;

/**
 * Works in the background. Serializes AppData for some time periods.
 * @author asilkaratas
 *
 */
public class AppDataSerializerThread extends Thread {
	
	private final App app;
	private final int sleepTime = 1000 * 60 * 10;

	public AppDataSerializerThread(App app) {
		super("AppDataSerializerThread");
		setDaemon(true);
		this.app = app;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				// sleep for 10 minutes
				sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			trySerialize();
		}
	}
	
	private void trySerialize() {
		if(app.getAppModel().getControlState() == ControlState.RUNNING ||
				app.getAppModel().getControlState() == ControlState.PAUSED) {
			serialize();
		}
	}
	
	private void serialize() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String dateString = format.format(date);
		String filename = String.format("%s_appdata.ser", dateString);
		
		app.saveAppData(filename);
	}
}
