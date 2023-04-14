package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.SubTask;
import commons.Tag;
import commons.Task;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class TaskOverviewCtrl {

	private final MainCtrl mainCtrl;
	private final ServerUtils server;

	@FXML
	private Button cancelName;
	@FXML
	private Button confirmName;
	@FXML
	private Label taskName;
	@FXML
	private TextArea description;
	@FXML
	private Button editName;
	@FXML
	private Button confirmDesc;
	@FXML
	private Button cancelDesc;
	@FXML
	private TextField newName;
	@FXML
	private ListView<HBox> taskList;
	@FXML
	private ListView<HBox> currTags;
	@FXML
	private ListView<HBox> availableTags;

	private Map<HBox, Long> taskMap;
	private Map<HBox, Long> currTagsMap;


	@Inject
	public TaskOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
		this.mainCtrl = mainCtrl;
		this.server = server;
		this.taskMap = new HashMap<>();
		this.currTagsMap = new HashMap<>();
	}

	@FXML
	public void initialize() {
		importPicture(this.editName, Path.of("", "client", "images", "pencil.png").toString());
		importPicture(this.cancelName, Path.of("", "client", "images", "close.png").toString());
		importPicture(this.confirmName, Path.of("", "client", "images", "check-mark-black-outline.png").toString());
		importPicture(this.cancelDesc, Path.of("", "client", "images", "close.png").toString());
		importPicture(this.confirmDesc, Path.of("", "client", "images", "check-mark-black-outline.png").toString());
	}

	/**
	 * sets the fields dependent on the task and restarts the other objects to default
	 */
	public void load() {
		Task task = mainCtrl.getCurrTask();
		Board board = mainCtrl.getCurrBoard();
		taskMap.clear();
		currTagsMap.clear();
		taskName.setText(task.getTitle());
		description.setText(task.getDesc());
		resetFields();
		initializeCurrTags(task.getTags());
		initializeRestTags(task.getTags(), board.getTags());
	}

	/**
	 * Resets the buttons and the fields to their default values
	 */
	private void resetFields() {
		taskName.setVisible(true);
		taskName.setDisable(false);
		newName.setVisible(false);
		newName.setDisable(true);
		newName.clear();
		this.editName.setVisible(true);
		this.editName.setDisable(false);
		this.cancelName.setDisable(true);
		this.cancelName.setVisible(false);
		this.cancelDesc.setDisable(true);
		this.cancelDesc.setVisible(false);
		this.confirmName.setDisable(true);
		this.confirmName.setVisible(false);
		this.confirmDesc.setDisable(true);
		this.confirmDesc.setVisible(false);
	}


	/**
	 * Connects to the server for automatic refreshing.
	 */
	public void connect() {
		initializeSubTasks(mainCtrl.getCurrTask().getSubtasks());
		server.subscribe("/topic/boards", Board.class, b -> Platform.runLater(() -> this.refresh(b)));
		server.subTaskSubscribe( mainCtrl.getCurrTask().getId(),
				b -> Platform.runLater(() -> this.initializeSubTasks(b)));
	}
	public void unsubscribe(){
		server.subTaskUnsubscribe();
	}

	/**
	 * Updates the board to a new board, and regenerates the boardOverview,
	 * only if the new boards key is equal to the previous boards key (i.e.
	 * only new versions of the same board are accepted).
	 * @param board the board to refresh to.
	 */
	public void refresh(Board board) {
		if(mainCtrl.getCurrBoard().getKey().equals(board.getKey())) {
			mainCtrl.setCurrBoard(board);
			mainCtrl.setCurrTask(server.getTask(mainCtrl.getCurrTask().getId()));
			load();
		}
	}

	private void initializeSubTasks(List<SubTask> subtasks) {
		this.taskList.getItems().clear();
		List<HBox> tasks = new ArrayList<>();
		for (int i=0;i<subtasks.size();i++) {
			SubTask task = subtasks.get(i);
			HBox box = taskHolder(task,i);
			tasks.add(box);
			this.taskMap.put(box, task.getId());
		}
		this.taskList.getItems().addAll(tasks);
	}

	private void initializeCurrTags(Set<Tag> tags) {
		this.currTags.getItems().clear();
		List<HBox> currTags = new ArrayList<>();
		for (Tag tag : tags) {
			HBox box = tagBox(tag);
			currTags.add(box);
			this.currTagsMap.put(box, tag.getId());
		}
		this.currTags.getItems().addAll(currTags);
	}

	private HBox tagBox(Tag tag) {
		if (tag == null) return null;
		Label tagName = new Label(tag.getTitle());
		tagName.setPrefSize(80, 25);
		String color = tag.getColor();
		int red  = Integer.parseInt(color.substring(0, 2), 16);
		int blue = Integer.parseInt(color.substring(2, 4), 16);
		int green = Integer.parseInt(color.substring(4, 6), 16);
		tagName.setBackground(new Background(new BackgroundFill(Color.rgb(red, blue, green), null, null)));
		tagName.setPadding(new Insets(5,1,5,2));
		String path = Path.of("", "client", "images", "cancel.png").toString();
		Button removeButton = new Button();
		importPicture(removeButton, path);
		HBox box = new HBox(tagName, removeButton);
		removeButton.setOnAction(e -> {
			this.mainCtrl.removeTag(tag);
		});
		return box;
	}

	private void initializeRestTags(Set<Tag> tags, Set<Tag> boardTags) {
		this.availableTags.getItems().clear();
		List<HBox> restTags = new ArrayList<>();
		for (Tag tag : boardTags) {
			if (!tags.contains(tag)) {
				HBox box = restTagBox(tag);
				restTags.add(box);
			}
		}
		this.availableTags.getItems().addAll(restTags);
	}

	private HBox restTagBox(Tag tag) {
		if (tag == null) return null;
		Label tagName = new Label(tag.getTitle());
		tagName.setPrefSize(80, 25);
		String color = tag.getColor();
		int red  = Integer.parseInt(color.substring(0, 2), 16);
		int blue = Integer.parseInt(color.substring(2, 4), 16);
		int green = Integer.parseInt(color.substring(4, 6), 16);
		tagName.setBackground(new Background(new BackgroundFill(Color.rgb(red, blue, green), null, null)));
		tagName.setPadding(new Insets(5,1,5,2));
		Button addButton = new Button("+");
		addButton.getStyleClass().add("header-btn");
		HBox box = new HBox(tagName, addButton);
		addButton.setOnAction(e -> {
			this.mainCtrl.addTag(tag);
		});
		return box;
	}


	private HBox taskHolder(SubTask task,int order) {
		if (task == null) return null;
		CheckBox check = new CheckBox();
		check.setSelected(task.getChecked());
		check.setOnAction(e -> {
			mainCtrl.checkSubTask(task);
		});
		check.setPadding(new Insets(4, 1, 4, 2));
		Label subTaskName = new Label(task.getTitle());
		subTaskName.setPrefSize(100, 25);
		subTaskName.setPadding(new Insets(5,1,5,2));
		String path = Path.of("", "client", "images", "cancel.png").toString();
		Button removeButton = new Button();
		importPicture(removeButton, path);
		path = Path.of("", "client", "images", "up.png").toString();
		Button upButton = new Button();
		importPicture(upButton, path);
		path = Path.of("", "client", "images", "down.png").toString();
		Button downButton = new Button();
		importPicture(downButton, path);
		HBox box = new HBox(check, subTaskName,upButton, downButton, removeButton);
		removeButton.setDisable(false);
		removeButton.setOnAction(e -> mainCtrl.deleteSubTask(taskMap.get(box)));
		upButton.setDisable(false);
		upButton.setOnAction(e -> server.moveSubTaskUp(order,task.getId()));
		downButton.setDisable(false);
		downButton.setOnAction(e -> server.moveSubTaskDown(order,task.getId()));
		return box;
	}


	/**
	 * Shows a textField where the user can input a new name for the task
	 */
	public void editName() {
		resetFields();
		this.editName.setVisible(false);
		this.editName.setDisable(true);
		this.confirmName.setVisible(true);
		this.confirmName.setDisable(false);
		this.cancelName.setVisible(true);
		this.cancelName.setDisable(false);
		this.taskName.setVisible(false);
		this.newName.setVisible(true);
		this.newName.setDisable(false);
		this.newName.setText(this.taskName.getText());
		this.newName.requestFocus();
	}

	/**
	 * Puts a picture in a button
	 * @param button - the button
	 * @param path - the path of the picture
	 */
	private void importPicture(Button button, String path) {
		String url = Objects.requireNonNull(getClass().getClassLoader().getResource(path.replace("\\", "/"))).toString();
		Image image = new Image(url);
		ImageView picture = new ImageView(image);
		picture.setFitHeight(18);
		picture.setFitWidth(18);
		button.setBackground(null);
		button.setGraphic(picture);
	}

	/**
	 * Allows the user to change the description of the task
	 */
	public void editDescription() {
		resetFields();
		this.confirmDesc.setVisible(true);
		this.confirmDesc.setDisable(false);
		this.cancelDesc.setVisible(true);
		this.cancelDesc.setDisable(false);
	}

	/**
	 * Shows a popup that asks the user if he wants to change the task's name
	 */
	public void confirmNameChange() {
		Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
		confirm.setHeaderText("Confirm change");
		confirm.setTitle("Save changes");
		confirm.setContentText("Are you sure you want to change the task's name?");
		Optional<ButtonType> result = confirm.showAndWait();
		((Button) confirm.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
		((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
		if (result.isPresent() && result.get() == ButtonType.OK) {
			mainCtrl.renameTask(this.newName.getText());
		}
	}

	/**
	 * Shows a popup that asks the user if he wants to change the task's description
	 */
	public void confirmDescChange() {
		Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
		confirm.setHeaderText("Confirm change");
		confirm.setTitle("Save changes");
		confirm.setContentText("Are you sure you want to change the task's description?");
		((Button) confirm.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
		((Button) confirm.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
		Optional<ButtonType> result = confirm.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			mainCtrl.changeTaskDesc(this.description.getText());
		}
	}

	/**
	 * Shows a popup that asks the user if he wants to cancel the change
	 */
	public void cancelChange() {
		Alert cancel = new Alert(Alert.AlertType.CONFIRMATION);
		cancel.setHeaderText("Cancel change");
		cancel.setTitle("Delete changes");
		cancel.setContentText("Are you sure you want to delete the changes that you made?");
		((Button) cancel.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
		((Button) cancel.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
		Optional<ButtonType> result = cancel.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK){
			load();
		}
	}

	public void createSubTask() {
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
		String path = Path.of("", "client", "images", "Logo.png").toString();
		Stage stage = (Stage) input.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(path));
		((Button) input.getDialogPane().lookupButton(ButtonType.OK)).setOnAction(e -> {
			mainCtrl.createSubTask(input.getEditor().getText());
		});
		input.showAndWait();
	}


}
