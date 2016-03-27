package GUI;

/*
Team Name: table_1
Student Numbers: 14480278, 14461158, 14745991

The class that displays a key for the continents
*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class Continents extends JComponent{
	public Continents(Output output){
		this.output = output;
		this.setPreferredSize(new Dimension((int)this.output.getPanelSize().getWidth()/2, (int)this.output.getPanelSize().getHeight()));
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D gfx2d = (Graphics2D)g;
		gfx2d.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		this.drawContinentKey(gfx2d);
	}
	private void drawContinentKey(Graphics2D gfx2d){
		Integer x = 16;
		Integer y = (int) this.output.getPanelSize().getHeight();
		Integer i = 0;
		for (String name : MapConstants.CONTINENT_NAMES){
			y = y - (int)(32 * MapConstants.SCALING_CONSTANT);
			//Set color
			gfx2d.setFont(new Font(MapConstants.CONT_FONTS[i], Font.PLAIN, (int)(12*MapConstants.SCALING_CONSTANT)));
			int[] color = MapConstants.CONT_COLORS[i];
			this.drawNameOutline(gfx2d, name, x, y);
			//Draw name
			gfx2d.setPaint(new Color(color[0], color[1], color[2]));
			gfx2d.drawString(name, x, y);
			//Draw Rectangle of color
			gfx2d.fill(new Rectangle(x + (int)(64 * MapConstants.SCALING_CONSTANT),
					y - (int)(16 * MapConstants.SCALING_CONSTANT),
					(int)(32 * MapConstants.SCALING_CONSTANT),
					(int)(16 * MapConstants.SCALING_CONSTANT)));
			//shift down to draw next name
			i++;
		}
	}
	private void drawNameOutline(Graphics2D gfx2d, String name, Integer x, Integer y){
		gfx2d.setPaint(Color.black);
		gfx2d.drawString(name, x - 1, y - 1);
		gfx2d.drawString(name, x - 1, y + 1);
		gfx2d.drawString(name, x + 1, y - 1);
		gfx2d.drawString(name, x + 1, y + 1);
	}
	private Output output;
	private static final long serialVersionUID = 1L;
}
