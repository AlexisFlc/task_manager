package modernproject.services;

import modernproject.LinkedList;
import modernproject.MysqlConSingleton;
import modernproject.Tasks;
import modernproject.UserAction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static modernproject.ModernProject.signedUser;

public class TaskServices {

    private static final MysqlConSingleton mysqlCon = MysqlConSingleton.getInstance();


    public boolean checkIfTaskExists(String taskName, String username) throws SQLException {
        UserAction userAction = new UserAction();
        int id = userAction.getUserInfo(username).getId();

        PreparedStatement stmt = mysqlCon.getCon().prepareStatement("select * from tasks where name = ? and user_id = ?");
        stmt.setString(1, taskName);
        stmt.setInt(2, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return true;
        }
        return false;
    }


    public Tasks getTasks(String name){
        Tasks task = null;
        try {
            PreparedStatement stmt = mysqlCon.getCon().prepareStatement("select * from tasks where name = ? and user_id = ?");
            stmt.setString(1, name);
            stmt.setInt(2, signedUser.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                task = new Tasks(rs.getString("name"), rs.getString("due_date"), rs.getString("type"), rs.getString("description"), rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    public void addTask(Tasks task) throws SQLException {
        PreparedStatement stmt = mysqlCon.getCon().prepareStatement("insert into tasks (name, description, user_id,type,due_date,status) values (?, ?, ?, ?, ?, ?)");
        stmt.setString(1, task.getTaskName());
        stmt.setString(2, task.getDesc());
        stmt.setInt(3,  signedUser.getId());
        stmt.setString(4, task.getType());
        stmt.setString(5, task.getDueDate());
        stmt.setString(6, task.getStatus());
        stmt.executeUpdate();
    }



    public void updateTask(Tasks task) throws SQLException{
        PreparedStatement stmt = mysqlCon.getCon().prepareStatement("update tasks set name = ?, description = ?, type = ?, due_date = ?, status = ? where name = ? and user_id = ?");
        stmt.setString(1, task.getTaskName());
        stmt.setString(2, task.getDesc());
        stmt.setString(3, task.getType());
        stmt.setString(4, task.getDueDate());
        stmt.setString(5, task.getStatus());
        stmt.setString(6, task.getTaskName());
        stmt.setInt(7, signedUser.getId());
        stmt.executeUpdate();
    }

    public void deleteTask(String name) throws SQLException{
        PreparedStatement stmt = mysqlCon.getCon().prepareStatement("delete from tasks where name = ? and user_id = ?");
        stmt.setString(1, name);
        stmt.setInt(2, signedUser.getId());
        stmt.executeUpdate();
    }

    public List<Tasks> getAllNotDone(){
        List<Tasks> tasks = new ArrayList<>();
        try {
            PreparedStatement stmt = mysqlCon.getCon().prepareStatement("select * from tasks where user_id = ? and status = ?");
            stmt.setInt(1, signedUser.getId());
            stmt.setString(2, "Not Done");
            ResultSet rs = stmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                tasks.add(new Tasks(rs.getString("name"), rs.getString("due_date"), rs.getString("type"), rs.getString("description"), rs.getString("status")));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }


    public List<Tasks> getAllCompleted(){
        List<Tasks> tasks = new ArrayList<Tasks>();
        try {
            PreparedStatement stmt = mysqlCon.getCon().prepareStatement("select * from tasks where user_id = ? and status = ?");
            stmt.setInt(1, signedUser.getId());
            stmt.setString(2, "Completed");
            ResultSet rs = stmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                tasks.add(new Tasks(rs.getString("name"), rs.getString("due_date"), rs.getString("type"), rs.getString("description"), rs.getString("status")));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}
