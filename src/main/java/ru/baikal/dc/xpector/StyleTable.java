package ru.baikal.dc.xpector;

import com.sun.javafx.binding.ObjectConstant;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import org.fxmisc.easybind.EasyBind;

public class StyleTable extends TableView<CssMetaData<?, ?>> {
    private ObjectProperty<Node> node;

    public StyleTable() {
        node = new SimpleObjectProperty<>(this, "node");

        itemsProperty().bind(EasyBind.monadic(node)
            .map(Styleable::getCssMetaData)
            .map(FXCollections::observableArrayList)
        );

        initColumns();
        setEditable(true);
    }

    private void initColumns() {
        TableColumn<CssMetaData<?, ?>, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(param -> ObjectConstant.valueOf(param.getValue().getProperty()));

        TableColumn<CssMetaData<?, ?>, StyleableProperty> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(param -> {
            CssMetaData value = param.getValue();
            StyleableProperty<?> prop = value.getStyleableProperty(node.get());
            return ObjectConstant.valueOf(prop);
        });
        valueCol.setCellFactory(param -> {
            TextFieldTableCell<CssMetaData<?, ?>, StyleableProperty> cell = new TextFieldTableCell<>();
//            cell.converterProperty().bind(cell.itemProperty().get().getCssMetaData().getConverter());
            cell.setEditable(true);
            return cell;
        });
        valueCol.setEditable(true);

        getColumns().addAll(nameCol, valueCol);
    }

    public ObjectProperty<Node> getNode() {
        return node;
    }
}
