/**
 * Created on 2015. 3. 8.
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package hufs.cse.grimpan.strategy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.border.EtchedBorder;

/**
 * @author cskim
 *
 */
public class GrimPanFrameMain extends JFrame implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GrimPanFrameMain thisClass = this;
	private JPanel contentPane;
	public static DrawPanelView drawPanelView = null;
	public static GrimPanModel model = null;
	public static TileModel tilemodel = null;

	public JCheckBoxMenuItem jcmiFill = null;
	private JFileChooser jFileChooser1 = null;
	private JFileChooser jFileChooser2 = null;
	private String defaultDir = "/home/cskim/temp/";

	JMenuBar panMenuBar;	
	JMenu fileMenu;
	JMenuItem jmiNew;
	JMenuItem jmiOpen;
	JMenuItem jmiSave;
	JMenuItem jmiSaveAs;
	JMenuItem jmiSaveAsSvg;
	JMenuItem jmiExit;
	JMenu shapeMenu;
	JRadioButtonMenuItem rdbtnmntmLine;
	JRadioButtonMenuItem rdbtnmntmPen;	
	JRadioButtonMenuItem rdbtnmntmPoly;
	JRadioButtonMenuItem rdbtnmntmRegular;
	JRadioButtonMenuItem rdbtnmntmOval;
	JRadioButtonMenuItem rdbtnmntmRect;
	JMenu editMenu;	
	JMenuItem jmiRemove;
	JMenuItem jmiMove;
	JMenu settingMenu;
	JMenuItem jmiStroke;
	JMenuItem jmiStorkeColor;	
	JMenuItem jmiFillColor;

	JMenu helpMenu;	
	JMenuItem jmiAbout;

	ButtonGroup btnGroup = new ButtonGroup();
	JPanel statusPanel;
	JLabel shapeLbl;
	JLabel sizeLbl;
	JMenuItem rmiAdd;
	JLabel countLbl;

	public JLabel modeLBL;
	
	private JScrollPane jspViewArea;
	
	private int baseWidth = 400;
	private int baseHeight = 300;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GrimPanFrameMain frame = new GrimPanFrameMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GrimPanFrameMain() {
		
		model = new GrimPanModel(this);
		tilemodel = new TileModel(this);
		
		drawPanelView = new DrawPanelView(model, tilemodel, this, baseWidth, baseHeight);
		
		//drawPanelView = new RectEditDrawPanel(baseWidth, baseHeight);
		
		//this.tilemodel = TileModel.getInstance();
		//this.tilemodel.tileFrame = BaseTileFrame.getInstance();
		
		setTitle("�׸���");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);

		panMenuBar = new JMenuBar();
		setJMenuBar(panMenuBar);

		fileMenu = new JMenu("File  ");
		panMenuBar.add(fileMenu);

		jmiNew = new JMenuItem("New  ");
		jmiNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.shapeList.clear();
				model.curDrawShape = null;
				model.polygonPoints.clear();
				drawPanelView.repaint();
			}
		});
		fileMenu.add(jmiNew);

		jmiOpen = new JMenuItem("Open ");
		jmiOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openAction();
			}
		});
		fileMenu.add(jmiOpen);

		jmiSave = new JMenuItem("Save ");
		jmiSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveAction();
			}
		});
		fileMenu.add(jmiSave);

		jmiSaveAs = new JMenuItem("Save  As...");
		jmiSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveAsAction();
			}
		});
		fileMenu.add(jmiSaveAs);

		jmiExit = new JMenuItem("Exit");
		jmiExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(jmiExit);

		shapeMenu = new JMenu("Shape  ");
		panMenuBar.add(shapeMenu);

		rdbtnmntmLine = new JRadioButtonMenuItem("����");
		rdbtnmntmLine.setSelected(true);
		rdbtnmntmLine.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setEditState(Util.SHAPE_LINE);
					tilemodel.setEditState(Util.SHAPE_LINE);
				}
			}
		});
		shapeMenu.add(rdbtnmntmLine);

		rdbtnmntmPen = new JRadioButtonMenuItem("����");
		rdbtnmntmPen.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setEditState(Util.SHAPE_PENCIL);
					tilemodel.setEditState(Util.SHAPE_PENCIL);
				}
			}
		});
		shapeMenu.add(rdbtnmntmPen);

		rdbtnmntmPoly = new JRadioButtonMenuItem("�ٰ���");
		rdbtnmntmPoly.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setEditState(Util.SHAPE_POLYGON);
					tilemodel.setEditState(Util.SHAPE_POLYGON);
				}
			}
		});
		shapeMenu.add(rdbtnmntmPoly);

		rdbtnmntmRegular = new JRadioButtonMenuItem("���ٰ���");
		rdbtnmntmRegular.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setEditState(Util.SHAPE_REGULAR);
					tilemodel.setEditState(Util.SHAPE_REGULAR);
					if (model.curDrawShape != null){
						model.shapeList
						.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
								model.getShapeStrokeColor(), model.isShapeFill(), model.getShapeFillColor()));
						model.curDrawShape = null;
					}
					Object[] possibleValues = { 
							"3", "4", "5", "6", "7",
							"8", "9", "10", "11", "12"
					};
					Object selectedValue = JOptionPane.showInputDialog(null,
							"Choose one", "Input",
							JOptionPane.INFORMATION_MESSAGE, null,
							possibleValues, possibleValues[0]);
					model.setNPolygon(Integer.parseInt((String)selectedValue));
					//drawPanelView.repaint();
				}
			}
		});
		shapeMenu.add(rdbtnmntmRegular);

		rdbtnmntmOval = new JRadioButtonMenuItem("Ÿ����");
		rdbtnmntmOval.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setEditState(Util.SHAPE_OVAL);
					tilemodel.setEditState(Util.SHAPE_OVAL);
					if (model.curDrawShape != null){
						model.shapeList
						.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
								model.getShapeStrokeColor(), model.isShapeFill(), model.getShapeFillColor()));
						model.curDrawShape = null;
					}
				}
			}
		});
		shapeMenu.add(rdbtnmntmOval);
		
		rdbtnmntmRect = new JRadioButtonMenuItem("�簢��");
		rdbtnmntmRect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					model.setEditState(Util.SHAPE_RECT);
					tilemodel.setEditState(Util.SHAPE_RECT);
					if (tilemodel.curDrawShape != null){
						tilemodel.addTileRect(tilemodel.curDrawShape);
						tilemodel.curDrawShape = null;
					}
				}
			}
		});
		shapeMenu.add(rdbtnmntmRect);

		btnGroup.add(rdbtnmntmLine);
		btnGroup.add(rdbtnmntmPen);
		btnGroup.add(rdbtnmntmPoly);
		btnGroup.add(rdbtnmntmRegular);
		btnGroup.add(rdbtnmntmOval);
		btnGroup.add(rdbtnmntmRect);

		editMenu = new JMenu("Edit  ");
		panMenuBar.add(editMenu);

		rmiAdd = new JMenuItem("�߰�");
		rmiAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.setEditState(Util.SHAPE_LINE);
				tilemodel.setEditState(Util.SHAPE_LINE);
				rdbtnmntmLine.setSelected(true);
				if (model.curDrawShape != null){
					model.shapeList
					.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
							model.getShapeStrokeColor(), model.isShapeFill(), model.getShapeFillColor()));
					model.curDrawShape = null;
				}
				drawPanelView.repaint();
			}
		});
		editMenu.add(rmiAdd);

		jmiRemove = new JMenuItem("����");
		jmiRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setEditState(Util.EDIT_DELETE);
				tilemodel.setEditState(Util.EDIT_DELETE);
				if (model.curDrawShape != null){
					model.shapeList
					.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
							model.getShapeStrokeColor(), model.isShapeFill(), model.getShapeFillColor()));
					model.curDrawShape = null;
				}
				drawPanelView.repaint();
			}
		});
		editMenu.add(jmiRemove);

		jmiMove = new JMenuItem("�̵�");
		jmiMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.setEditState(Util.EDIT_MOVE);
				tilemodel.setEditState(Util.EDIT_MOVE);
				if (model.curDrawShape != null){
					model.shapeList
					.add(new GrimShape(model.curDrawShape, model.getShapeStrokeWidth(),
							model.getShapeStrokeColor(), model.isShapeFill(), model.getShapeFillColor()));
					model.curDrawShape = null;
				}
				drawPanelView.repaint();
			}
		});
		editMenu.add(jmiMove);

		settingMenu = new JMenu("Setting");
		panMenuBar.add(settingMenu);

		jmiStroke = new JMenuItem("���β�");
		jmiStroke.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputVal = JOptionPane.showInputDialog("���β�", "1");
				if (inputVal!=null){
					model.setShapeStrokeWidth(Float.parseFloat(inputVal));
				}
				else {
					model.setShapeStrokeWidth(1f);
				}
			}
		});
		settingMenu.add(jmiStroke);

		jmiStorkeColor = new JMenuItem("�׵θ���");
		jmiStorkeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = 
						JColorChooser.showDialog(thisClass, 
								"Choose a color",
								Color.black);					
				if (color!=null){
					model.setShapeStrokeColor(color);
				}
				else {
					model.setShapeStrokeColor(Color.black);
				}
				drawPanelView.repaint();
			}
		});
		settingMenu.add(jmiStorkeColor);

		jcmiFill = new JCheckBoxMenuItem("ä��");
		jcmiFill.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean fillState = jcmiFill.getState();
				model.setShapeFill(fillState);
				drawPanelView.repaint();
			}
		});
		settingMenu.add(jcmiFill);

		jmiFillColor = new JMenuItem("ä���");
		jmiFillColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = 
						JColorChooser.showDialog(thisClass, 
								"Choose a color",
								Color.black);					
				if (color!=null){
					model.setShapeFillColor(color);
				}
				else {
					model.setShapeFillColor(Color.black);
				}
				drawPanelView.repaint();
			}
		});
		settingMenu.add(jmiFillColor);

		helpMenu = new JMenu("Help  ");
		panMenuBar.add(helpMenu);

		jmiAbout = new JMenuItem("About");
		jmiAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(thisClass,
						"GrimPan Ver0.2.2 \nProgrammed by cskim, cse, hufs.ac.kr", 
						"About", JOptionPane.INFORMATION_MESSAGE);

			}
		});
		helpMenu.add(jmiAbout);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		drawPanelView.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				String sizeText = String.format("Size: %d x %d  ", 
						drawPanelView.getSize().width, drawPanelView.getSize().height);
				thisClass.sizeLbl.setText(sizeText);
			}
		});
		jspViewArea = new JScrollPane();
		contentPane.add(jspViewArea, BorderLayout.CENTER);

		statusPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) statusPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		statusPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(statusPanel, BorderLayout.SOUTH);

		sizeLbl = new JLabel("Size:               ");
		statusPanel.add(sizeLbl);

		shapeLbl = new JLabel("Shape:              ");
		shapeLbl.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(shapeLbl);
		
				modeLBL = new JLabel("Mode:               ");
				statusPanel.add(modeLBL);
				
						countLbl = new JLabel("Shape Count:               ");
						statusPanel.add(countLbl);

		jFileChooser1 = new JFileChooser(defaultDir);
		jFileChooser1.setDialogTitle("Open Saved GrimPan");

		jFileChooser2 = new JFileChooser(defaultDir);
		jFileChooser2.setDialogType(JFileChooser.SAVE_DIALOG);
		jFileChooser2.setDialogTitle("Save As ...");

		model.setEditState(Util.SHAPE_LINE);
		tilemodel.setEditState(Util.SHAPE_LINE);
		jspViewArea.setViewportView(drawPanelView);
		jspViewArea.setCorner(JScrollPane.UPPER_LEFT_CORNER, new LCorner(tilemodel.getScaleRatio()));
	}
	private void openAction() {
		if (jFileChooser1.showOpenDialog(this) ==
				JFileChooser.APPROVE_OPTION) {
			File selFile = jFileChooser1.getSelectedFile();
			model.readShapeFromSaveFile(selFile);
			model.setSaveFile(selFile);
			drawPanelView.repaint();
		}
	}
	private void saveAction() {
		if (model.getSaveFile()==null){
			model.setSaveFile(new File(defaultDir+"noname.grm"));
		}
		File selFile = model.getSaveFile();
		model.saveGrimPanData(selFile);	
	}
	private void saveAsAction() {
		if (jFileChooser2.showSaveDialog(this) ==
				JFileChooser.APPROVE_OPTION) {
			File selFile = jFileChooser2.getSelectedFile();
			model.setSaveFile(selFile);
			model.saveGrimPanData(selFile);
		}
	}

	@Override
	public void update(String shapeName, String Mode, int Count) {
		this.shapeLbl.setText(String.format("Shape: %s  ", shapeName));
		this.modeLBL.setText(String.format("Mode: %s  ", Mode));
		this.countLbl.setText(String.format("Shape Count: %s  ", Count));
	}
	
	public void measurementChanged(){
		int editState = model.getEditState();
		
		if (editState == Util.EDIT_MOVE){
			update(Util.SHAPE_NAME[model.getEditState()], "�̵�", model.shapeList.size()+tilemodel.tileRectList.size());
		}
		else if(editState == Util.EDIT_DELETE){
			update(Util.SHAPE_NAME[model.getEditState()], "����", model.shapeList.size()+tilemodel.tileRectList.size());
		}
		else if(editState == Util.RECT_MOVE){
			update(Util.SHAPE_NAME[model.getEditState()], "�̵�-�簢��", model.shapeList.size()+tilemodel.tileRectList.size());
		}
		else if(editState == Util.RECT_RESIZE){
			update(Util.SHAPE_NAME[model.getEditState()], "����-�簢��", model.shapeList.size()+tilemodel.tileRectList.size());
		}
		else {
			update(Util.SHAPE_NAME[model.getEditState()], "�߰�", model.shapeList.size()+tilemodel.tileRectList.size());
		}
	}

}
