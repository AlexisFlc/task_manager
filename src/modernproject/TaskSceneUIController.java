package modernproject;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modernproject.services.TaskServices;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class TaskSceneUIController implements Initializable {
    @FXML private TextField searchField;
    @FXML private TextField nameText;
    @FXML private DatePicker dateText;
    @FXML private TextField dateField;
    @FXML private ListView<String> taskListView;
    @FXML private ListView<String> eventListView;
    @FXML private TextArea taskSelectedArea;
    private String[] taskName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
        taskSelectedArea.setText("Nothing is Selected");
    }

    @FXML public void handleMouseClick(MouseEvent arg0) throws SQLException {
        String taskName = taskListView.getSelectionModel().getSelectedItem();
        Tasks t = null;
        TaskServices o = new TaskServices();
        if(taskName == null || taskName.isEmpty()) {
            taskSelectedArea.setText("Nothing is Selected");
        }else {
            for (Tasks tasks : ModernProject.taskLL) {
                if (tasks.getTaskName().equals(taskName)) {
                    t = tasks;
                    break;
                }

            }
            if (t != null)
                o.deleteTask(t.getTaskName());
            else {
                System.out.println("Task not found");
            }
            taskSelectedArea.setText("Task Name: " + t.getTaskName() + "\n" + "Due Date: " + t.getDueDate()
                    + "\n" + "Type: " + t.getType() + "\n" + "Description: " + t.getDesc());
        }
    }
    
    @FXML private void homeButtonAction(ActionEvent event) throws IOException {
    	if(ModernProject.signedUser != null) {
    		Parent homeParent = FXMLLoader.load(getClass().getResource("HomeSceneUI.fxml"));
            Scene homeScene = new Scene(homeParent);
            Stage homeWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
            
            homeWindow.setScene(homeScene);
            homeWindow.show();
            homeWindow.setResizable(false);
            homeWindow.setTitle("Task Manager");
    	} else {
    		Parent homeParent = FXMLLoader.load(getClass().getResource("HomeControlSceneUI.fxml"));
            Scene homeScene = new Scene(homeParent);
            Stage homeWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
            
            homeWindow.setScene(homeScene);
            homeWindow.show();
            homeWindow.setResizable(false);
            homeWindow.setTitle("Task Manager");
    	}
    }
    
    @FXML private void taskButtonAction(ActionEvent event) throws IOException {
        Parent taskParent = FXMLLoader.load(getClass().getResource("TaskSceneUI.fxml"));
        Scene taskScene = new Scene(taskParent);
        Stage taskWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        taskWindow.setScene(taskScene);
        taskWindow.show();
        taskWindow.setResizable(false);
        taskWindow.setTitle("Task Manager - Task");
    }

    
    @FXML private void addBtnAction(ActionEvent event) throws IOException {
    	Parent inputParent = FXMLLoader.load(getClass().getResource("TaskInputSceneUI.fxml"));
        Scene inputScene = new Scene(inputParent);
        Stage inputWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        inputWindow.setScene(inputScene);
        inputWindow.show();
        inputWindow.setResizable(false);
        inputWindow.setTitle("Task Manager - Input");
    }

    @FXML private void deleteBtnAction(ActionEvent event) throws IOException, SQLException {
        String taskName = taskListView.getSelectionModel().getSelectedItem();
        TaskServices o = new TaskServices();
        Tasks t = null;
        if(!(taskName == null || taskName.isEmpty())) {
            for (Tasks tasks : ModernProject.taskLL) {
                if (tasks.getTaskName().equals(taskName)) {
                    t = tasks;
                    break;
                }

            }
           if (t != null)
             o.deleteTask(t.getTaskName());
           else {
               System.out.println("Task not found");
              }

            Parent taskParent = FXMLLoader.load(getClass().getResource("TaskSceneUI.fxml"));
            Scene taskScene = new Scene(taskParent);
            Stage taskWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
            taskWindow.setScene(taskScene);
            taskWindow.show();
            taskWindow.setResizable(false);
            taskWindow.setTitle("Task Manager - Task");
        }
    }

    @FXML private void completeBtn(ActionEvent event) throws IOException{
        String taskName = taskListView.getSelectionModel().getSelectedItem();
        TaskServices o = new TaskServices();

        if(!(taskName == null || taskName.isEmpty())) {
            Tasks t = o.getTasks(taskName);
            t.setStatus("Completed");

            Parent taskParent = FXMLLoader.load(getClass().getResource("TaskSceneUI.fxml"));
            Scene taskScene = new Scene(taskParent);
            Stage taskWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
            taskWindow.setScene(taskScene);
            taskWindow.show();
            taskWindow.setResizable(false);
            taskWindow.setTitle("Task Manager - Task");
        }
    }

    @FXML private void updateBtn(ActionEvent event) throws IOException, SQLException {
        String taskName = taskListView.getSelectionModel().getSelectedItem();
        TaskServices o = new TaskServices();
        if(!(taskName == null || taskName.isEmpty())) {
            Tasks t = o.getTasks(taskName);
            String[] edits = t.transferEditToFile(taskSelectedArea);
            t.setTaskName(edits[0]);
            t.setDueDate(edits[1]);
            t.setType(edits[2]);
            t.setDesc(edits[3]);

            o.updateTask(t);


            Parent taskParent = FXMLLoader.load(getClass().getResource("TaskSceneUI.fxml"));
            Scene taskScene = new Scene(taskParent);
            Stage taskWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
            taskWindow.setScene(taskScene);
            taskWindow.show();
            taskWindow.setResizable(false);
            taskWindow.setTitle("Task Manager - Task");
        }
    }

    @FXML private void dateTextAction(ActionEvent event) {
        LocalDate localDate = dateText.getValue();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        dateField.setText(date.format(localDate));
    }

    @FXML private void createBtnAction(ActionEvent event) throws IOException, NullPointerException {
        String[] eventData = new String[2];
        LocalDate localDate = dateText.getValue();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("EEEE MMMM dd, yyyy");
        eventData[0] = nameText.getText();
        eventData[1] = date.format(localDate);
        if(!(eventData[0].isEmpty() || eventData[1].isEmpty())) {
            Events o = new Events(eventData);
            ModernProject.eventsLL.insert(o);
            ModernProject.eventsLL.listToFile("eventList.txt");

            Parent taskParent = FXMLLoader.load(getClass().getResource("TaskSceneUI.fxml"));
            Scene taskScene = new Scene(taskParent);
            Stage taskWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
            taskWindow.setScene(taskScene);
            taskWindow.show();
            taskWindow.setResizable(false);
            taskWindow.setTitle("Task Manager - Task");
        }
    }

    @FXML private void deleteEventBtnAction(ActionEvent event) throws IOException{
        String[] selected = eventListView.getSelectionModel().getSelectedItem().split(" on ", 2);
        if(!(selected[0] == null || selected[0].isEmpty())) {
            Events o = ModernProject.eventsLL.findNode(selected[0]);
            ModernProject.eventsLL.removeNode(o);
            ModernProject.eventsLL.listToFile("eventList.txt");

            Parent taskParent = FXMLLoader.load(getClass().getResource("TaskSceneUI.fxml"));
            Scene taskScene = new Scene(taskParent);
            Stage taskWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
            taskWindow.setScene(taskScene);
            taskWindow.show();
            taskWindow.setResizable(false);
            taskWindow.setTitle("Task Manager - Task");
        }
    }

    @FXML private void searchBtnAction(ActionEvent event) throws IOException{
        int index = binarySearch(taskName, searchField.getText());
        taskListView.getSelectionModel().select(index);
    }

    //Searches for the taskName inside the ListView using binary search
    private int binarySearch(String[] taskName, String key){
        int lb = 0, ub = taskName.length - 1;
        while (lb <= ub) {
            int m = lb + (ub - lb) / 2;
            int res = key.compareToIgnoreCase(taskName[m]);
            if (res == 0)
                return m;
            if (res > 0)
                lb = m + 1;
            else
                ub = m - 1;
        }
        return -1;
    }

    private void loadData() {
        int i = 0;
        for (Tasks task : ModernProject.taskLL){
            taskName[i] = task.getTaskName();
            i++;
        }
        String[] eventNames = ModernProject.eventsLL.displayNodes();
        taskListView.getItems().addAll(taskName);
        eventListView.getItems().addAll(eventNames);
    }
}
