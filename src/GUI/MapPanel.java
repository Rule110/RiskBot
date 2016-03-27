package GUI;

/*
Team Name: table_1
Student Numbers: 14480278, 14461158, 14745991

The class that contains the map components
*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class MapPanel extends JPanel {
	public MapPanel(Output output){
		this.output = output;
		MapConstants.setScaling(output.getPanelSize().getHeight() / MapConstants.FRAME_HEIGHT);		
		this.setPreferredSize(output.getPanelSize());
		output.setLinks(new Links(output));
		this.add(output.getLinks());	
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D gfx2d = (Graphics2D)g;
		gfx2d.setColor(new Color(0,191,255));
		gfx2d.fill(new Rectangle(0, 0, (int)output.getPanelSize().getWidth(), (int)output.getPanelSize().getHeight()));	
	}
	private Output output;
	private static final long serialVersionUID = 1L;
}