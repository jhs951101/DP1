package hufs.cse.grimpan.strategy;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.JPopupMenu;

public class BaseTileFrame extends JFrame {
	
	private volatile static BaseTileFrame uniqueModelInstance;

	private int baseWidth = 400;
	private int baseHeight = 300;
	private static GrimPanModel grimmodel;
	private static TileModel model;

	public RectEditDrawPanel drawPanelView = null;

	private JMenuBar menuBar;
	private JMenu mnEdit;
	private JMenuItem jmiAdd;
	private JMenuItem jmiMove;
	private JMenuItem jmiResize;
	private JPanel contentPane;
	private JScrollPane jspViewArea;

	public Ruler columnView = null;
	public Ruler rowView = null;
	private JMenuItem jmiGrid;
	
	TileGridDialog tgDialog = null;
	
	/**
	 * Create the frame.
	 */
	
	public BaseTileFrame() {
		
	}

	public BaseTileFrame(int scaleIndex, GrimPanModel gm, TileModel tm) {
		
		this.grimmodel = gm;
		this.model = tm;
		this.model.scaleIndex = scaleIndex;
		this.model.tileFrame = this;

		initialize(tm);
		
		tgDialog = new TileGridDialog(this, model);
	}
	
	private void initialize(TileModel tm){
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setJMenuBar(getMenuBar1());
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getJspViewArea(tm), BorderLayout.CENTER);
		
		drawPanelView = new RectEditDrawPanel(baseWidth, baseHeight, grimmodel, model);
		
		jspViewArea.setViewportView(drawPanelView);
		this.validate();

	}

	private JMenuBar getMenuBar1() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMnEdit());
		}
		return menuBar;
	}
	private JMenu getMnEdit() {
		if (mnEdit == null) {
			mnEdit = new JMenu("Edit-Tile");
			mnEdit.add(getJmiGrid());
			mnEdit.add(getJmiAdd());
			mnEdit.add(getJmiMove());
			mnEdit.add(getJmiResize());
		}
		return mnEdit;
	}
	private JMenuItem getJmiAdd() {
		if (jmiAdd == null) {
			jmiAdd = new JMenuItem("Tile Add  ");
			jmiAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.setEditState(Util.SHAPE_RECT);
				}
			});
		}
		return jmiAdd;
	}
	private JMenuItem getJmiMove() {
		if (jmiMove == null) {
			jmiMove = new JMenuItem("Tile Move  ");
			jmiMove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.setEditState(Util.RECT_MOVE);
				}
			});
		}
		return jmiMove;
	}
	private JMenuItem getJmiResize() {
		if (jmiResize == null) {
			jmiResize = new JMenuItem("Tile Resize");
			jmiResize.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.setEditState(Util.RECT_RESIZE);

				}
			});
		}
		return jmiResize;
	}

	public JScrollPane getJspViewArea(TileModel tm) {
		
		if (jspViewArea == null) {
			jspViewArea = new JScrollPane();
			model = tm;
			
			jspViewArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jspViewArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jspViewArea.getHorizontalScrollBar().setUnitIncrement(20);
			jspViewArea.getVerticalScrollBar().setUnitIncrement(20);			
			//System.out.println("Hor. Scroll Inc. Unit = "+jspViewArea.getHorizontalScrollBar().getUnitIncrement());
			//System.out.println("Ver. Scroll Inc. Unit = "+jspViewArea.getVerticalScrollBar().getUnitIncrement());
			getColumnView();
			getRowView();
			jspViewArea.setColumnHeaderView(columnView);
			jspViewArea.setRowHeaderView(rowView);
			
			jspViewArea.setCorner(JScrollPane.UPPER_LEFT_CORNER, new LCorner(model.getScaleRatio()));
		}
		return jspViewArea;
	}
	
	/**
	 * This method initializes columnView	
	 * 	
	 * @return modenbox.Ruler	
	 */
	private Ruler getColumnView() {
		if (columnView == null) {
			columnView = new HRuler(1, model);
			
			columnView.setPreferredWidth(800);
		}
		return columnView;
	}

	/**
	 * This method initializes rowView	
	 * 	
	 * @return modenbox.Ruler	
	 */
	private Ruler getRowView() {
		if (rowView == null) {
			rowView = new VRuler(1, model);
			rowView.setPreferredHeight(600);
		}
		return rowView;
	}
	private JMenuItem getJmiGrid() {
		if (jmiGrid == null) {
			jmiGrid = new JMenuItem("Tile Grid");
			jmiGrid.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gridTileAction();
				}
			});
		}
		return jmiGrid;
	}
	void gridTileAction(){
		Rectangle dbounds = tgDialog.getBounds();
		Rectangle fbounds = this.getBounds();
		dbounds.setLocation(fbounds.x+100, fbounds.y+100);
		
		tgDialog.setBounds(dbounds);
		tgDialog.setVisible(true);
	}

}
