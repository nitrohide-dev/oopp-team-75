package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordChangeCtrl {

    private ServerUtils server;
    private  MainCtrl mainCtrl;
    @FXML
    private Button confirmButton;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField password1;


    @Inject
    public PasswordChangeCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    @FXML
    public void initialize(){
        confirmButton.setOnAction(event ->{

            if (!(password.getText().equals("")||password1.getText().equals(""))) {
                if(!password.getText().equals(password1.getText())){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords don't match.");
                    alert.showAndWait();
                }
                else{
                    Stage stage = (Stage) password.getScene().getWindow();
                    stage.close();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill out both fields.");
                alert.showAndWait();
            }
        });
    }

    /**
     *
     * used for hashing for increased security
     * @param password unhashed password
     * @return hashed password
     */
    public String hashPassword(String password) {
        // use a hash function to hash the password, will be used
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash function not available", e);
        }
    }

}
