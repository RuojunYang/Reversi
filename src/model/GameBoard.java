package model;

import model.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameBoard {
    private int row;
    private int col;
    private ChessState[][] map;

    public GameBoard() {
        row = 8;
        col = 8;
        map = new ChessState[8][8];
        // fill empty state for all map
        for (ChessState[] row : map) {
            Arrays.fill(row, ChessState.empty);
        }
        // initial board have four chess
        map[3][3] = ChessState.white;
        map[3][4] = ChessState.black;
        map[4][3] = ChessState.black;
        map[4][4] = ChessState.white;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public ChessState getElement(int row, int col) {
        ChessState state = map[row][col];
        return state;
    }

    // return true for success move
    // return false for unsuccessful move
    public boolean makeMove(int row, int col, ChessState player) {
        if (map[row][col] == ChessState.empty) {
            map[row][col] = player;
            ChessState other;
            if (player == ChessState.black) {
                other = ChessState.white;
            } else {
                other = ChessState.black;
            }
            flip(row, col, player, other);
            return true;
        } else {
            return false;
        }
    }

    private void flip(int row, int col, ChessState player, ChessState other) {
        if (checkUp(row, col, player, other)) {
            for (int i = row - 1; i >= 0; i--) {
                if (map[i][col] == other) {
                    map[i][col] = player;
                } else {
                    break;
                }
            }
        }
        if (checkDown(row, col, player, other)) {
            for (int i = row + 1; i < 8; i++) {
                if (map[i][col] == other) {
                    map[i][col] = player;
                } else {
                    break;
                }
            }
        }
        if (checkLeft(row, col, player, other)) {
            for (int i = col - 1; i >= 0; i--) {
                if (map[row][i] == other) {
                    map[row][i] = player;
                } else {
                    break;
                }
            }
        }
        if (checkRight(row, col, player, other)) {
            for (int i = col + 1; i < 8; i++) {
                if (map[row][i] == other) {
                    map[row][i] = player;
                } else {
                    break;
                }
            }
        }
        if (checkLeftUp(row, col, player, other)) {
            int smaller = Math.min(row, col) + 1;
            for (int i = 1; i < smaller; i++) {
                if (map[row - i][col - i] == other) {
                    map[row - i][col - i] = player;
                } else {
                    break;
                }
            }
        }
        if (checkRightUp(row, col, player, other)) {
            int smaller = Math.min(row, 7 - col) + 1;
            for (int i = 1; i < smaller; i++) {
                if (map[row - i][col + i] == other) {
                    map[row - i][col + i] = player;
                } else {
                    break;
                }
            }
        }
        if (checkLeftDown(row, col, player, other)) {
            int smaller = Math.min(7 - row, col) + 1;
            for (int i = 1; i < smaller; i++) {
                if (map[row + i][col - i] == other) {
                    map[row + i][col - i] = player;
                } else {
                    break;
                }
            }
        }
        if (checkRightDown(row, col, player, other)) {
            int smaller = Math.min(7 - row, 7 - col) + 1;
            for (int i = 1; i < smaller; i++) {
                if (map[row + i][col + i] == other) {
                    map[row + i][col + i] = player;
                } else {
                    break;
                }
            }

        }
    }

    public List<Pair<Integer, Integer>> possibleMove(ChessState player) {
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        ChessState other;
        if (player == ChessState.black) {
            other = ChessState.white;
        } else {
            other = ChessState.black;
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                // only empty and near a cheese could be possible move
                if (map[i][j] == ChessState.empty) {
                    if (checkMove(i, j, player, other)) {
                        list.add(new Pair<>(i, j));
                    }
                }
            }
        }
        return list;
    }

    private boolean checkMove(int row, int col, ChessState player, ChessState other) {
        // up
        return (checkUp(row, col, player, other) ||
                checkDown(row, col, player, other) ||
                checkLeft(row, col, player, other) ||
                checkRight(row, col, player, other) ||
                checkLeftUp(row, col, player, other) ||
                checkRightUp(row, col, player, other) ||
                checkLeftDown(row, col, player, other) ||
                checkRightDown(row, col, player, other));
    }

    private boolean checkUp(int row, int col, ChessState player, ChessState other) {
        // up
        if (row > 1 && map[row - 1][col] == other) {
            for (int i = row - 2; i >= 0; i--) {
                if (map[i][col] == player) {
                    return true;
                } else if (map[i][col] == ChessState.empty) {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean checkDown(int row, int col, ChessState player, ChessState other) {
        // down
        if (row < 6 && map[row + 1][col] == other) {
            for (int i = row + 2; i < 8; i++) {
                if (map[i][col] == player) {
                    return true;
                } else if (map[i][col] == ChessState.empty) {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean checkLeft(int row, int col, ChessState player, ChessState other) {
        // left
        if (col > 1 && map[row][col - 1] == other) {
            for (int i = col - 2; i >= 0; i--) {
                if (map[row][i] == player) {
                    return true;
                } else if (map[row][i] == ChessState.empty) {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean checkRight(int row, int col, ChessState player, ChessState other) {
        // right
        if (col < 6 && map[row][col + 1] == other) {
            for (int i = col + 2; i < 8; i++) {
                if (map[row][i] == player) {
                    return true;
                } else if (map[row][i] == ChessState.empty) {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean checkLeftUp(int row, int col, ChessState player, ChessState other) {
        // left up
        if (row > 1 && col > 1 && map[row - 1][col - 1] == other) {
            int smaller = Math.min(row, col) + 1;
            for (int i = 2; i < smaller; i++) {
                if (map[row - i][col - i] == player) {
                    return true;
                } else if (map[row - i][col - i] == ChessState.empty) {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean checkRightUp(int row, int col, ChessState player, ChessState other) {
        // right up
        if (row > 1 && col < 6 && map[row - 1][col + 1] == other) {
            int smaller = Math.min(row, 7 - col) + 1;
            for (int i = 2; i < smaller; i++) {
                if (map[row - i][col + i] == player) {
                    return true;
                } else if (map[row - i][col + i] == ChessState.empty) {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean checkLeftDown(int row, int col, ChessState player, ChessState other) {
        // left down
        if (row < 6 && col > 1 && map[row + 1][col - 1] == other) {
            int smaller = Math.min(7 - row, col) + 1;
            for (int i = 2; i < smaller; i++) {
                if (map[row + i][col - i] == player) {
                    return true;
                } else if (map[row + i][col - i] == ChessState.empty) {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean checkRightDown(int row, int col, ChessState player, ChessState other) {
        // right down
        if (row < 6 && col < 6 && map[row + 1][col + 1] == other) {
            int smaller = Math.min(7 - row, 7 - col) + 1;
            for (int i = 2; i < smaller; i++) {
                if (map[row + i][col + i] == player) {
                    return true;
                } else if (map[row + i][col + i] == ChessState.empty) {
                    return false;
                }
            }
        }
        return false;
    }

    private void setElement(int row, int col, ChessState state) {
        this.map[row][col] = state;
    }

    public GameBoard gameCopy() {
        GameBoard copy = new GameBoard();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                copy.setElement(i, j, getElement(i, j));
            }
        }
        return copy;
    }

    public boolean checkGameEnd() {
        return (possibleMove(ChessState.white).size() == 0 && possibleMove(ChessState.black).size() == 0);
    }

    public GameState checkStateForPlayer(ChessState player) {
        int white = 0;
        int black = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (map[i][j] == ChessState.white) {
                    white++;
                } else if (map[i][j] == ChessState.black) {
                    black++;
                }
            }
        }
        if (white == black) {
            return GameState.tie;
        } else if (player == ChessState.white) {
            if (white > black) {
                return GameState.win;
            }
            return GameState.lose;
        } else {
            if (white < black) {
                return GameState.win;
            }
            return GameState.lose;
        }
    }
}
