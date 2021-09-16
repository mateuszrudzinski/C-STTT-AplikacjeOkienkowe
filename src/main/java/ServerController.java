import java.io.IOException;
import java.net.SocketException;
public class ServerController implements Runnable 
{

    private final ServerModel model;
    public ServerController(Player p1, Player p2, int gameId) {

        this.model = new ServerModel();

        model.setGameId(gameId);
        model.setActivePlayer(p2);
        model.setIdlePlayer(p1);
        p1.setGameId(gameId);
        p2.setGameId(gameId);
    }

    @Override
    public void run() 
    {
        try
        {
            notifyGameStart();
            while (!model.hasEnded()) { playTurn(); }
            endGame();
        }
        catch (SocketException e) { handleOpponentDisconnect(); }
    }

    private void notifyGameStart()
    {
        System.out.println("Game started between " + model.getIdlePlayer().getName() + 
            " and " + model.getActivePlayer().getName() + ".");
        model.getActiveSocketOut().println("opponentFound");
        model.getIdleSocketOut().println("opponentFound");
        sleep();
    }

    /**
     * Puts the thread to sleep for the input amount of milliseconds.
     */
    private void sleep()
    {
        try { Thread.sleep(2500); }
        catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Plays one turn of tic-tac-toe.
     * @throws SocketException Thrown when player disconnects.
     */
    private void playTurn() throws SocketException
    {
        model.toggleActivePlayer();
        sendGameStateInfo();
        updateServerGameState();

    }

    /**
     * Sends the most up-to-date details from the server's model to the 
     * players.
     * @throws SocketException Thrown when player disconnects.
     */
    private void sendGameStateInfo() throws SocketException
    {
        try
        {
            GameState gameState = new GameState(model.getBoard(), model.getActivePlayer());
            model.getActiveOutputStream().writeObject(gameState);
            model.getIdleOutputStream().writeObject(gameState);
        }
        catch (IOException e) { throw new SocketException(); }
    }

    /**
     * Take a game update from the client and applies them to the server's
     * game model.
     * @throws SocketException Thrown when player disconnects.
     */
    private void updateServerGameState() throws SocketException
    {
        try 
        {
            GameState gameState = (GameState) model.getActiveInputStream().readObject();
            model.setBoard(gameState.getBoard());
        }
        catch (IOException e) { throw new SocketException();  }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
    }

    /**
     * Sends the ended GameState to the players and prints a game over prompt
     * to the server.
     * @throws SocketException Thrown when player disconnects.
     */
    private void endGame() throws SocketException
    {
        try
        {
            GameState gameState = new GameState(model.getBoard(), model.getActivePlayer());
            model.getActiveOutputStream().writeObject(gameState);
            model.getIdleOutputStream().writeObject(gameState);
            System.out.println("Game ended between " + model.getIdlePlayer().getName() + " and " +
            model.getActivePlayer().getName() + ". " + model.getActivePlayer().getName() + " won!");
        }
        catch (IOException e) { throw new SocketException(); }
    }

    /**
     * Prints a disconnection prompt to the players and server.
     */
    private void handleOpponentDisconnect()
    {
        System.out.println("One or more players have rage quit. Ending game between " + 
        model.getIdlePlayer().getName() + " and " + model.getActivePlayer().getName() + ".");
        model.getActiveSocketOut().println("Your opponent has rage quit. Game over.");
        model.getIdleSocketOut().println("Your opponent has rage quit. Game over.");
    }
}