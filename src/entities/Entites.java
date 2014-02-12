package entities;

import game.Animation;

public class Entites {
	// Player Animations
	public static Animation standingPlayer = null;
	public static Animation walkingPlayer = null;
	public static boolean hasEntitesFinished = false;
	public static void main(String args[]){
		
	}
	// Load Player animations
	public static void loadPlayer(){
		// Load all possible player animations
		walkingPlayer = new Animation("Animations/Walking Man","Walking Man",1,38);
		standingPlayer = new Animation("Animations/Walking Man","Standing Man",1);
		//walkingPlayer = standingPlayer;
		System.out.println("Loaded Players");
	}
}
