package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

public class BoardOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Button addTaskButton;

    @FXML
    private ListView<Label> taskList1;

    @FXML
    private ListView<Label> taskList2;

    @FXML
    private ListView<Label> taskList3;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.taskList1 = new ListView();
        this.taskList2 = new ListView();
        this.taskList3 = new ListView();
    }

    public void createTask() {
        String name = getTaskName();
        taskList1.getItems().add(new Label(name));
    }

    public String getTaskName() {
        TextInputDialog input = new TextInputDialog("task name");
        input.setHeaderText("Give a name to the new task.");
        input.showAndWait();
        input.setTitle("Name popup");
        return input.getEditor().getText();
    }

}
