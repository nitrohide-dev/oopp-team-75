package server.api.services;

import commons.Task;
import commons.TaskList;
import commons.models.CreateTaskModel;
import org.springframework.stereotype.Service;
import server.database.ListRepository;
import server.database.TaskRepository;
import server.exceptions.CannotCreateTask;
import server.exceptions.ListDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import java.util.List;

@Service
public class TaskService {

	private final TaskRepository taskRepository;
	private final ListRepository listRepository;

	public TaskService(TaskRepository taskRepository, ListRepository listRepository) {
		this.taskRepository = taskRepository;
		this.listRepository = listRepository;
		int x = 11;

	}

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	public Task getTaskById(long id) throws TaskDoesNotExist {
		if (!taskRepository.existsById(id))
			throw new TaskDoesNotExist("There exists no task with the provided id.");
		return taskRepository.findById(id).get();
	}

	public Task createTask(CreateTaskModel model) throws ListDoesNotExist, CannotCreateTask {
		if (!listRepository.existsById(model.taskListId)) {
			throw new ListDoesNotExist("There is no list with the provided id.");
		}
		TaskList taskList = listRepository.getById(model.taskListId);
		Task task = taskList.createTask(model.name);
		listRepository.save(taskList);
		return task;
	}

	public void deleteTaskById(long id) throws TaskDoesNotExist {
		if (!taskRepository.existsById(id))
			throw new TaskDoesNotExist("There is no task with the provided id.");
		taskRepository.deleteById(id);
	}

}
