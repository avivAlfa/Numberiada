package javafxUI;

import game.Cell;
import game.Colors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;

import static jdk.nashorn.internal.objects.NativeFunction.bind;

public class CellUI extends Button {
    private Cell content;
    SimpleBooleanProperty  isEmptyProperty;
    SimpleBooleanProperty isCursorProperty;
    ImageView markerImage;

    //private StringProperty value;

    public CellUI() {}

    public CellUI(Cell cell)
    {
        content = cell;
        isEmptyProperty = new SimpleBooleanProperty(content.isEmpty());
        isCursorProperty = new SimpleBooleanProperty(content.isCursor());
        markerImage = new ImageView(new Image(getClass().getResourceAsStream("/resources/marker.png")));


        this.setPrefSize(62,62);
        //this.disableProperty().bind(Bindings.or(isEmptyProperty,isCursorProperty));
//        this.textProperty().bind(
//                Bindings.when(isCursorProperty)
//                        .then("@")
//                        .otherwise
//                                (Bindings.when(isEmptyProperty)
//                                .then("")
//                                .otherwise(Bindings.concat(content.getValue()))));


        if(content.isCursor())
            this.setGraphic(markerImage);

        this.textProperty().bind(
                Bindings.when(Bindings.or(isCursorProperty,isEmptyProperty))
                                        .then("")
                                        .otherwise(Bindings.concat(content.getValue())));

        this.setStyle("-fx-text-fill: "+ Colors.getColor(content.getColor())+"; -fx-font-size: 14;font-weight: bold;");

    }

    public Cell getContent() {
        return content;
    }

    public void clear() {
        this.content = null;

    }

    public void updateValues(){
        isEmptyProperty.setValue(content.isEmpty());
        isCursorProperty.setValue(content.isCursor());

        this.setStyle("-fx-text-fill: "+Colors.getColor(content.getColor())+";-fx-font-size: 14;font-weight: bold;");
        if(isCursorProperty.getValue())
            this.setGraphic(markerImage);
        else
           this.setGraphic(null);

    }

}
