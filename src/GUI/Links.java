package GUI;

/*
Team Name: table_1
Student Numbers: 14480278, 14461158, 14745991

The class that draws the links between countries
*/

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import javax.swing.JComponent;

import Game.Country;

public class Links extends JComponent{
	public Links(Output output){
		this.output = output;
		this.setPreferredSize(output.getPanelSize());
		this.setLayout(new BorderLayout());
		output.setCountries(new Countries(output));
		this.add(output.getCountries());
	}
	@Override
	public void paintComponent(Graphics g){
		//Draw the Links with 2D graphics
		this.drawLinks(this.initialiseGFX2D(g));
		
	}
	private Graphics2D initialiseGFX2D(Graphics g){	
		super.paintComponent(g);
		Graphics2D gfx2d = (Graphics2D)g;
		gfx2d.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		return gfx2d;
	}
	private void drawLinks(Graphics2D gfx2d){
		for (Country country : output.getCountryList()){
			for (Country othercountry : country.getAdjacentCountries()){
				if (country.getID() < othercountry.getID()){
					drawLink(gfx2d, country, othercountry);
				}
			}
		}
	}
	private void drawLink(Graphics2D gfx2d, Country country, Country othercountry){
		Integer x = country.getXCoords();
		Integer	y = country.getYCoords();
		Integer otherx;
		Integer othery;
		if (country.getID() == 8){
			otherx = 0;
			othery = country.getYCoords();
		}
		else if (country.getID() == 22){
			otherx = (int)(output.getPanelSize().getWidth());
			othery = country.getYCoords();
		}
		else {
			otherx = othercountry.getXCoords();
			othery = othercountry.getYCoords();
		}
		Integer mainstroke = (int)(8*MapConstants.SCALING_CONSTANT);
		gfx2d.setStroke(new BasicStroke(mainstroke));
		gfx2d.setPaint(determineColor(country, othercountry));
		gfx2d.draw(new Line2D.Double(x, y, otherx, othery));
		//Draw Outline
		Integer stroke = mainstroke / 4;
		gfx2d.setStroke(new BasicStroke(stroke));
		gfx2d.setPaint(new Color(189,183,107));
		gfx2d.draw(new Line2D.Double(x + (stroke*2), y, otherx + (stroke*2), othery));
		gfx2d.setPaint(new Color(189,183,107));
		gfx2d.draw(new Line2D.Double(x - stroke, y , otherx - stroke, othery ));
		gfx2d.setPaint(new Color(240,230,140));
		gfx2d.draw(new Line2D.Double(x + (stroke*3), y, otherx + (stroke*3), othery));
		gfx2d.setPaint(new Color(240,230,140));
		gfx2d.draw(new Line2D.Double(x - (stroke*3), y, otherx - (stroke*3), othery));
		gfx2d.setPaint(new Color(240,230,140));
		gfx2d.draw(new Line2D.Double(x + (stroke*4), y, otherx + (stroke*4), othery));
		gfx2d.setPaint(new Color(127,255,212));
		gfx2d.draw(new Line2D.Double(x - (stroke*4), y, otherx - (stroke*4), othery));
	}
	private Color determineColor(Country country, Country othercountry){
		Color c = null;
		//Same continent?
		if (country.getContinentID().equals(othercountry.getContinentID())){
			c = country.getColor().darker();
		}
		//Link between continents?
		else {
			c = new Color(255, 255, 255);
		}
		return c;
	}
	private Output output;
	private static final long serialVersionUID = 1L;
}
