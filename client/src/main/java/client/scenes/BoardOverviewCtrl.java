package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;


/**
 * I added ListView everywhere with <String> attributes just for as a starter
 * Later it should have <Task>
 */
public class BoardOverviewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ListView<Label> taskList1;

    @FXML
    private ListView<Label> taskList2;

    @FXML
    private ListView<Label> taskList3;

    @FXML
    private TextField listName1;

    @FXML
    private TextField listName2;

    @FXML
    private TextField listName3;



    private Map<ListView, String> allLists;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        initialize();
        setListsNames();
    }

    private void initialize() {
        this.taskList1 = new ListView();
        this.taskList2 = new ListView();
        this.taskList3 = new ListView();
        this.allLists = new HashMap<>();
        this.listName1 = new TextField();
        this.listName2 = new TextField();
        this.listName3 = new TextField();
    }

    /**
     * Connects all lists to their names
     */
    private void setListsNames() {
        this.allLists.put(taskList1, listName1.getText());
        this.allLists.put(taskList2, listName2.getText());
        this.allLists.put(taskList3, listName3.getText());
    }

    /**
     * Creates task and puts it in the first list
     */
    public void createTask() {
        String name = getTaskNamePopup("Task");
        //if (!server.addTask(name)) return;
        Label task = new Label(name);
        taskList1.getItems().add(task);
    }

    /**
     * When any of the tasks is clicked it gives the user option to view, edit or remove it
     */
    public void taskOperations() {
        Label task = taskList1.getSelectionModel().getSelectedItem();
        if (task == null) return;
        //Popup with options to view, edit or remove the selected task
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(task.getText());
        alert.setContentText("Choose your option.");

        ButtonType editButton = new ButtonType("Edit");
        ButtonType viewButton = new ButtonType("View");
        ButtonType removeButton = new ButtonType("Remove");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(editButton, viewButton, removeButton, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == editButton){
            editTask(task);
        } else if (result.get() == viewButton) {
            viewTask(task);
        } else if (result.get() == removeButton) {
            deleteTask(task);
        } else {
            return;
        }
    }

    public void editTask(Label task) {
        //if (!server.editTask(task.getText())) return;
        int index = taskList1.getItems().indexOf(task);
        taskList1.getItems().get(index).setText(getTaskNamePopup("Task"));
    }

    public void viewTask(Label task) {
    }


    public void deleteTask(Label task) {
        //if (!server.removeTask(task.getText())) return;
        taskList1.getItems().remove(task);
    }

    /**
     * popup that ask you to input the name of the thing that you want to create
     * @return the input name
     */
    public String getTaskNamePopup(String item) {
        TextInputDialog input = new TextInputDialog(item + " name");
        input.setHeaderText(item);
        input.setContentText("Please enter a name for the new " + item.toLowerCase(Locale.ROOT) + ".");
        input.showAndWait();
        input.setTitle("Name Input Dialog");
        return input.getEditor().getText();
    }

    /**
     * method that moves a task from one list to another
     * @param fromList - the list containing the task
     * @param toList - the list in which we want to put the task
     * @param task the task to be moved
     */
    public void moveTask(ListView fromList, ListView toList, Task task) {
        String list1 = allLists.get(fromList);
        String list2 = allLists.get(toList);
        if (list1 == null || list2 == null) return;
        if (list1.equals(list2)) return;
        if (server.moveTask(mainCtrl.getCurrBoard(), list1, list2, task)) {
            fromList.getItems().remove(task);
            toList.getItems().add(task);
        }
    }

}
