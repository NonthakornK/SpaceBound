package logic;

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
	private GameLogic gameLogic;

	public ESemiBoss(GameLogic gameLogic) {
		super(2000, 0.1);
		this.originalHp = 2000;
		this.width = RenderableHolder.eSemiBoss.getWidth();
		this.height = RenderableHolder.eSemiBoss.getHeight();
		yOffset = 0;
		this.visible = true;
		this.destroyed = false;
		this.x = (SceneManager.SCENE_WIDTH - this.width) / 2.0;
		this.y = -this.height;
		this.collideDamage = 1000;
		this.gameLogic = gameLogic;
		
		GameLogic.isSemiAlive = true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		yOffset += this.speed;
		long now = System.nanoTime();
		this.x = Math.sin(2 * now * 1e-9) * ((SceneManager.SCENE_WIDTH - this.width) / 2)
				+ (SceneManager.SCENE_WIDTH - this.width) / 2.0;
		this.y = Math.cos(2 * now * 1e-9) * (200) + yOffset - 200 - this.height - this.speed;
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
		gc.fillRect(this.x + this.width / 2 - this.width / 4 * percentHp, this.y - 20, this.width / 2 * percentHp, 10);

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

}
