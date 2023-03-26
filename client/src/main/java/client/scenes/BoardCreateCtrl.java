package client.scenes;

import client.utils.ServerUtils;

import javafx.fxml.FXML;

import javafx.scene.control.*;


public class BoardCreateCtrl  {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML private TextField boardNameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox showPasswordCheckbox;

    public BoardCreateCtrl(ServerUtils server,MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }
    public void initialize() {
        showPasswordCheckbox.setOnAction(event -> {
            boolean showPassword = showPasswordCheckbox.isSelected();
            passwordField.setDisable(!showPassword);
        });
    }

}
