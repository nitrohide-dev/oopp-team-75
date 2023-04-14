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

import java.nio.file.Path;

public class LandingPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public HBox header;
    @FXML
    private TextField server_ip;
    @FXML
    private ImageView logo;
    @FXML
    private ImageView exit;

    @Inject
    public LandingPageCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    public void initialize() {
        logo.setImage(new Image(Path.of("", "client", "images", "Logo.png").toString()));
        exit.setImage(new Image(Path.of("", "client", "images", "ExitButton.png").toString()));
    }

    public void connect() {
        if (server_ip.getText().isEmpty())
            server_ip.setText("localhost:8080");
        final String ip = server_ip.getText();
        server_ip.setText("connecting...");
        new Thread(() -> {
            final StompSession session = server.safeConnect(ip);
            if (session != null) {
                server.setDomain(ip);
                server.setSERVER("http://" + ip + "/");
                server.setSession(session);
                Platform.runLater(() -> mainCtrl.showUserMenu());
            } Platform.runLater(() -> server_ip.setText(ip));
        }).start();
    }

    public void exit() {
        Platform.exit();
        System.exit(0);
    }
}
