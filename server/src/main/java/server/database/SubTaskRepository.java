package server.database;

import commons.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {

    /**
     * Finds the subtasks of a specific task in the database
     @param task_id the id of the task to look up subtasks of
     @return the set of subtasks corresponding to a given task
     */
    @Query(value = "SELECT * FROM SUB_TASK s1 WHERE s1.TASK_ID = ?1 ",nativeQuery = true)
    List<SubTask> getSubTasksOfTask(long task_id);


    @Modifying
    @Transactional
    @Query(value = "UPDATE SUB_TASK SET SUBTASKS_ORDER = SUBTASKS_ORDER+1 WHERE SUBTASKS_ORDER = ?1 ", nativeQuery = true)
    void movesubTaskUp1(int order);
    @Modifying
    @Transactional
    @Query(value = "UPDATE SUB_TASK SET SUBTASKS_ORDER = SUBTASKS_ORDER-1 WHERE ID = ?1 ", nativeQuery = true)
    void movesubTaskUp2(long id);
    @Modifying
    @Transactional
    @Query(value = "UPDATE SUB_TASK SET SUBTASKS_ORDER = SUBTASKS_ORDER-1 WHERE SUBTASKS_ORDER = ?1", nativeQuery = true)
    void movesubTaskDown1(int order);
    @Modifying
    @Transactional
    @Query(value = "UPDATE SUB_TASK SET SUBTASKS_ORDER = SUBTASKS_ORDER+1 WHERE ID = ?1", nativeQuery = true)
    void movesubTaskDown2(long id);
}