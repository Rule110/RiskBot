package Deck;

import Game.Country;

public class Card {
	private Country territory;
	private Integer insignia;
	Card(Country territory, Integer insignia){
		this.territory = territory;
		this.insignia = insignia;
	}
	public Country getTerritory(){
		return this.territory;
	}
	public Integer getInsignia(){
		return this.insignia;
	}
	public String toString(){
		String insignia_str = "";
		switch (this.insignia){
		case 0:		insignia_str = "Infantry";
					break;
		case 1:		insignia_str = "Cavalry";
					break;
		case 2:		insignia_str = "Artillery";
					break;
		case 3:		insignia_str = "Wild";
					break;
		default:	insignia_str = "Error";
					break;
	}
		return this.territory.getName() + " ; " + insignia_str;
	}
}
