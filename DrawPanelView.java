/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.strategy;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

public class DrawPanelView 
	extends JPanel implements MouseInputListener {

	/**
	 * 
	 */
	
	private GrimPanFrameMain mainFrame = null;
	
	private static final long serialVersionUID = 1L;
	private static GrimPanModel model;
	private static TileModel tilemodel;
	private DrawPanelView thisView = this;
	
	private int baseWidth = 400; // in milli
	private int baseHeight = 300; // in milli
	
	public JPopupMenu jPopupMenuEdit = null;
	private JMenuItem jpmiZoomIn = null;
	private JMenuItem jpmiZoomOut = null;
	private JMenuItem jpmiMove = null;
	private JMenuItem jpmiResize = null;
	private JMenuItem jpmiDelete = null;
	
	DrawPanelMouseAdapter mouseAdapter = null;
	
	public DrawPanelView(GrimPanModel model, TileModel tilemodel, GrimPanFrameMain mf, int w, int h){

		
		this.model = model;
		this.tilemodel = tilemodel;
		this.mainFrame = mf;
		
		this.baseWidth = w;
		this.baseHeight = h;
		this.tilemodel.setBaseWidth(getPixelWidth());
		this.tilemodel.setBaseHeight(getPixelHeight());

		this.setPreferredSize(new Dimension(this.tilemodel.getBaseWidth(), this.tilemodel.getBaseHeight()));
		this.setSize(new Dimension(this.tilemodel.getBaseWidth(), this.tilemodel.getBaseHeight()));
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		this.mouseAdapter = new DrawPanelMouseAdapter(tilemodel, this, model, this.mainFrame);
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		
		this.tilemodel.tileFrame = new BaseTileFrame();
		
		getJPopupMenuEdit();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (GrimShape grimShape:model.shapeList){
			grimShape.draw(g2);
		}
		
		for (RectShape rShape:tilemodel.tileRectList){
			rShape.draw(g2);
		}

		if (model.curDrawShape != null && model.getEditState()!=Util.SHAPE_RECT){
			g2.setColor(model.getShapeStrokeColor());
			g2.setStroke(new BasicStroke(model.getShapeStrokeWidth()));
			
			g2.draw(model.curDrawShape);

			if (model.isShapeFill() 
					&& model.getEditState()!=Util.SHAPE_PENCIL){
				g2.setColor(model.getShapeFillColor());
				g2.fill(model.curDrawShape);
			}
		}
		
		for (int i=0; i<tilemodel.tileRectList.size(); ++i){
			RectShape tile = tilemodel.tileRectList.get(i);
			if (i==tilemodel.getSelIndex()){
				RectShape gsclone = tile.clone();
				gsclone.setGrimFillColor(TileModel.SELECT_COLOR);
				gsclone.draw(g2);
			}
			else {
				tile.draw(g2);
			}
		}
		if (tilemodel.selMarkList!= null){
			for (RectShape mark:tilemodel.selMarkList){
				mark.draw(g2);
			}
		}
		if (tilemodel.curDrawShape != null){
			tilemodel.curDrawShape.draw(g2);
		}
	}
	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}
	public void mouseMoved(MouseEvent ev) {

	}

	public void mousePressed(MouseEvent ev) {

		if (SwingUtilities.isLeftMouseButton(ev)){
			model.sb.performMousePressed(ev);
		}
		repaint();
	}

	public void mouseReleased(MouseEvent ev) {

		if (SwingUtilities.isLeftMouseButton(ev)){
			model.sb.performMouseReleased(ev);
		}
		repaint();

	}

	public void mouseDragged(MouseEvent ev) {
		
		if (SwingUtilities.isLeftMouseButton(ev)){
			model.sb.performMouseDragged(ev);
		}
		repaint();

	}
	
	private JPopupMenu getJPopupMenuEdit() {
		if (jPopupMenuEdit == null) {
			jPopupMenuEdit = new JPopupMenu();
			jPopupMenuEdit.add(getJpmiZoomIn());
			jPopupMenuEdit.add(getJpmiZoomOut());
			jPopupMenuEdit.addSeparator();
			jPopupMenuEdit.add(getJpmiMove());
			jPopupMenuEdit.add(getJpmiResize());
			jPopupMenuEdit.add(getJpmiDelete());
		}
		return jPopupMenuEdit;
	}
	
	private JMenuItem getJpmiZoomIn() {
		if (jpmiZoomIn == null) {
			jpmiZoomIn = new JMenuItem("Zoom In",KeyEvent.VK_Z);
			jpmiZoomIn.setText("크게 ");
			jpmiZoomIn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ZoomState.state = 0;
					viewZoomIn();
				}
			});
			KeyStroke ctrlPlusKeyStroke = 
					KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK);
			jpmiZoomIn.setAccelerator(ctrlPlusKeyStroke);
		}
		return jpmiZoomIn;
	}

	/**
	 * This method initializes jpmiZoomOut	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJpmiZoomOut() {
		if (jpmiZoomOut == null) {
			jpmiZoomOut = new JMenuItem("Zoom Out",KeyEvent.VK_M);
			jpmiZoomOut.setText("작게 ");
			jpmiZoomOut.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ZoomState.state = 1;
					viewZoomOut();
				}
			});
			KeyStroke ctrlMinusKeyStroke = 
					KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK);
			jpmiZoomOut.setAccelerator(ctrlMinusKeyStroke);
		}
		return jpmiZoomOut;
	}
	
	public void viewZoomOut(){  // 크기를 축소하는 함수
		if (tilemodel.scaleIndex < tilemodel.getScaleMap().length-1){
			++tilemodel.scaleIndex;
			repaint();
		}

	}
	public void viewZoomIn(){  // 크기를 확대하는 함수
		if (tilemodel.scaleIndex > 0){
			--tilemodel.scaleIndex;
			repaint();
		}
	}
	
	private JMenuItem getJpmiMove() {
		if (jpmiMove == null) {
			jpmiMove = new JMenuItem();
			jpmiMove.setText("이동");
			jpmiMove.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					model.setEditState(Util.RECT_MOVE);
					tilemodel.setEditState(Util.RECT_MOVE);
					tilemodel.selMarkList = null;
					tilemodel.getSelectedShape();
					thisView.repaint();
				}
			});
		}
		return jpmiMove;
	}

	private JMenuItem getJpmiResize() {
		if (jpmiResize == null) {
			jpmiResize = new JMenuItem();
			jpmiResize.setText("크기조정");
			jpmiResize.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					model.setEditState(Util.RECT_RESIZE);
					tilemodel.setEditState(Util.RECT_RESIZE);
					tilemodel.getSelectedShape();
					thisView.mouseAdapter.setMarkOfSelectedRec();
					thisView.repaint();
				}
			});
		}
		return jpmiResize;
	}

	private JMenuItem getJpmiDelete() {
		if (jpmiDelete == null) {
			jpmiDelete = new JMenuItem();
			jpmiDelete.setText("삭제");
			jpmiDelete.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					tilemodel.getSelectedShape();
					thisView.repaint();
					
					int delCheck = JOptionPane.showConfirmDialog(thisView, "선택한 타일을 삭제하겠습니까?", 
							"Delete Tile", JOptionPane.OK_CANCEL_OPTION);
					if (delCheck == JOptionPane.OK_OPTION){
						// delete tile action
						tilemodel.tileRectList.remove(tilemodel.getSelIndex());
						//System.out.println("delete tile");
						
						mainFrame.measurementChanged();
						
						tilemodel.setSelIndex(-1);
						thisView.repaint();
					}
				}
			});
		}
		return jpmiDelete;
	}
	
	public int getPixelWidth(){
		return (int)tilemodel.getMilli2Pixel(baseWidth);
	}
	public int getPixelHeight(){
		return (int)tilemodel.getMilli2Pixel(baseHeight);
	}

}
