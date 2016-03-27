package GUI;

/*
	Team Name: table_1
	Student Numbers: 14480278, 14461158, 14745991
	
	Create the GUI and add the various components, e.g. Map, Game Info, Text Field.
	We use a stack structure to store commands entered in the Text Field.
*/

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Game.Army;
import Game.Country;
import Game.GameMechanics;

import java.util.ArrayList;

public class Output extends JFrame implements Main.Output{
	
	public Output(Game.GameMechanics gamemechanics) {
		this.gamemechanics = gamemechanics;
		//Set map dimensions using current screensize
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int map_width = (int)(screensize.getWidth() - 100);
		int map_height = (int)(screensize.getHeight() - gameinfoheight - 160);
		this.map_size = new Dimension(map_width, map_height);
		
		this.setTitle("Risk");
		this.setTextField();
		this.createPanels();
		
		//add the labels to the panels
		game_info_panel.add(game_info);
		input_panel.add(input);
		
		//make JTextArea uneditable and change background to opaque.
		game_info.setEditable(false);
		game_info.setOpaque(false);
		
		//add text field to user input panel.
		input_panel.add(textField);
		
		//create a new panel which consists of panels "game_info_panel" and "input_panel" on top of one another
		JSplitPane bottom_panels = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
		        true, scrollablepanel, input_panel);
		        
		//create a new panel which consists of "bottom_panels" beneath "map_panel"
		JSplitPane full_gui= new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
		            true, map_panel, bottom_panels);
		
		//set dimensions for panels
		map_panel.setPreferredSize(map_size);
		scrollablepanel.setPreferredSize(new Dimension(map_width, gameinfoheight));
		
		//prevent panels from being resizeable
		bottom_panels.setEnabled(false);
		game_info_panel.setEnabled(false);
		full_gui.setEnabled(false);
	 	
	 	//add gui to jframe
		this.getContentPane().add(full_gui);
		
		//set what happens when "X" is clicked in right top corner.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		
		//ensures window can't be resized and dimensions aren't ruined.
		this.setResizable(false);
		
		//make gui visible
		this.setVisible(true);
 
	}
	void createPanels(){
		this.game_info_panel = new JPanel();
		this.scrollablepanel = new JScrollPane(game_info_panel);
		this.input_panel = new JPanel();
		this.map_panel = new MapPanel(this);
	}
	
	// Updates the game information panel to display new text.
	public void updateGameInfoPanel(String updatedText) {
		game_info.append("\n" + updatedText);
		game_info.setCaretPosition(game_info.getDocument().getLength()); //jumps to bottom of game_info panel.
	}
	public void updateMapPanel(){
		this.map_panel.repaint();
	}
	private void setTextField() {
		this.textField = gamemechanics.getInputField();	
		this.textField.setPreferredSize(new Dimension(400,24));
	}
	public Dimension getPanelSize(){
		return map_size;
	}
	Links getLinks(){
		return this.linkscomponent;
	}
	void setLinks(Links links){
		this.linkscomponent = links;
	}
	Countries getCountries(){
		return countriescomponent;
	}
	void setCountries(Countries countries){
		this.countriescomponent = countries;
	}
	Armies getArmies(){
		return armiescomponent;
	}
	void setArmies(Armies armies){
		this.armiescomponent = armies;
	}
	Continents getContinents(){
		return this.continentskey;
	}
	void setContinents(Continents continents){
		this.continentskey = continents;
	}
	PlayerComponent getPlayerKey(){
		return this.playerkey;
	}
	void setPlayerKey(PlayerComponent playerkey){
		this.playerkey = playerkey;
	}
	ArrayList<Army> getArmyList(){
		return gamemechanics.getArmyList();
	}
	ArrayList<Country> getCountryList(){
		return gamemechanics.getCountryList();
	}
	private GameMechanics gamemechanics;
	private Links linkscomponent;
	private Countries countriescomponent;
	private Armies armiescomponent;
	private Continents continentskey;
	private PlayerComponent playerkey;
	private MapPanel map_panel;
	private Dimension map_size;
	private static final long serialVersionUID = 1L;
	
	private JTextField textField;
	private JPanel input_panel;
	private JPanel game_info_panel;
	private JScrollPane scrollablepanel;
	private JTextArea game_info = new JTextArea(5,109);
	private int gameinfoheight = 100;
	private JLabel input = new JLabel("User input");
	
}
