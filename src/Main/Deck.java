package Main;

/*
Team Name: nullptr
Student Numbers: 14745991

Development-by-Contract Deck Interface
*/

import Deck.Card;
import Game.Country;

import java.util.ArrayList;

public interface Deck {
	public void setCountryList(ArrayList<Country> countrylist);
	public Country getCountryCard();
	public boolean isEmpty();
	public Card drawTerritoryCard();
}
