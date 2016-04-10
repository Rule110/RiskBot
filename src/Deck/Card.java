package Deck;

import Game.Country;

public class Card {
	private Country territory;
	private String insignia;
	Card(Country territory, Integer insignia){
		this.territory = territory;
		switch (insignia){
			case 0:		this.insignia = "Infantry";
						break;
			case 1:		this.insignia = "Cavalry";
						break;
			case 2:		this.insignia = "Artillery";
						break;
			case 3:		this.insignia = "Wild";
						break;
			default:	this.insignia = "Error";
						break;
		}
	}
	public Country getTerritory(){
		return this.territory;
	}
	public String getInsignia(){
		return this.insignia;
	}
}
