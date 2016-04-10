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
}
