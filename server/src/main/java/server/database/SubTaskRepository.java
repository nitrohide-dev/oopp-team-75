package server.database;

import commons.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {

    /**
     * Finds the subtasks of a specific task in the database
     @param task_id the id of the task to look up subtasks of
     @return the set of subtasks corresponding to a given task
     */
    @Query("SELECT * FROM TAG t1 JOIN TAG_TASKS t2 ON t1.ID = t2.TAG_ID WHERE t2.TASK_ID = ?1 ")
    Collection<SubTask> getSubTasksOfTask(long task_id);
}