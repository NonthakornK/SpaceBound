package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sharedObject.RenderableHolder;
import window.SceneManager;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			RenderableHolder.loadResource();
			SceneManager.initialize(primaryStage);
			SceneManager.gotoMainMenu();
			primaryStage.setTitle("SpaceBound");
			primaryStage.setAlwaysOnTop(true);
			primaryStage.centerOnScreen();
			primaryStage.setResizable(false);
			primaryStage.sizeToScene();

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					// TODO Auto-generated method stub
					Platform.exit();
					System.exit(0);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

