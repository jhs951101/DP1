/**
 * Created on 2015. 4. 13.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.strategy;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 * @author cskim
 *
 */
public class DrawPanelMouseAdapter extends MouseInputAdapter {

	static final int  N_MARK = 0;
	static final int  E_MARK = 1;
	static final int  S_MARK = 2;
	static final int  W_MARK = 3;

	static final int  NE_MARK = 4;
	static final int  NW_MARK = 5;
	static final int  SE_MARK = 6;
	static final int  SW_MARK = 7;

	double selRectX = 0;
	double selRectY = 0;
	double selRectW = 0;
	double selRectH = 0;

	private static TileModel model;
	private static GrimPanModel grimmodel;
	private BaseTileFrame frame = null;
	private DrawPanelView drawView = null;
	private GrimPanFrameMain mainFrame = null;
	int markIndex = -1;
	double origX = 0;
	double origY = 0;

	public DrawPanelMouseAdapter(TileModel model, DrawPanelView view, GrimPanModel grimmodel, GrimPanFrameMain mf){
		
		this.model = model;
		this.drawView = view;
		this.grimmodel = grimmodel;
		this.mainFrame = mf;
		
		if(this.model == null) System.out.println("null");
	}
	public void mouseMoved(MouseEvent ev){
		performMouseMoved(ev);
	}
	public void mousePressed(MouseEvent ev) {

		if (SwingUtilities.isLeftMouseButton(ev)){
			performMousePressed(ev);
		}
		drawView.repaint();
	}

	public void mouseReleased(MouseEvent ev) {

		if (ev.isPopupTrigger()){
			Point p1 = ev.getPoint();
			model.setMousePosition(p1);
			model.setClickedMousePosition(p1);
			
			drawView.jPopupMenuEdit.show(drawView, ev.getPoint().x, ev.getPoint().y);
		}

		if (SwingUtilities.isLeftMouseButton(ev)){
			performMouseReleased(ev);
		}
		drawView.repaint();

	}

	public void mouseDragged(MouseEvent ev) {

		if (SwingUtilities.isLeftMouseButton(ev)){
			performMouseDragged(ev);
		}
		drawView.repaint();

	}
	void performMouseMoved(MouseEvent e){
		Point p1 = e.getPoint();
		model.setMousePosition(p1);

		switch (model.getEditState()){
		case Util.SHAPE_RECT:
			break;
		case Util.RECT_MOVE:
			break;
		case Util.RECT_RESIZE:
			changeResizeCursor();
			break;
		}
	}
	public void performMousePressed(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);
		model.setClickedMousePosition(p1);
		//System.out.println(p1.x);
		//System.out.println(p1.y);

		switch (model.getEditState()){
		case Util.SHAPE_RECT:
			model.selMarkList = null;
			genRectangle2D();
			break;
		case Util.RECT_MOVE:
			model.selMarkList = null;
			model.getSelectedShape();
			int selIndex = model.getSelIndex();
			if (selIndex != -1){
				Rectangle2D rect = (Rectangle2D)model.tileRectList.get(selIndex).getRect2D();
				origX = rect.getX();
				origY = rect.getY();
				drawView.setCursor(model.moveCursor);
			}
			break;
		case Util.RECT_RESIZE:
			if (markIndex==-1){
				model.getSelectedShape();
			}
			setMarkOfSelectedRec();
			break;
		case Util.RECT_DELETE:

			break;

		}
	}

	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMouseReleased(java.awt.event.MouseEvent)
	 */
	public void performMouseReleased(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setMousePosition(p1);
		//System.out.println("Released Mouse Point=("+p1.x+","+p1.y+")");

		switch (model.getEditState()){
		case Util.SHAPE_RECT:
			genRectangle2D();
			addShapeAction();
			break;
		case Util.RECT_MOVE:
			endShapeMove();
			drawView.setCursor(model.defaultCursor);
			break;
		case Util.RECT_RESIZE:
			break;
		case Util.RECT_DELETE:

			break;
		}
	}

	/* (non-Javadoc)
	 * @see hufs.cse.grimpan.strategy.ShapeBuilder#performMouseDragged(java.awt.event.MouseEvent)
	 */
	public void performMouseDragged(MouseEvent e) {
		Point p1 = e.getPoint();
		model.setLastMousePosition(model.getMousePosition());
		model.setMousePosition(p1);

		//System.out.println("Dragged Mouse Point=("+p1.x+","+p1.y+")");

		switch (model.getEditState()){
		case Util.SHAPE_RECT:
			genRectangle2D();
			break;
		case Util.RECT_MOVE:
			moveShapeByMouse();
			break;
		case Util.RECT_RESIZE:
			if (markIndex!=-1){
				int selIndex = model.getSelIndex();
				Rectangle2D rect = model.tileRectList.get(selIndex).getRect2D();
				resizeRectangel2D(rect);
				setMarkPointRect(rect);			
			}
			break;
		case Util.RECT_DELETE:

			break;

		}
	}

	private void genRectangle2D(){  // 사각형를 생성하는 함수
		Point pi = model.getMousePosition();
		Point topleft = model.getClickedMousePosition();
		
		if (pi.distance(new Point2D.Double(topleft.x, topleft.y)) <= TileModel.MIN_DIST){
			return;
		}

		Rectangle2D rect = new Rectangle2D.Double(
				model.getPixel2Milli(topleft.x), model.getPixel2Milli(topleft.y),
				model.getPixel2Milli(pi.x-topleft.x), model.getPixel2Milli(pi.y-topleft.y));

		RectShape newTile = new RectShape(rect, model);
		if (model.isTileCollide(newTile)) return; // no new shape for collision
		
		model.curDrawShape = newTile;
	}
	public void addShapeAction() {  // 사각형을 List에 추가하는 함수
		if (model.curDrawShape != null){
			model.addTileRect(model.curDrawShape);
			model.curDrawShape = null;
		}
	}
	private void moveShapeByMouse(){  // 도형을 이동시키는 함수
		int selIndex = model.getSelIndex();
		RectShape shape = null;
		if (selIndex != -1){
			shape = model.tileRectList.get(selIndex);
			//shape.translate(
			//		model.getMousePosition().x-model.getLastMousePosition().x, 
			//		model.getMousePosition().y-model.getLastMousePosition().y);
			Rectangle2D rect = shape.getRect2D();
			double oldX = rect.getX();
			double oldY = rect.getY();
			
			shape.setLoc(
					origX+model.getPixel2Milli(model.getMousePosition().x-model.getClickedMousePosition().x),
					origY+model.getPixel2Milli(model.getMousePosition().y-model.getClickedMousePosition().y));
			if (model.isTileCollide(selIndex)){ // if collide to other tile then dont move
				shape.setLoc(oldX, oldY);  // 다른 도형과 부딫치면 이동하지 않음
			}
		}
	}
	private void endShapeMove(){
		int selIndex = model.getSelIndex();
		RectShape shape = null;
		if (selIndex != -1){
			shape = model.tileRectList.get(selIndex);
			Color scolor = shape.getGrimStrokeColor();
			Color fcolor = shape.getGrimFillColor();
			if (scolor!=null){
				shape.setGrimStrokeColor(new Color (scolor.getRed(), scolor.getGreen(), scolor.getBlue()));
			}
			if (fcolor!=null){
				shape.setGrimFillColor(new Color (fcolor.getRed(), fcolor.getGreen(), fcolor.getBlue()));
			}
		}
	}
	void changeResizeCursor(){
		markIndex = -1;
		if (model.selMarkList!=null){
			for(int i=0; i<model.selMarkList.size(); ++i){
				RectShape gs = model.selMarkList.get(i);
				if (gs.contains(model.getMousePosition().getX(), model.getMousePosition().getY())){
					markIndex = i;
					break;
				}				
			}
			if (markIndex != -1){
				drawView.setCursor(model.resizeCursor[markIndex]);
			}
			else {
				drawView.setCursor(model.defaultCursor);

			}
		}
	}
	public void setMarkOfSelectedRec(){  // 크기 조절 마크가 나오게 하는 함수
		int selIndex = model.getSelIndex();
		if (selIndex != -1){
			Rectangle2D rect = model.tileRectList.get(selIndex).getRect2D();
			
			setMarkPointRect(rect);			
			selRectX = rect.getX();
			selRectY = rect.getY();
			selRectW = rect.getWidth();
			selRectH = rect.getHeight();

		}
		else {
			model.selMarkList = null;
			markIndex = -1;
		}
		
	}
	void setMarkPointRect(Rectangle2D rect){  // 크기 조절 시 마크가 도형을 따라가게 하는 함수

		Point2D cn = new Point2D.Double(rect.getX()+rect.getWidth()/2, rect.getY());
		Point2D ce = new Point2D.Double(rect.getX()+rect.getWidth(), rect.getY()+rect.getHeight()/2);
		Point2D cs = new Point2D.Double(rect.getX()+rect.getWidth()/2, rect.getY()+rect.getHeight());
		Point2D cw = new Point2D.Double(rect.getX(), rect.getY()+rect.getHeight()/2);

		Point2D ne = new Point2D.Double(rect.getX()+rect.getWidth(), rect.getY());
		Point2D nw = new Point2D.Double(rect.getX(), rect.getY());
		Point2D se = new Point2D.Double(rect.getX()+rect.getWidth(), rect.getY()+rect.getHeight());
		Point2D sw = new Point2D.Double(rect.getX(), rect.getY()+rect.getHeight());

		model.selMarkList = new ArrayList<RectShape>();
		model.selMarkList.add(new PointRect(cn));//0
		model.selMarkList.add(new PointRect(ce));//1
		model.selMarkList.add(new PointRect(cs));//2
		model.selMarkList.add(new PointRect(cw));//3
		model.selMarkList.add(new PointRect(ne));//4
		model.selMarkList.add(new PointRect(nw));//5
		model.selMarkList.add(new PointRect(se));//6
		model.selMarkList.add(new PointRect(sw));//7

	}
	void resizeRectangel2D(Rectangle2D rect){  // 크기 조절 후 도형을 세팅하는 함수
		Point pi = model.getMousePosition();
		Point pold = model.getClickedMousePosition();
		double dx = model.getPixel2Milli(pi.x - pold.x);
		double dy = model.getPixel2Milli(pi.y - pold.y);

		double nx = selRectX;
		double ny = selRectY;
		double nw = selRectW;
		double nh = selRectH;

		switch (markIndex){
		case N_MARK: // NORTH
			ny += dy;
			nh -= dy;
			break;
		case E_MARK: // EAST
			nw += dx;
			break;
		case S_MARK: // SOUTH
			nh += dy;
			break;
		case W_MARK: // WEST
			nx += dx;
			nw -= dx;
			break;
		case NE_MARK:
			nw += dx;
			ny += dy;
			nh -=dy;			
			break;
		case SE_MARK: 
			nw += dx;
			nh +=dy;			
			break;
		case NW_MARK: 
			nx += dx;
			ny += dy;
			nw -= dx;
			nh -= dy;
			break;
		case SW_MARK:
			nx += dx;
			nw -= dx;
			nh += dy;
			break;
		}
		if (nw < TileModel.MIN_DIST) return;
		if (nh < TileModel.MIN_DIST) return;
		
		double nxSave = rect.getX();
		double nySave = rect.getY();
		double nwSave = rect.getWidth();
		double nhSave = rect.getHeight();
		
		rect.setRect(nx, ny, nw, nh);
		if (model.isTileCollide(model.getSelIndex())) {
			rect.setRect(nxSave, nySave, nwSave, nhSave);
		}
	}
}
