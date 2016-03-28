package Deck;

/*
Team Name: nullptr
Student Numbers: 14745991

Card Deck class for drawing cards (Only Country cards at the moment
*/

import java.util.ArrayList;
import java.util.Collections;
import Game.Country;

public class Deck implements Main.Deck {
	
	public void setCountryList(ArrayList<Country> countrylist){
		this.countrycards = new ArrayList<Country>();
		for (Country country : countrylist){
			this.countrycards.add(country);
		}
	}
	public Country getCountryCard(){
		Collections.shuffle(countrycards);
		Country countrycard = countrycards.get(0);
		countrycards.remove(0);
		return countrycard;
	}
	public boolean isEmpty(){
		boolean empty;
		if (countrycards.size() == 0){
			empty = true;
		}
		else {
			empty = false;
		}
		return empty;
	}
	private ArrayList<Country> countrycards;
}
