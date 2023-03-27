package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Task;
import commons.TaskList;
import commons.CreateBoardModel;
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
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private long boardKey;
    private Map<ListView, String> allLists; // Stores all task lists
    private Map<ListView, TaskList> listMap; // Stores all task lists
    private Map<HBox, Task> taskMap; // Stores all tasks
    private Board board;
    private long boardTime;

    @FXML
    private ScrollPane scrollPaneMain;

    @FXML
    private AnchorPane anchorPaneMain;

    @Inject
    public BoardOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.allLists = new HashMap();
        this.listMap = new HashMap();
        this.taskMap = new HashMap();
        this.board = mainCtrl.getCurrBoard();
    }

    public ServerUtils getServer() {
        return server;
    }

    /**
     * Initializer
     * Initializes the objects in the scene
     */
    @FXML
    public void initialize() {
        ObservableList children = listContainer.getChildren();
        sampleGroup = (Group) children.get(0);
        ListsSetup();
        // Sets ScrollPane size, so it's slightly bigger than AnchorPane
        scrollPaneMain.setPrefSize(anchorPaneMain.getPrefWidth() + 10, anchorPaneMain.getPrefHeight() + 20);

        //initializes the default delete taskListsButton
        setDeleteAction(deleteTaskListsButton, listName1.getText(),taskList1);
        hoverOverDeleteButton(deleteTaskListsButton);
        //initializes the addTask button
        taskList1.setOnMouseClicked(e -> taskOperations(taskList1));
        addTaskButton(taskList1);

//         gets board from the database, or creates one if it doesnt exist
//        board = server.findBoard(boardKey);
//        if (board == null)
//            board = server.createBoard(new CreateBoardModel(boardKey, "a", 0));
//        refresh(board);

        // connects to /topic/boards
        server.subscribe("/topic/boards", Board.class, b -> Platform.runLater(() -> this.refresh(b)));
    }

    /**
     * Updates the board to show the changes that have been made(Experimental)
     */
    public void refresh(Board board) {

        // sets current board to board
        this.board = board;

        // removes all lists
        listContainer.getChildren().clear();

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
    public ListView<HBox> createTaskList() {
        TaskList taskList = board.createTaskList();
        ListView<HBox> list = addTaskList(taskList);
        server.updateBoard(board); // updates server
        return list;
    }

    /**
     * This eventHandler is waiting for the addButton to be clicked, after that creates
     * new Group of TextField, ScrollPane and a Deletion Button - new taskList
     */
    public ListView<HBox> addTaskList(TaskList taskList) {
        TextField sampleText = (TextField) sampleGroup.getChildren().get(0);
        ScrollPane samplePane = (ScrollPane) sampleGroup.getChildren().get(1);
        ListView<HBox> sampleList = (ListView<HBox>) samplePane.getContent();

        ObservableList<Node> children = listContainer.getChildren();

        ListView<HBox> listView = new ListView<>();
        listView.setPrefSize(sampleList.getPrefWidth(), sampleList.getPrefHeight());
        listView.setFixedCellSize(35);
        listView.setOnMouseClicked(e -> {
            taskOperations(listView);
        });

        ScrollPane scrollPane = new ScrollPane(listView);

        TextField textField = new TextField();
        textField.setOnInputMethodTextChanged(e ->{
            taskList.setTitle(textField.getText());
        });
        //create deleteTaskListsButton
        Button deleteTaskListsButton = new Button("x");

        setDeleteAction(deleteTaskListsButton, textField.getText(),listView);
        hoverOverDeleteButton(deleteTaskListsButton);

        deleteTaskListsButton.setLayoutX(191);
        deleteTaskListsButton.setLayoutY(0);
        deleteTaskListsButton.setPrefSize(25, 25);

        addTaskButton(listView);

        textField.setPrefSize(sampleText.getPrefWidth(), sampleText.getPrefHeight());
        textField.setLayoutX(0);
        textField.setLayoutY(0);
        textField.setText(taskList.getTitle());
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
        listMap.put(listView, taskList);
        return listView;
    }

    /**
     * Method for adding a TaskButton, used when creating a taskList, it creates new tasks
     * @param listView
     */
    public void addTaskButton(ListView<HBox> listView){
        Button addTaskButton = new Button("+");
        addTaskButton.setPadding(new Insets(2, 80, 2, 80));
        HBox box = new HBox(addTaskButton);
        listView.getItems().add(box);
        addTaskButton.setOnAction(e -> {
            createTask(listView);
        });
    }

    /**
     * A method to delete taskLists, and for pop-up asking for confirmation
     * @param button
     * @param taskListName
     */
    public void setDeleteAction(Button button, String taskListName, ListView<HBox> list){
        button.setOnAction(e -> {
            Group parentGroup = (Group) button.getParent();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation Dialog");
            alert.setHeaderText("Delete TaskList");
            alert.setContentText("Are you sure you want to delete '"+taskListName+"'?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                listContainer.getChildren().remove(parentGroup); // remove parent group from HBox
                TaskList list1 = listMap.get(list);
                board.removeTaskList(list1);
                server.updateBoard(board); // updates server
            }
        });
    }

    /**A method to turn the deleteTasksLists button into pink when mouse is hovering over it
     *
     * @param deleteTaskListsButton
     */
    public void hoverOverDeleteButton(Button deleteTaskListsButton){
        deleteTaskListsButton.setOnMouseEntered(e -> {
            deleteTaskListsButton.setStyle("-fx-background-color: pink;");
        });

        deleteTaskListsButton.setOnMouseExited(e -> {
            deleteTaskListsButton.setStyle(null);
        });
    }

    /**
     * Creates a task and puts it in the first list
     * A task contains a label and three buttons for task operations
     */
    public HBox createTask(ListView<HBox> list) {
        return createTask(inputTaskName(), list);
    }

    public HBox createTask(String name,ListView<HBox> list) {
        Task task1 = listMap.get(list).createTask();
        task1.setTitle(name);
        HBox task = addTask(name, list,task1);
        server.updateBoard(board); // updates server
        return task;
    }
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
        box.setHgrow(task, Priority.NEVER);
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
        String url = getClass().getClassLoader().getResource(path.replace("\\", "/")).toString();
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
        server.updateBoard(board); // updates server
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
        server.updateBoard(board); // updates server
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
                Task task1 = listMap.get(list).createTask();
                task1.setTitle(db.getString());
                success = true;
                db.clear();
            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    public void dragDetectHandler(HBox box,Label task,ListView<HBox> list) {
        ObservableList children = anchorPaneMain.getChildren();
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

