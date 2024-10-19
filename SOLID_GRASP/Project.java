import java.util.ArrayList;

public class Project {
    private ArrayList<Task> tasks;
    private ArrayList<Member> members;
    private Leader leader;

    private String name;
    private String description;
    private String startDate;
    private String endDate;

    public Project(String name, String description, String startDate, String endDate, TeamLeader leader) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leader = leader;

        tasks = new ArrayList<>();
        members = new ArrayList<>();
    }

    // Adds a task
    public void addTask(Task task) {
        tasks.add(task);
    }

    // Removes a task
    public void removeTask(Task task) {
        boolean foundTask = false;
        for (Task currentTask: tasks) {
            if (currentTask.equals(task)) {
                tasks.remove(task);
                foundTask = true;
                System.out.println("Removed the task!");
            }
        }

        if (foundTask == false) {
            System.out.println("Task not found");
        }
    }

    // Adds a new member
    public void addMember(TeamMember member) {
        members.add(member);
    }

    // Removes a member
    public void removeMember(TeamMember member) {
        boolean foundMember = false;
        for (Member currentMember: members) {
            if (currentMember.equals(member)) {
                members.remove(member);
                foundMember = true;
                System.out.println("Removed the member!");
            }
        }

        if (foundMember == false) {
            System.out.println("Member not found");
        }
    }
}
