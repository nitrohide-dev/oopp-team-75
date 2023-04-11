package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

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

	@Inject
	public TaskOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
		this.mainCtrl = mainCtrl;
		this.server = server;
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
	 * @param task - the task which is showed
	 */
	public void load(Task task) {
		taskName.setText(task.getTitle());
		description.setText(task.getDesc());
		resetFields();
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
			load(this.mainCtrl.getCurrTask());
			//there should be some method for refreshing the scene for everyone here, maybe with long polling
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
			load(this.mainCtrl.getCurrTask());
			//there should be some method for refreshing the scene for everyone here, maybe with long polling
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
			load(this.mainCtrl.getCurrTask());
		}
	}

}
