package ru.baikal.dc.xpector;

import com.sun.javafx.binding.ObjectConstant;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import org.fxmisc.easybind.EasyBind;

import java.lang.reflect.Method;

public class PropertiesTable extends TableView<Method> {
    private ObjectProperty<Node> node;

    public PropertiesTable() {
        node = new SimpleObjectProperty<>(this, "node");

        itemsProperty().bind(EasyBind.monadic(node)
            .map(Object::getClass)
            .map(Class::getMethods)
            .map(FXCollections::observableArrayList)
            .map(list -> list.filtered(this::isProperty))
            .map(FXCollections::observableArrayList)// to be sortable
        );

        initColumns();
        setEditable(true);
    }

    private void initColumns() {
        TableColumn<Method, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(param -> ObjectConstant.valueOf(formatPropertyName(param)));
        nameCol.setSortable(true);

        TableColumn<Method, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(param -> {
            String typeName = param.getValue().getGenericReturnType().getTypeName();
            return ObjectConstant.valueOf(formatTypeName(typeName));
        });
        typeCol.setSortable(true);

        TableColumn<Method, Object> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(param -> invokeMethod(param.getValue()));
        valueCol.setCellFactory(param -> {
            TextFieldTableCell<Method, Object> cell = new TextFieldTableCell<>();
            cell.converterProperty().bind(new ConverterBinding(cell.itemProperty()));
            cell.editableProperty().bind(EasyBind.monadic(cell.itemProperty()).map(this::isScalar));
            return cell;
        });
        valueCol.setEditable(true);

        getColumns().addAll(nameCol, typeCol, valueCol);

        NumberBinding colWidth = widthProperty().divide(3);
        getColumns().forEach(col -> col.prefWidthProperty().bind(colWidth));
    }

    private String formatTypeName(String typeName) {
        if (typeName.startsWith("javafx.beans.property.")) {
            typeName = typeName.substring("javafx.beans.property.".length());
        }
        return typeName;
    }

    private String formatPropertyName(TableColumn.CellDataFeatures<Method, String> param) {
        String name = param.getValue().getName();
        if (name.endsWith("Property")) {
            name = name.substring(0, name.length() - "Property".length());
        }
        return name;
    }

    private Property<Object> invokeMethod(Method method) {
        try {
            return (Property<Object>) method.invoke(node.get());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isProperty(Method m) {
        return Property.class.isAssignableFrom(m.getReturnType()) && m.getParameterCount() == 0;
    }

    private boolean isScalar(Object val) {
        if (val instanceof Boolean || val instanceof Double || val instanceof Integer || val instanceof String) {
            return true;
        }
        try {
            final Class<?> cls = val.getClass();
            return cls.isEnum() || cls.getMethod("valueOf", String.class) != null;
        } catch (NoSuchMethodException ignored) {
            return false;
        }
    }

    public ObjectProperty<Node> getNode() {
        return node;
    }
}
