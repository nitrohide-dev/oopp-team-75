package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


/**
 * I added ListView everywhere with <String> attributes just for as a starter
 * Later it should have <Task>
 */
public class BoardPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Button addButton;

    @FXML
    private ScrollBar scrollBar;

    @FXML
    private HBox hBox;

    private Group sampleGroup;
    private int counter;
    private ObservableList<ListOfTasks> Lists = FXCollections.observableArrayList();

    @Inject
    public BoardPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initializer
     * First I am getting all the children of hBox (which are the 3 groups)
     * and then for each group I am getting its TextField and its ListField and adding them to a Lists array
     */
    @FXML
    public void initialize(){
        ObservableList children = hBox.getChildren();

        sampleGroup = (Group) children.get(0);

        for (Object child : children) {
            if (child instanceof Group) {
                Lists.add(new ListOfTasks(
                        (TextField) ((Group) child).getChildren().get(0),
                        (ListView<String>) ((Group) child).getChildren().get(1)));
            }
        }
        counter = 3;
        scroll();
    }

    /**Not the best implementation, will work on making the scroll bar appear only when needed and also
     * so I will make the scroll bar proportionally larger.
     *
     */
    public void scroll(){
        scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            double scrollWidth =  - Math.abs(hBox.getWidth()  - hBox.getScene().getWidth());
            hBox.setTranslateX(newValue.doubleValue() * scrollWidth);
        });
    }

    /**
     * This eventHandler is waiting for the addButton to be clicked, after that creates
     * new ListOfTask, which is made of a textField and a listView
     */
    public void handleAddButtonAction() {
        ObservableList children = hBox.getChildren();
        TextField sampleText = (TextField) sampleGroup.getChildren().get(0);
        ListView<String> sampleList = (ListView<String>) sampleGroup.getChildren().get(1);
        TextField textField = new TextField();
        ListView<String> listView = new ListView<>();

        textField.setPrefSize(sampleText.getPrefWidth(), sampleText.getPrefHeight());
        textField.setLayoutX(0);
        textField.setLayoutY(0);
        textField.setPromptText("Name your list!");
        listView.setPrefSize(sampleList.getPrefWidth(), sampleList.getPrefHeight());
        listView.setLayoutX(0);
        listView.setLayoutY(37);

        System.out.println(sampleList+" ----- "+sampleText);

        ListOfTasks newListOfTasks = new ListOfTasks(textField, listView);
        Lists.add(newListOfTasks);

        Group newGroup = new Group(textField, listView);
        newGroup.setLayoutX(sampleGroup.getLayoutX());
        newGroup.setLayoutY(sampleGroup.getLayoutY());
        newGroup.setTranslateX(sampleGroup.getTranslateX());
        newGroup.setTranslateY(sampleGroup.getTranslateY());

        children.add(newGroup);
    }

    /**
     * So this class I created in order to group together each of the TextFields with the ListView
     * in the new object "ListOfTasks"
     *
     * I know we have a class TaskList, and I can work after meeting with you guys or talking it through
     * on how should I change ListOfTasks or how should we change TaskList so it contains both ListView and
     * TextField.
     *

     */
    private class ListOfTasks {
        private TextField textField;
        private ListView<String> listView;

        public ListOfTasks() {
            textField = new TextField();
            listView = new ListView<String>();
        }

        public ListOfTasks(TextField textField, ListView<String> listView) {
            textField = new TextField();
            listView = new ListView<>();
        }

        public TextField getTextField() {
            return textField;
        }

        public ListView<String> getListView() {
            return listView;
        }


        public void setTextField(TextField textField) {
            this.textField = textField;
        }

        public void setListView(ListView<String> listView) {
            this.listView = listView;
        }

    }
}
