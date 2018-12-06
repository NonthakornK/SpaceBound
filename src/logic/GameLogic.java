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
	private static final long LOOP_TIME = 1000000000 / FPS;
	private double hiddenDistance = 0;

	private int gameOverCountdown = 24;
	private int gameWinCountdown = 24;

	private int maxEnemyCap;
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
		GameLogic.relaxTime = System.nanoTime() + 9000000000l;
		GameLogic.currentEnemyWeight += 16.2;
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
			GameLogic.relaxTime = System.nanoTime() + 18000000000l;
			GameLogic.currentEnemyWeight += 32.4;

			nextItemsSpawnTime = System.nanoTime() + 12000000000l;
			
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
		// System.out.println("Number of gameObject\t" + gameObjectContainer.size());

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
		hiddenDistance += 0.5 + mod / 4;
		// if(hiddenDistance > 5000) {
		// Score.distance = ((int) hiddenDistance/100)*100;
		// }
		// else {
		Score.distance = (int) hiddenDistance;
		// }

	}

	public void addPendingBullet(Bullet a) {

		pendingBullet.add(a);

		canvas.addPendingBullet(a);
	}

	private void spawnEnemy() {
		Random r = new Random();
		this.maxEnemyCap = 6 + stageLevel;
		// check distance to spawn boss first
		// if didn't check it will spawn a lot of boss
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

		// System.out.println("cap" + this.maxEnemyCap + " current " +
		// this.currentEnemyWeight);

		if (GameLogic.currentEnemyWeight < this.maxEnemyCap) {
			int chance = r.nextInt(100) - 20000 / (Score.distance + 1); // difficulty factor , +1 to prevent zero when
																		// start
			// new game
			// System.out.println(" chance " + chance);
			if (chance < 40) {
				Image variation = RenderableHolder.asteroidArr[ThreadLocalRandom.current().nextInt(0, 4)];
				Asteroid asteroid = new Asteroid(
						ThreadLocalRandom.current().nextDouble(SceneManager.SCENE_WIDTH - variation.getWidth()),
						variation);
				
				addNewObject(asteroid);
				GameLogic.currentEnemyWeight += asteroid.getWeight();
			} else if (chance < 60) {
				EMachine emachine = new EMachine(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eMachine.getWidth()));
				addNewObject(emachine);
				GameLogic.currentEnemyWeight += emachine.getWeight();
			} 
			else if (chance < 75) {
				EJet ejet = new EJet(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eJet.getWidth()));
				addNewObject(ejet);
				GameLogic.currentEnemyWeight += ejet.getWeight();
			}else if (chance < 90) {
				EGhost eghost = new EGhost(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eGhost.getWidth()));
				addNewObject(eghost);
				GameLogic.currentEnemyWeight += eghost.getWeight();
			} else if (chance < 100) {
				ETree etree = new ETree(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eTree.getWidth())); 
				addNewObject(etree);
				GameLogic.currentEnemyWeight += etree.getWeight();
			}

		}

	}

	private void spawnItems() {
		long now = System.nanoTime();
		if (this.nextItemsSpawnTime <= now) {
			this.nextItemsSpawnTime = now + ThreadLocalRandom.current().nextLong(8000000000l, 12000000000l);

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
