package GUI;

/*
Team Name: table_1
Student Numbers: 14480278, 14461158, 14745991

The class that draws the countries
*/

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

import Game.Country;

public class Countries extends JComponent{

	public Countries(Output output){
		this.output = output;
		this.setPreferredSize(output.getPanelSize());
		this.setLayout(new BorderLayout());
		output.setArmies(new Armies(output));
		this.add(output.getArmies());
	}
	@Override
	public void paintComponent(Graphics g){		
		this.drawCountries(this.initialiseGFX2D(g));
	}
	private Graphics2D initialiseGFX2D(Graphics g){	
		super.paintComponent(g);
		Graphics2D gfx2d = (Graphics2D)g;
		gfx2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);		
		return gfx2d;
	}	
	private void drawCountries(Graphics2D gfx2d){
		for (Country country : output.getCountryList()){
			drawCountry(gfx2d, country);
			drawName(gfx2d, country);
		}
	}
	private void drawCountry(Graphics2D gfx2d, Country country){
		//Draw Country Outline
		//Draw central Outline
		Integer radius = country.getRadius() + (int)(4*MapConstants.SCALING_CONSTANT);
		Integer x = country.getXCoords() - radius;
		Integer	y = country.getYCoords() - radius;
		gfx2d.setStroke(new BasicStroke((int)(4*MapConstants.SCALING_CONSTANT)));
		gfx2d.setPaint(new Color(240,230,140));
		gfx2d.draw(new Ellipse2D.Double(x, y, radius*2, radius*2));
		//Draw Outer Outline
		radius = country.getRadius() + (int)(7*MapConstants.SCALING_CONSTANT);
		x = country.getXCoords() - radius;
		y = country.getYCoords() - radius;
		gfx2d.setStroke(new BasicStroke((int)(2*MapConstants.SCALING_CONSTANT)));
		gfx2d.setPaint(new Color(127,255,212));
		gfx2d.draw(new Ellipse2D.Double(x, y, radius*2, radius*2));
		//Draw Inner Outline
		radius = country.getRadius();
		x = country.getXCoords() - radius;
		y = country.getYCoords() - radius;
		gfx2d.setStroke(new BasicStroke((int)(4*MapConstants.SCALING_CONSTANT)));
		gfx2d.setPaint(new Color(189,183,107));
		gfx2d.draw(new Ellipse2D.Double(x, y, radius*2, radius*2));
		//Fill in Country centre
		radius = country.getRadius();
		x = country.getXCoords() - radius;
		y = country.getYCoords() - radius;
		gfx2d.setPaint(country.getColor());
		gfx2d.fill(new Ellipse2D.Double(x, y, radius*2, radius*2));	
	}
	private void drawName(Graphics2D gfx2d, Country country){
		Integer radius = country.getRadius();
		Integer nameoffset = radius + (radius / 4);
		Integer name_x = country.getXCoords() - nameoffset;
		Integer name_y = country.getYCoords() - nameoffset;
		String name = country.getName();
		gfx2d.setFont(country.getFont());
		this.drawNameOutline(gfx2d, name, name_x, name_y);
		gfx2d.setPaint(country.getColor().darker());
		gfx2d.drawString(name, name_x, name_y);
	}
	private void drawNameOutline(Graphics2D gfx2d, String name, Integer x, Integer y){
		gfx2d.setPaint(Color.black);
		gfx2d.drawString(name, x - 1, y - 1);
		gfx2d.drawString(name, x - 1, y + 1);
		gfx2d.drawString(name, x + 1, y - 1);
		gfx2d.drawString(name, x + 1, y + 1);
	}
	Output output;
	private static final long serialVersionUID = 1L;
}
