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
import renderer.RenderableHolder;
import window.SceneManager;

public class MainMenu extends Canvas {
	private static final Font TITLE_FONT = Font
			.loadFont(ClassLoader.getSystemResource("res/font/Astrobia.ttf").toString(), 70);
	private static final Font MENU_FONT = Font
			.loadFont(ClassLoader.getSystemResource("res/font/Astrobia.ttf").toString(), 35);
	private static final Font TUTORIAL_FONT = Font
			.loadFont(ClassLoader.getSystemResource("res/font/Astrobia.ttf").toString(), 22);
	private MediaPlayer music = RenderableHolder.mainMenuMusic;
	private boolean inTutorial;

	public MainMenu() {
		super(SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);
		music.play();
		HomeMenu();	
		this.inTutorial = false;	
	}
	
	private void HomeMenu() {
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.drawImage(RenderableHolder.backgroundMM, 0, 0, SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.BLUE);
		gc.setFont(TITLE_FONT);
		gc.fillText("SPACEBOUND", SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT/3);
		gc.setFont(MENU_FONT);
		gc.fillText("Press Enter to Start", SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT * 3 / 4 );
		gc.setFill(Color.DARKBLUE);
		gc.setFont(TUTORIAL_FONT);
		gc.fillText("Press Spacebar For Tutorial", SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT * 5 / 6 + 80);
		this.addKeyEventHandler();

	}
	
	private void TutorialMenu() {
		this.inTutorial = true;
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.drawImage(RenderableHolder.backgroundMM, 0, 0, SceneManager.SCENE_WIDTH, SceneManager.SCENE_HEIGHT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.BLUE);
		gc.setFont(TUTORIAL_FONT);
		gc.fillText("Use Arrow Keys to Move", SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT * 1 / 10);
		gc.fillText("Press Spacebar to Shoot", SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT * 1 / 10 + 50);
		gc.fillText("Press Ctrl to Use Power Attack", SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT * 1 / 10 + 100);
		gc.setFont(MENU_FONT);
		gc.setFill(Color.MEDIUMBLUE);
		gc.fillText("Press Enter to Start", SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT * 5/6 + 90);
		
		gc.fillText("Powerups", SceneManager.SCENE_WIDTH/2, SceneManager.SCENE_HEIGHT * 1 / 9 + 200);
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setFont(TUTORIAL_FONT);
		gc.setFill(Color.ROYALBLUE);
		gc.drawImage(RenderableHolder.attackBox, SceneManager.SCENE_WIDTH/7, SceneManager.SCENE_HEIGHT * 1 / 9 + 245, 40 ,40);
		gc.fillText("Increases Firepower", SceneManager.SCENE_WIDTH * 0.3, SceneManager.SCENE_HEIGHT * 1 / 9 + 270);
		
		gc.drawImage(RenderableHolder.shieldmax, SceneManager.SCENE_WIDTH/7, SceneManager.SCENE_HEIGHT * 1 / 9 + 295, 40 ,40);
		gc.fillText("Increases Maximum Shield", SceneManager.SCENE_WIDTH * 0.3, SceneManager.SCENE_HEIGHT * 1 / 9 + 320);

		gc.drawImage(RenderableHolder.shieldregen, SceneManager.SCENE_WIDTH/7, SceneManager.SCENE_HEIGHT * 1 / 9 + 350, 40 ,40);
		gc.fillText("Increases Shield Regeneration Rate", SceneManager.SCENE_WIDTH * 0.3, SceneManager.SCENE_HEIGHT * 1 / 9 + 370);
		gc.fillText("And Reduces Shield Cooldown", SceneManager.SCENE_WIDTH * 0.3, SceneManager.SCENE_HEIGHT * 1 / 9 + 395);
		
		gc.drawImage(RenderableHolder.powerattackBox, SceneManager.SCENE_WIDTH/7 + 3, SceneManager.SCENE_HEIGHT * 1 / 9 + 420, 36, 36);
		gc.fillText("Charges Power Attack  (Max : 3 )", SceneManager.SCENE_WIDTH * 0.3, SceneManager.SCENE_HEIGHT * 1 / 9 + 445);
		
		gc.drawImage(RenderableHolder.triplefirebox, SceneManager.SCENE_WIDTH/7 + 3, SceneManager.SCENE_HEIGHT * 1 / 9 + 470, 36, 36);
		gc.fillText("Triple The Fun", SceneManager.SCENE_WIDTH * 0.3, SceneManager.SCENE_HEIGHT * 1 / 9 + 495);
		
		gc.drawImage(RenderableHolder.healthpack, SceneManager.SCENE_WIDTH/7 + 5, SceneManager.SCENE_HEIGHT * 1 / 9 + 515, 32, 38);
		gc.fillText("Increases Health", SceneManager.SCENE_WIDTH * 0.3, SceneManager.SCENE_HEIGHT * 1 / 9 + 545);
		
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
				} else if (event.getCode() == KeyCode.SPACE && inTutorial == false) {
					TutorialMenu();
				} else if (event.getCode() == KeyCode.ESCAPE) {
					Platform.exit();
				}
			}
		});
	}
}
