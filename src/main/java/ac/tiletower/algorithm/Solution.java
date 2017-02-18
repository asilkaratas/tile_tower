package ac.tiletower.algorithm;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * A container object that stores current state of the problem. It can be serialized.
 * 
 * @author asilkaratas
 *
 */
public class Solution implements Serializable {

	private static final long serialVersionUID = 740571992017479001L;

	private final Problem problem;
	private final LinkedList<Board> boards;

	private int currentStep;
	private long totalTime;
	private Board bestBoard;
	private final List<Double> densitySeries;
	private double peekDensity;

	public Solution(final Problem problem) {
		this.problem = problem;
		this.boards = new LinkedList<>();
		this.currentStep = 0;
		this.totalTime = 0L;
		this.bestBoard = null;
		this.densitySeries = new LinkedList<>();
		this.peekDensity = 0;
		
		for(int i = problem.getBacktrack()-1; i >= 0; i--) {
			final Board board = new Board("Board" + i, problem.getBoardWidth(), problem.getBacktrack(),
					problem.getTiles(), problem.getTileCounts().clone(), problem.getMaxTileHeight());
			boards.add(board);
		}
	}

	/**
	 * Calculates best density for all boards.
	 * @return the best density.
	 */
	public void calculateBestBoard() {
		double bestDensity = 0;
		for(Board board : boards) {
			double density = board.getDensity();
			if(density >= bestDensity) {
				bestDensity = density;
				bestBoard = board;
			}
		}

	}

	/**
	 * 
	 * @return density series
	 */
	public List<Double> getDensitySeries() {
		return densitySeries;
	}

	public void addDensitySeries() {
		calculateBestBoard();
		double density = bestBoard.getDensity();
		if(density > peekDensity) {
			peekDensity = density;
		}
		densitySeries.add(density);
	}

	public double getPeekDensity() {
		return peekDensity;
	}

	public Board getBestBoard() {
		return bestBoard;
	}

	public Problem getProblem() {
		return problem;
	}

	public LinkedList<Board> getBoards() {
		return boards;
	}
	
	public int getCurrentStep() {
		return currentStep;
	}
	
	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}
	
	public long getTotalTime() {
		return totalTime;
	}
	
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
	
	
	
}
