package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LandingPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField server_ip;
    @FXML
    private Button connect_button;


    @Inject
    public LandingPageCtrl(ServerUtils server, MainCtrl mainCtrl, Button connect_button) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.connect_button = connect_button;
    }

    public void connect(){
        var q = server_ip.getText();
        long id = 1; //hardcoded id, should extract it from somewhere
        Board board = server.getBoard(id);
        mainCtrl.showBoard(board);
        //need to implement backend, hardcoded it for now
        clearFields();
    }

    private void clearFields() {
        server_ip.clear();
    }

    public void refresh() {
        //make something to refresh the data from the databases.
    }
}
