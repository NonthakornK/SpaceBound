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
import sharedObject.RenderableHolder;
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
		RenderableHolder.getInstance().add(new Distance());
		player = new Player(this);
		addNewObject(player);

		spawnEnemy();

		this.canvas = canvas;
		nextItemsSpawnTime = System.nanoTime() + ThreadLocalRandom.current().nextLong(8000000000l, 10000000000l);
		pendingBullet = new ConcurrentLinkedQueue<>();

	}

	protected void addNewObject(Unit unit) {
		if(unit instanceof Enemy) {
			GameLogic.currentEnemyWeight += ((Enemy) unit).getWeight();
		}
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
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateGame() {
		// TODO fill code

		if (killedSemi) {
			GameLogic.relaxTime = System.nanoTime() + 12000000000l;
			GameLogic.currentEnemyWeight += 21.6;

			nextItemsSpawnTime = System.nanoTime() + 11000000000l;
			
			addNewObject(new IShieldMaxBox((SceneManager.SCENE_WIDTH - RenderableHolder.shieldmax.getWidth()) / 2 - 100));
			addNewObject(new IAttackBox((SceneManager.SCENE_WIDTH - RenderableHolder.attackBox.getWidth())/2));
			addNewObject(new IShieldRegenBox((SceneManager.SCENE_WIDTH - RenderableHolder.shieldregen.getWidth()) / 2 + 100));
			
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

		double mod = Distance.distance / 500;
		Distance.hiddenDistance += 0.5 + mod / 4;
		Distance.distance = (int) Distance.hiddenDistance;

	}

	public void addPendingBullet(Bullet a) {
		pendingBullet.add(a);
		canvas.addPendingBullet(a);
	}

	private void spawnEnemy() {
		Random r = new Random();
		this.maxEnemyCap = 6 + stageLevel * 0.85;

		if (Distance.distance >= 5000 && !isSemiAlive) {
			esemi = new ESemiBoss(this);
			addNewObject(esemi);
			GameLogic.currentEnemyWeight += esemi.getWeight();
		}
		if (Distance.distance >= 50000 && !isBossAlive) {
			eboss = new EBoss(this);
			addNewObject(eboss);
			GameLogic.currentEnemyWeight += eboss.getWeight();
		}

		if (Distance.distance >= 800 * stageLevel * stageLevel) {
			stageLevel++;
		}

		if (GameLogic.currentEnemyWeight < this.maxEnemyCap) {
			int chance = r.nextInt(100) - 10000 / (Distance.distance + 1); 
			
			if (chance < 40) {
				Image variation = RenderableHolder.asteroidArr[ThreadLocalRandom.current().nextInt(0, 7)];
				EAsteroid easteroid = new EAsteroid(
						ThreadLocalRandom.current().nextDouble(SceneManager.SCENE_WIDTH - variation.getWidth()),
						variation);
				addNewObject(easteroid);
			} else if (chance < 60) {
				ELight elight = new ELight(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eLight.getWidth()));
				addNewObject(elight);
			} 
			else if (chance < 75) {
				EJet ejet = new EJet(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eJet.getWidth()));
				addNewObject(ejet);
			}else if (chance < 90) {
				EScout escout = new EScout(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eScout.getWidth()));
				addNewObject(escout);
			} else {
				EHeavy eheavy = new EHeavy(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eHeavy.getWidth())); 
				addNewObject(eheavy);
			}

		}

	}

	private void spawnItems() {
		long now = System.nanoTime();
		if (this.nextItemsSpawnTime <= now) {
			this.nextItemsSpawnTime = now + ThreadLocalRandom.current().nextLong(8000000000l, 11000000000l);

			double rand = ThreadLocalRandom.current().nextDouble(100);
			if (rand <= 10) {
				addNewObject(new IAttackBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.attackBox.getWidth())));
			} else if (rand <= 30) {
				addNewObject(new ITripleFireBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.triplefirebox.getWidth())));
			} else if (rand <= 50) {
				addNewObject(new IPowerAttackBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.powerattackBox.getWidth())));
			} else if (rand <= 60) {
				addNewObject(new IShieldMaxBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.shieldmax.getWidth())));
			} else if (rand <= 70) {
				addNewObject(new IShieldRegenBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.shieldregen.getWidth())));
			} else {
				addNewObject(new IHPBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.healthpack.getWidth())));
			}
		}

	}
}
