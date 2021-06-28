package hufs.cse.grimpan.strategy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class TileRect extends RectShape {

	private static TileModel model;
	private static int tileSeq = 100;
	private String tileID = null;

	public TileRect(Rectangle2D rect, TileModel tm){
		super(rect, model);
		model = tm;
		tileSeq++;
		this.tileID = "Tile-"+tileSeq;
	}
	public TileRect(String tileID, Rectangle2D rect){
		super(rect, model);
		this.tileID = tileID;
	}
	public TileRect(RectShape tileShape){
		this(tileShape.getRect2D(), model);
	}
	public TileRect(String tileID, RectShape tileShape){
		this(tileID, tileShape.getRect2D());
	}
	public TileRect clone(){
		return new TileRect(this.tileID, rect2DShape);
	}

	public String getTileID() {
		return tileID;
	}
	public void setTileID(String tileID) {
		this.tileID = tileID;
	}
	public void draw(Graphics2D g2){
		super.draw(g2);
		float x = (float)model.getMilli2Pixel(this.getRect2D().getX()+2);
		float y = (float)model.getMilli2Pixel(this.getRect2D().getY()+5);
		//g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
		//g2.setColor(Color.black);
		//g2.drawString(tileID, x, y);
	}
}
