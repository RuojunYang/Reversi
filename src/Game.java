import AI.pMCTS;
import model.GameBoard;
import model.ChessState;
import model.Pair;

import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        //gameHumanVsHuman();
        //gameHumanVsAi();
        gameAiVsAi();
    }

    private static void gameHumanVsAi() {
        GameBoard gameBoard = new GameBoard();
        UI ui = new UI();
        ChessState player1 = ChessState.black;
        ChessState player2 = ChessState.white;
        Scanner scanner = new Scanner(System.in);
        pMCTS AI = new pMCTS(ChessState.white);

        // game start
        while (gameBoard.possibleMove(player1).size() > 0 || gameBoard.possibleMove(player2).size() > 0) {
            // player 1
            ui.displayMap(gameBoard);
            System.out.println("Black chess turn");
            System.out.println(gameBoard.possibleMove(player1).size() + " possible moves");
            if (gameBoard.possibleMove(player1).size() > 0) {
                for (Pair<Integer, Integer> pair : gameBoard.possibleMove(player1)) {
                    System.out.print("(" + (pair.getKey() + 1) + "," + (pair.getValue() + 1) + ") ");
                }
                System.out.println();
                int row, col;
                do {
                    System.out.println("Black chess please enter row");
                    row = scanner.nextInt() - 1;
                    System.out.println("Black chess please enter col");
                    col = scanner.nextInt() - 1;
                } while (!gameBoard.possibleMove(player1).contains(new Pair<Integer, Integer>(row, col)));
                gameBoard.makeMove(row, col, player1);
            }

            // player 2
            ui.displayMap(gameBoard);
            System.out.println("White chess turn");
            if (gameBoard.possibleMove(player2).size() > 0) {
                Pair<Integer, Integer> move = AI.h_pMCTSMove(gameBoard);
                System.out.println("AI choose: " + (move.getKey() + 1) + ", " + (move.getValue() + 1));
                gameBoard.makeMove(move.getKey(), move.getValue(), AI.getState());
            }
        }
        ui.displayMap(gameBoard);
        // game end
        int white = 0;
        int black = 0;
        for (int i = 0; i < gameBoard.getRow(); i++) {
            for (int j = 0; j < gameBoard.getCol(); j++) {
                if (gameBoard.getElement(i, j) == ChessState.white) {
                    white++;
                } else if (gameBoard.getElement(i, j) == ChessState.black) {
                    black++;
                }
            }
        }

        if (white > black) {
            System.out.println("White win");
        } else if (black > white) {
            System.out.println("Black win");
        } else {
            System.out.println("Tie game");
        }

    }

    // yyh modified
    private static void gameAiVsAi() {
        GameBoard gameBoard = new GameBoard();
        UI ui = new UI();
        ChessState player1 = ChessState.black;
        ChessState player2 = ChessState.white;
        Scanner scanner = new Scanner(System.in);
        boolean pMCSTGoFirst;
        System.out.println("Type '1' let pure MCST go first, type other let heuristics MCST go first");
        if (scanner.next().equals("1")) {
            pMCSTGoFirst = true;
            pMCTS AIp = new pMCTS(ChessState.black);
            pMCTS AIh = new pMCTS(ChessState.white);
            // game start
            while (gameBoard.possibleMove(player1).size() > 0 || gameBoard.possibleMove(player2).size() > 0) {
                // player 1
                ui.displayMap(gameBoard);
                System.out.println("Black chess turn");
                System.out.println(gameBoard.possibleMove(player1).size() + " possible moves");
                if (gameBoard.possibleMove(player1).size() > 0) {
                    Pair<Integer, Integer> move = AIp.pMCTSMove(gameBoard);
                    System.out.println("pure MCST AI choose: " + (move.getKey() + 1) + ", " + (move.getValue() + 1));
                    gameBoard.makeMove(move.getKey(), move.getValue(), AIp.getState());
                }
                System.out.println();

                // player 2
                ui.displayMap(gameBoard);
                System.out.println("White chess turn");
                System.out.println(gameBoard.possibleMove(player2).size() + " possible moves");
                if (gameBoard.possibleMove(player2).size() > 0) {
                    Pair<Integer, Integer> move = AIh.h_pMCTSMove(gameBoard);
                    System.out.println("heuristics MCST AI choose: " + (move.getKey() + 1) + ", " + (move.getValue() + 1));
                    gameBoard.makeMove(move.getKey(), move.getValue(), AIh.getState());
                }
                System.out.println();
            }
        } else {
            pMCSTGoFirst = false;
            pMCTS AIp = new pMCTS(ChessState.white);
            pMCTS AIh = new pMCTS(ChessState.black);
            // game start
            while (gameBoard.possibleMove(player1).size() > 0 || gameBoard.possibleMove(player2).size() > 0) {
                // player 1
                ui.displayMap(gameBoard);
                System.out.println("Black chess turn");
                System.out.println(gameBoard.possibleMove(player1).size() + " possible moves");
                if (gameBoard.possibleMove(player1).size() > 0) {
                    Pair<Integer, Integer> move = AIh.h_pMCTSMove(gameBoard);
                    System.out.println("heuristics MCST AI choose: " + (move.getKey() + 1) + ", " + (move.getValue() + 1));
                    gameBoard.makeMove(move.getKey(), move.getValue(), AIh.getState());
                }
                System.out.println();

                // player 2
                ui.displayMap(gameBoard);
                System.out.println("White chess turn");
                System.out.println(gameBoard.possibleMove(player2).size() + " possible moves");
                if (gameBoard.possibleMove(player2).size() > 0) {
                    Pair<Integer, Integer> move = AIp.pMCTSMove(gameBoard);
                    System.out.println("pure MCST AI choose: " + (move.getKey() + 1) + ", " + (move.getValue() + 1));
                    gameBoard.makeMove(move.getKey(), move.getValue(), AIp.getState());
                }
                System.out.println();
            }
        }
        ui.displayMap(gameBoard);
        // game end
        int white = 0;
        int black = 0;
        for (int i = 0; i < gameBoard.getRow(); i++) {
            for (int j = 0; j < gameBoard.getCol(); j++) {
                if (gameBoard.getElement(i, j) == ChessState.white) {
                    white++;
                } else if (gameBoard.getElement(i, j) == ChessState.black) {
                    black++;
                }
            }
        }

        if (white > black) {
            if(pMCSTGoFirst){
                System.out.println("heuristics MCST win");
            }
            else{
                System.out.println("pure MCST win");
            }
            System.out.println("White win");
        } else if (black > white) {
            if(pMCSTGoFirst){
                System.out.println("pure MCST win");
            }
            else{
                System.out.println("heuristics MCST win");
            }
            System.out.println("Black win");
        } else {
            System.out.println("Tie game");
        }

    }


    private static void gameHumanVsHuman() {
        GameBoard gameBoard = new GameBoard();
        UI ui = new UI();
        ChessState player1 = ChessState.black;
        ChessState player2 = ChessState.white;
        Scanner scanner = new Scanner(System.in);

        // game start
        while (gameBoard.possibleMove(player1).size() > 0 || gameBoard.possibleMove(player2).size() > 0) {
            // player 1
            ui.displayMap(gameBoard);
            System.out.println("Black chess turn");
            System.out.println(gameBoard.possibleMove(player1).size() + " possible moves");
            if (gameBoard.possibleMove(player1).size() > 0) {
                for (Pair<Integer, Integer> pair : gameBoard.possibleMove(player1)) {
                    System.out.print("(" + (pair.getKey() + 1) + "," + (pair.getValue() + 1) + ") ");
                }
                System.out.println();
                int row, col;
                do {
                    System.out.println("Black chess please enter row");
                    row = scanner.nextInt() - 1;
                    System.out.println("Black chess please enter col");
                    col = scanner.nextInt() - 1;
                } while (!gameBoard.possibleMove(player1).contains(new Pair<Integer, Integer>(row, col)));
                gameBoard.makeMove(row, col, player1);
            }

            // player 2
            ui.displayMap(gameBoard);
            System.out.println("White chess turn");
            System.out.println(gameBoard.possibleMove(player2).size() + " possible moves");
            if (gameBoard.possibleMove(player2).size() > 0) {
                for (Pair<Integer, Integer> pair : gameBoard.possibleMove(player2)) {
                    System.out.print("(" + (pair.getKey() + 1) + "," + (pair.getValue() + 1) + ") ");
                }
                System.out.println();
                int row, col;
                do {
                    System.out.println("White chess please enter row");
                    row = scanner.nextInt() - 1;
                    System.out.println("White chess please enter col");
                    col = scanner.nextInt() - 1;
                } while (!gameBoard.possibleMove(player2).contains(new Pair<Integer, Integer>(row, col)));
                gameBoard.makeMove(row, col, player2);
            }
        }
        // game end
        ui.displayMap(gameBoard);
        int white = 0;
        int black = 0;
        for (int i = 0; i < gameBoard.getRow(); i++) {
            for (int j = 0; j < gameBoard.getCol(); j++) {
                if (gameBoard.getElement(i, j) == ChessState.white) {
                    white++;
                } else if (gameBoard.getElement(i, j) == ChessState.black) {
                    black++;
                }
            }
        }

        if (white > black) {
            System.out.println("White win");
        } else if (black > white) {
            System.out.println("Black win");
        } else {
            System.out.println("Tie game");
        }
    }
}
