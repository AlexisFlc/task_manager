package modernproject.services;

import modernproject.Events;
import modernproject.MysqlConSingleton;
import modernproject.UserAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            PreparedStatement stmt = mysqlCon.getCon().prepareStatement("insert into events (title, user_id, date, status) values (?, ?, ?, ?)");
            stmt.setString(1, title);
            stmt.setInt(2, signedUser.getId());
            stmt.setString(3, date);
            stmt.setString(4, status);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Events getEvent(String eventTitle, int userID) throws SQLException{
        try {
            PreparedStatement stmt = mysqlCon.getCon().prepareStatement("select * from events where title = ? and user_id = ?");
            stmt.setString(1, eventTitle);
            stmt.setInt(2, userID);
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

    public List<Events> getAllEvents() throws SQLException {
        List<Events> events = new ArrayList<>();
        try {
            PreparedStatement stmt = mysqlCon.getCon().prepareStatement("select * from events where user_id = ? and status = 'Incomplete'");
            stmt.setInt(1, signedUser.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("title");
                String date = rs.getString("date");
                events.add(new Events(name, date));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public void deleteEvent(String eventTitle, int userID) throws SQLException {
        try {
            PreparedStatement stmt = mysqlCon.getCon().prepareStatement("update events set status ='Complete'  where title = ? and user_id = ?");
            stmt.setString(1, eventTitle);
            stmt.setInt(2, signedUser.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
