package ac.tiletower.algorithm;

import ac.tiletower.algorithm.BestPlacements;
import ac.tiletower.algorithm.Placement;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by asilkaratas on 12/1/16.
 */
public class BestPlacementsTest {

    @Test
    public void testAdd() {
        final BestPlacements bestPlacements = new BestPlacements(4);
        bestPlacements.add(new Placement(null, null, 0, 2d));
        assertEquals(2d, bestPlacements.getBestLowestScore(), TestUtils.DELTA);

        bestPlacements.add(new Placement(null, null, 0, 10d));
        assertEquals(2d, bestPlacements.getBestLowestScore(), TestUtils.DELTA);

        bestPlacements.add(new Placement(null, null, 0, 1d));
        assertEquals(1d, bestPlacements.getBestLowestScore(), TestUtils.DELTA);

        bestPlacements.add(new Placement(null, null, 0, 0.8d));
        assertEquals(0.8d, bestPlacements.getBestLowestScore(), TestUtils.DELTA);

        bestPlacements.add(new Placement(null, null, 0, 0.5d));
        assertEquals(0.8d, bestPlacements.getBestLowestScore(), TestUtils.DELTA);

        bestPlacements.add(new Placement(null, null, 0, 15d));
        assertEquals(1d, bestPlacements.getBestLowestScore(), TestUtils.DELTA);

        bestPlacements.add(new Placement(null, null, 0, 12d));
        assertEquals(2d, bestPlacements.getBestLowestScore(), TestUtils.DELTA);
    }
}
