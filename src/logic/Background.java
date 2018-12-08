package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import renderer.IRenderable;
import renderer.RenderableHolder;

public class Background implements IRenderable {

	private Image bgImage = null;
	private double currentY;
//	private int imageWidth;
//	private int imageHeight;
//	private int screenWidth = 600;
	private int screenHeight = 3166;
	private double scrollSpeed = 2.1;
	private double scrollModifier = 1;

	public Background() {
		// TODO Auto-generated constructor stub
		bgImage = RenderableHolder.background;
		if (bgImage != null) {
//			imageWidth = (int) bgImage.getWidth();
//			imageHeight = (int) bgImage.getHeight();
			currentY = 0;
		} 
//		else {
//			imageWidth = 0;
//			imageHeight = 0;
//		}
	}

	@Override
	public int getZ() {
		// TODO Auto-generated method stub
		return Integer.MIN_VALUE;
	}

	public void updateBackground() {
		if (Distance.distance >= (scrollSpeed + scrollModifier) * 100) {
			scrollSpeed += 0.1;
			scrollModifier *= 1.15;
		}
		currentY += scrollSpeed;
		if (currentY >= screenHeight) {
			currentY = 0;
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		if (bgImage == null) {
			return;
		}
		updateBackground();
		
		gc.drawImage(bgImage, 0, currentY);

		gc.drawImage(bgImage, 0, currentY - screenHeight);

		updateBackground();

	}

	@Override
	public boolean isDestroyed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return true;
	}

}
