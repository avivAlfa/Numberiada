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
        // GameEngine model = new GameEngine();

        primaryStage.setTitle("Numberiada");

        //if you just want to load the FXML
//        Parent root = FXMLLoader.load(WelcomeFXML.class.getResource("welcome.fxml"));

        //if you want to load the FXML and get access to its controller
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/resources/gameFX.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        GameController gameController = fxmlLoader.getController();

        //  gameController.setModel(model);


        this.scene = new Scene(root, 730, 612);
        //styleList = FXCollections.observableArrayList();
        //  skinSelect = new ChoiceBox();
        //  styleList.addAll("2", "nofar");
        //skinSelect.getItems().addAll("2", "nofar");

//        skinSelect.setValue("2");
//        skinSelect.setItems(styleList);

        //skinSelect.setItems(styleList);
        //styleList.add("2");
        //styleList.add("nofar");
        // this.styleCssProperty.bind(skinSelect.valueProperty());

//        this.styleCssProperty.addListener((observable, oldValue, newValue) -> {
//            //this.styleChanged(oldValue, newValue);
//            scene.getStylesheets().remove("resources/" + oldValue);
//            scene.getStylesheets().add("resources/" + newValue);
//        });
        // scene.getStylesheets().addAll("resources/nofar.css", "resources/2.css");
//        Button singInButton = (Button) scene.lookup("#signInButton");
//        final Text actionTarget = (Text)scene.lookup("#actiontarget");
//        singInButton.setOnAction(e -> {
//            actionTarget.setText("Sign in button pressed!");
//        });

        //scene.getStylesheets().addAll("resources/nofar.css","resources/2.css");

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
