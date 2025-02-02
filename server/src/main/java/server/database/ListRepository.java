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

import commons.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface ListRepository extends JpaRepository<TaskList, Long> {

    /**
     * gets the board key of a list with the given id
     * @param listID - the id of the taskList that we want the boardkey of
     * @return  the board key of the board of the list
     */
    @Query(value = "SELECT BOARD_KEY FROM TASK_LIST WHERE ID = ?1",nativeQuery = true)
    String getBoardByListID(long listID);


}