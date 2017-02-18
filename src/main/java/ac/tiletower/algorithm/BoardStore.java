package ac.tiletower.algorithm;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * BoardStore is responsible to synchronize BoardSolverThreads and ProblemSolverThread. 
 * 
 * @author asilkaratas
 *
 */
public class BoardStore {
	
	private final Lock boardsLock = new ReentrantLock(true);
	private final Condition boardsIsEmpty = boardsLock.newCondition();
	private final Condition boardsIsNotEmpty = boardsLock.newCondition();
	
	private final Lock placementsListLock = new ReentrantLock(true);
	private final Condition placementsListIsEmpty = placementsListLock.newCondition();
	
	private final LinkedList<Board> boards;
	private final LinkedList<LinkedList<Placement>> placementsList;
	private int boardCount;
	private double bestLowestScore;
	
	public BoardStore() {
		boards = new LinkedList<>();
		placementsList = new LinkedList<>();
		boardCount = 0;
		bestLowestScore = 0d;
	}
	
	/**
	 * Sets boards for consumed for step.
	 * @param boards
	 */
	public void setBoards(LinkedList<Board> boards) {
		boardsLock.lock();
		
		this.boards.addAll(boards);
		boardCount = this.boards.size();
		
		bestLowestScore = 0d;
		
		boardsIsEmpty.signalAll();
		boardsLock.unlock();
	}
	
	/**
	 * 
	 * @return a board from the list.
	 */
	public Board getBoard() {
		Board board = null;
		boardsLock.lock();
		while(boards.size() == 0) {
			try {
				boardsIsEmpty.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		board = boards.poll();
		if(boards.size() == 0) {
			boardsIsNotEmpty.signal();
		}
		boardsLock.unlock();
		
		return board;
	}
	
	/**
	 * Adds placements to be checked by ProblemSolverThread.
	 * @param placements
	 */
	public void addPlacements(LinkedList<Placement> placements) {
		placementsListLock.lock();
		placementsList.add(placements);
		placementsListIsEmpty.signal();
		placementsListLock.unlock();
	}
	
	/**
	 * 
	 * @return 
	 */
	public LinkedList<Placement> getPlacements() {
		LinkedList<Placement> placements = null;
		placementsListLock.lock();
		while(placementsList.size() <= 0) {
			try {
				placementsListIsEmpty.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		placements = placementsList.poll();
		placementsListLock.unlock();
		
		return placements;
	}
	
	public synchronized double getBestLowestScore() {
		return bestLowestScore;
	}
	public synchronized void setBestLowestScore(double bestLowestScore) {
		this.bestLowestScore = bestLowestScore;
	}
	
	public int getBoardCount() {
		return boardCount;
	}
}
