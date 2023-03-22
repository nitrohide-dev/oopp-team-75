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
package client.utils;

import commons.Board;
import commons.Task;
import commons.TaskList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class BoardUtils {

    private static final String SERVER = "http://localhost:8080/";

//    BOARDS



//    TASK LISTS

    public TaskList getTaskList(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/tasks/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    public TaskList saveTaskList(TaskList taskList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/tasks") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(taskList, APPLICATION_JSON), TaskList.class);
    }

    public TaskList deleteTaskList(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/tasks/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(new GenericType<>() {
                });
    }

//    TASKS

    public Task getTask(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/tasks/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    public Task saveTask(Task task) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/tasks") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    public Task deleteTask(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/tasks/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(new GenericType<>() {
                });
    }
}