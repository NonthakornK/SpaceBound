package logic;

import java.util.Random;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;

import input.CharacterInput;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import renderer.IRenderable;
import renderer.RenderableHolder;
import window.SceneManager;

public class Player extends Unit implements IRenderable {
	private Image playerImage = null;
	GameLogic gameLogic;
	private int bulletDelayTick = 0, prevbulletTick = 0;
	private double maxHp;
	private int maxShield;
	private double shield;
	private int shieldLvl;
	private double shieldRegen;
	private int regenLvl;
	private long regenTimeOut = 0;
	private boolean isDamaged;
	private long TripleFireTimeOut = 0;
	private int powerAttack = 0;
	private int fireMode = 0;
	private boolean fullShield;
	private final double shieldReduction = 0.35;

	public Player(GameLogic gameLogic) {
		// TODO Auto-generated constructor stub
		super(2500, 6);
		this.maxHp = this.hp;
		maxShield = 750;
		shield = maxShield;
		shieldRegen = 1.5;
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
			this.collideDamage = 10; // test
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
		gc.setFill(Color.GREENYELLOW);
		FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
		if (powerAttack > 0 && fireMode == 1) {
			String remainPowerAttack = "Power Attack: " + Integer.toString(this.powerAttack);
			double remainPowerAttack_height = fontLoader.getFontMetrics(RenderableHolder.inGameFontSmall)
					.getLineHeight();
			gc.fillText(remainPowerAttack, 10, 10 + remainPowerAttack_height);

			String TripleFire = "Triple Fire: "
					+ Long.toString((this.TripleFireTimeOut - System.nanoTime()) / 1000000000);
			double TripleFire_height = fontLoader.getFontMetrics(RenderableHolder.inGameFontSmall).getLineHeight();
			gc.fillText(TripleFire, 10, 20 + remainPowerAttack_height + TripleFire_height);
		} else if (powerAttack > 0) {
			String remainPowerAttack = "Power Attack: " + Integer.toString(this.powerAttack);
			double remainPowerAttack_height = fontLoader.getFontMetrics(RenderableHolder.inGameFontSmall)
					.getLineHeight();
			gc.fillText(remainPowerAttack, 10, 10 + remainPowerAttack_height);
		} else if (fireMode == 1) {
			String TripleFire = "Triple Fire: "
					+ Long.toString((this.TripleFireTimeOut - System.nanoTime()) / 1000000000);
			double TripleFire_height = fontLoader.getFontMetrics(RenderableHolder.inGameFontSmall).getLineHeight();
			gc.fillText(TripleFire, 10, 10 + TripleFire_height);
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(playerImage, x, y);
		drawHpBar(gc);
		drawShieldBar(gc);
		drawItemsStatus(gc);

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
				gameLogic.addPendingBullet(new Bullet(x, y, 0, 30, 1, 6, this));
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
					RenderableHolder.fireBall.play();
				} else if (fireMode == 1) {
					gameLogic.addPendingBullet(new Bullet(x, y, 0, 20, 1, 0, this));
					RenderableHolder.fireBall.play();
					gameLogic.addPendingBullet(new Bullet(x - 20, y, 0, 20, 1, 0, this));
					RenderableHolder.fireBall.play();
					gameLogic.addPendingBullet(new Bullet(x + 20, y, 0, 20, 1, 0, this));
					RenderableHolder.fireBall.play();
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
			this.shield += this.shieldRegen;
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
			this.regenTimeOut = System.nanoTime() + 3000000000l;
		}

		if (other instanceof HPBox) {
			this.hp += ((HPBox) other).getHPStorage();
			if (this.hp > this.maxHp) {
				this.hp = this.maxHp;
			}
		}
		if (other instanceof TripleFireBox) {
			this.fireMode = 1;
			this.TripleFireTimeOut = System.nanoTime() + 10000000000l; // 10 seconds timeout
		}
		if (other instanceof PowerAttackBox) {
			powerAttack++;
		}
		if (other instanceof ShieldMaxBox) {
			this.maxShield += ((ShieldMaxBox) other).getShieldStorage();
		}
		if (other instanceof ShieldRegenBox) {
			this.shieldRegen += ((ShieldRegenBox) other).getRegenStorage();
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

	public boolean isDamaged() {
		return isDamaged;
	}

	public double getShield() {
		return shield;
	}

	public void setShield(int shield) {
		this.shield = shield;
	}

	public double getShieldRegen() {
		return shieldRegen;
	}

	public void setShieldRegen(double shieldRegen) {
		this.shieldRegen = shieldRegen;
	}

	public int getMaxShield() {
		return maxShield;
	}

	public void setMaxShield(int maxShield) {
		this.maxShield = maxShield;
	}

}