package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LandingPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField server_ip;


    @Inject
    public LandingPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void connect(){
        var q = server_ip.getText();
        //need to implement some kind of connect thingy?
        clearFields();
    }

    private void clearFields() {
        server_ip.clear();
    }
}
