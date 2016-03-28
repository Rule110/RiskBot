package Game;

/*
Team Name: nullptr
Student Numbers: 14745991

GameMechanics class
*/

import GUI.MapConstants;
import Input.Input;
import java.util.ArrayList;
import javax.swing.JTextField;
import GUI.Output;
import Deck.Deck;
import Dice.Die;

public class GameMechanics implements Main.GameMechanics{
	private boolean endgame = false;
	private JTextField tf;
	private Output output;
	private Input input;
	private ArrayList<Country> countrylist;
	private ArrayList<Army> armylist;
	private ArrayList<Player> playerlist;
	private ArrayList<Continent> continentlist;
	private Deck deck;
	private Die die;
	private Reinforce reinforcemechanics;
	private Combat combatmechanics;
	private Fortify fortifymechanics;
	private Integer initialhumanarmysize = 36;
	private Integer initialbotarmysize = 24;
	public GameMechanics(){
		this.tf = new JTextField();
		this.armylist = new ArrayList<Army>();
	}
	public JTextField getInputField(){
		return tf;
	}
	public void setOutput(Output output){
		this.output = output;
	}
	public Output getOutput(){
		return this.output;
	}
	public void setInput(Input input){
		this.input = input;
	}
	public Input getInput(){
		return this.input;
	}
	public void setContinentList(){
		this.continentlist = new ArrayList<Continent>();
		for (int i = 0; i < MapConstants.NUM_CONTINENTS; i++){
			continentlist.add(new Continent(i));
		}
	}
	public ArrayList<Continent> getContinentList(){
		return this.continentlist;
	}
	public void setCountryList(){
		this.countrylist = new ArrayList<Country>();
		for (int i = 0; i < MapConstants.COUNTRY_COORD.length; i++){
			countrylist.add(new Country(i, this));
		}
		for (int i = 0; i < MapConstants.COUNTRY_COORD.length; i++){
			countrylist.get(i).setAdjacentCountries(MapConstants.ADJACENT[i]);
		}
		for (Continent continent: continentlist){
			continent.setCountriesInContinent(countrylist);
		}
	}
	public ArrayList<Country> getCountryList(){
		return this.countrylist;
	}
	public void setArmyList(Player player, Country country, Integer armysize){
		boolean found = false;
		int i = 0;
		while (!found && i < armylist.size()){
			if (armylist.get(i).getCountry() == country){
				found = true;
				if (armylist.get(i).getPlayer() != player){
					armylist.get(i).getPlayer().removePlacedArmy(armylist.get(i));
					armylist.get(i).setPlayer(player);
					player.addPlacedArmies(armylist.get(i));
				}
				armylist.get(i).setSize(armysize);
				output.updateMapPanel();
				player.setAvailableArmies(player.getAvailableArmies() - armysize);
			}
			else {
				i++;
			}
		}
		if (!found){
			Army newarmy = new Army(armysize, player, country);
			armylist.add(newarmy);
			output.updateMapPanel();
			player.addPlacedArmies(newarmy);
			player.setAvailableArmies(player.getAvailableArmies() - armysize);
			country.setArmy(newarmy);
		}
	}
	public ArrayList<Army> getArmyList(){
		return this.armylist;
	}
	public void setPlayerList(ArrayList<Player> playerlist){
		this.playerlist = playerlist;
	}
	public ArrayList<Player> getPlayerList(){
		return this.playerlist;
	}
	public void setDeck(){
		this.deck = new Deck();
		this.deck.setCountryList(this.countrylist);
	}
	public void setDice(){
		this.die = new Die();
	}
	public Die getDice(){
		return this.die;
	}
	public Integer getInitialHumanArmySize(){
		return this.initialhumanarmysize;
	}
	public Integer getInitialBotArmySize(){
		return this.initialbotarmysize;
	}
	public void initialiseGameMap(){
		while (!deck.isEmpty()){
			for (Player player : playerlist){
				if (player.getHuman()){
					output.updateGameInfoPanel(player.getPlayerName() + " enter any character to draw a card!");
					input.getInputCommand();		
				}
				Country card = deck.getCountryCard();
				this.setArmyList(player, card, 1);
				output.updateGameInfoPanel(player.getPlayerName() + " drew the country card:  " + card.getName() + "  !");			
			}
		}
	}
	public void setReinforceMechanics(){
		this.reinforcemechanics = new Reinforce(this);
	}
	public void initialReinforce(){
		output.updateGameInfoPanel("Roll the dice to determine who is first to Reinforce!");
		ArrayList<Player> dynamicplayerlist = new ArrayList<Player>();
		Integer index = this.decideFirst();
		dynamicplayerlist.add(playerlist.get(index));
		for (Player player: playerlist){
			if (player != dynamicplayerlist.get(0)){
				dynamicplayerlist.add(player);
			}
		}
		Integer players2reinforce;
		do{
			players2reinforce = 6;
			for (Player player : dynamicplayerlist){
				if (player.getAvailableArmies() > 0){
					this.reinforcemechanics.setReinforcements(player);
				}
				else {
					player.setInitial(false);
					players2reinforce--;
				}
			}
		} while(players2reinforce > 0);
	}
	public void setCombatMechanics(){
		this.combatmechanics = new Combat(this);
	}
	public void setFortifyMechanics(){
		this.fortifymechanics = new Fortify(this);
	}
	private Integer decideFirst(){
		boolean draw;
		ArrayList<Integer> rolls = new ArrayList<Integer>();
		Integer index = 0;
		do{
			for (int i = 0; i < 2; i++){
				this.getOutput().updateGameInfoPanel(playerlist.get(i).getPlayerName() + " press any character to roll the dice!");
				this.getInput().getInputCommand();
				die.roll();
				Integer roll = die.getFace();
				rolls.add(roll);
				this.getOutput().updateGameInfoPanel(playerlist.get(i).getPlayerName() + " rolled a " + String.valueOf(die.getFace()));
			}
			if (rolls.get(0) == rolls.get(1)){
				draw = true;
				this.getOutput().updateGameInfoPanel("It's a draw! Let's roll again!");
			}
			else if (rolls.get(0) > rolls.get(1)){
				draw = false;
				index = 0;
				this.getOutput().updateGameInfoPanel(playerlist.get(index).getPlayerName() + " rolled the highest!");
			}
			else {
				draw = false;
				index = 1;
				this.getOutput().updateGameInfoPanel(playerlist.get(index).getPlayerName() + " rolled the highest!");
			}
			rolls.clear();
		} while (draw);
		return index;
	}
	public void startGame(){
		output.updateGameInfoPanel("Roll the dice to determine who takes the first turn!");
		ArrayList<Player> dynamicplayerlist = new ArrayList<Player>();
		Integer index = this.decideFirst();
		dynamicplayerlist.add(playerlist.get(index));
		for (Player player: playerlist){
			if (player != dynamicplayerlist.get(0)){
				dynamicplayerlist.add(player);
			}
		}
		do {
			for (Player player : dynamicplayerlist){
				if (player.getNumOccupiedCountry() > 0){
					this.takeTurn(player);
				}
				else if (player.getHuman()){
					this.getOutput().updateGameInfoPanel(player.getPlayerName() + " has lost!");
					this.endgame = true;
					System.exit(0);
				}
				else {
					this.getOutput().updateGameInfoPanel(player.getPlayerName() + " has lost!");
				}
			}
		} while (!endgame);
	}
	private void takeTurn(Player player){
		this.reinforcemechanics.setReinforcements(player);
		if (player.getHuman()){
			this.combatmechanics.setCombat(player);
			//this.fortifymechanics.setFortify(player);	
		}
	}
}
