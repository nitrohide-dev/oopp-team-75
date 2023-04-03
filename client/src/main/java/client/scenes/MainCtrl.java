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
//import javafx.geometry.Rectangle2D;
import commons.CreateBoardModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Objects;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainCtrl {
    private ServerUtils server;

    private Stage primaryStage;
    private LandingPageCtrl landingCtrl;
    private Scene landing;
    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private Board currBoard;
    private Scene userMenu;
    private UserMenuCtrl userMenuCtrl;
    private Scene boardCreate;
    private BoardCreateCtrl boardCreateCtrl;

    private AdminOverviewCtrl adminOverviewCtrl;
    private Scene adminOverview;
    private AdminLoginCtrl adminLoginCtrl;
    private Scene adminLogin;
    private PasswordChangeCtrl passwordChangeCtrl;
    private Scene passwordChange;

    @Inject
    public MainCtrl(ServerUtils server){
        this.server = server;
    }

    public void initialize(Stage primaryStage, Pair<LandingPageCtrl, Parent> landing,
                          Pair<BoardOverviewCtrl, Parent> boardOverview,
                            Pair<UserMenuCtrl, Parent> userMenu,
                           Pair<BoardCreateCtrl, Parent> boardCreate,
                            Pair<AdminOverviewCtrl, Parent> adminOverview,
                           Pair<AdminLoginCtrl, Parent> adminLogin,
                           Pair<PasswordChangeCtrl, Parent> passwordChange) throws IOException {
        this.primaryStage = primaryStage;

        this.landingCtrl = landing.getKey();
        this.landing = new Scene(landing.getValue());

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());


        this.userMenuCtrl = userMenu.getKey();
        this.userMenu = new Scene(userMenu.getValue());



        this.boardCreateCtrl = boardCreate.getKey();
        this.boardCreate = new Scene(boardCreate.getValue());

        this.adminOverviewCtrl = adminOverview.getKey();
        this.adminOverview = new Scene(adminOverview.getValue());

        this.adminLoginCtrl = adminLogin.getKey();
        this.adminLogin = new Scene(adminLogin.getValue());

        this.passwordChangeCtrl = passwordChange.getKey();
        this.passwordChange = new Scene(passwordChange.getValue());

        showLanding();
        //primaryStage.setScene(this.adminOverview);

        primaryStage.show();

        List<String> boardNames=this.readFromCsv();
        for(String board : boardNames){
            userMenuCtrl.addBoard(board);
        }



    }

    public Board getCurrBoard() {
        return currBoard;
    }

    public void setCurrBoard(Board board) {
        currBoard = board;
    }

    public void showLanding(){
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        landingCtrl.changeImageUrl();
        primaryStage.setTitle("Welcome to Talio!");
        primaryStage.setScene(landing);
        landing.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/styles.css")).toExternalForm());
    }

    public void showBoard(Board board) {
        currBoard = board;
        primaryStage.setTitle("Board: Your Board");
        //primaryStage.setMaximized(true);
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(600);
        //this fixes a bug where the maximized window will be opened in pref size.
        //but it causes a bug where the window is not properly set, so the buttons on the right side are not visible
        //TODO fix this bug
        Screen screen = Screen.getPrimary();
//        Rectangle2D bounds = screen.getVisualBounds();
//        primaryStage.setWidth(bounds.getWidth());
//        primaryStage.setHeight(bounds.getHeight());
        boardOverview.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("css/BoardOverview.css")).toExternalForm());
        boardOverviewCtrl.changeImageUrl();
        primaryStage.setScene(boardOverview);
        boardOverviewCtrl.load(board);
        boardOverviewCtrl.connect();
        // connects to /topic/boards
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

    public void createBoard(String name,String title){
        server.createBoard(new CreateBoardModel(name,title));
        Board b = new Board(new CreateBoardModel(name,title));
        userMenuCtrl.addBoard(name);
        showUserMenu();
    }

    /**
     * writes user's favorite boards and their hashed passwords to file on their computer
     * @throws IOException exception for input
     */
    public void writeToCsv() throws IOException {
        File dir = new File(System.getProperty("user.dir") + "/client/src/main/resources/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "data.csv");
        if(file.exists()){file.delete();}
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(userMenuCtrl.getBoards().toString());
        }
    }

    public UserMenuCtrl getUserMenuCtrl(){
        return userMenuCtrl;
    }
    /**
     * reads user's saved data(if they exist) from the local file
     * @return list of names of baords
     * @throws IOException shouldn't happen
     */
    public List<String> readFromCsv() throws IOException {
        List<String> boardNames=new ArrayList<>();
        File dir = new File(System.getProperty("user.dir") + "/client/src/main/resources/data.csv");
        if(!dir.exists()) {
            return boardNames;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(dir))) {
            String line= reader.readLine();
            line= line.substring(1,line.length()-1);
            String[] boards = line.split(",* ");
            for(String string : boards) {

                String key = string.trim();
                if(!key.equals("") && server.findBoard(key)!=null){
                    boardNames.add(key);}
            }
        }
        return boardNames;
    }

    public void adminLogin() {
        Stage create = new Stage();
        create.setScene(adminLogin);
        create.initModality(Modality.APPLICATION_MODAL);
        create.showAndWait();
    }
    public void adminOverview(){
        primaryStage.close();
        primaryStage = new Stage();
        setAdminPresence(true);
        primaryStage.setScene(adminOverview);
        adminOverviewCtrl.init();
        primaryStage.show();
    }

    /**
     * Used only by admin
     * @param board Board to view
     */
    public void showBoardNewWindow(Board board) {
        Stage stage = new Stage();
        currBoard = board;
        stage.setTitle("Board: Your Board");
        stage.setMinWidth(750);
        stage.setMinHeight(600);
        stage.setScene(boardOverview);
        boardOverview.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("css/BoardOverview.css")).toExternalForm());
        boardOverviewCtrl.changeImageUrl();
        boardOverviewCtrl.load(board);
        boardOverviewCtrl.connect();
        stage.show();
    }

    public void changePassword(){
        Stage create = new Stage();
        create.setScene(passwordChange);
        create.initModality(Modality.APPLICATION_MODAL);
        create.showAndWait();

    }
    public void setAdminPresence(boolean adminPresence) {
        boardOverviewCtrl.setAdminPresence(adminPresence);
    }
}