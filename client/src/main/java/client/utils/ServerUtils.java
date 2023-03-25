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

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import commons.Board;
import jakarta.ws.rs.core.Response;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

@SuppressWarnings("ALL")
public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }
    //can we delete those old ones now?
    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

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

    /**
     * Sends request to the server to create a task.
     * The task will be added to the first taskList in the first board
     * @param name - the name of the task
     * @return true if the task can be created, false otherwise
     */
    public boolean addTask(String name) {
        Response res =  ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/task/create")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(name, APPLICATION_JSON));
        return res.getStatus() == 200;
    }

    /**
     * Sends request to the server to remove a task.
     * The task will be removed from its taskList
     * @param name - the name of the task
     * @return true if the task can be removed, false otherwise
     */
    public boolean deleteTask(String name, long boardId) {
        Response res =  ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/task/delete/" + boardId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(name, APPLICATION_JSON));
        return res.getStatus() == 200;
    }

    /**
     * Sends request to the server to remove a task.
     * The task will be removed from its taskList
     * @param name - the name of the task
     * @return true if the task can be removed, false otherwise
     */
    public boolean editTask(String name, String newName, long boardId) {
        Response res =  ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/task/delete/" + boardId)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(new Pair(name, newName), APPLICATION_JSON));
        return res.getStatus() == 200;
    }

    /**
     * Sends a request to the server to move a task from one list to another
     * @param board - the board where the tasks are
     * @param fromList - the name of the list that stores the task
     * @param toList - the name of the list to which we will move the task
     * @param task - the task that we want to move
     * @return true if the task can be put in toList, false otherwise
     */
    public boolean moveTask(Board board, String fromList, String toList, HBox task) {
        Response res =  ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/board/move")
            .queryParam("board", board)
            .queryParam("fromList", fromList)
            .queryParam("toList", toList)
            .queryParam("task", task)
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .post(Entity.entity(null, APPLICATION_JSON), Response.class);

        return res.getStatus() == 200;
    }


}