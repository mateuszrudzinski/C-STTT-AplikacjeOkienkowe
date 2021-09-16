import java.io.*;
import java.net.Socket;


public class Player implements Serializable 
{
    private String password;

    private String name;

    private int id;

    private char mark;

    private boolean hasPlayed;

    private transient PrintWriter socketOut;

    private transient BufferedReader socketIn;

    private transient ObjectInputStream objectInputStream;

    private transient ObjectOutputStream objectOutputStream;

    private int gameId;


    @Serial
    private static final long serialVersionUID = 720785984605791249L;

    public Player(Socket socket, String name, char mark, int id, String password)
    {
        try 
        {
            this.password = password;
            this.id = id;
            this.name = name;
            this.mark = mark;
            socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketOut = new PrintWriter((socket.getOutputStream()), true);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void makeMove() 
    {
        hasPlayed = false;
        while (!hasPlayed) 
        {
            try { Thread.sleep(0); }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public String getPassword() {return password;}
    public int getId() { return id; }
    public String getName() { return name; }
    public char getMark() { return mark; }
    public void setHasPlayed(boolean hasPlayed) { this.hasPlayed = hasPlayed; }
    public PrintWriter getSocketOut() { return socketOut; }
    public ObjectInputStream getObjectInputStream() { return objectInputStream; }
    public ObjectOutputStream getObjectOutputStream() { return objectOutputStream; }
    public int getGameId() {return gameId;}
    public void setGameId(int gameId) {this.gameId = gameId;}

}