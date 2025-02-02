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
import javafx.scene.layout.Region;

import java.nio.file.Path;
import java.util.List;

public class AdminOverviewCtrl {

    private ServerUtils server;
    private MainCtrl mainCtrl;

    @FXML private ImageView logo;
    @FXML private ImageView exit;
    @FXML private ImageView password;

    private List<Board> boards;
    @FXML public ListView<HBox> boardsListView;

    @Inject
    public AdminOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize() {
        logo.setImage(new Image(Path.of("", "client", "images", "Logo.png").toString()));
        exit.setImage(new Image(Path.of("", "client", "images", "ExitButton.png").toString()));
        password.setImage(new Image(Path.of("", "client", "images", "password.png").toString()));
    }

    /**
     * refreshes admin overview
     */
    public void refresh() {
        boardsListView.getItems().clear();
        boards = server.getAllBoards();
        for (Board b : boards)
            this.addBoardToListView(b.getKey());
    }


    /**
     * exits admin overview and server
     */
    @FXML
    private void exit() {
        mainCtrl.showUserMenu();
        server.logout();
    }


    /**
     * @param text name of the board
     *
     * used for admin overview initialization
     */
    public void addBoardToListView(String text) {

        HBox itemBox = new HBox();
        Label itemLabel = new Label(text);
        itemLabel.setPrefWidth(120);
        itemLabel.setPadding(new Insets(6, 1, 6, 1));
        String path = Path.of("", "client", "images", "cancel.png").toString();
        Button removeButton = buttonBuilder(path);
        removeButton.setOnAction(event -> removeBoard(itemBox));
        Region region = new Region();
        region.setPrefSize(640, 20);
        itemBox.getChildren().addAll(itemLabel,region, removeButton);
        itemBox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2)
                openBoard(itemBox);
        });
        boardsListView.getItems().add(itemBox);
    }

    /**
     * @param itemBox item from list
     *
     *  opens a board for admin
     */
    private void openBoard(HBox itemBox) {
        String name = ((Label) itemBox.getChildren().get(0)).getText();
        Board board = server.findBoard(name);
        mainCtrl.showBoard(board);
    }

    /**
     * @param itemBox item from list
     *
     * removes board from database
     */
    private void removeBoard(HBox itemBox) {
        boardsListView.getItems().remove(itemBox);
        server.deleteBoard(((Label) itemBox.getChildren().get(0)).getText());
    }


    /**
     * button builder
     * @param path path to the image
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
     * password change
     */
    @FXML
    private void changePassword() {
        mainCtrl.showChangePassword();
    }

}
