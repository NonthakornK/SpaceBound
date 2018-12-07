package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

import drawing.GameScreen;
import game.GameMain;
import javafx.scene.image.Image;
import renderer.RenderableHolder;
import window.SceneManager;

public class GameLogic {

	private Queue<Bullet> pendingBullet;

	private List<Unit> gameObjectContainer;
	private static final int FPS = 60;
	public static final long LOOP_TIME = 1000000000 / FPS;

	private int gameOverCountdown = 24;
	private int gameWinCountdown = 24;

	private double maxEnemyCap;
	public static double currentEnemyWeight;
	public static boolean isBossAlive;
	public static boolean isSemiAlive;
	public static boolean killedBoss;
	public static boolean killedSemi;
	private int stageLevel;

	private long nextItemsSpawnTime;
	public static long relaxTime;

	private GameScreen canvas;
	private boolean isGameRunning;

	private Player player;
	private ESemiBoss esemi;
	private EBoss eboss;

	public GameLogic(GameScreen canvas) {
		this.gameObjectContainer = new ArrayList<Unit>();
		this.maxEnemyCap = 6; // default enemy capacity
		GameLogic.currentEnemyWeight = 0;
		stageLevel = 1;
		GameLogic.isBossAlive = false;
		GameLogic.isSemiAlive = false;
		killedBoss = false;
		killedSemi = false;

		RenderableHolder.getInstance().add(new Background());
		RenderableHolder.getInstance().add(new Score());
		player = new Player(this);
		addNewObject(player);

		spawnEnemy();

		this.canvas = canvas;
		nextItemsSpawnTime = System.nanoTime() + ThreadLocalRandom.current().nextLong(5000000000l, 15000000000l);
		pendingBullet = new ConcurrentLinkedQueue<>();

	}

	protected void addNewObject(Unit unit) {
		gameObjectContainer.add(unit);
		RenderableHolder.getInstance().add(unit);
	}

	protected void winGame() {
		this.isGameRunning = false;
		this.gameObjectContainer.clear();
		this.pendingBullet.clear();
	}

	public void startGame() {
		this.isGameRunning = true;
		new Thread(this::gameLoop, "Game Loop Thread").start();
	}

	public void stopGame() {
		this.isGameRunning = false;
		this.gameObjectContainer.clear();
		this.pendingBullet.clear();

	}

	private void gameLoop() {
		long lastLoopStartTime = System.nanoTime();
		GameLogic.relaxTime = System.nanoTime() + 6000000000l;
		GameLogic.currentEnemyWeight += 10.8;
		while (isGameRunning) {
			long elapsedTime = System.nanoTime() - lastLoopStartTime;
			if (elapsedTime >= LOOP_TIME) {
				lastLoopStartTime += LOOP_TIME;
				updateGame();
			}
		}
	}

	private void updateGame() {
		// TODO fill code

		if (killedSemi) {
			GameLogic.relaxTime = System.nanoTime() + 12000000000l;
			GameLogic.currentEnemyWeight += 21.6;

			nextItemsSpawnTime = System.nanoTime() + 11000000000l;
			
			addNewObject(new ShieldMaxBox((SceneManager.SCENE_WIDTH - RenderableHolder.shieldmax.getWidth()) / 2 - 100));
			addNewObject(new AttackBox((SceneManager.SCENE_WIDTH - RenderableHolder.attackBox.getWidth())/2));
			addNewObject(new ShieldRegenBox((SceneManager.SCENE_WIDTH - RenderableHolder.shieldregen.getWidth()) / 2 + 100));
			
			killedSemi = false;
		}

		if (relaxTime >= System.nanoTime()) {
			GameLogic.currentEnemyWeight -= 0.03;
		}
		spawnEnemy();
		spawnItems();

		while (!pendingBullet.isEmpty()) {
			gameObjectContainer.add(pendingBullet.poll());

		}

		for (Unit i : gameObjectContainer) {
			i.update();
		}
		for (Unit i : gameObjectContainer) {
			for (Unit j : gameObjectContainer) {
				if (i != j && ((Unit) i).collideWith((Unit) j)) {
					((Unit) i).onCollision((Unit) j);
				}
			}
		}
		int i = 0;
		while (i < gameObjectContainer.size()) {
			if (gameObjectContainer.get(i).isDestroyed()) {
				gameObjectContainer.remove(i);
			} else {
				i++;
			}
		}
		if (player.isDestroyed()) {
			gameOverCountdown--;
		}
		if (killedBoss) {
			gameWinCountdown--;
		}
		if (gameWinCountdown == 0) {
			GameMain.winGame();
		}
		if (gameOverCountdown == 0) {
			GameMain.loseGame();
		}

		double mod = Score.distance / 500;
		Score.hiddenDistance += 0.5 + mod / 4;
		Score.distance = (int) Score.hiddenDistance;

	}

	public void addPendingBullet(Bullet a) {
		pendingBullet.add(a);
		canvas.addPendingBullet(a);
	}

	private void spawnEnemy() {
		Random r = new Random();
		this.maxEnemyCap = 6 + stageLevel * 0.85;

		if (Score.distance >= 5000 && !isSemiAlive) {
			esemi = new ESemiBoss(this);
			addNewObject(esemi);
			GameLogic.currentEnemyWeight += esemi.getWeight();
		}
		if (Score.distance >= 50000 && !isBossAlive) {
			eboss = new EBoss(this);
			addNewObject(eboss);
			GameLogic.currentEnemyWeight += eboss.getWeight();
		}

		if (Score.distance >= 800 * stageLevel * stageLevel) {
			stageLevel++;
		}

		if (GameLogic.currentEnemyWeight < this.maxEnemyCap) {
			int chance = r.nextInt(100) - 10000 / (Score.distance + 1); 
			
			if (chance < 40) {
				Image variation = RenderableHolder.asteroidArr[ThreadLocalRandom.current().nextInt(0, 7)];
				Asteroid asteroid = new Asteroid(
						ThreadLocalRandom.current().nextDouble(SceneManager.SCENE_WIDTH - variation.getWidth()),
						variation);
				addNewObject(asteroid);
				GameLogic.currentEnemyWeight += asteroid.getWeight();
			} else if (chance < 60) {
				ELight elight = new ELight(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eLight.getWidth()));
				addNewObject(elight);
				GameLogic.currentEnemyWeight += elight.getWeight();
			} 
			else if (chance < 75) {
				EJet ejet = new EJet(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eJet.getWidth()));
				addNewObject(ejet);
				GameLogic.currentEnemyWeight += ejet.getWeight();
			}else if (chance < 90) {
				EScout escout = new EScout(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eScout.getWidth()));
				addNewObject(escout);
				GameLogic.currentEnemyWeight += escout.getWeight();
			} else {
				EHeavy eheavy = new EHeavy(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eHeavy.getWidth())); 
				addNewObject(eheavy);
				GameLogic.currentEnemyWeight += eheavy.getWeight();
			}

		}

	}

	private void spawnItems() {
		long now = System.nanoTime();
		if (this.nextItemsSpawnTime <= now) {
			this.nextItemsSpawnTime = now + ThreadLocalRandom.current().nextLong(8000000000l, 11000000000l);

			double rand = ThreadLocalRandom.current().nextDouble(100);
			if (rand <= 10) {
				addNewObject(new AttackBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.attackBox.getWidth())));
			} else if (rand <= 30) {
				addNewObject(new TripleFireBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.triplefirebox.getWidth())));
			} else if (rand <= 50) {
				addNewObject(new PowerAttackBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.powerattackBox.getWidth())));
			} else if (rand <= 60) {
				addNewObject(new ShieldMaxBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.shieldmax.getWidth())));
			} else if (rand <= 70) {
				addNewObject(new ShieldRegenBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.shieldregen.getWidth())));
			} else {
				addNewObject(new HPBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.healthpack.getWidth())));
			}
		}

	}
}
