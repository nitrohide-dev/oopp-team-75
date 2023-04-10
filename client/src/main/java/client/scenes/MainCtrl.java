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
import commons.models.CreateBoardModel;
import commons.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainCtrl {
    private ServerUtils server;

    @Getter
    @Setter
    private Board currBoard;
    @Getter
    @Setter
    private Task currTask;

    private Stage primaryStage;

    private LandingPageCtrl landingCtrl;
    private Scene landing;

    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    @Getter
    private UserMenuCtrl userMenuCtrl;
    private Scene userMenu;

    private Scene boardCreate;

    @Getter
    @Setter
    private boolean adminPresence = false;

    private AdminOverviewCtrl adminOverviewCtrl;
    private Scene adminOverview;

    private AdminLoginCtrl adminLoginCtrl;
    private Scene adminLogin;

    private PasswordChangeCtrl passwordChangeCtrl;
    private Scene passwordChange;

    private TaskOverviewCtrl taskOverviewCtrl;
    private Scene taskOverview;
    private TagOverviewCtrl tagOverviewCtrl;
    private Scene tagOverview;
    @Inject
    public MainCtrl(ServerUtils server){
        this.server = server;
    }

    public void initialize(Stage primaryStage, Pair<LandingPageCtrl, Parent> landing,
                          Pair<BoardOverviewCtrl, Parent> boardOverview,
                            Pair<UserMenuCtrl, Parent> userMenu,
                            Pair<AdminOverviewCtrl, Parent> adminOverview,
                           Pair<AdminLoginCtrl, Parent> adminLogin,
                           Pair<PasswordChangeCtrl, Parent> passwordChange,
                           Pair<TaskOverviewCtrl, Parent> taskOverview,Pair<TagOverviewCtrl,Parent> tagOverview) throws IOException {
        this.primaryStage = primaryStage;

        this.landingCtrl = landing.getKey();
        this.landing = new Scene(landing.getValue());
        this.landing.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());
        this.boardOverview.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        this.userMenuCtrl = userMenu.getKey();
        this.userMenu = new Scene(userMenu.getValue());
        this.userMenu.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        this.taskOverviewCtrl = taskOverview.getKey();
        this.taskOverview = new Scene(taskOverview.getValue());

        this.tagOverviewCtrl = tagOverview.getKey();
        this.tagOverview = new Scene(tagOverview.getValue());

        this.adminOverviewCtrl = adminOverview.getKey();
        this.adminOverview = new Scene(adminOverview.getValue());
        this.adminOverview.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        this.adminLoginCtrl = adminLogin.getKey();
        this.adminLogin = new Scene(adminLogin.getValue());
        this.adminLogin.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        this.passwordChangeCtrl = passwordChange.getKey();
        this.passwordChange = new Scene(passwordChange.getValue());
        this.passwordChange.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        showLanding();
        primaryStage.show();

    }

    public Board getCurrBoard() {
        return currBoard;
    }

    public void setCurrBoard(Board board) {
        currBoard = board;
    }

    public void showLanding(){
//        primaryStage.setMinWidth(600);
//        primaryStage.setMinHeight(400);
//        landingCtrl.changeImageUrl();
        primaryStage.setTitle("Welcome to Talio!");
        primaryStage.setScene(landing);
    }

    /**
     * Starts the main scene with the board
     * @param board board to show
     */
    public void showBoard(Board board) {
        currBoard = board;
        primaryStage.setTitle("Board: Your Board");
        //primaryStage.setMaximized(true);
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(600);
        //this fixes a bug where the maximized window will be opened in pref size.
        //but it causes a bug where the window is not properly set, so the buttons on the right side are not visible
//        TODO fix this bug
//        Screen screen = Screen.getPrimary();
//        Rectangle2D bounds = screen.getVisualBounds();
//        primaryStage.setWidth(bounds.getWidth());
//        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setScene(boardOverview);
        boardOverviewCtrl.load(board);
        boardOverviewCtrl.connect(); // connects to /topic/boards

    }

    /**
     * shows user menu the first time
     * @throws IOException exception for input
     */
    public void showUserMenuFirstTime() throws IOException {
        List<String> boardNames=readFromCsv();
        for(String board : boardNames){
            userMenuCtrl.addBoard(board);
        }
        primaryStage.setScene(userMenu);
    }

    /**
     * show the user menu
     */
    public void showUserMenu() {
        userMenuCtrl.loadVisitedBoards();
        primaryStage.setScene(userMenu);
    }

    /**
     * shows the board creation page
     */
    public void showBoardCreate(){
        Stage create = new Stage();
        create.setScene(boardCreate);
        create.initModality(Modality.APPLICATION_MODAL);
        create.showAndWait();

    }

    /**
     * Creates a new board
     * @param name name of the board
     * @param title title of the board
     */
    public void createBoard(String name,String title) {
        server.createBoard(new CreateBoardModel(name, title));
        Board b = new Board(new CreateBoardModel(name, title));
        userMenuCtrl.addBoard(name);
        showUserMenu();
    }

    public void showTaskOverview(Long taskId) {
        Task task = server.getTask(taskId);
        if (task == null) return;
        currTask = task;
        Stage taskStage = new Stage();
        taskStage.setTitle("Task: " + task.getTitle());
        taskOverviewCtrl.load(task);
        taskStage.setScene(taskOverview);
        taskStage.initModality(Modality.APPLICATION_MODAL);
        taskStage.showAndWait();
    }
    public void showTagOverview(String boardKey){
        Board board = server.findBoard(boardKey);
        Stage stage = new Stage();
        stage.setTitle("Tag List");
        tagOverviewCtrl.load(board);
        stage.setScene(tagOverview);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    /**
     * writes user's favorite boards and their hashed passwords to file on their computer
     * @throws IOException exception for input
     */
    public void writeToCsv(List<String> boardKeys) {
        File dir = new File(System.getProperty("user.dir") + "/client/src/main/resources/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "data.csv");
        if(file.exists()){file.delete();}
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(boardKeys.toString());
        } catch (Exception e) {}
    }

    /**
     * reads user's saved data(if they exist) from the local file
     * @return list of names of baords
     * @throws IOException shouldn't happen
     */
    public List<String> readFromCsv() {
        List<String> boardKeys = new ArrayList<>();
        File dir = new File(System.getProperty("user.dir") + "/client/src/main/resources/data.csv");
        if(!dir.exists()) {
            return boardKeys;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(dir))) {
            String line = reader.readLine();
            line = line.substring(1,line.length()-1);
            String[] boards = line.split(", ");
            for(String key : boards)
                if (!key.isEmpty())
                    boardKeys.add(key);
        } catch (Exception e) {}
        return boardKeys;
    }

    /**
     * Used to log in as admin
     */
    public void showAdminLogin() {
        if (!adminPresence) {
            Stage create = new Stage();
            create.setScene(adminLogin);
            create.initModality(Modality.APPLICATION_MODAL);
            create.showAndWait();
        } else showAdminOverview();
    }

    /**
     * Starts the admin overview
     */
    public void showAdminOverview(){
        setAdminPresence(true);
        primaryStage.setScene(adminOverview);
        adminOverviewCtrl.init();
    }

    /**
     * Used to change password
     */
    public void showChangePassword(){
        Stage create = new Stage();
        create.setScene(passwordChange);
        create.initModality(Modality.APPLICATION_MODAL);
        create.showAndWait();
    }

    /**
     * Renames a task
     * @param newName - the new task name
     */
    public void renameTask(String newName) {
        server.renameTask(this.currBoard.getKey(), this.currTask.getId(), newName);
    }

    /**
     * check if admin is logged in
     * @param adminPresence true if admin is logged in
     */
    public void setAdminPresence(boolean adminPresence) {
        boardOverviewCtrl.setAdminPresence(adminPresence);
    }

    /**
     * Changes the description of a task
     * @param newDesc - the new description
     */
    public void changeTaskDesc(String newDesc) {
        server.changeTaskDesc(this.currBoard.getKey(), this.currTask.getId(), newDesc);
    }

}