package logic;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;

import input.CharacterInput;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import renderer.IRenderable;
import renderer.RenderableHolder;
import window.SceneManager;

@SuppressWarnings("restriction")
public class Player extends Unit implements IRenderable {
	private Image playerImage = null;
	GameLogic gameLogic;
	private int bulletDelayTick = 0, prevbulletTick = 0;
	private double maxHp;
	private int maxShield;
	private double shield;
	private int shieldLvl;
	private int regenLvl;
	private long regenTimeOut = 0;	
	private boolean isDamaged;
	private boolean collided;
	private long TripleFireTimeOut = 0;
	private int powerAttack = 0;
	private int fireMode = 0;
	private boolean fullShield;
	private final double shieldReduction = 0.35;
    public static int atkLvl;
	
	public Player(GameLogic gameLogic) {
		// TODO Auto-generated constructor stub
		super(2500, 6);
		this.maxHp = this.hp;
		maxShield = 1200;
		shield = maxShield;
		shieldLvl = 1;
		regenLvl = 1;
		atkLvl = 1;
		isDamaged = false;

		this.z = 0;

		playerImage = RenderableHolder.dragon;

		this.gameLogic = gameLogic;
		
		
		if (playerImage != null) {
			this.width = playerImage.getWidth();
			this.height = playerImage.getHeight();
			// System.out.println(imageWidth + " " + imageHeight);
			this.x = SceneManager.SCENE_WIDTH / 2 - this.width / 2;
			this.y = (SceneManager.SCENE_HEIGHT - this.height) - 60;
			// this.speed = 3;
			this.side = 1;
			this.collideDamage = 15; // test
		} else {
			width = 0;
			height = 0;
		}
	}

	private void drawHpBar(GraphicsContext gc) {
		double percentHp = this.hp / this.maxHp;
		if (percentHp >= 0.65) {
			// gc.setFill(Color.LAWNGREEN);
			LinearGradient linearGrad = new LinearGradient(0, // start X
					0, // start Y
					0, // end X
					1, // end Y
					true, // proportional
					CycleMethod.NO_CYCLE, // cycle colors
					// stops
					new Stop(0.1f, Color.LAWNGREEN), new Stop(1.0f, Color.GREEN));
			gc.setFill(linearGrad);
		} else if (percentHp >= 0.25) {
			LinearGradient linearGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
					// stops
					new Stop(0.1f, Color.YELLOW), new Stop(1.0f, Color.RED));
			gc.setFill(linearGrad);
		} else {
			LinearGradient linearGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
					// stops
					new Stop(0.1f, Color.RED), new Stop(1.0f, Color.BLACK));
			gc.setFill(linearGrad);
		}
		//gc.fillRect(SceneManager.SCENE_WIDTH / 2 - 200 * percentHp, 750, 2 * 200 * percentHp, 20);
		gc.fillRect(SceneManager.SCENE_WIDTH / 2 - 200, 730, 400 * percentHp, 20);
		
		gc.setFill(Color.WHITE);
		gc.fillRect(SceneManager.SCENE_WIDTH / 2 - 202, 730, 2, 20);
		gc.fillRect(SceneManager.SCENE_WIDTH / 2 + 200, 730, 2, 20);

	}

	private void drawShieldBar(GraphicsContext gc) {
		double percentShield = this.shield / this.maxShield;
		if (percentShield >= 0.65) {
			// gc.setFill(Color.LAWNGREEN);
			LinearGradient linearGrad = new LinearGradient(0, // start X
					0, // start Y
					0, // end X
					1, // end Y
					true, // proportional
					CycleMethod.NO_CYCLE, // cycle colors
					// stops
					new Stop(0.1f, Color.ROYALBLUE), new Stop(1.0f, Color.BLUE));
			gc.setFill(linearGrad);
		} else if (percentShield >= 0.25) {
			LinearGradient linearGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
					// stops
					new Stop(0.1f, Color.DEEPSKYBLUE), new Stop(1.0f, Color.DODGERBLUE));
			gc.setFill(linearGrad);
		} else {
			LinearGradient linearGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
					// stops
					new Stop(0.1f, Color.LIGHTBLUE), new Stop(1.0f, Color.STEELBLUE));
			gc.setFill(linearGrad);
		}
		//gc.fillRect(SceneManager.SCENE_WIDTH / 2 - 200 * percentShield, 775, 2 * 200 * percentShield, 20);
		gc.fillRect(SceneManager.SCENE_WIDTH / 2 - 200, 760, 400 * percentShield, 20);
		
		gc.setFill(Color.WHITE);
		gc.fillRect(SceneManager.SCENE_WIDTH / 2 - 202, 760, 2, 20);
		gc.fillRect(SceneManager.SCENE_WIDTH / 2 + 200, 760, 2, 20);

	}

	private void drawItemsStatus(GraphicsContext gc) {
		gc.setFont(RenderableHolder.inGameFontSmall);
		FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
		
		gc.setFill(Color.RED);
		String atkLevelDisplay = "Firepower Level : " + Integer.toString(Player.atkLvl);
		double atkLevelDisplay_height = fontLoader.getFontMetrics(RenderableHolder.inGameFontSmall)
				.getLineHeight();
		gc.fillText(atkLevelDisplay, 10, 10 + atkLevelDisplay_height);
		
		gc.setFill(Color.DODGERBLUE);
		String shieldLevelDisplay = "Shield Level : " + Integer.toString(this.shieldLvl);
		double shieldLevelDisplay_height = fontLoader.getFontMetrics(RenderableHolder.inGameFontSmall)
				.getLineHeight();
		gc.fillText(shieldLevelDisplay, 10, 30 + shieldLevelDisplay_height);
		
		gc.setFill(Color.SKYBLUE);
		String regenLevelDisplay = "Regen Level : " + Integer.toString(this.regenLvl);
		double regenLevelDisplay_height = fontLoader.getFontMetrics(RenderableHolder.inGameFontSmall)
				.getLineHeight();
		gc.fillText(regenLevelDisplay, 10, 50 + regenLevelDisplay_height);
		
		gc.setFill(Color.GREENYELLOW);
		if (powerAttack > 0 && fireMode == 1) {
			String remainPowerAttack = "Power Attack: " + Integer.toString(this.powerAttack);
			double remainPowerAttack_height = fontLoader.getFontMetrics(RenderableHolder.inGameFontSmall)
					.getLineHeight();
			gc.fillText(remainPowerAttack, 10, 70 + remainPowerAttack_height);

			String TripleFire = "Triple Fire: "
					+ Long.toString((this.TripleFireTimeOut - System.nanoTime()) / 1000000000);
			double TripleFire_height = fontLoader.getFontMetrics(RenderableHolder.inGameFontSmall).getLineHeight();
			gc.fillText(TripleFire, 10, 70 + remainPowerAttack_height + TripleFire_height);
		} else if (powerAttack > 0) {
			String remainPowerAttack = "Power Attack: " + Integer.toString(this.powerAttack);
			double remainPowerAttack_height = fontLoader.getFontMetrics(RenderableHolder.inGameFontSmall)
					.getLineHeight();
			gc.fillText(remainPowerAttack, 10, 70 + remainPowerAttack_height);
		} else if (fireMode == 1) {
			String TripleFire = "Triple Fire: "
					+ Long.toString((this.TripleFireTimeOut - System.nanoTime()) / 1000000000);
			double TripleFire_height = fontLoader.getFontMetrics(RenderableHolder.inGameFontSmall).getLineHeight();
			gc.fillText(TripleFire, 10, 70 + TripleFire_height);
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(playerImage, x, y);
		drawHpBar(gc);
		drawShieldBar(gc);
		drawItemsStatus(gc);
		
		if(collided) {
			Image spark = RenderableHolder.sparkArr[ThreadLocalRandom.current().nextInt(0,3)];
			gc.drawImage(spark, x + 7, y);
			collided = false;
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (CharacterInput.getKeyPressed(KeyCode.UP)) {
			// System.out.println("GO uppppp");
			forward(true);
		}
		if (CharacterInput.getKeyPressed(KeyCode.DOWN)) {
			// System.out.println("GO DOWNNN");
			forward(false);
		}
		if (CharacterInput.getKeyPressed(KeyCode.RIGHT)) {
			// System.out.println("GO RIGHt");
			turn(true);
		}
		if (CharacterInput.getKeyPressed(KeyCode.LEFT)) {
			// System.out.println("GO Left");
			turn(false);
		}
		if (CharacterInput.getTriggeredCtrl().poll() == KeyCode.CONTROL) {
			

			if (this.powerAttack > 0) {
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 0, 40, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 0, -40, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 12, 0, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, -12, 0, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 10, 20, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, -10, -20, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 10, -20, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, -10, 20, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 6, 35, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, -6, 35, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, 6, -35, 1, 6, this));
				gameLogic.addPendingBullet(new Bullet(x, y + this.height, -6, -35, 1, 6, this));
				RenderableHolder.powerAttackLaunch.play();
				
				powerAttack--;

			}
		}
		
		if (CharacterInput.getKeyPressed(KeyCode.SPACE)) {
			// shoot a bullet
			// to be further discussed
			
			if (bulletDelayTick - prevbulletTick > 7) {
				// System.out.println("SHOOOOT");
				if (fireMode == 0) {
					gameLogic.addPendingBullet(new Bullet(x, y, 0, 20, 1, 0, this));
					RenderableHolder.laser.play();
				} else if (fireMode == 1) {
					gameLogic.addPendingBullet(new Bullet(x, y, 0, 20, 1, 0, this));
					RenderableHolder.laser.play();
					gameLogic.addPendingBullet(new Bullet(x - 20, y, 1, 20, 1, 0, this));
					RenderableHolder.laser.play();
					gameLogic.addPendingBullet(new Bullet(x + 20, y, -1, 20, 1, 0, this));
					RenderableHolder.laser.play();
				}
				prevbulletTick = bulletDelayTick;
			}

		}
		bulletDelayTick++;
		if (this.TripleFireTimeOut <= System.nanoTime()) {
			this.TripleFireTimeOut = 0;
			fireMode = 0;
		}

		if (isDamaged) {
			if (this.regenTimeOut <= System.nanoTime()) {
				this.isDamaged = false;
			}
		} else if (!fullShield) {
			this.shield += (this.regenLvl + 5) * this.maxShield / 2000;
			if (this.shield > this.maxShield) {
				this.shield = this.maxShield;
				this.fullShield = true;
			}
		}

	}

	@Override
	public void onCollision(Unit other) {
		// TODO Auto-generated method stub
		// this.hp -= other.collideDamage;

		if (other instanceof Enemy || other instanceof Bullet) {
			double damageReduced;
			if (other instanceof Enemy) {
				damageReduced = other.collideDamage * this.shieldReduction;
			} else {
				damageReduced = other.collideDamage;
			}
			if (damageReduced > this.shield) {
				damageReduced = this.shield;
				this.shield = 0;
			} else {
				this.shield -= damageReduced;
			}
			this.hp -= (other.collideDamage - damageReduced);
			this.isDamaged = true;
			this.fullShield = false;
			this.regenTimeOut = System.nanoTime() + (35 - this.regenLvl * 3) * 50000000l;
			this.collided = true;
		}

		if (other instanceof HPBox) {
			this.hp += ((HPBox) other).getHPStorage();
			if (this.hp > this.maxHp) {
				this.hp = this.maxHp;
			}
		}
		if (other instanceof TripleFireBox) {
			this.fireMode = 1;
			this.TripleFireTimeOut = System.nanoTime() + 12000000000l; // 10 seconds timeout
		}
		if (other instanceof PowerAttackBox) {
			powerAttack++;
		}
		if (other instanceof ShieldMaxBox) {
			this.maxShield += ((ShieldMaxBox) other).getShieldStorage();
			this.shield += ((ShieldMaxBox) other).getShieldStorage();
			shieldLvl++;
			if(shieldLvl > 5) {
				shieldLvl = 5;
			}
		}
		if (other instanceof ShieldRegenBox) {
			regenLvl++;
			if(regenLvl > 5) {
				regenLvl = 5;
			}
		}
		if (other instanceof AttackBox) {
			atkLvl++;
			if(atkLvl > 5) {
				atkLvl = 5;
			}
		}
		// to be further discussed (sound effect etc)
		if (this.hp <= 0) {
			this.destroyed = true;
			this.visible = false;
			Explosion e = new Explosion(x, y, width, height, z);
			e.playSfx();
			RenderableHolder.getInstance().add(e);
		}

	}

	private void forward(boolean b) {
		if (b == true) { // move forward
			if (this.y - speed >= 0) {
				this.y -= speed;
			}
		}
		if (b == false) { // move backward
			if (this.y + speed + this.height <= SceneManager.SCENE_HEIGHT) {
				this.y += speed;
			}
		}
	}

	private void turn(boolean b) {
		if (b == true) { // move right
			if (this.x + speed + this.width <= SceneManager.SCENE_WIDTH) {
				this.x += speed;
			}
		}
		if (b == false) { // move left
			if (this.x - speed >= 0) {
				this.x -= speed;
			}
		}
	}

	@Override
	public Shape getBoundary() {
		// TODO Auto-generated method stub
		Circle bound = new Circle();
		bound.setCenterX(x + width / 2);
		bound.setCenterY(y + height / 2);
		bound.setRadius(width / 4);
		return bound;
	}
}
