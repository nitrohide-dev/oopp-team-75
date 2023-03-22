package commons.models;

/**
 * Model for better communication with the server when creating task
 */
public class CreateTaskModel {

	public final String name;
	public final long taskListId;

	/**
	 * Constructor for the model
	 * @param name - The name for the task
	 * @param taskListId - The id of the list where we want to put the task
	 */
	public CreateTaskModel(String name, long taskListId) {
		this.name = name;
		this.taskListId = taskListId;
	}
}
