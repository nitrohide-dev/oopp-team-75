package client.scenes;

import client.utils.ServerUtils;

import javafx.fxml.FXML;
import com.google.inject.Inject;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class BoardCreateCtrl  {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML private TextField boardNameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox PasswordCheckbox;
    @FXML private Button createButton;
    @Inject
    public BoardCreateCtrl(ServerUtils server,MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }
    public void initialize() {
        PasswordCheckbox.setOnAction(event -> {
            boolean showPassword = PasswordCheckbox.isSelected();
            passwordField.setDisable(!showPassword);
        });
        createButton.setOnAction(event ->{
            String boardName = boardNameField.getText();
            boolean isPasswordEnabled = PasswordCheckbox.isSelected();
            String password = passwordField.getText();


            if (!boardName.isEmpty()) {
                if (isPasswordEnabled && !password.isEmpty()) {
                    // Close the stage if board name and password are entered
                    Stage stage = (Stage) createButton.getScene().getWindow();
                    mainCtrl.createBoard(boardName,password.hashCode());
                    stage.close();
                } else if (!isPasswordEnabled) {
                    // Close the stage if board name is entered and password is not required
                    Stage stage = (Stage) createButton.getScene().getWindow();
                    mainCtrl.createBoard(boardName,0);
                    stage.close();
                } else {
                    // Show an error message if password is required but not entered
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a password");
                    alert.showAndWait();
                }
            } else {
                // Show an error message if board name is not entered
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a board name");
                alert.showAndWait();
            }
        });

    }

}
