package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Path;

public class LandingPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField server_ip;
    @FXML
    private ImageView logo1;
    @FXML
    private ImageView exitButton;


    @Inject
    public LandingPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void connect(){
        var q = server_ip.getText();
        long id = 1; //hardcoded id, should extract it from somewhere
        Board board = server.getBoard(id);
        mainCtrl.showBoard(board);
        //need to implement backend, hardcoded it for now
        clearFields();
    }

    public void exit(){
        Platform.exit();
        System.exit(0);
    }

    private void clearFields() {
        server_ip.clear();
    }

    public void changeImageUrl() {
    // Set the image URL of ImageView
        String path = Path.of("", "client", "images", "Logo.png").toString();
        String path2 = Path.of("", "client", "images", "ExitButton.png").toString();
        logo1.setImage(new Image(path));
        exitButton.setImage(new Image(path2));
    }

    public void refresh() {
        //TODO make something to refresh the data from the databases.
    }
}
