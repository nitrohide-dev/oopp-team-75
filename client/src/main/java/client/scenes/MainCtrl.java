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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        List<String>boardNames=this.readFromCsv();
        for(String board:boardNames){
            userMenuCtrl.addBoardToListView(board);
        }

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

    public void createBoard(String name,String title, int password){
        userMenuCtrl.addBoardToList(name,password);
        server.createBoard(new CreateBoardModel(name,title,password));
        showUserMenu();
    }

    /**
     * writes user's favorite boards and their hashed passwords to file on their computer
     * @throws IOException exception for input
     */
    public void writeToCsv() throws IOException {
        File dir = new File(System.getProperty("user.home") + "/Documents/Talio");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "data.csv");
        if(file.exists()){file.delete();}
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(userMenuCtrl.getBoards().toString());
        }
    }


    /**
     * reads user's saved data(if they exist) from the local file
     * @return list of names of baords
     * @throws IOException shouldn't happen
     */
    public List<String> readFromCsv() throws IOException {
        List<String> boardNames=new ArrayList<>();
        if(isFirstTime()) return boardNames;
        HashMap<String, Integer> data = new HashMap<>();
        File dir = new File(System.getProperty("user.home") + "/Documents/Talio/data.csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(dir))) {
            String line= reader.readLine();
            line= line.substring(1,line.length()-1);
            String[] boards = line.split(",* ");
            for(String string : boards) {
                String[] tokens = string.split("=");
                if (tokens.length == 2) {

                    String key = tokens[0].trim();

                    boardNames.add(key);
                    int value = Integer.parseInt(tokens[1].trim());
                    data.put(key, value);
                }
            }
        }
        userMenuCtrl.setBoards(data);
        return boardNames;
    }

    /**
     * @return if the user ever used the application before
     */
    public boolean isFirstTime() {
        File file = new File(System.getProperty("user.home") + "/Documents/Talio/first_time");
        if (!file.exists()) {
            file.mkdirs();
            return true;
        }
        return false;
    }



}