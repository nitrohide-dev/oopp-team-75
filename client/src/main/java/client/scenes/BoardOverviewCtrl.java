package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.layout.Priority;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class BoardOverviewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ListView<HBox> taskList1;

    @FXML
    private TextField listName1;

    @FXML
    private Button deleteTaskListsButton;

    @FXML
    private HBox listContainer;

    private Group sampleGroup;

    private Map<ListView<HBox>, String> allLists; // Stores all task lists

    @FXML
    private ScrollPane scrollPaneMain;

    @FXML
    private AnchorPane anchorPaneMain;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.allLists = new HashMap<ListView<HBox>, String>();
    }

    /**
     * Initializer
     * Initializes the objects in the scene
     */
    @FXML
    public void initialize() {
        ObservableList<Node> children = listContainer.getChildren();
        sampleGroup = (Group) children.get(0);
        ListsSetup();
        // Sets ScrollPane size, so it's slightly bigger than AnchorPane
        scrollPaneMain.setPrefSize(anchorPaneMain.getPrefWidth()+10,anchorPaneMain.getPrefHeight()+20);

        //initializes the default delete taskListsButton
        setDeleteAction(deleteTaskListsButton, listName1.getText());
        hoverOverDeleteButton(deleteTaskListsButton);
        //initializes the addTask button
        taskList1.setOnMouseClicked(e -> taskOperations(taskList1));
        addTaskButton(taskList1);
    }

//Deleted Scrolling, implemented using ScrollPane
    /**
     * Connects the initial list to its name
     */
    private void ListsSetup() {
        this.allLists.put(taskList1, listName1.getText());
        dragOverHandler(taskList1);
        dragDroppedHandler(taskList1);
    }


    /**
     * This eventHandler is waiting for the addButton to be clicked, after that creates
     * new Group of TextField, ScrollPane and a Deletion Button - new taskList
     */
    public void createTaskList() {
        ObservableList<Node> children = listContainer.getChildren();
        TextField sampleText = (TextField) sampleGroup.getChildren().get(0);
        ScrollPane samplePane = (ScrollPane) sampleGroup.getChildren().get(1);
        ListView<HBox> sampleList = (ListView<HBox>) samplePane.getContent();
        TextField textField = new TextField();
        ListView<HBox> listView = new ListView<>();
        listView.setOnMouseClicked(e -> taskOperations(listView));
        listView.setPrefSize(sampleList.getPrefWidth(), sampleList.getPrefHeight());
        listView.setFixedCellSize(35);
        ScrollPane scrollPane = new ScrollPane(listView);

        //create deleteTaskListsButton
        Button deleteTaskListsButton = new Button("x");

        setDeleteAction(deleteTaskListsButton, textField.getText());
        hoverOverDeleteButton(deleteTaskListsButton);

        deleteTaskListsButton.setLayoutX(191);
        deleteTaskListsButton.setLayoutY(0);
        deleteTaskListsButton.setPrefSize(25, 25);

        addTaskButton(listView);

        textField.setPrefSize(sampleText.getPrefWidth(), sampleText.getPrefHeight());
        textField.setLayoutX(0);
        textField.setLayoutY(0);
        textField.setText("Name your list!");
        scrollPane.setPrefSize(samplePane.getPrefWidth(), samplePane.getPrefHeight());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setLayoutX(0);
        scrollPane.setLayoutY(60);

        Group newGroup = new Group(textField, scrollPane, deleteTaskListsButton);
        newGroup.setLayoutX(sampleGroup.getLayoutX());
        newGroup.setLayoutY(sampleGroup.getLayoutY());
        newGroup.setTranslateX(sampleGroup.getTranslateX());
        newGroup.setTranslateY(sampleGroup.getTranslateY());

        children.add(newGroup);
        dragOverHandler(listView);
        dragDroppedHandler(listView);
        allLists.put(listView, textField.getText());
    }

    /**
     * Method for adding a TaskButton, used when creating a taskList, it creates new tasks
     * @param listView the listview
     */
    public void addTaskButton(ListView<HBox> listView){
        Button addTaskButton = new Button("+");
        addTaskButton.setPadding(new Insets(2, 80, 2, 80));
        HBox box = new HBox(addTaskButton);
        listView.getItems().add(box);
        addTaskButton.setOnAction(e -> createTask(listView));
    }

    /**
     * A method to delete taskLists, and for pop-up asking for confirmation
     * @param deleteTaskListsButton The delete button
     * @param taskListName the name of the task list
     */
    public void setDeleteAction(Button deleteTaskListsButton, String taskListName){
        deleteTaskListsButton.setOnAction(e -> {
            Group parentGroup = (Group) deleteTaskListsButton.getParent();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation Dialog");
            alert.setHeaderText("Delete TaskList");
            alert.setContentText("Are you sure you want to delete '"+taskListName+"'?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                listContainer.getChildren().remove(parentGroup); // remove parent group from Hbox
            }
        });
    }

    /**A method to turn the deleteTasksLists button into pink when mouse is hovering over it
     *
     * @param deleteTaskListsButton the delete button
     */
    public void hoverOverDeleteButton(Button deleteTaskListsButton){
        deleteTaskListsButton.setOnMouseEntered(e -> deleteTaskListsButton.setStyle("-fx-background-color: pink;"));

        deleteTaskListsButton.setOnMouseExited(e -> deleteTaskListsButton.setStyle(null));
    }

    /**
     * Creates a task and puts it in the first list
     * A task contains a label and three buttons for task operations
     */
    public void createTask(ListView<HBox> list) {
        String name = getTaskNamePopup("Task");
        //if (!server.addTask(name)) return;
        createTask(name, list);
    }
    
    public void createTask(String name, ListView<HBox> list) {
        //if (!server.addTask(name)) return;

        //Removes the addTask button
        list.getItems().remove(list.getItems().get(list.getItems().size()-1));

        Label task = new Label(name);
        task.setPrefWidth(120);
        task.setPadding(new Insets(6, 1, 6, 1));
        String path = Path.of("", "client", "images", "cancel.png").toString();
        Button removeButton = buttonBuilder(path);
        path = Path.of("", "client", "images", "pencil.png").toString();
        Button editButton = buttonBuilder(path);
        path = Path.of("", "client", "images", "eye.png").toString();
        Button viewButton = buttonBuilder(path);
        HBox box = new HBox(task, viewButton, editButton, removeButton);
        dragDetectHandler(box,task,list);
        removeButton.setOnAction(e -> deleteTask(list, box));
        editButton.setOnAction(e -> editTask(box));
        viewButton.setOnAction(e -> viewTask(box));
        disableButtons(box);
        box.setHgrow(task, Priority.NEVER);
        list.getItems().add(box);
        //Re-adds the button to the end of the list
        addTaskButton(list);
    }

    /**
     * Creates button for task operations
     *
     * @param path - The path of the image that is on the button
     * @return the created button
     */
    private Button buttonBuilder(String path) {
        String url = Objects.requireNonNull(getClass().getClassLoader().getResource(path.replace("\\", "/"))).toString();
        Image image = new Image(url);
        ImageView picture = new ImageView(image);
        picture.setFitHeight(18);
        picture.setFitWidth(18);
        Button button = new Button();
        button.setMaxSize(20, 20);
        button.setBackground(null);
        button.setPadding(new Insets(6, 1, 6, 3));
        button.setGraphic(picture);
        return button;
    }

    /**
     * popup that ask you to input the name of the thing that you want to create
     *
     * @param item - the type of the thing that you want to create
     * @return the input name
     */
    public String getTaskNamePopup(String item) {
        TextInputDialog input = new TextInputDialog(item + " name");
        input.setHeaderText(item);
        input.setContentText("Enter a name for the " + item.toLowerCase(Locale.ROOT));
        input.setTitle("Name Input Dialog");
        //add css to dialog pane
        input.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("css/BoardOverview.css")).toExternalForm());
        //make preferred size bigger
        input.getDialogPane().setPrefSize(400, 200);

        input.showAndWait();
        return input.getEditor().getText();
    }

    /**
     * When any of the tasks is clicked it gives the user options to view, edit or remove it
     */
    public void taskOperations(ListView<HBox> list) {
        int index = list.getSelectionModel().getSelectedIndex();
        if (index >= list.getItems().size() - 1) return;
        resetOptionButtons();
        HBox box = list.getItems().get(index);
        Button removeButton = (Button) box.getChildren().get(1);
        Button editButton = (Button) box.getChildren().get(2);
        Button viewButton = (Button) box.getChildren().get(3);
        removeButton.setDisable(false);
        removeButton.setVisible(true);
        editButton.setDisable(false);
        editButton.setVisible(true);
        viewButton.setDisable(false);
        viewButton.setVisible(true);
    }

    /**
     * resets the buttons for task operations to their default settings
     */
    private void resetOptionButtons() {
        for (ListView<HBox> list : allLists.keySet()) {
            for (int i = 0; i < list.getItems().size() - 1; i++) {
                disableButtons(list.getItems().get(i));
            }
        }
    }

    /**
     * disables and hides all buttons in a box, containing a task
     *
     * @param box - box that contains a task and its operations buttons
     */
    private void disableButtons(HBox box) {
        Button removeButton = (Button) box.getChildren().get(1);
        Button editButton = (Button) box.getChildren().get(2);
        Button viewButton = (Button) box.getChildren().get(3);
        removeButton.setDisable(true);
        removeButton.setVisible(false);
        editButton.setDisable(true);
        editButton.setVisible(false);
        viewButton.setDisable(true);
        viewButton.setVisible(false);
    }

    /**
     * Edits the chosen task
     *
     * @param task - a HBox, containing the task
     */
    public void editTask(HBox task) {
        //if (!server.editTask(task.getText())) return;
        ((Label) task.getChildren().get(0)).setText(getTaskNamePopup("Task"));
    }

    /**
     * The user can see detailed info about the task
     *
     * @param task - a HBox, containing the task
     */
    public void viewTask(HBox task) {
    }

    /**
     * Deletes given task
     *
     * @param task - a HBox, containing the task
     */
    public void deleteTask(ListView<HBox> list, HBox task) {
        //if (!server.removeTask(task.getText())) return;
        list.getItems().remove(task);
    }

//    /**
//     * method that moves a task from one list to another
//     *
//     * @param fromList - the list containing the taskD
//     * @param toList   - the list in which we want to put the task
//     * @param task     the task to be moved
//     */
//    public void moveTask(ListView fromList, ListView toList, HBox task) {
//        String list1 = allLists.get(fromList);
//        String list2 = allLists.get(toList);
//        if (list1 == null || list2 == null) return;
//        if (list1.equals(list2)) return;
//        if (server.moveTask(mainCtrl.getCurrBoard(), list1, list2, task)) {
//            fromList.getItems().remove(task);
//            toList.getItems().add(task);
//        }
//    }

    public void dragOverHandler(ListView<HBox> list) {
        list.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if(db.hasString()) {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
    }

    public void dragDroppedHandler(ListView<HBox> list) {
        list.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                createTask(db.getString(),list);
                success = true;
                db.clear();
            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    public void dragDetectHandler(HBox box,Label task,ListView<HBox> list) {
        ObservableList<Node> children = anchorPaneMain.getChildren();
        box.setOnDragDetected(event -> {
            Dragboard db = box.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(task.getText());
            db.setContent(content);
            db.setDragView(new Text(task.getText()).snapshot(null, null), event.getX(), event.getY());
            event.consume();
        });
        box.setOnDragDone(event -> {
            Dragboard db = event.getDragboard();
            if(!db.hasString())
            {
                deleteTask(list, box);
            }
        });
    }
}

