public class SpecificTask implements Priority, Task {
    private String title;
    private String description;
    private String dueDate;
    private String status;
    private int priority;
    private int frequency;

    public BaseTask(String title, String description, String dueDate, String status, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
