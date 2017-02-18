package ac.tiletower.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * DrawInfo stores shallow information of boards. It is thread safe, it is for GUIThread
 */
public class DrawInfo {

    private final List<BoardInfo> boardInfos;

    /**
     * Copies necessary information into boardInfo from boards.
     * @param boards to be copied into boardInfo
     */
    public DrawInfo(final List<Board> boards) {
        boardInfos = new ArrayList<>();
        for(final Board board : boards) {
            final BoardInfo boardInfo = new BoardInfo(board);
            boardInfos.add(boardInfo);
        }
    }

    /**
     * 
     * @return list of board infos
     */
    public List<BoardInfo> getBoardInfos() {
        return boardInfos;
    }
}
