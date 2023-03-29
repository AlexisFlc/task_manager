package modernproject;

import java.sql.*;

public class MysqlConSingleton {

    private static MysqlConSingleton instance = null;
    private Connection con;

    private MysqlConSingleton() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/calentask","root","root");
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


    public UserInfo getUserInfo(String username) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("select * from users where username = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString("last_name");
            String firstName = rs.getString("first_name");
            String password = rs.getString("password");
            String usernamee = rs.getString("username");
            UserInfo user = new UserInfo(name, firstName, usernamee, password);
            return user;
        }
        return null;
    }

    public void addUser(UserInfo user) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("insert into users (last_name, first_name, password, username) values (?, ?, ?, ?)");
        System.out.println(user.getUserName()+ user.getFirstName()+user.getLastName()+ user.getPassword());
        stmt.setString(1, user.getFirstName());
        stmt.setString(2, user.getLastName());
        stmt.setString(3, user.getPassword());
        stmt.setString(4, user.getUserName());
        stmt.executeUpdate();
    }
}
