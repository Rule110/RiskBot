package Main;

/*
	Team Name: nullptr
	Student Numbers: 14745991
	
	The class from which the game will run.
*/

import java.util.ArrayList;

import GUI.MapConstants;
import GUI.Output;
import Input.Input;
import Game.GameMechanics;
import Game.Player;

public class PlayGame {
	
	public static void main(String args[]){ 
		GameMechanics gamemechanics = new GameMechanics();
		gamemechanics.setInput(new Input(gamemechanics));
		gamemechanics.setOutput(new Output(gamemechanics));
		gamemechanics.setContinentList();
		gamemechanics.setCountryList();
		gamemechanics.setPlayerList(createPlayerList(gamemechanics));
		gamemechanics.setDeck();
		gamemechanics.setDice();
		gamemechanics.initialiseGameMap();
		gamemechanics.setReinforceMechanics();
		gamemechanics.initialReinforce();
		gamemechanics.setCombatMechanics();
		gamemechanics.setFortifyMechanics();
		gamemechanics.startGame();
	}
	
	private static ArrayList<Player> createPlayerList(GameMechanics game){
		ArrayList<Player> playerlist = new ArrayList<Player>();
		for (int i = 1; i <= 6; i++){
			Player player = new Player(game, (i == 1 || i == 2));
			player.setPlayerName(i);
			player.setPlayerColour(MapConstants.PLAYER_COLORS[i - 1]);
			playerlist.add(player);
		}
		return playerlist;
	}
}