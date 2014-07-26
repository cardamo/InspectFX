package ru.baikal.dc.xpector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestApp extends Application {
    public static void main(String[] args) {
        Application.launch(TestApp.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new VBox(
            new Label("one"),
            new Label("two"),
            new Label("three")
        )));
        primaryStage.show();

        new Inspector(primaryStage.getScene().getRoot()).show();
    }
}
