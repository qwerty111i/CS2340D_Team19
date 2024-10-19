import java.util.ArrayList;

public interface Leader {
    String checkProgress(Task task, ArrayList<Task> tasks);
    void assignTasks(Member member, Task task);
}