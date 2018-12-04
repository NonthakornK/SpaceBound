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
	private int stageLevel;

	private long nextItemsSpawnTime;
	public static long relaxTime;

	private GameScreen canvas;
	private boolean isGameRunning;

	private Player player;
	private ESemiBoss esemi;
	private EBoss eboss;
	private Asteroid asteroid;

	public GameLogic(GameScreen canvas) {
		this.gameObjectContainer = new ArrayList<Unit>();
		this.maxEnemyCap = 6; // default enemy capacity
		GameLogic.currentEnemyWeight = 0;
		stageLevel = 1;
		GameLogic.isBossAlive = false;
		GameLogic.isSemiAlive = false;
		killedBoss=false;
		

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
		GameLogic.relaxTime = System.nanoTime() + 9000000001L;
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
		
		if(relaxTime >= System.nanoTime()) {
			System.out.println(GameLogic.currentEnemyWeight);
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
		if(killedBoss) {
			gameWinCountdown--;
		}
		if (gameWinCountdown == 0) {
			GameMain.winGame();
		}
		if (gameOverCountdown == 0) {
			GameMain.loseGame();
		}
		
		double mod = Score.distance / 500;
		hiddenDistance += 0.5 + mod/4;
		//if(hiddenDistance > 5000) {
			//Score.distance = ((int) hiddenDistance/100)*100;
		//}
		//else {
			Score.distance = (int) hiddenDistance;
		//}
		

	}

	public void addPendingBullet(Bullet a) {

		pendingBullet.add(a);

		canvas.addPendingBullet(a);
	}

	private void spawnEnemy() {
		Random r = new Random();
		this.maxEnemyCap = 6 + stageLevel;
		// check distance to spawn boss first
		//if didn't check it will spawn a lot of boss
		if (Score.distance >= 2000 && !isSemiAlive) {
			esemi = new ESemiBoss(this);
			addNewObject(esemi);
			GameLogic.currentEnemyWeight += 3.5;
		}
		if (Score.distance >= 30000 && !isBossAlive) {
			eboss = new EBoss(this);
			addNewObject(eboss);
			GameLogic.currentEnemyWeight += 5;
		}
		
		if (Score.distance >= 400 * stageLevel * stageLevel) {
			stageLevel++;
		}

		// System.out.println("cap" + this.maxEnemyCap + " current " +
		// this.currentEnemyWeight);

		if (GameLogic.currentEnemyWeight < this.maxEnemyCap) {
			int chance = r.nextInt(100) - 30000 / (Score.distance + 1); // difficulty factor , +1 to prevent zero when start
																	// new game
			// System.out.println(" chance " + chance);
			if (chance < 55) {
				Image variation = RenderableHolder.asteroidArr[ThreadLocalRandom.current().nextInt(0,4)];
				asteroid = new Asteroid(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - variation.getWidth()),variation);
				addNewObject(asteroid);
				GameLogic.currentEnemyWeight += 1;
			} else if (chance < 70) {
				addNewObject(new EMachine(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eGhost.getWidth())));
				GameLogic.currentEnemyWeight += 1.5;
			} else if (chance < 85) {
				addNewObject(new EGhost(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eGhost.getWidth())));
				GameLogic.currentEnemyWeight += 2;
			} else if (chance < 95)  {
				addNewObject(new ETree(this, ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.eGhost.getWidth())));
				GameLogic.currentEnemyWeight += 2.5;
			}
			
		}

	}

	private void spawnItems() {
		long now = System.nanoTime();
		if (this.nextItemsSpawnTime <= now) {
			long rand = ThreadLocalRandom.current().nextLong(5000000000l, 15000000000l); // random the time next Box
																							// will come out
			// System.out.println("\t\tNext Box in " + rand / 1000000000l + " seconds.");
			this.nextItemsSpawnTime = now + rand;

			double gachaPull = ThreadLocalRandom.current().nextDouble(100);
			// System.out.println("\t\tGacha: " + gachaPull);
			if (gachaPull <= 32.5) {
				addNewObject(new TripleFireBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.randomBox.getWidth())));
			} else if (gachaPull <= 55) {
				addNewObject(new PowerAttackBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.randomBox.getWidth())));
			} else {
				addNewObject(new HPBox(ThreadLocalRandom.current()
						.nextDouble(SceneManager.SCENE_WIDTH - RenderableHolder.healthpack.getWidth())));
			}
		}

	}
}