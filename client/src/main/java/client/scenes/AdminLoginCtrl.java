package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AdminLoginCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML private PasswordField passwordField;
    private Alert emptyPasswordAlert = new Alert(Alert.AlertType.ERROR, "Please enter a password.");
    private Alert incorrectPasswordAlert = new Alert(Alert.AlertType.ERROR, "Please enter a password.");

    @Inject
    public AdminLoginCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * logs you in as an admin
     */
    public void login() {
        if (passwordField.getText().isEmpty())
            emptyPasswordAlert.showAndWait();
        else if (!server.authenticate(hashPassword(passwordField.getText())) )
            incorrectPasswordAlert.showAndWait();
        else {
            passwordField.clear();
            Stage stage = (Stage) passwordField.getScene().getWindow();
            stage.close();
            mainCtrl.showAdminOverview();
        }
    }

    /**
     * for security
     * @param password string to be hashed
     * @return hashed password
     */
    public String hashPassword(String password) {
        if (password == null)
            throw new IllegalArgumentException();

        // use a hash function to hash the password
        try {
            byte[] hash = MessageDigest
                    .getInstance("SHA-256")
                    .digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash function not available", e);
        }
    }

}
