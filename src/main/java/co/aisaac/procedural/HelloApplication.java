package co.aisaac.procedural;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class HelloApplication extends Application {
	@Override
	public void start(Stage stage) {

		VBox box = new VBox();
		box.getChildren().add(new VBox());

		Scene scene = new Scene(box, 1600, 960);
		scene.setFill(Paint.valueOf("black"));

		stage.setTitle("Hello!");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}
