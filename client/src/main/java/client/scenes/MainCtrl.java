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
import commons.SubTask;
import commons.Tag;
import commons.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class MainCtrl {
    private ServerUtils server;

    @Getter
    @Setter
    private boolean adminPresence = false;

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

    private String styleSheet = getClass().getResource("styles.css").toExternalForm();

    @Inject
    public MainCtrl(ServerUtils server) {
        this.server = server;
    }

    public void initialize(Stage primaryStage, Pair<LandingPageCtrl, Parent> landing,
                          Pair<BoardOverviewCtrl, Parent> boardOverview,
                            Pair<UserMenuCtrl, Parent> userMenu,
                            Pair<AdminOverviewCtrl, Parent> adminOverview,
                           Pair<AdminLoginCtrl, Parent> adminLogin,
                           Pair<PasswordChangeCtrl, Parent> passwordChange,
                           Pair<TaskOverviewCtrl, Parent> taskOverview,
                           Pair<TagOverviewCtrl,Parent> tagOverview) {
        this.primaryStage = primaryStage;

        this.landingCtrl = landing.getKey();
        this.landing = new Scene(landing.getValue());
        this.landing.getStylesheets().add(styleSheet);

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());
        this.boardOverview.getStylesheets().add(styleSheet);

        this.userMenuCtrl = userMenu.getKey();
        this.userMenu = new Scene(userMenu.getValue());
        this.userMenu.getStylesheets().add(styleSheet);

        this.taskOverviewCtrl = taskOverview.getKey();
        this.taskOverview = new Scene(taskOverview.getValue());
        this.taskOverview.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());


        this.tagOverviewCtrl = tagOverview.getKey();
        this.tagOverview = new Scene(tagOverview.getValue());
        this.tagOverview.getStylesheets().add(styleSheet);

        this.adminOverviewCtrl = adminOverview.getKey();
        this.adminOverview = new Scene(adminOverview.getValue());
        this.adminOverview.getStylesheets().add(styleSheet);

        this.adminLoginCtrl = adminLogin.getKey();
        this.adminLogin = new Scene(adminLogin.getValue());
        this.adminLogin.getStylesheets().add(styleSheet);

        this.passwordChangeCtrl = passwordChange.getKey();
        this.passwordChange = new Scene(passwordChange.getValue());
        this.passwordChange.getStylesheets().add(styleSheet);


        showLanding();
        primaryStage.show();

    }


    /**
     * Shows the landing scene
     */
    public void showLanding() {
        primaryStage.setTitle("Welcome to Talio!");
        primaryStage.setScene(landing);
    }

    /**
     * Shows the board overview scene
     * @param board board to show
     */
    public void showBoard(Board board) {
        currBoard = board;
        primaryStage.setTitle("Board: Your Board");
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(boardOverview);
        boardOverviewCtrl.load(board);
        boardOverviewCtrl.connect(); // connects to /topic/boards
    }

    /**
     * show the user menu
     */
    public void showUserMenu() {
        userMenuCtrl.loadVisitedBoards();
        primaryStage.setScene(userMenu);
    }

    /**
     * Shows task overview scene
     * @param taskId key of the task to display the advanced features of.
     */
    public void showTaskOverview(Long taskId) {
        Task task = server.getTask(taskId);
        if (task == null) return;
        currTask = task;
        Stage taskStage = new Stage();
        taskStage.setTitle("Task: " + task.getTitle());
        taskStage.setResizable(false);
        taskOverviewCtrl.load();
        taskStage.setScene(taskOverview);
        taskOverviewCtrl.connect();
        taskStage.initModality(Modality.APPLICATION_MODAL);
        taskStage.showAndWait();
        taskOverviewCtrl.unsubscribe();
    }

    /**
     * Shows tag overview scene
     * @param boardKey key of the board to display the tags of.
     */
    public void showTagOverview(String boardKey) {
        Board board = server.findBoard(boardKey);
        Stage stage = new Stage();
        stage.setTitle("Tag List");
        stage.setResizable(false);
        tagOverviewCtrl.load(board);
        stage.setScene(tagOverview);
        stage.initModality(Modality.APPLICATION_MODAL);
        tagOverviewCtrl.connect();
        stage.showAndWait();
    }

    /**
     * Shows admin login scene
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
     * Shows admin overview scene
     */
    public void showAdminOverview() {
        setAdminPresence(true);
        primaryStage.setScene(adminOverview);
        adminOverviewCtrl.refresh();
    }

    /**
     * writes user's favorite boards and their hashed passwords to file on their computer
     * @param boardKeys board keys to write
     * @param server server on which the boards exist
     * @throws IOException exception for input
     */
    public void writeToCsv(List<String> boardKeys, String server) throws UnsupportedEncodingException {
        File dir = new File(System.getProperty("user.dir") + "/client/src/main/resources/servers/");
        String encodedUrl = URLEncoder.encode(server, StandardCharsets.UTF_8);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, encodedUrl + ".csv");
        if (file.exists()) { file.delete(); }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write(boardKeys.toString());
        } catch (Exception e) { }
    }

    /**
     * reads user's saved data(if they exist) from the local file of the given server
     * @param server current server
     * @return list of names of baords
     * @throws IOException shouldn't happen
     */
    public List<String> readFromCsv(String server) {
        String encodedUrl;
        encodedUrl = URLEncoder.encode(server, StandardCharsets.UTF_8);

        List<String> boardKeys = new ArrayList<>();
        File dir = new File(System.getProperty("user.dir") +
                "/client/src/main/resources/servers/" + encodedUrl + ".csv");
        if (!dir.exists()) {

            return boardKeys;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(dir))) {
            String line = reader.readLine();
            line = line.substring(1, line.length() - 1);
            String[] boards = line.split(", ");
            for (String key : boards)
                if (!key.isEmpty())
                    boardKeys.add(key);
        } catch (Exception e) {
        }
        return boardKeys;
    }

    /**
     * Used to change password
     */
    public void showChangePassword() {
        Stage create = new Stage();
        create.setScene(passwordChange);
        create.initModality(Modality.APPLICATION_MODAL);
        create.showAndWait();
    }


    public void deleteTask(Long taskId) {
        server.deleteTask(taskId);
        if (currTask.getId() == taskId) {
            currTask = null;
        }
    }

    /**
     * Renames a task
     * @param newName - the new task name
     */
    public void renameTask(String newName) {
        server.renameTask(this.currBoard.getKey(), this.currTask.getId(), newName);
    }

    /**
     * Changes the description of a task
     * @param newDesc - the new description
     */
    public void changeTaskDesc(String newDesc) {
        server.changeTaskDesc(this.currBoard.getKey(), this.currTask.getId(), newDesc);
    }

    public Alert createWarning(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING, content);
        alert.getDialogPane().getStylesheets().add(styleSheet);
        alert.getDialogPane().setPrefSize(400, 200);
        return alert;
    }

    public Alert createWarning(String title, String header, String content) {
        Alert alert = createWarning(content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }

    public Alert createError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a password.");
        alert.getDialogPane().getStylesheets().add(styleSheet);
        alert.getDialogPane().setPrefSize(400, 200);
        return alert;
    }

    public void createSubTask(String name) {
        server.createSubTask(currTask.getId(), name);
    }

    public void deleteSubTask(Long id) {
        server.deleteSubTask(currBoard.getKey(), id);
    }

    public void checkSubTask(SubTask task) {
        server.checkSubTask(currBoard.getKey(), task.getId());
    }

    public void removeTag(Tag tag) {
        this.server.removeTag(currTask.getId(), tag.getId());
    }

    public void addTag(Tag tag) {
        this.server.addTag(currTask.getId(), tag.getId());
    }
}