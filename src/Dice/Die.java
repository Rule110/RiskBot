package Dice;

/*
Team Name: table_1
Student Numbers: 14480278, 14461158, 14745991

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
