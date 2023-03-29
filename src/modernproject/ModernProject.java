package modernproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModernProject extends Application {
    public static UserInfo signedUser;
    public static List<Tasks> taskLL, completedLL, sortedLL;
    public static ArrayList<Events> eventsLL;
    public static StackLinkedList journalLL;
    public static File directory;
    
    @Override
    public void start(Stage stage) throws Exception {
        //Opens the GUI Panels
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("HomeControlSceneUI.fxml"))));
        stage.show();
        stage.setResizable(false);
        stage.setTitle("Task Manager");
        //Sets up all limitations and title of the start GUI Panel

        //Initialize Linked List that will be needed for this program
        taskLL = new ArrayList<>();
        completedLL = new ArrayList<>();
        sortedLL = new ArrayList<>();
        eventsLL = new ArrayList<Events>();
        journalLL = new StackLinkedList();
        setDirectory();
    }
    
    public static void setSignedUser(UserInfo user) {
    	signedUser = user;
    }

    public static List<Tasks> getLinkedList(){
        return taskLL;
    }

    public static void setLL() {
        taskLL = new ArrayList<Tasks>();
        completedLL = new ArrayList<Tasks>();
        eventsLL = new ArrayList<Events>();
        journalLL = new StackLinkedList();
    }

    public static void setSortedLL(){
        sortedLL = new ArrayList<Tasks>();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setDirectory() {
        directory = new File("Saved-Files-For-Users");
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public static void updateDir(File dir){
        directory = dir;
    }
}
