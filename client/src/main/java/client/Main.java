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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static MainCtrl mainCtrl;

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

//        var overview = FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "QuoteOverview.fxml");
//        var add = FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml");
        var landing = FXML.load(LandingPageCtrl.class, "client", "scenes", "LandingPage.fxml");
        var board = FXML.load(BoardOverviewCtrl.class, "client", "scenes", "BoardOverview.fxml");
        var userMenu = FXML.load(UserMenuCtrl.class,"client","scenes","UserMenu.fxml");
        var boardCreate = FXML.load(BoardCreateCtrl.class,"client","scenes","BoardCreate.fxml");
        mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, landing, board,userMenu,boardCreate);


    }
@Override
    public void stop(){
        try {
            mainCtrl.getUser().writeToCsv();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}