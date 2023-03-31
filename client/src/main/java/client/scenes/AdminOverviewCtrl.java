package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AdminOverviewCtrl{

    private ServerUtils server;
    private MainCtrl mainCtrl;

    private List<Board> boards;

    public ListView<HBox> boardsListView;

    @Inject
    public AdminOverviewCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;

    }

    public void refresh(){
        boards = server.getAllBoards();
        System.out.println(boards);
        for(Board b : boards){
            this.addBoardToListView(b.getKey());
        }
    }




    @FXML
    private void logOut(){
        boards= new ArrayList<>();
        boardsListView.getItems().clear();
        mainCtrl.showUserMenu();
        mainCtrl.setAdminPresence(false);
    }


    public void addBoardToListView(String text) {

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

    private void openBoard(HBox itemBox) {
        String name = ((Label) itemBox.getChildren().get(0)).getText();
        Board board = server.findBoard(name);
        mainCtrl.showBoardNewWindow(board);
    }

    private void removeBoard(HBox itemBox) {
        boardsListView.getItems().remove(itemBox);
        server.deleteBoard(((Label) itemBox.getChildren().get(0)).getText());

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

@FXML
    private void changePassword(){
     mainCtrl.changePassword();
    }

}
