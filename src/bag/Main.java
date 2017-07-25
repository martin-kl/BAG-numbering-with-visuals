package bag;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("BAG Hollabrunn - Startnummeralgorithmus        (c) Maximilian Peer & Martin Klampfer");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

}
