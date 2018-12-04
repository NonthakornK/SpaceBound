package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import renderer.RenderableHolder;

public class ShieldMaxBox extends Items {
	private final int shieldStorage = 150;
	
	public ShieldMaxBox(double x) {
		super(ThreadLocalRandom.current().nextDouble(1, 5));
		this.width = RenderableHolder.shieldmax.getWidth();
		this.height = RenderableHolder.shieldmax.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = x;
		this.y = -this.height - ThreadLocalRandom.current().nextDouble(500);
		this.collideDamage = 0;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.shieldmax, x, y);
	}

	@Override
	public void onCollision(Unit other) {
		// TODO Auto-generated method stub
		this.hp = 0;
		this.destroyed = true;
		this.visible = false;
	}

	public int getShieldStorage() {
		return shieldStorage;
	}
	
}
