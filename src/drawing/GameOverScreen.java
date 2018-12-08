package drawing;

import game.GameMain;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.Distance;
import renderer.RenderableHolder;
import window.SceneManager;

public class GameOverScreen extends Canvas {

	private static final Font TITLE_FONT = Font
			.loadFont(ClassLoader.getSystemResource("res/font/Astrobia.ttf").toString(), 70);
	private static final Font Distance_FONT = Font
			.loadFont(ClassLoader.getSystemResource("res/font/Astrobia.ttf").toString(), 40);
	private MediaPlayer music = RenderableHolder.gameOverMusic;

	public GameOverScreen() {
		super(SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);
		GraphicsContext gc = this.getGraphicsContext2D();
		music.play();
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.RED);
		gc.setFont(TITLE_FONT);
		gc.fillText("GAME OVER", SceneManager.SCENE_WIDTH / 2, SceneManager.SCENE_HEIGHT / 4);
		gc.setFont(Distance_FONT);
		gc.setFill(Color.RED);
		String distance = "Your survived " + Distance.distance + " ly";
		gc.fillText(distance, SceneManager.SCENE_WIDTH / 2, SceneManager.SCENE_HEIGHT * 2 / 4);
		gc.setFill(Color.RED);
		gc.fillText("Press Enter to retry", SceneManager.SCENE_WIDTH / 2, SceneManager.SCENE_HEIGHT * 3 / 4);
		this.addKeyEventHandler();
	}

	private void addKeyEventHandler() {
		// TODO Fill Code

		this.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getCode() == KeyCode.ENTER) {
					music.stop();
					GameMain.newGame();
				} else if (event.getCode() == KeyCode.ESCAPE) {
					Platform.exit();
				}
			}
		});
	}

}
