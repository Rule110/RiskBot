package Game;

/*
Team Name: nullptr
Student Numbers: 14745991

The class that stores Country information
*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import GUI.MapConstants;

public class Country {
	private Dimension panel_size;
	private int id;
	private String name;
	private Integer[] coords;
	private Integer radius;
	private ArrayList<Country> adjacent;
	private ArrayList<Country> othercountries;
	private Continent continent;
	private Army army;
	private Color color;
	private Font font;
	private GameMechanics gamemechanics;
	
	public Country(Integer arrayindex, GameMechanics gamemechanics){
		this.gamemechanics = gamemechanics;
		this.panel_size = this.gamemechanics.getOutput().getPanelSize();
		id = arrayindex;
		radius = (int)(20*MapConstants.SCALING_CONSTANT);
		this.name = MapConstants.COUNTRY_NAMES[arrayindex];
		this.setCoords(arrayindex);
		this.othercountries = gamemechanics.getCountryList();
		continent = gamemechanics.getContinentList().get(MapConstants.CONTINENT_IDS[arrayindex]);
		this.setColor();
		this.setFont();
	}
	
	private void setCoords(Integer arrayindex){
		coords = new Integer[2];
		coords[0] = (int)(MapConstants.COUNTRY_COORD[arrayindex][0]*(panel_size.getWidth() / MapConstants.FRAME_WIDTH));
		coords[1] = (int)(MapConstants.COUNTRY_COORD[arrayindex][1]*MapConstants.SCALING_CONSTANT);
	}
	
	public Integer getXCoords(){
		return coords[0];
	}
	
	public Integer getYCoords(){
		return coords[1];
	}
	
	public String getName(){
		return name;
	}
	
	public int getID(){
		return id;
	}
	
	public Integer getRadius(){
		return radius;
	}
	
	public void setAdjacentCountries(int[] links){
		this.adjacent = new ArrayList<Country>();
		for (int j = 0; j < links.length; j++){
			adjacent.add(othercountries.get(links[j]));
		}
	}
	
	public ArrayList<Country> getAdjacentCountries(){
		return adjacent;
	}
	
	private	void setColor(){
		Integer R = MapConstants.CONT_COLORS[continent.getID()][0];
		Integer G = MapConstants.CONT_COLORS[continent.getID()][1];
		Integer B = MapConstants.CONT_COLORS[continent.getID()][2];
		this.color = new Color(R, G, B);
	}
	
	public Color getColor(){	
		return color;
	}
	
	private void setFont(){
		Integer fontsize = (int)(12 * (panel_size.getWidth()) / MapConstants.FRAME_WIDTH);
		this.font = new Font(MapConstants.CONT_FONTS[continent.getID()], Font.BOLD, fontsize);
	}
	
	public Font getFont(){
		return font;
	}
	
	public Continent getContinent(){
		return continent;
	}
	
	public Army getArmy(){
		return army;
	}
	
	public void setArmy(Army army){
		this.army = army;
	}
}
