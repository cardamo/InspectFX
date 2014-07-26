package ru.baikal.dc.xpector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class TestApp extends Application {
    public static void main(String[] args) {
        Application.launch(TestApp.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ComboBox<Shape> comboBox = new ComboBox<Shape>();
            comboBox.getItems().addAll(
                new Text("Text"),
                new Rectangle(10, 10, Color.RED),
                new Rectangle(10, 10, Color.GREEN),
                new Rectangle(10, 10, Color.BLUE)
            );
        comboBox.getSelectionModel().selectFirst();

        final VBox box = new VBox(
                comboBox,
                new Label("label one"),
                new Label("label two"),
                new Label("label three"),
                new Button("button"),
                new CheckBox("checkbox")
        );

        box.setSpacing(8.0);
        box.setMinSize(200, 200);
        primaryStage.setScene(new Scene(box));
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
