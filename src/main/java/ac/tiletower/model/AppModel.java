package ac.tiletower.model;

import ac.tiletower.algorithm.Problem;
import ac.tiletower.algorithm.Tile;
import ac.tiletower.io.InputData;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 * Created by asilkaratas on 12/3/16.
 */
public class AppModel extends Observable implements Serializable {

    public static final String ZOOM = "zoom";
    public static final String START = "start";
    public static final String BACKTRACK = "backtrack";
    public static final String SELECTED_BOARD = "selectedBoard";
    public static final String RESET_GRID_POSITION = "resetGridPosition";

    private InputData inputData;
    private int[] tileCounts;
    private int zoom;
    private int defaultZoom;
    private ControlState controlState;
    private int backtrack;
    private boolean followBoardHeight;
    private int selectedBoard;

    public AppModel() {
        backtrack = 1;
        controlState = ControlState.CLEAR;
        defaultZoom = 6;
        zoom = defaultZoom;
        followBoardHeight = false;
    }

    public InputData getInputData() {
        return inputData;
    }

    public void setInputData(InputData inputData) {
        this.inputData = inputData;
        this.tileCounts = new int[inputData.getTiles().length];
        Arrays.fill(tileCounts, 1);
    }

    public int[] getTileCounts() {
        return tileCounts;
    }

    public void copyFrom(final AppModel appModel) {
        inputData = appModel.inputData;
        tileCounts = appModel.tileCounts;
        backtrack = appModel.getBacktrack();
        zoom = appModel.getZoom();
        setControlState(ControlState.PAUSED);
        resetGridPosition();
    }

    public Problem createProblem() {
        return new Problem(inputData.getTiles(), tileCounts, inputData.getBoardWidth(), backtrack);
    }

    public void resetGridPosition() {
        setZoom(defaultZoom);
        setChanged();
        notifyObservers(RESET_GRID_POSITION);
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;

        setChanged();
        notifyObservers(ZOOM);
    }

    public ControlState getControlState() {
        return controlState;
    }

    public void setControlState(ControlState controlState) {
        this.controlState = controlState;

        setChanged();
        notifyObservers();
    }

    public int getBacktrack() {
        return backtrack;
    }

    public void setBacktrack(int backtrack) {
        this.backtrack = backtrack;

        setChanged();
        notifyObservers(BACKTRACK);
    }

    public boolean isFollowBoardHeight() {
        return followBoardHeight;
    }

    public void setFollowBoardHeight(boolean followBoardHeight) {
        this.followBoardHeight = followBoardHeight;

        setChanged();
        notifyObservers();
    }

    public boolean showAllBoards() {
        return selectedBoard == 0;
    }

    public void setSelectedBoard(final int selectedBoard) {
        this.selectedBoard = selectedBoard < 0 ? 0 : selectedBoard;

        setChanged();
        notifyObservers(SELECTED_BOARD);
    }

    public int getSelectedBoard() {
        return selectedBoard;
    }
}
