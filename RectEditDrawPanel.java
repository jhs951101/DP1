/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.strategy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

public class RectEditDrawPanel 	extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RectEditDrawPanel thisView = this;
	private static GrimPanModel grimmodel;
	private static TileModel model;
	DrawPanelMouseAdapter mouseAdapter = null;

	private int baseWidth = 400; // in milli
	private int baseHeight = 300; // in milli

	public JPopupMenu jPopupMenuEdit = null;

	public RectEditDrawPanel(int w, int h, GrimPanModel gm, TileModel tm){
		this.model = tm;
		this.grimmodel = gm;

		this.baseWidth = w;
		this.baseHeight = h;
		model.setBaseWidth(getPixelWidth());
		model.setBaseHeight(getPixelHeight());

		this.setPreferredSize(new Dimension(model.getBaseWidth(), model.getBaseHeight()));
		this.setSize(new Dimension(model.getBaseWidth(), model.getBaseHeight()));

		//this.mouseAdapter = new DrawPanelMouseAdapter(model, this);
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);

	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(TileModel.BASE_COLOR);
		g2.fillRect(0, 0, (int)(model.getMilli2Pixel(baseWidth)+1), (int)(model.getMilli2Pixel(baseHeight)+1));

		for (int i=0; i<model.tileRectList.size(); ++i){
			RectShape tile = model.tileRectList.get(i);
			if (i==model.getSelIndex()){
				RectShape gsclone = tile.clone();
				gsclone.setGrimFillColor(TileModel.SELECT_COLOR);
				gsclone.draw(g2);
			}
			else {
				tile.draw(g2);
			}
		}
		if (model.selMarkList!= null){
			for (RectShape mark:model.selMarkList){
				mark.draw(g2);
			}
		}
		if (model.curDrawShape != null){
			model.curDrawShape.draw(g2);
		}
	}
	public int getBaseWidth() {
		return baseWidth;
	}
	public void setBaseWidth(int baseWidth) {
		this.baseWidth = baseWidth;
	}
	public int getBaseHeight() {
		return baseHeight;
	}
	public void setBaseHeight(int baseHeight) {
		this.baseHeight = baseHeight;
	}
	public int getPixelWidth(){
		return (int)model.getMilli2Pixel(baseWidth);
	}
	public int getPixelHeight(){
		return (int)model.getMilli2Pixel(baseHeight);
	}

}
