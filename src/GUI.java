import controllers.Controller;
import controllers.OptionsController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

// TODO: eventually add open cv image processing stuff
// TODO: eventually switch to writableimage and pixelwriter instead of bufferedimage
public class GUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/Main.fxml"));
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/resources/options.fxml"));
            Parent root = loader.load();
            loader2.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/resources/main.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            Controller.primaryStage = primaryStage;
            Controller.scene = scene;

            OptionsController.controller = loader.getController();
            Controller.optionsController = loader2.getController();

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
