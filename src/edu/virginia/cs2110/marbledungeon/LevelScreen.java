	package edu.virginia.cs2110.marbledungeon;

import edu.virginia.cs2110.marbledungeon.R;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Displays the level select screen. More buttons will needed to 
 * be added as additional levels are created. 
 * @author Kevin Goehring, Alishan Hassan, Nikhil Padmanabhan
 *
 */
public class LevelScreen extends ActionBarActivity {

	//Declare fields.
	MediaPlayer mp1 = new MediaPlayer();
	ImageButton lvl1Btn, returnBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		
		/**
		 * Activity is FULLSCREEN
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_level_screen);

		/**
		 * Sets custom font for LevelScreen header TextView. Easily adjustable
		 * by changing Typeface tf variable. Current choice is "Faith Collapsing"
		 * by user nihilschiz via http://www.dafont.com/faith-collapsing.font
		 */
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/faith.tff");
		TextView contentView = (TextView) findViewById(R.id.fullscreen_content);
		contentView.setTypeface(tf);
		
		addListenerOnButton();
		
	}

	/**
	 * Sets results of clicking various LevelScreen buttons. 
	 */
	public void addListenerOnButton() {
		 
		returnBtn = (ImageButton) findViewById(R.id.return_button);
		returnBtn.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) { 
				finish();
			}
		});
		
		//TODO NEEDS TO OPEN LEVEL 1 WHEN ITS CREATED
		//Sets results of clicking the "Change Settings" button. 
		lvl1Btn = (ImageButton) findViewById(R.id.level1_button);
		lvl1Btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Intent lvlOne = new Intent(LevelScreen.this, LevelOne.class);
				finish();
				startActivity(lvlOne);
			}
		});
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Creates new Music field. 
		// Song file courtesy of Oliver Iacano: https://www.youtube.com/watch?v=QiOFAXW0akg
		mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp1 = MediaPlayer.create(this, R.raw.vidgametune2);
		mp1.setLooping(true);
		mp1.setVolume(100, 100);
		mp1.start();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mp1.stop();
	}

}
