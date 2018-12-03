package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import renderer.RenderableHolder;

public class EMachine extends Enemy {

	private int bulletDelayTick = 0;
	private GameLogic gameLogic;

	public EMachine(GameLogic gameLogic, double x) {
		super(70, 2.5);
		// TODO Auto-generated constructor stub
		this.width = RenderableHolder.eMachine.getWidth();
		this.height = RenderableHolder.eMachine.getHeight();
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
		gc.drawImage(RenderableHolder.eMachine, x, y);
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
		if (bulletDelayTick % 50 == 0) {
			gameLogic.addPendingBullet(new Bullet(x, y, 0, 10, -1, 5, this));
			RenderableHolder.fireBall.play();
		}
		bulletDelayTick++;
	}
}
