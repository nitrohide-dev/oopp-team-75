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
package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CreateBoardModel;
import commons.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.SocketException;

public class MainCtrl {
    private ServerUtils server;

    private Stage primaryStage;
    private LandingPageCtrl landingCtrl;
    private Scene landing;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private Board currBoard;
    private Scene userMenu;
    private UserMenuCtrl userMenuCtrl;
    private Scene boardCreate;
    private BoardCreateCtrl boardCreateCtrl;

    @Inject
    public MainCtrl(ServerUtils server){
        this.server = server;
    }

    public void initialize(Stage primaryStage, Pair<LandingPageCtrl, Parent> landing,
//                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<BoardOverviewCtrl, Parent> boardOverview,
                            Pair<UserMenuCtrl, Parent> userMenu,
                           Pair<BoardCreateCtrl, Parent> boardCreate) throws IOException {
        this.primaryStage = primaryStage;
        this.landingCtrl = landing.getKey();
        this.landing = new Scene(landing.getValue());
//        this.overviewCtrl = overview.getKey();
//        this.overview = new Scene(overview.getValue());
//
//        this.addCtrl = add.getKey();
//        this.add = new Scene(add.getValue());

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());


        this.userMenuCtrl = userMenu.getKey();
        this.userMenu = new Scene(userMenu.getValue());

        this.boardCreateCtrl = boardCreate.getKey();
        this.boardCreate = new Scene(boardCreate.getValue());
        showLanding();


        primaryStage.show();




    }

    public Board getCurrBoard() {
        return currBoard;
    }

    public void setCurrBoard(Board board) {
        currBoard = board;
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showLanding(){
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        landingCtrl.changeImageUrl();
        primaryStage.setTitle("Welcome to Talio!");
        primaryStage.setScene(landing);
        landing.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm());
        landingCtrl.refresh();
    }

    public void showBoard(Board board) {
        currBoard = board;
        primaryStage.setTitle("Board: Your Board");
        primaryStage.setScene(boardOverview);
        boardOverviewCtrl.load(board);
        boardOverviewCtrl.connect();
        // connects to /topic/boards
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showUserMenu(){
        primaryStage.setScene(userMenu);
    }


    public void showBoardCreate(){
        Stage create = new Stage();
        create.setScene(boardCreate);
        create.initModality(Modality.APPLICATION_MODAL);
        create.showAndWait();

    }

    public void createBoard(String name, int password){
        userMenuCtrl.addBoardToList(name,password);
        server.createBoard(new CreateBoardModel(name,name,password));
        showUserMenu();
    }

    public void closingApp() throws IOException {
        userMenuCtrl.getUser().writeToCsv();
    }



}