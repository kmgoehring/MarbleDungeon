package edu.virginia.cs2110.marbledungeon;



import android.graphics.Rect;

/**
 * A fireball class that represents some moving objects 
 * that the player's marble must try to avoid. 
 * @author Kevin Goehring, Alishan Hassan, Nikhil Padmanabhan
 *
 */
public class Fireball {

	public float x;
	public float y;
	public boolean right;
	public boolean down;
	
	//Sets the hitbox around the fireball at 50x50
	public Rect fireballHitbox = new Rect ((int)x, (int)y, ((int)x + 50), ((int)y + 50));
	
	/**
	 * The parameters required to establish a fireball object. 
	 * @param x - Its x coordinate.
	 * @param y - Its y coordinate.
	 * @param right - Boolean -> Whether or not the fireball is moving to the right (else left).
	 * @param down - Boolean -> Whether or not the fireball is moving down (else up).
	 */
	public Fireball(float x, float y, boolean right, boolean down) {
		super();
		this.x = x;
		this.y = y;
		this.right = right;
		this.down = down;
	}

	public Rect getFireballHitbox() {
		return fireballHitbox;
	}

	public void setFireballHitbox(Rect fireballHitbox) {
		this.fireballHitbox = fireballHitbox;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
	
	/**
	 * This will check to see if a fireball is colliding with another game object. 
	 * @param fireballHitbox - The hitbox radius for the fireball object.
	 * @param objectbox - The hitbox radius for any other object. 
	 * @return True if colliding, else false. 
	 */
	public boolean fireBallCollision(Rect fireballHitbox, Rect objectbox)
	{
		if(fireballHitbox.intersect(objectbox)){
			return true;
		}
		else return false;
	}
	
	
	
	
	
	
	
	
}
