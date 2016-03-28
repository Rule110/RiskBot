package Dice;

/*
Team Name: nullptr
Student Numbers: 14745991

Dice Die class
*/

import Main.Dice;

public class Die implements Dice {
	private int diceFace;
	
	//constructor
	public Die(){
		roll();
	}
	
	//assign random number between 1 and 6 to diceFace
	public void roll(){
		diceFace = (int)(Math.random() * 6)+1;
	}
	
	//return diceFace
	public int getFace(){
		return diceFace;
	}
}
