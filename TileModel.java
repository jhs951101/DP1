/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.strategy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class TileModel{
	
	private GrimPanFrameMain mainFrame = null;
	private volatile static TileModel uniqueModelInstance;
	
	public static final int MIN_DIST = 8;
	public static final Color TILE_COLOR = new Color(220, 190, 160);
	public static final Color BASE_COLOR = new Color(180, 120, 60);
	public static final Color SELECT_COLOR = new Color(240, 220, 210);

	private int unitPixels = 56;
	private final double [] scaleMap = {4.0, 3.0, 2.0, 1.0, 0.5};
	public static int scaleIndex = 3;// default 1:1
	
	private String defaultDir = "C:/home/cskim/temp/";
	
	private int editState = Util.SHAPE_LINE;
	
	public final Cursor defaultCursor = Cursor.getDefaultCursor();
	public final Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
	public final Cursor resizeN = new Cursor(Cursor.N_RESIZE_CURSOR);
	public final Cursor resizeE = new Cursor(Cursor.E_RESIZE_CURSOR);
	public final Cursor resizeS = new Cursor(Cursor.S_RESIZE_CURSOR);
	public final Cursor resizeW = new Cursor(Cursor.W_RESIZE_CURSOR);
	public final Cursor resizeNE = new Cursor(Cursor.NE_RESIZE_CURSOR);
	public final Cursor resizeNW = new Cursor(Cursor.NW_RESIZE_CURSOR);
	public final Cursor resizeSE = new Cursor(Cursor.SE_RESIZE_CURSOR);
	public final Cursor resizeSW = new Cursor(Cursor.SW_RESIZE_CURSOR);
	public Cursor[] resizeCursor = {
			resizeN, resizeE, resizeS, resizeW,
			resizeNE, resizeNW, resizeSE, resizeSW			
	};
	public static Stroke shapeStroke = new BasicStroke(2f);
	public static Color strokeColor = Color.white;
	public static Color fillColor = TILE_COLOR;
	public static boolean shapeFill = true;
	
	public ArrayList<RectShape> tileRectList = null;
	public ArrayList<RectShape> selMarkList = null;
	
	private Point mousePosition = null;
	private Point clickedMousePosition = null;
	private Point lastMousePosition = null;
	
	public RectShape curDrawShape = null;
	//private RectShape savedPositionShape = null;
	
	private int baseWidth = 700; 
	private int baseHeight = 500;
	private double xMarginLow = 0;
	private double xMarginHigh = getPixel2Milli(baseWidth);
	private double yMarginLow = 0;
	private double yMarginHigh = getPixel2Milli(baseHeight);
	
	public int selIndex = 0;
		
	public BaseTileFrame tileFrame = null;
	
	GrimPanFrameMain gpm = null;
	
	public TileModel(GrimPanFrameMain mf){
		this.tileRectList = new ArrayList<RectShape>();
		this.shapeStroke = new BasicStroke();
		this.mainFrame = mf;
	}
	
	public void getSelectedShape(){
		selIndex = -1;
		RectShape shape = null;
		for (int i=tileRectList.size()-1; i >= 0; --i){
			shape = tileRectList.get(i);
			if (shape.contains(getMousePosition().getX(), getMousePosition().getY())){
				selIndex = i;
				break;
			}
		}
		if (selIndex != -1){
			setLastMousePosition(getClickedMousePosition());
		}
		setSelIndex(selIndex);
	}
	public void addTileRect(RectShape tileShape){
		if (tileShape instanceof RectShape){
			tileRectList.add(tileShape);
		}
		else {
			tileRectList.add(new TileRect(tileShape));
		}
		 mainFrame.measurementChanged();
	}
	public boolean isTileCollide(int sindex){

		RectShape selTile = tileRectList.get(sindex);
		Rectangle2D rect = selTile.getRect2D();
		//System.out.println("xleft="+String.valueOf(rect.getX()+rect.getWidth())+" xmarhigh="+xMarginHigh);
		if (rect.getX() < this.xMarginLow) return true;
		if (rect.getX()+rect.getWidth() > this.xMarginHigh) return true;
		if (rect.getY() < this.yMarginLow) return true;
		if (rect.getY()+rect.getHeight() > this.yMarginHigh) return true;
		
		for (int i=0; i<tileRectList.size(); ++i){
			if (i==sindex) continue;
			if (selTile.intersects(tileRectList.get(i))){
				//System.out.println("tile "+sindex+" and tile "+i+" collide");
				return true;
			}
		}		
		return false;
	}
	public boolean isTileCollide(RectShape selTile){

		Rectangle2D rect = selTile.getRect2D();
		//System.out.println("xleft="+String.valueOf(rect.getX()+rect.getWidth())+" xmarhigh="+xMarginHigh);
		if (rect.getWidth() < getPixel2Milli(MIN_DIST)) return true;
		if (rect.getHeight() < getPixel2Milli(MIN_DIST)) return true;
		
		if (rect.getX() < this.xMarginLow) return true;
		if (rect.getX()+rect.getWidth() > this.xMarginHigh) return true;
		if (rect.getY() < this.yMarginLow) return true;
		if (rect.getY()+rect.getHeight() > this.yMarginHigh) return true;
		
		for (int i=0; i<tileRectList.size(); ++i){
			if (selTile.intersects(tileRectList.get(i))){
				//System.out.println("tile "+sindex+" and tile "+i+" collide");
				return true;
			}
		}		
		return false;
	}

	public Point getMousePosition() {
		return mousePosition;
	}

	public void setMousePosition(Point mousePosition) {
		this.mousePosition = mousePosition;
	}
	public Point getLastMousePosition() {
		return lastMousePosition;
	}

	public void setLastMousePosition(Point mousePosition) {
		this.lastMousePosition = mousePosition;
	}

	public Point getClickedMousePosition() {
		return clickedMousePosition;
	}

	public void setClickedMousePosition(Point clickedMousePosition) {
		this.clickedMousePosition = clickedMousePosition;
	}

	/**
	 * @return the selectedShape
	 */
	public int getSelIndex() {
		return selIndex;
	}

	/**
	 * @param selectedShape the selectedShape to set
	 */
	public void setSelIndex(int selectedShape) {
		this.selIndex = selectedShape;
	}

	public Stroke getShapeStroke() {
		return shapeStroke;
	}

	public void setShapeStroke(Stroke shapeStroke) {
		this.shapeStroke = shapeStroke;
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public boolean isShapeFill() {
		return shapeFill;
	}

	public void setShapeFill(boolean shapeFill) {
		this.shapeFill = shapeFill;
	}
	/**
	 * @return the defaultDir
	 */
	public String getDefaultDir() {
		return defaultDir;
	}
	/**
	 * @param defaultDir the defaultDir to set
	 */
	public void setDefaultDir(String defaultDir) {
		this.defaultDir = defaultDir;
	}
/*
	public void setSavedPositionShape(RectShape grimShape) {
		savedPositionShape = grimShape.clone();		
	}
	public RectShape getSavedPositionShape() {
		return savedPositionShape;		
	}
	*/
	public int getEditState() {
		return editState;
	}

	public void setEditState(int editState) {
		this.editState = editState;
	}
	public int getUnitPixels() {
		return unitPixels;
	}

	public void setUnitPixels(int unitPixels) {
		this.unitPixels = unitPixels;
	}
	public double getScaleRatio(){
		return scaleMap[scaleIndex];
	}
	/*
	public int getMilli2Pixel(int mil){
		return (int)(mil*getUnitPixels()*getScaleRatio()/10);
	}
	public int getPixel2Milli(int pix){
		return (int)(pix*10/getScaleRatio()/getUnitPixels());
	}
	*/
	public double getMilli2Pixel(double mil){
		return (double)(mil*getUnitPixels()*getScaleRatio()/10);
	}
	public double getPixel2Milli(double pix){
		return (double)(pix*10/getScaleRatio()/getUnitPixels());
	}
	public int getBaseWidth() {
		return baseWidth;
	}
	public void setBaseWidth(int baseWidth) {
		this.baseWidth = baseWidth;
		this.xMarginHigh = getPixel2Milli(baseWidth);
	}
	public int getBaseHeight() {
		return baseHeight;
	}
	public void setBaseHeight(int baseHeight) {
		this.baseHeight = baseHeight;
		this.yMarginHigh = getPixel2Milli(baseHeight);
	}
	public double[] getScaleMap() {
		return scaleMap;
	}
	
}
