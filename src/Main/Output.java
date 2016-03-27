package Main;

/*
Team Name: table_1
Student Numbers: 14480278, 14461158, 14745991

Development-by-Contract Output Interface
*/

import java.awt.Dimension;

public interface Output {
	public void updateMapPanel();
	public void updateGameInfoPanel(String input);
	public Dimension getPanelSize();
}
