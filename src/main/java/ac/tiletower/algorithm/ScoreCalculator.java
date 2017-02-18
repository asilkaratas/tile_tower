package ac.tiletower.algorithm;

import ac.tiletower.algorithm.Bitmap;
import ac.tiletower.algorithm.PlacementCalculator;

/**
 * Created by asilkaratas on 12/2/16.
 */
public final class ScoreCalculator {
    private final PlacementCalculator placementCalculator;
    private final int[] levelHeights;
    private final int[] levelWidths;

    public ScoreCalculator(final PlacementCalculator placementCalculator) {
        this.placementCalculator = placementCalculator;
        this.levelHeights = new int[placementCalculator.getBoard().getWidth()];
        this.levelWidths = new int[placementCalculator.getBoard().getWidth()];
    }

    /**
     * Calculates score for a bitmap at x position
     * @param bitmap rotation of a tile
     * @param x position of tile
     * @return
     */
    public double calculateScore(final Bitmap bitmap, final int x) {
        boolean started = placementCalculator.startCalculation(bitmap, x);

        if(!started) {
            return 0d;
        }

        final double densityWeight = 2;
        final double heightWeight = 0.1;
        final double holeWeight = 0.015;
        final double smoothWeight = 0.015;
        final double totalWeight = densityWeight + holeWeight + heightWeight + smoothWeight;

        final double densityScore = calculateVerticalFillScore() * densityWeight;
        final double holeScore = calculateHoleScore(bitmap) * holeWeight;
        final double heightScore = calculateHeightScore() * heightWeight;
        final double smoothScore = calculateSmoothScore() * smoothWeight;
        double totalScore = densityScore + heightScore + holeScore + smoothScore;// + 0.1 * (1d/holeScore);// + densityScore/smoothScore;
        totalScore /= totalWeight;

        placementCalculator.resetHeights(bitmap, x);

        return totalScore;
    }

    private double calculateHeightScore() {
        return 1d/(placementCalculator.getHeight() - placementCalculator.getRowFillsY());
    }

    /**
     * Calculates hole score.
     * @param bitmap to calculate
     * @return hole score
     */
    private double calculateHoleScore(final Bitmap bitmap) {
        final int totalHoles = placementCalculator.getTotalHoles() + bitmap.getMiddleHoles();


        final double score = (1d/(1d + totalHoles));
        return score;
    }

    /**
     * This is the most important score calculations. 
     * It does many thing with only one calculation.
     * 
     * maxRowFillsY is used for arranging all boards bounding boxes height.
     * @return verticalFillScore
     */
    private double calculateVerticalFillScore() {
        double score = 0;

        final int boardWidth = placementCalculator.getHeights().length;
        final int maxRowFillsY = placementCalculator.getBoard().getMaxRowFillsY();
        final int rowFillsY = placementCalculator.getBoard().getRowFillsY();
        final int[] rowFills = placementCalculator.getRowFills();
        int rowFillsYDif = maxRowFillsY - rowFillsY;
        if(rowFillsYDif < 0) {
            rowFillsYDif = 0;
        }
        final int rowFillsLength = rowFills.length - rowFillsYDif;
        int scoredCount = 0;
        for(int y = rowFillsYDif; y < rowFills.length; y++) {
            int count = rowFills[y];
            double rowPositionScore = (double)(rowFillsLength - y + rowFillsYDif)/rowFillsLength;
            double rowScore = (double)count/boardWidth *  Math.exp(rowPositionScore);
            score += rowScore;
            if(rowScore > 0) {
                scoredCount ++;
            }
        }

        return score/scoredCount;
    }

    /**
     * Calculates smooth score.
     * 
     * @return smooth score
     */
    private double calculateSmoothScore() {

        final int[] heights = placementCalculator.getHeights();
        final int boardWidth = heights.length;
        int lastHeight = heights[0];
        int levelCount = 0;
        int levelSize = 1;
        int prevHeight = 0;
        int totalHeight = lastHeight;
        for(int i = 1; i < boardWidth; i++) {
            prevHeight = heights[i];
            totalHeight += prevHeight;
            if(prevHeight != lastHeight) {
                levelWidths[levelCount] = levelSize;
                levelHeights[levelCount] = lastHeight;
                levelSize = 1;
                lastHeight = prevHeight;
                levelCount++;
            } else {
                levelSize++;
            }
        }
        levelHeights[levelCount] = prevHeight;
        levelWidths[levelCount] = levelSize;
        levelCount++;

        double averageHeight = (double)totalHeight/boardWidth;
        double score = 0d;
        for(int i = 0; i < levelCount; i++) {
            score += (Math.abs(averageHeight-levelHeights[i]))/levelWidths[i];
        }
        score *= ((double)levelCount/boardWidth);
        return 1d/(1d+score);
    }




}
