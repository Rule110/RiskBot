package Main;

/*
Team Name: table_1
Student Numbers: 14480278, 14461158, 14745991

Development-by-Contract Deck Interface
*/

import Game.Country;

import java.util.ArrayList;

public interface Deck {
	public void setCountryList(ArrayList<Country> countrylist);
	public Country getCountryCard();
	public boolean isEmpty();
}
