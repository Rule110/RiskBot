package Game;

/*
Team Name: nullptr
Student Numbers: 14745991

Reinforcement class
*/

import java.util.ArrayList;

public class Reinforce {
	private GameMechanics gamemechanics;
	private ArrayList<Army> matches;
	public Reinforce(GameMechanics gamemechanics){
		this.gamemechanics = gamemechanics;
		matches = new ArrayList<Army>();
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
					player.getPlayerName() + " Enter unambiguous country name (your armies already occupy) to reinforce it:\n");
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
}
