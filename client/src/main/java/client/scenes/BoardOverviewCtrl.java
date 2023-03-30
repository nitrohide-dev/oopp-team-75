package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.application.Platform;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class BoardOverviewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Group group;
    @FXML
    public HBox header;
    @FXML
    public ScrollPane sPaneListView;

    @FXML
    private ListView<HBox> taskList1;

    @FXML
    private TextField listName1;

    @FXML
    private Button deleteTaskListsButton;

    @FXML
    private HBox listContainer;

    private Group sampleGroup;

    private long boardKey;
    private Map<ListView, String> allLists; // Stores all task lists
    private final Map<ListView, TaskList> listMap; // Stores all task lists
    private final Map<HBox, Task> taskMap; // Stores all tasks

    @FXML
    private ScrollPane scrollPaneMain;

    @FXML
    private AnchorPane anchorPaneMain;
    @FXML
    private ImageView logo1;
    @FXML
    private ImageView exitButton;
    @FXML
    private ImageView lockButton;
    @FXML
    private ImageView dropDownMenu;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.allLists = new HashMap<>();
        this.listMap = new HashMap<>();
        this.taskMap = new HashMap<>();
    }

    /**
     * Initializer
     * Initializes the initial objects in the scene
     */
    @FXML
    public void initialize() {
        ObservableList<Node> children = listContainer.getChildren();
        sampleGroup = (Group) children.get(0);
        // Sets ScrollPane size, so it's slightly bigger than AnchorPane
        scrollPaneMain.setPrefSize(anchorPaneMain.getPrefWidth() + 10, anchorPaneMain.getPrefHeight() + 20);
    }

    /**
     * Connects to the server for automatic refreshing.
     */
    public void connect() {
        server.subscribe("/topic/boards", Board.class, b -> Platform.runLater(() -> this.refresh(b)));
    }

    /**
     * Updates the board to a new board, and regenerates the boardOverview,
     * only if the new boards key is equal to the previous boards key (i.e.
     * only new versions of the same board are accepted).
     * @param board the board to refresh to.
     */
    public void refresh(Board board) {
        mainCtrl.setCurrBoard(board);
        load(board);
    }

    /**
     * Clears the previous boardOverview, and generates a new one from a given board.
     * @param board the board set the boardOverview to
     */
    public void load(Board board) {
        // removes all lists including their tasks
        listContainer.getChildren().clear();
        allLists.clear();
        listMap.clear();
        taskMap.clear();

        // creates new lists
        List<TaskList> listOfLists = board.getTaskLists();
        if (listOfLists.size() == 0)
            return;
        for (TaskList taskList : listOfLists) {
            ListView<HBox> ourList = addTaskList(taskList);
            dragOverHandler(ourList);
            dragDroppedHandler(ourList);
            for(Task task : taskList.getTasks())
                addTask(task.getTitle(),ourList,task);
        }

        // sets attributes accordingly
        sampleGroup = (Group) listContainer.getChildren().get(0);

        //initializes the default delete taskListsButton
        setDeleteAction(deleteTaskListsButton, listName1.getText(),taskList1);
        hoverOverDeleteButton(deleteTaskListsButton);
    }

    /**
     * Shortened variant to make access to the board easier.
     * @return the current board
     */
    public Board getBoard() {
        return mainCtrl.getCurrBoard();
    }


    /**
     * This eventHandler is waiting for the addButton to be clicked, after that creates
     * new Group of TextField, ScrollPane and a Deletion Button - new taskList
     */
    public ListView<HBox> createTaskList() {
        TaskList taskList = getBoard().createTaskList();
        ListView<HBox> list = addTaskList(taskList);
        server.updateBoard(getBoard()); // updates server
        return list;
    }

    /**
     * This eventHandler is waiting for the addButton to be clicked, after that creates
     * new Group of TextField, ScrollPane and a Deletion Button - new taskList
     * @param taskList a TaskList common object that is mapped with the created tasklist for backend-frontend communication
     */
    public ListView<HBox> addTaskList(TaskList taskList) {
        ScrollPane samplePane = (ScrollPane) sampleGroup.getChildren().get(1);
        ListView<HBox> sampleList = (ListView<HBox>) samplePane.getContent();
        TextField textField = new TextField();
        textField.setId("listName1");
        textField.setOnInputMethodTextChanged(e -> taskList.setTitle(textField.getText()));

        ListView<HBox> listView = new ListView<>();
        listView.setOnMouseClicked(e -> taskOperations(listView));
        listView.setPrefSize(sampleList.getPrefWidth(), sampleList.getPrefHeight());
        listView.setFixedCellSize(35);
        listView.setId("taskList1");
        ScrollPane scrollPane = new ScrollPane(listView);

        //create deleteTaskListsButton
        Button deleteTaskListsButton = new Button("X");
        setDeleteAction(deleteTaskListsButton, textField.getText(), listView);
        hoverOverDeleteButton(deleteTaskListsButton);


        setPropertiesTaskList(deleteTaskListsButton, textField, scrollPane);
        addTaskButton(listView);

        Group newGroup = new Group(textField, scrollPane, deleteTaskListsButton);
        newGroup.setLayoutX(sampleGroup.getLayoutX());
        newGroup.setLayoutY(sampleGroup.getLayoutY());
        newGroup.setTranslateX(sampleGroup.getTranslateX());
        newGroup.setTranslateY(sampleGroup.getTranslateY());
        newGroup.getStylesheets().addAll(sampleGroup.getStylesheets()); //does not work

        listContainer.getChildren().add(newGroup);
        dragOverHandler(listView);
        dragDroppedHandler(listView);
        allLists.put(listView, textField.getText());
        listMap.put(listView, taskList);
        return listView;
    }

    /**
     * Sets the properties of the taskList
     * @param deleteTaskListsButton the delete button
     * @param textField the text field
     * @param scrollPane the scrollpane
     */
    public void setPropertiesTaskList(Button deleteTaskListsButton, TextField textField, ScrollPane scrollPane) {
        deleteTaskListsButton.setLayoutX(170);
        deleteTaskListsButton.setLayoutY(0);
        deleteTaskListsButton.setPrefSize(25, 25);
        deleteTaskListsButton.setFont(new Font(19));
        deleteTaskListsButton.setId("deleteTaskListsButton");

        textField.setPrefSize(180, 25);
        textField.setLayoutX(0);
        textField.setLayoutY(0);
        textField.setAlignment(javafx.geometry.Pos.CENTER);
        textField.setFont(new Font(19));
        textField.setText("Name your list!");
        ScrollPane samplePane = (ScrollPane) sampleGroup.getChildren().get(1);
        scrollPane.setPrefSize(samplePane.getPrefWidth(), samplePane.getPrefHeight());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setLayoutX(0);
        scrollPane.setLayoutY(50);
        scrollPane.setId("sPaneListView");

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
     * @param button The delete button
     * @param taskListName the name of the task list
     */
    public void setDeleteAction(Button button, String taskListName, ListView<HBox> list){
        button.setOnAction(e -> {
            Group parentGroup = (Group) button.getParent();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete Confirmation Dialog");
            alert.setHeaderText("Delete TaskList");
            alert.setContentText("Are you sure you want to delete '"+taskListName+"'?");
            //add css to dialog pane
            alert.getDialogPane().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("css/BoardOverview.css")).toExternalForm());
            //make preferred size bigger
            alert.getDialogPane().setPrefSize(400, 200);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                listContainer.getChildren().remove(parentGroup); // remove parent group from HBox
                TaskList list1 = listMap.get(list);
                getBoard().removeTaskList(list1);
                server.updateBoard(getBoard()); // updates server
            }
        });
    }

    /**A method to turn the deleteTasksLists button into pink when mouse is hovering over it
     *
     * @param deleteTaskListsButton the delete button
     */
    public void hoverOverDeleteButton(Button deleteTaskListsButton){
        deleteTaskListsButton.setOnMouseEntered(e -> deleteTaskListsButton.setStyle("-fx-background-color: pink;"));

        deleteTaskListsButton.setOnMouseExited(e -> deleteTaskListsButton.setStyle("-fx-background-color: transparent;"));
    }

    /**
     * task creation method caused by the user's manual task addition.
     * @return the created task
     */
    public HBox createTask(ListView<HBox> list) {
        return createTask(inputTaskName(), list);
    }


    /**
     * creates a task in the given list with the given name
     * @param name the name of the task to be created
     * @param list the list in which the task should be created
     * @return the created task
     */
    public HBox createTask(String name,ListView<HBox> list) {
        Task task1 = listMap.get(list).createTask();
        task1.setTitle(name);
        HBox task = addTask(name, list,task1);
        server.updateBoard(getBoard()); // updates server
        return task;
    }
    /**
     * adds a task to a given list in frontend and maps it to the corresponding Task common data type
     * @param name the name of the task to be added
     * @param list the list to which the task should be added
     * @param task1 the Task common data type to map to the task for frontend-backend communication
     * @return the created task
     */
    public HBox addTask(String name, ListView<HBox> list,Task task1) {

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
        disableTaskButtons(box);
        HBox.setHgrow(task, Priority.NEVER);
        list.getItems().add(box);
        //Re-adds the button to the end of the list
        addTaskButton(list);
        taskMap.put(box,task1);

        return box;
    }

    /**
     * Creates button for task operations
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
     * popup that ask you to input a task name.
     * @return the input name
     */
    public String inputTaskName() {
        TextInputDialog input = new TextInputDialog("task name");
        input.setHeaderText("Task name");
        input.setContentText("Please enter a name for the task:");
        input.setTitle("Input Task Name");
        //add css to dialog pane
        input.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("css/BoardOverview.css")).toExternalForm());
        //make preferred size bigger
        input.getDialogPane().setPrefSize(400, 200);
        //trying to add icon to dialog
        String path = Path.of("", "client", "images", "Logo.png").toString();
        Stage stage = (Stage) input.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(path));

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
        enableTaskButtons(list.getItems().get(index));
    }

    /**
     * resets the buttons for task operations to their default settings
     */
    private void resetOptionButtons() {
        for (ListView<HBox> list : allLists.keySet()) {
            for (int i = 0; i < list.getItems().size() - 1; i++) {
                disableTaskButtons(list.getItems().get(i));
            }
        }
    }

    /**
     * Disables and hides the buttons of a task
     * @param box the task box
     */
    private void disableTaskButtons(HBox box) {
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
     * Enables and shows the buttons of a task
     * @param box the task box
     */
    private void enableTaskButtons(HBox box) {
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
     * Edits the chosen task
     * @param task the task box
     */
    public void editTask(HBox task) {
        String name = inputTaskName();
        ((Label) task.getChildren().get(0)).setText(name);
        Task task1 = taskMap.get(task);
        task1.setTitle(name);
        server.updateBoard(getBoard()); // updates server
    }

    /**
     * The user can see detailed info about the task
     * @param task - a HBox, containing the task
     */
    public void viewTask(HBox task) {}

    /**
     * Deletes given task
     * @param task - a HBox, containing the task
     */
    public void deleteTask(ListView<HBox> list, HBox task) {
        TaskList list1 = listMap.get(list);
        Task task1 = taskMap.get(task);
        list1.removeTask(task1);
        list.getItems().remove(task);
        server.updateBoard(getBoard()); // updates server
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

    /**
     * Handles the list's behaviour once a task is being dragged over it
     * @param list the list which behaviour is to be configured
     */
    public void dragOverHandler(ListView<HBox> list) {
        list.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if(db.hasString()) {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
    }

    /**
     *  handles a list's behaviour when a task is dropped onto it
     * @param list the list which behaviour is to be configured
     */
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

    /**
     * Handles the task's behaviour when it's being dragged AND deletes it from the given list when it's being dropped
     * @param box - the box that contains the task which behaviour is to be configured
     * @param task - the task label, containing its name
     * @param list - the list that contains the task
     */
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

    /**
     * @param mouseEvent - the mouse event
     */
    public void goToPrevious(MouseEvent mouseEvent) {
        //TODO write code to go to previous page
    }

    public void changeImageUrl() {
        // Set the image URL of ImageView
        String path = Path.of("", "client", "images", "Logo.png").toString();
        String path2 = Path.of("", "client", "images", "ExitButton.png").toString();
        String path3 = Path.of("", "client", "images", "Dots.png").toString();
        String path4 = Path.of("", "client", "images", "lockUnlocked.png").toString();
        logo1.setImage(new Image(path));
        exitButton.setImage(new Image(path2));
        dropDownMenu.setImage(new Image(path3));
        lockButton.setImage(new Image(path4));
    }
}

