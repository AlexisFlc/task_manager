package modernproject;

public class UserInfo implements java.io.Serializable{
	private String firstName;
    private String lastName;
    private String userName;
    private String password;

    private int id;
    
    public UserInfo(String firstName, String lastName, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }

    public UserInfo(String firstName, String lastName, String userName, String password, int id){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.id = id;
    }
    
    public String getFirstName() {
    	return firstName;
    }
    
    public String getLastName() {
    	return lastName;
    }
    
    public String getUserName() {
    	return userName;
    }
    
    public String getPassword() {
    	return password;
    }
    
    public void setName(String firstName, String lastName) {
    	this.firstName = firstName;
    	this.lastName = lastName;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }
    
    public void setUserName(String userName) {
    	this.userName = userName;
    }

    public int getId() {
    	return id;
    }

    public void setId(int id) {
    	this.id = id;
    }
}
