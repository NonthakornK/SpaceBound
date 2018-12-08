package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import renderer.RenderableHolder;
import window.SceneManager;

public class ESemiBoss extends Enemy {
	private int originalHp;
	private int bulletDelayTick = 0;
	private double yOffset;
	private double xOffset;
	private long moveTime;
	private double yMultiplier;
	private boolean returning;
	private GameLogic gameLogic;
	private long chargeDelay;

	public ESemiBoss(GameLogic gameLogic) {
		super(4000, 0.15);
		this.originalHp = 4000;
		this.width = RenderableHolder.eSemiBoss.getWidth();
		this.height = RenderableHolder.eSemiBoss.getHeight();
		this.yOffset = 0;
		this.xOffset = 0;
		this.yMultiplier = 0.7;
		this.visible = true;
		this.destroyed = false;
		this.x = (SceneManager.SCENE_WIDTH - this.width) / 2.0;
		this.y = -this.height;
		this.collideDamage = 3000;
		this.weight = 7;
		this.gameLogic = gameLogic;
		this.chargeDelay = System.nanoTime() + ThreadLocalRandom.current().nextLong(8000000000l, 10000000000l);
		this.returning = false;
		this.moveTime = System.nanoTime();

		GameLogic.isSemiAlive = true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		long now = System.nanoTime();

		if (now >= this.chargeDelay) {
			if (this.returning) {
				if (this.y >= yOffset) {
					this.y -= 8 * (SceneManager.SCENE_HEIGHT - this.yOffset) / SceneManager.SCENE_HEIGHT;
				} else {
					this.returning = false;
					this.chargeDelay = now + ThreadLocalRandom.current().nextLong(7000000000l, 9000000000l);
				}

			} else if (this.y < SceneManager.SCENE_HEIGHT * this.yMultiplier) {
				this.y += (SceneManager.SCENE_HEIGHT * this.yMultiplier) / Math.cbrt(SceneManager.SCENE_HEIGHT);
			} else {
				this.returning = true;
			}
		} else if (now >= this.chargeDelay - 1000000000l) {
			this.y -= 10 * (SceneManager.SCENE_HEIGHT - this.yOffset) / SceneManager.SCENE_HEIGHT;
			
		} else {
			this.moveTime += GameLogic.LOOP_TIME;
			this.x = Math.sin(3.5 * (this.moveTime) * 1e-9 + Math.toRadians(90)) * ((SceneManager.SCENE_WIDTH - this.width) / 2)
					+ (SceneManager.SCENE_WIDTH - this.width) / 2.0;
			this.y += this.speed;
			this.yOffset = this.y;
			this.xOffset = this.x;
		}


		if (this.isOutOfScreen()) {
			this.visible = false;
			this.destroyed = true;
		}
		if(now < this.chargeDelay - 1000000000l) {
			if (bulletDelayTick % 35 == 0) {
				gameLogic.addPendingBullet(new Bullet(x, y, 0, 15, -1, 4, this));
				gameLogic.addPendingBullet(new Bullet(x - 50, y - 20, 9, 15, -1, 4, this));
				gameLogic.addPendingBullet(new Bullet(x + 50, y - 20, -9, 15, -1, 4, this));
				gameLogic.addPendingBullet(new Bullet(x - 15, y, 5, 15, -1, 4, this));
				gameLogic.addPendingBullet(new Bullet(x + 15, y, -5, 15, -1, 4, this));
				RenderableHolder.fireBall.play();
			}
			bulletDelayTick++;
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.eSemiBoss, x, y);
		drawHpBar(gc);
		if(collided) {
			Image spark = RenderableHolder.sparkArr[ThreadLocalRandom.current().nextInt(0,4)];
			gc.drawImage(spark, x + this.width/10, y + this.height * 0.32, this.width * 0.8, this.height * 0.8);
			collided = false;
		}
	}

	private void drawHpBar(GraphicsContext gc) {
		double percentHp = this.hp / this.originalHp;
		gc.setFill(Color.RED);
		gc.fillRect(this.x + this.width / 5, this.y + this.height + 20, this.width * percentHp * 0.6, 10);

	}

	@Override
	public Shape getBoundary() {
		// TODO Auto-generated method stub
		Circle bound = new Circle();
		bound.setCenterX(x + width / 2);
		bound.setCenterY(y + width / 2);
		bound.setRadius(width / 2);
		return bound;
	}

	public double getWeight() {
		return weight;
	}

}
