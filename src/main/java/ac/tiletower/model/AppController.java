package ac.tiletower.model;

import java.util.Observable;

import ac.tiletower.App;
import ac.tiletower.algorithm.ColorModel;
import ac.tiletower.algorithm.Problem;
import ac.tiletower.algorithm.ProblemSolverThread;

/**
 * AppController is responsible to process user actions.It coordinates other objects according to user's action.
 * 
 * @author asilkaratas
 *
 */
public class AppController {

	private final AppModel appModel;
	private final ColorModel colorModel;
	private final ProblemSolverThread problemSolverThread;

	public AppController(final AppModel appModel, final ColorModel colorModel, final ProblemSolverThread problemSolverThread) {
		this.appModel = appModel;
		this.colorModel = colorModel;
		this.problemSolverThread = problemSolverThread;
	}
	
	public void start() {
		final Problem problem = appModel.createProblem();
		colorModel.createColors(problem.getTileCount() + 1);
		problemSolverThread.solve(problem);
		appModel.resetGridPosition();
		pause();
		goToNextStep();
	}
	
	public void resume() {
		appModel.setControlState(ControlState.RUNNING);
		problemSolverThread.resumePlay();
	}
	
	public void pause() {
		appModel.setControlState(ControlState.PAUSED);
		problemSolverThread.stopPlay();
	}
	
	public void stop() {
		appModel.setControlState(ControlState.STOPPED);
		problemSolverThread.stopPlay();
	}
	
	public void goToStep(int stepCount) {
		problemSolverThread.goToStep(stepCount);
	}
	
	public void goToNextStep() {
		problemSolverThread.nextStep();
	}

}
