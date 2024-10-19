import java.util.ArrayList;

public class TeamMember implements Member {
	private ArrayList<Task> tasks;
	
	private String role;
	private String name;
	private String email;


	public TeamMember(String role, String name, String email) {
		this.role = role;
		this.name = name;
		this.email = email;
	}

	// Member joining project
	@Override
	public void joinProject(Project project) {
		project.addMember(this);
	}
	
	// Member leaving project
	@Override
	public void leaveProject(Project project) {
		project.removeMember(this);
		
	}

	// Adds a task to a specific member
    @Override
    public void addTask(Task task) {
        tasks.add(task);
    }
	
}
