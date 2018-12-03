package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import renderer.RenderableHolder;

public class ETree extends Enemy {
	private int bulletDelayTick = 0;
	private GameLogic gameLogic;

	public ETree(GameLogic gameLogic, double x) {
		super(600, 0.5);
		// TODO Auto-generated constructor stub
		this.width = RenderableHolder.eTree.getWidth();
		this.height = RenderableHolder.eTree.getHeight();
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
		gc.drawImage(RenderableHolder.eTree, x, y);
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
			gameLogic.addPendingBullet(new Bullet(x, y, 5, 10, -1, 3, this));
			gameLogic.addPendingBullet(new Bullet(x, y, -5, 10, -1, 3, this));
			gameLogic.addPendingBullet(new Bullet(x, y, 0, 10, -1, 3, this));
			RenderableHolder.fireBall.play();
		}
		bulletDelayTick++;
	}

}
