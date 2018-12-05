package logic;

import renderer.RenderableHolder;
import window.SceneManager;

public abstract class Enemy extends Unit {
	
	protected double weight;
	private static int zCounter = -200; // to generate different z for each Enemy to prevent flashing when 2 or more
										// enemy are overlap.
										// Enemy z is between -200 and -100 inclusive.

	public Enemy(double hp, double speed) {
		super(hp, speed);
		this.side = -1;
		this.z = zCounter;
		zCounter++;
		if (zCounter > -100) {
			zCounter = -200;
		}


	}

	public void onCollision(Unit others) {
		this.hp -= others.collideDamage;
		if (this.hp <= 0) {
			if (!this.destroyed) {
				GameLogic.currentEnemyWeight -= this.getWeight();
				if(this instanceof ESemiBoss) {
					GameLogic.killedSemi = true;
				}
				else if(this instanceof EBoss) {
					GameLogic.killedBoss = true;
				}
				Explosion e = new Explosion(x, y, width, height, z);
				e.playSfx();
				RenderableHolder.getInstance().add(e);
			}
			this.destroyed = true;
			this.visible = false;
		}
	}

	public boolean isOutOfScreen() {
		if ((int) this.y > SceneManager.SCENE_HEIGHT) {
			GameLogic.currentEnemyWeight -= this.getWeight();
			return true;
		}
		return false;
	}
	
	public abstract double getWeight();

}
