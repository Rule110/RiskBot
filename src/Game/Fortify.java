package Game;

import java.util.ArrayList;

/*
Team Name: nullptr
Student Numbers: 14745991

Fortify encapsulates fortify mechanics
*/
public class Fortify {
	private GameMechanics gamemechanics;
	Fortify(GameMechanics gamemechanics){
		this.gamemechanics = gamemechanics;
	}
	void setFortify(Player player){
		Army fortifyfrom = null;
		while (fortifyfrom == null) {
			gamemechanics.getOutput().updateGameInfoPanel(
				player.getPlayerName() + " select country to take forces from!"
				+ " Enter unambiguous name:");
			String country = gamemechanics.getInput().getInputCommand();
			fortifyfrom = placedMatches(player, country);
		}
		this.fortifyDestinationCheck(fortifyfrom);
	}
	private Army placedMatches(Player player, String attackfrom){
		Army fortifyfrom = null;
		boolean found = false;
		for (Army army : player.getPlacedArmies()){
			String occupiedcountry = army.getCountry().getName().toLowerCase();
			if (occupiedcountry.contains(attackfrom.toLowerCase())){
				found = true;
				if (fortifyfrom != null){
					gamemechanics.getOutput().updateGameInfoPanel(
						player.getPlayerName() + " country name is ambiguous. Try again!");
					fortifyfrom = null;
				}
				else if (army.getSize() < 2){
					gamemechanics.getOutput().updateGameInfoPanel(
						player.getPlayerName() + " Cannot remove units from a territory with army size of 1.");
					fortifyfrom = null;
				}
				else {
					gamemechanics.getOutput().updateGameInfoPanel(
						player.getPlayerName() + " will be moving units from "
						+ army.getCountry().getName() + " to fortify another territory!");
					fortifyfrom = army;
				}
			}
		}
		if (!found){
			gamemechanics.getOutput().updateGameInfoPanel(
				player.getPlayerName() + " no fortifyto for " + attackfrom + " found in your occupied countries!");
		}
		return fortifyfrom;
	}
	private void fortifyDestinationCheck(Army fortifyfrom){
		Player player = fortifyfrom.getPlayer();
		String fortifyto_substr = null;
		Army fortifyto = null;
		boolean found = false;
		while (fortifyto == null){
			gamemechanics.getOutput().updateGameInfoPanel(
				player.getPlayerName() + " select country to fortify! Enter unambiguous name:");
			fortifyto_substr = gamemechanics.getInput().getInputCommand();
			for (Army army : player.getPlacedArmies()){
				String occupiedcountry = army.getCountry().getName().toLowerCase();
				if (occupiedcountry.contains(fortifyto_substr.toLowerCase())){
					found = true;
					if (fortifyto != null){
						gamemechanics.getOutput().updateGameInfoPanel(
							player.getPlayerName() + fortifyto_substr + " is ambiguous. Try again!");
						fortifyto = null;
					}
					else {
						fortifyto = army;
						String fortifyfrom_str = fortifyfrom.getCountry().getName();
						String fortifyto_str = fortifyto.getCountry().getName();
						ArrayList<Army> alreadychecked = new ArrayList<Army>();
						alreadychecked.add(fortifyfrom);
						ArrayList<Army> adjacentownedarmies= getAdjacentOwned(fortifyfrom, alreadychecked);
						boolean contiguous = checkContiguous(adjacentownedarmies, fortifyto, alreadychecked);
						if (contiguous){
							Integer fortifysize = getFortifySize(player, fortifyfrom, fortifyto);
							this.fortify(fortifyfrom, fortifyto, fortifysize);
							gamemechanics.getOutput().updateGameInfoPanel(
								player.getPlayerName() + " will be fortifying from " +
								fortifyfrom_str + " to " + fortifyto_str + "!");
						}
						else {
							gamemechanics.getOutput().updateGameInfoPanel(
								player.getPlayerName() + " Countries are not contiguous! Cannot fortify from " +
								fortifyfrom_str + " to " + fortifyto_str + "!");
							fortifyto = null;
						}
					}
				}
			}
			if (!found){
				gamemechanics.getOutput().updateGameInfoPanel(
					player.getPlayerName() + " no match for " + fortifyto_substr + " found in your occupied countries!");
			}
		}
	}
	private ArrayList<Army> getAdjacentOwned(Army fortifyfrom, ArrayList<Army> alreadychecked){
		ArrayList<Country> adjacentcountries = fortifyfrom.getCountry().getAdjacentCountries();
		ArrayList<Army> adjacentownedarmies = new ArrayList<Army>();
		for (Country country : adjacentcountries){
			Army army = country.getArmy();
			if (army.getPlayer() == fortifyfrom.getPlayer()){
				if (!AlreadyChecked(army, alreadychecked)){
					adjacentownedarmies.add(army);
				}
			}
		}
		return adjacentownedarmies;		 
	}
	private boolean AlreadyChecked(Army checkthisarmy, ArrayList<Army> alreadychecked){
		boolean found = false;
		for (Army army : alreadychecked){
			if (army == checkthisarmy){
				found = true;
			}
		}
		return found;
	}
	private boolean checkContiguous(ArrayList<Army> adjacentownedarmies, Army fortifyto, ArrayList<Army> alreadychecked){
		boolean contiguityfound = false;
		for (Army ownedarmy : adjacentownedarmies){
			if (ownedarmy == fortifyto){
				contiguityfound = true;
			}
			else {
				alreadychecked.add(ownedarmy);
			}
		}
		if (contiguityfound){
			return true;
		}
		else if (adjacentownedarmies.size() == 0){
			return false;
		}
		else {
			for (Army fortifyfrom : adjacentownedarmies){
				alreadychecked.add(fortifyfrom);
				ArrayList<Army> adjacentownedarmies_next = getAdjacentOwned(fortifyfrom, alreadychecked);
				boolean recursivereturn = checkContiguous(adjacentownedarmies_next, fortifyto, alreadychecked);
				if (recursivereturn){
					contiguityfound = true;
				}
			}
			return contiguityfound;
		}
	}
	private Integer getFortifySize(Player player, Army fortifyfrom, Army fortifyto){
		Integer fortifysize = null;
		boolean loop = true;
		do {
			String num = null;
			do {
				gamemechanics.getOutput().updateGameInfoPanel(
					player.getPlayerName() + " Enter the number of units you wish to move from here to fortify " +
					fortifyto.getCountry().getName());
				num	 = gamemechanics.getInput().getInputCommand();
			} while (isNotAnInteger(num));
			fortifysize = Integer.parseInt(num);
			if (fortifysize > fortifyfrom.getSize()){
				gamemechanics.getOutput().updateGameInfoPanel(
					player.getPlayerName() + " that's bigger than the " +
					fortifyfrom.getSize() + " armies you have on " + fortifyfrom.getCountry().getName() + "!");
				loop = true;
			}
			else if (fortifysize < 1){
				gamemechanics.getOutput().updateGameInfoPanel(
						player.getPlayerName() + " you have to enter a positive whole number! ");
					loop = true;
			}
			else if (fortifyfrom.getSize() - fortifysize < 1){
				gamemechanics.getOutput().updateGameInfoPanel(
					player.getPlayerName() + " you have to leave at least one army on your country!");
				loop = true;
			}
			else {
				loop = false;
			}
		} while (loop);
		return fortifysize;
	}
	private boolean isNotAnInteger(String str){
		try{
			Integer.parseInt(str);
			return false;
		} catch (NumberFormatException e){
			gamemechanics.getOutput().updateGameInfoPanel("Not an Integer!");
			return true;
		}
	}
	private void fortify(Army fortifyfrom, Army fortifyto, Integer fortifysize){
		Player player = fortifyfrom.getPlayer();
		Country from = fortifyfrom.getCountry();
		Country to = fortifyto.getCountry();
		Integer reduce = fortifyfrom.getSize() - fortifysize;
		Integer increase = fortifyto.getSize() + fortifysize;
		gamemechanics.setArmyList(player, from, reduce);
		gamemechanics.setArmyList(player, to, increase);
	}
}
