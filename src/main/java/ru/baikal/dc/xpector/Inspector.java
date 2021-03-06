package ru.baikal.dc.xpector;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
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
        propertiesTab.setClosable(false);
        Tab styleTab = new Tab("Style");
        styleTab.setClosable(false);
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

        root.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getTarget() instanceof Node) {
                Node node = (Node) event.getTarget();
                TreeItem<Node> foundTreeItem = findTreeItemNode(tree.getRoot(), node);
                if (foundTreeItem != null) {
                    tree.getSelectionModel().select(foundTreeItem);
                }
            }
        });
    }

    private TreeItem<Node> findTreeItemNode(TreeItem<Node> treeItem, Node node) {
        Node value = treeItem.getValue();

        if (value == node) {
            return treeItem;
        }
        for( TreeItem<Node> child : treeItem.getChildren()){
            TreeItem<Node> found = findTreeItemNode(child, node);
            if(found != null)
                return found;
        }

        return null;
    }

    private void addHighlightPopup(Parent root, ObservableValue<Node> selected) {
        HighlightPopup pop = new HighlightPopup(selected);
        EventStreams.changesOf(selected).subscribe(change -> {
            Node node = change.getNewValue();
            if (node != null && node != root && Inspector.this.isShowing()) {
                final Bounds bounds = node.getBoundsInParent();
                final Point2D point = node.getParent().localToScreen(bounds.getMinX(), bounds.getMinY(), bounds.getMinZ());
                pop.show(node, point.getX(), point.getY());
            } else {
                pop.hide();
            }
        });

        EventStreams.changesOf(focusedProperty()).subscribe(
                f -> {
                    if (!f.getNewValue()) {
                        pop.hide();
                    }
                }
        );
    }
}
