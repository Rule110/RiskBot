package Main;

/*
Team Name: nullptr
Student Numbers: 14745991

Development-by-Contract GameMechanics Interface
*/

import java.util.ArrayList;

import javax.swing.JTextField;

import Dice.Die;
import GUI.Output;
import Game.Army;
import Game.Continent;
import Game.Country;
import Game.Player;
import Input.Input;

public interface GameMechanics {
	public JTextField getInputField();
	public void setOutput(Output output);
	public Output getOutput();
	public void setInput(Input input);
	public Input getInput();
	public void setContinentList();
	public ArrayList<Continent> getContinentList();
	public void setCountryList();
	public ArrayList<Country> getCountryList();
	public void setArmyList(Player player, Country country, Integer armysize);
	public ArrayList<Army> getArmyList();
	public void setPlayerList(ArrayList<Player> playerlist);
	public ArrayList<Player> getPlayerList();
	public void setDeck();
	public void setDice();
	public Die getDice();
	public Integer getInitialHumanArmySize();
	public Integer getInitialBotArmySize();
	public void initialiseGameMap();
	public void setReinforceMechanics();
	public void initialReinforce();
	public void setCombatMechanics();
	public void setFortifyMechanics();
	public Deck getDeck();
}
