package ru.baikal.dc.xpector;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.control.TreeItem;
import org.fxmisc.easybind.EasyBind;

public class NodeTreeItem extends TreeItem<Node> {
    public NodeTreeItem(Node value) {
        super(value);

        // let the value be immutable for now

        if (value instanceof Parent) {
            ObservableList<Node> nodes = ((Parent) value).getChildrenUnmodifiable();
            nodes.addListener((Observable observable) -> recreateChildren(nodes));
            recreateChildren(nodes);
        }
    }

    private void recreateChildren(ObservableList<Node> nodes) {
        getChildren().setAll(EasyBind.map(nodes, NodeTreeItem::new));
    }
}
