package edu.virginia.cs2110.marbledungeon;




/**
 * Class defines a "ScoreBoard" object that will hold a player's name 
 * and final score to see if they can get on the scoreboard. 
 * @author Kevin Goehring, Alishan Hassan, Nikhil Padmanabhan
 *
 */
public class ScoreBoard implements Comparable<ScoreBoard> {

	//Fields
	private String name; 
	private int score;
	public ScoreBoard() {
	}
	
	public ScoreBoard(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName() {
		this.name = name;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore() {
		this.score = score;
	}

	  public boolean equals(Object o) {
		  boolean result = false;
	        if (o instanceof ScoreBoard) {
	        	ScoreBoard s = (ScoreBoard) o;
	        	result = s.getScore() == this.getScore() && 
	        			s.getName().equals(this.getName());
	        }
	        return result;
	    }
	
	@Override
	public int compareTo(ScoreBoard o) {
		int result;
		result = o.score - this.score;
		if(result == 0) {
			return -1;
		}
		if(result >= 0){
			return 1;
		}
		else {
			return result = -1;
		}
	}
	
	
}
