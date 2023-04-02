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

import commons.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@SuppressWarnings("ALL")
public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";
    /**
     * Sends request to the server that gets the board by id
     * @param id - the id
     * @return the board
     */
    public Board getBoard(long id) {
        return new Board();
//        return ClientBuilder.newClient(new ClientConfig())
//            .target(SERVER).path("api/board/get/" + id)
//            .request(APPLICATION_JSON)
//            .accept(APPLICATION_JSON)
//            .get(Board.class);
    }

//    /**
//     * Sends request to the server to create a task.
//     * The task will be added to the first taskList in the first board
//     * @param name - the name of the task
//     * @return true if the task can be created, false otherwise
//     */
//    public boolean addTask(String name) {
//        Response res =  ClientBuilder.newClient(new ClientConfig())
//            .target(SERVER).path("api/task/create")
//            .request(APPLICATION_JSON)
//            .accept(APPLICATION_JSON)
//            .post(Entity.entity(name, APPLICATION_JSON));
//        return res.getStatus() == 200;
//    }
//    /**
//     * Sends request to the server to remove a task.
//     * The task will be removed from its taskList
//     * @param name - the name of the task
//     * @return true if the task can be removed, false otherwise
//     */
//    public boolean editTask(String name, String newName, long boardId) {
//        Response res =  ClientBuilder.newClient(new ClientConfig())
//            .target(SERVER).path("api/task/delete/" + boardId)
//            .request(APPLICATION_JSON)
//            .accept(APPLICATION_JSON)
//            .post(Entity.entity(new Pair(name, newName), APPLICATION_JSON));
//        return res.getStatus() == 200;
//    }

//    /**
//     * Sends a request to the server to move a task from one list to another
//     * @param board - the board where the tasks are
//     * @param fromList - the name of the list that stores the task
//     * @param toList - the name of the list to which we will move the task
//     * @param task - the task that we want to move
//     * @return true if the task can be put in toList, false otherwise
//     */
//    public boolean moveTask(Board board, String fromList, String toList, HBox task) {
//        Response res =  ClientBuilder.newClient(new ClientConfig())
//            .target(SERVER).path("api/board/move")
//            .queryParam("board", board)
//            .queryParam("fromList", fromList)
//            .queryParam("toList", toList)
//            .queryParam("task", task)
//            .request(APPLICATION_JSON)
//            .accept(APPLICATION_JSON)
//            .post(Entity.entity(null, APPLICATION_JSON), Response.class);
//
//        return res.getStatus() == 200;
//    }


    // METHODS THAT ARE ACTUALLY USEFUL

    public Board findBoard(String key) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/find/" + key)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Board.class);
    }

    public Board deleteBoard(String key) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/delete/" + key)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Board.class);
    }

    public List<Board> getAllBoards() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Board>>() {});
    }

    public Board createBoard(CreateBoardModel model) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/create")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(model, APPLICATION_JSON), Board.class);
    }
    private StompSession session = connect("ws://localhost:8080/websocket");

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

    public <T> void subscribe(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest , new StompFrameHandler() {
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


    public void send(String dest, Object o) {
        session.send(dest, o);
    }
    /**
     * Sends a request to the server to update the board
     * @param board - the board to be update
     */
    public void updateBoard(Board board) { send("/app/boards", board);}
    /**
     * Sends a request to the server to move a task from one board to another
     * @param TaskMoveModel - the model used for this operation
     */
    public void moveTask(TaskMoveModel model) {
        send("/app/task/move", model);
    }
    /**
     * Sends a request to the server to get a task from the database
     * @param TaskId - the id of the task
     */
    public void getTask(String taskId) {
        send("/app/task/get", taskId);
    }
    /**
     * Sends a request to the server to delete a task from the database
     * @param TaskId - the id of the task
     */
    public void deleteTask(String taskId){ send("/app/task/delete",taskId);}
    /**
     * Sends a request to the server to get a list from the database
     * @param listId - the id of the list
     */
    public void getList(Long listId) { send("/app/list/get",listId);}
    /**
     * Sends a request to the server to delete a list from the database
     * @param listId - the id of the list
     */
    public void deleteList(String listId) { send("/app/list/delete",listId);}
    /**
     * Sends a request to the server to create a list in the database
     * @param board - the Board that is to contain the list
     */
    public void createList(Board board) {send("/app/list/createlist",board);}
    /**
     * Sends a request to the server to rename a list in the database
     * @param listId - the id of the list
     * @param listTitle - the new list name
     */
    public void renameList(String listId,String listTitle) { send("/app/list/renamelist/" + listId,listTitle);}
    /**
     * Sends a request to the create a task in the database
     * @param listID - the ID of the list that is supposed to contain the task
     * @param boardkey - the key of the board in which the task is added
     * @param taskTitle - the title of the created task
     */
    public void createTask(int listID,String boardkey,String taskTitle) {
        send("/app/list/createTask/"+ taskTitle + "/"+boardkey,listID);}
    /**
     * Sends a request to the server to rename a task in the database
     * @param taskId - the id of the task
     * @param taskTitle - the new task name
     */
    public void renameTask(String taskId,String taskTitle) { send("/app/task/rename/" + taskId,taskTitle);}

}