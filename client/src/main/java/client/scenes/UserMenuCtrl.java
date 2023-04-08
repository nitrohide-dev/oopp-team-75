package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CreateBoardModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class UserMenuCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML public VBox boardsListView;
    @FXML public TextField textBox;

    @FXML private ImageView logo;

    @FXML private ImageView exit;

    private List<String> visitedBoards; // list of keys of visited boards
    private Image deleteImage;

    @Inject
    public UserMenuCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.visitedBoards = new ArrayList<>();
        this.deleteImage = new Image(Path.of("", "client", "images", "cancel.png").toString());
    }

    public void initialize() {
        logo.setImage(new Image(Path.of("", "client", "images", "Logo.png").toString()));
        exit.setImage(new Image(Path.of("", "client", "images", "ExitButton.png").toString()));
    }

    /**
     * Loads visited boards, should be done every time user menu is shown.
     */
    public void loadVisitedBoards() {
        // clear user menu
        textBox.clear();
        visitedBoards.clear();
        boardsListView.getChildren().clear();

        // start adding visited boards
        for (String key : mainCtrl.readFromCsv())
            if (server.findBoard(key) != null)
                addBoard(key);
    }

    /**
     * Saves visited boards, should be done before every exit.
     */
    public void saveVisitedBoards() {
        // remove duplicate keys
        visitedBoards = new ArrayList<>(new HashSet<>(visitedBoards));

        // store keys in a csv file
        mainCtrl.writeToCsv(visitedBoards);
    }

    /**
     * admin panel button is pressed
     */
    public void login(){
        saveVisitedBoards();
        mainCtrl.showAdminLogin();
    }


    /**
     * create board button is pressed
     */
    public void createBoard() {
        String key = textBox.getText().trim();
        if (key.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a board key.").showAndWait();
            return;
        }
        if (server.findBoard(key) != null){
            new Alert(Alert.AlertType.ERROR, "Board already exists.").showAndWait();
            return;
        }

        CreateBoardModel model = new CreateBoardModel(key, key);
        server.createBoard(model);
        Board b = new Board(model);
        visitedBoards.add(key);
        saveVisitedBoards();
        mainCtrl.showBoard(b);
    }


    /**
     * Adds a board to the list view for the user
     * @param key name/key of the board
     */
    public void addBoard(String key) {

        visitedBoards.add(key);

        HBox itemBox = new HBox();
        itemBox.setAlignment(Pos.CENTER_LEFT);
        itemBox.setPadding(new Insets(6));
        itemBox.setSpacing(10);
        itemBox.setBackground(Background.fill(Paint.valueOf("white")));
        itemBox.setPrefHeight(30);
        itemBox.setCursor(Cursor.HAND);

        Label itemLabel = new Label(key);
        itemLabel.setPrefWidth(120);

        Region region = new Region();
        region.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(region, Priority.ALWAYS);

        ImageView removeButton = new ImageView(this.deleteImage);
        removeButton.setFitHeight(20);
        removeButton.setFitWidth(20);
        removeButton.setPickOnBounds(true); // makes clicking button easier
        removeButton.setSmooth(true);
        removeButton.getStyleClass().add("header-btn"); //
        removeButton.setOnMouseClicked(event -> {
            removeBoard(key);
        });
        itemBox.getChildren().addAll(itemLabel, region, removeButton);
        itemBox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2)
                openBoard(itemBox);
        });

        boardsListView.getChildren().add(itemBox);
    }


    /**
     * User removes a board from the visited boards list.
     * @param key the key of the board to be removed.
     */
    public void removeBoard(String key) {
        // first remove it from user's internal list
        visitedBoards.remove(key);

        // then remove it from the list of viewed boards
        for(Node node : boardsListView.getChildren()) {
            HBox box = (HBox) node;
            String text = ((Label) box.getChildren().get(0)).getText();
            if(text.equals(key)) {
                boardsListView.getChildren().remove(box);
                break;
            }
        }
    }

    /**
     * User double-clicks on a board in the visited boards list
     * @param itemBox HBox selected
     */
    public void openBoard(HBox itemBox) {
        String key = ((Label) itemBox.getChildren().get(0)).getText();
        Board board = server.findBoard(key);
        if (board == null) {
            new Alert(Alert.AlertType.ERROR, "Board does not exist.").showAndWait();
            removeBoard(key);
            return;
        }
        visitedBoards.add(key);
        saveVisitedBoards();
        mainCtrl.showBoard(board);
    }

    /**
     * Join button is pressed
     */
    public void joinBoard() {
        String key = textBox.getText().trim();
        if (key.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a board key.").showAndWait();
            return;
        }
        Board board = server.findBoard(key);
        if (board == null) {
            new Alert(Alert.AlertType.ERROR, "Board does not exist.").showAndWait();
            return;
        }
        mainCtrl.showBoard(board);
        visitedBoards.add(key);
        saveVisitedBoards();
    }

    /**
     * Exit button is pressed
     */
    public void exit() {
        mainCtrl.showLanding();
        saveVisitedBoards();
    }

}

