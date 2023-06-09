package modernproject;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Label;
import modernproject.services.EventService;
import modernproject.services.TaskServices;

import java.text.DecimalFormat;

import static modernproject.ModernProject.*;

public class HomeSceneUIController implements Initializable{
    @FXML private Label ntNumLabel;
    @FXML private Label ctNumLabel;
    @FXML private Label eNumLabel;
    @FXML private Label ppNumLabel;
    @FXML private Label time;
    @FXML private Label nameMessageLabel;
    @FXML private Label nameMessageLabel1;
    @FXML private Label date;
    @FXML private ListView<String> deadlineList;
    private int minute, hour, second;
    private int cmonth, cday, cyear;

    public EventService eventService = new EventService();

    public TaskServices taskService = new TaskServices();

    @Override
	public void initialize(URL location, ResourceBundle resources) {
        nameMessageLabel.setText("Welcome " + signedUser.getFirstName() + "!");
    	Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
            second = LocalDateTime.now().getSecond();
            minute = LocalDateTime.now().getMinute();
            hour = LocalDateTime.now().getHour();
            cmonth = LocalDateTime.now().getMonthValue();
            cday = LocalDateTime.now().getDayOfMonth();
            cyear = LocalDateTime.now().getYear();
            time.setText((hour > 12 ? (hour - 12) : hour) + ":" + (minute < 10 ? ("0" + minute) : minute) + ":" 
            			+ (second < 10 ? ("0" + second) : second) + " " + (hour >= 12 ? "PM" : "AM") );
            date.setText(cmonth + "/" + cday + "/" + cyear);
        }),
             new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        try{
            sortedLL = sortTasksByDueDate(taskLL);
        }catch(Exception e){
            System.out.println("Error: " + e);
        }
        try {
            loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Tasks> sortTasksByDueDate(List<Tasks> taskList) {
        if(taskList.size() <= 1) {
            return taskList;
        }

        int middle = taskList.size() / 2;
        List<Tasks> left = sortTasksByDueDate(taskList.subList(0, middle));
        List<Tasks> right = sortTasksByDueDate(taskList.subList(middle, taskList.size()));

        return mergeTasksByDueDate(left, right);
    }

    private List<Tasks> mergeTasksByDueDate(List<Tasks> left, List<Tasks> right) {
        List<Tasks> result = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;

        while(leftIndex < left.size() && rightIndex < right.size()) {
            if(left.get(leftIndex).getDueDate().compareTo(right.get(rightIndex).getDueDate()) < 0) {
                result.add(left.get(leftIndex));
                leftIndex++;
            } else {
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        result.addAll(left.subList(leftIndex, left.size()));
        result.addAll(right.subList(rightIndex, right.size()));

        return result;
    }

	private void loadData() throws SQLException {
        ntNumLabel.setText(String.format("%03d%n", taskService.getAllNotDone().size()));
        ctNumLabel.setText(String.format("%03d%n", completedLL.size()));
        eNumLabel.setText(String.format("%03d%n", eventService.getAllEvents().size()));

        DecimalFormat dFormat = new DecimalFormat("00.00");
        double pp = ((double)completedLL.size() / (double)taskLL.size()) * 100;
        ppNumLabel.setText(dFormat.format(pp) + "%");

        String[] eventName = new String[eventService.getAllEvents().size()];
        for(int i = 0; i < eventService.getAllEvents().size(); i++) {
        	eventName[i] = eventService.getAllEvents().get(i).getEventName() + "\n\tdue by " + eventService.getAllEvents().get(i).getEventDate();
        }

        String[] taskName = new String[taskService.getAllNotDone().size()];
        for(int i = 0; i < taskService.getAllNotDone().size(); i++) {
        	taskName[i] = taskService.getAllNotDone().get(i).getTaskName() + "\n\tdue by " + taskService.getAllTasks().get(i).getDueDate();
        }

        /*String[] taskName = new String[taskLL.size()];
        for(int i = 0; i < taskLL.size(); i++){
            taskName[i] = "• " + taskLL.get(i).getTaskName() + "\n\tdue by " + taskLL.get(i).getDueDate();
        }*/
        deadlineList.getItems().addAll(taskName);
        deadlineList.getItems().addAll(eventName);
    }

    @FXML void homeButtonAction(ActionEvent event) throws IOException {
    	Parent taskParent = FXMLLoader.load(getClass().getResource("HomeSceneUI.fxml"));
        Scene taskScene = new Scene(taskParent);
        Stage taskWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        taskWindow.setScene(taskScene);
        taskWindow.show();
        taskWindow.setResizable(false);
        taskWindow.setTitle("Task Manager - Home");
    }

    @FXML void taskButtonAction(ActionEvent event) throws IOException {
    	Parent taskParent = FXMLLoader.load(getClass().getResource("TaskSceneUI.fxml"));
        Scene taskScene = new Scene(taskParent);
        Stage taskWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        taskWindow.setScene(taskScene);
        taskWindow.show();
        taskWindow.setResizable(false);
        taskWindow.setTitle("Task Manager - Task");
    }

    @FXML void calendarButtonAction(ActionEvent event) throws IOException {
    	Parent calendarParent = FXMLLoader.load(getClass().getResource("CalendarSceneUI.fxml"));
        Scene calendarScene = new Scene(calendarParent);
        Stage calendarWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        calendarWindow.setScene(calendarScene);
        calendarWindow.show();
        calendarWindow.setResizable(false);
        calendarWindow.setTitle("Task Manager - Calendar");
    }

    @FXML void journalButtonAction(ActionEvent event) throws IOException {
    	Parent journalParent = FXMLLoader.load(getClass().getResource("JournalSceneUI.fxml"));
        Scene journalScene = new Scene(journalParent);
        Stage journalWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        journalWindow.setScene(journalScene);
        journalWindow.show();
        journalWindow.setResizable(false);
        journalWindow.setTitle("Task Manager - Journal");

        ErrorHandle o = new ErrorHandle();
        o.warningMessage();
    }
    
    @FXML void logoutBtnAction(ActionEvent event) throws IOException {
    	setSignedUser(null);
    	setDirectory();
    	setLL();
    	setSortedLL();
    	
    	Parent taskParent = FXMLLoader.load(getClass().getResource("HomeControlSceneUI.fxml"));
        Scene taskScene = new Scene(taskParent);
        Stage taskWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        taskWindow.setScene(taskScene);
        taskWindow.show();
        taskWindow.setResizable(false);
        taskWindow.setTitle("Task Manager - Home");
    }
}
