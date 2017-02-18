package ac.tiletower.algorithm;

import java.util.*;

/**
 * Stores only best placements.
 */
public class BestPlacements {

    private final int size;
    private final PriorityQueue<Placement> queue;

    private double bestLowestScore;

    /**
     * 
     * @param size Size of the queue. 
     */
    public BestPlacements(int size) {
        this.size = size;

        final Comparator<Placement> comparator = new Comparator<Placement>() {
            @Override
            public int compare(Placement o1, Placement o2) {
                if(o1.getScore() == o2.getScore()) {
                    return 0;
                } else if(o1.getScore() < o2.getScore()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };

        queue = new PriorityQueue<>(size + 1, comparator);
        bestLowestScore = 0d;
    }

    /**
     * Adds placements in to queue. But it does not add if object is already in the list
     * @param placement Placement to add
     */
    public void add(Placement placement) {
        if(queue.contains(placement)) {
            return;
        }

        queue.add(placement);
        if(queue.size() > size) {
            queue.poll();
        }
        bestLowestScore = queue.peek().getScore();
    }

    /**
     * Returns best lowest score
     * @return bestLowestScore
     */
    public double getBestLowestScore() {
        return bestLowestScore;
    }

    /**
     * Clears the queue.
     */
    public void clear() {
        bestLowestScore = 0d;
        queue.clear();
    }

    /**
     * Create a new linked list with items in the queue.
     * @return
     */
    public LinkedList<Placement> toLinkedList() {
        return new LinkedList<>(queue);
    }
}
