package client.scenes;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import com.google.inject.Inject;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import javafx.stage.Stage;


public class BoardCreateCtrl  {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    @FXML private TextField boardNameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox PasswordCheckbox;
    @FXML private Button createButton;
    @Inject
    public BoardCreateCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * button, checkbox and textfield configuration
     */
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
                if(server.findBoard(boardName)!=null){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "This board already exists.");
                    alert.showAndWait();
                }
                else{

                    if (isPasswordEnabled && !password.isEmpty()) {
                        // Close the stage if board name and password are entered
                        Stage stage = (Stage) createButton.getScene().getWindow();
                        mainCtrl.createBoard(boardName,boardName,password.hashCode());
                        boardNameField.clear();
                        passwordField.clear();
                        PasswordCheckbox.setSelected(false);
                        passwordField.setDisable(true);
                        stage.close();
                    } else if (!isPasswordEnabled) {
                        // Close the stage if board name is entered and password is not required
                        Stage stage = (Stage) createButton.getScene().getWindow();
                        mainCtrl.createBoard(boardName,boardName,0);
                        boardNameField.clear();
                        passwordField.clear();
                        stage.close();
                    } else {
                        // Show an error message if password is required but not entered
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a password");
                        alert.showAndWait();
                    }
                }
            } else {
                // Show an error message if board name is not entered
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a board name");
                alert.showAndWait();
            }
        });

    }

}
