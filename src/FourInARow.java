import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TicTacToe class implements the interface
 *
 * @author relkharboutly
 * @author Thomas Kwashnak
 * @date 2/12/2022
 */
public class FourInARow implements IGame {

    // The game board and the game status
    private static final int ROWS = 6, COLS = 6; // number of rows and columns
    private final int[][] board = new int[ROWS][COLS]; // game board in 2D array

    public static int COLOR_PLAYER = RED, COLOR_COMPUTER = BLUE;

    private final Random random;

    /**
     * clear board and set current player (???)
     */
    public FourInARow() {
        clearBoard();
        random = new Random();
    }

    /**
     * Clears the board
     */
    @Override
    public void clearBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY;
            }
        }
    }


    @Override
    public void setMove(int player, int location) {
        if(getLocation(location) == EMPTY) {
            setLocation(location,player);
        }
    }

    @Override
    public int getComputerMove() {
        List<Integer> validMoves = new ArrayList<>();

        for(int i = 0; i < ROWS * COLS; i++) {
            if(getLocation(i) == EMPTY) {
                validMoves.add(i);
            }
        }

        //Check if any will win
        for(Integer move : validMoves) {
            if(checkMoveResult(COLOR_COMPUTER,move) != PLAYING) {
                return move;
            }
        }

        //Check if any will lose
        for (Integer move : validMoves) {
            if (checkMoveResult(COLOR_PLAYER, move) != PLAYING) {
                return move;
            }
        }

        List<Integer> bestMoves = new ArrayList<>();
        int bestEval = 0;

        final int COMPUTER_COEFF = 1, PLAYER_COEFF = 2;

        for (Integer move : validMoves) {
            int eval = evaluatePosition(move, COLOR_COMPUTER) * COMPUTER_COEFF + evaluatePosition(move, COLOR_PLAYER) * PLAYER_COEFF;
            if (eval > bestEval) {
                bestEval = eval;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (eval == bestEval) {
                bestMoves.add(move);
            }
        }

        return bestMoves.get(random.nextInt(bestMoves.size()));
    }

    /**
     * Evaluates how good a position is based on how close it is to making a four-in-a-row
     *
     * @param location The location to evaluate, between 0-35
     * @param player   The player to evaluate the location based on
     *
     * @return The locations' evaluation. Higher numbers means the spot is a better move for the specified player, and lower numbers means it is a
     * worse move for the player
     */
    private int evaluatePosition(int location, int player) {

        if (getLocation(location) != EMPTY) {
            return -1;
        }

        int eval = 0;

        for (int d : new int[]{1, COLS - 1, COLS, COLS + 1}) {
            //Check the range
            int length = 0, count = 0;
            boolean goPos = true, goNeg = true;
            for (int i = 0; i < 4; i++) {
                int probePos = getLocation(location + d * i);
                int probeNeg = getLocation(location - d * i);

                if (goPos && (probePos == player || probePos == 0)) {
                    length++;
                    if (probePos == player) {
                        count++;
                    }
                } else {
                    goPos = false;
                }

                if (goNeg && (probeNeg == player || probeNeg == 0)) {
                    length++;
                    if (probeNeg == player) {
                        count++;
                    }
                } else {
                    goNeg = false;
                }
            }
            if (length >= 3) {
                eval += count;
            }
        }

        return eval;
    }

    /**
     * Returns the value at the provided location if it is valid
     *
     * @param location A location number between 0-35
     *
     * @return The value at that given location. Returns -1 if location is not in the correct bounds.
     */
    public int getLocation(int location) {
        return inBounds(location) ? board[location / COLS][location % COLS] : -1;
    }

    /**
     * Whether or not a give location is within the specified bounds
     *
     * @param location The location number in question
     *
     * @return True if the location is within the proper bounds, False if it is not in the bounds
     */
    public boolean inBounds(int location) {
        return location >= 0 && location < ROWS * COLS;
    }

    public void setLocation(int location, int value) {
        if (inBounds(location)) {
            board[location / COLS][location % COLS] = value;
        }
    }

    /**
     * Temporarily makes a move, checks for winner, reverts the move, and returns the result.
     * @param player
     * @param location
     * @return
     */
    private int checkMoveResult(int player, int location) {
        int original = getLocation(location);
        setLocation(location,player);
        int result = checkForWinner();
        setLocation(location,original);
        return result;
    }

    @Override
    public int checkForWinner() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int location = row * COLS + col;
                int value = getLocation(location);
                if (value != EMPTY) {
                    for (int d : new int[]{1, COLS - 1, COLS, COLS + 1}) {
                        for (int i = 0; i < 4; i++) {
                            if (getLocation(location + d * i) != value) {
                                break;
                            } else if (i == 3) {
                                return value == BLUE ? BLUE_WON : RED_WON;
                            }
                        }
                    }
                }
            }
        }

        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                if (board[row][col] == EMPTY) {
                    return PLAYING;
                }
            }
        }
        return TIE;
    }

    /**
     * Print the game board
     */
    public void printBoard() {

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                printCell(board[row][col]); // print each of the cells
                if (col != COLS - 1) {
                    System.out.print("|");   // print vertical partition
                }
            }
            System.out.println();
            if (row != ROWS - 1) {
                System.out.println("-----------------------"); // print horizontal partition
            }
        }
        System.out.println();
    }

    /**
     * Print a cell with the specified "content"
     *
     * @param content either BLUE, RED or EMPTY
     */
    public void printCell(int content) {
        switch (content) {
            case EMPTY: System.out.print("   ");
                break;
            case BLUE: System.out.print(" B ");
                break;
            case RED: System.out.print(" R ");
                break;
        }
    }
}
