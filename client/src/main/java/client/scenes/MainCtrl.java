/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import commons.Board;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Objects;

public class MainCtrl {

    private Stage primaryStage;
    private LandingPageCtrl landingCtrl;
    private Scene landing;
    private BoardOverviewCtrl boardOverviewCtrl;
    private Scene boardOverview;

    private Board currBoard;

    public void initialize(Stage primaryStage, Pair<LandingPageCtrl, Parent> landing,
//                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<BoardOverviewCtrl, Parent> boardOverview) {
        this.primaryStage = primaryStage;
        this.landingCtrl = landing.getKey();
        this.landing = new Scene(landing.getValue());
//        this.overviewCtrl = overview.getKey();
//        this.overview = new Scene(overview.getValue());
//
//        this.addCtrl = add.getKey();
//        this.add = new Scene(add.getValue());

        this.boardOverviewCtrl = boardOverview.getKey();
        this.boardOverview = new Scene(boardOverview.getValue());

        showLanding();
        primaryStage.show();
    }

    public Board getCurrBoard() {
        return currBoard;
    }

    public void showLanding(){
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        landingCtrl.changeImageUrl();
        primaryStage.setTitle("Welcome to Talio!");
        primaryStage.setScene(landing);
        landing.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/styles.css")).toExternalForm());
        landingCtrl.refresh();
        //do we need this one? I put one here as reminder,
        //but if the database sync is implemented I don't think this one is not useful
    }

    public void showBoard(Board board) {
        currBoard = board;
        primaryStage.setTitle("Board: Your Board");
        primaryStage.setMaximized(true);
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(550);
        //this fixes a bug where the maximized window will be opened in pref size.
        //but it causes a bug where the window is not properly set, so the buttons on the right side are not visible
        //TODO fix this bug
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        boardOverviewCtrl.changeImageUrl();
        primaryStage.setScene(boardOverview);
    }
}