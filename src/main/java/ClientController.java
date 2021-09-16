import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.sql.SQLException;


// Klasa ta jest kontolerem obsługującym relacje logiki z ClientModel oraz GUI z ClientView. Odpowiada również za połączenie klienta z serwerem

public class ClientController {

    private Socket socket;

    private PrintWriter socketOut;

    private BufferedReader socketIn;

    private ObjectInputStream objectInputStream;

    private ObjectOutputStream objectOutputStream;

    private final ClientView view = new ClientView();

    private final ClientModel model = new ClientModel();

    private boolean isLive = false;

    private String name = null;

    private String password = null;

    //Konkstruktor łaczy z serwerem, dodaje fukncje przycisków
    public ClientController(String serverName, int portNumber) {
        connectToServer(serverName, portNumber);
        addButtonFunctionality();
    }

    //Ustawia, uruchamia i konczy gre i połączenie klienta
    public void communicate() throws SQLException {
        setUpGame();
        runGame();
        disconnectFromServer();
    }

  // Połączenie z serwerem
    private void connectToServer(String serverName, int portNumber) {
        try {
            socket = new Socket(serverName, portNumber);
            socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e)
        {e.printStackTrace();}
        }

    //Funkcje przycisków
    private void addButtonFunctionality() {
        view.addr0c0Listener((ActionEvent e) ->
        {
            int[] move = {0, 0};
            try {
                model.attemptMove(move);
                view.retrieve(model.getActivePlayer().getGameId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        view.addr0c1Listener((ActionEvent e) ->
        {
            int[] move = {0, 1};
            try {
                model.attemptMove(move);
                view.retrieve(model.getActivePlayer().getGameId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        view.addr0c2Listener((ActionEvent e) ->
        {
            int[] move = {0, 2};
            try {
                model.attemptMove(move);
                view.retrieve(model.getActivePlayer().getGameId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        view.addr1c0Listener((ActionEvent e) ->
        {
            int[] move = {1, 0};
            try {
                model.attemptMove(move);
                view.retrieve(model.getActivePlayer().getGameId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        view.addr1c1Listener((ActionEvent e) ->
        {
            int[] move = {1, 1};
            try {
                model.attemptMove(move);
                view.retrieve(model.getActivePlayer().getGameId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        view.addr1c2Listener((ActionEvent e) ->
        {
            int[] move = {1, 2};
            try {
                model.attemptMove(move);
                view.retrieve(model.getActivePlayer().getGameId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        view.addr2c0Listener((ActionEvent e) ->
        {
            int[] move = {2, 0};
            try {
                model.attemptMove(move);
                view.retrieve(model.getActivePlayer().getGameId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        view.addr2c1Listener((ActionEvent e) ->
        {
            int[] move = {2, 1};
            try {
                model.attemptMove(move);
                view.retrieve(model.getActivePlayer().getGameId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        view.addr2c2Listener((ActionEvent e) ->
        {
            int[] move = {2, 2};
            try {
                model.attemptMove(move);
                view.retrieve(model.getActivePlayer().getGameId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        });

    }


    //Laczy gracza z serwerem i czeka na rozpoczęcie gry
    private void setUpGame()
    {
        setUpPlayer();
        waitForGameStart();
    }


    //Funkcja wyświetlająca POP UP z logowaniem
    //Dodaje gracza do gry hostowanej przez serwer
    private void setUpPlayer()
    {
        logInPop();

        try
        {
            view.setNameField(name);
            socketOut.println(name);
            socketOut.println(password);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) { e.printStackTrace(); }
    }

   //Funkcja wyczekuje do rozpoczoczęcia gry przez serwer, w momencie uzyskania stosownego komunikatu ze strony serwera przerywa działanie while'a
    // i pozwala uruchomić się funkcji runGame()
    private void waitForGameStart()
    {
        try
        {
            while (!isLive) 
            {
                String response = socketIn.readLine();
                if (response.equals("opponentFound"))
                {
                    isLive = true;
                }
            }
        }
        catch (IOException e) { e.printStackTrace(); } 
    }

    // Uruchamia gre, czeka na sygnał z update od serwera, kiedy go otrzyma ustawia lokalna sesje gry klientowi na tę którą otrzymała z serwera, po czym gra jest kontynuowana.
    private void runGame() throws SQLException {
        while (isLive) 
        {
            GameState gameState = receiveGameStateUpdate();
            if (gameState == null) { break; }
            updateClientGameState(gameState);
            continueGameState(gameState);
        }
    }

    // Funkcja pobierająca z serwera najbarrdziej aktualny stan rozgrywki, jeżeli nie uda się takowego pobrać, zwraca null.
    private GameState receiveGameStateUpdate()

    {
        try { return (GameState)objectInputStream.readObject(); }
        catch (StreamCorruptedException e) 
        {
            isLive = false;
        }
        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
        return null;
    }


    // Podmienia lokalne zmienne ( status gry ) klienta na te pobrane z serwera
    private void updateClientGameState(GameState gameState) throws SQLException {
        model.setBoard(gameState.getBoard());
        model.setActivePlayer(gameState.getActivePlayer());
        view.updateButtonText(model.getBoardArr());
        view.setNameField(gameState.getActiveName());
        view.setMarkField(gameState.getActiveMark());
        view.retrieve(model.getActivePlayer().getGameId());

    }

    //Funkcja ta pozwala na puszczenie gry dalej, jeżeli nie jest ona zakończona. Jeżeli użytkownik wygrał bądź zremisował, kończy gre.

    private void continueGameState(GameState gameState) {
        if (!gameState.hasEnded()) { processTurn(gameState); }
        else if (gameState.hasWon()) { handleWinEnding(gameState); } 
        else { handleTieEnding(); }
    }

    //Jesli jest tura klienta odblokowywuje przyciski natomiast jeżeli tura należy do przeciwnika oczekuje do jej zakończenia
    private void processTurn(GameState gameState) {
        // If active player
        if (gameState.getActiveName().equals(name)) 
        {
            playTurn(gameState);
        }
    }

    //Pozwala na rozegranie tury klientowi, na końcu przesyłając do serwera stan gry po ruchu.
    private void playTurn(GameState gameState)
    {
        view.enableButtons(true);
        model.performOneMove();
        view.enableButtons(false);
        view.updateButtonText(model.getBoardArr());
        gameState.setBoard(model.getBoard());
        gameState.setActivePlayer(model.getActivePlayer());
        sendGameStateUpdate(gameState);
    }
   // wysyła stan gry do serwera
    private void sendGameStateUpdate(GameState gameState)
    {
        try { objectOutputStream.writeObject(gameState); }
        catch (IOException e) { e.printStackTrace(); } 
    }

   //Wyświetla popup z wygranym i przerywa pętle z grą
    private void handleWinEnding(GameState gameState) 
    {
        view.winnerPop(gameState);
        isLive = false;
    }

    //Wyświetla popup z remisem i przerywa pętle z grą
    private void handleTieEnding() 
    {
        view.tiePop();
        isLive = false;
    }

   // zamyka sockety miedzy klientem a serwerem
    private void disconnectFromServer()
    {

        try
        {

            socketIn.close();
            socketOut.close();
            objectInputStream.close();
            objectOutputStream.close();

        } 
        catch (IOException e) { e.printStackTrace(); }
    }

    public void logInPop(){
        while (name == null || name.isEmpty() || password == null || password.isEmpty()) {
        int result = JOptionPane.showConfirmDialog(null, view.logInPanel, "Please Enter name and password", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                name = view.nameTF.getText();
                password = view.passwordTF.getText();
            } else if (result == JOptionPane.OK_CANCEL_OPTION) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        ClientController clientController = new ClientController("localhost", 6667);
        clientController.communicate();

    }
}