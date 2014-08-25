package edu.virginia.cs2110.marbledungeon;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Kevin Goehring, Alishan Hassan, Nikhil Padmanabhan
 * This class presents the "End Game" screen where a user's score is checked against
 * the high scores and stored if applicable. 
 *
 */
public class EndGame extends ActionBarActivity {
	public final static String NEW_NAME = "edu.virginia.cs2110.marbledungion.NEWNAME";
	public final static String NEW_SCORE = "edu.virginia.cs2110.marbledungion.NEWSCORE";
	String message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_end_game);

		Intent intent = getIntent();
		message = intent.getStringExtra(LevelOne.SCORE);
		TextView textView = (TextView) findViewById(R.id.score1);
		
	    textView.setText(message);
	    // Set the text view as the activity layout
	    TextView enter = (TextView) findViewById(R.id.edit_message);
	    enter.setEnabled(false);
	    
	    if(Integer.parseInt(message)>ScoreScreen.thirdPlace()){
	    	TextView high = (TextView) findViewById(R.id.textView2);
	    	high.setText("High Score!");
	    	enter.setEnabled(true);
	    	
		}
	    
	}
	
	
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
		finish();
		Intent scoresPage = new Intent(this, ScoreScreen.class);
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	String name = editText.getText().toString();
    	scoresPage.putExtra(NEW_NAME, name);
    	scoresPage.putExtra(NEW_SCORE, message);
    	startActivity(scoresPage);

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.end_game, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
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
	
	


}

