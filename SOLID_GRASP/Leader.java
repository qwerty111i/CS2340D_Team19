public interface Leader {
    void checkProgress();
    void assignTasks(Project project);
}