import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * This class is the model of the server MVC pattern. It contains the 
 * tic-tac-toe game logic for the client.
 */
public class ServerModel 
{
    //=========================================================================
    // Instance variables
    //=========================================================================

    private int gameId;
    /**
     * The board for the tic-tac-toe game.
     */
    private Board board;
    /**
     * The active player in the current turn.
     */
    private Player activePlayer;
    /**
     * The idle player in the current turn.
     */
    private Player idlePlayer;

    //=========================================================================
    // Constructor
    //=========================================================================
    /**
     * Constructs a ServerModel object.
     */
    public ServerModel()
    {
        this.board = new Board();
    }

    //=========================================================================
    // Public methods
    //=========================================================================
    /**
     * Toggles the active player in the match.
     */
    public void toggleActivePlayer() 
    {
        Player temp = activePlayer;
        activePlayer = idlePlayer;
        idlePlayer = temp;
    }

    /**
     * Checks to see if the game has ended. Tha game can end if the board is
     * full or if either player has three markers in a row.
     * @return True if the game has ended, false otherwise.
     */
    public boolean hasEnded() 
    {
        return board.hasEnded();
    }

    //=========================================================================
    // Getters and setters
    //=========================================================================
    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
    public Player getActivePlayer() { return activePlayer; }
    public void setActivePlayer(Player activePlayer) { this.activePlayer = activePlayer; }
    public Player getIdlePlayer() { return idlePlayer; }
    public void setIdlePlayer(Player idlePlayer) { this.idlePlayer = idlePlayer; }
    public PrintWriter getActiveSocketOut() { return activePlayer.getSocketOut(); }
    public PrintWriter getIdleSocketOut() { return idlePlayer.getSocketOut(); }
    public ObjectInputStream getActiveInputStream() { return activePlayer.getObjectInputStream(); }
    public ObjectOutputStream getActiveOutputStream() { return activePlayer.getObjectOutputStream(); }
    public ObjectOutputStream getIdleOutputStream() { return idlePlayer.getObjectOutputStream(); }
    public void setGameId(int gameId) {this.gameId = gameId;}
}