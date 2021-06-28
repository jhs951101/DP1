package hufs.cse.grimpan.strategy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class PointRect extends RectShape {
	private static TileModel model;
	private static final int POINT_SIZE = 4;
	private double x = 0;// milli
	private double y = 0;// milli
	public PointRect(double x, double y, TileModel tm){
		super(new Rectangle2D.Double(x-POINT_SIZE, y-POINT_SIZE, POINT_SIZE*2, POINT_SIZE*2),
				1, null, Color.black, true, model);
		model = tm;
		this.x = x;
		this.y = y;
		
	}
	public PointRect(Point2D p){
		this(p.getX(), p.getY(), model);
	}
	public void draw(Graphics2D g2){
		double rectX = mainFrame.tilemodel.getMilli2Pixel(x)-POINT_SIZE;
		double rectY = mainFrame.tilemodel.getMilli2Pixel(y)-POINT_SIZE;

		Rectangle2D markRect = new Rectangle2D.Double(rectX, rectY, POINT_SIZE*2, POINT_SIZE*2);
				
		if (grimStrokeColor!=null){
			g2.setStroke(new BasicStroke(2f));
			g2.setColor(grimStrokeColor);
			g2.draw(markRect);
		}

		if (isGrimFill()){
			g2.setColor(grimFillColor);
			g2.fill(markRect);
		}

	}

}
