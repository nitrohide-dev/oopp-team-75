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
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javax.inject.Inject;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

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

    public void connect() {
        server.subscribe("/topic/boards", Board.class, b -> Platform.runLater(() -> this.refresh(b)));
    }
    public void refresh(Board board) {
        if(mainCtrl.getCurrBoard().getKey().equals(board.getKey())) {
            mainCtrl.setCurrBoard(board);
            load(board);
        }
    }
    public void load(Board board){

        tagList.getItems().clear();
        addTagButton(tagList);
        Iterator<Tag> iterator = board.getTags().iterator();
        while(iterator.hasNext())
        {
            addTag(iterator.next());
        }
    }
    public void addTagButton(ListView<HBox> list){
        Button addTagButton = new Button("ADD TAG");
        addTagButton.setId("addButton");
        addTagButton.setAlignment(Pos.CENTER);
        addTagButton.setShape(new Ellipse(150, 25));
        addTagButton.setMinSize(150, 25);
        HBox box = new HBox(addTagButton);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(4, 16, 4, 16));
        list.getItems().add(box);
        addTagButton.setOnAction(e -> createTag());
    }
    public void addTag(Tag tag){
        tagList.getItems().remove(tagList.getItems().get(tagList.getItems().size()-1));
        String path = Path.of("", "client", "images", "cancel.png").toString();
        Button removeButton = buttonBuilder(path);
        path = Path.of("", "client", "images", "pencil.png").toString();
        removeButton.setOnAction(e -> server.deleteTag(Long.toString(tag.getId()),mainCtrl.getCurrBoard().getKey()));
        Button editButton = buttonBuilder(path);
        editButton.setOnAction(e-> renameTag(tag.getId(),mainCtrl.getCurrBoard().getKey()));
        Label label = new Label(tag.getTitle());
        label.setBackground(new Background(new BackgroundFill(Color.rgb(55,205,155,0.7),null,null)));
        HBox box = new HBox();
        box.getChildren().addAll(label, editButton, removeButton);
        box.setAlignment(Pos.CENTER);
        tagMap.put(box,tag.getId());
        tagList.getItems().add(box);
        addTagButton(tagList);
    }
    public void createTag(){
        String name = tagNameSetter();
        server.createTag(mainCtrl.getCurrBoard().getKey(),name);
    }
    public void renameTag(Long id, String key){
        String newName = tagNameSetter();
        server.renameTag(Long.toString(id),key,newName);
    }
    public String tagNameSetter(){
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
