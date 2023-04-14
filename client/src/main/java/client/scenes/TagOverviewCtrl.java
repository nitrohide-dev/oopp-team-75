package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class TagOverviewCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    @FXML
    private Button exitButton;
    @FXML
    private ListView<HBox> tagList;
    private HashMap<HBox,Long> tagMap;
    @Inject
    public TagOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.tagMap = new HashMap<>();
    }

    /**
     * Connects to the server for automatic refreshing.
     * same method as in boardOverview
     */
    public void connect() {
        server.subscribe("/topic/boards", Board.class, b -> Platform.runLater(() -> this.refresh(b)));
    }
    /**
     * Updates the board to a new board, and regenerates the boardOverview
     * same method as in boardOverview
     */
    public void refresh(Board board) {
        if (mainCtrl.getCurrBoard().getKey().equals(board.getKey())) {
            mainCtrl.setCurrBoard(board);
            load(board);
        }
    }
    /**
     * (re-)loads all the tags in the tagList
     * @param board - the board in which the tags that are to be shown are stored
     */
    public void load(Board board) {

        tagList.getItems().clear();
        addTagButton();
        Iterator<Tag> iterator = board.getTags().iterator();
        while (iterator.hasNext())
        {
            addTag(iterator.next());
        }
    }
    /**
     * appends the "add tag" button to the end of the tag list
     */
    public void addTagButton() {
        Button addTagButton = new Button("ADD TAG");
        addTagButton.setId("addButton");
        addTagButton.setAlignment(Pos.CENTER);
        addTagButton.setShape(new Ellipse(150, 25));
        addTagButton.setMinSize(150, 25);
        HBox box = new HBox(addTagButton);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(4, 16, 4, 16));
        tagList.getItems().add(box);
        addTagButton.setOnAction(e -> createTag());
    }

    /**
     * adds a tag to the tag list
     * @param tag - the tag common object to be used for the added tag's properties
     */
    private void addTag(Tag tag) {
        tagList.getItems().remove(tagList.getItems().get(tagList.getItems().size() - 1));

        Ellipse ellipse = new Ellipse();
        ellipse.setRadiusX(8);
        ellipse.setRadiusY(8);
        ellipse.setFill(Paint.valueOf(tag.getColor()));

        Label label = new Label(tag.getTitle());

        Region region = new Region();
        region.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(region, Priority.ALWAYS);

        Button editButton = buttonBuilder(Path.of("", "client", "images", "pencil.png").toString());
        editButton.setOnAction(e -> renameTag(tag.getId(),mainCtrl.getCurrBoard().getKey()));

        Button removeButton = buttonBuilder(Path.of("", "client", "images", "cancel.png").toString());
        removeButton.setOnAction(e -> server.deleteTag(Long.toString(tag.getId()),mainCtrl.getCurrBoard().getKey()));

        HBox box = new HBox();
        box.setSpacing(10);
        box.getChildren().addAll(ellipse, label, region, editButton, removeButton);
        box.setAlignment(Pos.CENTER_LEFT);

        tagMap.put(box,tag.getId());
        tagList.getItems().add(box);
        addTagButton();
    }

    /**
     * creates a tag with a name given by the user in the database
     */
    public void createTag() {
        String name = tagNameSetter();
        server.createTag(mainCtrl.getCurrBoard().getKey(),name);
    }
    /**
     * renames a tag to a name given by the user in the database
     */
    public void renameTag(Long id, String key) {
        String newName = tagNameSetter();
        server.renameTag(Long.toString(id),key,newName);
    }
    /**
     * creates a popup for (re-)naming of the tag by the user
     */
    public String tagNameSetter() {
        TextInputDialog input = new TextInputDialog("tag name");
        input.setHeaderText("Tag name");
        input.setContentText("Please enter a name for the tag:");
        input.setTitle("Input tag name");
        input.getDialogPane().setPrefSize(400, 200);
        input.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        String path = Path.of("", "client", "images", "Logo.png").toString();
        Stage stage = (Stage) input.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(path));
        Label label = new Label();
        ((Button)
                input.getDialogPane().lookupButton(ButtonType.OK)).setOnAction(e -> {
                    label.setText(input.getEditor().getText());
                });
        input.showAndWait();
        return label.getText();
    }
    /**
     * creates a button with the given image and gives it appropriate properties
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

}
