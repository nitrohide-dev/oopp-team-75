package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Task;
import commons.TaskList;
import commons.models.TaskMoveModel;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class BoardOverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML public Group group;
    @FXML public HBox header;
    @FXML public ScrollPane sPaneListView;

    @FXML private ListView<HBox> taskList1;

    @FXML private TextField listName1;

    @FXML private Button deleteTaskListsButton;

    @FXML private HBox listContainer;

    private Group sampleGroup;

    private Map<ListView, String> allLists; // Stores all task lists
    private final Map<ListView, Long> listMap; // Stores all task lists
    private final Map<HBox, Long> taskMap; // Stores all tasks
    private final Map<Long, Integer> taskOrderMap;

    @FXML
    private ScrollPane scrollPaneMain;

    @FXML private AnchorPane anchorPaneMain;
    @FXML private ImageView logo;
    @FXML private ImageView exit;
    @FXML private ImageView menu;
    @FXML private ImageView tags;
    @FXML private Label titleLabel;

    @FXML
    private BorderPane borderPane;
    private UserMenuCtrl usermenuCtrl;
    @Getter
    @Setter
    private boolean adminPresence=false;

    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.allLists = new HashMap<>();
        this.listMap = new HashMap<>();
        this.taskMap = new HashMap<>();
        this.taskOrderMap = new HashMap<>();
    }

    /**
     * Initializer
     * Initializes the initial objects in the scene
     */
    @FXML
    public void initialize() {
        logo.setImage(new Image(Path.of("", "client", "images", "Logo.png").toString()));
        exit.setImage(new Image(Path.of("", "client", "images", "ExitButton.png").toString()));
        menu.setImage(new Image(Path.of("", "client", "images", "Dots.png").toString()));
        tags.setImage(new Image(Path.of("", "client", "images", "tag.png").toString()));
        ObservableList<Node> children = listContainer.getChildren();
        sampleGroup = (Group) children.get(0);
        // Sets ScrollPane size, so it's slightly bigger than AnchorPane
        scrollPaneMain.setPrefSize(anchorPaneMain.getPrefWidth() + 10, anchorPaneMain.getPrefHeight() + 20);
        borderPane.setOnMouseClicked(null);
        titleLabel.setOnMouseClicked(e -> renameBoard());
        titleLabel.setCursor(Cursor.HAND);
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
        if(getBoard().getKey().equals(board.getKey())) {
            mainCtrl.setCurrBoard(board);
            load(board);
        }
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

        // sets board title
        titleLabel.setText(board.getTitle());

        // creates new lists
        List<TaskList> listOfLists = board.getTaskLists();
        if (listOfLists.size() == 0)
            return;
        for (TaskList taskList : listOfLists) {
            ListView<HBox> ourList = addTaskList(taskList);
            dragOverHandler(ourList);
            dragDroppedHandler(ourList);
            List<Task> listOfTasks = taskList.getTasks();
            for (int i = 0; i < listOfTasks.size(); i++) {
                Task task = listOfTasks.get(i);
                addTask(task.getTitle(), ourList, task);
                taskOrderMap.put(task.getId(),i);
            }
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
     * creates the key copy button
     * clicking on it will copy the board key to the user's clipboard
     * @return the key copy button
     */
    public Button createCopyKeyButton(){
        Button keyCopyButton = new Button();
        keyCopyButton.setText("Copy Board Key");
        keyCopyButton.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(getBoard().getKey());
            clipboard.setContent(clipboardContent);
        });
        keyCopyButton.getStyleClass().add("smButton");
        return keyCopyButton;
    }

    /**
     * creates the board rename button
     * clicking on it will show a popup that asks you for the new name of the board
     * @return the key rename button
     */
    public Button createRenameBoardButton(){
        Button boardRenameButton = new Button();
        boardRenameButton.setText("Rename board");
        boardRenameButton.setOnAction(e -> renameBoard());
        boardRenameButton.getStyleClass().add("smButton");
        return boardRenameButton;
    }

    public void renameBoard() {
        String name = inputBoardName();
        if (name == null || name.equals("")) return;
        getBoard().setTitle(name);
        server.updateBoard(getBoard());
    }

    /**
     * creates the board deletion button
     * clicking on it will delete the current board from the server and take the user back to the main menu
     * when clicked on, it will show a confirmation popup first to prevent accidental deletion
     * @return the key deletion button
     */
    public Button createBoardDeletionButton(){
        Button boardDeletionButton = new Button();
        boardDeletionButton.setText("Delete Board");
        boardDeletionButton.setOnAction(e->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete Confirmation Dialog");
            alert.setHeaderText("Delete Board");
            alert.setContentText("Are you sure you want to delete this board?");
            //add css to dialog pane
            alert.getDialogPane().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
            //make preferred size bigger
            alert.getDialogPane().setPrefSize(400, 200);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                server.deleteBoard(getBoard().getKey());
                exit();
            }
        });
        boardDeletionButton.getStyleClass().add("smButton");
        return boardDeletionButton;
    }

     /**
     * creates the menu bar and appends the boards name and the buttons with functionalities to it
     * the menu is added to the right side of the scene
     */
    public void addMenu(){
        if(borderPane.getRight() != null) {
            borderPane.setRight(null);
            return;
        }
        VBox menuBar = new VBox();
        menuBar.prefHeightProperty().bind(borderPane.heightProperty());
        menuBar.setTranslateX(0);
        menuBar.getChildren().add(new Label("key: "+ getBoard().getKey()));
        menuBar.setId("sideMenu");
        menuBar.setOnMouseClicked(null);
        menuBar.getChildren().add(createCopyKeyButton());;
        menuBar.getChildren().add(createRenameBoardButton());
        menuBar.getChildren().add(createBoardDeletionButton());
        menuBar.setSpacing(10);
        menuBar.setAlignment(Pos.TOP_LEFT);
        menuBar.setFillWidth(true);
        menuBar.setPadding(new Insets(8));
//      TranslateTransition menuBarTranslation = new TranslateTransition(Duration.millis(400), menuBar);
//
//      menuBarTranslation.setFromX(772);
//      menuBarTranslation.setToX(622);
//
//      menuBar.setOnMouseEntered(e -> {
//          menuBarTranslation.setRate(1);
//          menuTranslation.play();
//      });
//        menuBar.setOnMouseExited(e -> {
//            menuBarTranslation.setRate(-1);
//          menuBarTranslation.play();
//      });
        borderPane.setRight(menuBar);
    }

    /**
     * Creates a taskList in the given board
     */
    public void createTaskList() {
        server.createList(getBoard().getKey());
    }

    /**
     * This eventHandler is waiting for the addButton to be clicked, after that creates
     * new Group of TextField, ScrollPane and a Deletion Button - new taskList
     * @param taskList a TaskList common object that is mapped with the created tasklist for backend-frontend communication
     */
    public ListView<HBox> addTaskList(TaskList taskList) {
        ScrollPane samplePane = (ScrollPane) sampleGroup.getChildren().get(1);
        ListView<HBox> sampleList = (ListView<HBox>) samplePane.getContent();
        TextField textField = new TextField(taskList.getTitle());
        textField.setId("listName1");
        textField.focusedProperty().addListener((obs,oldVal,newVal) -> {
            if(newVal == false)
            {
                server.renameList(taskList.getId(),textField.getText());
            }
        });
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

        listContainer.getChildren().add(newGroup);
        dragOverHandler(listView);
        dragDroppedHandler(listView);
        allLists.put(listView, textField.getText());
        listMap.put(listView,taskList.getId());
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
        textField.setPromptText("Name your list!");
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
        Button addTaskButton = new Button("ADD TASK");
        addTaskButton.setId("addButton");
        addTaskButton.setAlignment(Pos.CENTER);
        addTaskButton.setShape(new Ellipse(150, 25));
        addTaskButton.setMinSize(150, 25);
        HBox box = new HBox(addTaskButton);
        box.setPadding(new Insets(4, 16, 4, 16));
        listView.getItems().add(box);

        addTaskButton.setOnAction(e -> {
            TextField textField = new TextField();
            textField.setOnAction(actionEvent -> {
                // Submit the text when Enter is pressed
                String text = textField.getText();
                createTask(text,listView);

            });

            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    // If the text field loses focus, go back to the label
                    box.getChildren().clear();
                    box.getChildren().add(addTaskButton);
                }
            });
            box.getChildren().clear();
            box.getChildren().add(textField);
            textField.requestFocus();

        });


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
            alert.setContentText("Are you sure you want to delete this tasklist ?");
            //add css to dialog pane
            alert.getDialogPane().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
            //make preferred size bigger
            alert.getDialogPane().setPrefSize(400, 200);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                server.deleteList(listMap.get(list));
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
     * creates a task in the given list with the given name
     * @param name the name of the task to be created
     * @param list the list in which the task should be created
     */
    public void createTask(String name,ListView<HBox> list) {
        server.createTask(listMap.get(list),name);
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
        task.setPrefWidth(130);
        task.setPadding(new Insets(6, 1, 6, 1));
        String path = Path.of("", "client", "images", "cancel.png").toString();
        Button removeButton = buttonBuilder(path);
        path = Path.of("", "client", "images", "pencil.png").toString();
        Button editButton = buttonBuilder(path);

        HBox box = new HBox(task, editButton, removeButton);
        box.setSpacing(5);

        // just for testing - delete when fully implemented
      // addDescriptionIndicator(box);
      // addProgressIndicator(box,0.7);


        dragHandler(box,task,list);
        removeButton.setOnAction(e -> deleteTask(box));
        editButton.setOnAction(e -> System.out.println("holder"));
        box.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                viewTask(box); // changed view button for double click
            }
        });
        disableTaskButtons(box);
        HBox.setHgrow(task, Priority.NEVER);
        list.getItems().add(box);
        //Re-adds the button to the end of the list
        addTaskButton(list);
        taskMap.put(box,task1.getId());

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
        button.setPrefSize(20, 20);
        button.setBackground(null);
        button.setPadding(new Insets(6, 1, 6, 2));
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
                Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        //make preferred size bigger
        input.getDialogPane().setPrefSize(400, 200);
        //trying to add icon to dialog
        Label label = new Label();
        String path = Path.of("", "client", "images", "Logo.png").toString();
        Stage stage = (Stage) input.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(path));
        ((Button) input.getDialogPane().lookupButton(ButtonType.OK)).setOnAction(e -> {
            label.setText(input.getEditor().getText());
        });
        input.showAndWait();
        if(label.getText()==null)
            return "task name";
        return label.getText();

    }

    /**
     * popup that ask you to input a board name.
     * @return the input name
     */
    public String inputBoardName() {
        TextInputDialog input = new TextInputDialog("board name");
        input.setHeaderText("Board name");
        input.setContentText("Please enter a name for the board:");
        input.setTitle("Input Board Name");
        //add css to dialog pane
        input.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        //make preferred size bigger
        input.getDialogPane().setPrefSize(400, 200);
        //trying to add icon to dialog
        String path = Path.of("", "client", "images", "Logo.png").toString();
        Stage stage = (Stage) input.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(path));
        Label label = new Label();
        ((Button) input.getDialogPane().lookupButton(ButtonType.OK)).setOnAction(e -> {
            label.setText(input.getEditor().getText());
        });
        input.showAndWait();
        return label.getText();
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
        removeButton.setDisable(true);
        removeButton.setVisible(false);
        editButton.setDisable(true);
        editButton.setVisible(false);

    }

    /**
     * Enables and shows the buttons of a task
     * @param box the task box
     */
    private void enableTaskButtons(HBox box) {
        Button removeButton = (Button) box.getChildren().get(1);
        Button editButton = (Button) box.getChildren().get(2);
        removeButton.setDisable(false);
        removeButton.setVisible(true);
        editButton.setDisable(false);
        editButton.setVisible(true);

    }

    /**
     * The user can see detailed info about the task
     * @param task - a HBox, containing the task
     */
    public void viewTask(HBox task) {
        mainCtrl.showTaskOverview(taskMap.get(task));
    }

    /**
     * Deletes given task
     * @param task - a HBox, containing the task
     */
    public void deleteTask(HBox task) {
        server.deleteTask(taskMap.get(task));
    }

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
                TaskMoveModel model = new TaskMoveModel(Long.parseLong(db.getString()), listMap.get(list),Integer.MAX_VALUE);
                server.moveTask(model,getBoard().getKey());
                success = true;
                db.clear();
            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    /**
     * Handles the task's behaviour when it's being dragged AND when a task gets dropped onto it
     * @param box - the box that contains the task which behaviour is to be configured
     * @param task - the task label, containing its name
     * @param list - the list that contains the task
     */
    public void dragHandler(HBox box,Label task,ListView<HBox> list) {
        ObservableList<Node> children = anchorPaneMain.getChildren();
        box.setOnDragDetected(event -> {
            Dragboard db = box.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(Long.toString(taskMap.get(box)));
            db.setContent(content);
            db.setDragView(new Text(task.getText()).snapshot(null, null), event.getX(), event.getY());
            event.consume();
        });
        box.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                int order = taskOrderMap.get(taskMap.get(box))+1;
                TaskMoveModel model = new TaskMoveModel(Long.parseLong(db.getString()),listMap.get(list),order);
                server.moveTask(model,getBoard().getKey());
                success = true;
                db.clear();
            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    /**
        * sets the current scene to main menu
     */
    public void exit() {
        borderPane.setRight(null);
        mainCtrl.showUserMenu();
    }



    /**
     * adds and sets progress indicator to the task box - should be called when nested tasks are added
     *
     * @param box task box
     */
    public void addProgressIndicator(HBox box, double percentage){
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setProgress(percentage); // set for the respective parameter
        progressIndicator.getStylesheets()
                .add(getClass().getResource("styles.css").toExternalForm());
        Label l  = (Label) box.getChildren().get(0);
        l.setPrefWidth(l.getPrefWidth()-30);

        VBox vbox= new VBox(progressIndicator);
        vbox.setAlignment(Pos.CENTER);

        box.getChildren().add(vbox);

    }

    /**
     *
     * removes progress indicator if exists - to be called when no nested tasks are set
     * @param box task box
     */
    public void removeProgressIndicator(HBox box){
        for (Iterator<Node> it = box.getChildren().iterator(); it.hasNext(); ) {
            Node childNode = it.next();
            if (childNode instanceof VBox) {
                if(((VBox) childNode).getChildren().get(0) instanceof ProgressIndicator){
                    it.remove();
                    Label l  = (Label) box.getChildren().get(0);
                    l.setPrefWidth(l.getPrefWidth()+30);
                    break;
                }
            }
        }

    }

    /**
     *
     * adds description indicator to the task HBox
     * @param box task HBox
     */
    public void addDescriptionIndicator(HBox box){

        ImageView image = new ImageView(new Image(Path.of("",
                "client", "images", "description.png").toString()));
        image.setFitHeight(18);
        image.setFitWidth(18);
        VBox vbox= new VBox(image);
        vbox.setAlignment(Pos.CENTER);

        Label l  = (Label) box.getChildren().get(0);
        l.setPrefWidth(l.getPrefWidth()-25);

        box.getChildren().add(vbox);

    }

    /**
     *
     * removes the indicator if there's one
     * @param box task HBox
     */
    public void removeDescriptionIndicator(HBox box){
        for (Iterator<Node> it = box.getChildren().iterator(); it.hasNext(); ) {
            Node childNode = it.next();
            if (childNode instanceof VBox) {
                if(((VBox) childNode).getChildren().get(0) instanceof ImageView){
                    Label l  = (Label) box.getChildren().get(0);
                    l.setPrefWidth(l.getPrefWidth()+25);
                    it.remove();
                    break;
                }
            }
        }}

    /**
     * shows the tag list associated with the current board
     * this method is called by the user clicking on the tag button at the top of the board overview
     */
    public void viewTags() {
        mainCtrl.showTagOverview(getBoard().getKey());
    }

    public void setAdminPresence(boolean adminPresence) {
        this.adminPresence = adminPresence;
    }
}

