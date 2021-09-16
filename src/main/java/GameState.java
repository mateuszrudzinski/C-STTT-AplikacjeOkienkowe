import java.io.Serial;
import java.io.Serializable;


public class GameState implements Serializable 
{

    private Board board;

    private Player activePlayer;

    @Serial
    private static final long serialVersionUID = 3002053260033745936L;


    public GameState(Board board, Player activePlayer) 
    {
        this.board = board;
        this.activePlayer = activePlayer;
    }

    public boolean hasEnded() 
    {
        return board.hasEnded();
    }


    public boolean hasWon() 
    {
        return board.hasWon();
    }

    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
    public Player getActivePlayer() { return activePlayer; }
    public void setActivePlayer(Player activePlayer) { this.activePlayer = activePlayer; }
    public String getActiveName() { return activePlayer.getName(); }
    public String getActiveMark() { return activePlayer.getMark() + ""; }



}