public class SpecificTask implements Priority, Task, Recurring {
    private String title;
    private String description;
    private String dueDate;
    private String status;
    private int priority;
    private int frequency;

    public SpecificTask(String title, String description, String dueDate, String status, int priority, int frequency) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
        this.frequency = frequency;
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
