// Teamname: nullptr
// Student Number: 14745991
// Description: the nullptr Risk bot

import java.util.ArrayList;
import java.util.Collections;

public class nullptr implements Bot {
	
	private BoardAPI board;
	private PlayerAPI player;
	
	nullptr (BoardAPI inBoard, PlayerAPI inPlayer) {
		board = inBoard;
		player = inPlayer;
		return;
	}
	
	public String getName () {
		String command = "";
		command = "nullptr";
		return(command);
	}

	public String getReinforcement () {
		this.phase = 0;
		String command = "";
		Strategy strategy = new Strategy();
		if (strategy.reinforcepriority.size() == 0){
			command = GameData.COUNTRY_NAMES[(int)(Math.random() * GameData.NUM_COUNTRIES)];
			command = command.replaceAll("\\s", "");
		}
		else {
			Territory territory = strategy.reinforcepriority.remove(0);
			command = territory.countryname;
		}
		command += " 1";
		return(command);
	}
	
	public String getPlacement (int forPlayer) {
		String command = "";
		ArrayList<Integer> enemyterritories = new ArrayList<Integer>();
		for (int i = 0; i < GameData.NUM_COUNTRIES; i++){
			if (board.getOccupier(i) < GameData.NUM_PLAYERS && board.getOccupier(i) != player.getId()){
				boolean bordering = false;
				int j = 0;
				while (j > GameData.ADJACENT[i].length && !bordering){
					if (board.getOccupier(j) == player.getId()){
						bordering = true;
					}
					else if (board.getOccupier(j) == forPlayer){
						enemyterritories.add(i);
						j++;
					}
				}
			}
		}
		if (enemyterritories.size() == 0){
			command = GameData.COUNTRY_NAMES[(int)(Math.random() * GameData.NUM_COUNTRIES)];
			command = command.replaceAll("\\s", "");
		}
		else {
			command = GameData.COUNTRY_NAMES[enemyterritories.get(0)];
		}
		return(command);
	}
	
	public String getCardExchange () {
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
		this.phase = 2;
		String command = "";
		Strategy strategy = new Strategy();
		if (strategy.bestattacks.size() > 0){
			Territory attackfrom = strategy.bestattacks.remove(0);
			String attackfromstr = attackfrom.countryname;
			String attackto = attackfrom.attackchoice.countryname;
			Integer amount = attackfrom.force - 1;
			if (amount > 3){
				amount = 3;
			}
			command = attackfromstr + " " + attackto +" "+ amount;
		}
		else {
			command = "skip";	
		}
		return(command);
	}

	public String getDefence (int countryId) {
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
		String command = "";
		Strategy strategy = new Strategy();
		for (Territory territory : strategy.myterritories){
			if (territory.countryid == attackCountryId){
				if (territory.defenceneed <= 0){
					Integer amount = territory.force - 1;
					command += amount;
				}
				else {
					command = "0";
				}
			}
		}
		return(command);
	}

	public String getFortify () {
		this.phase = 1;
		String command = "";
		Strategy strategy = new Strategy();
		Territory fortifyfrom;
		Territory fortifyto;
		if (strategy.fortifypriority.size() > 0 && strategy.reinforcepriority.size() > 0){
			int i = 0;
			do {
				fortifyto = strategy.reinforcepriority.get(i++);
				int j = 0;
				do {
					fortifyfrom = strategy.fortifypriority.get(j++);

				} while ((!board.isConnected(fortifyfrom.countryid, fortifyto.countryid) || fortifyfrom.countryid == fortifyto.countryid) && j < strategy.fortifypriority.size());
			} while (i < strategy.reinforcepriority.size());
			if (board.isConnected(fortifyfrom.countryid, fortifyto.countryid) && fortifyfrom.countryid != fortifyto.countryid){
				Integer amount = fortifyfrom.force - 1;
				command = fortifyfrom.countryname +" "+ fortifyto.countryname +" "+ amount;
			}
			else {
				command = "skip";
			}
		}
		else {
			command = "skip";	
		}
		return(command);
	}
	
	// Custom code
	private Integer phase = 0;
	private class Territory implements Comparable<Territory>{
		private Integer countryid;
		private Integer force;
		private ArrayList<Front> fronts;
		private String countryname;
		private Integer continentid;
		private Integer defenceneed;
		private Integer fortifyavailable;
		private Double bestattack;
		private Front attackchoice;
		private boolean continentbridge;
		private boolean priority;
		public Territory(int incountryid){
			this.fronts = new ArrayList<Front>();
			this.countryid = incountryid;
			this.force = board.getNumUnits(this.countryid);
			this.countryname = GameData.COUNTRY_NAMES[this.countryid].replaceAll("\\s", "");
			this.continentid = GameData.CONTINENT_IDS[this.countryid];
			for (int i = 0; i < GameData.ADJACENT[this.countryid].length; i++){
				Integer adjacentid = GameData.ADJACENT[this.countryid][i];
				if (board.getOccupier(adjacentid) != player.getId()){
					this.fronts.add(new Front(adjacentid));
				}
			}
			for (Front front : fronts){
				if (front.continentid != this.continentid){
					continentbridge = true;
				}
				else {
					continentbridge = false;
				}
			}
			this.defenceneed = 0;
			this.priority = false;
			for (Front front: this.fronts){
				this.defenceneed += (int)((front.opposingforce * 3.0 / 2.0) - this.force);
				if (front.oponentid < GameData.NUM_PLAYERS){
					this.priority = true;
				}
			}
			this.bestattack = 0.0;
			for (Front front : this.fronts){
				Double attackcandidate = ((double) this.force) / ((double) front.opposingforce);
				if (attackcandidate > (3.0 / 2.0) && attackcandidate > this.bestattack && this.force > 3){
					this.bestattack = attackcandidate;
					this.attackchoice = front;
				}
				else {
					Integer enemydefenceneed = assessEnemyDefenceNeed(front.adjacentid);
					if (enemydefenceneed > 0 && attackcandidate > 0.8 && this.force > 3){
						this.bestattack = ((double) this.force + enemydefenceneed) / ((double) front.opposingforce);
						this.attackchoice = front;
					}
				}
			}
			this.fortifyavailable = 0;
			if (fronts.size() == 0){
				this.fortifyavailable = this.force - 1;
			}
		}
		public int compareTo(Territory other){
			int comparison = 0;
			if (phase == 0){
				if (GameData.CONTINENT_VALUES[this.continentid] < GameData.CONTINENT_VALUES[other.continentid] && this.defenceneed > 0){
					comparison = -1;
				}
				else if (GameData.CONTINENT_VALUES[this.continentid] < GameData.CONTINENT_VALUES[other.continentid] && this.defenceneed <= 0){
					comparison = 1;
				}
				else if (GameData.CONTINENT_VALUES[this.continentid] > GameData.CONTINENT_VALUES[other.continentid] && other.defenceneed > 0){
					comparison = 1;
				}
				else if (GameData.CONTINENT_VALUES[this.continentid] > GameData.CONTINENT_VALUES[other.continentid] && other.defenceneed <= 0){
					comparison = -1;
				}
				else if (this.continentbridge && !other.continentbridge && this.defenceneed > 0){
					comparison = -1;
				}
				else if (this.continentbridge && !other.continentbridge && this.defenceneed <= 0){
					comparison = 1;
				}
				else if (other.continentbridge && !this.continentbridge && this.defenceneed > 0){
					comparison = 1;
				}
				else if (other.continentbridge && !this.continentbridge && this.defenceneed <= 0){
					comparison = -1;
				}
				else if (this.priority && !other.priority){
					comparison = -1;
				}
				else if (!this.priority && other.priority){
					comparison = 1;
				}
				else {
					comparison = this.defenceneed.compareTo(other.defenceneed);
				}
			}
			else if (phase == 1){
				comparison = this.fortifyavailable.compareTo(other.fortifyavailable);
			}
			else if (phase == 2){
				if (GameData.CONTINENT_VALUES[this.continentid] < GameData.CONTINENT_VALUES[other.continentid]){
					comparison = -1;
				}
				else if (GameData.CONTINENT_VALUES[this.continentid] > GameData.CONTINENT_VALUES[other.continentid]){
					comparison = 1;
				}
				else {
					comparison = this.bestattack.compareTo(other.bestattack);
				}
			}
			return comparison;
		}
		private Integer assessEnemyDefenceNeed(Integer enemyterritoryid){
			Integer enemydefenceneed = 0;
			ArrayList<Front> enemyfronts = new ArrayList<Front>();
			for (int i = 0; i < GameData.ADJACENT[this.countryid].length; i++){
				Integer adjacentid = GameData.ADJACENT[this.countryid][i];
				if (board.getOccupier(adjacentid) != board.getOccupier(enemyterritoryid)){
					enemyfronts.add(new Front(adjacentid));
				}
			}
			Integer cumulativeopponents = 0;
			for (Front front: enemyfronts){
				if (front.oponentid < GameData.NUM_PLAYERS){
					cumulativeopponents += front.opposingforce;
				}
			}
			enemydefenceneed += cumulativeopponents - board.getNumUnits(enemyterritoryid);
			return enemydefenceneed;
		}
	}
	private class Front {
		private Integer adjacentid;
		private Integer oponentid;
		private Integer opposingforce;
		private String countryname;
		private Integer continentid;
		public Front(int adjacentid){
			this.adjacentid = adjacentid;
			this.opposingforce = board.getNumUnits(this.adjacentid);
			this.countryname = GameData.COUNTRY_NAMES[adjacentid].replaceAll("\\s", "");
			this.oponentid = board.getOccupier(this.adjacentid);
		}
	}
	public class Strategy{
		private ArrayList<Territory> myterritories;
		private ArrayList<Territory> reinforcepriority;
		private ArrayList<Territory> fortifypriority;
		private ArrayList<Territory> bestattacks;
		public Strategy(){
			this.myterritories = new ArrayList<Territory>();
			for (int i = 0; i < GameData.NUM_COUNTRIES; i++){
				if (board.getOccupier(i) == player.getId()){
					myterritories.add(new Territory(i));
				}
			}
			this.reinforcepriority = new ArrayList<Territory>();
			this.fortifypriority = new ArrayList<Territory>();
			this.bestattacks = new ArrayList<Territory>();
			for (Territory territory : myterritories){
				if (territory.defenceneed != 0){
					this.reinforcepriority.add(territory);
				}
				if (territory.fortifyavailable > 0){
					this.fortifypriority.add(territory);
				}
				if (territory.bestattack > 0.0){
					this.bestattacks.add(territory);
				}
			}
			Collections.sort(this.reinforcepriority);
			Collections.sort(this.fortifypriority);
			Collections.sort(this.bestattacks);
		}
	}
}