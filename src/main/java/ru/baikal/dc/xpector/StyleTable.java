package ru.baikal.dc.xpector;

import com.google.common.base.Joiner;
import com.google.common.collect.ObjectArrays;
import com.sun.javafx.binding.ObjectConstant;
import com.sun.javafx.css.CascadingStyle;
import com.sun.javafx.css.Declaration;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.StyleManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.ParsedValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicBinding;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StyleTable extends TableView<CascadingStyle> {
    private ObjectProperty<Node> node;

    public StyleTable() {
        node = new SimpleObjectProperty<>(this, "node");
        StyleManager styleManager = StyleManager.getInstance();

        MonadicBinding<List<CascadingStyle>> flatten = EasyBind.monadic(node).map(
            n -> styleManager.findMatchingStyles(n, getTriggerStates(n))
                .getCascadingStyles()
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList())
        );

        itemsProperty().bind(flatten.map(FXCollections::observableArrayList));

        initColumns();
        setEditable(true);
    }

    private Set<PseudoClass>[] getTriggerStates(Node n) {
//        return null;
        return new Set[]{ new HashSet<>(n.getPseudoClassStates())};
    }

    private void initColumns() {
        TableColumn<CascadingStyle, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(param -> ObjectConstant.valueOf(param.getValue().getProperty()));

        TableColumn<CascadingStyle, String> selectorCol = new TableColumn<>("Selector");
        selectorCol.setCellValueFactory(param -> ObjectConstant.valueOf(param.getValue().getSelector().toString()));

        TableColumn<CascadingStyle, String> ruleCol = new TableColumn<>("Value");
        ruleCol.setCellValueFactory(param -> ObjectConstant.valueOf(getValue(param.getValue())));

        TableColumn<CascadingStyle, String> originCol = new TableColumn<>("Origin");
        originCol.setCellValueFactory(param -> ObjectConstant.valueOf(param.getValue().getOrigin().toString()));

        getColumns().addAll(nameCol, selectorCol, ruleCol, originCol);
    }

    private String getValue(CascadingStyle style) {
        Joiner joiner = Joiner.on("\n");
        return style.getRule().getDeclarations().stream()
            .filter(d -> d.getProperty().equals(style.getProperty()))
            .map(d -> d.getParsedValue().getValue())
            .map(this::mapArray)
            .map(joiner::join)
            .collect(Collectors.joining("\n"));
    }

    private Object[] mapArray(Object val) {
        Object[] result = val.getClass().isArray() ? (Object[]) val : new Object[]{val};
        Object[] transformed = new Object[result.length];
        for (int i = 0; i < result.length; i++) {
            transformed[i] = result[i] instanceof ParsedValue ? ((ParsedValue) result[i]).getValue() : result[i];
        }
        return transformed;
    }

    public ObjectProperty<Node> getNode() {
        return node;
    }
}
