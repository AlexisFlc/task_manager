package modernproject.services;

import modernproject.MysqlConSingleton;
import modernproject.Task;
import modernproject.Tasks;
import modernproject.UserAction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public Task getTaskByNameAndUserID(String taskName) throws SQLException{
        PreparedStatement stmt = mysqlCon.getCon().prepareStatement("select * from tasks where name = ? and user_id = ?");
        stmt.setString(1, taskName);
        stmt.setInt(2, signedUser.getId());
        ResultSet rs = stmt.executeQuery();

        if(rs.next()){
            String name = rs.getString("name");
            String desc = rs.getString("description");
            String type = rs.getString("type");
            String dueDate = rs.getString("due_date");
            String status = rs.getString("status");

            return new Task(name, desc, type, dueDate, status);
        }

        return null;
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
}
