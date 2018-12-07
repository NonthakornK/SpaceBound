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
	public static Image dragon, eSemiBoss, eBoss, eGhost, eSpiriteFire, eJet, eMachine, eHeavy, bullet, background,
			backgroundMM, backgroundW, healthpack, bossBullet, bossPower, bossLow, roundBulletB, roundBulletY,
			roundBulletR, roundBulletP, beamSmallG, beamSmallY, sparkArr[], powerAttack, exploArr[], randomBox,
			asteroidArr[], shieldmax, shieldregen, attackBox, triplefirebox, powerattackBox;

	public static AudioClip bgm, fireBall, explosion, explosion2, gameOverMusic, mainMenuMusic, powerAttackLaunch,
			gameWinnerMusic, laser;
	public static Font inGameFont, inGameFontSmall;

	public static AudioClip[] explosions;

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

	public static void loadResource() throws LoadUnableException {
		// dragon = imageLoader("res/player/SpaceD.gif"));
		dragon = imageLoader("res/player/SpaceD.gif");
		// player picture

		eSemiBoss = imageLoader("res/enemy/semiboss.gif");
		eBoss = imageLoader("res/enemy/BigBoss.gif");
		eGhost = imageLoader("res/enemy/Extra.gif");
		asteroidArr = new Image[4];
		for (int i = 0; i < 4; i++) {
			asteroidArr[i] = imageLoader("res/enemy/asteroid" + i + ".gif");
		}

		eMachine = imageLoader("res/enemy/Normal.png");
		eJet = imageLoader("res/enemy/Normal.png");
		eHeavy = imageLoader("res/enemy/Excusive.gif");
		// mob picture
		exploArr = new Image[12];
		for (int i = 0; i < 12; i++) {
			exploArr[i] = imageLoader("res/explosion/" + i + ".gif");
		}

		// explore loop from sprite sheet that was cut before
		sparkArr = new Image[4];
		for (int i = 0; i < 4; i++) {
			sparkArr[i] = imageLoader("res/spark/" + i + ".png");
		}

		bullet = imageLoader("res/bullet/Laser.png");
		powerAttack = imageLoader("res/bullet/fireball.gif");
		bossBullet = imageLoader("res/bullet/bossBullet.gif");
		bossPower = imageLoader("res/bullet/bossPower.png");
		bossLow = imageLoader("res/bullet/bossLow.png");

		roundBulletB = imageLoader("res/bullet/roundBulletB.png");
		roundBulletY = imageLoader("res/bullet/roundBulletY.png");
		roundBulletR = imageLoader("res/bullet/roundBulletR.png");
		roundBulletP = imageLoader("res/bullet/roundBulletP.png");
		beamSmallG = imageLoader("res/bullet/beamSmallG.png");
		beamSmallY = imageLoader("res/bullet/beamSmallY.png");
		// bullet picture
		background = imageLoader("res/background/BackSpace.jpg");
		backgroundMM = imageLoader("res/background/BackMenu.png");
		backgroundW = imageLoader("res/background/BackWinner.jpg");
		// background of 3 screen
		randomBox = imageLoader("res/items/randombox.png");
		attackBox = imageLoader("res/items/attackbox.gif");
		// ------------
		triplefirebox = imageLoader("res/items/triple.png");
		// ------------
		powerattackBox = imageLoader("res/items/SUPERPOWER.png");
		healthpack = imageLoader("res/items/health.png");
		shieldmax = imageLoader("res/items/shieldmax.gif");
		shieldregen = imageLoader("res/items/shieldregen.gif");

		bgm = audioLoader("res/song/GameScreen.mp3");
		fireBall = audioLoader("res/song/Fire_Ball.mp3");
		fireBall.setVolume(0.35);
		laser = audioLoader("res/song/laser.wav");
		laser.setVolume(0.35);
		gameWinnerMusic = audioLoader("res/song/GameWinner.mp3");
		gameOverMusic = audioLoader("res/song/GameLoser.mp3");
		mainMenuMusic = audioLoader("res/song/MenuSound.mp3");
		explosion = audioLoader("res/song/Explosion.wav");
		explosion2 = audioLoader("res/song/Explosion2.wav");

		powerAttackLaunch = audioLoader("res/song/PowerAttack.mp3");

		explosions = new AudioClip[] { explosion, explosion2 };
		// sound effect
		inGameFont = fontLoader("res/font/Astrobia.ttf", 40);
		inGameFontSmall = fontLoader("res/font/Astrobia.ttf", 22.5);
		// inGameFontSmall =
		// Font.loadFont(ClassLoader.getSystemResource("res/font/Astrobia.ttf").toString(),
		// 22.5);
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

	private static Image imageLoader(String path) throws LoadUnableException {
		try {
			return new Image(ClassLoader.getSystemResource(path).toString());
		} catch (Exception e) {
			throw new LoadUnableException(path);
		}
	}

	private static AudioClip audioLoader(String path) throws LoadUnableException {
		try {
			return new AudioClip(ClassLoader.getSystemResource(path).toExternalForm());
		} catch (Exception e) {
			throw new LoadUnableException(path);
		}
	}

	private static Font fontLoader(String path, double size) throws LoadUnableException {
		try {
			return Font.loadFont(ClassLoader.getSystemResource(path).toString(), size);
		} catch (Exception e) {
			throw new LoadUnableException(path);
		}
	}
}
