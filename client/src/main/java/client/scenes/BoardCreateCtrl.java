package client.scenes;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import com.google.inject.Inject;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class BoardCreateCtrl  {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    @FXML private TextField boardNameField;

    @FXML private Button createButton;
    @Inject
    public BoardCreateCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }


    /**
     * creates a new board(or not if it exists)
     */
    public void send(){
        String boardName = boardNameField.getText();

        if (!boardName.isEmpty()) {
            if(server.findBoard(boardName)!=null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "This board already exists.");
                alert.showAndWait();
            }
            else{

                // Close the stage if board name is entered
                Stage stage = (Stage) createButton.getScene().getWindow();
                mainCtrl.createBoard(boardName.trim(),boardName.trim());
                boardNameField.clear();
                stage.close();
            }
        } else {
            // Show an error message if board name is not entered
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a board name");
            alert.showAndWait();
        }
    }}



