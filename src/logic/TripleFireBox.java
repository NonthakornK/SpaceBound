package logic;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.canvas.GraphicsContext;
import renderer.RenderableHolder;

public class TripleFireBox extends Items {

	public TripleFireBox(double x) {
		// TODO Auto-generated constructor stub
		super(ThreadLocalRandom.current().nextDouble(1, 5));
		this.width = RenderableHolder.randomBox.getWidth();
		this.height = RenderableHolder.randomBox.getHeight();
		this.visible = true;
		this.destroyed = false;
		this.x = x;
		this.y = -this.height - ThreadLocalRandom.current().nextDouble(500);
		this.collideDamage = 0;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		gc.drawImage(RenderableHolder.randomBox, x, y);
	}

	@Override
	public void onCollision(Unit others) {
		// TODO Auto-generated method stub
		this.hp = 0;
		this.destroyed = true;
		this.visible = false;
	}

}
