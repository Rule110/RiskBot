package Game;
/*
Team Name: nullptr
Student Numbers: 14745991

Continent contains all the countries on a continent and can check if a player owns it
*/
import java.util.ArrayList;
import GUI.MapConstants;

public class Continent {
	private ArrayList<Country> countriesincontinent;
	private Integer id;
	private String name;
	private Integer value;
	
	Continent(Integer id){
		this.id = id;
		this.name = MapConstants.CONTINENT_NAMES[id];
		this.value = MapConstants.CONTINENT_VALUES[id];
		this.countriesincontinent = new ArrayList<Country>();
	}
	
	void setCountriesInContinent(ArrayList<Country> countrylist){
		for (int i = 0; i < MapConstants.CONTINENT_IDS.length; i++){
			if (MapConstants.CONTINENT_IDS[i] == id){
				countriesincontinent.add(countrylist.get(i));
			}
		}
	}
	
	public Integer getID(){
		return id;
	}
	
	String getName(){
		return name;
	}
	
	Integer getValue(){
		return value;
	}
	
	ArrayList<Country> getCountriesInContinent(){
		return countriesincontinent;
	}
}
