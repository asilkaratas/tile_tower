package ac.tiletower.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;


/**
 * When user wants to solve a solution it comes to here.
 * This class is responsible to synchronize steps and process best placements.
 * 
 * BoardSolverThreads sends list of their best placements to this class. It chooses the best placements 
 * between all other threads and prepares the next step.
 * 
 * @author asilkaratas
 *
 */
public class ProblemSolverThread extends Thread {
	private static final Logger logger = Logger.getLogger(ProblemSolverThread.class);
	
	private final Lock stepLock = new ReentrantLock(true);
	private final Condition stepFinished = stepLock.newCondition();
	
	private final BoardStore boardStore;
	private final LinkedList<BoardSolverThread> solverThreads;

	private StatsListener statsListener;
	private DrawListener drawListener;
	private StopListener stopListener;
	private BestPlacements bestPlacements;
	private Solution solution;

	private int step;
	private int maxStep;
	private int stopStep;

	private long timeStamp;
	private long stepTime;
	private long fpsTime;
	private final long fpsTimeMax = 100;
	
	public ProblemSolverThread(int threadCount) {
		super("ProblemSolverThread");
		setDaemon(true);
		setPriority(MAX_PRIORITY);

		boardStore = new BoardStore();
		bestPlacements = null;
		statsListener = null;
		drawListener = null;
		stopListener = null;
		solution = null;
		
		solverThreads = new LinkedList<>();
		for(int i = 0; i < threadCount; i++) {
			BoardSolverThread solverThread = new BoardSolverThread("BoardSolverThread" + i, boardStore);
			solverThreads.add(solverThread);
		}
	}
	
	public void goToStep(int stepCount) {
		stepLock.lock();
		setStopStep(step + stepCount);
		stepFinished.signal();
		stepLock.unlock();
	}

	private void setStopStep(int stopStep) {
		if(stopStep > maxStep) {
			stopStep = maxStep;
		}
		this.stopStep = stopStep;
	}
	
	/**
	 * Goes next step
	 */
	public void nextStep() {
		stepLock.lock();
		setStopStep(step + 1);
		stepFinished.signal();
		stepLock.unlock();
	}
	
	/**
	 * Resumes play
	 */
	public void resumePlay() {
		stepLock.lock();
		setStopStep(maxStep);
		stepFinished.signal();
		stepLock.unlock();
	}
	
	/**
	 * Stops play
	 */
	public void stopPlay() {
		stepLock.lock();
		setStopStep(step);
		stepLock.unlock();
	}

	private boolean hasJustStopped() {
		return step == stopStep;
	}
	private boolean hasNextStep() {
		return step < maxStep;
	}
	/**
	 * Each step is waited for this method.
	 */
	private void waitForStep() {
		stepLock.lock();
		while(solution == null || step >= stopStep) {
			try {
				//logger.info("waitStep waiting");
				stepFinished.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		step++;
		stepLock.unlock();
	}
	
	/**
	 * Loop of thread.
	 */
	@Override
	public void run() {
		for(BoardSolverThread solverThread : solverThreads) {
			solverThread.start();
		}
		logger.info("run");
		while(true) {
			//logger.info("waiting:");

			waitForStep();
			beforeStep();
			calculateBestPlacements();
			afterStep();
		}
	}
	
	private void beforeStep() {
		timeStamp = System.currentTimeMillis();
		
		LinkedList<Board> workingBoards = new LinkedList<>();
		workingBoards.addAll(solution.getBoards());
		boardStore.setBoards(workingBoards);
	}
	
	/**
	 * Calculates best placements for one step.
	 */
	private void calculateBestPlacements() {

		int count = 0;
		while(true) {
			LinkedList<Placement> placements = boardStore.getPlacements();
			for(Placement placement : placements) {
				bestPlacements.add(placement);
			}
			count++;
			
			// After calculating best scores for all boards it quits. 
			// Prepares next step.
			if(count == boardStore.getBoardCount()) {
				break;
			} else {
				boardStore.setBestLowestScore(bestPlacements.getBestLowestScore());
			}
		}
		
		// Solves conflicts and arranges boards.
		arrangeBoards(solution.getBoards(), bestPlacements.toLinkedList());
		bestPlacements.clear();
	}


	/**
	 * It is called after each step to calculate statistics.
	 */
	private void afterStep() {
		stepTime = System.currentTimeMillis() - timeStamp;

		updateSolutionStats();
		if(checkFps()) {
			updateDraw();
			updateStats();
		}
		checkStop();
	}

	private void updateSolutionStats() {
		solution.addDensitySeries();
		solution.setCurrentStep(step);
		solution.setTotalTime(solution.getTotalTime() + stepTime);
	}

	private boolean checkFps() {
		// FpsLimitter limits updates of user interface. It is default 20fps
		// It is updated end of each continuous running.
		fpsTime -= stepTime;
		if(fpsTime <= 0 || hasJustStopped()) {
			fpsTime = fpsTimeMax;
			//logger.info("Buffers!");

			return true;
		}
		return false;
	}

	private void updateDraw() {
		if(drawListener != null) {
			final DrawInfo drawInfo = new DrawInfo(solution.getBoards());
			drawListener.onDraw(drawInfo);
		}
	}

	private void updateStats() {
		if(statsListener != null) {
			final StatsInfo statsInfo = new StatsInfo(solution);
			statsListener.onStats(statsInfo);
		}
	}

	/**
	 * Checks if it is the last step of the solution. It calls stop listener.
	 */
	private void checkStop() {
		if(!hasNextStep()) {
			if(stopListener != null) {
				stopListener.onStop();
				System.out.println("STOPPED! density:" + solution.getBestBoard().getDensity());
			}
		}
	}
	
	
	/**
	 * 
	 * @return the current solution
	 */
	public Solution getSolution() {
		return this.solution;
	}
	
	/**
	 * Sets solution and resets other object for beginning of calculation.
	 * @param solution A new solution.
	 */
	public void setSolution(Solution solution) {
		this.solution = solution;

		step = solution.getCurrentStep();
		maxStep = solution.getProblem().getTileCount() + 1;
		fpsTime = fpsTimeMax;

		bestPlacements = new BestPlacements(solution.getProblem().getBacktrack());

		solution.calculateBestBoard();
		updateDraw();
		updateStats();
	}

	/**
	 * Solves a problem
	 * @param problem to be solved
	 */
	public void solve(final Problem problem) {
		final Solution solution = new Solution(problem);
		setSolution(solution);
	}

	/**
	 * Arranges boards
	 * @param boards to be arranged
	 * @param bestPlacements boards will be arranged according to best placements
	 */
	public void arrangeBoards(final List<Board> boards, final List<Placement> bestPlacements) {
		final List<Placement> conflictPlacements = new ArrayList<>();
		final List<Board> usedBoards = new ArrayList<>();
		for(Placement placement : bestPlacements) {
			if(usedBoards.contains(placement.getBoard())) {
				conflictPlacements.add(placement);
			} else {
				usedBoards.add(placement.getBoard());
			}
		}

		final List<Board> freeBoards = new ArrayList<>();
		for(Board board : boards) {
			if(!usedBoards.contains(board)) {
				freeBoards.add(board);
			}
		}

		for(int i = 0; i < conflictPlacements.size(); i++) {
			final Placement placement = conflictPlacements.get(i);
			final Board freeBoard = freeBoards.get(i);
			freeBoard.copyFrom(placement.getBoard());
			placement.setBoard(freeBoard);
		}
		//logger.info(conflictPlacements.size() + " board copied!");
		final int maxRowFillsY =  findMaxRowFillsY(bestPlacements);

		for(Placement placement : bestPlacements) {
			final Board board = placement.getBoard();
			board.setWaitingPlacement(placement);
			board.setMaxRowFillsY(maxRowFillsY);
		}

	}

	/**
	 * Finds max row fills y position
	 * @param bestPlacements
	 * @return
	 */
	private int findMaxRowFillsY(List<Placement> bestPlacements) {
		int maxRowFillsY = 0;
		for(Placement placement : bestPlacements) {
			if(placement.getRowFillsY() > maxRowFillsY) {
				maxRowFillsY = placement.getRowFillsY();
			}
		}
		return maxRowFillsY;
	}

	public void setStatsListener(StatsListener statsListener) {
		this.statsListener = statsListener;
	}

	public void setStopListener(StopListener stopListener) {
		this.stopListener = stopListener;
	}

	public void setDrawListener(DrawListener drawListener) {
		this.drawListener = drawListener;
	}

	public interface StopListener {
		void onStop();
	}

	public interface DrawListener {
		void onDraw(final DrawInfo drawInfo);
	}

	public interface StatsListener {
		void onStats(final StatsInfo statsInfo);
	}
}
