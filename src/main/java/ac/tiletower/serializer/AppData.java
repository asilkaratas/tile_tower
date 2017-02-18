package ac.tiletower.serializer;

import java.io.Serializable;
import ac.tiletower.algorithm.Solution;
import ac.tiletower.model.AppModel;

/**
 * A container object that stores important information that can be serialized.
 * 
 * @author asilkaratas
 *
 */
public class AppData implements Serializable {

	private static final long serialVersionUID = 5921550469500232200L;
	
	private AppModel appModel;
	private Solution solution;
	
	public AppData() {
	}
	
	public AppModel getAppModel() {
		return appModel;
	}
	
	public void setAppModel(final AppModel appModel) {
		this.appModel = appModel;
	}

	public Solution getSolution() {
		return solution;
	}

	public void setSolution(final Solution solution) {
		this.solution = solution;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("AppData\n"));
		return builder.toString();
	}
	
}
