package Input;

/*
Team Name: table_1
Student Numbers: 14480278, 14461158, 14745991

Input processing class
*/

import javax.swing.JTextField;

import Listeners.TextActionListener;
import Game.GameMechanics;

import java.util.Stack;

public class Input implements Main.Input {
	
	public Input(GameMechanics gamemechanics) {
		inputField = gamemechanics.getInputField();
		inputBuffer = new Stack<String>();
		inputHistory = new Stack<String>();
		// Add an action listener for the text field.
		inputField.addActionListener(new TextActionListener(this));
		this.gamemechanics = gamemechanics;
	}
	//takes user input as argument and adds it to the stack "inputBuffer"
	public void addInputToBuffer(String input) {
		inputBuffer.add(input);
		inputHistory.add(input);
		gamemechanics.getOutput().updateGameInfoPanel(">" + input);
	}
	
	public Stack<String> getInputBuffer() {
		return inputBuffer;
	}
	//used to make TextField accessible from Listener class.
	public JTextField getTextField(){
		return this.inputField;
	}
	// Method used to get input command from the user.
	public String getInputCommand() {
		synchronized (inputBuffer) {
			while (inputBuffer.isEmpty()) {		
				try {
					inputBuffer.wait();
				}		
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return inputBuffer.pop();
	}
	private JTextField inputField;
	private Stack<String> inputBuffer;
	private Stack<String> inputHistory; 
	private GameMechanics gamemechanics;
}
