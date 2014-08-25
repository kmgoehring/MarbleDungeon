package edu.virginia.cs2110.marbledungeon;

import edu.virginia.cs2110.marbledungeon.LevelOne.CustomDrawableView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;



/**
 * A wyvern class enemy. 
 * @author Kevin Goehring, Alishan Hassan, Nikhil Padmanabhan
 */
public class Wyvern {

	int x, y;
	int xSpeed, ySpeed;
	int height, width;
	Bitmap b;
	CustomDrawableView mv;
	int currentFrame = 0;
	int direction = 2; // The variable that establishes the direction the wyvern bitmap image faces.
	
	
	/**
	 * @return - Dimensions of wyvern enemy hitbox radius.
	 */
	public Rect getWyvernHitBox() {
		Rect WyvernHitBox = new Rect(this.x, this.y, this.x+84, this.y+84);
		return WyvernHitBox;
	}
	
	/**
	 * Requirements to establish a wyvern class enemy.
	 * @param myView - The view to draw on.
	 * @param wyvern - The bitmap image that represents the enemy.
	 * @param x - The x coordinate.
	 * @param y - The y coordinate. 
	 */
	public Wyvern(CustomDrawableView myView, Bitmap wyvern, int x, int y) {
		mv = myView;
		b = wyvern;
		height = b.getHeight() / 4 ; //Divide by relevant rows/columns
		width = b.getWidth() / 3;
		this.x = x;
		this.y = y;
		xSpeed = 14;
		ySpeed = 0;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getxSpeed() {
		return xSpeed;
	}

	public void setxSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	public int getySpeed() {
		return ySpeed;
	}

	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * This method updates the wyvern bitmap and draws it according to any 
	 * potential change in x/y speed and/or direction. 
	 */
	private void update() {
		//Facing down, 0 
		if(x > mv.getWidth() - width - xSpeed) {
			xSpeed = -14;
			ySpeed = 0;
			direction  = 1;
		}
		//Facing left, 1
		if(y > mv.getHeight() - height- ySpeed) {
			xSpeed = -14;
			ySpeed = 0;
			direction = 2;
		}
		//Facing right, 2
		if(y + ySpeed < 0) {
			y = 0;
			ySpeed = 0;
			xSpeed = 14;
			direction = 0;
		}	
		//Facing up, 3
		if(x + xSpeed < 0) {
			x = 0;
			xSpeed = 14;
			ySpeed = 0;
			direction = 2;
		}
	/**
	 * Thread sleeping adjustable IF the wyvern movement is too jittery.
	 */
//		try {
//			Thread.sleep(8);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		currentFrame = ++ currentFrame % 3;
		x += xSpeed;
		y+= ySpeed;
	}
	
	/**
	 * Draws the wyvern bitmap from a rectangular "cut" out of the 
	 * applicable sprite sheet. 
	 * @param canvas - The canvas to draw on. 
	 */
	public void Draw(Canvas canvas) {
		update();
		int srcY = direction*height;
		int srcX = currentFrame*width;
		Rect src = new Rect(srcX, srcY, srcX+width, srcY+height);
		Rect dest = new Rect(x, y, x+width, y+height);
		canvas.drawBitmap(b, src, dest, null);
		
	}

	/**
	 * Establishes an "invisible" rectangular hitbox that will act as the wyvern 
	 * flame breath attack. Useful for drawing the animation and checkig for 
	 * player collisions. 
	 * @return - The four corner coordinates that establish the boundaries of the flame breath. 
	 */
	public Rect wyvernBreathA() {
		Rect flame = new Rect(	this.getWyvernHitBox().left+100, this.getWyvernHitBox().top+25,
				this.getWyvernHitBox().right+200, this.getWyvernHitBox().bottom);
		return flame;
	}
	
	
	
}