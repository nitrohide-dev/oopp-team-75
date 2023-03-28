package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Path;

public class LandingPageCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    @FXML
    private TextField server_ip;
    @FXML
    private Button connect_button;
    @FXML
    private ImageView logo1;


    @Inject
    public LandingPageCtrl(ServerUtils server, MainCtrl mainCtrl, Button connect_button) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.connect_button = connect_button;
    }

    public void connect(){
        //String boardKey = "a";
        // gets board from the database, or creates one if it doesn't exist
//        Board board = server.findBoard(boardKey);
//        if (board == null)
//            board = server.createBoard(new CreateBoardModel(boardKey, "a", 0));
//        mainCtrl.showBoard(board);
        // need to implement backend, hardcoded it for now

        mainCtrl.showUserMenu();
        clearFields();
    }

    private void clearFields() {
        server_ip.clear();
    }

    public void changeImageUrl() {
    // Set the image URL of ImageView
        String path = Path.of("", "client", "images", "Logo.png").toString();
        logo1.setImage(new Image(path));
    }

    public void refresh() {
        //make something to refresh the data from the databases.
    }
}
