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
package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import server.api.controllers.BoardController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = { "commons", "server" })
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        try {
            BoardController.readPassword("password",System.getProperty("user.dir")
                    + "/server/src/main/java/server/api/configs/pwd.txt");
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}