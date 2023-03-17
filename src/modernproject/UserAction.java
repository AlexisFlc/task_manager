package modernproject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserAction {
	private static final MysqlConSingleton  mysqlCon = MysqlConSingleton.getInstance();

	//takes the incoming UserInfo object and serializes the information into a text file
	public static void serializeSignUp(UserInfo user) throws IOException, ClassNotFoundException, SQLException {
		addUser(user);
	}

	//the incoming String will be the username of the user and it will read the database for the information
	public static UserInfo readUserData(String username) throws IOException, ClassNotFoundException, SQLException {
		UserInfo user = getUserInfo(username);
		return user;
	}


	public static UserInfo getUserInfo(String username) throws SQLException {
		PreparedStatement stmt = mysqlCon.getCon().prepareStatement("select * from users where username = ?");
		stmt.setString(1, username);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			String name = rs.getString("last_name");
			String firstName = rs.getString("first_name");
			String password = rs.getString("password");
			String usernamee = rs.getString("username");
			int id = rs.getInt("id");
			UserInfo user = new UserInfo(name, firstName, usernamee, password, id);
			return user;
		}
		return null;
	}

	public static void addUser(UserInfo user) throws SQLException {
		PreparedStatement stmt = mysqlCon.getCon().prepareStatement("insert into users (last_name, first_name, password, username) values (?, ?, ?, ?)");
		System.out.println(user.getUserName()+ user.getFirstName()+user.getLastName()+ user.getPassword());
		stmt.setString(1, user.getFirstName());
		stmt.setString(2, user.getLastName());
		stmt.setString(3, user.getPassword());
		stmt.setString(4, user.getUserName());
		stmt.executeUpdate();
	}

}
