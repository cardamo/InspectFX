package ru.baikal.dc.xpector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
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

        Label two = new Label("two");
        two.setStyle("-fx-opacity: 0.3");
        HBox hBox = new HBox(
                new Label("one"),
                two,
                new Label("three")
        );
        hBox.setSpacing(12);

        TextField textField = new TextField();

        Button button = new Button("Button");
        VBox vBox = new VBox(
                comboBox,
                hBox,
                button,
                new CheckBox("checkbox"),
                textField
        );

        textField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                vBox.getChildren().add(new Label(textField.getText()));
            }
        });

        vBox.setSpacing(8.0);
        vBox.setMinSize(200, 200);
        primaryStage.setScene(new Scene(vBox));
        primaryStage.getScene().getStylesheets().addAll("app.css");
        primaryStage.setX(100);
        primaryStage.setY(100);
        primaryStage.setTitle("JavaFX app");
        primaryStage.show();

        Inspector inspector = new Inspector(primaryStage.getScene().getRoot());
        inspector.setWidth(800);
        inspector.setHeight(600);
        button.setOnAction(e -> {
            if (!inspector.isShowing())
            {
                inspector.show();
            }
        });
    }
}
