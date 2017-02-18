package ac.tiletower;

import java.io.File;
import java.util.List;

import ac.tiletower.algorithm.ColorModel;
import ac.tiletower.io.InputData;
import ac.tiletower.io.InputFileParser;
import ac.tiletower.model.AppController;
import ac.tiletower.model.AppModel;
import ac.tiletower.model.ControlState;
import ac.tiletower.serializer.AppDataSerializer;
import ac.tiletower.validator.RepeatedTileException;
import ac.tiletower.validator.TileValidator;
import ac.tiletower.view.DensityChartWindow;
import ac.tiletower.view.helper.WindowHelper;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import ac.tiletower.algorithm.ProblemSolverThread;
import ac.tiletower.io.InputFileParserException;
import ac.tiletower.serializer.AppData;
import ac.tiletower.serializer.AppDataSerializerThread;

/**
 * Facade of the application. It configures other shared object.
 * 
 * @author asilkaratas
 *
 */
public class App {
	
	private static final Logger logger = Logger.getLogger(App.class);
	
	private AppModel appModel;
	private ColorModel colorModel;
	private AppController appController;
	private ProblemSolverThread problemSolverThread;
	private AppDataSerializerThread appDataSerializerThread;
	private final Scene mainScene;

	public App(final Scene mainScene) {
		this.mainScene = mainScene;

		appModel = new AppModel();
		colorModel = new ColorModel(Color.WHITE);

		int threadCount = Runtime.getRuntime().availableProcessors();
		problemSolverThread = new ProblemSolverThread(threadCount);
		getProblemSolverThread().setStopListener(new ProblemSolverThread.StopListener() {

			public void onStop() {
				appModel.setControlState(ControlState.STOPPED);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						showDensityChart();
					}
				});
			}
		});
		appController = new AppController(appModel, colorModel, problemSolverThread);

		appDataSerializerThread = new AppDataSerializerThread(this);
	}
	
	/**
	 * Starts problemSolverThread.
	 */
	public void start() {
		problemSolverThread.start();
		appDataSerializerThread.start();
	}

	public void showDensityChart() {
		if(problemSolverThread.getSolution() != null) {
			final List<Double> densitySeries = problemSolverThread.getSolution().getDensitySeries();
			final DensityChartWindow densityChartWindow = new DensityChartWindow(densitySeries);
			WindowHelper.showModal(densityChartWindow, "Density Chart", mainScene);
		}
	}
	
	/**
	 * 
	 * @return AppModel
	 */
	public AppModel getAppModel() {
		return appModel;
	}

	/**
	 *
	 * @return ColorModel
	 */
	public ColorModel getColorModel() {
		return colorModel;
	}

	/**
	 * @return ProblemSolverThread
	 */
	public ProblemSolverThread getProblemSolverThread() {
		return problemSolverThread;
	}

	/**
	 *
	 * @return AppController
	 */
	public AppController getAppController() {
		return appController;
	}

	/**
	 * Creates a problem object and sets to problemSolverThread.
	 */
	public void prepareProblem() {
		
		problemSolverThread.solve(appModel.createProblem());
	}
	
	/**
	 * Serializes application state in to a file.
	 * @param filePath File to save.
	 */
	public void saveAppData(String filePath) {
		final AppData appData = new AppData();
		appData.setAppModel(appModel);
		appData.setSolution(problemSolverThread.getSolution());

		final AppDataSerializer appDataSerializer = new AppDataSerializer();
		appDataSerializer.serialize(appData, filePath);
		
	}
	
	/**
	 * Loads application state from a file.
	 * @param filePath File to load.
	 */
	public void loadAppData(String filePath) {
		final AppDataSerializer appDataSerializer = new AppDataSerializer();
		final AppData appData = appDataSerializer.deserialize(filePath);
		if(appData != null) {
			appModel.copyFrom(appData.getAppModel());
			
			//colorFactory.createColors(appData.getProblem().getTileCount() + 1);
			problemSolverThread.setSolution(appData.getSolution());
		}
	}
	
	/**
	 * Loades input data from a file, creates tiles and stores in app model.
	 * @param file Input file.
	 * @throws RepeatedTileException Exception contains repeated tiles.
	 * @throws InputFileParserException Exception contains error in input files.
	 */
	public void loadTiles(File file) throws RepeatedTileException, InputFileParserException {
		final InputFileParser inputFileParser = new InputFileParser();
		final InputData inputData = inputFileParser.parse(file);
		final TileValidator tileValidator = new TileValidator();
		tileValidator.validate(inputData.getTiles());
		appModel.setInputData(inputData);
	}
}
