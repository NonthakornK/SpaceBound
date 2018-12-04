package drawing;

import game.GameMain;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.Score;
import renderer.RenderableHolder;
import window.SceneManager;

public class GameWinnerScreen extends Canvas {
	private static final Font TITLE_FONT = Font
			.loadFont(ClassLoader.getSystemResource("res/font/Winner.otf").toString(), 70);
	private static final Font SCORE_FONT = Font
			.loadFont(ClassLoader.getSystemResource("res/font/Winner.otf").toString(), 40);
	private AudioClip music = RenderableHolder.gameWinnerMusic;

	public GameWinnerScreen() {
		super(SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);
		GraphicsContext gc = this.getGraphicsContext2D();
		music.play();
		gc.drawImage(RenderableHolder.backgroundW, 0, 0, SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.YELLOW);
		gc.setFont(TITLE_FONT);
		gc.fillText("YOU WIN", SceneManager.SCENE_WIDTH / 2, SceneManager.SCENE_HEIGHT / 4);
		gc.setFont(SCORE_FONT);
		gc.setFill(Color.DARKORANGE);
		String distance = "You travelled " + Score.distance + " ly";
		gc.fillText(distance, SceneManager.SCENE_WIDTH / 2, SceneManager.SCENE_HEIGHT * 2 / 4);
		gc.setFill(Color.DODGERBLUE);
		gc.fillText("Press Enter to Play Again", SceneManager.SCENE_WIDTH / 2, SceneManager.SCENE_HEIGHT * 3 / 4);
		this.addKeyEventHandler();
	}

	private void addKeyEventHandler() {
		// TODO Fill Code

		this.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				// System.out.println(event.getCode());
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
