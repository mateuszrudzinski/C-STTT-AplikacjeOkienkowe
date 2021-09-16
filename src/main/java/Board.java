import java.io.Serial;
import java.io.Serializable;

/**
 * This class represents a tic-tac-toe board/grid. It contains the logic to
 * place markers and check win conditions.
 */
// ta klasa zawiera logike i sprawdza warunki zakonczenia gry
public class Board implements Serializable
{

    @Serial
    private static final long serialVersionUID = -4309518207161622889L;

    private final char[][] board;

    private int markCount;


  //konstruktor board, tworzy tablice 2D oraz ustawia ilosc znakow na planszy na 0
    public Board()
    {
        markCount = 0;
        board = new char[3][3];
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                board[i][j] = ' ';
            }
        }
    }

    // zwraca true jesli pole jest puste
    public boolean isBlank(int[] move)
    {
        return board[move[0]][move[1]] == ' ';
    }

    // zwraca true jezeli board jest pełny
    public boolean isFull()
    {
        return markCount >= 9;
    }

   // zwraca true jezeli gracz wygrał którąś z kombinacji
    public boolean hasWon()
    {
        return isHorizontalWin() || isVerticalWin() || isDiagonalWin();
    }

  // zwraca true jezeli którys gracz wygrał lub jest remis
    public boolean hasEnded()
    {
        return hasWon() || isFull();
    }

    // dodaje znak do boarda oraz zwieksza markcount czyli ilosc znakow znajdujących sie juz na planszy
    public void addMark(int row, int col, char mark)
    {
        board[row][col] = mark;
        markCount++;
    }


    // sprawdza czy gracz wygrał w poziomie
    private boolean isHorizontalWin()
    {
        for (int row = 0; row < 3; row++)
        {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2] && board[row][2] != ' ')
            {
                return true;
            }
        }
        return false;
    }

   //sprawdza czy gracz wygrał w pionie
    private boolean isVerticalWin()
    {
        for (int col = 0; col < 3; col++)
        {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col] && board[2][col] != ' ')
            {
                return true;
            }
        }
        return false;
    }

   // sprawdza czy gracz wygrał pod skosem
    private boolean isDiagonalWin()
    {
        return
            (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != ' ') ||
            (board[2][0] == board[1][1] && board[2][0] == board[0][2] && board[2][0] != ' ');
    }
    public char[][] getBoard() { return board; }
}