package ac.tiletower.algorithm;

import java.util.LinkedList;

import org.apache.log4j.Logger;

/**
 * Obtains a board for a solver to solve.
 * 
 * @author asilkaratas
 *
 */
public class BoardSolverThread extends Thread {
	private static final Logger logger = Logger.getLogger(BoardSolverThread.class);

	private final BoardStore boardStore;
	
	public BoardSolverThread(final String name, final BoardStore boardStore) {
		super(name);
		setDaemon(true);
		setPriority(MAX_PRIORITY);
		this.boardStore = boardStore;
	}

	/**
	 * Gets a board then gives to solver. It adds returned best placements to BoardStore.
	 */
	@Override
	public void run() {
		while(true) {
			final Board board = boardStore.getBoard();
			final double bestLowestScore = boardStore.getBestLowestScore();
			final LinkedList<Placement> placements = board.nextStep(bestLowestScore);
			boardStore.addPlacements(placements);
		}
	}

}
