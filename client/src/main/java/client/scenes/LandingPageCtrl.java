package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.springframework.messaging.simp.stomp.StompSession;

import java.io.IOException;
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
    public LandingPageCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void connect(){
        final String text = server_ip.getText();
        final String ip = "".equals(text) ? "localhost:8080" : text;
        server_ip.setText("connecting...");
        new Thread(() -> {
            final StompSession session = server.safeConnect(ip);
            if (session != null) {
                server.setSERVER("http://" + ip + "/");
                server.setSession(session);
                Platform.runLater(() -> {
                    try {
                        mainCtrl.showUserMenuFirstTime();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else Platform.runLater(() -> server_ip.setText(ip));

        }).start();
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
