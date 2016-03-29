package Game;
/*
Team Name: nullptr
Student Numbers: 14745991

Combat encapsulates combat mechanics
*/
import java.util.ArrayList;
public class Combat {
	private GameMechanics gamemechanics;
	Combat(GameMechanics gamemechanics){
		this.gamemechanics = gamemechanics;
	}
	void setCombat(Player attacker){
		Army assaultforce = null, defenceforce = null;
		while (assaultforce == null) {
			gamemechanics.getOutput().updateGameInfoPanel(
				attacker.getPlayerName() + " select country to attack from!"
				+ " Enter unambiguous name:");
			String attackfrom = gamemechanics.getInput().getInputCommand();
			assaultforce = placedMatches(attacker, attackfrom);
		}
		while(defenceforce == null) {
			gamemechanics.getOutput().updateGameInfoPanel(
				attacker.getPlayerName() + " select country to attack against!"
				+ " Enter unambiguous name:");
			String attackto = gamemechanics.getInput().getInputCommand();
			defenceforce = adjacentMatches(assaultforce, attackto);
		}
		Integer assaultsize = getAssaultSize(attacker, assaultforce);
		Player defender = defenceforce.getPlayer();
		Integer defencesize = null;
		if (defender.getHuman()){
			defencesize = getDefenceSize(defender);	
		}
		else {
			defencesize = 1 + (defenceforce.getSize() % 2);
		}
		beginInvasion(assaultforce, assaultsize, defenceforce, defencesize);
	}
	private Army placedMatches(Player player, String attackfrom){
		Army match = null;
		boolean found = false;
		for (Army army : player.getPlacedArmies()){
			String occupiedcountry = army.getCountry().getName().toLowerCase();
			if (occupiedcountry.contains(attackfrom.toLowerCase())){
				found = true;
				if (match != null){
					gamemechanics.getOutput().updateGameInfoPanel(
						player.getPlayerName() + " country name is ambiguous. Try again!");
					match = null;
				}
				else if (army.getSize() < 2){
					gamemechanics.getOutput().updateGameInfoPanel(
						player.getPlayerName() + " Cannot attack from a territory with army size of 1.");
					match = null;
				}
				else {
					gamemechanics.getOutput().updateGameInfoPanel(
						player.getPlayerName() + " will be attacking from "
						+ army.getCountry().getName() + "!");
					match = army;
				}
			}
		}
		if (!found){
			gamemechanics.getOutput().updateGameInfoPanel(
				player.getPlayerName() + " no match for " + attackfrom + " found in your occupied countries!");
		}
		return match;
	}
	private Army adjacentMatches(Army army, String attackto){
		Army match = null;
		boolean found = false;
		for (Country country : army.getCountry().getAdjacentCountries()){
			String adjacentcountry = country.getName().toLowerCase();
			if (adjacentcountry.contains(attackto.toLowerCase())){
				found = true;
				if (country.getArmy().getPlayer() == army.getPlayer()){
					gamemechanics.getOutput().updateGameInfoPanel("Can't attack self! Try again!");
				}
				else if (match != null){
					gamemechanics.getOutput().updateGameInfoPanel(
						army.getPlayer().getPlayerName() + " country name is ambiguous. Try again!");
					match = null;
				}
				else {
					gamemechanics.getOutput().updateGameInfoPanel(
							army.getPlayer().getPlayerName() + " will be attacking against "
							+ country.getName() + "!");
					match = country.getArmy();
				}
			}
		}
		if (!found){
			gamemechanics.getOutput().updateGameInfoPanel(
				army.getPlayer().getPlayerName() + " no match for " + attackto + " found in your adjacent countries!");
		}
		return match;
	}
	private Integer getAssaultSize(Player attacker, Army assaultforce){
		Integer assaultsize = null;
		boolean loop = true;
		do {
			String num = null;
			do {
				gamemechanics.getOutput().updateGameInfoPanel(
						attacker.getPlayerName() + " Enter the size of your assault force!");
				num	 = gamemechanics.getInput().getInputCommand();
			} while (isNotAnInteger(num));
			assaultsize = Integer.parseInt(num);
			if (assaultsize > 3 || assaultsize < 1){
				gamemechanics.getOutput().updateGameInfoPanel(
						attacker.getPlayerName() + " Assault size can only be 1, 2 or 3!");
				loop = true;
			}
			else if (assaultforce.getSize() - assaultsize < 1){
				gamemechanics.getOutput().updateGameInfoPanel(
						attacker.getPlayerName() + " you have to leave at least one army on your country!");
				loop = true;
			}
			else {
				loop = false;
			}
		} while (loop);
		return assaultsize;
	}
	private Integer getDefenceSize(Player defender){
		Integer defencesize = null;
		boolean loop = true;
		do {
			String num = null;
			do {
				gamemechanics.getOutput().updateGameInfoPanel(
					defender.getPlayerName() + " Enter the size of your defence force!");
				num	 = gamemechanics.getInput().getInputCommand();
			} while (isNotAnInteger(num));
			defencesize = Integer.parseInt(num);
			if (defencesize > 2 || defencesize < 1){
				gamemechanics.getOutput().updateGameInfoPanel(
						defender.getPlayerName() + " Defence size can only be 1 or 2!");
				loop = true;
			}
			else {
				loop = false;
			}
		} while (loop);
		return defencesize;
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
	private void beginInvasion(Army assaultforce, Integer assaultsize, Army defenceforce, Integer defencesize){
		boolean loop = true;
		Player attacker = assaultforce.getPlayer();
		Player defender = defenceforce.getPlayer();
		do {
			ArrayList<Integer> attackerrolls = new ArrayList<Integer>();
			for (int i = assaultsize; i > 1; i--){
				gamemechanics.getOutput().updateGameInfoPanel(
					attacker.getPlayerName() + " you get " + i +
					" rolls. Enter any character to roll the dice!");
				gamemechanics.getInput().getInputCommand();
				gamemechanics.getDice().roll();
				attackerrolls.add(gamemechanics.getDice().getFace());
			}
			System.out.println("assault size: " + assaultsize);
			Integer attackerhighestroll = max(attackerrolls);
			ArrayList<Integer> defenderrolls = new ArrayList<Integer>();
			for (int i = defencesize; i > 1; i--){
				gamemechanics.getOutput().updateGameInfoPanel(
					attacker.getPlayerName() + " you get " + i +
					" rolls. Enter any character to roll the dice!");
				gamemechanics.getInput().getInputCommand();
				gamemechanics.getDice().roll();
				defenderrolls.add(gamemechanics.getDice().getFace());
			}
			System.out.println("defence size: " + defencesize);
			Integer defenderhighestroll = max(defenderrolls);
			Country attackercountry = assaultforce.getCountry();
			Country defendercountry = defenceforce.getCountry();
			if (defenderhighestroll == attackerhighestroll){
				gamemechanics.getOutput().updateGameInfoPanel("It's a draw! " +
					defender.getPlayerName() + " rolled a " + defenderhighestroll + "! " +
					attacker.getPlayerName() + " rolled a " + attackerhighestroll + "! " +
					defender.getPlayerName() + " wins battle by default!");
				assaultsize--;
				gamemechanics.setArmyList(attacker, attackercountry, assaultforce.getSize() - 1);
			}
			else if (defenderhighestroll > attackerhighestroll){
				gamemechanics.getOutput().updateGameInfoPanel(
					defender.getPlayerName() + " rolled a " + defenderhighestroll + "! " +
					attacker.getPlayerName() + " rolled a " + attackerhighestroll + "! " +
					defender.getPlayerName() + " wins battle!");
				assaultsize--;
				gamemechanics.setArmyList(attacker, attackercountry, assaultforce.getSize() - 1);
			}
			else {
				gamemechanics.getOutput().updateGameInfoPanel(
					defender.getPlayerName() + " rolled a " + defenderhighestroll + "! " +
					attacker.getPlayerName() + " rolled a " + attackerhighestroll + "! " +
					attacker.getPlayerName() + " wins battle!");
				defencesize--;
				gamemechanics.setArmyList(defender, defendercountry, defenceforce.getSize() - 1);
			}
			if (defencesize == 0){
				loop = false;
				Integer assaultforceremainder = assaultforce.getSize() - assaultsize;
				gamemechanics.setArmyList(attacker, attackercountry, assaultforceremainder);
				gamemechanics.setArmyList(attacker, defendercountry, assaultsize);
				gamemechanics.getOutput().updateGameInfoPanel(
					attacker.getPlayerName() + " has conquered " + defenceforce.getCountry().getName() + "! ");
			}
			else if (assaultsize == 0){
				loop = false;
			}
			else {
				gamemechanics.getOutput().updateGameInfoPanel(
					attacker.getPlayerName() + " enter any character to attack again." +
					" Enter skip to cease the assault!");
				String again = gamemechanics.getInput().getInputCommand();
				if (again.equalsIgnoreCase("skip")){
					loop = false;
				}
			}
		} while (loop);
	}
	private Integer max(ArrayList<Integer> rolls){
		int max = 0;
		for (int i = 0; i < rolls.size(); i++){
			if (rolls.get(i) > max){
				max = rolls.get(i);
			}
		}
		return max;
	}
}
