package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * I added ListView everywhere with <String> attributes just for as a starter
 * Later it should have <Task>
 */
public class BoardOverviewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ListView<HBox> taskList1;

    @FXML
    private ListView<HBox> taskList2;

    @FXML
    private ListView<HBox> taskList3;

    @FXML
    private TextField listName1;

    @FXML
    private TextField listName2;

    @FXML
    private TextField listName3;

    @FXML
    private HBox hBox;

    private Group sampleGroup;

    private Map<ListView, String> allLists; // Stores all task lists

    @FXML
    private ScrollPane scrollPaneMain;

    @FXML
    private AnchorPane anchorPaneMain;

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
    public void initialize() {
        ObservableList children = hBox.getChildren();
        sampleGroup = (Group) children.get(1);
        setListsNames();
        // Sets ScrollPane size, so it's slightly bigger than AnchorPane
        scrollPaneMain.setPrefSize(anchorPaneMain.getPrefWidth()+10,anchorPaneMain.getPrefHeight()+20);
    }

//Deleted Scrolling, implemented using ScrollPane
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
        ListView<Label> sampleList = (ListView<Label>) samplePane.getContent();
        TextField textField = new TextField();
        ListView<Label> listView = new ListView<>();
        listView.setPrefSize(sampleList.getPrefWidth(), sampleList.getPrefHeight());
        ScrollPane scrollPane = new ScrollPane(listView);

        textField.setPrefSize(sampleText.getPrefWidth(), sampleText.getPrefHeight());
        textField.setLayoutX(0);
        textField.setLayoutY(0);
        textField.setText("Name your list!");
        scrollPane.setPrefSize(samplePane.getPrefWidth(), samplePane.getPrefHeight());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setLayoutX(0);
        scrollPane.setLayoutY(60);

        System.out.println(samplePane + " ----- " + sampleText);

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
     * A task contains a label and three buttons for task operations
     */
    public void createTask() {
        String name = getTaskNamePopup("Task");
        //if (!server.addTask(name)) return;
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
        box.setOnDragDetected(e -> dragTask(e, task, box));
        removeButton.setOnAction(e -> deleteTask(box));
        editButton.setOnAction(e -> editTask(box));
        viewButton.setOnAction(e -> viewTask(box));
        disableButtons(box);
        box.setHgrow(task, Priority.NEVER);
        taskList1.getItems().add(box);
    }

    /**
     * Creates button for task operations
     *
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
     * popup that ask you to input the name of the thing that you want to create
     *
     * @param item - the type of the thing that you want to create
     * @return the input name
     */
    public String getTaskNamePopup(String item) {
        TextInputDialog input = new TextInputDialog(item + " name");
        input.setHeaderText(item);
        input.setContentText("Please enter a name for the " + item.toLowerCase(Locale.ROOT) + ".");
        input.setTitle("Name Input Dialog");
        input.showAndWait();
        return input.getEditor().getText();
    }

    /**
     * When any of the tasks is clicked it gives the user options to view, edit or remove it
     */
    public void taskOperations() {
        HBox box = taskList1.getSelectionModel().getSelectedItem();
        if (box == null) return;
        resetOptionButtons();
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
            for (HBox box : list.getItems()) {
                disableButtons(box);
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
    public void deleteTask(HBox task) {
        //if (!server.removeTask(task.getText())) return;
        taskList1.getItems().remove(task);
    }

    /**
     * method that moves a task from one list to another
     *
     * @param fromList - the list containing the taskD
     * @param toList   - the list in which we want to put the task
     * @param task     the task to be moved
     */
    public void moveTask(ListView fromList, ListView toList, HBox task) {
        String list1 = allLists.get(fromList);
        String list2 = allLists.get(toList);
        if (list1 == null || list2 == null) return;
        if (list1.equals(list2)) return;
        if (server.moveTask(mainCtrl.getCurrBoard(), list1, list2, task)) {
            fromList.getItems().remove(task);
            toList.getItems().add(task);
        }
    }

    public void dragTask(MouseEvent n, Label task, HBox box) {
        ObservableList children = anchorPaneMain.getChildren();
        Text text = new Text();
        text.setText(task.getText());
        text.setOpacity((0.4));
        text.setX(n.getX() + 5);
        text.setY(n.getY() + 115);
        task.setOnMouseDragged(e -> {
            text.setX(e.getX() + 5);
            text.setY(e.getY() + 115);
        });
        text.setOnMouseExited(e -> {
            children.remove(text);
            determineDragAction(box);
        });
        children.add(text);
    }

    public void determineDragAction(HBox box) {
        if (taskList1.isHover()) {
            moveTask(taskList1, taskList1, box);

        } else if (taskList2.isHover()) {
            moveTask(taskList1, taskList2, box);

        } else if (taskList3.isHover()) {
            moveTask(taskList1, taskList3, box);

        } else {
            return;
        }
    }
}

