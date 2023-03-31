package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

import commons.Board;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class UserMenuCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public ListView<HBox> boardsListView;

    @FXML
    public TextField textBox;

    public void setBoardNames(List<String> boardNames) {
        this.boardNames = boardNames;
    }

    private List<String> boardNames; // list of current boards



    @Inject
    public UserMenuCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardNames=new ArrayList<>();
    }

    public void initialize() throws IOException {

    }

    /**
     * login method for admin
     */
    public void login(){
        mainCtrl.adminLogin();

    }


    /**
     * shows popup for board creation
     */
    public void createBoard() {
        mainCtrl.showBoardCreate();
    }


    /**
     * Adds a board to the list view for the user
     *
     * @param text name/key of the board
     */
    public void addBoard(String text) {
        Board board = server.findBoard(text);
        if(board==null){
            return;
        }
        boardNames.add(text);

        ObservableList<HBox> boardsList = boardsListView.getItems();
        for(int i=0;i<boardsList.size();i++)
        {
            HBox box = (HBox) boardsList.get(i);
            String text2 = ((Label) box.getChildren().get(0)).getText();
            if(text2.equals(text)) {
                System.out.println("hello");
                return;
            }
        }
        HBox itemBox = new HBox();
        Label itemLabel = new Label(text);
        itemLabel.setPrefWidth(120);
        itemLabel.setPadding(new Insets(6, 1, 6, 1));
        String path = Path.of("", "client", "images", "cancel.png").toString();
        Button removeButton = buttonBuilder(path);
        removeButton.setOnAction(event -> {
            removeBoard(text);
        });
        itemBox.getChildren().addAll(itemLabel, removeButton);
        itemBox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                openBoard(itemBox);
            }
        });

        boardsListView.getItems().add(itemBox);
    }


    /**
     * removes HBox with board from user listView
     *
     * @param key the key of the board to be remvoed
     */
    public void removeBoard(String key) {
        boardNames.remove(key); // first remove it from user's internal list

        ObservableList<HBox> boardsList = boardsListView.getItems();
        HBox itemBox = null;
        for(int i=0;i<boardsList.size();i++)
        {
            HBox box = (HBox) boardsList.get(i);
            String text = ((Label) box.getChildren().get(0)).getText();
            if(text.equals(key)) {
                itemBox = box;
                boardsList.remove(itemBox);
                break;
            }
        }
        String name = ((Label) itemBox.getChildren().get(0)).getText();
        boardsListView.getItems().remove(itemBox);

    }

    /**
     * Opens particular board
     *
     * @param itemBox HBox selected
     */
    public void openBoard(HBox itemBox) {
        String name = ((Label) itemBox.getChildren().get(0)).getText();
        Board board = server.findBoard(name);
        if(board==null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Board does not exist.");
            alert.showAndWait();
            removeBoard(name);
        }
        else{
            mainCtrl.showBoard(board);
        }

    }

    /**
     * Creates delete button
     *
     * @param path path to image
     * @return button with image
     */
    private Button buttonBuilder(String path) {
        String url = getClass().getClassLoader().getResource(path.replace("\\", "/")).toString();
        Image image = new Image(url);
        ImageView picture = new ImageView(image);
        picture.setFitHeight(18);
        picture.setFitWidth(18);
        Button button = new Button();
        button.setMaxSize(20, 20);
        button.setBackground(null);
        button.setPadding(new Insets(6, 1, 6, 3));
        button.setGraphic(picture);
        return button;
    }

    /**
     * User joins a particular board and displays it immediately
     */
    public void joinBoard() {
        String name = textBox.getText().trim();
        if (!name.isEmpty()) {
            Board board = server.findBoard(name);
            if (board == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Board does not exist.");
                alert.showAndWait();
            } else {
                mainCtrl.showBoard(board);
                addBoard(name);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Enter a board name.");
            alert.showAndWait();

        }

        textBox.clear();
    }
    public List<String> getBoards(){
        return boardNames;
    }


}

