package hufs.cse.grimpan.strategy;

import java.awt.*;
import javax.swing.*;

public abstract class Ruler extends JComponent {
	public static final int PPINCH = Toolkit.getDefaultToolkit().getScreenResolution();
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	public static final int SIZE = 40;

	public int unitType; // px = 0, cm = 1, inch = 2
	protected int increment;
	protected int units;
	protected static TileModel model;
	protected Ruler rulerView = null;

	public Ruler(int m, TileModel model) {
		unitType = m;
		this.model = model;
		
		setIncrementAndUnits();
		//System.out.println("inc="+increment+" units="+units+" ratio="+model.getScaleRatio());
	}

	public void setIsMetric(int utype) {
		this.unitType = utype;
		setIncrementAndUnits();
		repaint();
	}

	private void setIncrementAndUnits() {
		if (unitType == 1) { // cm
			units = (int)((double)PPINCH / (double)2.54); // dots per centimeter
			increment = units;
		} else if (unitType == 2){ // inch
			units = PPINCH;
			increment = units / 2;
		} else {
			units = 100; // 100 pixel
			increment = 50;
		}
		//model.setUnitPixels(units);
	}

	public boolean isMetric() {
		return this.unitType == 1;
	}

	public int getIncrement() {
		return increment;
	}

	public void setPreferredHeight(int ph) {
		setPreferredSize(new Dimension(SIZE, ph));
	}

	public void setPreferredWidth(int pw) {
		setPreferredSize(new Dimension(pw, SIZE));
	}

	protected abstract void paintComponent(Graphics g);
}