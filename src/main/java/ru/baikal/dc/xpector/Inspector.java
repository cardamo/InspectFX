package ru.baikal.dc.xpector;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import org.fxmisc.easybind.EasyBind;
import org.reactfx.EventStreams;

public class Inspector extends Stage {

    public Inspector(Parent root) {
        DomTree tree = new DomTree(root);
        PropertiesTable properties = new PropertiesTable();
        StyleTable style = new StyleTable();
        SplitPane splitPane = new SplitPane();
        TabPane tabs = new TabPane();

        Tab propertiesTab = new Tab("Properties");
        Tab styleTab = new Tab("Style");
        tabs.getTabs().addAll(propertiesTab, styleTab);
        propertiesTab.setContent(properties);
        styleTab.setContent(style);
        tabs.getSelectionModel().select(0);

        splitPane.getItems().addAll(tree, tabs);
        splitPane.setDividerPosition(0, 0.2);

        ObservableValue<Node> selected = EasyBind
            .monadic(tree.getSelectionModel().selectedItemProperty())
            .flatMap(TreeItem::valueProperty);

        properties.getNode().bind(selected);
        style.getNode().bind(selected);

        setScene(new Scene(splitPane));

        setTitle("Inspector");
        tree.getRoot().setExpanded(true);
        tree.getSelectionModel().selectFirst();

        addHighlightPopup(root, selected);
    }

    private void addHighlightPopup(Parent root, ObservableValue<Node> selected) {
        HighlightPopup pop = new HighlightPopup(selected);
        EventStreams.changesOf(selected).subscribe(change -> {
            Node node = change.getNewValue();
            if (node != null) {
                Point2D point = node.localToScreen(new Point2D(0, 0));
                pop.show(node, point.getX(), point.getY());
            } else {
                pop.hide();
            }
        });

        BooleanBinding focused = Bindings.or(focusedProperty(), root.getScene().getWindow().focusedProperty());

        EventStreams.changesOf(focused).subscribe(
                f -> {
                    if (!f.getNewValue()) {
                        pop.hide();
                    }
                }
        );
    }
}
