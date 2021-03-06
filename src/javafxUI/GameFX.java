package javafxUI; /**
 * Created by alfav on 12/28/2016.
 */

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.net.URL;

public class GameFX extends Application {
    private Scene scene;
    private StringProperty styleCssProperty = new SimpleStringProperty("gameStyle1.css");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Numberiada");

        //if you just want to load the FXML
//        Parent root = FXMLLoader.load(WelcomeFXML.class.getResource("welcome.fxml"));

        //if you want to load the FXML and get access to its controller
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/resources/gameFX.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        GameController gameController = fxmlLoader.getController();

        this.scene = new Scene(root, 730, 612);

        this.styleCssProperty.bind(gameController.getStyleCssProperty());
        this.styleCssProperty.addListener((observable, oldValue, newValue) -> {
            this.styleChanged(oldValue, newValue);
        });

        scene.getStylesheets().add("resources/" + styleCssProperty.getValue() + ".css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void styleChanged(String oldStyle, String newStyle) {
        this.scene.getStylesheets().remove("resources/" + oldStyle + ".css");
        this.scene.getStylesheets().add("resources/" + newStyle + ".css");

    }
}
