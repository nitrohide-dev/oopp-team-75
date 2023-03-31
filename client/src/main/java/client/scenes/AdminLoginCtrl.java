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

    @FXML
    private PasswordField password;

    @Inject
    public AdminLoginCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void login(){
        System.out.println(hashPassword("pass"));
        if(!password.getText().equals("") && server.authenticate(hashPassword(password.getText())) ){ // TBD
            password.clear();
            Stage stage = (Stage) password.getScene().getWindow();
            mainCtrl.adminOverview();
            stage.close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a correct password.");
            password.clear();
            alert.showAndWait();
        }
    }

    public String hashPassword(String password) {
        // use a hash function to hash the password
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash function not available", e);
        }
    }

}
