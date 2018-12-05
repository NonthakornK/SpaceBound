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
import renderer.RenderableHolder;
import window.SceneManager;

public class MainMenu extends Canvas {
	private static final Font TITLE_FONT = Font
			.loadFont(ClassLoader.getSystemResource("res/font/Astrobia.ttf").toString(), 50);
	private static final Font MENU_FONT = Font
			.loadFont(ClassLoader.getSystemResource("res/font/Astrobia.ttf").toString(), 35);
	private AudioClip music = RenderableHolder.mainMenuMusic;

	public MainMenu() {
		super(SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);

		music.play();
		
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.drawImage(RenderableHolder.backgroundMM, 0, 0, SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.BLUE);
		gc.setFont(TITLE_FONT);
		gc.fillText("SPACE BOUND", SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT/3);
		gc.setFont(MENU_FONT);
		gc.fillText("Press Enter to Start", SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT * 3 / 4+35);

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
