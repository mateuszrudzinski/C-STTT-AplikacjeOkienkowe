import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Server
{
    private ServerSocket serverSocket;
    private PrintWriter socketOut;
    private BufferedReader socketIn;
    private ExecutorService pool;
    private boolean firstPlayerFound;
    private final Player [] playerList = new Player[1000];
    private int playerID;
    private int joinedPlayers = 1;
    private final Database db = new Database();
    public Server() 
    {
        try 
        {

            firstPlayerFound = false;
            serverSocket = new ServerSocket(6667);
            pool = Executors.newFixedThreadPool(5);
            System.out.println("Server is running...");
        } 
        catch (IOException  e) { e.printStackTrace(); }
    }
    public void runServer() 
    {
        try {
            playerID = db.getLastId()+1;
            while(true)
            {
                addPlayerToList();
                joinedPlayers++;
            }
        } catch (Exception e) { e.printStackTrace(); }
        finally { closeServer(); }
    }
    private void addPlayerToList() throws SQLException {
        playerList[playerID] = getPlayer();
        db.addPlayerToDBQUERY(playerID,playerList[playerID].getName(),playerList[playerID].getPassword());
        selectPlayer(playerID);
        playerID++;
    }



   private void selectPlayer(int playerID) throws SQLException {
       if(joinedPlayers==2)
       {
        Player p1 = playerList[playerID];
        Player p2 = playerList[playerID-1];
        joinedPlayers = 0;
        int gameId = db.getLastGameId() + 1;
        pool.execute(new ServerController(p1, p2, gameId));
       }

   }


    private Player getPlayer()
    {
        while (true)
        {
            try
            {
                Socket socket = serverSocket.accept();
                socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                socketOut = new PrintWriter((socket.getOutputStream()), true);
                Player player = new Player(socket, socketIn.readLine(), setPlayerMark(), playerID,socketIn.readLine());
                firstPlayerFound = !firstPlayerFound;
                System.out.println(player.getName() + " with PASSWORD "+ player.getPassword() + " connected.");
                return player;
            }
            catch (IOException e) { System.out.println("Problem setting up player. Retrying..."); }
        }
    }

    private char setPlayerMark()
    {
        if (!firstPlayerFound) { return 'X'; }
        else return 'O';
    }

    private void closeServer()
    {
        try 
        {
            socketIn.close();
            socketOut.close();
        } 
        catch (IOException e) { System.out.println(e.getMessage()); }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.runServer();
    }
}