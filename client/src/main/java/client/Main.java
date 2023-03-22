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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import client.scenes.BoardOverviewCtrl;
import client.scenes.LandingPageCtrl;
import client.scenes.MainCtrl;
import com.google.inject.Injector;

import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

//        var overview = FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "QuoteOverview.fxml");
//        var add = FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml");
        var landing = FXML.load(LandingPageCtrl.class, "client", "scenes", "LandingPage.fxml");
        var board = FXML.load(BoardOverviewCtrl.class, "client", "scenes", "BoardOverview.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, landing, board);

        var boc = board.getKey();
        Board board1 = new Board("a", "a", "a");
        TaskList tl1 = board1.createTaskList();
        TaskList tl2 = board1.createTaskList();
        tl2.setTitle("Orange");
        Task t1 = tl2.createTask();
        t1.setTitle("Cheese");
        Task t2 = tl2.createTask();
        t2.setTitle("Apple");
        List<TaskList> listOfLists = new ArrayList<>();
        listOfLists.add(tl1);
        listOfLists.add(tl2);
        boc.update(board1);
    }
}