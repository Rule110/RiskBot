package Game;

/*
	Team Name: nullptr
	Student Numbers: 14745991
  
	Class stores information about the players, i.e. Name, Colour. 
	Setting the information involves interacting with the GUI's text field.
*/

import java.awt.Color;
import java.util.ArrayList;

import Deck.Card;
import Main.GameMechanics;

public class Player implements Main.Player{
	private GameMechanics gamemechanics;
	private boolean human;
	private String playerName;
	private Color playerColour;
	private Integer playerNumber;
	private Integer availablearmies;
	private ArrayList<Army> placedarmies;
	private boolean initialreinforcement;
	private boolean lost = false;
	private ArrayList<Card> hand;
	public Player(GameMechanics gamemechanics, boolean human){
		this.gamemechanics = gamemechanics;
		this.human = human;
		if (human){
			this.availablearmies = gamemechanics.getInitialHumanArmySize();
		}
		else {
			this.availablearmies = gamemechanics.getInitialBotArmySize();
		}
		this.placedarmies = new ArrayList<Army>();
		this.initialreinforcement = true;
		this.hand = new ArrayList<Card>();
	}
	public void setPlayerName(Integer playernumber) {
		if (human){
			playerNumber = playernumber;
			gamemechanics.getOutput().updateGameInfoPanel("Enter player " + playerNumber + "'s name:");
			
			playerName = gamemechanics.getInput().getInputCommand();
			
			gamemechanics.getOutput().updateGameInfoPanel("Player " + playerName + " has joined the game");
		}
		else {
			playerName = "Neutral " + String.valueOf(playernumber - 2);
		}
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerColour(Color _playerColour) {
		playerColour = _playerColour;
	}
	
	public Color getPlayerColour() {
		return playerColour;
	}
	
	public int getPlayerNumber() {
		return this.playerNumber;
	}
	public boolean getHuman(){
		return this.human;
	}
	public void setAvailableArmies(Integer availablearmies){
		this.availablearmies = availablearmies;
	}
	public Integer getAvailableArmies(){
		return availablearmies;
	}
	public void addPlacedArmies(Army army){
		this.placedarmies.add(army);
	}
	public void removePlacedArmy(Army armytoremove){
		for (int i = 0; i < placedarmies.size(); i++){
			if (placedarmies.get(i).getCountry().getName().equals(armytoremove.getCountry().getName())){
				this.placedarmies.remove(i);
				i = placedarmies.size();
			}
		}
	}
	public ArrayList<Army> getPlacedArmies(){
		return this.placedarmies;
	}
	public Integer getNumOccupiedCountry(){
		return this.placedarmies.size();
	}
	public void setInitial(boolean bool){
		this.initialreinforcement = bool;
	}
	public boolean getInitial(){
		return initialreinforcement;
	}
	public boolean getLost(){
		return lost;
	}
	public void setLost(boolean lost){
		this.lost = lost;
	}
	public ArrayList<Card> getHand(){
		return this.hand;
	}
	public void addCardToHand(Card card){
		this.hand.add(card);
	}
}
