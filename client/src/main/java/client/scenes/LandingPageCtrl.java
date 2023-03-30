package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.nio.file.Path;

public class LandingPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public HBox header;
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
        String ip = server_ip.getText();
        server_ip.setText("connecting");
        if (ip.equals(""))
            ip = "localhost:8080";
        if (!server.testServer(ip)) {
            server_ip.setText("could not connect");
            return;
        }
        server.setServer(ip);
        mainCtrl.showUserMenu();
    }

    public void exit(){
        Platform.exit();
        System.exit(0);
    }

    public void changeImageUrl() {
        // Set the image URL of ImageView
        String path = Path.of("", "client", "images", "Logo.png").toString();
        String path2 = Path.of("", "client", "images", "ExitButton.png").toString();
        logo1.setImage(new Image(path));
        exitButton.setImage(new Image(path2));
    }
}
