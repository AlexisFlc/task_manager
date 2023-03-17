package modernproject;

import java.sql.*;

public class MysqlConSingleton {

    private static MysqlConSingleton instance = null;
    private Connection con;

    private MysqlConSingleton() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/calentask","root","mdp");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static MysqlConSingleton getInstance() {
        if (instance == null) {
            instance = new MysqlConSingleton();
        }
        return instance;
    }


    public void closeConnection() {
        try {
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Connection getCon() {
        return con;
    }
}
