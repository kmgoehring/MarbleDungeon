package edu.virginia.cs2110.marbledungeon;

import java.util.ArrayList;
import java.util.Collections;

import edu.virginia.cs2110.marbledungeon.R;
import android.support.v7.app.ActionBarActivity;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * This activity will display the "Scoreboard" screen using ScoreBoard 
 * objects that hold Strings for names and scores. It will check to see, 
 * upon completion of the game, if the newest score is worthy of placing
 * on the scoreboard and update it accordingly. 
 * @author Kevin Goehring, Alishan Hassan, Nikhil Padmanabhan
 *
 */
//TODO Known bug: A single game score is being recorded twice and the screen to enter initials for
// a score is also being displayed twice. 


public class ScoreScreen extends ActionBarActivity  {
	//Fields. 
	MediaPlayer mp2 = new MediaPlayer();
	ImageButton returnBtn;
	static int thirdPlace;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		/**
		 * Activity is FULLSCREEN
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_score_screen);

		/**
		 * Sets custom font for Scoreboard header TextView. Easily adjustable
		 * by changing Typeface tf variable. Current choice is "Faith Collapsing"
		 * by user nihilschiz via http://www.dafont.com/faith-collapsing.font
		 */
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/faith.tff");
		TextView contentView = (TextView) findViewById(R.id.fullscreen_content);
		contentView.setTypeface(tf);

		/**
		 * Sets TextViews to custom fonts for score names/numbers. 
		 * Easily adjustable by changing Typeface tf2 variable declaration. 
		 * Current choice is "Olde English" by Dieter Steffman via 
		 * http://www.dafont.com/olde-english.font
		 */
		Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/oldeng.ttf");
		TextView nameView1 = (TextView) findViewById(R.id.scoreName1);
		nameView1.setTypeface(tf2);
		TextView scoreView1 = (TextView) findViewById(R.id.scoreNumber1);
		scoreView1.setTypeface(tf2);
		TextView nameView2 = (TextView) findViewById(R.id.scoreName2);
		nameView2.setTypeface(tf2);
		TextView scoreView2 = (TextView) findViewById(R.id.scoreNumber2);
		scoreView2.setTypeface(tf2);
		TextView nameView3 = (TextView) findViewById(R.id.scoreName3);
		nameView3.setTypeface(tf2);
		TextView scoreView3 = (TextView) findViewById(R.id.scoreNumber3);
		scoreView3.setTypeface(tf2);
		
		/**
		 * Gets the new high score and name
		 */
		Intent intent = getIntent();
		String newName = intent.getStringExtra(EndGame.NEW_NAME);
		String newScore = intent.getStringExtra(EndGame.NEW_SCORE);
		ScoreBoard userGame = new ScoreBoard();
		
		/**
		 * Create initial game "dummy" scoreboard names and values. 
		 */
		ScoreBoard gold = new ScoreBoard("Kevin", 9999999);
		ScoreBoard silver = new ScoreBoard("Somebody", 4800);
		ScoreBoard bronze = new ScoreBoard("You", 0);
		
		if(newScore != null) {
		userGame = new ScoreBoard(newName, Integer.parseInt(newScore));
		}
		
		/**
		 * Add high scores and user score to ArrayList and then sort by score. 
		 */
		
		SharedPreferences prefs = this.getSharedPreferences("MarbleKey", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		
		
		//boolean a = prefs.getBoolean("MarbleKey",false);
		
		ArrayList<ScoreBoard> topScores = new ArrayList<ScoreBoard>();
		

		
			SharedPreferences trial = this.getSharedPreferences("MarbleKey", Context.MODE_PRIVATE);
			bronze = new ScoreBoard(trial.getString("GoldName", null)
					,trial.getInt("GoldKey", 0));
			silver = new ScoreBoard(trial.getString("SilverName", null)
					,trial.getInt("SilverKey", 0));
			gold = new ScoreBoard(trial.getString("BronzeName", null)
					,trial.getInt("BronzeKey", 0));
			if(newScore != null) {
			topScores.add(userGame);
			}
			topScores.add(bronze);
			topScores.add(silver);
			topScores.add(gold);
			Collections.sort(topScores);
		
		
	/**
	 *  Put high scores and names in the SharedPrefence editor for saving. 
	 */
		editor.putInt("GoldKey", topScores.get(0).getScore());
		editor.putInt("SilverKey", topScores.get(1).getScore());
		editor.putInt("BronzeKey", topScores.get(2).getScore());
		
		editor.putString("GoldName", topScores.get(0).getName());
		editor.putString("SilverName", topScores.get(1).getName());
		editor.putString("BronzeName",topScores.get(2).getName());
		editor.commit();
		
		/**
		 * Get the saved preferences and set them to textViews for display on 
		 * the Scoreboard screen. 
		 */
		SharedPreferences prefsTwo = this.getSharedPreferences("MarbleKey", Context.MODE_PRIVATE);
		int scoreGold = prefsTwo.getInt("GoldKey", 0); //0 is the default value
		int scoreSilver = prefsTwo.getInt("SilverKey", 0);
		int scoreBronze = prefsTwo.getInt("BronzeKey", 0);
		
		String nameGold = prefsTwo.getString("GoldName", "");
		String nameSilver = prefsTwo.getString("SilverName", "");
		String nameBronze = prefsTwo.getString("BronzeName", "");
		/**
		 * Sets the sorted top 3 scores and names on the corresponding TextViews
		 * for display. 
		 */
		nameView1.setText(nameGold/*topScores.get(0).getName()*/);
		scoreView1.setText(""+scoreGold/*Integer.toString(topScores.get(0).getScore())*/);
		nameView2.setText(nameSilver/*topScores.get(1).getName()*/);
		scoreView2.setText(""+scoreSilver/*Integer.toString(topScores.get(1).getScore())*/);
		nameView3.setText(nameBronze/*topScores.get(2).getName()*/);
		scoreView3.setText(""+scoreBronze/*Integer.toString(topScores.get(2).getScore())*/);
		
		
		//sets the value of thirdPlace
		thirdPlace = scoreBronze;
		
		
		//Enable listener for returnButton
		addListenerOnButton();
	}	
    
	
	public static int thirdPlace(){ 
		return thirdPlace;
	}
	
	/**
	 * Returns user to Main Menu screen upon clicking returnButton
	 */
	public void addListenerOnButton() {
		 
		//Sets results of clicking the "Return to Main menu" button. 
		//Opens FullscreenActivity. 
		returnBtn = (ImageButton) findViewById(R.id.return_button);
		returnBtn.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		// Creates new Music field.
		// Song file courtesy of Oliver Iacano:
		// https://www.youtube.com/watch?v=rMRhpMm_76c
		mp2.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp2 = MediaPlayer.create(this, R.raw.vidgametune3);
		mp2.setLooping(true);
		mp2.setVolume(100, 100);
		mp2.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		mp2.stop();
	}
	
}

