package AI;

import model.Pair;
import model.GameBoard;
import model.ChessState;
import model.GameState;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.lang.Float.NEGATIVE_INFINITY;

public class pMCTS {
    ChessState state;
    int weight[][] = {{20, -3, 11, 8, 8, 11, -3, 20},
            {-3, -7, -4, 1, 1, -4, -7, -3},
            {11, -4, 2, 2, 2, 2, -4, 11},
            {8, 1, 2, -3, -3, 2, 1, 8},
            {8, 1, 2, -3, -3, 2, 1, 8},
            {11, -4, 2, 2, 2, 2, -4, 11},
            {-3, -7, -4, 1, 1, -4, -7, -3},
            {20, -3, 11, 8, 8, 11, -3, 20}};

    public pMCTS(ChessState state) {
        this.state = state;
    }

    public ChessState getState() {
        return state;
    }

    public Pair<Integer, Integer> pMCTSMove(GameBoard gameBoard) {
        List<Pair<Integer, Integer>> possibleMove = gameBoard.possibleMove(state);
        int score = Integer.MAX_VALUE;
        int row = -1;
        int col = -1;
        for (Pair<Integer, Integer> pair : possibleMove) {
            long start = new Date().getTime();
            int currentWin = 0;
            int currentLose = 0;
            int currentTie = 0;
            int currentScore;
            int range = 2500;
            for (int i = 0; i < range; i++) {
                // 5000 is 5 second
                if (new Date().getTime() - start > (5000 / possibleMove.size())){
                    System.out.println("5 second bound appear");
                    break;
                }
                GameBoard gameCopy = gameBoard.gameCopy();
                Pair<GameState, Boolean> result = randomPlayouts(gameCopy, pair.getKey(), pair.getValue());
                if (result.getKey() == GameState.win) {
                    currentWin++;
                    if (result.getValue()) {
                        System.out.println("true!!!!!!");
                        return pair;
                    }
                } else if (result.getKey() == GameState.lose) {
                    currentLose++;
                } else {
                    currentTie++;
                }
            }
            currentScore = currentLose * 10 + currentWin;
            if (currentScore <= score) {
                score = currentScore;
                row = pair.getKey();
                col = pair.getValue();
            }
        }
        System.out.println("score: " + score);
        // if couldn't make a choose
        if (row == -1 || col == -1) {
            Random random = new Random();
            return gameBoard.possibleMove(state).get(random.nextInt(gameBoard.possibleMove(state).size()));
        }
        return new Pair<>(row, col);
    }

    private Pair<GameState, Boolean> randomPlayouts(GameBoard gameBoard, Integer row, Integer col) {
        ChessState other;
        boolean oneMove = false;
        GameState gameState = GameState.lose;
        if (state == ChessState.black) {
            other = ChessState.white;
        } else {
            other = ChessState.black;
        }
        gameBoard.makeMove(row, col, state);
        if (gameBoard.checkGameEnd()) {
            if (gameBoard.checkStateForPlayer(state) == GameState.win) {
                oneMove = true;
                gameState = GameState.win;
            } else if (gameBoard.checkStateForPlayer(state) == GameState.lose) {
                gameState = GameState.lose;
            } else {
                gameState = GameState.tie;
            }
        }
        List<Pair<Integer, Integer>> possibleMove;
        Pair<Integer, Integer> newtMove;
        Random rand;
        // while game not end
        while (!gameBoard.checkGameEnd()) {
            // other turn
            possibleMove = gameBoard.possibleMove(other);
            if (possibleMove.size() > 0) {
                rand = new Random();
                newtMove = possibleMove.get(rand.nextInt(possibleMove.size()));
                gameBoard.makeMove(newtMove.getKey(), newtMove.getValue(), other);
                if (gameBoard.checkGameEnd()) {
                    if (gameBoard.checkStateForPlayer(state) == GameState.win) {
                        gameState = GameState.win;
                    } else if (gameBoard.checkStateForPlayer(state) == GameState.lose) {
                        gameState = GameState.lose;
                    } else {
                        gameState = GameState.tie;
                    }
                    break;
                }
            }

            // self turn
            possibleMove = gameBoard.possibleMove(state);
            if (possibleMove.size() > 0) {
                rand = new Random();
                newtMove = possibleMove.get(rand.nextInt(possibleMove.size()));
                gameBoard.makeMove(newtMove.getKey(), newtMove.getValue(), state);
                if (gameBoard.checkGameEnd()) {
                    if (gameBoard.checkStateForPlayer(state) == GameState.win) {
                        gameState = GameState.win;
                    } else if (gameBoard.checkStateForPlayer(state) == GameState.lose) {
                        gameState = GameState.lose;
                    } else {
                        gameState = GameState.tie;
                    }
                    break;
                }
            }
        }
        return new Pair<>(gameState, oneMove);
    }

    // yyh add for Heuristic MCTS
    public Pair<Integer, Integer> h_pMCTSMove(GameBoard gameBoard) {
        List<Pair<Integer, Integer>> possibleMove = gameBoard.possibleMove(state);
        int totalScore = 0;
        int row = -1;
        int col = -1;
        //int currentWin = 0;
        //int currentLose = 0;
        //int currentTie = 0;
        int findBug = 100;
        double score = NEGATIVE_INFINITY;
        for (Pair<Integer, Integer> pair : possibleMove) {
            int currentScore = 0;
            //int weightValue=weight[pair.getKey()-1][pair.getValue()-1];
            int range = 2000;
            for (int i = 0; i < range; i++) {
                GameBoard gameCopy = gameBoard.gameCopy();
                Pair<GameState, Integer> result = randomPlayoutsForValue(gameCopy, pair.getKey(), pair.getValue());
                //System.out.println("state:  "+result.getKey()+" score: "+result.getValue());
                if (result.getKey() == GameState.win) {
                    //currentWin++;
                    currentScore = currentScore + 2000 + result.getValue();
                    /*if (result.getValue()) {
                        System.out.println("true!!!!!!");
                        return pair;
                    }*/
                } else if (result.getKey() == GameState.lose) {
                    currentScore = currentScore - 2000 + result.getValue();
                    //currentLose++;
                } else {
                    //currentTie++;
                    currentScore = currentScore + result.getValue();
                }
            }
            if (currentScore >= score) {
                System.out.println("current score:   " + currentScore);
                score = currentScore;
                row = pair.getKey();
                col = pair.getValue();
            }

        }

        System.out.println("socre:   " + score);
        return new Pair<>(row, col);
    }

    public int getScore(int row, int col) {
        //System.out.println("row:   !!!!"+row+"col: !!!!!!"+col);
        return weight[row][col];
    }

    public Pair<GameState, Integer> randomPlayoutsForValue(GameBoard gameBoard, Integer row, Integer col) {
        ChessState other;
        boolean oneMove = false;
        int otherScore = 0;
        int selfScore = 0;
        GameState gameState = GameState.lose;
        if (state == ChessState.black) {
            other = ChessState.white;
        } else {
            other = ChessState.black;
        }
        gameBoard.makeMove(row, col, state);
        if (gameBoard.checkGameEnd()) {
            if (gameBoard.checkStateForPlayer(state) == GameState.win) {
                oneMove = true;
                gameState = GameState.win;
            } else if (gameBoard.checkStateForPlayer(state) == GameState.lose) {
                gameState = GameState.lose;
            } else {
                gameState = GameState.tie;
            }
        }
        List<Pair<Integer, Integer>> possibleMove;
        Pair<Integer, Integer> newtMove;
        Random rand;
        // while game not end
        while (!gameBoard.checkGameEnd()) {
            // other turn
            possibleMove = gameBoard.possibleMove(other);
            if (possibleMove.size() > 0) {
                rand = new Random();
                newtMove = possibleMove.get(rand.nextInt(possibleMove.size()));
                gameBoard.makeMove(newtMove.getKey(), newtMove.getValue(), other);

                otherScore = otherScore + getScore(newtMove.getKey(), newtMove.getValue());     // user get a better position, so the ai are more likely to loss.
                //  user get a worse position, so the ai are more likely to win.
                if (gameBoard.checkGameEnd()) {
                    if (gameBoard.checkStateForPlayer(state) == GameState.win) {
                        gameState = GameState.win;
                    } else if (gameBoard.checkStateForPlayer(state) == GameState.lose) {
                        gameState = GameState.lose;
                    } else {
                        gameState = GameState.tie;
                    }
                    break;
                }
            }

            // self turn
            possibleMove = gameBoard.possibleMove(state);
            if (possibleMove.size() > 0) {
                rand = new Random();
                newtMove = possibleMove.get(rand.nextInt(possibleMove.size()));
                gameBoard.makeMove(newtMove.getKey(), newtMove.getValue(), state);

                selfScore = selfScore + getScore(newtMove.getKey(), newtMove.getValue());    // user get a better position, so the ai are more likely to loss.
                //  user get a worse position, so the ai are more likely to win.

                if (gameBoard.checkGameEnd()) {
                    if (gameBoard.checkStateForPlayer(state) == GameState.win) {
                        gameState = GameState.win;
                    } else if (gameBoard.checkStateForPlayer(state) == GameState.lose) {
                        gameState = GameState.lose;
                    } else {
                        gameState = GameState.tie;
                    }
                    break;
                }
            }
        }
        return new Pair<>(gameState, selfScore - otherScore);
    }
}
