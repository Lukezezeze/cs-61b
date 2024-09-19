package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }
    /**
    1. findNextAvailableRow(int col, int startRow)
       作用：确定给定列中一个方块可以移动到的目标行，或者可以合并的行。
       逻辑：
     从 startRow 向上开始检查列中的每一行。
     找到第一个空位置，或者找到可以合并的方块。
     如果找到可合并的方块（相同数值），则返回该行。
     如果找到空位置且没有其他可合并的方块，则返回最近的空位置//这个函数没用了，思想可以学习

     */
    public int findNextAvailableRow(int col, int startRow) {
        int index = startRow;
        // Start looking from the row just above the startRow
        for (int row = startRow + 1; row < board.size(); row++) {
            Tile currentTile = board.tile(col, row);
            if (currentTile == null) {
                // Found an empty space, update the target row
                index = row;
            } else if (currentTile.value() == board.tile(col, startRow).value()) {
                // Found a tile with the same value, can merge here
                return row;
            } else {
                // Found a different tile, no more moves possible
                break;
            }
        }
        return index;
    }

    /**
     * findTargetRow 方法的逻辑
     * 初始化目标行: 从当前方块所在的行开始向上搜索，targetRow 初始化为 row + 1，表示开始检查上方的第一行。
     *
     * 查找第一个空位: 使用 while 循环从 targetRow 开始，一直向上查找直到找到一个非空的方块或者到达边界:
     *
     * 如果当前行 targetRow 是空的（board.tile(col, targetRow) == null），继续向上移动，直到找到一个非空的方块或达到边界。
     * 检查合并: 一旦找到一个非空的方块，检查它是否与当前方块的值相同且没有合并过:
     *
     * 如果可以合并（board.tile(col, targetRow).value() == board.tile(col, row).value()）并且这一行没有被合并过（!merged[targetRow]），
     * 返回这个 targetRow 作为可以合并的目标行。
     * 如果不能合并，返回 targetRow - 1 作为最后一个可放置的位置（空位）。
     * 返回目标行:
     *
     * 返回的行要么是一个可以合并的行，要么是最后一个空位所在的行。
     * @param col
     * @param row
     * @param merged
     * @return
     */
    private int findTargetRow(int col, int row, boolean[] merged) {
        int targetRow = row + 1;
        while (targetRow < board.size() && board.tile(col, targetRow) == null) {
            targetRow++;
        }
        // 检查能否合并
        if (targetRow < board.size() && board.tile(col, targetRow).value() == board.tile(col, row).value() && !merged[targetRow]) {
            return targetRow; // 可以合并
        }
        return targetRow - 1; // 不能合并，返回最后一个空位
    }

    /**
     * processColumn(int col)
     * 作用：处理一列中的所有方块，将它们移动到正确的位置或进行合并。
     * 逻辑：
     * 从上到下遍历该列中的每一行。
     * 对每个非空方块，调用 findNextAvailableRow 来确定其目标行。
     * 使用 moveTile 方法将方块移动或合并到目标位置。
     *
     */
    public boolean process(int col) {
        boolean changed = false;
        boolean[] merged = new boolean[board.size()]; // 记录哪些位置已经合并过

        // 从第二行开始向上处理（从下往上）
        for (int row = board.size() - 2; row >= 0; row--) {
            Tile currentTile = board.tile(col, row);
            if (currentTile != null) {
                int targetRow = findTargetRow(col, row, merged);

                // 进行移动或合并
                if (targetRow != row) {
                    boolean mergedTile = board.move(col, targetRow, currentTile);
                    if (mergedTile) {
                        score += board.tile(col, targetRow).value();
                        merged[targetRow] = true; // 标记合并
                    }
                    changed = true;
                }
            }
        }
        return changed;
    }
    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        board.setViewingPerspective(side);

        for(int col =0;col<board.size();col++){
            boolean columnChanged = process(col);
            if (columnChanged) {
                changed = true;
            }
        }
        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        int i = b.size();
        for(int row=0;row<i;row++){
            for(int column=0;column<i;column++){

                if(b.tile(row,column) == null){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        int i = b.size();
        for(int row=0;row<i;row++){
            for(int column=0;column<i;column++){

                Tile t = b.tile(row,column);

                if( t != null && t.value() == MAX_PIECE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size();
        // 检查是否有空格
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (b.tile(row, col) == null) {
                    return true;
                }
            }
        }

        // 检查相邻方格的值是否相等
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (col < size - 1 && b.tile(row, col).value() == b.tile(row, col + 1).value()) {
                    return true;
                }
                if (row < size - 1 && b.tile(row, col).value() == b.tile(row + 1, col).value()) {
                    return true;
                }
            }
        }
      return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
