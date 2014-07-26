package ru.baikal.dc.xpector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestApp extends Application {
    public static void main(String[] args) {
        Application.launch(TestApp.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(
                "item 1",
                "item 2",
                "Ещё один item");
        comboBox.getSelectionModel().selectFirst();

        final Label two = new Label("two");
        two.setStyle("-fx-opacity: 0.3");
        final HBox hBox = new HBox(
                new Label("one"),
                two,
                new Label("three")
        );
        hBox.setSpacing(12);
        final VBox vBox = new VBox(
                comboBox,
                hBox,
                new Button("button"),
                new CheckBox("checkbox")
        );

        vBox.setSpacing(8.0);
        vBox.setMinSize(200, 200);
        primaryStage.setScene(new Scene(vBox));
        primaryStage.getScene().getStylesheets().addAll("app.css");
        primaryStage.setX(100);
        primaryStage.setY(100);
        primaryStage.setTitle("JavaFX app");
        primaryStage.show();

        final Inspector inspector = new Inspector(primaryStage.getScene().getRoot());
        inspector.setWidth(1200);
        inspector.setHeight(1000);
        inspector.show();
    }
}
