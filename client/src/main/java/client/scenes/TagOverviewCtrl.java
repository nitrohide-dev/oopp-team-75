package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javax.inject.Inject;
import javafx.scene.paint.Color;

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
        Label tag1Label = new Label("tag 1");
        tag1Label.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        HBox box1 = new HBox(tag1Label);
        Label tag2Label = new Label("tag 2");
        tag2Label.setBackground(new Background(new BackgroundFill(Color.BLUE,null,null)));
        HBox box2 = new HBox(tag2Label);
        Label tag3Label = new Label("tag 3");
        tag3Label.setBackground(new Background(new BackgroundFill(Color.YELLOW,null,null)));
        HBox box3 = new HBox(tag3Label);
        tagList.getItems().add(box1);
        tagList.getItems().add(box2);
        tagList.getItems().add(box3);
    }
}
