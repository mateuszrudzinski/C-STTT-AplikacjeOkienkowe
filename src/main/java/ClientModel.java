import java.sql.SQLException;

public class ClientModel
{

    private Board board;

    private Player activePlayer;

    public ClientModel() 
    {
        this.board = new Board();
    }

    private final Database db = new Database();

    public void performOneMove() {activePlayer.makeMove();}

    public void addMark(int row, int col, char mark) 
    {
        board.addMark(row, col, mark);
    }

    public void attemptMove(int[] move) throws SQLException {
        if (board.isBlank(move))
        {
            addMark(move[0], move[1], activePlayer.getMark());
            db.insertMovetoDB(activePlayer.getGameId(),move[1],move[0], activePlayer.getMark(), activePlayer.getName());
            activePlayer.setHasPlayed(true);
        }
    }

    public char[][] getBoardArr() 
    { 
        return board.getBoard(); 
    }


    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
    public Player getActivePlayer() { return activePlayer; }
    public void setActivePlayer(Player activePlayer) { this.activePlayer = activePlayer; }
}