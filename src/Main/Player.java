package Main;

/*
Team Name: nullptr
Student Numbers: 14745991

Development-by-Contract Player Interface
*/

import java.awt.Color;
import java.util.ArrayList;

import Game.Army;

public interface Player {
	public void setPlayerName(Integer playernumber);
	public String getPlayerName();
	public void setPlayerColour(Color _playerColour);
	public Color getPlayerColour();
	public int getPlayerNumber();
	public boolean getHuman();
	public void setAvailableArmies(Integer availablearmies);
	public Integer getAvailableArmies();
	public void addPlacedArmies(Army army);
	public ArrayList<Army> getPlacedArmies();
	public Integer getNumOccupiedCountry();
}
