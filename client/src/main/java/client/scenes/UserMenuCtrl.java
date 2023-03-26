package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class UserMenuCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private Button createBoard;
    @Inject
    public UserMenuCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }
    public void createBoard() throws IOException {
        mainCtrl.createBoard();

    }

}
