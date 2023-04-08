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

import commons.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface TagRepository extends JpaRepository<Tag, Long> {
    /**
     * Finds the tags of a specific task in the database
     @param task_id the id of the task to look up tags of
     @return the taglist corresponding to a given task
     */
    @Query(value = "SELECT t1.ID,t1.TITLE,t1.BOARD_KEY FROM TAG t1 JOIN TAG_TASKS t2 ON t1.ID = t2.TAG_ID  " +
            "WHERE t2.TASK_ID = ?1 ", nativeQuery = true)
    Collection<Tag> getTagsByTask(long task_id);
    /**
     * Finds the tags of a specific board in the database
     @param board_key the id of the board to look up tags of
     @return the taglist corresponding to a given board
     */
    @Query(value = "SELECT * FROM TAG t1 WHERE t1.BOARD_KEY = ?1 ", nativeQuery = true)
    Collection<Tag> getTagsByBoard(long board_key);

    Collection<Tag> getTagsByBoard(String board_key);
}