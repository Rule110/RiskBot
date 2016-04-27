// Teamname: nullptr
// Student Number: 14745991
// Description: TestBot2 simulates random behaviour

import java.util.ArrayList;

public class TestBot2 implements Bot {
	
	private BoardAPI board;
	private PlayerAPI player;
	
	TestBot2 (BoardAPI inBoard, PlayerAPI inPlayer) {
		board = inBoard;	
		player = inPlayer;
		return;
	}
	
	public String getName () {
		String command = "";
		command = "TestBOT2";
		return(command);
	}

	public String getReinforcement () {
		System.out.println("1");
		String command = "";
		command = GameData.COUNTRY_NAMES[(int)(Math.random() * GameData.NUM_COUNTRIES)];
		command = command.replaceAll("\\s", "");
		command += " 1";
		return(command);
	}
	
	public String getPlacement (int forPlayer) {
		System.out.println("test2placement");
		String command = "";
		command = GameData.COUNTRY_NAMES[(int)(Math.random() * GameData.NUM_COUNTRIES)];
		command = command.replaceAll("\\s", "");
		return(command);
	}
	
	public String getCardExchange () {
		System.out.println("test2exchange");
		String command = "";
		if (player.isForcedExchange()){
			ArrayList<Card> cards = player.getCards();
			int inf = 0, cav = 0, art = 0, wild = 0;
			for (Card card : cards){
				switch (card.getInsigniaId()){
					case 0 :  	inf++;
								break;
					case 1 : 	cav++;
								break;
					case 2 : 	art++;
								break;
					case 3 : 	wild++;
								break;
				}
			}
			for (int i = 0; i < wild; i++){
				command += "w";
			}
			int i = 0;
			while (command.length() < 3 && i < cards.size()){
				if (inf + wild > 2){
					command += "i";
				}
				else if (cav + wild > 2){
					command += "c";
				}
				else if (art + wild > 2){
					command += "a";
				}
				else if (inf + cav + art > 2){
					command = "ica";
				}
				i++;
			}
		}
		else {
			command = "skip";
		}
		return(command);
	}

	public String getBattle () {
		System.out.println("test2battle");
		String command = "";
		String attackfrom = "";
		ArrayList<Integer> owned = new ArrayList<Integer>();
		for (int i = 0; i < GameData.NUM_COUNTRIES; i++){
			if (board.getOccupier(i) == player.getId()){
				owned.add(i);
			}
		}
		ArrayList<Integer> enemies = new ArrayList<Integer>();
		Integer j;
		do {
			j = (int) (Math.random() * owned.size());
		} while (board.getNumUnits(j) < 2);
		
		attackfrom = GameData.COUNTRY_NAMES[j];
		int[] adjacent = GameData.ADJACENT[j];
		for (int i = 0; i < adjacent.length; i++){
			if (board.getOccupier(adjacent[i]) != player.getId()){
				enemies.add(adjacent[i]);
			}
		}
		if (enemies.size() > 0){
			Integer k = (int) (Math.random() * adjacent.length);
			String attackto = GameData.COUNTRY_NAMES[adjacent[k]];
			Integer amount;
			if (board.getNumUnits(j) == 2){
				amount = 1;
			}
			else if (board.getNumUnits(j) == 3){
				amount = 2;
			}
			else {
				amount = 3;
			}
			command = attackfrom +" "+ attackto +" "+ amount;
		}
		else {
			command = "skip";
		}
		return(command);
	}

	public String getDefence (int countryId) {
		System.out.println("5");
		String command = "";
		if (board.getNumUnits(countryId) < 2){
			command = "1";
		}
		else {
			command = "2";
		}
		return(command);
	}

	public String getMoveIn (int attackCountryId) {
		System.out.println("6");
		String command = "";
		// put your code here
		command = "0";
		return(command);
	}

	public String getFortify () {
		System.out.println("7");
		String command = "";
		// put code here
		command = "skip";
		return(command);
	}

}

