package edu.virginia.cs2110.marbledungeon;

import java.util.ArrayList;
import java.util.Random;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * The class that represents the Leve One dungeon. Many of these
 * methods should be moved into other classes to avoid overuse of similar lines
 * of code! For example, there is currently no "Marble Class" and so the marble
 * split method repeats code. (Work for the future/improvement of code).
 * 
 * @author Kevin Goehring, Alishan Hassan, Nikhil Padmanabhan
 * 
 */
public class LevelOne extends ActionBarActivity implements SensorEventListener {
	public final static String SCORE = "edu.virginia.cs2110.marbledungeon.SCORE";

	/**
	 * Difficulty Setting
	 */
	boolean hardcore = false;

	/**
	 * Variables related to music/sounds.
	 */
	MediaPlayer mp = new MediaPlayer();
	private SoundPool sounds;
	private int bigboom;
	private int zap;
	private int marbleSwoosh;
	private int dragonDead;

	// Marble "split bonus" variables
	private boolean split;
	float currentTick = -700;
	private boolean remove;

	// random cherries
	int random = -1;
	int heartRandom = -1;

	// For use in a few applications.
	Random randomGenerator = new Random();

	boolean GameOver = false; // Whether or not the game is over.
	boolean LevelComplete = false;
	boolean bossAdead = false;
	boolean bossBdead = false;

	public int score = 0; // Stores player points.
	private float ticks = 0; // A possible timer!

	// Variables for marble characteristics.
	public float xAcc, xVel = 0.0f;
	public float yAcc, yVel = 0.0f;
	public float xPosition = 1880.0f;
	public float yPosition = 1100.0f;
	public float xMax, yMax;
	public float marbleHealth = 5; // Marble hit points.
	public float full = 5; // Cherry refill level.
	public int immunity = 0; // Protects marble from taking multiple hits at a
								// time.
	public int toggle = 0; // Toggle allows for flashing marble at critical
							// health.
	final int ballWidth = 50; // can adjust the dimensions of the ball
	final int ballHeight = 50;
	Rect ballHitBox = new Rect(); // Establish the marble's hitbox.
	Rect ball1HitBox = new Rect(); // And any "split" marbles
	Rect ball2HitBox = new Rect();
	Rect ball3HitBox = new Rect();
	Rect ball4HitBox = new Rect();

	// Variables for split marble characteristics.
	public float xAcc1, xVel1 = 0.0f;
	public float yAcc1, yVel1 = 0.0f;
	public float xPosition1 = 1880.0f;
	public float yPosition1 = 1100.0f;
	public float xAcc2, xVel2 = 0.0f;
	public float yAcc2, yVel2 = 0.0f;
	public float xPosition2 = 1880.0f;
	public float yPosition2 = 1100.0f;
	public float xAcc3, xVel3 = 0.0f;
	public float yAcc3, yVel3 = 0.0f;
	public float xPosition3 = 1880.0f;
	public float yPosition3 = 1100.0f;
	public float xAcc4, xVel4 = 0.0f;
	public float yAcc4, yVel4 = 0.0f;
	public float xPosition4 = 1880.0f;
	public float yPosition4 = 1100.0f;

	// TODO Don't think this is needed ....
	public float xMax1, yMax1;

	public float marbleHealth1 = 5; // Marble hit points.
	public int immunity1 = 0; // Protects marble from taking multiple hits at a
								// time.
	public int toggle1 = 0; // Toggle allows for flashing marble at critical
							// health.

	final int ballWidth1 = 50; // can adjust the dimensions of the ball
	final int ballHeight1 = 50;

	// Variables for the timer graphic
	private int tickTimeSpacing = 1200; // Ticks per timing gap
	final int timerWidth = 100;
	final int timerHeight = 100;

	/**
	 * Creates instances of Fireball objects.
	 */
	public int fireballSpeed = 5;
	ArrayList<Fireball> flameArray = new ArrayList<Fireball>();
	Fireball flameOne = new Fireball(600, 75, true, true);
	Fireball flameTwo = new Fireball(200, 750, false, false);
	Fireball flameThree = new Fireball(300, 50, false, false);
	Fireball flameFour = new Fireball(400, 550, false, false);
	Fireball flameFive = new Fireball(1000, 80, false, false);
	Fireball flameSix = new Fireball(1500, 500, false, false);

	/**
	 * Creates instances of Toggle Switches
	 */
	boolean greenSwitch = false;
	boolean yellowSwitch = false;
	boolean redSwitch = false;
	Rect greenDown = new Rect();
	Rect yellowUp = new Rect();
	Rect redUp = new Rect();

	float castleBlastTick = 0;

	boolean castleBlast = false; // Flag to blow the gate!!
	int bloodTick = 0; // For blood splatter effect timer
	boolean splatter = false;

	/**
	 * Traps.
	 */
	Rect light1 = new Rect();
	Rect light2 = new Rect();
	Rect lava1 = new Rect();
	Rect lava2 = new Rect();
	Rect lava3 = new Rect();
	Rect lava4 = new Rect();
	Rect lava5 = new Rect();
	Rect lava6 = new Rect();
	Rect lava7 = new Rect();
	Rect lava8 = new Rect();
	Rect lava9 = new Rect();
	Rect lava10 = new Rect();
	ArrayList<Rect> lavaList = new ArrayList<Rect>();

	Rect intersectbox = new Rect();

	/**
	 * Various wall obstacles.
	 */
	ArrayList<Rect> obstacles = new ArrayList<Rect>(); // ArrayList makes it
														// easier to check for
														// collisions!
	Rect r1 = new Rect(); // Wall element
	Rect r2 = new Rect();
	Rect r3 = new Rect();
	Rect r4 = new Rect();
	Rect r5 = new Rect();
	Rect r6 = new Rect();
	Rect r7 = new Rect();
	Rect r8 = new Rect();
	Rect r9 = new Rect();
	Rect r10 = new Rect();
	Rect r11 = new Rect();
	Rect r12 = new Rect();
	Rect r13 = new Rect();
	Rect r14 = new Rect();
	Rect r15 = new Rect();
	Rect gate1 = new Rect(); // Gate element
	Rect gate2 = new Rect();
	Rect gate3 = new Rect();
	Rect gate4 = new Rect();
	Rect winBox = new Rect(40, 40, 140, 140); // HitBox dimensions for marble reaching the "goal" aka the timer graphic
	public final float restitution = -0.35f; // Marble "bounce" variable.
												// Adjustable!
	public float frameTime = 0.446f; // This variable controls the marbles speed
										// changes. Adjustable here!

	private Bitmap mBitmap;
	private SensorManager sensorManager = null;
	private Display display;

	/**
	 * See AnimTask() method
	 */
	AnimTask at;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * Checking if player is up to the challenge.
		 * Changes these setting if hardcore mode is chosen.
		 */
		if (SettingsScreen.hardcoreCalled == "yes") {
			hardcore = true;
			fireballSpeed = 15;
			marbleHealth = 3;
			tickTimeSpacing = 300;
			full = 3;
		} else
			hardcore = false;

		/**
		 * Adding rectangle obstacles into the Rect array.
		 */
		obstacles.add(r1);
		obstacles.add(r2);
		obstacles.add(r3);
		obstacles.add(r4);
		obstacles.add(r5);
		obstacles.add(r6);
		obstacles.add(r7);
		obstacles.add(r8);
		obstacles.add(r9);
		obstacles.add(r10);
		obstacles.add(r11);
		obstacles.add(r12);
		obstacles.add(r13);
		obstacles.add(r14);
		obstacles.add(r15);
		obstacles.add(gate1);
		obstacles.add(gate2);
		obstacles.add(gate3);
		obstacles.add(gate4);

		/**
		 * Adding fireball objects into flameArray.
		 */
		flameArray.add(flameOne);
		flameArray.add(flameTwo);
		flameArray.add(flameThree);
		flameArray.add(flameFour);
		flameArray.add(flameFive);
		flameArray.add(flameSix);

		/**
		 * Add lava hitboxes into array.
		 */
		lavaList.add(lava1);
		lavaList.add(lava2);
		lavaList.add(lava3);
		lavaList.add(lava4);
		lavaList.add(lava5);
		lavaList.add(lava6);
		lavaList.add(lava7);
		lavaList.add(lava8);
		lavaList.add(lava9);
		lavaList.add(lava10);

		/**
		 * Activity is FULLSCREEN Screen timeout is turned OFF during gameplay.
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			Sensor accelerometer = sensorManager.getSensorList(
					Sensor.TYPE_ACCELEROMETER).get(0);
			sensorManager.registerListener(this, accelerometer,
					SensorManager.SENSOR_DELAY_GAME);
		}

		setContentView(R.layout.activity_level_one);
		CustomDrawableView mv = new CustomDrawableView(this);
		setContentView(mv);
		this.at = new AnimTask(mv);
		display = getWindowManager().getDefaultDisplay();
		xMax = (float) display.getWidth() - 50;
		yMax = (float) display.getHeight() - 50;
		xMax1 = (float) display.getWidth() - 50;
		yMax1 = (float) display.getHeight() - 50;
	}

	/**
	 * Acceleration via the devices orientation and the accelerometer. Works for
	 * ALL marbles.
	 */
	public void onSensorChanged(SensorEvent sensorEvent) {
		{
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				switch (display.getRotation()) {
				case Surface.ROTATION_0:
					xAcc = sensorEvent.values[0];
					yAcc = sensorEvent.values[1];
					xAcc1 = sensorEvent.values[0];
					yAcc1 = sensorEvent.values[1];
					xAcc2 = sensorEvent.values[0];
					yAcc2 = sensorEvent.values[1];
					xAcc3 = sensorEvent.values[0];
					yAcc3 = sensorEvent.values[1];
					xAcc4 = sensorEvent.values[0];
					yAcc4 = sensorEvent.values[1];
					break;
				case Surface.ROTATION_90:
					xAcc = -sensorEvent.values[1];
					yAcc = sensorEvent.values[0];
					xAcc1 = -sensorEvent.values[0];
					yAcc1 = sensorEvent.values[1];
					xAcc2 = -sensorEvent.values[0];
					yAcc2 = sensorEvent.values[1];
					xAcc3 = -sensorEvent.values[0];
					yAcc3 = sensorEvent.values[1];
					xAcc4 = -sensorEvent.values[0];
					yAcc4 = sensorEvent.values[1];
					break;
				case Surface.ROTATION_180:
					xAcc = -sensorEvent.values[0];
					yAcc = -sensorEvent.values[1];
					xAcc1 = -sensorEvent.values[0];
					yAcc1 = -sensorEvent.values[1];
					xAcc2 = -sensorEvent.values[0];
					yAcc2 = -sensorEvent.values[1];
					xAcc3 = -sensorEvent.values[0];
					yAcc3 = -sensorEvent.values[1];
					xAcc4 = -sensorEvent.values[0];
					yAcc4 = -sensorEvent.values[1];
					break;
				case Surface.ROTATION_270:
					xAcc = sensorEvent.values[1];
					yAcc = -sensorEvent.values[0];
					xAcc1 = sensorEvent.values[0];
					yAcc1 = -sensorEvent.values[1];
					xAcc2 = sensorEvent.values[0];
					yAcc2 = -sensorEvent.values[1];
					xAcc3 = sensorEvent.values[0];
					yAcc3 = -sensorEvent.values[1];
					xAcc4 = sensorEvent.values[0];
					yAcc4 = -sensorEvent.values[1];
					break;
				}
				updateBall();
			}
		}
	}

	/**
	 * Method that updates the balls position and implements bouncing off the
	 * edges of the screen.
	 */
	private void updateBall() {

		// Calculate new speed
		xVel += (xAcc * frameTime);
		yVel += (yAcc * frameTime);

		xVel1 += (xAcc1 * frameTime);
		yVel1 += (yAcc1 * frameTime);

		xVel2 += (xAcc2 * frameTime);
		yVel2 += (yAcc2 * frameTime);

		xVel3 += (xAcc3 * frameTime);
		yVel3 += (yAcc3 * frameTime);

		xVel4 += (xAcc4 * frameTime);
		yVel4 += (yAcc4 * frameTime);

		// Calc distance traveled in that time
		float xS = (xVel / 2) * frameTime;
		float yS = (yVel / 2) * frameTime;

		float xS1 = (xVel1 / 2) * frameTime;
		float yS1 = (yVel1 / 2) * frameTime;

		float xS2 = (xVel2 / 2) * frameTime;
		float yS2 = (yVel2 / 2) * frameTime;

		float xS3 = (xVel3 / 2) * frameTime;
		float yS3 = (yVel3 / 2) * frameTime;

		float xS4 = (xVel4 / 2) * frameTime;
		float yS4 = (yVel4 / 2) * frameTime;

		// Changes position based on speed. 
		xPosition -= xS;
		yPosition += yS;
		xPosition1 -= xS1;
		yPosition1 += yS1;
		xPosition2 -= xS2;
		yPosition2 += yS2;
		xPosition3 -= xS3;
		yPosition3 += yS3;
		xPosition4 -= xS4;
		yPosition4 += yS4;

		/**
		 * Implement wall bounces via coefficient of restitution. Easily
		 * adjustable at initial variable declarations.
		 */
		if (xPosition > xMax) {
			xPosition = xMax;
			xVel *= restitution;

		} else if (xPosition < 0) {
			xPosition = 0;
			xVel *= restitution;
		}
		if (yPosition > yMax) {
			yPosition = yMax;
			yVel *= restitution;
		} else if (yPosition < 0) {
			yPosition = 0;
			yVel *= restitution;
		}

		if (xPosition1 > xMax1) {
			xPosition1 = xMax1;
			xVel1 *= restitution;

		} else if (xPosition1 < 0) {
			xPosition1 = 0;
			xVel1 *= restitution;
		}

		if (yPosition1 > yMax1) {
			yPosition1 = yMax1;
			yVel1 *= restitution;
		} else if (yPosition1 < 0) {
			yPosition1 = 0;
			yVel1 *= restitution;
		}

		if (xPosition2 > xMax1) {
			xPosition2 = xMax1;
			xVel2 *= restitution;

		} else if (xPosition2 < 0) {
			xPosition2 = 0;
			xVel2 *= restitution;
		}

		if (yPosition2 > yMax1) {
			yPosition2 = yMax1;
			yVel2 *= restitution;
		} else if (yPosition2 < 0) {
			yPosition2 = 0;
			yVel2 *= restitution;
		}

		if (xPosition3 > xMax1) {
			xPosition3 = xMax1;
			xVel2 *= restitution;

		} else if (xPosition3 < 0) {
			xPosition3 = 0;
			xVel3 *= restitution;
		}

		if (yPosition3 > yMax1) {
			yPosition3 = yMax1;
			yVel3 *= restitution;
		} else if (yPosition3 < 0) {
			yPosition3 = 0;
			yVel3 *= restitution;
		}

		if (xPosition4 > xMax1) {
			xPosition4 = xMax1;
			xVel4 *= restitution;

		} else if (xPosition4 < 0) {
			xPosition4 = 0;
			xVel4 *= restitution;
		}

		if (yPosition4 > yMax1) {
			yPosition4 = yMax1;
			yVel4 *= restitution;
		} else if (yPosition4 < 0) {
			yPosition4 = 0;
			yVel4 *= restitution;
		}

	}

	public class CustomDrawableView extends View {
		Resources res = getResources();

		/**
		 * Background marble bitmap.
		 */
		Bitmap bitmap = BitmapFactory.decodeResource(res,
				R.drawable.blackmarble);
		Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1920, 1280, false);

		/**
		 * Fireball bitmaps.
		 */
		Bitmap fireBit = BitmapFactory.decodeResource(res,
				R.drawable.firemarble1);
		Bitmap fireScaled = Bitmap.createScaledBitmap(fireBit, 80, 80, false);

		/**
		 * Stone wall bitmaps.
		 */
		Bitmap stone1 = BitmapFactory
				.decodeResource(res, R.drawable.onehundred);
		Bitmap stonegate = BitmapFactory.decodeResource(res, R.drawable.gate);
		Bitmap scaleGate = Bitmap
				.createScaledBitmap(stonegate, 100, 500, false);

		/**
		 * Lightning trap bitmap.
		 */
		Bitmap lightning = BitmapFactory.decodeResource(res,
				R.drawable.lightning);
		Bitmap lightningscale = Bitmap.createScaledBitmap(lightning, 50, 100,
				false);
		Bitmap lava = BitmapFactory.decodeResource(res, R.drawable.lava);

		/**
		 * Toggle switch bitmaps.
		 */
		Bitmap green = BitmapFactory.decodeResource(res, R.drawable.greendown);
		Bitmap yellow = BitmapFactory.decodeResource(res, R.drawable.yellowup);
		Bitmap red = BitmapFactory.decodeResource(res, R.drawable.redup);

		/**
		 * CastleBlast bitmap.
		 */
		Bitmap blast = BitmapFactory.decodeResource(res, R.drawable.boom);

		/**
		 * Blood bitmap.
		 */
		Bitmap blood = BitmapFactory.decodeResource(res, R.drawable.blood);

		public Paint blackPaint, clearPaint;
		boolean right;
		boolean down;

		/**
		 * Gate bitmaps.
		 */
		Bitmap center = BitmapFactory.decodeResource(res, R.drawable.door);

		/**
		 * Wyvern bitmap.
		 */
		private Bitmap wyvern = BitmapFactory.decodeResource(res,
				R.drawable.wyvern);
		/**
		 * Instantiate boss A and B
		 */

		Wyvern bossA = new Wyvern(this, wyvern, 800, 500);
		Wyvern bossB = new Wyvern(this, wyvern, 200, 800);

		/**
		 * Fire breath bitmap.
		 */
		private Bitmap fireBreath = BitmapFactory.decodeResource(res,
				R.drawable.flames);

		/**
		 * Powerup BitMAP
		 */
		Bitmap powerupMarble = BitmapFactory.decodeResource(getResources(),
				R.drawable.powerupmarble);
		Bitmap scaledPowerupMarble = Bitmap.createScaledBitmap(powerupMarble,
				100, 100, false);

		/**
		 * JeffDollar Bitmap
		 */
		Bitmap cherry = BitmapFactory.decodeResource(getResources(),
				R.drawable.jeffdollar);
		Bitmap scaledjeffdollar = Bitmap.createScaledBitmap(cherry, 100, 100,
				false);

		/**
		 * Heart Bitmap
		 */
		Bitmap heart = BitmapFactory.decodeResource(getResources(),
				R.drawable.heart);
		Bitmap scaledHeart = Bitmap.createScaledBitmap(heart, 40, 40, false);

		// Coin hitboxes
		Rect rect0 = new Rect(200, 900, 300, 1000);
		Rect rect1 = new Rect(600, 100, 700, 200);
		Rect rect2 = new Rect(700, 800, 800, 900);
		Rect rect3 = new Rect(1200, 50, 1300, 150);
		Rect rect4 = new Rect(1050, 850, 1150, 950);
		Rect rect5 = new Rect(1700, 300, 1800, 400);

		// Heart hitboxes
		Rect heart0 = new Rect(400, 700, 420, 720);
		Rect heart1 = new Rect(500, 150, 520, 170);
		Rect heart2 = new Rect(1600, 500, 1620, 520);
		Rect heart3 = new Rect(1200, 150, 1220, 170);

		public CustomDrawableView(Context context) {
			super(context);
			this.init();

			/**
			 * Set sound effects.
			 */
			sounds = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
			bigboom = sounds.load(context, R.raw.bigboom, 1);
			zap = sounds.load(context, R.raw.zap, 1);
			dragonDead = sounds.load(context, R.raw.dragondead, 1);
			marbleSwoosh = sounds.load(context, R.raw.swoosh, 1);

		}

		/**
		 * Initializes different paint schemes.
		 */
		private void init() {
			this.blackPaint = new Paint();
			this.blackPaint.setStyle(Style.FILL_AND_STROKE);
			this.blackPaint.setStrokeWidth(3.5f);
			this.blackPaint.setAntiAlias(true);
			this.blackPaint.setColor(0xff000000);
			this.clearPaint = new Paint();
			this.clearPaint.setStyle(Style.FILL_AND_STROKE);
			this.clearPaint.setStrokeWidth(3.5f);
			this.clearPaint.setAntiAlias(true);
			this.clearPaint.setColor(0x00000000);
		}

		/**
		 * The main game method that draws everything to the screen.
		 */
		protected void onDraw(Canvas canvas) {
			// Initial game over check
			if (ballHitBox.intersect(winBox)) {
				LevelComplete = true;
				GameOver = true;
			}
			if (marbleHealth <= 0) {
				GameOver = true;
			}
			if (GameOver == false) {
				super.onDraw(canvas);
				ticks++;
				int yellowTick = 0;

				Canvas d = canvas;

				/**
				 * Call marble/wall collisions.
				 */
				marbleWallCollision(ballHitBox, obstacles);
				d.drawBitmap(scaled, 0, 0, null);

				/**
				 * Various methods for the wyvern, such as collisions and fire
				 * breath.
				 */

				if (bossAdead == false) {
					bossA.Draw(canvas);
					for (int i = 0; i < obstacles.size(); i++) {
						if (bossA.getWyvernHitBox().intersect(obstacles.get(i))) {

							intersectbox.setIntersect(bossA.getWyvernHitBox(),
									(obstacles.get(i)));

							if (intersectbox.width() >= intersectbox.height()
									&& bossA.getWyvernHitBox().centerY() < obstacles
											.get(i).centerY()) {

								bossA.ySpeed *= -1;
							}
							if (intersectbox.width() >= intersectbox.height()
									&& bossA.getWyvernHitBox().centerY() > obstacles
											.get(i).centerY()) {
								bossA.ySpeed *= -1;

							}
							if (intersectbox.height() >= intersectbox.width()
									&& bossA.getWyvernHitBox().centerX() < obstacles
											.get(i).centerX()) {
								bossA.xSpeed *= -1;
							}
							if (intersectbox.height() >= intersectbox.width()
									&& bossA.getWyvernHitBox().centerX() > obstacles
											.get(i).centerX()) {
								bossA.xSpeed *= -1;
								bossA.direction = 2;
							}
						}
					}
				}
				if (bossAdead == true) {
					bossA.getWyvernHitBox().set(-10, -10, -20, -20);
				}

				if (bossBdead == false) {
					bossB.Draw(canvas);
					for (int i = 0; i < obstacles.size(); i++) {
						if (bossB.getWyvernHitBox().intersect(obstacles.get(i))) {

							intersectbox.setIntersect(bossB.getWyvernHitBox(),
									(obstacles.get(i)));

							if (intersectbox.width() >= intersectbox.height()
									&& bossB.getWyvernHitBox().centerY() < obstacles
											.get(i).centerY()) {

								bossA.ySpeed *= -1;
							}
							if (intersectbox.width() >= intersectbox.height()
									&& bossB.getWyvernHitBox().centerY() > obstacles
											.get(i).centerY()) {
								bossA.ySpeed *= -1;

							}
							if (intersectbox.height() >= intersectbox.width()
									&& bossA.getWyvernHitBox().centerX() < obstacles
											.get(i).centerX()) {
								bossB.xSpeed *= -1;
								bossB.direction = 1;

							}
							if (intersectbox.height() >= intersectbox.width()
									&& bossB.getWyvernHitBox().centerX() > obstacles
											.get(i).centerX()) {
								bossB.xSpeed *= -1;
								bossB.direction = 3;
							}
						}
					}
				}

				/**
				 * Handles damaging output between wyvern and marble.
				 */
				boolean breath = false;
				if (ticks % 20 == 0) {
					breath = true;
				}
				// Wyvern A starts breathing fire only after its "gate" is opened. 
				if (breath == true && castleBlast == true && bossAdead == false
						&& bossA.getxSpeed() > 0) {
					Rect flames = bossA.wyvernBreathA();
					canvas.drawRect(flames, clearPaint);
					canvas.drawRect(flames, blackPaint);
					canvas.drawBitmap(fireBreath,
							bossA.getWyvernHitBox().left + 100,
							flames.top - 30, null);
					if (ballHitBox.intersect(flames)) {
						marbleHealth -= 1;
					}
				}
				
				// Marble/wyvern collision. 
				if (ballHitBox.intersect(bossA.getWyvernHitBox())
						&& bossAdead == false) {
					marbleHealth -= 1;
				}
				//Draws fire breath and handles breath/marble collision. 
				if (breath == true && bossBdead == false
						&& bossB.getxSpeed() > 0 && bossB.getX() < 700) {
					Rect flames = bossB.wyvernBreathA();
					canvas.drawRect(flames, clearPaint);
					canvas.drawRect(flames, blackPaint);
					canvas.drawBitmap(fireBreath,
							bossB.getWyvernHitBox().left + 100,
							flames.top - 30, null);
					if (ballHitBox.intersect(flames)) {
						marbleHealth -= 1;
					}
				}

				if (ballHitBox.intersect(bossB.getWyvernHitBox())
						&& bossBdead == false) {
					marbleHealth -= 1;
				}

				/**
				 * Draw the stone wall bitmaps.
				 */
				canvas.drawBitmap(stone1, 300, 1000, null);
				canvas.drawBitmap(stone1, 1700, 1000, null);
				canvas.drawBitmap(stone1, 300, 0, null);
				canvas.drawBitmap(stone1, 300, 100, null);
				canvas.drawBitmap(stone1, 1700, 0, null);
				canvas.drawBitmap(stone1, 1700, 660, null);
				canvas.drawBitmap(stone1, 1800, 660, null);
				canvas.drawBitmap(stone1, 700, 600, null);
				canvas.drawBitmap(stone1, 700, 400, null);
				canvas.drawBitmap(stone1, 800, 600, null);
				canvas.drawBitmap(stone1, 800, 400, null);
				canvas.drawBitmap(stone1, 1300, 600, null);
				canvas.drawBitmap(stone1, 1300, 400, null);
				canvas.drawBitmap(stone1, 700, 1000, null);
				canvas.drawBitmap(stone1, 700, 900, null);
				canvas.drawBitmap(scaleGate, 900, 0, null);
				canvas.drawBitmap(scaleGate, 900, 600, null);

				/**
				 * Draw switches.
				 */
				if (greenSwitch == false) {
					canvas.drawBitmap(center, 900, 500, null);
				}
				greenDown.set(1800, 0, 1900, 100);
				yellowUp.set(1830, 790, 1930, 850);
				redUp.set(800, 1000, 900, 1100);

				/**
				 * Second gate is closed if bossA is still alive.
				 */
				if (bossAdead == false) {
					canvas.drawBitmap(center, 700, 500, null);
				}

				if (yellowSwitch == false) {
					light1.set(1710, 780, 1730, 990);
					light2.set(1335, 510, 1355, 610);
					/**
					 * Draw the lightning trap bitmaps.
					 */
					if (ticks % 10 == 0) {
						canvas.drawBitmap(lightning, 1700, 750, null);
						canvas.drawBitmap(lightningscale, 1325, 500, null);
					}
				}

				/**
				 * Draw the toggle switch bitmaps.
				 */
				canvas.drawBitmap(green, 1800, 0, null);
				canvas.drawBitmap(yellow, 1800, 760, null);
				canvas.drawBitmap(red, 800, 1000, null);

				/**
				 * Check for toggle switch collisions.
				 */
				if (ballHitBox.intersect(greenDown)) {
					greenSwitch = true;
					if (obstacles.contains(gate2) == true) {
						sounds.play(bigboom, 0.5f, 0.5f, 0, 0, 1.2f);
						castleBlast = true;
						castleBlastTick = 100;
						score += 750;
					}
					obstacles.remove(gate2);
				}
				if (ballHitBox.intersect(yellowUp) && yellowSwitch == false) {
					yellowSwitch = true;
					yellowTick = 400;
				} else if (ballHitBox.intersect(yellowUp) && yellowTick == 0) {
					yellowSwitch = false;
				}
				yellowTick--;

				if (ballHitBox.intersect(redUp) && redSwitch == false) {
					redSwitch = true;
					bossBdead = true;
					score += 5000;
					sounds.play(bigboom, 0.5f, 0.5f, 0, 0, 1.2f);
					bloodTick = 50;
				}

				/**
				 * Lava traps.
				 */
				lava1.set(75, 110, 175, 220);
				lava2.set(185, 205, 285, 305);
				lava3.set(55, 200, 155, 320);
				lava4.set(160, 180, 260, 280);
				lava5.set(30, 90, 130, 190);
				lava6.set(800, 670, 900, 770);
				lava7.set(300, 700, 400, 800);
				lava8.set(1000, 205, 1100, 305);
				lava9.set(1050, 160, 1150, 260);
				lava10.set(1110, 920, 1120, 1020);
		

				canvas.drawBitmap(lava, 75, 110, null);
				canvas.drawBitmap(lava, 185, 205, null);
				canvas.drawBitmap(lava, 55, 200, null);
				canvas.drawBitmap(lava, 160, 180, null);
				canvas.drawBitmap(lava, 30, 90, null);
				canvas.drawBitmap(lava, 800, 670, null);
				canvas.drawBitmap(lava, 300, 700, null);
				canvas.drawBitmap(lava, 1000, 205, null);
				canvas.drawBitmap(lava, 1050, 160, null);
				canvas.drawBitmap(lava, 1110, 920, null);

				if (redSwitch == false) {
					for (int i = 0; i < lavaList.size(); i++) {
						if (ballHitBox.intersect(lavaList.get(i))) {
							GameOver = true;
						}
					}
				}
				/**
				 * Trap collision for boss.
				 */
				if (bossA.getWyvernHitBox().intersect(light2)
						&& yellowSwitch == false && splatter == false) {
					bossAdead = true;
					score += 5000;
					bloodTick = 50;
					sounds.play(dragonDead, 1.2f, 1.2f, 0, 0, 1.2f);
					obstacles.remove(r9);
					sounds.play(bigboom, 0.5f, 0.5f, 0, 0, 1.2f);
					canvas.drawBitmap(blast,
							700 - randomGenerator.nextInt(100),
							500 - randomGenerator.nextInt(100), null);
				}
				
				//Draws blood graphic. 
				if (bloodTick > 0) {
					if (redSwitch == true) {
						canvas.drawBitmap(blood,
								bossB.x + 50 - randomGenerator.nextInt(10),
								bossB.y - randomGenerator.nextInt(10), null);
						splatter = true;
					}
					if (bossBdead == false) {
						canvas.drawBitmap(blood,
								bossA.x + 50 - randomGenerator.nextInt(10),
								bossA.y - randomGenerator.nextInt(10), null);
						splatter = true;
					}
				}
				bloodTick--;
				if (bloodTick == 0) {
					splatter = true;
				}

				/**
				 * Draw the bomb graphic for the gate explosion.
				 */
				if (castleBlastTick > 0) {
					canvas.drawBitmap(blast,
							900 - randomGenerator.nextInt(100),
							500 - randomGenerator.nextInt(100), null);
					castleBlastTick--;
				}

				/**
				 * Draw clearPaint Rectangles that correspond to the stone walls
				 * and handle various object collisions.
				 */
				gate1.set(900, 0, 1000, 500);
				gate2.set(900, 500, 1000, 600); // Center section of gate that
												// will open.
				gate3.set(900, 600, 1000, 1200);
				gate4.set(700, 800, 500, 600);
				r1.set(1700, 660, 1940, 760);
				r2.set(300, 0, 400, 100);
				r3.set(1700, 1000, 1800, 1100);
				r4.set(300, 1000, 400, 1100);
				r5.set(1700, 0, 1800, 100);
				r6.set(1800, 660, 1920, 760);
				r7.set(700, 600, 900, 700);
				r8.set(700, 400, 900, 500);
				r9.set(700, 500, 800, 600);
				r10.set(1300, 600, 1400, 700);
				r11.set(1300, 400, 1400, 500);
				r12.set(700, 1000, 800, 1100);
				r14.set(700, 900, 800, 1000);
				r13.set(300, 0, 400, 100);
				r15.set(300, 100, 400, 200);


				/**
				 * Draws clear Rectangles used to handle collisions.
				 */
				for (int i = 0; i < obstacles.size(); i++) {
					canvas.drawRect(obstacles.get(i), clearPaint);
				}

				/**
				 * Calls method to draw fireball and marble objects.
				 */
				for (int i = 0; i < flameArray.size(); i++) {
					fireBallDraw(flameArray.get(i), canvas);
				}
				// Draw marbles and timers.
				timerDraw(canvas);
				marbleDraw(canvas);
				invalidate();

				/**
				 * Handles drawing and collisions for the "power up" marble splitter. 
				 *
				 */
				Rect splitter = new Rect(600, 500, 700, 600);
				if (currentTick != -700)
					splitter.set(-1, -1, -1, -1);
				canvas.drawRect(splitter, clearPaint);

				if (currentTick == -700) {
					canvas.drawBitmap(scaledPowerupMarble, 600, 500, null);
				}
				if (currentTick != -700) {
					splitter.set(-1, -1, -1, -1);
				}
				if (ballHitBox.intersect(splitter)) {
					sounds.play(marbleSwoosh, 0.5f, 0.5f, 0, 0, 1.6f);
					sounds.play(marbleSwoosh, 0.5f, 0.5f, 0, 0, 1.6f);
					sounds.play(marbleSwoosh, 0.5f, 0.5f, 0, 0, 1.6f);
					sounds.play(marbleSwoosh, 0.5f, 0.5f, 0, 0, 1.6f);
					score += 2000;
					split = true;
					currentTick = ticks;
				}
				if ((ticks - 600) == currentTick)
					split = false;

				/**
				 * Methods for drawing coin and heart cherries.
				 */
				if ((int) (ticks) % 150 == 0 && ticks >= 150) {
					random = (int) (Math.random() * 6);
				}
				if (random == 0) {
					canvas.drawRect(rect0, clearPaint);
					if (ballHitBox.intersect(rect0)) {
						rect0.set(-109, -109, -109, -109);
						score += 500;
					}
					canvas.drawBitmap(scaledjeffdollar, rect0.left, rect0.top,
							blackPaint);
				} else if (random == 1) {
					canvas.drawRect(rect1, clearPaint);
					if (ballHitBox.intersect(rect1)) {
						rect1.set(-109, -109, -109, -109);
						score += 500;
					}
					canvas.drawBitmap(scaledjeffdollar, rect1.left, rect1.top,
							blackPaint);
				} else if (random == 2) {
					canvas.drawRect(rect2, clearPaint);
					if (ballHitBox.intersect(rect2)) {
						rect2.set(-109, -190, -109, -190);
						score += 500;
					}
					canvas.drawBitmap(scaledjeffdollar, rect2.left, rect2.top,
							blackPaint);
				} else if (random == 3) {
					canvas.drawRect(rect3, clearPaint);
					if (ballHitBox.intersect(rect3)) {
						rect3.set(-109, -109, -190, -109);
						score += 500;
					}
					canvas.drawBitmap(scaledjeffdollar, rect3.left, rect3.top,
							blackPaint);
				} else if (random == 4) {
					canvas.drawRect(rect4, clearPaint);
					if (ballHitBox.intersect(rect4)) {
						rect4.set(-109, -109, -109, -109);
						score += 500;
					}
					canvas.drawBitmap(scaledjeffdollar, rect4.left, rect4.top,
							blackPaint);
				} else if (random == 5) {
					canvas.drawRect(rect5, clearPaint);
					if (ballHitBox.intersect(rect5)) {
						rect5.set(-109, -109, -109, -109);
						score += 500;
					}
					canvas.drawBitmap(scaledjeffdollar, rect5.left, rect5.top,
							blackPaint);
				}

				if ((int) (ticks) % 400 == 0 && ticks >= 400) {
					heartRandom = (int) (Math.random() * 4);
				}
				if (heartRandom == 0) {
					canvas.drawRect(heart0, clearPaint);
					if (ballHitBox.intersect(heart0)) {
						heart0.set(-109, -109, -109, -109);
						marbleHealth = full;
					}
					canvas.drawBitmap(scaledHeart, heart0.left, heart0.top,
							blackPaint);
				}
				if (heartRandom == 1) {
					canvas.drawRect(heart1, clearPaint);
					if (ballHitBox.intersect(heart1)) {
						heart1.set(-109, -109, -109, -109);
						marbleHealth = full;
					}
					canvas.drawBitmap(scaledHeart, heart1.left, heart1.top,
							blackPaint);
				}
				if (heartRandom == 2) {
					canvas.drawRect(heart2, clearPaint);
					if (ballHitBox.intersect(heart2)) {
						heart2.set(-109, -109, -109, -109);
						marbleHealth = full;
					}
					canvas.drawBitmap(scaledHeart, heart2.left, heart2.top,
							blackPaint);
				}
				if (heartRandom == 3) {
					canvas.drawRect(heart3, clearPaint);
					if (ballHitBox.intersect(heart3)) {
						heart3.set(-109, -109, -109, -109);
						marbleHealth = full;
					}
					canvas.drawBitmap(scaledHeart, heart3.left, heart3.top,
							blackPaint);
				}

			} else if (GameOver == true) {
				// Launches end game tasks.
				Intent intent = new Intent(LevelOne.this, EndGame.class);

				if (LevelComplete == true) {
					// Scoring conventions for winning quickly and/or being
					// hardcore.
					if (ticks <= 2000) {
						score += 8000;
					} else if (ticks <= 3000) {
						score += 6000;
					} else if (ticks <= 4000) {
						score += 3000;
					} else
						score += 2000;

					if (hardcore == true) {
						score *= 5;
					}
				}
				intent.putExtra(SCORE, "" + score);
				startActivity(intent);
			}

		}

		/**
		 * Method to move/draw the fireballs around the screen. Also handles
		 * collisions between fireballs and walls.
		 */
		public void fireBallDraw(Fireball f, Canvas c) {
			fireBallWallCollision(flameArray, obstacles);
			FireBallFireBallCollision(flameArray);

			if ((f.getX() + 50) >= this.getMeasuredWidth()) {
				f.right = false;
			}
			if (f.getX() <= 0) {
				f.right = true;
			}
			if ((f.getY() + 50) >= this.getMeasuredHeight()) {
				f.down = false;
			}
			if (f.getY() <= 0) {
				f.down = true;
			}
			/**
			 * Adjusting x/y values here will change fireball speed. Could be
			 * nice for difficulty level settings!!
			 */
			if (f.right) {
				f.x += fireballSpeed;
			} else
				f.x -= fireballSpeed;

			if (f.down) {
				f.y += fireballSpeed;
			} else
				f.y -= fireballSpeed;

			c.drawBitmap(fireScaled, f.getX() - 15, f.getY() - 15, blackPaint);
			// Set a fireball object's hitbox
			f.fireballHitbox.set((int) f.getX(), (int) f.getY(),
					(int) f.getX() + 50, (int) f.getY() + 50);
			// Marble HitBox that follows around the marble.
			ballHitBox.set((int) xPosition, (int) yPosition,
					(int) xPosition + 50, (int) yPosition + 50);
			ball1HitBox.set((int) xPosition1, (int) yPosition1,
					(int) xPosition1 + 50, (int) yPosition1 + 50);

		}

		/**
		 * Set marble image to bitmap. The marble changes its perspective to
		 * indicate how much health is left. Green/blue is healthy. Yellow is
		 * weaker. Red and flashing is critical!
		 * 
		 * @param c
		 *            - The canvas to draw on.
		 */
		public void marbleDraw(Canvas c) {
			if (marbleHealth == 5) {
				Bitmap ball = BitmapFactory.decodeResource(getResources(),
						R.drawable.fivehealth);
				mBitmap = Bitmap.createScaledBitmap(ball, ballWidth,
						ballHeight, true);
			} else if (marbleHealth == 4) {
				Bitmap ball = BitmapFactory.decodeResource(getResources(),
						R.drawable.fourhealth);
				mBitmap = Bitmap.createScaledBitmap(ball, ballWidth,
						ballHeight, true);
			} else if (marbleHealth == 3) {
				Bitmap ball = BitmapFactory.decodeResource(getResources(),
						R.drawable.threehealth);
				mBitmap = Bitmap.createScaledBitmap(ball, ballWidth,
						ballHeight, true);
			} else if (marbleHealth == 2) {
				Bitmap ball = BitmapFactory.decodeResource(getResources(),
						R.drawable.twohealth);
				mBitmap = Bitmap.createScaledBitmap(ball, ballWidth,
						ballHeight, true);
				/**
				 * Toggle parameter allows for flashing marble at critical
				 * health.
				 */
			} else if (marbleHealth == 1 && toggle % 10 == 0) {
				Bitmap ball = BitmapFactory.decodeResource(getResources(),
						R.drawable.onehealth);
				mBitmap = Bitmap.createScaledBitmap(ball, ballWidth,
						ballHeight, true);
				toggle = 0;
			} else {
				Bitmap ballAgain = BitmapFactory.decodeResource(getResources(),
						R.drawable.ball);
				mBitmap = Bitmap.createScaledBitmap(ballAgain, ballWidth,
						ballHeight, true);
			}

			/**
			 * Protects marble from taking multiple hits from a single
			 * collision.
			 */
			if (immunity == 0) {
				if (ballHitBox.intersect(light1) && yellowSwitch == false) {
					marbleHealth -= 1;
					sounds.play(zap, 0.5f, 0.5f, 0, 0, 1.2f);
				}
				if (ballHitBox.intersect(light2) && yellowSwitch == false) {
					marbleHealth -= 1;
					sounds.play(zap, 0.5f, 0.5f, 0, 0, 1.2f);
				}
				marbleFireBallCollision(ballHitBox, flameArray);
				immunity = 8;
			}
			Bitmap bitmap = mBitmap;
			c.drawBitmap(bitmap, xPosition, yPosition, null);
			immunity--;
			toggle++;

			// Marble 2
			if (marbleHealth == 5) {
				Bitmap ball1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.fivehealth);
				mBitmap = Bitmap.createScaledBitmap(ball1, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 4) {
				Bitmap ball1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.fourhealth);
				mBitmap = Bitmap.createScaledBitmap(ball1, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 3) {
				Bitmap ball1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.threehealth);
				mBitmap = Bitmap.createScaledBitmap(ball1, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 2) {
				Bitmap ball1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.twohealth);
				mBitmap = Bitmap.createScaledBitmap(ball1, ballWidth1,
						ballHeight1, true);

				/**
				 * Toggle parameter allows for flashing marble at critical
				 * health.
				 */
			} else if (marbleHealth == 1 && toggle % 10 == 0) {
				Bitmap ball1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.onehealth);
				mBitmap = Bitmap.createScaledBitmap(ball1, ballWidth1,
						ballHeight1, true);
				// toggle = 0;
			} else {
				Bitmap ballAgain1 = BitmapFactory.decodeResource(
						getResources(), R.drawable.ball);
				mBitmap = Bitmap.createScaledBitmap(ballAgain1, ballWidth1,
						ballHeight1, true);
			}

			// Marble 3
			if (marbleHealth == 5) {
				Bitmap ball2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.fivehealth);
				mBitmap = Bitmap.createScaledBitmap(ball2, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 4) {
				Bitmap ball2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.fourhealth);
				mBitmap = Bitmap.createScaledBitmap(ball2, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 3) {
				Bitmap ball2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.threehealth);
				mBitmap = Bitmap.createScaledBitmap(ball2, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 2) {
				Bitmap ball2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.twohealth);
				mBitmap = Bitmap.createScaledBitmap(ball2, ballWidth1,
						ballHeight1, true);
				/**
				 * Toggle parameter allows for flashing marble at critical
				 * health.
				 */
			} else if (marbleHealth == 1 && toggle % 10 == 0) {
				Bitmap ball2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.onehealth);
				mBitmap = Bitmap.createScaledBitmap(ball2, ballWidth1,
						ballHeight1, true);
				// toggle = 0;
			} else {
				Bitmap ballAgain2 = BitmapFactory.decodeResource(
						getResources(), R.drawable.ball);
				mBitmap = Bitmap.createScaledBitmap(ballAgain2, ballWidth1,
						ballHeight1, true);
			}

			// Marble 4
			if (marbleHealth == 5) {
				Bitmap ball3 = BitmapFactory.decodeResource(getResources(),
						R.drawable.fivehealth);
				mBitmap = Bitmap.createScaledBitmap(ball3, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 4) {
				Bitmap ball3 = BitmapFactory.decodeResource(getResources(),
						R.drawable.fourhealth);
				mBitmap = Bitmap.createScaledBitmap(ball3, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 3) {
				Bitmap ball3 = BitmapFactory.decodeResource(getResources(),
						R.drawable.threehealth);
				mBitmap = Bitmap.createScaledBitmap(ball3, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 2) {
				Bitmap ball3 = BitmapFactory.decodeResource(getResources(),
						R.drawable.twohealth);
				mBitmap = Bitmap.createScaledBitmap(ball3, ballWidth1,
						ballHeight1, true);
				/**
				 * Toggle parameter allows for flashing marble at critical
				 * health.
				 */
			} else if (marbleHealth == 1 && toggle % 10 == 0) {
				Bitmap ball3 = BitmapFactory.decodeResource(getResources(),
						R.drawable.onehealth);
				mBitmap = Bitmap.createScaledBitmap(ball3, ballWidth1,
						ballHeight1, true);
				// toggle = 0;
			} else {
				Bitmap ballAgain3 = BitmapFactory.decodeResource(
						getResources(), R.drawable.ball);
				mBitmap = Bitmap.createScaledBitmap(ballAgain3, ballWidth1,
						ballHeight1, true);
			}

			// Marble 5
			if (marbleHealth == 5) {
				Bitmap ball4 = BitmapFactory.decodeResource(getResources(),
						R.drawable.fivehealth);
				mBitmap = Bitmap.createScaledBitmap(ball4, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 4) {
				Bitmap ball4 = BitmapFactory.decodeResource(getResources(),
						R.drawable.fourhealth);
				mBitmap = Bitmap.createScaledBitmap(ball4, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 3) {
				Bitmap ball4 = BitmapFactory.decodeResource(getResources(),
						R.drawable.threehealth);
				mBitmap = Bitmap.createScaledBitmap(ball4, ballWidth1,
						ballHeight1, true);
			} else if (marbleHealth == 2) {
				Bitmap ball4 = BitmapFactory.decodeResource(getResources(),
						R.drawable.twohealth);
				mBitmap = Bitmap.createScaledBitmap(ball4, ballWidth1,
						ballHeight1, true);
				/**
				 * Toggle parameter allows for flashing marble at critical
				 * health.
				 */
			} else if (marbleHealth == 1 && toggle % 10 == 0) {
				Bitmap ball4 = BitmapFactory.decodeResource(getResources(),
						R.drawable.onehealth);
				mBitmap = Bitmap.createScaledBitmap(ball4, ballWidth1,
						ballHeight1, true);
				// toggle = 0;
			} else {
				Bitmap ballAgain4 = BitmapFactory.decodeResource(
						getResources(), R.drawable.ball);
				mBitmap = Bitmap.createScaledBitmap(ballAgain4, ballWidth1,
						ballHeight1, true);
			}

			if (split == true) {

				Bitmap bitmap1 = mBitmap;
				Bitmap bitmap2 = mBitmap;
				Bitmap bitmap3 = mBitmap;
				Bitmap bitmap4 = mBitmap;
				// float current = ticks;
				if (remove == false) {
					xPosition1 = xPosition;
					yPosition1 = yPosition;
					xPosition2 = xPosition;
					yPosition2 = yPosition;
					xPosition3 = xPosition;
					yPosition3 = yPosition;
					xPosition4 = xPosition;
					yPosition4 = yPosition;
				}
				remove = true;
				c.drawBitmap(bitmap1, xPosition1, yPosition1, null);
				c.drawBitmap(bitmap2, xPosition2, yPosition2, null);
				c.drawBitmap(bitmap3, xPosition3, yPosition3, null);
				c.drawBitmap(bitmap4, xPosition4, yPosition4, null);
			}

		}

		/**
		 * Handles drawing of the timer graphic that served as the "finish line" for the level. 
		 * @param c - The canvas to draw on. 
		 */
		public void timerDraw(Canvas c) {
			Bitmap timer = null;
			Bitmap mTimerBitmap = null;
			if (ticks >= 0 && ticks <= tickTimeSpacing) {
				timer = BitmapFactory.decodeResource(res, R.drawable.eighttime);
				mTimerBitmap = Bitmap.createScaledBitmap(timer, timerWidth,
						timerHeight, true);
			} else if (ticks > tickTimeSpacing && ticks <= tickTimeSpacing * 2) {
				timer = BitmapFactory.decodeResource(res, R.drawable.sixtime);
				mTimerBitmap = Bitmap.createScaledBitmap(timer, timerWidth,
						timerHeight, true);

			} else if (ticks * 2 > tickTimeSpacing
					&& ticks <= tickTimeSpacing * 3) {
				timer = BitmapFactory.decodeResource(res, R.drawable.fourtime);
				mTimerBitmap = Bitmap.createScaledBitmap(timer, timerWidth,
						timerHeight, true);

			} else if (ticks > tickTimeSpacing * 3
					&& ticks <= tickTimeSpacing * 4) {
				timer = BitmapFactory.decodeResource(res, R.drawable.twotime);
				mTimerBitmap = Bitmap.createScaledBitmap(timer, timerWidth,
						timerHeight, true);
			} else if (ticks > tickTimeSpacing * 4) {
				timer = BitmapFactory.decodeResource(res, R.drawable.zerotime);
				mTimerBitmap = Bitmap.createScaledBitmap(timer, timerWidth,
						timerHeight, true);
				GameOver = true;
			}

			c.drawBitmap(mTimerBitmap, 40, 40, null); // timer location
		}

	}

	public class AnimTask extends AsyncTask<Void, Void, Void> {
		CustomDrawableView mv;

		public AnimTask(CustomDrawableView mv) {
			this.mv = mv;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			while (!this.isCancelled()) {
				this.publishProgress();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					break;
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... args) {
			mv.invalidate();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		// Creates new Music field.
		// Song file courtesy of Brett Bunn:
		// https://www.youtube.com/watch?v=lGySR2GwrJ4
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp = MediaPlayer.create(this, R.raw.levelone);
		mp.setLooping(true);
		mp.setVolume(100, 100);
		mp.start();
		at.execute();
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onPause() {
		super.onPause();
		at.cancel(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
		mp.stop();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * This method handles collisions between Fireballs and Walls by redirecting
	 * the Fireball when its hitbox intersects with a wall Rect.
	 * @param f - ArrayList of Fireball objects
	 * @param o - ArrayList of wall Rect objects
	 */
	public void fireBallWallCollision(ArrayList<Fireball> f, ArrayList<Rect> o) {
		for (int j = 0; j < f.size(); j++) {
			for (int i = 0; i < o.size(); i++) {
				if (f.get(j).getFireballHitbox().intersect(obstacles.get(i))) {
					Rect intersectbox = new Rect();
					intersectbox.setIntersect(f.get(j).getFireballHitbox(),
							(o.get(i)));

					if (intersectbox.width() >= intersectbox.height()
							&& f.get(j).getFireballHitbox().centerY() < o
									.get(i).centerY()) {
						f.get(j).down = false;
					}
					if (intersectbox.width() > intersectbox.height()
							&& f.get(j).getFireballHitbox().centerY() > o
									.get(i).centerY()) {
						f.get(j).down = true;
					}

					if (intersectbox.height() > intersectbox.width()
							&& f.get(j).getFireballHitbox().centerX() < o
									.get(i).centerX()) {
						f.get(j).right = false;
					}
					if (intersectbox.height() > intersectbox.width()
							&& f.get(j).getFireballHitbox().centerX() > o
									.get(i).centerX()) {
						f.get(j).right = true;
					}
				}
			}
		}
	}

	/**
	 * Method checks the FireBall ArrayList against itself and handles
	 * collisions between two FireBall objects.
	 * @param f - Fireball Array
	 */
	public void FireBallFireBallCollision(ArrayList<Fireball> f) {
		for (int j = 0; j < f.size(); j++) {
			for (int i = 0; i < f.size(); i++) {
				if (f.get(j).getFireballHitbox()
						.intersect(f.get(i).getFireballHitbox())) {
					Rect intersectbox = new Rect();
					intersectbox.setIntersect(f.get(j).getFireballHitbox(),
							(f.get(i).getFireballHitbox()));

					if (intersectbox.width() >= intersectbox.height()
							&& f.get(j).getFireballHitbox().centerY() < f
									.get(i).getFireballHitbox().centerY()) {
						f.get(j).down = true;
						f.get(i).down = false;
						f.get(i).y += -restitution;
						f.get(j).y += restitution;
					}
					if (intersectbox.width() > intersectbox.height()
							&& f.get(j).getFireballHitbox().centerY() > f
									.get(i).getFireballHitbox().centerY()) {
						f.get(j).down = false;
						f.get(i).down = true;
						f.get(i).y += -restitution;
						f.get(j).y += restitution;
					}

					if (intersectbox.height() > intersectbox.width()
							&& f.get(j).getFireballHitbox().centerX() < f
									.get(i).getFireballHitbox().centerX()) {
						f.get(j).right = false;
						f.get(i).right = true;
						f.get(i).x += -restitution;
						f.get(j).x += restitution;
					}
					if (intersectbox.height() > intersectbox.width()
							&& f.get(j).getFireballHitbox().centerX() > f
									.get(i).getFireballHitbox().centerX()) {
						f.get(j).right = true;
						f.get(i).right = false;
						f.get(i).x += -restitution;
						f.get(j).x += restitution;
					}
				}
			}
		}
	}

	/**
	 * This method will handle collisions between the marbles and the walls.
	 * Note that changing the restiution variable at the start of the class will
	 * affect the marble's tendency to "bounce" off the walls.
	 * @param marbleHitBox - The Rect region that defines a marble's collisional boundary.
	 * @param o - The ArrayList of Rects that serve as the level's walls.
	 */
	public void marbleWallCollision(Rect marbleHitBox, ArrayList<Rect> o) {
		for (int i = 0; i < obstacles.size(); i++) {
			if (marbleHitBox.intersect(obstacles.get(i))) {
				Rect intersectbox = new Rect();
				intersectbox.setIntersect(marbleHitBox, (obstacles.get(i)));

				if (intersectbox.width() >= intersectbox.height()
						&& marbleHitBox.centerY() < o.get(i).centerY()) {
					yVel *= -restitution;
					yPosition -= 40;

				}
				if (intersectbox.width() >= intersectbox.height()
						&& marbleHitBox.centerY() > o.get(i).centerY()) {
					yVel *= -restitution;
					yPosition += 40;
				}
				if (intersectbox.height() >= intersectbox.width()
						&& marbleHitBox.centerX() < o.get(i).centerX()) {
					xVel *= -restitution;
					xPosition -= 40;
				}
				if (intersectbox.height() >= intersectbox.width()
						&& marbleHitBox.centerX() > o.get(i).centerX()) {
					xVel *= -restitution;
					xPosition += 40;
				}
			}
		}
	}

	/**
	 * This method subtracts health from the marble upon being hit by a
	 * fireball. Also calls Endgame conditions if health is reduced to zero.
	 * @param marbleHitBox - The player's marble's hitbox radius.
	 * @param f - ArrayList of Fireball objects.
	 */
	public void marbleFireBallCollision(Rect marbleHitBox, ArrayList<Fireball> f) {

		for (int i = 0; i < f.size(); i++) {
			if (marbleHitBox.intersect(f.get(i).getFireballHitbox())) {
				marbleHealth -= 1;
			}
		}
	}

}
