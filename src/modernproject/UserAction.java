package modernproject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserAction {
	private static ArrayList <UserInfo> userList = new ArrayList<UserInfo>();
	private static MysqlConSingleton  mysqlCon = MysqlConSingleton.getInstance();

	//takes the incoming UserInfo object and serializes the information into a text file
	public static void serializeSignUp(UserInfo user) throws IOException, ClassNotFoundException, SQLException {
		mysqlCon.addUser(user);
	}

	//the incoming String will be the username of the user and it will read the database for the information
	public static UserInfo readUserData(String username) throws IOException, ClassNotFoundException, SQLException {
		UserInfo user = mysqlCon.getUserInfo(username);
		return user;
	}

}
