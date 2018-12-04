package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import renderer.RenderableHolder;

public class ShieldRegenBox extends Items {

	public ShieldRegenBox(double x) {
		super(ThreadLocalRandom.current().nextDouble(2,4));
		this.width = RenderableHolder.shieldregen.getWidth();
		this.height = RenderableHolder.shieldregen.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = x;
		this.y = -this.height - ThreadLocalRandom.current().nextDouble(500);
		this.collideDamage = 0;
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.shieldregen, x, y);
	}

	@Override
	public void onCollision(Unit other) {
		// TODO Auto-generated method stub
		this.hp = 0;
		this.destroyed = true;
		this.visible = false;
	}

}
