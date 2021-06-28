/**
 * Created on Oct 29, 2012
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.strategy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * @author cskim
 *
 */
public class RectShape implements Serializable {

	protected static TileModel model;

	protected Rectangle2D rect2DShape = null;
	protected float grimStrokeWidth = 2f;
	protected Color grimStrokeColor = Color.black;
	protected Color grimFillColor = null;
	protected boolean grimFill = false;
	
	protected DrawPanelView drawPanel = null;
	protected GrimPanFrameMain mainFrame = null;
	
	/**
	 * 
	 */
	public RectShape(Rectangle2D grimShape, TileModel tm){
		
		this.rect2DShape = grimShape;
		this.grimStrokeWidth = ((BasicStroke)TileModel.shapeStroke).getLineWidth();
		this.grimStrokeColor = TileModel.strokeColor;
		this.grimFillColor = TileModel.fillColor;
		this.grimFill = true;
		mainFrame.tilemodel = tm;
		 if(model != null) System.out.println("success instance");
	}
	/**
	 * @param grimShape
	 * @param grimStrokeWidth
	 * @param grimFillColor
	 * @param grimFill
	 */
	public RectShape(Rectangle2D  grimShape, Stroke grimStroke, Color grimStrokeColor, 
			Color grimFillColor, boolean grimFill, TileModel tm) {
		super();
		this.rect2DShape = grimShape;
		this.grimStrokeWidth = ((BasicStroke)grimStroke).getLineWidth();
		this.grimStrokeColor = grimStrokeColor;
		this.grimFillColor = grimFillColor;
		this.grimFill = grimFill;
		model = tm;
	}
	public RectShape(Rectangle2D grimShape, float grimStrokeWidth, Color grimStrokeColor, 
			Color grimFillColor, boolean grimFill, TileModel tm) {
		super();
		this.rect2DShape = grimShape;
		this.grimStrokeWidth = grimStrokeWidth;
		this.grimStrokeColor = grimStrokeColor;
		this.grimFillColor = grimFillColor;
		this.grimFill = grimFill;
		model = tm;
	}
	public RectShape clone(){
		return new RectShape(rect2DShape, grimStrokeWidth, grimStrokeColor, grimFillColor, grimFill, model);
	}

	/**
	 * @return the grimShape
	 */
	public Rectangle2D getRect2D() {
		return rect2DShape;
	}
	/**
	 * @param grimShape the grimShape to set
	 */
	public void setRect2D(Rectangle2D grimShape) {
		this.rect2DShape = grimShape;
	}
	/**
	 * @return the grimStrokeWidth
	 */
	public Stroke getGrimStroke() {
		return new BasicStroke(this.grimStrokeWidth);
	}
	/**
	 * @param grimStrokeWidth the grimStrokeWidth to set
	 */
	public void setGrimStrokeWidth(Stroke grimStroke) {
		this.grimStrokeWidth = ((BasicStroke)grimStroke).getLineWidth();
	}
	/**
	 * @return the grimFill
	 */
	public boolean isGrimFill() {
		return grimFill;
	}
	/**
	 * @param grimFill the grimFill to set
	 */
	public void setGrimFill(boolean grimFill) {
		this.grimFill = grimFill;
	}
	public void draw(Graphics2D g2){  // !
		
		double rectX = mainFrame.tilemodel.getMilli2Pixel(rect2DShape.getX());
		double rectY = mainFrame.tilemodel.getMilli2Pixel(rect2DShape.getY());
		double rectW = mainFrame.tilemodel.getMilli2Pixel(rect2DShape.getWidth());
		double rectH = mainFrame.tilemodel.getMilli2Pixel(rect2DShape.getHeight());
				
		Rectangle2D scaledRect = new Rectangle2D.Double(rectX, rectY, rectW, rectH);
				
		if (grimStrokeColor!=null){
			g2.setStroke(new BasicStroke(2f));
			g2.setColor(grimStrokeColor);
			g2.draw(scaledRect);
		}

		if (isGrimFill()){
			g2.setColor(grimFillColor);
			g2.fill(scaledRect);
		}
		
		if((ZoomState.state == 0 || ZoomState.state == 1)){
			int i = 0;
			while(i < model.tileRectList.size()){
				
				RectShape rr = model.tileRectList.get(i);
				
				double newX = 0.0;
				double newY = 0.0;
				double newW = 0.0;
				double newH = 0.0;

				if(ZoomState.state == 0 && model.scaleIndex > 0){
					newX = rr.getRect2D().getX()*2;
					newY = rr.getRect2D().getY()*2;
					newW = rr.getRect2D().getWidth()*2;
					newH = rr.getRect2D().getHeight()*2;
					
					rr.setLoc(newX, newY);
					rr.setSize(newW, newH);
					model.tileRectList.set(i, rr);
				}
				else if(ZoomState.state == 1 && model.scaleIndex < model.getScaleMap().length-1){
					newX = rr.getRect2D().getX()/2;
					newY = rr.getRect2D().getY()/2;
					newW = rr.getRect2D().getWidth()/2;
					newH = rr.getRect2D().getHeight()/2;
					
					rr.setLoc(newX, newY);
					rr.setSize(newW, newH);
					model.tileRectList.set(i, rr);
				}
				
				i++;
			}
			
			GrimPanFrameMain.drawPanelView.repaint();
			ZoomState.state = -1;
		}

	}
	public boolean contains(double px, double py){
		
		
		double rectX = mainFrame.tilemodel.getMilli2Pixel(rect2DShape.getX());
		double rectY = mainFrame.tilemodel.getMilli2Pixel(rect2DShape.getY());
		double rectW = mainFrame.tilemodel.getMilli2Pixel(rect2DShape.getWidth());
		double rectH = mainFrame.tilemodel.getMilli2Pixel(rect2DShape.getHeight());
				
		Rectangle2D scaledRect = new Rectangle2D.Double(rectX, rectY, rectW, rectH);
		return scaledRect.contains(px, py);
	}
	public void translate(double dx, double dy){
		double x = rect2DShape.getX();
		double y = rect2DShape.getY();
		double w = rect2DShape.getWidth();
		double h = rect2DShape.getHeight();
		rect2DShape.setRect(x+dx, y+dy, w, h);
	}
	public void setLoc(double x, double y){
		double w = rect2DShape.getWidth();
		double h = rect2DShape.getHeight();
		rect2DShape.setRect(x, y, w, h);
	}
	
	public void setSize(double sw, double sh){
		double x = rect2DShape.getX();
		double y = rect2DShape.getY();
		//double w = rect2DShape.getWidth();
		//double h = rect2DShape.getHeight();
		rect2DShape.setRect(x, y, sw, sh);
	}
	
	boolean intersects(RectShape tile){
		return rect2DShape.intersects(tile.getRect2D());
	}
	public Color getGrimStrokeColor() {
		return grimStrokeColor;
	}
	public void setGrimStrokeColor(Color grimStrokeColor) {
		this.grimStrokeColor = grimStrokeColor;
	}
	public Color getGrimFillColor() {
		return grimFillColor;
	}
	public void setGrimFillColor(Color grimFillColor) {
		this.grimFillColor = grimFillColor;
	}
	public float getGrimStrokeWidth() {
		return grimStrokeWidth;
	}
	public void setGrimStrokeWidth(float grimStrokeWidth) {
		this.grimStrokeWidth = grimStrokeWidth;
	}

}
