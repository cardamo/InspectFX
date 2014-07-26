package ru.baikal.dc.xpector;

import javafx.beans.value.ObservableValue;
import javafx.util.StringConverter;
import org.fxmisc.easybind.PreboundBinding;

import java.lang.reflect.InvocationTargetException;

public class ConverterBinding<T> extends PreboundBinding<StringConverter<T>> {
    private final ObservableValue<T> value;

    public ConverterBinding(ObservableValue<T> value) {
        super(value);
        this.value = value;
    }

    @Override
    protected StringConverter<T> computeValue() {

        final Object val = value.getValue();
        if (val == null) {
            return new Converter();
        }
        final Class<?> cls = val.getClass();

        return (cls.isEnum())? new Converter() {
                    @Override
                    public T fromString(String string) {
                        return (T)Enum.valueOf((Class)value.getValue().getClass(), string);
                    }
                } :
                val instanceof Boolean? new Converter() {
                    @Override
                    public T fromString(String string) {
                        return (T) Boolean.valueOf(string);
                    }
                } :
                val instanceof Integer? new Converter() {
                    @Override
                    public T fromString(String string) {
                        return (T) Integer.valueOf(string);
                    }
                } :
                val instanceof Double? new Converter() {
                    @Override
                    public T fromString(String string) {
                        return (T) Double.valueOf(string);
                    }
                } :
                val instanceof String? new Converter() {
                    @Override
                    public T fromString(String string) {
                        return (T)string;
                    }
                } :
                new Converter();
    }

    private class Converter extends StringConverter<T> {
        @Override
        public String toString(T object) {
            return String.valueOf(object);
        }

        @Override
        public T fromString(String string) {
            try {
                return (T) value.getValue().getClass().getMethod("valueOf", String.class).invoke(string);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
