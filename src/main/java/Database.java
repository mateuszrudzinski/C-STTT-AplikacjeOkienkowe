import javax.swing.*;
import java.sql.*;

public class Database {
    public void addPlayerToDBQUERY(int id, String name, String password) throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tttdb", "root", "admin");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * from players WHERE name = '"+name+"' AND password = '"+password+"'");
        if (!rs.next())
        {
            statement.executeUpdate("INSERT INTO players(idplayers,name,password) VALUES('"+id+"','"+name+"','"+password+"')");
        }

    }

    public int getLastId() throws SQLException{
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tttdb", "root", "admin");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT max(idplayers) FROM players");
        int id;
        if (rs.next())
        {
            id = rs.getInt(1);
        }else
        {
            id = 1;
        }
        return id;
    }

    public int getLastGameId() throws SQLException{
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tttdb", "root", "admin");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT max(idgames) FROM games");
        int id;
        if (rs.next())
        {
            id = rs.getInt(1);
        }else
        {
            id = 1;
        }
        return id;
    }

    public void insertMovetoDB(int idGames,int moveX,int moveY, char mark,String name) throws SQLException{
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tttdb", "root", "admin");
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO games(idgames,x,y,mark,name) VALUES('"+idGames+"','"+moveX+"','"+moveY+"','"+mark+"','"+name+"')");
    }
    public DefaultListModel<String> getListOfMoves(int idGames) throws SQLException {
        DefaultListModel<String> dm = new DefaultListModel<>();
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tttdb", "root", "admin");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from games where idgames = '"+idGames+"'");

        while(rs.next())
        {
            int x = rs.getInt(2);
            int y = rs.getInt(3);
            String name = rs.getString(5);
            String mark = rs.getString(4);
            dm.addElement("==================");
            dm.addElement("X POS: "+x);
            dm.addElement("Y POS: "+y);
            dm.addElement("PLAYER NAME: "+name);
            dm.addElement("PLAYER MARK: "+mark);
        }
        return dm;
    }

}
