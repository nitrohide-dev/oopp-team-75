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

import client.scenes.MainCtrl;
import client.scenes.LandingPageCtrl;
import client.scenes.BoardOverviewCtrl;
import client.scenes.UserMenuCtrl;
import client.scenes.AdminLoginCtrl;
import client.scenes.AdminOverviewCtrl;
import client.scenes.TaskOverviewCtrl;
import client.scenes.TagOverviewCtrl;
import client.scenes.PasswordChangeCtrl;

import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static MainCtrl mainCtrl = INJECTOR.getInstance(MainCtrl.class);


    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        var landing = FXML.load(LandingPageCtrl.class, "client", "scenes", "LandingPage.fxml");
        var board = FXML.load(BoardOverviewCtrl.class, "client", "scenes", "BoardOverview.fxml");
        var userMenu = FXML.load(UserMenuCtrl.class,"client","scenes","UserMenu.fxml");
        var adminOverview = FXML.load(AdminOverviewCtrl.class,"client","scenes","AdminOverview.fxml");
        var adminLogin = FXML.load(AdminLoginCtrl.class,"client","scenes","AdminLogin.fxml");
        var passwordChange = FXML.load(PasswordChangeCtrl.class,"client","scenes","PasswordChange.fxml");
        var taskOverview = FXML.load(TaskOverviewCtrl.class, "client", "scenes", "TaskOverview.fxml");
        var tagOverview = FXML.load(TagOverviewCtrl.class, "client", "scenes", "TagOverview.fxml");
        mainCtrl.initialize(primaryStage, landing, board, userMenu,
                                adminOverview, adminLogin, passwordChange, taskOverview,tagOverview);

    }
}