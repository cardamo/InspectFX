package ru.baikal.dc.xpector;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import org.fxmisc.easybind.EasyBind;

public class HighlightPopup extends Popup {

    private static final String INSPECTOR_SELECTED_NODE = "inspector-selected-node";

    public HighlightPopup(ObservableValue<Node> selectedNode) {
        setAutoHide(false);

        VBox root = new VBox();
        root.getStylesheets().add(Inspector.class.getPackage().getName()
            .replace('.', '/') + "/inspector.css");
        root.getStyleClass().add(INSPECTOR_SELECTED_NODE);

        Rectangle region = new Rectangle();
        region.setFill(Color.TRANSPARENT);

        region.widthProperty().bind(
            EasyBind.monadic(selectedNode).map(
                n -> n.getLayoutBounds().getWidth())
        );
        region.heightProperty().bind(
            EasyBind.monadic(selectedNode).map(
                n -> n.getLayoutBounds().getHeight())
        );

        root.getChildren().add(region);
        getContent().add(root);
    }
}
