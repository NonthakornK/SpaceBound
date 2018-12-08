package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import sharedObject.RenderableHolder;

public class IPowerAttackBox extends Items {

	public IPowerAttackBox(double x) {
		super(ThreadLocalRandom.current().nextDouble(3, 6));
		this.width = RenderableHolder.powerattackBox.getWidth();
		this.height = RenderableHolder.powerattackBox.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = x;
		this.y = -this.height - ThreadLocalRandom.current().nextDouble(500);
		this.collideDamage = 0;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.powerattackBox, x, y);
	}

	@Override
	public void onCollision(Unit others) {
		// TODO Auto-generated method stub
		this.hp = 0;
		this.destroyed = true;
		this.visible = false;
	}

}
