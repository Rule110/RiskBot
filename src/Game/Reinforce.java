package Game;

/*
Team Name: nullptr
Student Numbers: 14745991

Reinforcement class
*/

import java.util.ArrayList;
import Deck.Card;
public class Reinforce {
	private GameMechanics gamemechanics;
	private ArrayList<Army> matches;
	private Integer tradedinsets;
	private boolean ownedterritorycardbonus;
	private Integer territoryreinforcements;
	
	public Reinforce(GameMechanics gamemechanics){
		this.gamemechanics = gamemechanics;
		this.matches = new ArrayList<Army>();
		this.tradedinsets = 0;
		this.territoryreinforcements = 2;
	}
	
	void setReinforcements(Player player){
		if (player.getHuman()){
			if (player.getInitial() == true){
				this.reinforceHuman(player, 3);
			}
			else {
				this.setAvailableReinforcements(player);
				while (player.getAvailableArmies() > 0){
					String num;
					boolean loop = true;
					do {
						do {
							gamemechanics.getOutput().updateGameInfoPanel(
								player.getPlayerName() + " Enter number of reinforcements to place out of the "
									+ player.getAvailableArmies() + " available:");
							num = gamemechanics.getInput().getInputCommand();
						} while (this.isNotANumber(num));
						if (Integer.parseInt(num) > player.getAvailableArmies()){
							gamemechanics.getOutput().updateGameInfoPanel(
								player.getPlayerName() + " the number entered is bigger than the  "
								+ player.getAvailableArmies() + " reinforcements available! Try again!");
							loop = true;
						}
						else if (Integer.parseInt(num) < 1){
							gamemechanics.getOutput().updateGameInfoPanel(
								player.getPlayerName() + " the number is less than 1! You must place at least 1!");
							loop = true;
						}
						else {
							loop = false;
						}
					} while (loop);
					gamemechanics.getOutput().updateGameInfoPanel(
							player.getPlayerName() + " You will be placing " + num + " reinforcements on a country you will now choose:");
					this.reinforceHuman(player, Integer.parseInt(num));
				}
			}
		}
		else {
			this.reinforceBot(player);
		}
	}
	
	private void reinforceHuman(Player player, Integer num){
		do {
			gamemechanics.getOutput().updateGameInfoPanel(
					player.getPlayerName() + " Enter unambiguous country name (your armies already occupy) to reinforce it:");
			String input = gamemechanics.getInput().getInputCommand();
			for (Army army : player.getPlacedArmies()){
				String countryname = army.getCountry().getName();
				if (countryname.toLowerCase().contains(input.toLowerCase())){
					matches.add(army);
				}
			}
			this.checkMatches(player, num);
		} while (matches.size() == 0);
		this.matches.clear();
	}
	
	private void reinforceBot(Player player){
		Integer i = (int) Math.floor(Math.random() * player.getPlacedArmies().size());
		Army army = player.getPlacedArmies().get(i);
		army.setSize(army.getSize() + 1);
		player.setAvailableArmies(player.getAvailableArmies() - 1);
	}
	
	private void checkMatches(Player player, Integer num){
		switch (matches.size()){
			case 0:	gamemechanics.getOutput().updateGameInfoPanel(player.getPlayerName() +
						" No matches found for that word in the list of countries you occupy!");
					break;
			case 1:	matches.get(0).setSize(matches.get(0).getSize() + num);
					player.setAvailableArmies(player.getAvailableArmies() - num);
					gamemechanics.getOutput().updateGameInfoPanel(
							player.getPlayerName() + " reinforced " + matches.get(0).getCountry().getName() + "!");
					break;
			default:	gamemechanics.getOutput().updateGameInfoPanel(player.getPlayerName() +
							" Ambiguous name for country given! Please try again!");
						matches.clear();
						break;
		}
		gamemechanics.getOutput().updateMapPanel();
	}
	
	private boolean isNotANumber(String str){
		try{
			Integer.parseInt(str);
			return false;
		} catch (NumberFormatException e){
			gamemechanics.getOutput().updateGameInfoPanel("Not a number!");
			return true;
		}
	}
	
	private void setAvailableReinforcements(Player player){
		Integer reinforcements = player.getPlacedArmies().size() / 3;
		if (reinforcements < 3){
			reinforcements = 3;
		}
		reinforcements += getContinentReinforcements(player);
		reinforcements += getTerritoryCardReinforcements(player);
		gamemechanics.getOutput().updateGameInfoPanel(
				player.getPlayerName() + " you will have " + reinforcements + " reinforcements available to place!");
		player.setAvailableArmies(reinforcements);
	}
	
	private Integer getContinentReinforcements(Player player){
		Integer extrareinforcements = 0;
		for (Continent continent : gamemechanics.getContinentList()){
			boolean fullyoccupied = true;
			int i = 0;
			while (fullyoccupied && i < continent.getCountriesInContinent().size()){
				Country country = continent.getCountriesInContinent().get(i++);
				boolean found = false;
				int j = 0;
				while (!found && j < player.getPlacedArmies().size()){
					Army army = player.getPlacedArmies().get(j++);
					if (army.getCountry().getID() == country.getID()){
						found = true;
					}
				}
				if (!found){
					fullyoccupied = false;
				}
			}
			if (fullyoccupied){
				extrareinforcements += continent.getValue();
			}
		}
		return extrareinforcements;
	}
	
	private Integer getTerritoryCardReinforcements(Player player){
		Integer territorycardreinforcements = 0;
		if (player.getHand().size() > 2){
			this.gamemechanics.getOutput().updateGameInfoPanel(player.getPlayerName() +
					" these are the territory cards in your hand: ");
			String playerhand_str = "";
			for (Card card : player.getHand()){
				playerhand_str += card + ",\t";
			}
			this.gamemechanics.getOutput().updateGameInfoPanel(playerhand_str);
			if (player.getHand().size() > 4){
				this.gamemechanics.getOutput().updateGameInfoPanel(
						"You have greater than five territory cards. You must exchange a set!\n");
				territorycardreinforcements = this.exchange(player);
			}
			else if (hasSet(player.getHand())){
				this.gamemechanics.getOutput().updateGameInfoPanel(
						"You have a set you can exchange! Enter any character to continue or skip to skip!");
				String choice = this.gamemechanics.getInput().getInputCommand();
				if (!choice.equalsIgnoreCase("skip")){
					territorycardreinforcements = this.exchange(player);
				}
			}
			else {
				this.gamemechanics.getOutput().updateGameInfoPanel("No territory cards to exchange!");
			}
		}
		return territorycardreinforcements;
	}
	
	private boolean hasSet(ArrayList<Card> playerhand){
		boolean foundset = false;
		Integer inf = 0, cav = 0, art = 0, wild = 0;
		for (Card card : playerhand){
			switch (card.getInsignia()){
				case 0:		inf++;
							break;					
				case 1:		cav++;
							break;
				case 2:		art++;
							break;
				case 3:		wild++;
							break;
			}
		}
		if (inf > 2 || cav > 2 || art > 2){
			foundset = true;
		}
		else if ((inf > 1 || cav > 1 || art > 1) && wild > 0){
			foundset = true;
		}
		else if ((inf > 0 || cav > 0 || art > 0) && wild > 1){
			foundset = true;
		}
		else {
			foundset = false;
		}
		return foundset;
	}
	
	private Integer exchange(Player player){
		boolean loop = true;
		String set;
		do {
			this.gamemechanics.getOutput().updateGameInfoPanel(
					"Enter III to exchange 3 infantry, CCC to exchange 3 cavalry" +
					" or AAA to exchange 3 artillery!");
			set  = this.gamemechanics.getInput().getInputCommand();
			if (set.equalsIgnoreCase("III")){
				loop = removeSet(player, 0);
			}
			else if (set.equalsIgnoreCase("CCC")) {
				loop = removeSet(player, 1);
			}
			else if (set.equalsIgnoreCase("AAA")){
				loop = removeSet(player, 2);
			}
			else {
				this.gamemechanics.getOutput().updateGameInfoPanel("Enter one of the above options!");
				loop = true;
			}
		} while (loop);
		if (tradedinsets < 6){
			this.territoryreinforcements += 2;
		}
		else if (tradedinsets == 6){
			this.territoryreinforcements += 3;
		}
		else {
			this.territoryreinforcements += 5;
		}
		Integer reinforcements = this.territoryreinforcements;
		if (ownedterritorycardbonus){
			this.gamemechanics.getOutput().updateGameInfoPanel("You own a territory in your hand! Extra 2 reinforcements!");
			reinforcements += 2;
			ownedterritorycardbonus = false;
		}
		this.gamemechanics.getOutput().updateGameInfoPanel(player.getPlayerName()
				+ " gets " + reinforcements + " extra reinforcements from trading in a set!");
		return reinforcements;
	}
	
	private boolean removeSet(Player player, Integer insignia){
		ArrayList<Card> playerhand = player.getHand();
		boolean failure = true;
		Integer match = 0;
		for (Card card : playerhand){
			if (card.getInsignia() == insignia){
				match++;
				this.ownedterritorycardbonus = this.checkIfTerritoryOwned(player, card);
			}
			else if (card.getInsignia() == 3){
				match++;
			}
		}
		if (match >= 3){
			int j = 0;
			for (int i = 0; i < playerhand.size(); i++){
				Integer cardinsignia = playerhand.get(i).getInsignia();
				if (cardinsignia == insignia || cardinsignia == 3){
					if (j < 3){
						playerhand.remove(i);
						j++;
					}
				}
			}
			this.gamemechanics.getOutput().updateGameInfoPanel("Set traded in!");
			this.tradedinsets++;
			failure = false;
		}
		else {
			this.gamemechanics.getOutput().updateGameInfoPanel("You don't have 3 cards with that insignia!");
			failure = true;
		}
		return failure;
	}
	
	private boolean checkIfTerritoryOwned(Player player, Card card){
		boolean owned = false;
		for (Army army : player.getPlacedArmies()){
			if (card.getTerritory().getID() == army.getCountry().getID()){
				owned = true;
			}
		}
		return owned;
	}
}
