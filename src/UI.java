import model.GameBoard;
import model.ChessState;

public class UI {
    public void displayMap(GameBoard gameBoard) {
        for (int i = 0; i < gameBoard.getRow() * 2 + 2; i++) {
            for (int j = 0; j < gameBoard.getCol() * 2 + 2; j++) {
                if (i == 0 && j == 0) {
                    System.out.print(" ");
                } else if (i == 0 && j > 1 && j % 2 == 0) {
                    System.out.print(j / 2);
                } else if (j == 0 && i > 1 && i % 2 == 0) {
                    System.out.print(i / 2);
                } else if (i % 2 == 1) {
                    System.out.print("-");
                } else if (j % 2 == 1) {
                    System.out.print("|");
                } else if (i > 1 && j > 1 && i % 2 == 0 && j % 2 == 0) {
                    if (gameBoard.getElement(i / 2 - 1, j / 2 - 1) == ChessState.black) {
                        System.out.print("X");
                    } else if (gameBoard.getElement(i / 2 - 1, j / 2 - 1) == ChessState.white) {
                        System.out.print("O");
                    } else {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
