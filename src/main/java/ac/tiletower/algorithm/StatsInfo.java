package ac.tiletower.algorithm;

/**
 * Created by asilkaratas on 12/5/16.
 */
public class StatsInfo {
    private final double bestDensity;
    private final double peekDensity;
    private final long totalTime;
    private final int currentStep;
    private final int totalSteps;
    private final int totalTypes;

    public StatsInfo(final Solution solution) {
        final Board bestBoard = solution.getBestBoard();
        bestDensity = bestBoard.getDensity();
        peekDensity = solution.getPeekDensity();
        totalTime = solution.getTotalTime();
        currentStep = solution.getCurrentStep();
        totalSteps = solution.getProblem().getTileCount();
        totalTypes = solution.getProblem().getTiles().length;
    }

    public double getBestDensity() {
        return bestDensity;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public int getTotalTypes() {
        return totalTypes;
    }

    public double getPeekDensity() {
        return peekDensity;
    }
}
