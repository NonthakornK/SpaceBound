package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import renderer.RenderableHolder;
import window.SceneManager;

public class EGhost extends Enemy {
	private int bulletDelayTick = 0;
	private GameLogic gameLogic;
	private double startingX;
	public static final double weight = 2.5;

	public EGhost(GameLogic gameLogic, double x) {
		super(250, 1);
		// TODO Auto-generated constructor stub
		this.width = RenderableHolder.eGhost.getWidth();
		this.height = RenderableHolder.eGhost.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = x;
		this.startingX = x;
		this.y = -this.height;
		this.collideDamage = 15;
		this.gameLogic = gameLogic;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.eGhost, x, y);
	}

	@Override
	public Shape getBoundary() {
		// TODO Auto-generated method stub
		Rectangle bound = new Rectangle();
		bound.setX(x);
		bound.setY(y);
		bound.setWidth(width);
		bound.setHeight(height);
		return bound;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		long now = System.nanoTime();
		this.x = Math.sin(5 * now * 2.5e-10 + Math.toRadians(90) + startingX/SceneManager.SCENE_WIDTH * 180) * ((SceneManager.SCENE_WIDTH - this.width) / 2)
				+ (SceneManager.SCENE_WIDTH - this.width) / 2.0;
		y += this.speed;
		if (this.isOutOfScreen()) {
			this.visible = false;
			this.destroyed = true;
		}
		if (bulletDelayTick % 20 == 0) {
			gameLogic.addPendingBullet(new Bullet(x, y - this.height / 2, -5, 10, -1, 2, this));
			gameLogic.addPendingBullet(new Bullet(x, y - this.height / 2, 5, 10, -1, 2, this));
			RenderableHolder.fireBall.play();
		}
		bulletDelayTick++;
	}

	public double getWeight() {
		return weight;
	}

}
