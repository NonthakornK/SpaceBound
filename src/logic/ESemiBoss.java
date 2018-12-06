package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
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
	private double yMultiplier;
	private boolean returning;
	private GameLogic gameLogic;
	private long chargeDelay;

	public ESemiBoss(GameLogic gameLogic) {
		super(3000, 0.2);
		this.originalHp = 3000;
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
		this.weight = 5.5;
		this.gameLogic = gameLogic;
		this.chargeDelay = System.nanoTime() + ThreadLocalRandom.current().nextLong(7000000000l, 10000000000l);
		this.returning = false;

		GameLogic.isSemiAlive = true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		// yOffset += this.speed;
		long now = System.nanoTime();

		if (now >= this.chargeDelay) {
			if (returning) {
				if (this.y >= yOffset) {
					this.y -= 6 * (SceneManager.SCENE_HEIGHT - this.yOffset) / SceneManager.SCENE_HEIGHT;
				} else {
					this.returning = false;
					this.chargeDelay = now + ThreadLocalRandom.current().nextLong(5000000000l, 7000000000l);
				}

			} else if (this.y < SceneManager.SCENE_HEIGHT * this.yMultiplier) {
				this.y += (SceneManager.SCENE_HEIGHT * this.yMultiplier) / Math.cbrt(SceneManager.SCENE_HEIGHT);
			} else {
				this.returning = true;
			}
		} else if (now >= this.chargeDelay - 1300000000l) {
			
		} else {
			this.x = Math.abs(this.x - 5 - SceneManager.SCENE_WIDTH - this.width);
			this.y += this.speed;
			this.yOffset = this.y;
			this.xOffset = this.x;
		}

		// this.y = Math.cos(2 * now * 1e-9) * (200) + yOffset - 200 - this.height -
		// this.speed;

		if (this.isOutOfScreen()) {
			this.visible = false;
			this.destroyed = true;
		}
		if (bulletDelayTick % 20 == 0) {
			gameLogic.addPendingBullet(new Bullet(x - 50, y - 20, 8, 15, -1, 4, this));
			gameLogic.addPendingBullet(new Bullet(x + 50, y - 20, -8, 15, -1, 4, this));
			gameLogic.addPendingBullet(new Bullet(x - 15, y, 4, 15, -1, 4, this));
			gameLogic.addPendingBullet(new Bullet(x + 15, y, -4, 15, -1, 4, this));
			RenderableHolder.fireBall.play();
		}
		bulletDelayTick++;

	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.eSemiBoss, x, y);
		drawHpBar(gc);
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
