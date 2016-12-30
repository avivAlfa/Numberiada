/**
 * Created by alfav on 12/28/2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class GameFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameEngine model = new GameEngine();

        primaryStage.setTitle("Numberiada");

        //if you just want to load the FXML
//        Parent root = FXMLLoader.load(WelcomeFXML.class.getResource("welcome.fxml"));

        //if you want to load the FXML and get access to its controller
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("resources/gameFX.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        GameController gameController = fxmlLoader.getController();
        gameController.setModel(model);

        Scene scene = new Scene(root, 700, 500);

//        Button singInButton = (Button) scene.lookup("#signInButton");
//        final Text actionTarget = (Text)scene.lookup("#actiontarget");
//        singInButton.setOnAction(e -> {
//            actionTarget.setText("Sign in button pressed!");
//        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
