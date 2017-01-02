import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;

public class CellUI extends Button {
    private Cell content;

    public CellUI() {}

    public CellUI(Cell cell)
    {
        content = cell;
        this.setPrefSize(60,60);
        this.disableProperty().bind(new SimpleBooleanProperty(cell.isEmpty() || cell.isCursor()));
        //TODO: conditional binding to textProperty
        textProperty().bind(new SimpleIntegerProperty(cell.getValue()).asString());
    }
}
