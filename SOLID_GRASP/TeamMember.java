public class TeamMember extends Member {
	public String role;
	public String name;
	public String email;

	public TeamMember(String role, String name, String email) {
		this.role = role;
		this.name = name;
		this.email = email;
	}

	public void joinProject(Project project) {
		//join 
		project.addMember(TeamMember);
	}
	
	public void leaveProject(Project project) {
		//leave
		project.removeMember(TeamMember);
		
	}
	
}
