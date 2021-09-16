import java.awt.*;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.io.Serial;
import java.sql.SQLException;

public class ClientView extends JFrame 
{

    @Serial
    private static final long serialVersionUID = 1L;

    private JPanel upPanel;

    private JTextField markField;

    private JTextField nameField;

    private JPanel centerPanel;

    private JButton r0c0Button;

    private JButton r0c1Button;

    private JButton r0c2Button;

    private JButton r1c0Button;

    private JButton r1c1Button;

    private JButton r1c2Button;

    private JButton r2c0Button;

    private JButton r2c1Button;

    private JButton r2c2Button;

    private JPanel westPanel;

    private JScrollPane listScrollPane;

    private JList<String> jList;

    public  JTextField nameTF;

    public JPasswordField passwordTF;

    public JPanel logInPanel;

    public void retrieve(int idGames) throws SQLException {
        DefaultListModel<String> dm=new Database().getListOfMoves(idGames);
        jList.setModel(dm);
    }

    public ClientView() 
    {
        super("Tic Tac Toe");
        initVariables();
        initContainer();
        initPanels();
    }


    public void enableButtons(boolean enable)
    {
        r0c1Button.setEnabled(enable);
        r0c0Button.setEnabled(enable);
        r0c2Button.setEnabled(enable);
        r1c0Button.setEnabled(enable);
        r1c1Button.setEnabled(enable);
        r1c2Button.setEnabled(enable);
        r2c0Button.setEnabled(enable);
        r2c1Button.setEnabled(enable);
        r2c2Button.setEnabled(enable);
    }


    public void updateButtonText(char[][] board) 
    {
        r0c0Button.setText(board[0][0] + "");
        r0c1Button.setText(board[0][1] + "");
        r0c2Button.setText(board[0][2] + "");
        r1c0Button.setText(board[1][0] + "");
        r1c1Button.setText(board[1][1] + "");
        r1c2Button.setText(board[1][2] + "");
        r2c0Button.setText(board[2][0] + "");
        r2c1Button.setText(board[2][1] + "");
        r2c2Button.setText(board[2][2] + "");
    }


    private void initVariables() 
    {
        //logIn
        logInPanel = new JPanel();
        nameTF = new JTextField(5);
        passwordTF = new JPasswordField(5);

        // North
        upPanel = new JPanel();
        markField = new JTextField(5);
        nameField = new JTextField(5);
        // Center
        centerPanel = new JPanel();
        r0c0Button = new JButton("");
        r0c1Button = new JButton("");
        r0c2Button = new JButton("");
        r1c0Button = new JButton("");
        r1c1Button = new JButton("");
        r1c2Button = new JButton("");
        r2c0Button = new JButton("");
        r2c1Button = new JButton("");
        r2c2Button = new JButton("");
        //West
        westPanel = new JPanel();
        jList = new JList<>();
        listScrollPane = new JScrollPane();
        listScrollPane.setViewportView(jList);
        jList.setLayoutOrientation(JList.VERTICAL);
        // wyłaczenie przycisków gridu
        enableButtons(false);
        // blokowanie edycji okien tekstowych
        markField.setEditable(false);
        nameField.setEditable(false);

    }




    private void initContainer()
    {
        // container dla komponentów okna
        Container c = getContentPane();
        // zatrzymuje program po zamknieciu okna
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // rozmiar okna
        setPreferredSize(new Dimension(900, 500));
        // Layout okna głownego
        setLayout(new BorderLayout());
        // Dodanie paneli do frame'a
        c.add("North", upPanel);
        c.add("Center", centerPanel);
        c.add("West", westPanel);
        setVisible(true);
        pack();
    }


    private void initPanels()
    {
        //logInPanel
        logInPanel.add(new JLabel("name:"));
        logInPanel.add(nameTF);
        logInPanel.add(Box.createHorizontalStrut(15)); // a spacer
        logInPanel.add(new JLabel("password:"));
        logInPanel.add(passwordTF);

        // Leyout panelu centralnego
        centerPanel.setLayout(new GridLayout(3, 3, 5, 5));
        // Dodawanie komponentów do paneli
        upPanel.add(new JLabel("Active Player:"));
        upPanel.add(nameField);
        upPanel.add(new JLabel("Mark:"));
        upPanel.add(markField);
        centerPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        centerPanel.add(r0c0Button);
        centerPanel.add(r0c1Button);
        centerPanel.add(r0c2Button);
        centerPanel.add(r1c0Button);
        centerPanel.add(r1c1Button);
        centerPanel.add(r1c2Button);
        centerPanel.add(r2c0Button);
        centerPanel.add(r2c1Button);
        centerPanel.add(r2c2Button);
        westPanel.add(listScrollPane);

    }

    public void winnerPop(GameState gamestate)
    {
        JFrame frame = new JFrame("WINNER");
        JOptionPane.showMessageDialog(frame,"The winner is " + gamestate.getActiveName());
    }
    public void tiePop()
    {
        JFrame frame = new JFrame("TIE");
        JOptionPane.showMessageDialog(frame,"There was a tie!");
    }





    public void addr0c0Listener(ActionListener listener) {r0c0Button.addActionListener(listener);}

    public void addr0c1Listener(ActionListener listener)
    {
        r0c1Button.addActionListener(listener);
    }

    public void addr0c2Listener(ActionListener listener) 
    {
        r0c2Button.addActionListener(listener);
    }

    public void addr1c0Listener(ActionListener listener) 
    {
        r1c0Button.addActionListener(listener);
    }

    public void addr1c1Listener(ActionListener listener) {r1c1Button.addActionListener(listener);}

    public void addr1c2Listener(ActionListener listener) {r1c2Button.addActionListener(listener);}

    public void addr2c0Listener(ActionListener listener) 
    {
        r2c0Button.addActionListener(listener);
    }

    public void addr2c1Listener(ActionListener listener) 
    {
        r2c1Button.addActionListener(listener);
    }

    public void addr2c2Listener(ActionListener listener) {r2c2Button.addActionListener(listener);}

    public void setMarkField(String text) { this.markField.setText(text); }
    public void setNameField(String text) { this.nameField.setText(text); }
}
