package ru.baikal.dc.xpector;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TreeView;

public class DomTree
extends TreeView<Node>
{
    public DomTree(
        Parent root)
    {
        setCellFactory(param -> new NodeTreeCell());
        setRoot(new NodeTreeItem(root));
    }
}
