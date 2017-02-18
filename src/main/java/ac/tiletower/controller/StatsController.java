package ac.tiletower.controller;

import java.util.Observable;
import java.util.Observer;

import ac.tiletower.App;
import ac.tiletower.algorithm.ProblemSolverThread;
import ac.tiletower.algorithm.StatsInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Shows simple statistical information for current problem.
 * @author asilkaratas
 *
 */
public class StatsController {

	@FXML Label stepLabel;
	@FXML Label tileCountLabel;
	@FXML Label tileRemainedLabel;
	@FXML Label tileTypeLabel;
	@FXML Label densityLabel;
	@FXML Label timeLabel;
	@FXML Label peekDensityLabel;

	private StatsInfo statsInfo;

	public StatsController() {
		statsInfo = null;
	}
	

	public void setApp(App app) {
		app.getProblemSolverThread().setStatsListener(new ProblemSolverThread.StatsListener() {
			@Override
			public void onStats(StatsInfo info) {
				statsInfo = info;

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						updateUI();
					}
				});
			}
		});
	}

	private void updateUI() {
		if(statsInfo == null) return;

		stepLabel.setText(String.valueOf(statsInfo.getCurrentStep()-1));
		densityLabel.setText(String.format("%.3f", statsInfo.getBestDensity()));
		timeLabel.setText(String.format("%.3f s", ((double)statsInfo.getTotalTime()/1000)));
		tileCountLabel.setText(String.valueOf(statsInfo.getTotalSteps()));
		peekDensityLabel.setText(String.format("%.3f", statsInfo.getPeekDensity()));
		tileTypeLabel.setText(String.valueOf(statsInfo.getTotalTypes()));
		int tileRemained = 0;
		if(statsInfo.getCurrentStep() > 0) {
			tileRemained = statsInfo.getTotalSteps() - statsInfo.getCurrentStep() +1;
		}
		tileRemainedLabel.setText(String.valueOf(tileRemained));
	}
	
}
