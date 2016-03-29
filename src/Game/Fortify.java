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
						+ army.getCountry().getName() + "to fortify another territory!");
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
		String fortifyto_str = null;
		Army fortifyto = null;
		boolean found = false;
		while (fortifyto == null){
			gamemechanics.getOutput().updateGameInfoPanel(
					player.getPlayerName() + " select country to fortify!"
					+ " Enter unambiguous name:");
			fortifyto_str = gamemechanics.getInput().getInputCommand();
			for (Army army : player.getPlacedArmies()){
				String occupiedcountry = army.getCountry().getName().toLowerCase();
				if (occupiedcountry.contains(fortifyto_str.toLowerCase())){
					found = true;
					if (fortifyto != null){
						gamemechanics.getOutput().updateGameInfoPanel(
							player.getPlayerName() + " country name is ambiguous. Try again!");
						fortifyto = null;
					}
					else {
						fortifyto = army;
						ArrayList<Army> adjacentownedarmies= getAdjacentOwned(fortifyfrom, null);
						boolean contiguous = checkContiguous(adjacentownedarmies, fortifyfrom, fortifyto);
						if (contiguous){
							Integer fortifysize = getFortifySize(player, fortifyfrom);
							this.fortify(fortifyfrom, fortifyto, fortifysize);
						}
						else {gamemechanics.getOutput().updateGameInfoPanel(
								player.getPlayerName() + " Countries are not contiguous! Cannot fortify from " +
									fortifyfrom + " to " + fortifyto_str + "!");
							fortifyto = null;
						}
					}
				}
			}
			if (!found){
				gamemechanics.getOutput().updateGameInfoPanel(
					player.getPlayerName() + " no match for " + fortifyto_str + " found in your occupied countries!");
			}
		}
	}
	private ArrayList<Army> getAdjacentOwned(Army fortifyfrom, Army fortifyfrom_prev){
		ArrayList<Country> adjacentcountries = fortifyfrom.getCountry().getAdjacentCountries();
		ArrayList<Army> adjacentownedarmies = new ArrayList<Army>();
		for (Country country : adjacentcountries){
			if (country.getArmy().getPlayer() == fortifyfrom.getPlayer()){
				if (country.getArmy() != fortifyfrom_prev){
					adjacentownedarmies.add(country.getArmy());
				}	
			}
		}
		return adjacentownedarmies;		 
	}
	private boolean checkContiguous(ArrayList<Army> adjacentownedarmies, Army fortifyfrom_prev, Army fortifyto){
		boolean contiguityfound = false;
		for (Army ownedarmy : adjacentownedarmies){
			if (ownedarmy == fortifyto){
				contiguityfound = true;
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
				ArrayList<Army> adjacentownedarmies_next = getAdjacentOwned(fortifyfrom, fortifyfrom_prev);
				boolean recursivereturn = checkContiguous(adjacentownedarmies_next, fortifyfrom, fortifyto);
				if (recursivereturn){
					contiguityfound = true;
				}
			}
			return contiguityfound;
		}
	}
	private Integer getFortifySize(Player player, Army fortifyfrom){
		Integer fortifysize = null;
		boolean loop = true;
		do {
			String num = null;
			do {
				gamemechanics.getOutput().updateGameInfoPanel(
					player.getPlayerName() + " Enter the number of units you wish to move from here to fortify elsewhere.");
				num	 = gamemechanics.getInput().getInputCommand();
			} while (isNotAnInteger(num));
			fortifysize = Integer.parseInt(num);
			if (fortifyfrom.getSize() - fortifysize < 1){
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
