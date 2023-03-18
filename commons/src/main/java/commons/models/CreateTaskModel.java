package commons.models;

public class CreateTaskModel {

	public final String name;
	public final long taskListId;

	public CreateTaskModel(String name, long taskListId) {
		this.name = name;
		this.taskListId = taskListId;
	}
}
