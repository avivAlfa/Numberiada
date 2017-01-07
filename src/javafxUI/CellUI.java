package javafxUI;

import game.Cell;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

import static jdk.nashorn.internal.objects.NativeFunction.bind;

public class CellUI extends Button {
    private Cell content;
    SimpleBooleanProperty  isEmptyProperty;
    SimpleBooleanProperty isCursorProperty;

    //private StringProperty value;

    public CellUI() {}

    public CellUI(Cell cell)
    {
        content = cell;
        isEmptyProperty = new SimpleBooleanProperty(content.isEmpty());
        isCursorProperty = new SimpleBooleanProperty(content.isCursor());


        this.setPrefSize(62,62);
        //this.disableProperty().bind(Bindings.or(isEmptyProperty,isCursorProperty));
        this.textProperty().bind(
                Bindings.when(isCursorProperty)
                        .then("@")
                        .otherwise
                                (Bindings.when(isEmptyProperty)
                                .then("")
                                .otherwise(Bindings.concat(content.getValue()))));

    }

    public Cell getContent() {
        return content;
    }

    public void updateValues(){
        isEmptyProperty.setValue(content.isEmpty());
        isCursorProperty.setValue(content.isCursor());
        if(isCursorProperty.getValue())
            this.setStyle("-fx-base: #b6e7c8;");
        else
           this.setStyle("-fx-base: #ececec ");

    }

}
