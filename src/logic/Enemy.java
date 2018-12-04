package logic;

import renderer.RenderableHolder;
import window.SceneManager;

public abstract class Enemy extends Unit {

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
				//calculateScore(this);
				if(this instanceof Asteroid) {
					GameLogic.currentEnemyWeight -= 1;
				}
				else if(this instanceof EMachine) {
					GameLogic.currentEnemyWeight -= 1.5;
				}
				else if(this instanceof EGhost) {
					GameLogic.currentEnemyWeight -= 2;
				}
				else if(this instanceof ETree) {
					GameLogic.currentEnemyWeight -= 2.5;
				}
				else if(this instanceof ESemiBoss) {
					GameLogic.currentEnemyWeight -= 3.5;
					GameLogic.relaxTime = System.nanoTime() + 18000000001L;
					GameLogic.currentEnemyWeight += 32.4;
				}
				else if(this instanceof EBoss) {
					GameLogic.currentEnemyWeight -= 5;
					GameLogic.killedBoss = true;
				}
				Explosion e = new Explosion(x, y, width, height, z);
				e.playSfx();
				RenderableHolder.getInstance().add(e);
			}
			this.destroyed = true;
			this.visible = false;
		}
		// System.out.println(this.getClass() + " is collided! by player " + this.hp);
	}

	public boolean isOutOfScreen() {
		if ((int) this.y > SceneManager.SCENE_HEIGHT) {
			if(this instanceof Asteroid) {
				GameLogic.currentEnemyWeight -= 1;
			}
			else if(this instanceof EMachine) {
				GameLogic.currentEnemyWeight -= 1.5;
			}
			else if(this instanceof EGhost) {
				GameLogic.currentEnemyWeight -= 2;
			}
			else if(this instanceof ETree) {
				GameLogic.currentEnemyWeight -= 2.5;
			}
			return true;
		}
		return false;
	}

//	private void calculateScore(Enemy e) {
//		if (e instanceof EBoss) {
//			Score.score += 30;
//			GameLogic.isBossAlive = false;
//			GameLogic.killedBoss = true;
//		}
//		if (e instanceof Asteroid) {
//			Score.score += 1;
//		}
//		if (e instanceof EGhost) {
//			Score.score += 5;
//		}
//		if (e instanceof EMachine) {
//			Score.score += 3;
//		}
//		if (e instanceof ETree) {
//			Score.score += 10;
//		}
//		if (e instanceof ESemiBoss) {
//			Score.score += 15;
//		}
//	}

}
