package modernproject.services;

import modernproject.Events;
import modernproject.MysqlConSingleton;
import modernproject.UserAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static modernproject.ModernProject.signedUser;

public class EventService {

    private static final MysqlConSingleton mysqlCon = MysqlConSingleton.getInstance();

    public boolean checkIfEventExist(String eventTitle, String username) throws SQLException {
        UserAction userAction = new UserAction();
        int id = signedUser.getId();

        try {
            PreparedStatement stmt = mysqlCon.getCon().prepareStatement("select * from events where title = ? and user_id = ?");

            stmt.setString(1, eventTitle);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addEvent(String title, String date, String status) throws SQLException {
        try {
            PreparedStatement stmt = mysqlCon.getCon().prepareStatement("insert into event (title, user_id, date, status) values (?, ?, ?, ?)");
            stmt.setString(1, title);
            stmt.setInt(2, signedUser.getId());
            stmt.setString(3, date);
            stmt.setString(4, status);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Events getEvent(String eventTitle) throws SQLException{
        try {
            PreparedStatement stmt = mysqlCon.getCon().prepareStatement("select * from events where name = ? and user_id = ?");
            stmt.setString(1, eventTitle);
            stmt.setInt(2, signedUser.getId());
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                String status = rs.getString("status");
                if (status.equals("Complete")) {
                    return null;
                }
                else {
                    String name = rs.getString("name");
                    String date = rs.getString("date");

                    return new Events(name, date);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
