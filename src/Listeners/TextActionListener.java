package Listeners;

/*
	Team Name: table_1
	Student Numbers: 14480278, 14461158, 14745991
	
	Listener we assign to the JTextField in the input.
	When an action event occurs, the actionPerformed method is invoked.
	The action we listen for is the user hitting the ENTER key.
*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Input.Input;

public class TextActionListener implements ActionListener {
	
	private Input input;
	// This Method is used in order to make JTextField from output class accessible.
	public TextActionListener(Input input){
		this.input = input;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		synchronized (input.getInputBuffer()) {
			input.addInputToBuffer(input.getTextField().getText());
			input.getTextField().setText("");
			input.getInputBuffer().notify();
		}
		return;
	}
}
