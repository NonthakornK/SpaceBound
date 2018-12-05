package renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;

public class RenderableHolder {// the picture class maker and we use this to make instead o
	private static final RenderableHolder instance = new RenderableHolder();

	private List<IRenderable> entities;
	private Comparator<IRenderable> comparator;
	// various image plz check the image first before using (like to find its size /
	// how it looks etc)
	public static Image dragon, eSemiBoss, eBoss, eGhost, eSpiriteFire, eJet, eMachine, eTree, bullet, background,
			backgroundMM, backgroundW, healthpack, bossBullet, roundBulletB, roundBulletY, roundBulletR, roundBulletP, sparkArr[],
			powerAttack, exploArr[], randomBox, asteroidArr[], shieldmax, shieldregen, attackBox, triplefirebox, powerattackBox;

	public static AudioClip bgm, fireBall, explosion, explosion2, gameOverMusic, mainMenuMusic, powerAttackLaunch,
			gameWinnerMusic,laser;
	public static Font inGameFont, inGameFontSmall;

	public static AudioClip[] explosions;

	static {
		loadResource();
	}

	public RenderableHolder() {
		entities = Collections.synchronizedList(new ArrayList<>());
		comparator = (IRenderable o1, IRenderable o2) -> {
			if (o1.getZ() > o2.getZ()) {
				return 1;
			}
			return -1;
		};
	}

	public static RenderableHolder getInstance() {
		return instance;
	}

	public static void loadResource() {
		dragon = new Image(ClassLoader.getSystemResource("res/player/SpaceD.gif").toString());
		// player picture

		eSemiBoss = new Image(ClassLoader.getSystemResource("res/enemy/semiboss.gif").toString());
		eBoss = new Image(ClassLoader.getSystemResource("res/enemy/BigBoss.gif").toString());
		eGhost = new Image(ClassLoader.getSystemResource("res/enemy/Extra.gif").toString());
		asteroidArr = new Image[4];
		for (int i = 0; i < 4; i++) {
			asteroidArr[i] = new Image(ClassLoader.getSystemResource("res/enemy/asteroid" + i + ".gif").toString());
		}
		
		eMachine = new Image(ClassLoader.getSystemResource("res/enemy/Normal.png").toString());
		eJet = new Image(ClassLoader.getSystemResource("res/enemy/Normal.png").toString());
		eTree = new Image(ClassLoader.getSystemResource("res/enemy/Excusive.gif").toString());
		// mob picture
		exploArr = new Image[12];
		for (int i = 0; i < 12; i++) {
			exploArr[i] = new Image(ClassLoader.getSystemResource("res/explosion/" + i + ".gif").toString());
		}
		
		
		// explore loop from sprite sheet that was cut before
		sparkArr = new Image[4];
		for (int i = 0; i < 4; i++) {
			sparkArr[i] = new Image(ClassLoader.getSystemResource("res/spark/" + i + ".png").toString());
		}

		bullet = new Image(ClassLoader.getSystemResource("res/bullet/Laser.png").toString());
		powerAttack = new Image(ClassLoader.getSystemResource("res/bullet/fireball.gif").toString());
		bossBullet = new Image(ClassLoader.getSystemResource("res/bullet/bossBullet.gif").toString());
		roundBulletB = new Image(ClassLoader.getSystemResource("res/bullet/roundBulletB.png").toString());
		roundBulletY = new Image(ClassLoader.getSystemResource("res/bullet/roundBulletY.png").toString());
		roundBulletR = new Image(ClassLoader.getSystemResource("res/bullet/roundBulletR.png").toString());
		roundBulletP = new Image(ClassLoader.getSystemResource("res/bullet/roundBulletP.png").toString());
		// bullet picture
		background = new Image(ClassLoader.getSystemResource("res/background/BackSpace.jpg").toString());
		backgroundMM = new Image(ClassLoader.getSystemResource("res/background/BackMenu.png").toString());
		backgroundW = new Image(ClassLoader.getSystemResource("res/background/BackWinner.jpg").toString());
		// background of 3 screen
		randomBox = new Image(ClassLoader.getSystemResource("res/items/randombox.png").toString());
		attackBox = new Image(ClassLoader.getSystemResource("res/items/attackbox.gif").toString());
		//------------
		triplefirebox = new Image(ClassLoader.getSystemResource("res/items/triple.png").toString());
		//------------
		powerattackBox = new Image(ClassLoader.getSystemResource("res/items/SUPERPOWER.png").toString());
		healthpack = new Image(ClassLoader.getSystemResource("res/items/health.png").toString());
		shieldmax = new Image(ClassLoader.getSystemResource("res/items/shieldmax.gif").toString());
		shieldregen = new Image(ClassLoader.getSystemResource("res/items/shieldregen.gif").toString());
		
		
		bgm = new AudioClip(ClassLoader.getSystemResource("res/song/GameScreen.mp3").toExternalForm());
		fireBall = new AudioClip(ClassLoader.getSystemResource("res/song/Fire_Ball.mp3").toExternalForm());
		fireBall.setVolume(0.35);
		laser = new AudioClip(ClassLoader.getSystemResource("res/song/laser.wav").toExternalForm());
		laser.setVolume(0.35);
		gameWinnerMusic = new AudioClip(ClassLoader.getSystemResource("res/song/GameWinner.mp3").toExternalForm());
		gameOverMusic = new AudioClip(ClassLoader.getSystemResource("res/song/GameLoser.mp3").toExternalForm());
		mainMenuMusic = new AudioClip(ClassLoader.getSystemResource("res/song/MenuSound.mp3").toExternalForm());
		explosion = new AudioClip(ClassLoader.getSystemResource("res/song/Explosion.wav").toExternalForm());
		explosion2 = new AudioClip(ClassLoader.getSystemResource("res/song/Explosion2.wav").toExternalForm());
		
		powerAttackLaunch = new AudioClip(ClassLoader.getSystemResource("res/song/PowerAttack.mp3").toExternalForm());

		explosions = new AudioClip[] { explosion, explosion2 };
		// sound effect
		inGameFont = Font.loadFont(ClassLoader.getSystemResource("res/font/Astrobia.ttf").toString(), 40);
		inGameFontSmall = Font.loadFont(ClassLoader.getSystemResource("res/font/Astrobia.ttf").toString(), 22.5);
	}

	public void add(IRenderable entity) {
		// System.out.println("add");
		entities.add(entity);
		Collections.sort(entities, comparator);
	}

	public void update() {
		for (int i = entities.size() - 1; i >= 0; i--) {
			if (entities.get(i).isDestroyed())
				entities.remove(i);
		}
	}

	public List<IRenderable> getEntities() {
		return entities;
	}

	public void clear() {
		entities.clear();
	}

}
