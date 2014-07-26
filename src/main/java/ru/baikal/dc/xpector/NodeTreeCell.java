package ru.baikal.dc.xpector;

import com.sun.javafx.binding.ObjectConstant;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.TreeCell;
import org.fxmisc.easybind.EasyBind;

public class NodeTreeCell extends TreeCell<Node> {
    public NodeTreeCell() {
        textProperty().bind(EasyBind.monadic(itemProperty()).flatMap(this::toStringNode));
    }

    private ObservableValue<String> toStringNode(Node node) {
        String className = node.getClass().getSimpleName();

        if (node instanceof Labeled)
        {
            return EasyBind.map(
                ((Labeled) node).textProperty(),
                text -> className + " [" + text + "]"
            );
        }

        return ObjectConstant.valueOf(className);
    }
}
