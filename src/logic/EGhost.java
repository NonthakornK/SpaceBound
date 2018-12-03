package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import renderer.RenderableHolder;

public class EGhost extends Enemy {
	private int bulletDelayTick = 0;
	private GameLogic gameLogic;

	public EGhost(GameLogic gameLogic, double x) {
		super(120, 1);
		// TODO Auto-generated constructor stub
		this.width = RenderableHolder.eGhost.getWidth();
		this.height = RenderableHolder.eGhost.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = x;
		this.y = -this.height;
		this.collideDamage = 10;
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
		y += this.speed;
		if (this.isOutOfScreen()) {
			this.visible = false;
			this.destroyed = true;
		}
		if (bulletDelayTick % 20 == 0) {
			gameLogic.addPendingBullet(new Bullet(x, y, 0, 10, -1, 2, this));
			RenderableHolder.fireBall.play();
		}
		bulletDelayTick++;
	}

}
