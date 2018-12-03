package renderer;

import javafx.scene.canvas.GraphicsContext;

public interface IRenderable {//Interface part that make some function
	public int getZ();

	public void draw(GraphicsContext gc);

	public boolean isDestroyed();

	public boolean isVisible();
}
