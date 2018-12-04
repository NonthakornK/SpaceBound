package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import renderer.RenderableHolder;
import window.SceneManager;

public class EBoss extends Enemy {

	private int originalHp;
	private GameLogic gameLogic;
	private int bulletDelayTick = 0;
	public static final double weight = 8;

	public EBoss(GameLogic gameLogic) {
		super(4500, 0.1);
		this.originalHp = 4000;
		this.width = RenderableHolder.eBoss.getWidth();
		this.height = RenderableHolder.eBoss.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = (SceneManager.SCENE_WIDTH - this.width) / 2.0;
		this.y = -this.height;
		this.collideDamage = 1000;
		this.gameLogic = gameLogic;

		GameLogic.isBossAlive = true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		
		long now = System.nanoTime();
		this.x = Math.sin(5 * now * 8e-10 + Math.toRadians(90)) * ((SceneManager.SCENE_WIDTH - this.width) / 2)
				+ (SceneManager.SCENE_WIDTH - this.width) / 2.0;
		if (this.y < 40) {
			this.y += this.speed;
		}
		if (this.isOutOfScreen()) {
			this.visible = false;
			this.destroyed = true;
			GameLogic.isBossAlive = false;
			GameLogic.killedBoss = true;
		}
		if (bulletDelayTick % 30 == 0) {
			gameLogic.addPendingBullet(new Bullet(x, y, 0, 20, -1, 1, this));
			RenderableHolder.fireBall.play();
		}
		bulletDelayTick++;
		
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.eBoss, x, y);
		drawHpBar(gc);
	}
	

	private void drawHpBar(GraphicsContext gc) {
		double percentHp = this.hp / this.originalHp;
		gc.setFill(Color.RED);
		gc.fillRect(this.x, this.y + this.height + 20, this.width * percentHp, 10);

	}

	@Override
	public Shape getBoundary() {
		// TODO Auto-generated method stub
		Circle bound = new Circle();
		bound.setCenterX(x + width / 2);
		bound.setCenterY(y + height / 2);
		bound.setRadius(width / 2);
		return bound;
	}

	public double getWeight() {
		return weight;
	}

}
