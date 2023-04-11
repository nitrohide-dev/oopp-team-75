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
import commons.SubTask;
import commons.Tag;
import commons.models.CreateBoardModel;
import commons.Task;
import commons.models.TaskMoveModel;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import lombok.Getter;
import lombok.Setter;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
public class ServerUtils {

    @Getter
    @Setter
    private String domain;

    @Getter
    @Setter
    private String SERVER;

    @Getter
    @Setter
    private StompSession session;

    //REST API

    /**
     * Connects to the server and subscribes to the given topic.
     *
     * @param key the key of the board to find
     */
    public Board findBoard(String key) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/find/" + key)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Board.class);
    }

    /**
     * @param key the key of the board to delete
     */
    public void deleteBoard(String key) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/delete/" + key)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Board.class);
    }

    /**
     * Gets all boards from the database
     *
     * @return a list of all boards in the database
     */
    public List<Board> getAllBoards() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Board>>() {});
    }

    /**
     * @param model the model used to create the board
     */
    public void createBoard(CreateBoardModel model) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/create")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(model, APPLICATION_JSON), Board.class);
    }

    /**
     * Adding a catch to catch exceptions when connecting to the server
     *
     * @param url the url to connect to
     * @return the stomp session
     */
    public StompSession safeConnect(String url) {
        try {
            return connect("ws://" + url + "/websocket");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * connects to websocket
     *
     * @param url the url to connect to
     * @return the stomp session
     */
    private StompSession connect(String url) {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter(){}).get();
        } catch (ExecutionException e) {
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Subscribes to a topic to get updates
     * @param dest the destination to subscribe to
     * @param type the type of the payload
     * @param consumer the consumer to handle the payload
     * @param <T> the type of the payload
     */
    public <T> void subscribe(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    /**
     * initial authentication on the side of the server
     *
     * @param password password hashed
     * @return whether it was successful or not
     */
    public boolean authenticate(String password) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/login")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("password", password)
                .get(Boolean.class);
    }


    /**
     * Changes the password of the admin
     *
     * @param passwordHashed the new password hashed
     */
    public void changePassword(String passwordHashed) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/changePassword")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("passwordHashed", passwordHashed)
                .get(Boolean.class);
    }

    /**
     * Logs out the admin
     */
    public void logout() {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/logout")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
    }

    /**
     * Gets a tag from the database by ID
     * @param id - the id of the tag
     * @return the tag with the given id
     */
    public Tag getTag(String id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/tag/getById/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Tag.class);
    }

    /**
     * Gets all tags by task from the database
     * @param taskKey the key of the task to get the tags from
     * @return a set of tags
     */
    public Set getTagsByTask(String taskKey) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/tag/getByTask/" + taskKey)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Set.class);
    }

    /**
     * Gets all tags by board from the database
     * @param boardKey the key of the board to get the tags from
     * @return a list of tags
     */
    public List getTagsByBoard(String boardKey) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/tag/getByBoard/" + boardKey)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(List.class);
    }

    //STOMP API (Websockets)
    public void send(String dest, Object o) {
        session.send(dest, o);
    }
    /**
     * Sends a request to the server to update the board
     *
     * @param board - the board to be updated
     */
    public void updateBoard(Board board) {
        send("/app/boards", board);
    }

    /**
     * Sends a request to the server to move a task from one board to another
     * @param model - the model used for this operation
     */
    public void moveTask(TaskMoveModel model, String boardKey) {
        send("/app/task/move/" + boardKey, model);
    }
    /**
     * Sends a request to the server to get a task from the database
     * @param taskId - the id of the task
     */
    public Task getTask(Long taskId) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/tasks/find/" + taskId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(Task.class);
    }
    /**
     * Sends a request to the server to delete a task from the database
     * @param taskId - the id of the task
     */
    public void deleteTask(Long taskId) {
        send("/app/task/delete" , taskId);
    }

    /**
     * Sends a request to the server to get a list from the database
     *
     * @param listId - the id of the list
     */
    public void getList(Long listId) {
        send("/app/list/get", listId);
    }

    /**
     * Sends a request to the server to delete a list from the database
     *
     * @param listId - the id of the list
     */
    public void deleteList(Long listId) {
        send("/app/list/delete", listId);
    }

    /**
     * Sends a request to the server to create a list in the database
     *
     * @param boardKey - the Board that is to contain the list
     */
    public void createList(String boardKey) {
        send("/app/list/createList", boardKey);
    }

    /**
     * Sends a request to the server to rename a list in the database
     *
     * @param listId    - the id of the list
     * @param listTitle - the new list name
     */
    public void renameList(Long listId, String listTitle) {
        send("/app/list/rename/" + listTitle, listId);
    }

    /**
     * Sends a request to create a task in the database
     *
     * @param listID    - the ID of the list that is supposed to contain the task
     * @param taskTitle - the title of the created task
     */
    public void createTask(Long listID, String taskTitle) {
        send("/app/list/createTask/" + taskTitle, listID);
    }

    /**
     * Sends a request to the server to rename a task in the database
     *
     * @param taskId    - the id of the task
     * @param taskTitle - the new task name
     */
    public void renameTask(String boardKey, Long taskId, String taskTitle) {
        send("/app/task/rename/" + boardKey + "/" + taskTitle, taskId);
    }

    /**
     * Sends a request to the server to change the description of a task
     * @param boardKey - the key of the board of the task
     * @param taskId - the id of the task
     * @param newDesc - the new description
     */
    public void changeTaskDesc(String boardKey, long taskId, String newDesc) {
        send("/app/task/desc/" + boardKey + "/" + taskId, newDesc);
    }

    /**
     * Sends a request to the server to delete a tag from the database
     * @param tagKey - the key of the tag to be deleted
     * @param boardKey - the key of the board to be deleted from
     */
    public void deleteTag(String tagKey, String boardKey) {
        send("/app/tag/delete/" + tagKey, boardKey);
    }

    /**
     * Sends a request to the server to edit a tag in the database
     * @param tagKey - the key of the tag to be edited
     * @param boardKey - the key of the board to be edited from
     * @param newTitle - the new title of the tag
     */
    public void renameTag(String tagKey, String boardKey, String newTitle) {
        send("/app/tag/edit/" + boardKey + "/" + tagKey, newTitle);
    }

    /**
     * Sends a request to the server to create a tag in the database
     * @param boardKey - the key of the board to be created from
     * @param tagTitle - the title of the tag to be created
     */
    public void createTag(String boardKey, String tagTitle) {
        send("/app/tag/create/" + boardKey, tagTitle);
    }

    /**
     * Sends a request to the server to add a tag to a task in the database
     * @param taskKey - the key of the task to be added to
     * @param tag - the tag to be added
     */
    public void addTag(Long taskKey, Long tagId) {
        send("/app/task/addTag/" + taskKey, tagId);
    }

    /**
     * Sends a request to the server to add a tag to a task in the database
     * @param taskKey - the key of the task to be added to
     * @param tag - the tag to be added
     */
    public void removeTag(Long taskKey, Long id) {
        send("/app/task/removeTag/" + taskKey, id);
    }

    public SubTask getSubTask(String id){
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/subtask/getById/" + id)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(SubTask.class);
    }

    public Set getSubTasksByTask(String taskKey) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/subtask/getByTask/" + taskKey)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(Set.class);
    }

    public void createSubTask(Long taskKey, String subTaskTitle) {
        send("/app/subtask/create/" + subTaskTitle, taskKey);
    }

    public void deleteSubTask(String boardKey, Long subTaskId) {
        send("/app/subtask/delete/" + boardKey, subTaskId);
    }


    public void checkSubTask(String boardKey, Long id) {
        send("/app/subtask/check/" + boardKey, id);
    }

    public void changeTaskTag(String boardKey, Long id, Collection<Long> values) {
        send("app/task/tags/" + boardKey + "/" + id, values);
        System.out.println("esrver");
    }
}