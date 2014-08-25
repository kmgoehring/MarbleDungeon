package edu.virginia.cs2110.marbledungeon;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import edu.virginia.cs2110.marbledungeon.R;
import edu.virginia.cs2110.marbledungeon.util.SystemUiHider;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * @author Kevin M. Goehring, Alishan Hassan, Nikhil Padmanabhan
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	
	// Declare fields; media player for audio and various menu buttons. 
	MediaPlayer mp = new MediaPlayer();
	ImageButton lvlBtn, settingBtn, scoreBtn;


	
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = Integer.MAX_VALUE;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
		/**
		 * Activity is FULLSCREEN Screen timeout is turned OFF during gameplay.
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		
		setContentView(R.layout.activity_fullscreen);
		/**
		 * Sets custom font for Screen header TextView. Easily adjustable
		 * by changing Typeface tf variable. Current choice is "Faith Collapsing"
		 * by user nihilschiz via http://www.dafont.com/faith-collapsing.font
		 */
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/faith.tff");
		TextView contentView = (TextView) findViewById(R.id.fullscreen_content);
		contentView.setTypeface(tf);

		/**
		 * Calls method for opening various menu screens via buttons.
		 */
		addListenerOnButton();
		
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
						
					
					}
				});
		
		

		//Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) { 
				if(mp.isPlaying()) {
					onPause();
					mp.pause();
				}else {
					mp.start();
				}
					onPause();
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});
	}

	public void addListenerOnButton() {
		 
		//Sets results of clicking the "Choose Level" button. 
		//Opens the LevelScreen activity. 
		lvlBtn = (ImageButton) findViewById(R.id.level_button);
		lvlBtn.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				Intent lvlScrn = new Intent(FullscreenActivity.this, LevelScreen.class);
				startActivity(lvlScrn);
			}
		});
		
		//Sets results of clicking the "Change Settings" button. 
		//Opens the SettingsScreen activity. 
		settingBtn = (ImageButton) findViewById(R.id.settings_button);
		settingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent setScrn = new Intent(FullscreenActivity.this, SettingsScreen.class);
				startActivity(setScrn);
			}
		});
		
		//Sets the results of clicking the "High Scores" button. 
		//Opens the ScoreScreen activity.
		scoreBtn = (ImageButton) findViewById(R.id.score_button);
		scoreBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent scoreScrn = new Intent(FullscreenActivity.this, ScoreScreen.class);
				startActivity(scoreScrn);
			}
		});
	}

	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.show();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//Creates new Music field. 
		//Song file courtesy of Oliver Iacano: https://www.youtube.com/watch?v=NbPORSbr6To
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp = MediaPlayer.create(this, R.raw.vidgametune);
		mp.setLooping(true);
		mp.setVolume(100, 100);
		mp.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		mp.stop();
	}
	

}
