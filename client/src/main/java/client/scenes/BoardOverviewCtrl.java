package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;

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

    @FXML
    private ScrollBar scrollBar;

    @FXML
    private HBox hBox;

    private Group sampleGroup;

    private Map<ListView, String> allLists; // Stores all task lists

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.allLists = new HashMap();
    }

    /**
     * Initializer
     * Initializes the objects in the scene
     */
    @FXML
    public void initialize(){
        ObservableList children = hBox.getChildren();
        sampleGroup = (Group) children.get(1);
        setListsNames();
        scroll();
    }

    /**
     * Not the best implementation, will work on making the scroll bar appear only when needed and also
     * so I will make the scroll bar proportionally larger.
     */
    public void scroll(){
        scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            double scrollWidth =  - Math.abs(hBox.getWidth()  - hBox.getScene().getWidth());
            hBox.setTranslateX(newValue.doubleValue() * scrollWidth);
        });
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
     * This eventHandler is waiting for the addButton to be clicked, after that creates
     * new Group of TextField and a ScrollPane - new taskList
     */
    public void createTaskList() {
        ObservableList children = hBox.getChildren();
        TextField sampleText = (TextField) sampleGroup.getChildren().get(0);
        ScrollPane samplePane = (ScrollPane) sampleGroup.getChildren().get(1);
        TextField textField = new TextField();
        ListView<Label> listView = new ListView<>();
        ScrollPane scrollPane = new ScrollPane(listView);

        textField.setPrefSize(sampleText.getPrefWidth(), sampleText.getPrefHeight());
        textField.setLayoutX(0);
        textField.setLayoutY(0);
        textField.setText("Name your list!");
        scrollPane.setPrefSize(samplePane.getPrefWidth(), samplePane.getPrefHeight());
        scrollPane.setLayoutX(0);
        scrollPane.setLayoutY(60);

        System.out.println(samplePane+" ----- "+sampleText);

        Group newGroup = new Group(textField, scrollPane);
        newGroup.setLayoutX(sampleGroup.getLayoutX());
        newGroup.setLayoutY(sampleGroup.getLayoutY());
        newGroup.setTranslateX(sampleGroup.getTranslateX());
        newGroup.setTranslateY(sampleGroup.getTranslateY());

        children.add(newGroup);
        allLists.put(listView, textField.getText());
    }

    /**
     * Creates a task and puts it in the first list
     */
    public void createTask() {
        String name = getTaskNamePopup("Task");
        //if (!server.addTask(name)) return;
        Label task = new Label(name);
        System.out.println(task);
        taskList1.getItems().add(task);
        System.out.println(taskList1.getItems());
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
