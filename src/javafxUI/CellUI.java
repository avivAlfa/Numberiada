package javafxUI;

import game.Cell;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class CellUI extends Button {
    private Cell content;
    private StringProperty value;

    public CellUI() {}

    public CellUI(Cell cell)
    {
        content = cell;
        this.setPrefSize(60,60);
        this.disableProperty().bind(new SimpleBooleanProperty(cell.isEmpty() || cell.isCursor()));
        //TODO: conditional binding to textProperty
      //  textProperty().bind(Bindings.when(new SimpleBooleanProperty(cell.isEmpty())).then
        textProperty().bind(new SimpleIntegerProperty(cell.getValue()).asString());
    }
}
