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
	private ArrayList<Country> countrycards;
	private ArrayList<Card> territorycards;
	
	public void setCountryList(ArrayList<Country> countrylist){
		this.countrycards = new ArrayList<Country>();
		for (Country country : countrylist){
			this.countrycards.add(country);
		}
		this.setTerritoryCards(countrylist);
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
	
	private void setTerritoryCards(ArrayList<Country> countrylist){
		this.territorycards = new ArrayList<Card>();
		int i = 0;
		for (Country country : countrylist){
			Integer insignia = DeckConstants.insignia[i++];
			Card newcard = new Card(country, insignia);
			this.territorycards.add(newcard);
		}
		this.territorycards.add(new Card(null, DeckConstants.insignia[i++]));
		this.territorycards.add(new Card(null, DeckConstants.insignia[i]));
	}
	
	public Card drawTerritoryCard(){
		Collections.shuffle(territorycards);
		Card territorycard = territorycards.get(0);
		territorycards.remove(0);
		return territorycard;
	}
}
