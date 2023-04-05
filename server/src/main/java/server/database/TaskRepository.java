/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.database;


import commons.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;


public interface TaskRepository extends JpaRepository<Task, Long> {


    @Query(value = "SELECT TASKS_ORDER FROM TASK WHERE ID = ?1",nativeQuery = true)
    int getOrderById(long task_id);

    /**
     * query for updating the task order of the initial list
     * so that the order stays correct after the moving of the task
     * @param task_id the id of the task that is being moved
     * @param task_order the value of TASKS_ORDER of the task before the move
     *
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE TASK SET TASKS_ORDER = TASKS_ORDER-1 WHERE TASK_LIST_" +
            "ID IN(SELECT TASK_LIST_ID FROM TASK WHERE ID = ?2) AND TASKS_ORDER > ?1 ",nativeQuery = true)
    void updateInitialListOrder(int task_order,long task_id);
    /**
     * query for updating the task order of the target list
     * so that the order stays correct after the moving of the task
     * @param task_order the target value of TASKS_ORDER of the task after the move
     * @param list_id the list into which the task will be moved
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE TASK SET " +
            "TASKS_ORDER = TASKS_ORDER+1 WHERE TASK_LIST_ID = ?2 AND TASKS_ORDER >= ?1 ",nativeQuery = true)
    void updateTargetListOrder(int task_order,long list_id);
    /**
     * query for putting the drag and dropped task into the correct list
     * @param task_id the id of the task to be moved in the database
     * @param list_id the id of the list that the task should be moved to
     *
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE TASK SET TASK_LIST_ID = ?2, TASKS_ORDER = ?3 WHERE ID = ?1",nativeQuery = true)
    void moveTask(long task_id,long list_id,int order);

}