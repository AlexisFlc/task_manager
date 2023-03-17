package modernproject;

public class Task {

    private String taskName;
    private String date;
    private String type;
    private String desc;
    private String status;

    private int id;


    public Task(String taskName, String date, String type, String desc, String status) {
        this.taskName = taskName;
        this.date = date;
        this.type = type;
        this.desc = desc;
        this.status = status;
        this.id = id;
    }


    public String getTaskName() {
        return taskName;
    }

    public String getDueDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public void setTaskName(String name) {
        taskName = name;
    }

    public void setDueDate(String ddate) {
        date = ddate;
    }

    public void setType(String nType) {
        type = nType;
    }

    public void setDesc(String nDesc) {
        desc = nDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
