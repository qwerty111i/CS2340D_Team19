public class TeamLeader extends Leader {
    private String name;
    private String email;
    public TeamLeader(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public String checkProgress() {
        String result = "Status of Tasks in List: ";
        for (int i = 0; i < tasks.size(); i++) {
            result += task.get(i).getStatus();
            if (i < tasks.size() - 1) {
                result += ", ";
            }
        }
        return result;
    }
    public void assignTasks(Project project, Task task) {
        this.addTask(); //unfinished
    }
}