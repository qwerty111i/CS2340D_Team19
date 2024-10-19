import java.util.ArrayList;

public class TeamLeader implements Leader {
    private String name;
    private String email;

    public TeamLeader(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Checks the progress of tasks for a specific member
    @Override
    public String checkProgress(Task task, ArrayList<Task> tasks) {
        String result = "Tasks needed to be completed: ";
        for (Task currentTask : tasks) {
            result += currentTask + "\n";
        }

        return result;
    }

    // Assigns tasks to a specific member
    @Override
    public void assignTasks(Member member, Task task) {
        member.addTask(task);
    }
}