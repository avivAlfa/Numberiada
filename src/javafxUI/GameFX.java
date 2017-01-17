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

    public static void main(String[] args) {
        launch(args);
    }
    private StringProperty styleCssProperty = new SimpleStringProperty("gameStyle1.css");
    private ObservableList<String> styleList = FXCollections.observableArrayList("2", "nofar");

    @FXML
    private ChoiceBox skinSelect;


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
      //  GameController gameController = fxmlLoader.getController();
      //  gameController.setModel(model);


        Scene scene = new Scene(root, 730, 612);
        //styleList = FXCollections.observableArrayList();
        skinSelect = new ChoiceBox();
      //  styleList.addAll("2", "nofar");
        //skinSelect.getItems().addAll("2", "nofar");

        skinSelect.setValue("2");
        skinSelect.setItems(styleList);

        //skinSelect.setItems(styleList);
        //styleList.add("2");
        //styleList.add("nofar");
        this.styleCssProperty.bind(skinSelect.valueProperty());
        this.styleCssProperty.addListener((observable, oldValue, newValue) -> {
            //this.styleChanged(oldValue, newValue);
            scene.getStylesheets().remove("resources/" + oldValue);
            scene.getStylesheets().add("resources/" + newValue);
        });
       // scene.getStylesheets().addAll("resources/nofar.css", "resources/2.css");
//        Button singInButton = (Button) scene.lookup("#signInButton");
//        final Text actionTarget = (Text)scene.lookup("#actiontarget");
//        singInButton.setOnAction(e -> {
//            actionTarget.setText("Sign in button pressed!");
//        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
