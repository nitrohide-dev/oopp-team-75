package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javax.inject.Inject;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.nio.file.Path;

public class TagOverviewCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    @FXML
    private Button exitButton;
    @FXML
    private ListView<HBox> tagList;
    @Inject
    public TagOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }


    public void load(Board board){

        tagList.getItems().clear();
        String path = Path.of("", "client", "images", "cancel.png").toString();
        Button removeButton = buttonBuilder(path);
        Label tag1Label = new Label("tag 1");
        tag1Label.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        HBox box1 = new HBox();
        box1.getChildren().addAll(tag1Label, removeButton);
        box1.setAlignment(Pos.CENTER);
        Label tag2Label = new Label("tag 2");
        tag2Label.setBackground(new Background(new BackgroundFill(Color.BLUE,null,null)));
        HBox box2 = new HBox();
        box2.getChildren().addAll(tag2Label, removeButton);
        box2.setAlignment(Pos.CENTER);
        Label tag3Label = new Label("tag 3");
        tag3Label.setBackground(new Background(new BackgroundFill(Color.YELLOW,null,null)));
        HBox box3 = new HBox();
        box3.getChildren().addAll(tag3Label, removeButton);
        box3.setAlignment(Pos.CENTER);
        tagList.getItems().add(box1);
        tagList.getItems().add(box2);
        tagList.getItems().add(box3);
        addTagButton(tagList);
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
    public void createTag(){

    }
    public void removeTag(){

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
