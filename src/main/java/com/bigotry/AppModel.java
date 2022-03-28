package com.bigotry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.springframework.stereotype.Component;

/**
 * @author ：JJHu
 * @date ：Created in 2020/12/4 17:34
 * @description：
 * @modified By：
 * @version: $
 */

public class AppModel {
    private StringProperty text = new SimpleStringProperty();

    public AppModel()
    {
        this.text = new SimpleStringProperty();
    }

    public StringProperty textProperty() {
        return text;
    }

    public final String getText() {
        return textProperty().get();
    }

    public final void setText(String text) {
        textProperty().set(text);
    }
}
