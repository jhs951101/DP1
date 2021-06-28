package hufs.cse.grimpan.strategy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.event.MouseInputAdapter;

import hufs.cse.grimpan.strategy.HRuler.RuleMouseAdapter;

public class VRuler extends Ruler {

	public VRuler(int m, TileModel model) {
		super(m, model);
		RuleMouseAdapter mouseAdapter = new RuleMouseAdapter();
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
	}
	protected void paintComponent(Graphics g) {
		Rectangle drawHere = g.getClipBounds();

		// Fill clipping area with bright cyan.
		g.setColor(new Color(240,240,240));
		g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

		// Do the ruler labels in a small font that's black.
		g.setFont(new Font("SansSerif", Font.PLAIN, 10));
		g.setColor(Color.black);

		// Some vars we need.
		int end = 0;
		//int start = -units;
		int start = 0;
		int tickLength = 0;
		String text = null;
		double ratio = model.getScaleRatio();

		// Use clipping bounds to calculate first and last tick locations.
		end = (((drawHere.y + drawHere.height) / increment) + 1) * increment;

		// Make a special case of 0 to display the number
		// within the rule and draw a units label.
		String unitName = null;
		if (unitType == 0)
			unitName = "px";
		else if (unitType == 1)
			unitName = "cm";
		else
			unitName = "in";

		int istart = 0;
		end = (int) (end / ratio);
		// ticks and labels
		for (int i = start; i < end; i += increment, istart += increment) {
			if (i % units == 0 && i > 0)  {
				tickLength = 10;
				if (unitType == 0)
					text = Integer.toString(i);
				else
					text = Integer.toString(i/units);
			} else {
				tickLength = 7;
				text = null;
			}

			if (i == increment) {
				text = Integer.toString(1) + unitName;
				//text = unitName;
				tickLength = 10;
				g.drawLine(SIZE-1,(int)(istart*ratio),SIZE-tickLength-1,(int)(istart*ratio));
				g.drawString(text, 5, (int)(istart*ratio)+3);
				text = null;
			}
			else if (tickLength != 0 && i != start) {
				g.drawLine(SIZE-1, (int)(istart*ratio), SIZE-tickLength-1, (int)(istart*ratio));
				if (text != null)
					g.drawString(text, 5, (int)(istart*ratio)+3);
			}
		}
	}
	class RuleMouseAdapter extends MouseInputAdapter {

	}

}
