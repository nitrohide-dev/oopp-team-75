package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

import commons.Board;
import commons.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


public class UserMenuCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public ListView<HBox> boardsListView;

    private List<String> boardNames;
    private User user;
    @FXML
    private Button createBoard;
    @Inject
    public UserMenuCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.user = new User();

    }

    public void initialize() throws IOException {
        boardNames=user.readFromCsv();
        for(String board:boardNames){
           addHBoxToListView(board);
        }
    }

    public User getUser(){
        return user;
    }
    public void addBoardToList(String name,int password){
        user.addBoard(name,password);
        addHBoxToListView(name);
    }

    public void createBoard(){
        mainCtrl.showBoardCreate();
    }

    public void showUserBoards(){

    }
    public void addHBoxToListView(String text) {

        HBox itemBox = new HBox();
        Label itemLabel = new Label(text);
        itemLabel.setPrefWidth(120);
        itemLabel.setPadding(new Insets(6, 1, 6, 1));
        String path = Path.of("", "client", "images", "cancel.png").toString();
        Button removeButton = buttonBuilder(path);
        removeButton.setOnAction(event -> {
                removeBoard(itemBox);
        });
        itemBox.getChildren().addAll(itemLabel, removeButton);
        itemBox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                openBoard(itemBox);
            }
        });

        boardsListView.getItems().add(itemBox);
    }

    public void removeBoard(HBox itemBox){

        String name = ((Label)itemBox.getChildren().get(0)).getText();
        removeBoardForUser(name);
        boardsListView.getItems().remove(itemBox);

    }

    public void openBoard(HBox itemBox){
        String name = ((Label)itemBox.getChildren().get(0)).getText();
        System.out.println(name);
        Board board = server.findBoard(name);
        System.out.println(board.toString());
        mainCtrl.showBoard(board);
    }

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
    public void removeBoardForUser(String name){
        user.deleteBoard(name);
        boardNames.remove(name);
    }
}
