package ru.baikal.dc.xpector;

import javafx.beans.value.ObservableValue;
import javafx.css.StyleableProperty;
import javafx.util.StringConverter;
import org.fxmisc.easybind.PreboundBinding;

public class ConverterBinding<T> extends PreboundBinding<StringConverter<T>> {
    private final ObservableValue<StyleableProperty<T>> value;

    public ConverterBinding(ObservableValue<StyleableProperty<T>> value) {
        super(value);
        this.value = value;
    }

    @Override
    protected StringConverter<T> computeValue() {
        return new StringConverter<T>() {
            @Override
            public String toString(T object) {
                return String.valueOf(object);
            }

            @Override
            public T fromString(String string) {
                return (T) string;
            }
        };
    }
}
